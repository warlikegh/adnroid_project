package com.example.technoparkmobileproject.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.technoparkmobileproject.R;
import com.example.technoparkmobileproject.auth.AuthActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    static SharedPreferences mSettings;
    static SharedPreferences.Editor editor;
    static String SALT="salt";
    static String AUTH_TOKEN = "auth_token";
    static String LOGIN = "login";
    static String PASSWORD = "password";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button mButton=view.findViewById(R.id.logOut);
        mButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String masterKeyAlias = null;
                try {
                    masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    mSettings = EncryptedSharedPreferences.create(
                            "secret_shared_prefs",
                            masterKeyAlias,
                            getContext(),
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    );
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                editor = mSettings.edit();
                editor.remove(LOGIN)
                        .remove(PASSWORD)
                        .remove(AUTH_TOKEN)
                        .apply();
                startActivity(new Intent(getContext(), AuthActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }));
    }
}