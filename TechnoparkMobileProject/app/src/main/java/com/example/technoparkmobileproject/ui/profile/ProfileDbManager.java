package com.example.technoparkmobileproject.ui.profile;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProfileDbManager {

    @SuppressLint("StaticFieldLeak")
    private static final ProfileDbManager INSTANCE = new ProfileDbManager();

    static ProfileDbManager getInstance(Context context) {
        INSTANCE.context = context.getApplicationContext();
        return INSTANCE;
    }

    private final Executor executor = Executors.newSingleThreadExecutor();

    private Context context;

    void insert(final int id, final String username, final Integer projectId, final String project, final String fullname,
                final String gender, final String avatarUrl, final String mainGroup, final String birthdate, final Boolean online, final String about,
                final List<UserProfile.Subgroup> subgroups,
                final UserProfile.Activity activity,
                final List<UserProfile.Contact> contacts,
                final List<UserProfile.Account> accounts,
                final Double rating) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                insertRoom(id, username, projectId, project, fullname, gender, avatarUrl, mainGroup, birthdate, online,
                        about, subgroups, activity, contacts, accounts, rating);
            }
        });
    }

  /*  void readAll(final ReadAllListener<Profile> listener) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                readAllRoom(listener);
            }
        });
    }*/

    void clean(final long id) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cleanElement(id);
            }
        });
    }

    private void cleanElement(long id) {
        ProfileDao profileDao = ProfileDataBase.getInstance(context).getProfileDao();
        profileDao.delete(profileDao.getById(id));
    }

    private void insertRoom(int id, String username, Integer projectId, String project, String fullname,
                            String gender, String avatarUrl, String mainGroup, String birthdate, Boolean online, String about,
                            List<UserProfile.Subgroup> subgroups,
                            UserProfile.Activity activity,
                            List<UserProfile.Contact> contacts,
                            List<UserProfile.Account> accounts,
                            Double rating) {
        Profile profile = new Profile(id, username, projectId, project, fullname, gender, avatarUrl, mainGroup, birthdate, online,
                about, subgroups, activity, contacts, accounts, rating);
        ProfileDataBase.getInstance(context).getProfileDao().insert(profile);
    }

   /* private void readAllRoom(final ReadAllListener<Profile> listener) {
        List<Profile> list = ProfileDataBase.getInstance(context).getNewsDao().getAll();
        ArrayList<Profile> strings = new ArrayList<>();
        for (Profile profile : list) {
            strings.add(profile);
        }
        listener.onRead(strings);
    }

    public interface ReadAllListener<T> {
        void onRead(final Collection<T> allItems);
    }

*/
    void read(final ReadListener<Profile> listener, final long id) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                readRoom(listener, id);
            }
        });
    }

    private void readRoom(final ReadListener<Profile> listener, long id) {
        Profile profile = ProfileDataBase.getInstance(context).getProfileDao().getById(id);
        listener.onRead(profile, id);
    }

    public interface ReadListener<T> {
        void onRead(final T item, long id);
    }

}
