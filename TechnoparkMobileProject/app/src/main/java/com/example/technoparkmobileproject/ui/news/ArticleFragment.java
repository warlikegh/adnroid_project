package com.example.technoparkmobileproject.ui.news;

import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technoparkmobileproject.R;

import java.util.ArrayList;
import java.util.List;


public class ArticleFragment extends Fragment {
    protected RecyclerView mContent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_article, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mContent = view.findViewById(R.id.content);

        Bundle arguments = getArguments();
        ArrayList<String> text=new ArrayList<>();
        ArrayList<String> type=new ArrayList<>();
        String title="";
        String blog="";
        String date="";
        String author="";
        Integer сommentsCount=0;
        Double rating=0.;

        if (arguments != null) {

            title=arguments.getString("title");
            blog=arguments.getString("blog");
            author=arguments.getString("author");
            date=arguments.getString("date");
            rating = arguments.getDouble("rating");
            сommentsCount = arguments.getInt("commentsCount");
            text = arguments.getStringArrayList("content");
            type = arguments.getStringArrayList("type");
        }
        final PageAdapter adapter = new PageAdapter();
        adapter.setContent(text, type, title, blog, author, date, сommentsCount, rating);
        mContent.setAdapter(adapter);
        mContent.setLayoutManager(new LinearLayoutManager(getContext()));

    }


    private class ContentAdapter extends RecyclerView.Adapter<ContentViewHolder> {

        private List<String> mText = new ArrayList<>();
        private List<String> mType = new ArrayList<>();

        public void setContent(List<String> text, List<String> type) {
            mText = text;
            mType = type;
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
            final String text = mText.get(position);
            final String type = mType.get(position);
            if (type.equals("p")||type.equals("ul")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.mTextNews.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.mTextNews.setText(Html.fromHtml(text));
                }
                holder.mTextNews.setMovementMethod(LinkMovementMethod.getInstance());
            } else
            if (type.equals("img")){
                holder.mTextNews.setText(text);
            } else {
            }
        }

        @Override
        public int getItemCount() {
            return mText.size();
        }
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {

        protected TextView mTextNews;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextNews = itemView.findViewById(R.id.text_news);
        }
    }




    public static ArticleFragment newInstance(UserNews.Result result) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle bundle =new Bundle();
        bundle.putString("title",result.getTitle());
        bundle.putString("author",result.getAuthor().getFullname());
        bundle.putInt("commentsCount",result.getCommentsCount());
        bundle.putString("blog",result.getBlog());
        bundle.putString("date",result.getPublishDate());
        bundle.putDouble("rating",result.getRating());
        ArrayList<String> text=new ArrayList<>();
        for (int i = 0; i < result.getText().size(); i++){
            text.add(result.getText().get(i).getContent());
        }
        ArrayList<String> type=new ArrayList<>();
        for (int i = 0; i < result.getText().size(); i++){
            type.add(result.getText().get(i).getType());
        }
        bundle.putStringArrayList("content",text);
        bundle.putStringArrayList("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    private class PageAdapter extends RecyclerView.Adapter<PageViewHolder> {

        private List<String> mText = new ArrayList<>();
        private List<String> mType = new ArrayList<>();
        private String mTitle;
        private String mBlog;
        private String mDate;
        private String mAuthor;
        private Integer mCommentsCount;
        private Double mRating;

        public void setContent(List<String> text, List<String> type, String title, String blog,
                               String author, String date, Integer count, Double rating) {
            mText = text;
            mType = type;
            mTitle=title;
            mBlog= blog;
            mAuthor = author;
            mDate=date;
            mCommentsCount=count;
            mRating=rating;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PageViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_article, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
            holder.mTitle.setText(mTitle);
            holder.mBlog.setText(mBlog);
            holder.mAuthor.setText(mAuthor);
            holder.mDate.setText(mDate);
            holder.mRating.setText(mRating.toString());
            holder.mCommentsCount.setText(mCommentsCount.toString());

            final ContentAdapter adapter = new ContentAdapter();
            adapter.setContent(mText, mType);
            holder.mTextNews.setAdapter(adapter);
            holder.mTextNews.setLayoutManager(new LinearLayoutManager(getContext()));

        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

    static class PageViewHolder extends RecyclerView.ViewHolder {

        protected RecyclerView mTextNews;
        protected TextView mTitle;
        protected TextView mBlog;
        protected TextView mAuthor;
        protected TextView mDate;
        protected TextView mCommentsCount;
        protected TextView mRating;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextNews = itemView.findViewById(R.id.content);
            mTitle = itemView.findViewById(R.id.title);
            mBlog = itemView.findViewById(R.id.blog);
            mAuthor = itemView.findViewById(R.id.author);
            mDate = itemView.findViewById(R.id.date_news_author);
            mRating = itemView.findViewById(R.id.rating_news_author);
            mCommentsCount = itemView.findViewById(R.id.comments_news);
        }
    }


}
