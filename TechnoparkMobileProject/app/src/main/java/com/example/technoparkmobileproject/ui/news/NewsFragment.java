package com.example.technoparkmobileproject.ui.news;

import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technoparkmobileproject.R;

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment {

    private NewsViewModel mNewsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recycler = view.findViewById(R.id.news);
        final NewsAdapter adapter = new NewsAdapter();
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        Observer<List<UserNews.Result>> observer = new Observer<List<UserNews.Result>>() {
            @Override
            public void onChanged(List<UserNews.Result> news) {
                if (news != null) {
                    adapter.setNews(news);
                }
            }
        };
        mNewsViewModel = new ViewModelProvider(getActivity())
                .get(NewsViewModel.class);
        mNewsViewModel
                .getLessons()
                .observe(getViewLifecycleOwner(), observer);
        mNewsViewModel.refresh();
    }

    private class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

        private List<UserNews.Result> mNews = new ArrayList<>();
        //private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.US);

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

        @Override
        public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
            final UserNews.Result lesson = mNews.get(position);
            holder.mTitle.setText(lesson.getTitle());
            holder.mBlog.setText(lesson.getBlog());
            holder.mAuthor.setText(lesson.getAuthor().getFullname());
            holder.mDate.setText(lesson.getPublishDate());
            holder.mRating.setText(lesson.getRating().toString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.mContent1.setText(Html.fromHtml(lesson.getText().get(0).getContent(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.mContent1.setText(Html.fromHtml(lesson.getText().get(0).getContent()));
            }
            holder.mCommentsCount.setText(lesson.getCommentsCount().toString());
            //content
        }

        @Override
        public int getItemCount() {
            return mNews.size();
        }
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        protected TextView mTitle;
        protected TextView mBlog;
        protected RecyclerView mContent;
        protected TextView mAuthor;
        protected TextView mDate;
        protected TextView mRating;
        protected TextView mContent1;
        protected TextView mCommentsCount;
       

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mBlog = itemView.findViewById(R.id.blog);
            mContent = itemView.findViewById(R.id.content);
            mContent1 = itemView.findViewById(R.id.content1);
            mAuthor = itemView.findViewById(R.id.author);
            mDate = itemView.findViewById(R.id.date_news_author);
            mRating = itemView.findViewById(R.id.rating_news_author);
            mCommentsCount = itemView.findViewById(R.id.comments_news);
            //bindViews(itemView);
        }
/*
        // This get called in PrimaryAdapter onBindViewHolder method
        public void bindViews(@NonNull View itemView) {
            RecyclerView recycler = itemView.findViewById(R.id.content);
            final ContentAdapter adapter = new ContentAdapter();
            recycler.setLayoutManager(new LinearLayoutManager(getContext()));
            mContent.setAdapter(adapter);
            Observer<List<UserNews.Result>> observer = new Observer<List<UserNews.Text>>() {
                @Override
                public void onChanged(List<UserNews.Text> text) {
                    if (text != null) {
                        adapter.setContent(text);
                    }
                }
            };
        }
    }

    private class ContentViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public ContentViewHolder(View view) {
            super(view);
            mTextView = (TextView) itemView.findViewById(R.id.text_news);
        }

        public void bindView(UserNews.Text name) {
            mTextView.setText(name.getContent());
        }
    }

    private class ContentAdapter extends RecyclerView.Adapter<ContentViewHolder> {
        private List<UserNews.Text> mText= new ArrayList<>();
        public void setContent(List<UserNews.Result> news) {
            mNews = news;
            notifyDataSetChanged();
        }



        @Override
        public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.news_text_item, parent, false);
            return new ContentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ContentViewHolder holder, int position) {
            final UserNews.Text lesson = mMovies.get(position);
            holder.mTextView.setText(lesson.getContent());
        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }


    private ContentAdapter getContentAdapter(int position) {

        ContentAdapter adapter;

                return new ContentAdapter(mMovies);


    }*/
}
}