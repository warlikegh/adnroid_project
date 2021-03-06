package com.zzz.technoparkmobileproject.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zzz.technoparkmobileproject.SecretData;
import com.zzz.technoparkmobileproject.TechnoparkApplication;
import com.zzz.technoparkmobileproject.network.ApiRepo;
import com.zzz.technoparkmobileproject.network.ProfileApi;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.zzz.technoparkmobileproject.TechnoparkApplication.AUTH_TOKEN;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.CREATE_FIRST_SETTINGS;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.IS_FIRST_PROFILE;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.PROFILE_MY_ID;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.PROFILE_PATH_URL;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.SITE;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.TOKEN;

class ProfileRepo {

    private final static MutableLiveData<UserProfile> mProfile = new MutableLiveData<>();
    private SharedPreferences mSettings;
    private SharedPreferences mSecretSettings;
    SharedPreferences.Editor mEditor;
    private final Context mContext;
    private ProfileApi mProfileApi;
    private int key = 0;
    private static String username_repo;
    private MutableLiveData<ProfileProgress> mProfileProgress = new MutableLiveData<>();

    private final ProfileDbManager.ReadListener<Profile> readListener = new ProfileDbManager.ReadListener<Profile>() {
        @Override
        public void onRead(final Profile item, final long id) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (item != null) {
                        post(item);
                    } else {
                        refresh(username_repo, (int) id);
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    };

    ProfileRepo(Context context) {
        mContext = context;
        mProfileApi = ApiRepo.from(mContext).getProfileApi(new SecretData().getSecretData(mContext).getInt(SITE, 0));

        mSettings = mContext.getSharedPreferences(CREATE_FIRST_SETTINGS, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    public LiveData<UserProfile> getProfile() {
        return mProfile;
    }

    public LiveData<ProfileProgress> getProfileProgress() {
        return mProfileProgress;
    }

    public void refreshMe() {
        mProfileProgress.postValue(ProfileProgress.IN_PROGRESS);
        mProfile.postValue(null);
        mEditor.putBoolean(IS_FIRST_PROFILE, false);
        mEditor.apply();
        mSecretSettings = new SecretData().getSecretData(mContext);
        mProfileApi.getUserProfile(TOKEN + mSecretSettings.getString(AUTH_TOKEN, "")).enqueue(new Callback<ProfileApi.UserProfilePlain>() {
            @Override
            public void onResponse(Call<ProfileApi.UserProfilePlain> call,
                                   Response<ProfileApi.UserProfilePlain> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileApi.UserProfilePlain result = response.body();
                    mProfile.postValue(transform(result));
                    mProfileProgress.postValue(ProfileProgress.SUCCESS);
                    saveData(transform(result));
                    mEditor.putInt(PROFILE_MY_ID, result.getId()).commit();
                } else {
                    if (key == 0) {
                        pullMeFromDB();
                        key++;
                    } else {
                      //  mProfile.postValue(null);
                        mProfileProgress.postValue(ProfileProgress.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileApi.UserProfilePlain> call, Throwable t) {
                if (key == 0) {
                    pullMeFromDB();
                    key++;
                } else {
                   // mProfile.postValue(null);
                    mProfileProgress.postValue(ProfileProgress.FAILED_NET);
                }
            }
        });
    }


    public void refresh(final String username, final int id) {
        mProfileProgress.postValue(ProfileProgress.IN_PROGRESS);
        mProfile.postValue(null);
        username_repo = username;
        final ProfileDbManager manager = ProfileDbManager.getInstance(mContext);
        mSecretSettings = new SecretData().getSecretData(mContext);
        mProfileApi.getOtherUserProfile(TOKEN + mSecretSettings.getString(AUTH_TOKEN, ""), PROFILE_PATH_URL + username).enqueue(new Callback<ProfileApi.UserProfilePlain>() {
            @Override
            public void onResponse(Call<ProfileApi.UserProfilePlain> call,
                                   Response<ProfileApi.UserProfilePlain> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileApi.UserProfilePlain result = response.body();
                    mProfile.postValue(transform(result));
                    mProfileProgress.postValue(ProfileProgress.SUCCESS);
                    saveData(transform(result));
                } else {
                    if (key == 0) {
                        pullFromDB(id, username);
                        key++;
                    } else {
                   //     mProfile.postValue(null);
                        mProfileProgress.postValue(ProfileProgress.FAILED);
                    }

                }
            }

            @Override
            public void onFailure(Call<ProfileApi.UserProfilePlain> call, Throwable t) {
                if (key == 0) {
                    pullFromDB(id, username);
                    key++;
                } else {
                  //  mProfile.postValue(null);
                    mProfileProgress.postValue(ProfileProgress.FAILED_NET);
                }
            }
        });
    }

    public void pullMeFromDB() {
        mProfileProgress.postValue(ProfileProgress.IN_PROGRESS);
    //    mProfile.postValue(null);
        ProfileDbManager manager = ProfileDbManager.getInstance(mContext);
        long id = mSettings.getInt(PROFILE_MY_ID, -1);
        manager.read(readListener, id);
    }

    public void cleanDB() {
        final ProfileDbManager manager = ProfileDbManager.getInstance(mContext);
        long id = mSettings.getInt(PROFILE_MY_ID, -1);
        manager.clean(id);
    }

    public void pullFromDB(long id, String username) {
        mProfileProgress.postValue(ProfileProgress.IN_PROGRESS);
    //    mProfile.postValue(null);
        ProfileDbManager manager = ProfileDbManager.getInstance(mContext);
        username_repo = username;
        manager.read(readListener, id);
    }

    private static UserProfile transform(ProfileApi.UserProfilePlain plain) {
        List<UserProfile.Subgroup> subgroups = new ArrayList<>();
        for (ProfileApi.UserProfilePlain.Subgroup subgroupPlain : plain.getSubgroups()) {
            try {
                UserProfile.Subgroup subgroup = mapSubgroup(subgroupPlain);
                subgroups.add(subgroup);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        UserProfile.Activity activity = null;
        try {
            activity = mapActivity(plain.getActivity());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<UserProfile.Contact> contacts = new ArrayList<>();
        for (ProfileApi.UserProfilePlain.Contact contactPlain : plain.getContacts()) {
            try {
                UserProfile.Contact contact = mapContact(contactPlain);
                contacts.add(contact);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<UserProfile.Account> accounts = new ArrayList<>();
        for (ProfileApi.UserProfilePlain.Account accountPlain : plain.getAccounts()) {
            try {
                UserProfile.Account account = mapAccount(accountPlain);
                accounts.add(account);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return new UserProfile(plain.getId(), plain.getUsername(), plain.getProjectId(), plain.getProject(), plain.getFullname(),
                plain.getGender(), plain.getAvatarUrl(), plain.getMainGroup(), plain.getBirthdate(), plain.getOnline(), plain.getAbout(),
                subgroups, activity, contacts, accounts, plain.getRating());
    }

    private static UserProfile.Subgroup mapSubgroup(ProfileApi.UserProfilePlain.Subgroup subgroupPlain) throws ParseException {
        UserProfile temp = new UserProfile();
        return temp.new Subgroup(subgroupPlain.getId(), subgroupPlain.getName());
    }

    private static UserProfile.Activity mapActivity(ProfileApi.UserProfilePlain.Activity activityPlain) throws ParseException {
        UserProfile temp = new UserProfile();
        return temp.new Activity(activityPlain.getLastSeen(), activityPlain.getDateJoined());
    }

    private static UserProfile.Contact mapContact(ProfileApi.UserProfilePlain.Contact contactsPlain) throws ParseException {
        UserProfile temp = new UserProfile();
        return temp.new Contact(contactsPlain.getName(), contactsPlain.getValue());
    }

    private static UserProfile.Account mapAccount(ProfileApi.UserProfilePlain.Account accountsPlain) throws ParseException {
        UserProfile temp = new UserProfile();
        return temp.new Account(accountsPlain.getName(), accountsPlain.getValue());
    }

    private void post(Profile profile) {
        UserProfile temp = new UserProfile(profile.id, profile.username, profile.projectId, profile.project, profile.fullname,
                profile.gender, profile.avatarUrl, profile.mainGroup, profile.birthdate, profile.online, profile.about,
                profile.getSubgroups(), profile.getActivity(), profile.getContacts(), profile.getAccounts(), profile.rating);
        mProfile.postValue(temp);
        mProfileProgress.postValue(ProfileProgress.SUCCESS);;
    }

    private void saveData(UserProfile result) {
        ProfileDbManager.getInstance(mContext).insert(
                result.getId(),
                result.getUsername(),
                result.getProjectId(),
                result.getProject(),
                result.getFullname(),
                result.getGender(),
                result.getAvatarUrl(),
                result.getMainGroup(),
                result.getBirthdate(),
                result.getOnline(),
                result.getAbout(),
                result.getSubgroups(),
                result.getActivity(),
                result.getContacts(),
                result.getAccounts(),
                result.getRating());
    }

    public enum ProfileProgress {
        IN_PROGRESS,
        SUCCESS,
        FAILED,
        FAILED_NET
    }
}
