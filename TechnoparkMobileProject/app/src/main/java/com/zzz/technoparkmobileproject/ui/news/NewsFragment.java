package com.zzz.technoparkmobileproject.ui.news;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zzz.technoparkmobileproject.R;
import com.zzz.technoparkmobileproject.Router;
import com.zzz.technoparkmobileproject.SecretData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.zzz.technoparkmobileproject.TechnoparkApplication.CREATE_FIRST_SETTINGS;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.IS_FIRST_NEWS;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.NEWS_BLOCKQOTE;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.NEWS_CODE;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.NEWS_H4;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.NEWS_H5;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.NEWS_H6;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.NEWS_IFRAME;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.NEWS_IMG;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.NEWS_O1;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.NEWS_P;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.NEWS_PATH_URL;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.NEWS_POS;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.NEWS_PRE;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.NEWS_U1;


public class NewsFragment extends Fragment {

    private UserNews mNews;
    static UserNews mExtraNews;
    private static NewsAdapter adapter;
    private static NewsViewModel mNewsViewModel;
    public static String mUrl = "";
    RecyclerView recycler;
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
        recycler.setLayoutManager(linearLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (mNews.getNext() != null) {
                    mUrl = NEWS_PATH_URL + "?" + mNews.getNext().substring(mNews.getNext().indexOf("?") + 1);
                    loadNextDataFromApi(mUrl);
                }
            }
        };

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorAccent, null), getResources().getColor(R.color.colorBlueBackgroungIS, null),
                getResources().getColor(R.color.colorGradientBottomAuth, null), getResources().getColor(R.color.colorGradientTopAuth, null));
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNewsViewModel.refresh();
                pullToRefresh.setRefreshing(false);
            }
        });

        recycler.addOnScrollListener(scrollListener);


        Observer<UserNews> observer = new Observer<UserNews>() {
            @Override
            public void onChanged(UserNews news) {
                if (news != null) {
                    adapter.setNews(news.getResults());
                    mNews = news;
                    mExtraNews = mNews;
                }
            }
        };

        mNewsViewModel
                .getNews()
                .observe(getViewLifecycleOwner(), observer);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getContext();
        mNewsViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(NewsViewModel.class);

        mSettings = Objects.requireNonNull(getContext()).getSharedPreferences(CREATE_FIRST_SETTINGS, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
        if (mSettings.getBoolean(IS_FIRST_NEWS, true)) {
            mNewsViewModel.refresh();
        } else {
            mNewsViewModel.pullFromDB();
            positionSave = mSettings.getInt(NEWS_POS, 0);
        }
        if (savedInstanceState != null) {
            mNewsViewModel.pullFromDB();
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.news_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
            holder.mTitle.setText(news.getTitle());
            holder.mBlog.setText(news.getBlog());
            holder.mAuthor.setText(news.getAuthor().getFullname());

            if (news.getAuthor().getFullname().length() < 25)
                holder.mAuthor.setPadding(0,12,0,0);


            String date = new SecretData().getDateString(news.getPublishDate());
            holder.mDate.setText(date);
            String rating = "";
            if (news.getRating() > 0)
                rating = "+";
            rating += ((Integer) news.getRating().intValue()).toString();
            holder.mRating.setText(rating);

            boolean buttonIsActive = false;
            holder.mCommentsCount.setText(news.getCommentsCount().toString());
            if ((news.getText().size() > 1 ||
                    (!(news.getText().get(0).getContent().equals(news.getTextShort().get(0).getContent())))) && !news.getOpen())
            {
                holder.mNext.setVisibility(View.VISIBLE);
                holder.mNext.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                buttonIsActive = true;
            }
            if (!buttonIsActive)
                holder.mNext.setVisibility(View.GONE);

            final ContentAdapter contentAdapter = new ContentAdapter();
            List<UserNews.TextShort> textShort = new ArrayList<UserNews.TextShort>();
            textShort.add(news.getTextShort().get(0));
            contentAdapter.setContent(news.getText(), textShort, buttonIsActive);
            holder.mContent.setAdapter(contentAdapter);
            holder.mContent.setLayoutManager(new LinearLayoutManager(getContext()));

            Glide.with(Objects.requireNonNull(getContext()))
                    .load(news.getAuthor().getAvatarUrl())
                    .placeholder(R.mipmap.profile)
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

    class NewsViewHolder extends RecyclerView.ViewHolder {
        protected TextView mTitle;
        protected TextView mBlog;
        protected RecyclerView mContent;
        protected TextView mAuthor;
        protected TextView mDate;
        protected TextView mRating;
        protected TextView mCommentsCount;
        private final TextView mNext;
        protected ImageView mAvatar;
        protected RelativeLayout mTitleLayout;
        protected RelativeLayout mAuthorLayout;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mTitleLayout = itemView.findViewById(R.id.title_layout);
            mTitleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = NewsViewHolder.this.getAdapterPosition();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mNews.getResults().get(pos).getUrl()));
                    startActivity(browserIntent);
                }});
            mBlog = itemView.findViewById(R.id.blog);
            mContent = itemView.findViewById(R.id.content);
            mAuthor = itemView.findViewById(R.id.author);
            mAuthorLayout = itemView.findViewById(R.id.author_layout);

            mAuthorLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = NewsViewHolder.this.getAdapterPosition();
                    String username = mExtraNews.getResults().get(pos).getAuthor().getUsername();
                    int id = mExtraNews.getResults().get(pos).getAuthor().getId();
                    ((Router) context).onProfileSelected(id, username);
                }
            });

            mDate = itemView.findViewById(R.id.date_news_author);
            mRating = itemView.findViewById(R.id.rating_news_author);
            mCommentsCount = itemView.findViewById(R.id.comments_news);
            mAvatar = itemView.findViewById(R.id.photo);
            mNext = itemView.findViewById(R.id.next);

            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNext.setVisibility(View.GONE);
                    int pos = NewsViewHolder.this.getAdapterPosition();
                    adapter.setDisactive(NewsViewHolder.this, pos);
                    mNews.getResults().get(pos).setOpen(true);
                    mNewsViewModel.openNews(mNews.getResults().get(pos).getId());
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
            SecretData secretData = new SecretData();
            if (isActive) {
                text = mTextShort.get(position).getContent();
                type = mTextShort.get(position).getType();
            } else {
                text = mText.get(position).getContent();
                type = mText.get(position).getType();
            }
            if (type.equals(NEWS_P) || type.equals(NEWS_H4) || type.equals(NEWS_H5) || type.equals(NEWS_H6) || type.equals(NEWS_O1)
                    || type.equals(NEWS_CODE) || type.equals(NEWS_BLOCKQOTE) || type.equals(NEWS_PRE) || type.equals(NEWS_U1) || (type.equals(NEWS_IFRAME) && text.contains("https://www.youtube.com/"))) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.mTextNews.setText(Html.fromHtml(secretData.deleteEnter(text), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.mTextNews.setText(Html.fromHtml(secretData.deleteEnter(text)));
                }
                holder.mTextNews.setMovementMethod(LinkMovementMethod.getInstance());
            } else if (type.equals(NEWS_IMG)) {
                Glide.with(Objects.requireNonNull(getContext()))
                        .load(text)
                        .placeholder(R.drawable.ic_restore_black_24dp)
                        .into(holder.mImageNews);
                holder.mTextNews.setVisibility(View.GONE);
            } else {
                holder.mTextNews.setText("Here must be " + type);
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
