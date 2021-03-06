package com.zzz.technoparkmobileproject.ui.shedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zzz.technoparkmobileproject.R;
import com.zzz.technoparkmobileproject.SecretData;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.CREATE_FIRST_SETTINGS;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.DEFAULT_TWO_WEEK;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.DISCIPLINE;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.IS_FIRST_SCHEDULE;

public class ScheduleFragment extends Fragment {
    private List<UserSchedule> mSchedule = new ArrayList<>();
    private ScheduleRepo.ScheduleProgress mScheduleProgress;
    private ScheduleRepo.CheckProgress mCheckProgress;
    private List<UserSchedule> tempSchedule = new ArrayList<>();
    final List<String> disciplines = new ArrayList<>();
    final ScheduleAdapter scheduleAdapter = new ScheduleAdapter();
    String ALL_DISCIPLINES;
    String disciplineText;
    final Boolean[] isDefault = {true};
    private ScheduleViewModel mScheduleViewModel;
    static Context context;
    RecyclerView recycler;
    SharedPreferences mSettings;
    SharedPreferences.Editor mEditor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        mScheduleViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(ScheduleViewModel.class);
        mSettings = Objects.requireNonNull(getContext()).getSharedPreferences(CREATE_FIRST_SETTINGS, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
        if (mSettings.getBoolean(IS_FIRST_SCHEDULE, true))
            mScheduleViewModel.refresh();
        else
            mScheduleViewModel.pullFromDB();
        ALL_DISCIPLINES = context.getResources().getString(R.string.all_disciplines);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        final ProgressBar mProgressBar = view.findViewById(R.id.progress_bar);
        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorAccent, null), getResources().getColor(R.color.colorBlueBackgroungIS, null),
                getResources().getColor(R.color.colorGradientBottomAuth, null), getResources().getColor(R.color.colorGradientTopAuth, null));
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mScheduleViewModel.refresh();
                pullToRefresh.setRefreshing(false);
            }
        });

        final Button allSemesters = view.findViewById(R.id.all_semester);
        final Button twoWeeks = view.findViewById(R.id.two_week);

        recycler = view.findViewById(R.id.schedule);
        recycler.setAdapter(scheduleAdapter);
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

        disciplineText = mSettings.getString(DISCIPLINE, ALL_DISCIPLINES);
        isDefault[0] = mSettings.getBoolean(DEFAULT_TWO_WEEK, true);
        final Spinner spinner = view.findViewById(R.id.spinner_discipline);
        final ArrayAdapter<String>[] adapter = new ArrayAdapter[]{new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, disciplines)};

        adapter[0].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter[0]);
        spinner.setSelection(adapter[0].getPosition(disciplineText));

        allSemesters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allSemesters.setEnabled(false);
                twoWeeks.setEnabled(true);
                allSemesters.setTextColor(getResources().getColor(R.color.colorWhite, null));
                twoWeeks.setTextColor(getResources().getColor(R.color.colorAccent, null));
                isDefault[0] = false;
                scheduleAdapter.setTime(isDefault[0]);
                mEditor.putBoolean(DEFAULT_TWO_WEEK, isDefault[0]).commit();
            }
        });

        twoWeeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twoWeeks.setEnabled(false);
                allSemesters.setEnabled(true);
                allSemesters.setTextColor(getResources().getColor(R.color.colorAccent, null));
                twoWeeks.setTextColor(getResources().getColor(R.color.colorWhite, null));
                isDefault[0] = true;
                scheduleAdapter.setTime(isDefault[0]);
                mEditor.putBoolean(DEFAULT_TWO_WEEK, isDefault[0]).commit();
            }
        });

        if (isDefault[0]) {
            twoWeeks.callOnClick();
        } else {
            allSemesters.callOnClick();
        }

        Observer<List<UserSchedule>> observer = new Observer<List<UserSchedule>>() {
            @Override
            public void onChanged(List<UserSchedule> schedule) {
                if (schedule != null) {
                    mSchedule = schedule;
                    mProgressBar.setVisibility(GONE);
                    disciplines.clear();
                    disciplines.add(ALL_DISCIPLINES);
                    boolean isReplay;
                    for (int i = 0; i < mSchedule.size(); i++) {
                        isReplay = false;
                        for (int j = 0; j < disciplines.size(); j++)
                            if (mSchedule.get(i).getDiscipline().equals(disciplines.get(j)))
                                isReplay = true;
                        if (!isReplay)
                            disciplines.add(mSchedule.get(i).getDiscipline());
                    }
                    adapter[0] = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, disciplines);
                    adapter[0].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter[0]);
                    spinner.setSelection(adapter[0].getPosition(disciplineText));
                    scheduleAdapter.setSchedule(mSchedule, disciplineText, isDefault[0]);
                }
            }
        };

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                mEditor.putString(DISCIPLINE, item).commit();
                disciplineText = item;
                scheduleAdapter.setDiscipline(disciplineText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

        mScheduleViewModel
                .getSchedule()
                .observe(getViewLifecycleOwner(), observer);

        Observer<ScheduleRepo.ScheduleProgress> observerProgress = new Observer<ScheduleRepo.ScheduleProgress>() {
            @Override
            public void onChanged(ScheduleRepo.ScheduleProgress scheduleProgress) {
                if (scheduleProgress != null) {
                    mScheduleProgress = scheduleProgress;
                    if (mScheduleProgress == ScheduleRepo.ScheduleProgress.FAILED_NET) {
                        mProgressBar.setVisibility(GONE);
                    } else if (mScheduleProgress == ScheduleRepo.ScheduleProgress.IN_PROGRESS) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        mScheduleViewModel
                .getScheduleProgress()
                .observe(getViewLifecycleOwner(), observerProgress);

        Observer<ScheduleRepo.CheckProgress> observerCheckProgress = new Observer<ScheduleRepo.CheckProgress>() {
            @Override
            public void onChanged(ScheduleRepo.CheckProgress checkProgress) {
                if (checkProgress != null) {
                    mCheckProgress = checkProgress;
                    if (mCheckProgress == ScheduleRepo.CheckProgress.SUCCESS) {
                        Snackbar.make(view, R.string.check_success, BaseTransientBottomBar.LENGTH_SHORT);
                    } else if (mCheckProgress == ScheduleRepo.CheckProgress.FAILED) {
                        Snackbar.make(view, R.string.error, BaseTransientBottomBar.LENGTH_SHORT);
                    } else if (mCheckProgress == ScheduleRepo.CheckProgress.FAILED_NET) {
                        Snackbar.make(view, R.string.http_failed, BaseTransientBottomBar.LENGTH_SHORT);
                    }
                }
            }
        };

        mScheduleViewModel
                .getCheckProgress()
                .observe(getViewLifecycleOwner(), observerCheckProgress);

        return view;
    }

    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleViewHolder> {

        private List<UserSchedule> mSchedule = new ArrayList<>();
        private List<UserSchedule> mWholeSchedule = new ArrayList<>();
        private boolean isDefaultTime;
        private String discipline = "";
        int[] drawableStart = {R.drawable.shape_blue_start,
                R.drawable.shape_pink_start,
                R.drawable.shape_green_dark_start,
                R.drawable.shape_purple_start,
                R.drawable.shape_red_dark_start,
                R.drawable.shape_blue_dark_start};
        String[] daysOfTheWeek;

        public void setSchedule(List<UserSchedule> schedule, String disciplineText, boolean defaultTime) {
            mWholeSchedule = schedule;
            discipline = disciplineText;
            isDefaultTime = defaultTime;
            filter(discipline, isDefaultTime);
            daysOfTheWeek = new String[]{getResources().getString(R.string.monday), getResources().getString(R.string.tuesday),
                    getResources().getString(R.string.wednesday), getResources().getString(R.string.thursday),
                    getResources().getString(R.string.friday), getResources().getString(R.string.saturday),
                    getResources().getString(R.string.sunday)};
            notifyDataSetChanged();
        }

        public void setTime(boolean time) {
            isDefaultTime = time;
            filter(discipline, isDefaultTime);
        }

        public void setDiscipline(String text) {
            discipline = text;
            filter(discipline, isDefaultTime);
        }

        public void filter(String text, boolean defaultTime) {
            mSchedule.clear();
            if (mWholeSchedule != null) {
                if (!defaultTime) {
                    if (text.isEmpty() || text.equals(ALL_DISCIPLINES))
                        mSchedule.addAll(mWholeSchedule);
                    else
                        for (UserSchedule item : mWholeSchedule)
                            if (item.getDiscipline().equals(text))
                                mSchedule.add(item);
                } else {
                    for (int i = 0; i < mWholeSchedule.size(); i++) {
                        Date localDate = new SecretData().getDate(mWholeSchedule.get(i).getDate());

                        Calendar instance = Calendar.getInstance();
                        instance.setTime(localDate);
                        instance.add(Calendar.DAY_OF_MONTH, 1);
                        Date newDate = instance.getTime();

                        Date today = new Date();
                        instance.setTime(today);
                        instance.add(Calendar.DAY_OF_MONTH, 14);
                        Date daysBefore = instance.getTime();

                        if (newDate.compareTo(today) >= 0 && daysBefore.compareTo(newDate) >= 0)
                            if (mWholeSchedule.get(i).getDiscipline().equals(text) || text.equals(ALL_DISCIPLINES))
                                mSchedule.add(mWholeSchedule.get(i));
                    }
                }
            }
            tempSchedule = mSchedule;
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
            if (mSchedule.size() > 0) {
                holder.mNoSchedule.setVisibility(GONE);
                holder.mScheduleLayout.setVisibility(View.VISIBLE);
                final UserSchedule schedule = mSchedule.get(position);

                holder.mDiscipline.setText(schedule.getDiscipline());
                holder.mShortTitle.setText(schedule.getShortTitle());
                holder.mLocation.setText(schedule.getLocation());

                Date now = new Date();
                Date end = new SecretData().getDate(schedule.getEndTime());
                String date = new SecretData().getDateString(schedule.getDate());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(end);
                String dayOfWeek = daysOfTheWeek[Math.abs(calendar.get(Calendar.DAY_OF_WEEK) - 2) % daysOfTheWeek.length];
                holder.mDateSchedule.setText(String.format("%s\n%s", date, dayOfWeek));
                if (now.after(end))
                    holder.mSeparator1.setBackground(getResources().getDrawable(R.drawable.shape_done_start, null));
                else
                    holder.mSeparator1.setBackground(getResources()
                            .getDrawable(drawableStart[(disciplines.indexOf(schedule.getDiscipline()) - 1) % drawableStart.length], null));

                String time = new SecretData().getTimeString(schedule.getStartTime());
                holder.mTime.setText(time);
                holder.mTitle.setText(schedule.getTitle());
                holder.mDiscipline.setText(schedule.getDiscipline());

                if (schedule.getAttended()) {
                    holder.mCheckButton.setVisibility(View.VISIBLE);
                    holder.mCheckButton.setText(R.string.checked);
                    holder.mCheckButton.setEnabled(false);
                    holder.mCheckButton.setTextColor(getResources().getColor(R.color.colorGradientBottomAuth, null));
                    if (schedule.getFeedbackUrl() != null) {
                        holder.mFeedbackButton.setVisibility(View.VISIBLE);
                    } else {
                        holder.mFeedbackButton.setVisibility(GONE);
                    }
                } else {
                    holder.mFeedbackButton.setVisibility(GONE);
                    if (schedule.getCheckingOpened()) {
                        holder.mCheckButton.setVisibility(View.VISIBLE);
                        holder.mCheckButton.setEnabled(true);
                        holder.mCheckButton.setText(R.string.check);
                        holder.mCheckButton.setTextColor(getResources().getColor(R.color.colorAccent, null));
                    } else {
                        holder.mCheckButton.setVisibility(INVISIBLE);
                    }
                }
            } else {
                holder.mScheduleLayout.setVisibility(GONE);
                holder.mNoSchedule.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            if (mSchedule.size() > 0)
                return mSchedule.size();
            else
                return 1;
        }

    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder {

        protected TextView mDateSchedule;
        protected TextView mDiscipline;
        protected TextView mShortTitle;
        protected TextView mLocation;
        protected TextView mTime;
        protected TextView mTitle;
        protected TextView mCheckButton;
        protected TextView mFeedbackButton;
        protected TextView mNoSchedule;
        protected RelativeLayout mScheduleLayout;

        protected View mSeparator1;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            mDateSchedule = itemView.findViewById(R.id.date_schedule);
            mDiscipline = itemView.findViewById(R.id.discipline);
            mShortTitle = itemView.findViewById(R.id.short_title);
            mLocation = itemView.findViewById(R.id.location);
            mTime = itemView.findViewById(R.id.time);
            mTitle = itemView.findViewById(R.id.title);
            mCheckButton = itemView.findViewById(R.id.check_button);
            mFeedbackButton = itemView.findViewById(R.id.feedback_button);
            mNoSchedule = itemView.findViewById(R.id.no_schedule);
            mScheduleLayout = itemView.findViewById(R.id.layout_schedule);

            mCheckButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = ScheduleViewHolder.this.getAdapterPosition();
                    mScheduleViewModel.check(tempSchedule.get(pos).getId());
                    Snackbar.make(view, R.string.checking, BaseTransientBottomBar.LENGTH_SHORT);
                }
            });

            mFeedbackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = ScheduleViewHolder.this.getAdapterPosition();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tempSchedule.get(pos).getFeedbackUrl()));
                    startActivity(browserIntent);
                }
            });

            mSeparator1 = itemView.findViewById(R.id.separator1);
        }
    }
}