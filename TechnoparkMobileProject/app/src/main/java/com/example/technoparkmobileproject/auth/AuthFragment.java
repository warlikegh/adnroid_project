package com.example.technoparkmobileproject.auth;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.technoparkmobileproject.R;


public class AuthFragment extends Fragment {
    Button enter;
    TextView result;
    EditText mLogin;
    EditText mPassword;

    private AuthViewModel mAuthViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.auth_fragment, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        result = view.findViewById(R.id.result);
        mLogin = view.findViewById(R.id.login);
        mPassword = view.findViewById(R.id.password);
        enter = view.findViewById(R.id.getBtn);

        mAuthViewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);

        mAuthViewModel.getProgress().observe(getViewLifecycleOwner(), new Observer<AuthViewModel.AuthState>() {
            @Override
            public void onChanged(AuthViewModel.AuthState authState) {
                if (authState == AuthViewModel.AuthState.FAILED) {
                    enter.setEnabled(true);
                    enter.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    result.setText("Что-то не так! Вероятно, неправильно указаны данные");
                }else if (authState == AuthViewModel.AuthState.FAILED_NET) {
                    enter.setEnabled(true);
                    enter.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                    result.setText("Нет соединения!");
                } else if (authState == AuthViewModel.AuthState.IN_PROGRESS) {
                    enter.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    enter.setEnabled(false);
                    result.setText("Загружаю...");
                } else if (authState == AuthViewModel.AuthState.SUCCESS) {
                    Router router = (Router) getActivity();
                    if (router != null) {
                        router.openMain();
                    }


                } else {
                    enter.setBackground(getContext().getDrawable(android.R.drawable.btn_default));
                    enter.setEnabled(true);
                }
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthViewModel.login(mLogin.getText().toString(), mPassword.getText().toString());
            }
        });
    }

}
