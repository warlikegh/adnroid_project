package com.example.technoparkmobileproject.ui.news;

import androidx.fragment.app.Fragment;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technoparkmobileproject.R;

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment {

    private UserNews mNews;
    private static NewsAdapter adapter;
    private NewsViewModel mNewsViewModel;
    private static FragmentManager fragmentManager = null;
    public static final String STATE = "change";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        fragmentManager=getFragmentManager();
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
        adapter  = new NewsAdapter();
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        Observer<UserNews> observer = new Observer<UserNews>() {
            @Override
            public void onChanged(UserNews news) {
                if (news != null) {
                    adapter.setNews(news.getResults());
                    mNews = news;
                }
            }
        };
        mNewsViewModel = new ViewModelProvider(getActivity())
                .get(NewsViewModel.class);
        mNewsViewModel
                .getNews()
                .observe(getViewLifecycleOwner(), observer);
        mNewsViewModel.refresh();

    }
    /*
     public interface OnItemSelectedListener {
   public void onItemSelected(UserNews.Result result);
    }*/


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
            final UserNews.Result news = mNews.get(position);
            holder.mTitle.setText(news.getTitle());
            holder.mBlog.setText(news.getBlog());
            holder.mAuthor.setText(news.getAuthor().getFullname());
            holder.mDate.setText(news.getPublishDate());
            holder.mRating.setText(news.getRating().toString());

            if (news.getText().get(0).getType().equals("p")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.mContent.setText(Html.fromHtml(news.getText().get(0).getContent(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.mContent.setText(Html.fromHtml(news.getText().get(0).getContent()));
                }
                holder.mContent.setMovementMethod(LinkMovementMethod.getInstance());
            }

            holder.mCommentsCount.setText(news.getCommentsCount().toString());
            if (news.getText().size()>1){
            holder.mNext.setText("Читать дальше...");
            holder.mNext.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
            holder.mNext.setTextSize(17);
            }

        }

        @Override
        public int getItemCount() {
            return mNews.size();
        }
    }

        static class NewsViewHolder extends RecyclerView.ViewHolder {

            protected TextView mTitle;
            protected TextView mBlog;
            protected TextView mContent;
            protected TextView mAuthor;
            protected TextView mDate;
            protected TextView mRating;
            protected TextView mCommentsCount;
            private final TextView mNext;

            public NewsViewHolder(@NonNull View itemView) {
                super(itemView);
                mTitle = itemView.findViewById(R.id.title);
                mBlog = itemView.findViewById(R.id.blog);
                mContent = itemView.findViewById(R.id.content);
                mAuthor = itemView.findViewById(R.id.author);
                mDate = itemView.findViewById(R.id.date_news_author);
                mRating = itemView.findViewById(R.id.rating_news_author);
                mCommentsCount = itemView.findViewById(R.id.comments_news);
                mNext = itemView.findViewById(R.id.next);

                mNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = NewsViewHolder.this.getAdapterPosition();
                        UserNews.Result myData = adapter.mNews.get(pos);

                        //((OnItemSelectedListener)context).onItemSelected(myData);
                                fragmentManager
                                .beginTransaction()
                                .replace(R.id.nav_host_fragment,  ArticleFragment.newInstance(myData))
                                .addToBackStack(null)
                                .commit();
                       /* NavController a =new NavController()
                        NavigationUI.setupActionBarWithNavController(action_articleFragment2_to_navigation_home);*/
                    }
                });
            }
        }
    private UserNews restoreState(Bundle savedInstanceState) {
        UserNews news = new UserNews();
        if (savedInstanceState != null) {

        }
        return news;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       /* outState.putStringArrayList("title",result.getTitle());
        outState.putStringArrayList("author",result.getAuthor().getFullname());
        outState.putIntegerArrayList("commentsCount",result.getCommentsCount());
        outState.putStringArrayList("blog",result.getBlog());
        outState.putStringArrayList("date",result.getPublishDate());
        outState.putDoubleArray("rating",result.getRating());
        ArrayList<String> text=new ArrayList<>();
        for (int i = 0; i < result.getText().size(); i++){
            text.add(result.getText().get(i).getContent());
        }
        ArrayList<String> type=new ArrayList<>();
        for (int i = 0; i < result.getText().size(); i++){
            type.add(result.getText().get(i).getType());
        }
        outState.put("content",text);
        outState.putStringArrayList("type",type);*/
    }
}
