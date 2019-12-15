package com.example.technoparkmobileproject.ui.news;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.technoparkmobileproject.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class NewsFragment extends Fragment {

    private UserNews mNews;
    private static NewsAdapter adapter;
    private NewsViewModel mNewsViewModel;
    private static FragmentManager fragmentManager = null;
    public static final String STATE = "change";
    public static String mUrl = "";
    public static String BASE_URL = "topics/subscribed/";
    RecyclerView recycler;
    boolean isSaveState;
    static Context context;
    Integer positionSave = 0;
    SharedPreferences mSettings;
    SharedPreferences.Editor mEditor;

    private EndlessRecyclerViewScrollListener scrollListener;

    public NewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recycler = view.findViewById(R.id.news);
        adapter = new NewsAdapter();
        recycler.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.scrollToPositionWithOffset(positionSave, 0);
        recycler.setLayoutManager(linearLayoutManager);


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (mNews.getNext() != null) {
                    mUrl = BASE_URL + "?" + mNews.getNext().substring(mNews.getNext().indexOf("?") + 1);
                    loadNextDataFromApi(mUrl);
                }
            }
        };

        recycler.addOnScrollListener(scrollListener);


        Observer<UserNews> observer = new Observer<UserNews>() {
            @Override
            public void onChanged(UserNews news) {
                if (news != null) {
                    adapter.setNews(news.getResults());
                    mNews = news;
                }
            }
        };

        Observer<UserNews> observer_next = new Observer<UserNews>() {
            @Override
            public void onChanged(UserNews news) {
                if (news != null) {
                    List<UserNews.Result> tempResult = new ArrayList<>();
                    if (mNews != null)
                        tempResult.addAll(mNews.getResults());
                    tempResult.addAll(news.getResults());
                    if (mNews != null) {
                        UserNews temp = new UserNews(news.getCount(), news.getNext(), news.getPrevious(), tempResult);
                        mNews = temp;
                    } else {
                        mNews = news;
                    }
                    adapter.setNews(mNews.getResults());
                }
            }
        };

        mNewsViewModel
                .getNews()
                .observe(getViewLifecycleOwner(), observer);
        mNewsViewModel
                .getNextNews()
                .observe(getViewLifecycleOwner(), observer_next);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        fragmentManager = getFragmentManager();
        context = getContext();
        mNewsViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(NewsViewModel.class);

        mSettings = Objects.requireNonNull(getContext()).getSharedPreferences("createFirst", Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
        if (mSettings.getBoolean("isFirstNews", true)) {
            mNewsViewModel.refresh();
        } else {
            mNewsViewModel.pullFromDB();
            positionSave = mSettings.getInt("pos", 0);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.news_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            mNewsViewModel.refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean restoreState(Bundle savedInstanceState) {
        return savedInstanceState != null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt("pos", positionSave);
        mEditor.putInt("pos", positionSave).commit();
    }

    private void loadNextDataFromApi(String url) {
        final Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mNewsViewModel.setNextNews(mUrl);
            }
        });
    }

    public interface OnItemSelectedListener {
        public void onItemSelected(int id);
    }


    private class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

        private List<UserNews.Result> mNews = new ArrayList<>();

        public void setNews(List<UserNews.Result> news) {
            mNews = news;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new NewsViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_item, parent, false));
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
            positionSave = position;
            final UserNews.Result news = mNews.get(position);
            String url = "<a href=\"" + news.getUrl() + "\" target=\"_blank\">" + news.getTitle() + "</a>";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.mTitle.setText(Html.fromHtml(url, Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.mTitle.setText(Html.fromHtml(url));
            }
            holder.mTitle.setMovementMethod(LinkMovementMethod.getInstance());
            holder.mTitle.setTextColor(getResources().getColor(android.R.color.black));                                   //       overthink
            holder.mBlog.setText(news.getBlog());
            holder.mAuthor.setText(news.getAuthor().getFullname());

            String data = news.getPublishDate().replace("T", " ").replace("Z", "");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.US);
            LocalDateTime localDateTime = LocalDateTime.parse(data, formatter);

            holder.mDate.setText(localDateTime.format(formatter));
            holder.mRating.setText(news.getRating().toString());

            boolean buttonIsActive = false;
            holder.mCommentsCount.setText(news.getCommentsCount().toString());
            if (news.getText().size() > 1 ||
                    (!(news.getText().get(0).getContent().equals(news.getTextShort().get(0).getContent())))) {
                holder.mNext.setText("Показать полностью...");
                holder.mNext.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                holder.mNext.setTextSize(17);
                buttonIsActive = true;
            }

            final ContentAdapter contentAdapter = new ContentAdapter();
            List<UserNews.TextShort> textShort = new ArrayList<UserNews.TextShort>();
            textShort.add(news.getTextShort().get(0));
            contentAdapter.setContent(news.getText(), textShort, buttonIsActive);
            holder.mContent.setAdapter(contentAdapter);
            holder.mContent.setLayoutManager(new LinearLayoutManager(getContext()));

            Glide.with(Objects.requireNonNull(getContext()))
                    .load(news.getAuthor().getAvatarUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.mAvatar);
        }

        @Override
        public int getItemCount() {
            return mNews.size();
        }

        public void setDisactive(@NonNull NewsViewHolder holder, int pos) {
            final UserNews.Result news = mNews.get(pos);
            final ContentAdapter contentAdapter = new ContentAdapter();
            contentAdapter.setContent(news.getText(), news.getTextShort(), false);
            holder.mContent.setAdapter(contentAdapter);
            holder.mContent.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        protected TextView mTitle;
        protected TextView mBlog;
        protected RecyclerView mContent;
        protected TextView mAuthor;
        protected TextView mDate;
        protected TextView mRating;
        protected TextView mCommentsCount;
        private final TextView mNext;
        protected ImageView mAvatar;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mBlog = itemView.findViewById(R.id.blog);
            mContent = itemView.findViewById(R.id.content);
            mAuthor = itemView.findViewById(R.id.author);
            mDate = itemView.findViewById(R.id.date_news_author);
            mRating = itemView.findViewById(R.id.rating_news_author);
            mCommentsCount = itemView.findViewById(R.id.comments_news);
            mAvatar = itemView.findViewById(R.id.photo);
            mNext = itemView.findViewById(R.id.next);

            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNext.setTextSize(0);
                    int pos = NewsViewHolder.this.getAdapterPosition();
                    adapter.setDisactive(NewsViewHolder.this, pos);
                }

            });
        }
    }


    private class ContentAdapter extends RecyclerView.Adapter<ContentViewHolder> {

        private List<UserNews.Text> mText = new ArrayList<>();
        private List<UserNews.TextShort> mTextShort = new ArrayList<>();
        private boolean isActive;

        public void setContent(List<UserNews.Text> text, List<UserNews.TextShort> textShort, boolean buttonisActive) {
            mText = text;
            mTextShort = textShort;
            isActive = buttonisActive;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContentViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_text_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
            final String text;
            final String type;
            if (isActive) {
                text = mTextShort.get(position).getContent();
                type = mTextShort.get(position).getType();
            } else {
                text = mText.get(position).getContent();
                type = mText.get(position).getType();
            }
            if (type.equals("p") || type.equals("ul") || type.equals("code")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.mTextNews.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.mTextNews.setText(Html.fromHtml(text));
                }
                holder.mTextNews.setMovementMethod(LinkMovementMethod.getInstance());
            } else if (type.equals("img")) {
                Glide.with(Objects.requireNonNull(getContext()))
                        .load(text)
/*rewrite*/.placeholder(R.drawable.ic_launcher_foreground)
                        .into(holder.mImageNews);
                holder.mTextNews.setEnabled(true);
            } else {
                holder.mTextNews.setText(type);
            }
        }

        @Override
        public int getItemCount() {
            if (isActive)
                return mTextShort.size();
            else return mText.size();
        }
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {

        protected TextView mTextNews;
        protected ImageView mImageNews;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextNews = itemView.findViewById(R.id.text_news);
            mImageNews = itemView.findViewById(R.id.photo);
        }
    }
}
