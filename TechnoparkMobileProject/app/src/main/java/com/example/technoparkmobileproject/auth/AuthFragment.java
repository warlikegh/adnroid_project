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
import static com.example.technoparkmobileproject.R.color.colorBlueBackgroungIS;
import static com.example.technoparkmobileproject.R.color.colorOrange;
import static com.example.technoparkmobileproject.R.color.colorRed;
import static com.example.technoparkmobileproject.R.color.colorRedMadeAuth;
import static com.example.technoparkmobileproject.R.color.colorRedTrackAuth;
import static com.example.technoparkmobileproject.R.color.colorWindow;


public class AuthFragment extends Fragment {

    Button enter;
    EditText mLogin;
    EditText mPassword;
    SnowView snowView;
    View viewAll;
    Integer mPos = 1;

    private AuthViewModel mAuthViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewAll = inflater.inflate(R.layout.auth_fragment, container, false);
        mLogin = viewAll.findViewById(R.id.login);
        mPassword = viewAll.findViewById(R.id.password);
        enter = viewAll.findViewById(R.id.getBtn);
        snowView = viewAll.findViewById(R.id.snow_view);
        int[] pictureIds = new int[]{
                R.mipmap.tech_park,
                R.mipmap.tech_sfera,
                R.mipmap.logo_track,
                R.mipmap.tech_polis,
                R.mipmap.tech_atom,
                R.mipmap.voronezsch,
                R.mipmap.pensa,
                R.mipmap.p_manager,
                R.mipmap.big_data
        };
        final LoopViewPager viewpager = viewAll.findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(), pictureIds);
        viewpager.setAdapter(adapter);
        viewpager.setPageTransformer(true, new ZoomOutPageTransformer());

        TabLayout tabLayout = viewAll.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewpager, true);

        mAuthViewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);

        mAuthViewModel.getProgress().observe(getViewLifecycleOwner(), new Observer<AuthViewModel.AuthState>() {
            @Override
            public void onChanged(AuthViewModel.AuthState authState) {
                if (authState == AuthViewModel.AuthState.FAILED) {
                    enter.setEnabled(true);
                    enter.setBackgroundColor(getResources().getColor(colorRed));
                    Snackbar.make(viewAll, R.string.error_400, Snackbar.LENGTH_LONG)
                            .show();
                } else if (authState == AuthViewModel.AuthState.FAILED_NET) {
                    enter.setEnabled(true);
                    enter.setBackgroundColor(getResources().getColor(colorOrange));
                    Snackbar.make(viewAll, R.string.http_failed, Snackbar.LENGTH_LONG)
                            .show();
                } else if (authState == AuthViewModel.AuthState.IN_PROGRESS) {
                    enter.setBackgroundColor(getResources().getColor(colorAccent));
                    enter.setEnabled(false);
                    Snackbar.make(viewAll, R.string.load, Snackbar.LENGTH_LONG)
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

        return viewAll;

    }



    public class ViewPagerAdapter extends PagerAdapter {
        private Context mContext;
        private int[] mPictureIDs;
        int[] redpos = new int[]{44, 255, 208, 44, 135, 255, 255, 252, 255};
        int[] greenpos = new int[]{43, 255, 18, 43, 66, 255, 255, 44, 255};
        int[] bluepos = new int[]{41, 255, 22, 41, 221, 255, 255, 56, 255};
        int[] viewpos = new int[]{getResources().getColor(colorWindow),
                getResources().getDrawable(R.drawable.tech_sfera_background).getAlpha(),
                getResources().getColor(colorWindow),
                getResources().getColor(colorWindow),
                getResources().getColor(colorWindow),
                getResources().getColor(colorBlueBackgroungIS),
                getResources().getColor(colorBlueBackgroungIS),
                getResources().getColor(colorWindow),
                getResources().getColor(colorRedMadeAuth)};

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
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.auth_pager_holder, container, false);
            avatarImageView = itemView.findViewById(R.id.imageViewAvatar);
            avatarImageView.setImageResource(mPictureIDs[position]);
            avatarImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            if (mPos - position == 3 || mPos - position == 2 || mPos - position == 1 ||
                    (mPos == 0 && (position == 8 || position == 6)) || (mPos == 2 && position == 8)) {
                int posDown = (position + 1) % mPictureIDs.length;
                if (position == 0) {
                    viewAll.setBackground(getResources().getDrawable(R.drawable.tech_sfera_background));
                } else {
                    viewAll.setBackgroundColor(viewpos[posDown]);
                }
                snowView.setRGB(redpos[posDown], greenpos[posDown], bluepos[posDown]);
            } else {
                int posUp = (position - 1 >= 0) ? position - 1 : 8;
                if (position == 2) {
                    viewAll.setBackground(getResources().getDrawable(R.drawable.tech_sfera_background));
                } else {
                    viewAll.setBackgroundColor(viewpos[posUp]);
                }
                snowView.setRGB(redpos[posUp], greenpos[posUp], bluepos[posUp]);
            }
            mPos = position;
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}