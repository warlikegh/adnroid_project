package com.example.technoparkmobileproject.group;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.technoparkmobileproject.SecretData;
import com.example.technoparkmobileproject.network.ApiRepo;
import com.example.technoparkmobileproject.network.GroupApi;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupRepo {
    private final static MutableLiveData<UserGroup> mGroup = new MutableLiveData<>();
    private SharedPreferences mSettings;
    private final Context mContext;
    private GroupApi mGroupApi;
    private static String AUTH_TOKEN = "auth_token";
    private static String SITE = "site";
    private int key = 0;


    GroupRepo(Context context) {
        mContext = context;
        mGroupApi = ApiRepo.from(mContext).getGroupApi(new SecretData().getSecretData(mContext).getInt(SITE, 0));
    }

    public LiveData<UserGroup> getGroup() {
        return mGroup;
    }

    public void refresh(Integer id) {
        String url = "groups/" + id.toString();
        mSettings = new SecretData().getSecretData(mContext);
        mGroupApi.getStudentsGroup(" Token " + mSettings.getString(AUTH_TOKEN, ""), url).enqueue(new Callback<GroupApi.StudentGroupPlain>() {
            @Override
            public void onResponse(Call<GroupApi.StudentGroupPlain> call,
                                   Response<GroupApi.StudentGroupPlain> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GroupApi.StudentGroupPlain result = response.body();
                    mGroup.postValue(transform(result));
                } else {

                }
            }
            @Override
            public void onFailure(Call<GroupApi.StudentGroupPlain> call, Throwable t) {

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
        return new UserGroup(plains.getId(), plains.getName(), result);
    }

    private static UserGroup.Student map(GroupApi.StudentGroupPlain.Student resultPlain) throws ParseException {
        UserGroup temp = new UserGroup();
        return temp.new Student(resultPlain.getId(), resultPlain.getUsername(), resultPlain.getFullname(),
                resultPlain.getAvatarUrl(), resultPlain.getOnline(), resultPlain.getRating());
    }
}
