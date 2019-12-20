package com.example.technoparkmobileproject.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.technoparkmobileproject.SecretData;
import com.example.technoparkmobileproject.network.ApiRepo;
import com.example.technoparkmobileproject.network.ProfileApi;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ProfileRepo {

    private final static MutableLiveData<UserProfile> mProfile = new MutableLiveData<>();
    private SharedPreferences mSettings;
    SharedPreferences.Editor mEditor;
    private final Context mContext;
    private ProfileApi mProfileApi;
    private static String AUTH_TOKEN = "auth_token";
    private static String SITE = "site";
    private final Executor executor = Executors.newSingleThreadExecutor();
    private int key = 0;
  //  private final Integer myId;
  //  private static String username_repo;

    private final ProfileDbManager.ReadListener<Profile> readListener = new ProfileDbManager.ReadListener<Profile>() {
        @Override
        public void onRead(final Profile item, final long id) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
               //     if (item != null) {
                        post(item);
                //    } else {
                //         refresh(username_repo, (int) id);
                 //   }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    };


    ProfileRepo(Context context) {
        mContext = context;
        mProfileApi = ApiRepo.from(mContext).getProfileApi(new SecretData().getSecretData(mContext).getInt(SITE, 0));

        mSettings = mContext.getSharedPreferences("createFirst", Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
      //  myId = mSettings.getInt("my_id", -1);
    }

    public LiveData<UserProfile> getProfile() {
        return mProfile;
    }

    public void refreshMe() {
      //  mEditor.putBoolean("isFirstProfile", false);
      //  mEditor.apply();

        final ProfileDbManager manager = ProfileDbManager.getInstance(mContext);
        mSettings = new SecretData().getSecretData(mContext);
        mProfileApi.getUserProfile(" Token " + mSettings.getString(AUTH_TOKEN, "")).enqueue(new Callback<ProfileApi.UserProfilePlain>() {
            @Override
            public void onResponse(Call<ProfileApi.UserProfilePlain> call,
                                   Response<ProfileApi.UserProfilePlain> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileApi.UserProfilePlain result = response.body();
                    mProfile.postValue(transform(result));
                  //  if (mSettings.getInt("my_id", -1) == result.getId()) {
                    manager.clean(result.getId());
                 //   }
                    saveData(transform(result));
                    mEditor.putInt("my_id", result.getId());

                } else {
                  //  manager.read(readListener, mSettings.getInt("my_id", -1));
                }
            }

            @Override
            public void onFailure(Call<ProfileApi.UserProfilePlain> call, Throwable t) {
               //     manager.read(readListener, mSettings.getInt("my_id", -1));
                pullMeFromDB();
             /*   if (key == 1) {

                }*/
            }
        });
    }


    public void refresh(String username, final int id) {
        final ProfileDbManager manager = ProfileDbManager.getInstance(mContext);
        mSettings = new SecretData().getSecretData(mContext);
      //  username_repo = username;
        mProfileApi.getOtherUserProfile(" Token " + mSettings.getString(AUTH_TOKEN, ""),"profile/"+username).enqueue(new Callback<ProfileApi.UserProfilePlain>() {
            @Override
            public void onResponse(Call<ProfileApi.UserProfilePlain> call,
                                   Response<ProfileApi.UserProfilePlain> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileApi.UserProfilePlain result = response.body();
                    mProfile.postValue(transform(result));
                    saveData(transform(result));

                } else {
                    // manager.readAll(readListener);
                //    manager.read(readListener, id);
                }
            }

            @Override
            public void onFailure(Call<ProfileApi.UserProfilePlain> call, Throwable t) {
               // if (key == 0) {
                 //   manager.read(readListener, id);
             //       key++;
              //  }
            }
        });
    }

    public void pullMeFromDB() {
        ProfileDbManager manager = ProfileDbManager.getInstance(mContext);
        long id = mSettings.getInt("my_id", -1);
        manager.read(readListener, id);
    }

    public void pullFromDB() {
        ProfileDbManager manager = ProfileDbManager.getInstance(mContext);
       /* long id = mSettings.getInt("my_id", -1);
        manager.read(readListener, id);*/
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

}
