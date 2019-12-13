package com.example.technoparkmobileproject.ui.shedule;

import android.content.Context;
import android.content.res.Resources;
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
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technoparkmobileproject.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ScheduleFragment extends Fragment {

    private List<UserSchedule> mSchedule;
    private ScheduleViewModel mScheduleViewModel;
    private static FragmentManager fragmentManager = null;
    public static final String STATE = "change";
    boolean isSaveState;
    static Context context;
    RecyclerView recycler;
    final MyAdapter adapter = new MyAdapter();

    private ScheduleViewModel scheduleViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        context = getContext();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = view.findViewById(R.id.schedule);
        recycler.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(linearLayoutManager);
        final String discipline;
        if (savedInstanceState == null) {
            discipline = null;
        } else {
            discipline = savedInstanceState.getString("discipline");
            Log.e("saveCreate", discipline);
        }


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

        mScheduleViewModel = new ViewModelProvider(getActivity())
                .get(ScheduleViewModel.class);
        mScheduleViewModel
                .getSchedule()
                .observe(getViewLifecycleOwner(), observer);
        mScheduleViewModel.refresh();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("discipline", adapter.disciplineText);
        Log.e("saveState", adapter.disciplineText);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<UserSchedule> mSchedule = new ArrayList<>();
        private List<UserSchedule> tempSchedule = new ArrayList<>();
        final ScheduleAdapter scheduleAdapter = new ScheduleAdapter();
        String disciplineText;
        String ALL_DISCIPLINES = "Все дисциплины";

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
            final Button allSemester = holder.allSemestr;
            final Button twoWeeks = holder.twoWeeks;
            twoWeeks.setClickable(false);
            twoWeeks.setBackgroundColor(getResources().getColor(R.color.colorWindow));
            allSemester.setClickable(true);
            allSemester.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            final boolean[] isDefault = {true};
            scheduleAdapter.setSchedule(tempSchedule);
            holder.mSchedule.setAdapter(scheduleAdapter);
            holder.mSchedule.setLayoutManager(new LinearLayoutManager(getContext()));

            Spinner spinner = holder.mFilter;
            List<String> disciplines = new ArrayList<>();
            disciplines.add(ALL_DISCIPLINES);
            for (int i = 0; i < mSchedule.size(); i++) {
                Boolean isReplay = false;
                for (int j = 0; j < disciplines.size(); j++) {
                    if (mSchedule.get(i).getDiscipline().equals(disciplines.get(j))) {
                        isReplay = true;
                    }
                }
                if (!isReplay) {
                    disciplines.add(mSchedule.get(i).getDiscipline());
                }
            }
            if (disciplineText == null && disciplines.size() > 1) {
                disciplineText = disciplines.get(0);
                Log.e("saveFirst", disciplineText);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, disciplines);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            default_settings(isDefault[0]);
            AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tempSchedule.clear();
                    String item = (String) parent.getItemAtPosition(position);
                    disciplineText = item;
                    Log.e("saveChange", disciplineText);

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

            allSemester.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tempSchedule.clear();
                    allSemester.setClickable(false);
                    allSemester.setBackgroundColor(getResources().getColor(R.color.colorWindow));
                    twoWeeks.setClickable(true);
                    twoWeeks.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    isDefault[0]=false;
                    default_settings(isDefault[0]);
                }
            });

            twoWeeks.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    twoWeeks.setClickable(false);
                    twoWeeks.setBackgroundColor(getResources().getColor(R.color.colorWindow));
                    allSemester.setClickable(true);
                    allSemester.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    isDefault[0] =true;
                    default_settings(isDefault[0]);
                }

            });


        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void default_settings(boolean isDefault) {
            if (isDefault) {
            List<UserSchedule> temp = new ArrayList<>();
            for (int i = 0; i < tempSchedule.size(); i++) {
                String date = tempSchedule.get(i).getDate().replace("T", " ").replace("Z", "");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.US);
                LocalDateTime localDate = LocalDateTime.parse(date, formatter);
                LocalDateTime today = LocalDateTime.now();
                LocalDateTime daysBefore = today.plusDays(14);

                if (localDate.compareTo(today) > 0 && daysBefore.compareTo(localDate) > 0) {
                    Log.d("date", "est");
                    temp.add(tempSchedule.get(i));
                }
            }
            tempSchedule = temp;

            } else {
                for (int i = 0; i < mSchedule.size(); i++) {
                    if (mSchedule.get(i).getDiscipline().equals(disciplineText) ||
                            (disciplineText.equals(ALL_DISCIPLINES))) {
                        tempSchedule.add(mSchedule.get(i));
                    }
                }
            }
            scheduleAdapter.setSchedule(tempSchedule);
        }


        @Override
        public int getItemCount() {
            return 1;
        }

    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        protected RecyclerView mSchedule;
        protected Spinner mFilter;
        protected Button allSemestr;
        protected Button twoWeeks;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mSchedule = itemView.findViewById(R.id.schedule);
            mFilter = itemView.findViewById(R.id.spinner_discipline);
            allSemestr = itemView.findViewById(R.id.all_semestr);
            twoWeeks = itemView.findViewById(R.id.two_week);
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

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
            final UserSchedule schedule = mSchedule.get(position);

            String date = schedule.getDate().replace("T", " ").replace("Z", "");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.US);
            LocalDateTime localDate = LocalDateTime.parse(date, formatter);

            holder.mDateSchedule.setText(localDate.format(formatter));
            holder.mDiscipline.setText(schedule.getDiscipline());
            holder.mShortTitle.setText(schedule.getShortTitle());
            holder.mLocation.setText(schedule.getLocation());

            String time = schedule.getStartTime().replace("T", " ").replace("Z", "");
            LocalDateTime localTime = LocalDateTime.parse(time, formatter);

            holder.mTime.setText(localTime.format(formatter));
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
        //protected ImageView mTitle

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