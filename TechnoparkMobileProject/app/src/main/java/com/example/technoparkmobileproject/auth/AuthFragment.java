package com.example.technoparkmobileproject.auth;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.technoparkmobileproject.R;
import com.google.android.material.tabs.TabLayout;


public class AuthFragment extends Fragment {
    Button enter;
    TextView result;
    EditText mLogin;
    EditText mPassword;
    ProgressBar mProgressBar;

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
        mProgressBar = view.findViewById(R.id.progress);
//////////////////////////////выделил сюда всё что связано с ViewPager
        int[] pictureIds = new int[]{R.drawable.icon1, R.drawable.icon2};

        ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(), pictureIds);

        ViewPager viewPager = view.findViewById(R.id.photos_viewpager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
////////////////////////////////////
        mAuthViewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);

        mAuthViewModel.getProgress().observe(getViewLifecycleOwner(), new Observer<AuthViewModel.AuthState>() {
            @Override
            public void onChanged(AuthViewModel.AuthState authState) {
                if (authState == AuthViewModel.AuthState.FAILED) {
                    enter.setEnabled(true);
                    enter.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    result.setText("Что-то не так! Вероятно, неправильно указаны данные");
                    mProgressBar.setVisibility(View.GONE);
                }else if (authState == AuthViewModel.AuthState.FAILED_NET) {
                    enter.setEnabled(true);
                    enter.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                    result.setText("Нет соединения!");
                    mProgressBar.setVisibility(View.GONE);
                } else if (authState == AuthViewModel.AuthState.IN_PROGRESS) {
                    enter.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    enter.setEnabled(false);
                    result.setText("Загружаю...");
                    mProgressBar.setVisibility(View.VISIBLE);
                } else if (authState == AuthViewModel.AuthState.SUCCESS) {
                    mProgressBar.setVisibility(View.GONE);
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
//Ниже реализован адаптер для ViewPager, хочешь - меняй, хочешь - удаляй
    public class ViewPagerAdapter extends PagerAdapter {
        private Context mContext;
        private int[] mPictureIDs;

        public ViewPagerAdapter(Context context, int[] resids) {
            this.mContext = context;
            this.mPictureIDs = resids;
        }

        @Override
        public int getCount() {
            return mPictureIDs.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView avatarImageView;

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.auth_pager_holder, container,
                    false);


            avatarImageView = itemView.findViewById(R.id.imageViewAvatar);
            avatarImageView.setImageResource(mPictureIDs[position]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
