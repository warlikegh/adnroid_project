package com.zzz.technoparkmobileproject.ui.news;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zzz.technoparkmobileproject.SecretData;
import com.zzz.technoparkmobileproject.network.ApiRepo;
import com.zzz.technoparkmobileproject.network.NewsApi;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zzz.technoparkmobileproject.TechnoparkApplication.AUTH_TOKEN;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.CREATE_FIRST_SETTINGS;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.IS_FIRST_NEWS;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.SITE;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.TOKEN;


class NewsRepo {
    private final static MutableLiveData<UserNews> mNews = new MutableLiveData<>();
    private SharedPreferences mSettings;
    private final Context mContext;
    private NewsApi mNewsApi;
    private int key = 0;

    private final NewsDbManager.ReadAllListener<News> readListener = new NewsDbManager.ReadAllListener<News>() {
        @Override
        public void onReadAll(final Collection<News> allItems) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    postList(allItems);
                    if (allItems.isEmpty()) {
                        refresh();
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    };

    NewsRepo(Context context) {
        mContext = context;
        mNewsApi = ApiRepo.from(mContext).getNewsApi(new SecretData().getSecretData(mContext).getInt(SITE, 0));
    }

    public LiveData<UserNews> getNews() {
        return mNews;
    }

    public void refresh() {
        SharedPreferences mSettings;
        mSettings = mContext.getSharedPreferences(CREATE_FIRST_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(IS_FIRST_NEWS, false);
        editor.apply();

        final NewsDbManager manager = NewsDbManager.getInstance(mContext);
        mSettings = new SecretData().getSecretData(mContext);
        mNewsApi.getUserNews(TOKEN + mSettings.getString(AUTH_TOKEN, "")).enqueue(new Callback<NewsApi.UserNewsPlain>() {
            @Override
            public void onResponse(Call<NewsApi.UserNewsPlain> call,
                                   Response<NewsApi.UserNewsPlain> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NewsApi.UserNewsPlain result = response.body();
                    mNews.postValue(transform(result));
                    manager.clean();
                    savedata(transform(result));
                } else {
                    if (key == 0) {
                        manager.readAll(readListener);
                        key++;
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsApi.UserNewsPlain> call, Throwable t) {
                if (key == 0) {
                    manager.readAll(readListener);
                    key++;
                }
            }
        });
    }

    public void openNews(int pos) {
        NewsDbManager.getInstance(mContext).openNews(pos);
    }

    public void pullFromDB() {
        NewsDbManager manager = NewsDbManager.getInstance(mContext);
        manager.readAll(readListener);
    }


    public void setNextNews(String url) {
        mSettings = new SecretData().getSecretData(mContext);
        mNewsApi.getReUserNews(TOKEN + mSettings.getString(AUTH_TOKEN, ""), url).enqueue(new Callback<NewsApi.UserNewsPlain>() {
            @Override
            public void onResponse(Call<NewsApi.UserNewsPlain> call,
                                   Response<NewsApi.UserNewsPlain> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NewsApi.UserNewsPlain result = response.body();
                    savedata(transform(result));
                    pullFromDB();
                } else {

                }
            }

            @Override
            public void onFailure(Call<NewsApi.UserNewsPlain> call, Throwable t) {

            }
        });
    }

    private static UserNews transform(NewsApi.UserNewsPlain plains) {
        List<UserNews.Result> result = new ArrayList<>();
        for (NewsApi.UserNewsPlain.Result newsPlain : plains.results) {
            try {
                UserNews.Result news = map(newsPlain);
                result.add(news);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return new UserNews(plains.count, plains.next, plains.previous, result);
    }


    private static List<UserNews.Text> transformText(List<NewsApi.UserNewsPlain.Text> plains) {
        List<UserNews.Text> texts = new ArrayList<>();
        for (NewsApi.UserNewsPlain.Text textPlain : plains) {
            try {
                UserNews.Text text = mapText(textPlain);
                texts.add(text);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return texts;
    }

    private static List<UserNews.TextShort> transformTextShort(List<NewsApi.UserNewsPlain.TextShort> plains) {
        List<UserNews.TextShort> texts = new ArrayList<>();
        for (NewsApi.UserNewsPlain.TextShort textPlain : plains) {
            try {
                UserNews.TextShort text = mapTextShort(textPlain);
                texts.add(text);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return texts;
    }

    private static UserNews.Author transformAuthor(NewsApi.UserNewsPlain.Author plains) {
        UserNews.Author author = null;

        try {
            author = mapAuthor(plains);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return author;
    }


    private static UserNews.Result map(NewsApi.UserNewsPlain.Result resultPlain) throws ParseException {
        UserNews temp = new UserNews();
        UserNews.Author author = transformAuthor(resultPlain.author);
        List<UserNews.Text> text = transformText(resultPlain.text);
        List<UserNews.TextShort> textShort = transformTextShort(resultPlain.textShort);
        return temp.new Result(
                author,
                resultPlain.blog,
                resultPlain.title,
                resultPlain.rating,
                resultPlain.publishDate,
                text,
                resultPlain.commentsCount,
                resultPlain.id,
                textShort,
                resultPlain.url,
                false);
    }

    private static UserNews.Author mapAuthor(NewsApi.UserNewsPlain.Author authorPlain) throws ParseException {
        UserNews temp = new UserNews();
        return temp.new Author(
                authorPlain.fullname,
                authorPlain.avatarUrl,
                authorPlain.username,
                authorPlain.id
        );
    }

    private static UserNews.Text mapText(NewsApi.UserNewsPlain.Text textPlain) throws ParseException {
        UserNews temp = new UserNews();
        return temp.new Text(
                textPlain.type,
                textPlain.content
        );
    }

    private static UserNews.TextShort mapTextShort(NewsApi.UserNewsPlain.TextShort textPlain) throws ParseException {
        UserNews temp = new UserNews();
        return temp.new TextShort(
                textPlain.type,
                textPlain.content
        );
    }

    private void savedata(UserNews news) {
        UserNews temp = new UserNews();

        for (int i = 0; i < news.getResults().size(); i++) {
            UserNews.Result result = temp.new Result();
            result = news.getResults().get(i);
            NewsDbManager.getInstance(mContext).insert(
                    result.getId(),
                    result.getTitle(),
                    result.getBlog(),
                    result.getAuthor().getFullname(),
                    result.getAuthor().getId(),
                    result.getAuthor().getUsername(),
                    result.getAuthor().getAvatarUrl(),
                    result.getCommentsCount(),
                    result.getPublishDate(),
                    result.getRating(),
                    result.getText(),
                    result.getTextShort(),
                    result.getUrl(),
                    news.getNext(),
                    result.getOpen());
        }

    }

    private void postList(Collection<News> list) {
        List<News> data = new ArrayList();
        Comparator<News> comp = new Comparator<News>() {
            @Override
            public int compare(News a, News b) {
                if (b.id > a.id) {
                    return 1;
                } else if (b.id == a.id) {
                    return 0;
                } else {
                    return -1;
                }
            }
        };
        data.addAll(list);
        Collections.sort(data, comp);

        List<UserNews.Result> tempResult = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            UserNews temp = new UserNews();
            UserNews.Author tempAuthor = temp.new Author(data.get(i).authorName,
                    data.get(i).authorAva,
                    data.get(i).authorUsername,
                    data.get(i).authorId);
            tempResult.add(temp.new Result(tempAuthor, data.get(i).blog, data.get(i).title, data.get(i).rating,
                    data.get(i).publishDate, data.get(i).getText(), data.get(i).commentsCount, data.get(i).id,
                    data.get(i).getTextShort(), data.get(i).url, data.get(i).isOpen));
        }
        UserNews temp = new UserNews(null, data.get(data.size() - 1).next,
                null, tempResult);
        mNews.postValue(temp);
    }

}