package com.zzz.technoparkmobileproject.group;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zzz.technoparkmobileproject.SecretData;
import com.zzz.technoparkmobileproject.network.ApiRepo;
import com.zzz.technoparkmobileproject.network.GroupApi;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zzz.technoparkmobileproject.TechnoparkApplication.AUTH_TOKEN;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.GROUP_LAST_ID;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.GROUP_PATH_URL;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.GROUP_SETTINGS;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.SITE;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.TOKEN;

class GroupRepo {
    private final static MutableLiveData<UserGroup> mGroup = new MutableLiveData<>();
    private final static MutableLiveData<GroupProgress> mGroupProgress = new MutableLiveData<>();
    private SharedPreferences mSecretSettings;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;
    private final Context mContext;
    private GroupApi mGroupApi;
    private int key = 0;

    private final GroupDbManager.ReadListener<Group> readListener = new GroupDbManager.ReadListener<Group>() {
        @Override
        public void onRead(final List<Group> item, final long id) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (!item.isEmpty()) {
                        post(item);
                    } else {
                        refresh((int) id);
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    };

    GroupRepo(Context context) {
        mContext = context;
        mGroupApi = ApiRepo.from(mContext).getGroupApi(new SecretData().getSecretData(mContext).getInt(SITE, 0));
    }

    public LiveData<UserGroup> getGroup() {
        return mGroup;
    }

    public LiveData<GroupProgress> getGroupProgress() {
        return mGroupProgress;
    }

    public void refresh(final Integer id) {
        mGroupProgress.postValue(GroupProgress.IN_PROGRESS);
        String url = GROUP_PATH_URL + id.toString();
        mSecretSettings = new SecretData().getSecretData(mContext);
        mGroupApi.getStudentsGroup(TOKEN + mSecretSettings.getString(AUTH_TOKEN, ""), url).enqueue(
                new Callback<GroupApi.StudentGroupPlain>() {
            @Override
            public void onResponse(Call<GroupApi.StudentGroupPlain> call,
                                   Response<GroupApi.StudentGroupPlain> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GroupApi.StudentGroupPlain result = response.body();
                    cleanGroup(result.getId());
                    mGroup.postValue(transform(result));
                    mGroupProgress.postValue(GroupProgress.SUCCESS);
                    saveData(transform(result));
                } else {
                    if (key == 0) {
                        pullFromDB(id);
                        key++;
                    } else {
                        //mGroup.postValue(null);
                        mGroupProgress.postValue(GroupProgress.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<GroupApi.StudentGroupPlain> call, Throwable t) {
                if (key == 0) {
                    pullFromDB(id);
                    key++;
                } else {
                    //mGroup.postValue(null);
                    mGroupProgress.postValue(GroupProgress.FAILED_NET);
                }
            }
        });
    }

    private static UserGroup transform(GroupApi.StudentGroupPlain plains) {
        List<UserGroup.Student> result = new ArrayList<>();
        for (GroupApi.StudentGroupPlain.Student studentsPlain : plains.getStudents()) {
            try {
                UserGroup.Student students = map(studentsPlain);
                result.add(students);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Comparator<UserGroup.Student> comp = new Comparator<UserGroup.Student>() {
            @Override
            public int compare(UserGroup.Student a, UserGroup.Student b) {
                return a.getFullname().compareToIgnoreCase(b.getFullname());
            }};
        Collections.sort(result, comp);
        return new UserGroup(plains.getId(), plains.getName(), result);
    }

    private static UserGroup.Student map(GroupApi.StudentGroupPlain.Student resultPlain) throws ParseException {
        UserGroup temp = new UserGroup();
        return temp.new Student(resultPlain.getId(), resultPlain.getUsername(), resultPlain.getFullname(),
                resultPlain.getAvatarUrl(), resultPlain.getOnline(), resultPlain.getRating());
    }

    public void cleanGroup(Integer id) {
        final GroupDbManager manager = GroupDbManager.getInstance(mContext);
        manager.clean(id);
    }

    public void pullFromDB(long id) {
        GroupDbManager manager = GroupDbManager.getInstance(mContext);
        manager.read(readListener, id);
    }


    private void post(Collection<Group> list) {
        List<Group> data = new ArrayList();
        data.addAll(list);
        UserGroup temp = new UserGroup();
        List<UserGroup.Student> tempResult = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            UserGroup.Student tempStudent = temp.new Student(data.get(i).idUser, data.get(i).username,
                    data.get(i).fullname, data.get(i).avatarUrl, data.get(i).online, data.get(i).rating);
            tempResult.add(tempStudent);
        }
        mGroup.postValue(new UserGroup(data.get(0).idGroup, data.get(0).nameGroup, tempResult));
        mGroupProgress.postValue(GroupProgress.SUCCESS);
    }

    private void saveData(UserGroup result) {
        mSettings = mContext.getSharedPreferences(GROUP_SETTINGS, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();

        Integer id = mSettings.getInt(GROUP_LAST_ID, 1);

        for (int i = 0; i < result.getStudents().size(); i++, id++) {
            GroupDbManager.getInstance(mContext).insert(id, result.getId(), result.getName(),
                    result.getStudents().get(i).getId(), result.getStudents().get(i).getUsername(),
                    result.getStudents().get(i).getFullname(), result.getStudents().get(i).getAvatarUrl(),
                    result.getStudents().get(i).getOnline(), result.getStudents().get(i).getRating());
        }

        mEditor.putInt(GROUP_LAST_ID, id).apply();
    }
    public enum GroupProgress {
        IN_PROGRESS,
        SUCCESS,
        FAILED,
        FAILED_NET
    }
}
