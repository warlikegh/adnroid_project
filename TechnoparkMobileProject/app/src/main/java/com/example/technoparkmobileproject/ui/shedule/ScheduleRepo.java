package com.example.technoparkmobileproject.ui.shedule;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.technoparkmobileproject.SecretData;
import com.example.technoparkmobileproject.network.ApiRepo;
import com.example.technoparkmobileproject.network.ScheduleApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleRepo {
    private final static MutableLiveData<List<UserSchedule>> mSchedule = new MutableLiveData<>();
    private final static MutableLiveData<ScheduleProgress> mScheduleProgress = new MutableLiveData<>();
    private SharedPreferences mSettings;
    private final Context mContext;
    private ScheduleApi mScheduleApi;
    private static String AUTH_TOKEN = "auth_token";
    private static String SITE = "site";
    private int key = 0;

    private final ScheduleDbManager.ReadAllListener<Schedule> readListener = new ScheduleDbManager.ReadAllListener<Schedule>() {
        @Override
        public void onReadAll(final Collection<Schedule> allItems) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    postList(allItems);
                    if (allItems.isEmpty()) {
                        refresh();
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    };

    public ScheduleRepo(Context context) {
        mContext = context;

        SharedPreferences mSettings;
        mSettings = mContext.getSharedPreferences("createFirst", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("isFirstSchedule", false);
        editor.apply();

        mScheduleApi = ApiRepo.from(mContext).getScheduleApi(new SecretData().getSecretData(mContext).getInt(SITE, 0));
    }

    public LiveData<List<UserSchedule>> getSchedule() {
        return mSchedule;
    }

    public LiveData<ScheduleProgress> getScheduleProgress() {
        return mScheduleProgress;
    }

    public void refresh() {
        mScheduleProgress.postValue(ScheduleProgress.IN_PROGRESS);
        final ScheduleDbManager manager = ScheduleDbManager.getInstance(mContext);
        mSettings = new SecretData().getSecretData(mContext);
        mScheduleApi.getUserSchedule(" Token " + mSettings.getString(AUTH_TOKEN, "")).enqueue(new Callback<List<ScheduleApi.UserSchedulePlain>>() {
            @Override
            public void onResponse(Call<List<ScheduleApi.UserSchedulePlain>> call,
                                   Response<List<ScheduleApi.UserSchedulePlain>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ScheduleApi.UserSchedulePlain> result = response.body();
                    mSchedule.postValue(transform(result));
                    manager.clean();
                    savedata(transform(result));
                    mScheduleProgress.postValue(ScheduleProgress.SUCCESS);
                } else {
                    if (key == 0) {
                        manager.readAll(readListener);
                        key++;
                    } else {
                        mScheduleProgress.postValue(ScheduleProgress.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleApi.UserSchedulePlain>> call, Throwable t) {
                if (key == 0) {
                    manager.readAll(readListener);
                    key++;
                } else {
                    mScheduleProgress.postValue(ScheduleProgress.FAILED_NET);
                }
            }
        });
    }

    public void pullFromDB() {
        ScheduleDbManager manager = ScheduleDbManager.getInstance(mContext);
        manager.readAll(readListener);
    }

    public void cleanDB() {
        final ScheduleDbManager manager = ScheduleDbManager.getInstance(mContext);
        manager.clean();
    }

    private static List<UserSchedule> transform(List<ScheduleApi.UserSchedulePlain> plains) {
        List<UserSchedule> result = new ArrayList<>();
        for (ScheduleApi.UserSchedulePlain schedulePlain : plains) {
            try {
                UserSchedule schedule = map(schedulePlain);
                result.add(schedule);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    private static List<UserSchedule.Group> transformGroup(List<ScheduleApi.UserSchedulePlain.Group> plains) {
        List<UserSchedule.Group> groups = new ArrayList<>();
        for (ScheduleApi.UserSchedulePlain.Group groupPlain : plains) {
            try {
                UserSchedule.Group group = mapGroup(groupPlain);
                groups.add(group);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return groups;
    }


    private static UserSchedule map(ScheduleApi.UserSchedulePlain resultPlain) throws ParseException {

        List<UserSchedule.Group> groups = transformGroup(resultPlain.getGroups());

        return new UserSchedule(resultPlain.getId(),
                resultPlain.getDiscipline(),
                resultPlain.getTitle(),
                resultPlain.getShortTitle(),
                resultPlain.getSuperShortTitle(),
                resultPlain.getDate(),
                resultPlain.getStartTime(),
                resultPlain.getEndTime(),
                resultPlain.getLocation(),
                groups);
    }


    private static UserSchedule.Group mapGroup(ScheduleApi.UserSchedulePlain.Group textPlain) throws ParseException {
        UserSchedule temp = new UserSchedule();
        return temp.new Group(
                textPlain.getId(),
                textPlain.getName());
    }


    private void savedata(List<UserSchedule> result) {
        for (int i = 0; i < result.size(); i++) {
            ScheduleDbManager.getInstance(mContext).insert(result.get(i).getId(), result.get(i).getTitle(), result.get(i).getDiscipline(),
                    result.get(i).getShortTitle(), result.get(i).getSuperShortTitle(), result.get(i).getDate(), result.get(i).getStartTime(),
                    result.get(i).getEndTime(), result.get(i).getGroups(), result.get(i).getLocation());
        }

    }

    private void postList(Collection<Schedule> collection) {
        Comparator<Schedule> comp = new Comparator<Schedule>() {
            @Override
            public int compare(Schedule a, Schedule b) {
                Date resultB = new SecretData().getDate(b.startTime);
                Date resultA = new SecretData().getDate(a.startTime);

                if (resultA != null) {
                    return resultA.compareTo(resultB);
                } else {
                    return Integer.compare(a.id, b.id);
                }
            }
        };

        List<Schedule> data = new ArrayList(collection);
        Collections.sort(data, comp);

        List<UserSchedule> tempResult = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            UserSchedule temp = new UserSchedule(data.get(i).id, data.get(i).discipline,
                    data.get(i).title, data.get(i).shortTitle, data.get(i).superShortTitle, data.get(i).date,
                    data.get(i).startTime, data.get(i).endTime, data.get(i).location, data.get(i).getGroup());
            tempResult.add(temp);
        }
        mSchedule.postValue(tempResult);
        mScheduleProgress.postValue(ScheduleProgress.SUCCESS);
    }

    public enum ScheduleProgress {
        IN_PROGRESS,
        SUCCESS,
        FAILED,
        FAILED_NET
    }
}
