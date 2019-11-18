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

    public void login(String login, String password) {
        AuthData AuthData = new AuthData(login, password);
       if (mAuthState.getValue() != AuthState.IN_PROGRESS) {
            requestAuth(AuthData);
        }
    }

    private void requestAuth(final AuthData AuthData) {
        mAuthState.postValue(AuthState.IN_PROGRESS);
        final LiveData<AuthRepo.AuthProgress> progressLiveData = AuthRepo.getInstance(getApplication())
                .login(AuthData.getAuth(), AuthData.getPassword());
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

        public AuthData(String login, String password) {
            mLogin = login;
            mPassword = password;
        }

        public String getAuth() {
            return mLogin;
        }

        public String getPassword() {
            return mPassword;
        }
    }
}