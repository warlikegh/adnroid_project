package com.example.technoparkmobileproject.ui.shedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.technoparkmobileproject.R;
import com.example.technoparkmobileproject.SecretData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ScheduleFragment extends Fragment {

    private List<UserSchedule> mSchedule;
    private ScheduleViewModel mScheduleViewModel;
    private static FragmentManager fragmentManager = null;
    public static final String STATE = "change";
    boolean isSaveState;
    static Context context;
    RecyclerView recycler;
    final MyAdapter adapter = new MyAdapter();
    Integer positionSave = 0;
    SharedPreferences mSettings;
    SharedPreferences.Editor mEditor;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        context = getContext();
        mScheduleViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(ScheduleViewModel.class);

        mSettings = Objects.requireNonNull(getContext()).getSharedPreferences("createFirst", Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
        if (!mSettings.getBoolean("isFirstSchedule", true)) {
            positionSave = mSettings.getInt("pos", 0);
        }
        mScheduleViewModel.pullFromDB();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_all, container, false);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mScheduleViewModel.refresh();
                pullToRefresh.setRefreshing(false);
            }
        });

        final String discipline;
        if (savedInstanceState == null) {
            discipline = null;
        } else {
            discipline = savedInstanceState.getString("discipline");
            Log.e("saveCreate", discipline);
        }
        recycler = view.findViewById(R.id.schedule);
        recycler.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(linearLayoutManager);


        Observer<List<UserSchedule>> observer = new Observer<List<UserSchedule>>() {
            @Override
            public void onChanged(List<UserSchedule> schedule) {
                if (schedule != null) {
                    adapter.setSchedule(schedule, discipline);
                    if (discipline != null) {
                        Log.e("saveObserver", discipline);
                    }
                    mSchedule = schedule;
                }
            }
        };

        mScheduleViewModel
                .getSchedule()
                .observe(getViewLifecycleOwner(), observer);
        return view;
    }

    public void setSaveState() {
        mEditor.putInt("pos", positionSave).commit();
        mEditor.putString("discipline", adapter.disciplineText).commit();
        Log.e("saveState", adapter.disciplineText + " " + positionSave.toString());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("discipline", adapter.disciplineText);
        setSaveState();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setSaveState();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<UserSchedule> mSchedule = new ArrayList<>();
        private List<UserSchedule> tempSchedule = new ArrayList<>();
        final ScheduleAdapter scheduleAdapter = new ScheduleAdapter();
        String ALL_DISCIPLINES = "Все дисциплины";
        String disciplineText = ALL_DISCIPLINES;
        final Boolean[] isDefault = {true};

        public void setSchedule(List<UserSchedule> schedule, String discipline) {
            mSchedule = schedule;
            disciplineText = discipline;
            if (disciplineText != null) {
                Log.e("saveSet", disciplineText);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_schedule, parent, false));
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Log.e("save", isDefault[0].toString());
            final Button allSemesters = holder.allSemesters;
            final Button twoWeeks = holder.twoWeeks;
            twoWeeks.setClickable(false);
            twoWeeks.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            twoWeeks.setTextColor(getResources().getColor(R.color.colorGrey));
            allSemesters.setClickable(true);
            allSemesters.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            allSemesters.setTextColor(getResources().getColor(R.color.colorAccent));

            scheduleAdapter.setSchedule(tempSchedule);
            holder.mSchedule.setAdapter(scheduleAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.scrollToPositionWithOffset(positionSave, 0);
            holder.mSchedule.setLayoutManager(linearLayoutManager);


            disciplineText = mSettings.getString("discipline", ALL_DISCIPLINES);

            Spinner spinner = holder.mFilter;

            List<String> disciplines = new ArrayList<>();
            disciplines.add(ALL_DISCIPLINES);
            for (int i = 0; i < mSchedule.size(); i++) {
                boolean isReplay = false;
                for (int j = 0; j < disciplines.size(); j++) {
                    if (mSchedule.get(i).getDiscipline().equals(disciplines.get(j))) {
                        isReplay = true;
                    }
                }
                if (!isReplay) {
                    disciplines.add(mSchedule.get(i).getDiscipline());
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, disciplines);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(adapter.getPosition(disciplineText));
            isDefault[0] = mSettings.getBoolean("default", true);
            default_settings(isDefault[0]);
            AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tempSchedule.clear();
                    String item = (String) parent.getItemAtPosition(position);
                    disciplineText = item;

                    for (int i = 0; i < mSchedule.size(); i++) {
                        if (mSchedule.get(i).getDiscipline().equals(item) ||
                                (item.equals(ALL_DISCIPLINES))) {
                            tempSchedule.add(mSchedule.get(i));
                        }
                    }
                    default_settings(isDefault[0]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };
            spinner.setOnItemSelectedListener(itemSelectedListener);

            allSemesters.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allSemesters.setClickable(false);
                    allSemesters.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    allSemesters.setTextColor(getResources().getColor(R.color.colorGrey));
                    twoWeeks.setClickable(true);
                    twoWeeks.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                    twoWeeks.setTextColor(getResources().getColor(R.color.colorAccent));
                    isDefault[0] = false;
                    default_settings(isDefault[0]);
                    Log.e("savefalse", isDefault[0].toString());
                }
            });

            twoWeeks.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    twoWeeks.setClickable(false);
                    twoWeeks.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    twoWeeks.setTextColor(getResources().getColor(R.color.colorGrey));
                    allSemesters.setClickable(true);
                    allSemesters.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                    allSemesters.setTextColor(getResources().getColor(R.color.colorAccent));
                    isDefault[0] = true;
                    default_settings(isDefault[0]);
                }
            });
            if (isDefault[0]) {
                twoWeeks.callOnClick();
            } else {
                allSemesters.callOnClick();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void default_settings(boolean isDefault) {
            if (isDefault) {
                List<UserSchedule> temp = new ArrayList<>();
                for (int i = 0; i < tempSchedule.size(); i++) {
                    Date localDate = new SecretData().getDate(tempSchedule.get(i).getEndTime());
                    Date today = new Date();
                    Calendar instance = Calendar.getInstance();
                    instance.setTime(today);
                    instance.add(Calendar.DAY_OF_MONTH, 14);
                    Date daysBefore = instance.getTime();

                    if (localDate.compareTo(today) >= 0 && daysBefore.compareTo(localDate) >= 0) {
                        temp.add(tempSchedule.get(i));
                    }
                }
                tempSchedule = temp;
            } else {
                tempSchedule.clear();
                for (int i = 0; i < mSchedule.size(); i++) {
                    if (mSchedule.get(i).getDiscipline().equals(disciplineText) ||
                            (disciplineText.equals(ALL_DISCIPLINES))) {
                        tempSchedule.add(mSchedule.get(i));
                    }
                }
            }
            scheduleAdapter.setSchedule(tempSchedule);
            mEditor.putBoolean("default", isDefault);
        }

        @Override
        public int getItemCount() {
            return 1;
        }

    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        protected RecyclerView mSchedule;
        protected Spinner mFilter;
        protected Button allSemesters;
        protected Button twoWeeks;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mSchedule = itemView.findViewById(R.id.schedule);
            mFilter = itemView.findViewById(R.id.spinner_discipline);
            allSemesters = itemView.findViewById(R.id.all_semestr);
            twoWeeks = itemView.findViewById(R.id.two_week);
        }
    }

    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleViewHolder> {

        private List<UserSchedule> mSchedule = new ArrayList<>();

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

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
            final UserSchedule schedule = mSchedule.get(position);

            String date = new SecretData().getDateString(schedule.getDate());
            holder.mDateSchedule.setText(date);
            holder.mDiscipline.setText(schedule.getDiscipline());
            holder.mShortTitle.setText(schedule.getShortTitle());
            holder.mLocation.setText(schedule.getLocation());

            String time = new SecretData().getTimeString(schedule.getStartTime());
            holder.mTime.setText(time);
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
            mGroup = itemView.findViewById(R.id.group_schedule);
        }
    }

}