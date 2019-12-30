package com.example.technoparkmobileproject.auth;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.technoparkmobileproject.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.imbryk.viewPager.LoopViewPager;

import static com.example.technoparkmobileproject.R.color.colorAccent;
import static com.example.technoparkmobileproject.R.color.colorOrange;
import static com.example.technoparkmobileproject.R.color.colorRed;
import static com.example.technoparkmobileproject.R.color.colorWindow;


public class AuthFragment extends Fragment {

    Button enter;
    EditText mLogin;
    EditText mPassword;

    private AuthViewModel mAuthViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.auth_fragment, container, false);
        mLogin = view.findViewById(R.id.login);
        mPassword = view.findViewById(R.id.password);
        enter = view.findViewById(R.id.getBtn);

        int[] pictureIds = new int[]{
                R.mipmap.tech_park,
                R.mipmap.tech_sfera,
                R.mipmap.tech_track,
                R.mipmap.tech_polis,
                R.mipmap.tech_atom,
                R.mipmap.voronezsch,
                R.mipmap.pensa,
                R.mipmap.p_manager,
                R.mipmap.big_data
        };

        final LoopViewPager viewpager = view.findViewById(R.id.viewpager);
        viewpager.setBackgroundColor(getResources().getColor(colorWindow));
        ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(), pictureIds);
        viewpager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewpager, true);

        mAuthViewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);

        mAuthViewModel.getProgress().observe(getViewLifecycleOwner(), new Observer<AuthViewModel.AuthState>() {
            @Override
            public void onChanged(AuthViewModel.AuthState authState) {
                if (authState == AuthViewModel.AuthState.FAILED) {
                    enter.setEnabled(true);
                    enter.setBackgroundColor(getResources().getColor(colorRed));
                    Snackbar.make(view, "Что-то не так! Вероятно, неправильно указаны данные", Snackbar.LENGTH_LONG)
                            .show();
                } else if (authState == AuthViewModel.AuthState.FAILED_NET) {
                    enter.setEnabled(true);
                    enter.setBackgroundColor(getResources().getColor(colorOrange));
                    Snackbar.make(view, "Нет соединения!", Snackbar.LENGTH_LONG)
                            .show();
                } else if (authState == AuthViewModel.AuthState.IN_PROGRESS) {
                    enter.setBackgroundColor(getResources().getColor(colorAccent));
                    enter.setEnabled(false);
                    Snackbar.make(view, "Загружаю...", Snackbar.LENGTH_LONG)
                            .show();
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
                mAuthViewModel.login(mLogin.getText().toString(), mPassword.getText().toString(), viewpager.getCurrentItem());
            }
        });

        return view;

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

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
            avatarImageView.setBackgroundColor(getResources().getColor(colorWindow));
            avatarImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((LinearLayout) object);
        }

    }
}
