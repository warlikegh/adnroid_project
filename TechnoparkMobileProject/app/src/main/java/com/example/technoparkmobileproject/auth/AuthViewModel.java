package com.example.technoparkmobileproject.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.technoparkmobileproject.auth.AuthRepo;

public class AuthViewModel extends AndroidViewModel {

  private MediatorLiveData<AuthState> mAuthState = new MediatorLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        mAuthState.setValue(AuthState.NONE);
    }

    public LiveData<AuthState> getProgress() {
        return mAuthState;
    }

    public void login(String login, String password, int page) {
        AuthData AuthData = new AuthData(login, password, page);
       if (mAuthState.getValue() != AuthState.IN_PROGRESS) {
            requestAuth(AuthData);
        }
    }

    private void requestAuth(final AuthData AuthData) {
        mAuthState.postValue(AuthState.IN_PROGRESS);
        final LiveData<AuthRepo.AuthProgress> progressLiveData = AuthRepo.getInstance(getApplication())
                .login(AuthData.getAuth(), AuthData.getPassword(),AuthData.getUrl());
        mAuthState.addSource(progressLiveData, new Observer<AuthRepo.AuthProgress>() {
            @Override
            public void onChanged(AuthRepo.AuthProgress authProgress) {
                if (authProgress == AuthRepo.AuthProgress.SUCCESS) {
                    mAuthState.postValue(AuthState.SUCCESS);
                    mAuthState.removeSource(progressLiveData);
                } else if (authProgress == AuthRepo.AuthProgress.FAILED) {
                    mAuthState.postValue(AuthState.FAILED);
                    mAuthState.removeSource(progressLiveData);
                }else if (authProgress == AuthRepo.AuthProgress.FAILED_NET) {
                    mAuthState.postValue(AuthState.FAILED_NET);
                    mAuthState.removeSource(progressLiveData);
                }
            }
        });
    }


    public enum AuthState {
        NONE,
        IN_PROGRESS,
        SUCCESS,
        FAILED,
        FAILED_NET
    }

    public static class AuthData {
        private final String mLogin;
        private final String mPassword;
        private final String mUrl;

        public AuthData(String login, String password, int page) {
            mLogin = login;
            mPassword = password;
            mUrl = pageUrl[page];
        }

        public String getAuth() {
            return mLogin;
        }

        public String getPassword() {
            return mPassword;
        }

        public String getUrl() {
            return mUrl;
        }


        String[] pageUrl = new String[]{
                "https://park.mail.ru/api/mobile/v1/",
                "https://sphere.mail.ru/api/mobile/v1/",
                "https://track.mail.ru/api/mobile/v1/",
                "https://polis.mail.ru/api/mobile/v1/",
                "https://technoatom.mail.ru/api/mobile/v1/",
                "https://vgu.mail.ru/api/mobile/v1/",
                "https://pgu.mail.ru/api/mobile/v1/",
                "https://pm.mail.ru/api/mobile/v1/",
                "https://data.mail.ru/api/mobile/v1/"

        };


    }
}