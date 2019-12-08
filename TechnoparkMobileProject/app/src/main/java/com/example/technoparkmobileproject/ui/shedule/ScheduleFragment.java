package com.example.technoparkmobileproject.ui.shedule;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technoparkmobileproject.R;

import java.util.ArrayList;
import java.util.List;


public class ScheduleFragment extends Fragment {

    private List<UserSchedule> mSchedule;
    private ScheduleViewModel mScheduleViewModel;
    private static FragmentManager fragmentManager = null;
    public static final String STATE = "change";

    boolean isSaveState;
    static Context context;

    private ScheduleViewModel scheduleViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        context = getContext();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recycler= view.findViewById(R.id.schedule);
        final MyAdapter adapter = new MyAdapter();
        recycler.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(linearLayoutManager);

        Observer<List<UserSchedule>> observer = new Observer<List<UserSchedule>>() {
            @Override
            public void onChanged(List<UserSchedule> schedule) {
                if (schedule != null) {
                    adapter.setSchedule(schedule);
                    mSchedule = schedule;
                }
            }
        };

        mScheduleViewModel = new ViewModelProvider(getActivity())
                .get(ScheduleViewModel.class);
        mScheduleViewModel
                .getSchedule()
                .observe(getViewLifecycleOwner(), observer);
        mScheduleViewModel.refresh();

    }





    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<UserSchedule> mSchedule = new ArrayList<>();
        //private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.US);

        public void setSchedule(List<UserSchedule> schedule) {
            mSchedule = schedule;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.schedule_filter, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            final ScheduleAdapter scheduleAdapter = new ScheduleAdapter();
            scheduleAdapter.setSchedule(mSchedule);
            holder.mSchedule.setAdapter(scheduleAdapter);
            holder.mSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        @Override
        public int getItemCount() {
            return 1;
        }

    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        protected RecyclerView mSchedule;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mSchedule = itemView.findViewById(R.id.schedule);
        }
    }








    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleViewHolder> {

        private List<UserSchedule> mSchedule = new ArrayList<>();
        //private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.US);

        public void setSchedule(List<UserSchedule> schedule) {
            mSchedule = schedule;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ScheduleViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.schedule_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
            final UserSchedule schedule = mSchedule.get(position);
            holder.mDateSchedule.setText(schedule.getDate());
            holder.mDiscipline.setText(schedule.getDiscipline());
            holder.mShortTitle.setText(schedule.getShortTitle());
            holder.mLocation.setText(schedule.getLocation());
            holder.mTime.setText(schedule.getStartTime());
            holder.mTitle.setText(schedule.getTitle());
            holder.mDiscipline.setText(schedule.getDiscipline());

            final GroupAdapter groupAdapter = new GroupAdapter();
            groupAdapter.setGroup(schedule.getGroups());
            holder.mGroups.setAdapter(groupAdapter);
            holder.mGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        @Override
        public int getItemCount() {
            return mSchedule.size();
        }

    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {

        protected TextView mDateSchedule;
        protected TextView mDiscipline;
        protected RecyclerView mGroups;
        protected TextView mShortTitle;
        protected TextView mLocation;
        protected TextView mTime;
        protected TextView mTitle;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            mDateSchedule = itemView.findViewById(R.id.date_schedule);
            mDiscipline = itemView.findViewById(R.id.discipline);
            mGroups = itemView.findViewById(R.id.groups);
            mShortTitle = itemView.findViewById(R.id.short_title);
            mLocation = itemView.findViewById(R.id.location);
            mTime = itemView.findViewById(R.id.time);
            mTitle = itemView.findViewById(R.id.title);
        }
    }



    private class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {

        private List<UserSchedule.Group> mGroup = new ArrayList<>();

        public void setGroup(List<UserSchedule.Group> group) {
            mGroup = group;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new GroupViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.schedule_group, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
            final UserSchedule.Group group = mGroup.get(position);
            holder.mGroup.setText(group.getName());
        }

        @Override
        public int getItemCount() {
            return mGroup.size();
        }

    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {

        protected TextView mGroup;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            mGroup = itemView.findViewById(R.id.group);
        }
    }

}