package com.example.technoparkmobileproject.ui.news;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.technoparkmobileproject.R;
import com.example.technoparkmobileproject.Router;
import com.example.technoparkmobileproject.SecretData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class NewsFragment extends Fragment {

    private UserNews mNews;
    static UserNews mExtraNews;
    private static NewsAdapter adapter;
    private static NewsViewModel mNewsViewModel;
    public static String mUrl = "";
    public static String BASE_URL = "topics/subscribed/";
    RecyclerView recycler;
    static Context context;
    Integer positionSave = 0;
    SharedPreferences mSettings;
    SharedPreferences.Editor mEditor;

    private EndlessRecyclerViewScrollListener scrollListener;

    public NewsFragment() {
    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
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
                    mUrl = BASE_URL + "?" + mNews.getNext().substring(mNews.getNext().indexOf("?") + 1);
                    loadNextDataFromApi(mUrl);
                }
            }
        };

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
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
                        mExtraNews = mNews;
                    } else {
                        mNews = news;
                        mExtraNews = mNews;
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
        context = getContext();
        mNewsViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(NewsViewModel.class);

        mSettings = Objects.requireNonNull(getContext()).getSharedPreferences("createFirst", Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
        if (mSettings.getBoolean("isFirstNews", true)) {
            mNewsViewModel.refresh();
        } else {
            mNewsViewModel.pullFromDB();
            positionSave = mSettings.getInt("pos_news", 0);
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
            String url = "<a href=\"" + news.getUrl() + "\" target=\"_blank\">" + news.getTitle() + "</a>";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.mTitle.setText(Html.fromHtml(url, Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.mTitle.setText(Html.fromHtml(url));
            }

            holder.mTitle.setMovementMethod(LinkMovementMethod.getInstance());
            holder.mBlog.setText(news.getBlog());
            holder.mAuthor.setText(news.getAuthor().getFullname());

            String date = new SecretData().getDateString(news.getPublishDate());
            holder.mDate.setText(date);
            String rating = "";
            if (news.getRating()>0)
                rating = "+";
            rating += ((Integer)news.getRating().intValue()).toString();
            holder.mRating.setText(rating);

            boolean buttonIsActive = false;
            holder.mCommentsCount.setText(news.getCommentsCount().toString());
            if (news.getText().size() > 1 ||
                    (!(news.getText().get(0).getContent().equals(news.getTextShort().get(0).getContent())))) {
                holder.mNext.setVisibility(View.VISIBLE);
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

            mAuthor.setOnClickListener(new View.OnClickListener() {
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
            if (type.equals("p") || type.equals("ul") || type.equals("code") || type.equals("ol") || type.equals("blockquote")
                    || type.equals("h4") || type.equals("h5") || type.equals("h6") || type.equals("pre")/* || type.equals("iframe")*/) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.mTextNews.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.mTextNews.setText(Html.fromHtml(text));
                }
                holder.mTextNews.setMovementMethod(LinkMovementMethod.getInstance());
            } else if (type.equals("img")) {
                Glide.with(Objects.requireNonNull(getContext()))
                        .load(text)
                        .placeholder(R.drawable.ic_restore_black_24dp)
                        .into(holder.mImageNews);
                holder.mTextNews.setEnabled(true);
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
