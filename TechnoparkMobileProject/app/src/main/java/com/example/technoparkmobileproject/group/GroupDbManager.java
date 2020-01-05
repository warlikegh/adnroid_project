package com.example.technoparkmobileproject.group;

import android.annotation.SuppressLint;
        import android.content.Context;

        import java.util.List;
        import java.util.concurrent.Executor;
        import java.util.concurrent.Executors;

public class GroupDbManager {

    @SuppressLint("StaticFieldLeak")
    private static final GroupDbManager INSTANCE = new GroupDbManager();

    static GroupDbManager getInstance(Context context) {
        INSTANCE.context = context.getApplicationContext();
        return INSTANCE;
    }

    private final Executor executor = Executors.newSingleThreadExecutor();

    private Context context;

    void insert(final Integer id, final Integer idGroup,final String nameGroup,final Integer idUser,final String username,
                final String fullname,final String avatarUrl,final Boolean online,final Double rating) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                insertRoom(id, idGroup, nameGroup,idUser, username,
                        fullname, avatarUrl, online, rating);
            }
        });
    }

    void clean(final long id) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cleanGroup(id);
            }
        });
    }

    private void cleanGroup(long id) {
        GroupDao GroupDao = GroupDataBase.getInstance(context).getGroupDao();
        GroupDao.deleteByGroupId(id);
    }

    private void insertRoom(Integer id, Integer idGroup, String nameGroup, Integer idUser, String username,
                            String fullname, String avatarUrl, Boolean online, Double rating) {
        Group Group = new Group(id, idGroup, nameGroup,idUser, username,
                fullname, avatarUrl, online, rating);
        GroupDataBase.getInstance(context).getGroupDao().insert(Group);
    }

    void read(final ReadListener<Group> listener, final long id) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                readRoom(listener, id);
            }
        });
    }

    private void readRoom(final ReadListener<Group> listener, long id) {
        List<Group> group = GroupDataBase.getInstance(context).getGroupDao().getById(id);
        listener.onRead(group, id);
    }

    public interface ReadListener<T> {
        void onRead(final List<T> item, long id);
    }

}
