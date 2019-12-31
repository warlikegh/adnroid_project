package com.example.technoparkmobileproject.group;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.technoparkmobileproject.R;
import com.example.technoparkmobileproject.Router;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupFragment extends Fragment {

    private UserGroup mGroup;
    private static GroupViewModel mGroupViewModel;
    static Context context;
    RecyclerView recycler;
    static GroupAdapter adapter;
    Integer id;
    SearchView searchView;
    String searchSave = "";

    public static GroupFragment newInstance(Integer id) {
        GroupFragment fragment = new GroupFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        mGroupViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(GroupViewModel.class);

    }

    @Override
    public void onStart() {
        super.onStart();
        id = getArguments().getInt("id");
        mGroupViewModel.refresh(id);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("search", searchSave);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        adapter = new GroupAdapter();
        searchView = view.findViewById(R.id.searchView);
        final TextView groupName = view.findViewById(R.id.group_name);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                searchSave = query;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                searchSave = newText;
                return true;
            }
        });
        if (savedInstanceState != null) {
            searchSave = savedInstanceState.getString("search");
        }
        searchView.setQuery(searchSave, true);


        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mGroupViewModel.refresh(id);
                pullToRefresh.setRefreshing(false);
            }
        });

        recycler = view.findViewById(R.id.students);

        recycler.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(linearLayoutManager);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                pullToRefresh.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        Observer<UserGroup> observer = new Observer<UserGroup>() {
            @Override
            public void onChanged(UserGroup group) {
                if (group != null) {
                    adapter.setGroup(group.getStudents());
                    adapter.filter(searchSave);
                    mGroup = group;
                    groupName.setText(group.getName());
                }
            }
        };

        mGroupViewModel
                .getNews()
                .observe(getViewLifecycleOwner(), observer);

        return view;
    }

    private class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {
        private List<UserGroup.Student> mStudents = new ArrayList<>();
        private List<UserGroup.Student> items = new ArrayList<>();

        @NonNull
        @Override
        public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new GroupViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.student_item, parent, false));
        }

        public void setGroup(List<UserGroup.Student> students) {
            mStudents = students;
            filter("");
            notifyDataSetChanged();
        }

        public void filter(String text) {
            items.clear();
            if (text.isEmpty()) {
                items.addAll(mStudents);
            } else {
                text = text.toLowerCase();
                for (UserGroup.Student item : mStudents) {
                    if (item.getFullname().toLowerCase().contains(text)) {
                        items.add(item);
                    }
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
            final UserGroup.Student student = items.get(position);
            Glide.with(Objects.requireNonNull(getContext()))
                    .load(student.getAvatarUrl())
                    .placeholder(R.mipmap.profile)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.mAva);
            holder.mFullName.setText(student.getFullname());
        }


        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mAva;
        protected TextView mFullName;

        public GroupViewHolder(@NonNull final View itemView) {
            super(itemView);
            mAva = itemView.findViewById(R.id.photo);
            mFullName = itemView.findViewById(R.id.full_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = GroupViewHolder.this.getAdapterPosition();
                    Integer id = adapter.items.get(pos).getId();
                    String username = adapter.items.get(pos).getUsername();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    ((Router) context).onProfileSelected(id, username);
                }
            });
        }
    }
}
