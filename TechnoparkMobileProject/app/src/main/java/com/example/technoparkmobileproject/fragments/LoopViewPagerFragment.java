package com.example.technoparkmobileproject.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.example.technoparkmobileproject.R;
import com.imbryk.viewPager.LoopViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import me.relex.circleindicator.CircleIndicator;

public class LoopViewPagerFragment extends Fragment {
    private static final String TAG = "Current page: ";
    int mg =1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loop_view_pager_fragment, container, false);
    }



    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final LoopViewPager viewpager = view.findViewById(R.id.viewpager);
        CircleIndicator indicator = view.findViewById(R.id.indicator);
        viewpager.setAdapter(new AuthPagerAdapter());
        indicator.setViewPager(viewpager);
        Button button = view.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mg = viewpager.getCurrentItem();
                Log.d(TAG,""+ mg);
            }
        });
    }

    public class AuthPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mThumbIds.length;
        }
        @Override public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(@NonNull ViewGroup view, int position, @NonNull Object object) {
            view.removeView((View) object);
        }
        @NonNull @Override public Object instantiateItem(@NonNull ViewGroup view, int position) {

            ImageView imageView = new ImageView(view.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setImageResource(mThumbIds[position]);
            view.addView(imageView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

           return imageView;

        }

        public	Integer[] mThumbIds = {
                R.drawable.techpark,
                R.drawable.techsphere,
                R.drawable.voronezh };

    }
}
