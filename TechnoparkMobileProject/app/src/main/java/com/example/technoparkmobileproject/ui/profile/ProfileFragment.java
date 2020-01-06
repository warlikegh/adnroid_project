package com.example.technoparkmobileproject.ui.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.technoparkmobileproject.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.technoparkmobileproject.Router;
import com.example.technoparkmobileproject.SecretData;
import com.example.technoparkmobileproject.auth.AuthActivity;
import com.example.technoparkmobileproject.ui.shedule.ScheduleViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.VIBRATOR_SERVICE;
import static android.view.View.GONE;

public class ProfileFragment extends Fragment {
    static GroupAdapter groupAdapter;
    private UserProfile mProfile;
    private static ProfileViewModel mProfileViewModel;
    private static ScheduleViewModel mScheduleViewModel;
    static SharedPreferences mSettings;
    static Context context;
    static SharedPreferences.Editor mEditor;

    RecyclerView recycler;
    final MyAdapter adapter = new MyAdapter();
    int id;
    String username;
    Boolean isOther;
    static FragmentManager fragmentManager;

    public static ProfileFragment newInstance(int id, String username) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void del() {
        adapter.cleanProfile();
        Log.d(getLogTag(), "adapter.setProfile(null);");
    }

    @Override
    public void onDestroyView() {
        del();
        super.onDestroyView();
        Log.d(getLogTag(), "onDestroyView");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(getLogTag(), "onCreate");
        super.onCreate(savedInstanceState);
        context = getContext();
        mProfileViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(ProfileViewModel.class);
        mScheduleViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(ScheduleViewModel.class);
        fragmentManager = getChildFragmentManager();

    }

    @Override
    public void onStart() {
        Log.d(getLogTag(), "onStart");
        super.onStart();
        mSettings = Objects.requireNonNull(getContext()).getSharedPreferences("createFirst", Context.MODE_PRIVATE);
        mEditor = mSettings.edit();

        id = getArguments().getInt("id");
        username = getArguments().getString("username");
        if (id == -1) {
            if (mSettings.getBoolean("isFirstProfile", true)) {
                mProfileViewModel.refreshMe();
            } else {
                mProfileViewModel.pullMeFromDB();
            }
            isOther = false;
        } else {
            isOther = true;
            mProfileViewModel.pullFromDB(id, username);
        }
    }

    public ProfileFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d(getLogTag(), "onCreateView");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                assert getArguments() != null;
                id = getArguments().getInt("id");
                username = getArguments().getString("username");
                if (id == -1) {
                    mProfileViewModel.refreshMe();
                    isOther = false;
                } else {
                    isOther = true;
                    mProfileViewModel.refresh(username, id);
                }
                pullToRefresh.setRefreshing(false);
            }
        });
        recycler = view.findViewById(R.id.profile);
        recycler.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(linearLayoutManager);


        Observer<UserProfile> observer = new Observer<UserProfile>() {
            @Override
            public void onChanged(UserProfile profile) {
                adapter.setProfile(profile);
                mProfile = profile;
            }
        };

        mProfileViewModel
                .getProfile()
                .observe(getViewLifecycleOwner(), observer);


        return view;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private UserProfile mProfile;

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_profile, parent, false));
        }

        public void setProfile(UserProfile profile) {
            mProfile = profile;
            notifyDataSetChanged();
        }

        public void cleanProfile() {
            mProfile = null;
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            if (mProfile != null) {
                Glide.with(Objects.requireNonNull(getContext()))
                        .load(mProfile.getAvatarUrl())
                        .placeholder(R.mipmap.profile)
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.mAva);
                holder.mAva.setVisibility(View.VISIBLE);
                holder.mFullName.setText(mProfile.getFullname());
                holder.mFullName.setVisibility(View.VISIBLE);
                holder.mFullName.setTextIsSelectable(true);
                holder.mMainGroup.setText(mProfile.getMainGroup());
                holder.mMainGroup.setVisibility(View.VISIBLE);
                holder.mMainGroup.setTextIsSelectable(true);
                groupAdapter = new GroupAdapter();
                groupAdapter.setGroup(mProfile.getSubgroups());
                holder.mGroups.setAdapter(groupAdapter);
                holder.mGroups.setLayoutManager(new LinearLayoutManager(getContext()));
                holder.mGroups.setVisibility(View.VISIBLE);

                //holder.mAbout.setText(mProfile.getAbout());
                holder.mAbout.setTextSize(16);
                holder.mAbout.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                holder.mAbout.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.mAbout.setText(Html.fromHtml(mProfile.getAbout(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.mAbout.setText(Html.fromHtml(mProfile.getAbout()));
                }
                holder.mAbout.setMovementMethod(LinkMovementMethod.getInstance());


                holder.mBirthday.setText(mProfile.getBirthdate());
                holder.mBirthday.setVisibility(View.VISIBLE);
                holder.mBirthday.setTextIsSelectable(true);

                holder.mPhone.setText(mProfile.getContacts().get(0).getValue());
                holder.mPhone.setVisibility(View.VISIBLE);

                holder.mMail.setText(mProfile.getContacts().get(1).getValue());
                holder.mMail.setVisibility(View.VISIBLE);

                final AccountAdapter accountAdapter = new AccountAdapter();
                accountAdapter.setGroup(mProfile.getAccounts());
                holder.mAccounts.setAdapter(accountAdapter);
                holder.mAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
                holder.mAccounts.setVisibility(View.VISIBLE);

                if (isOther) {
                    holder.mButton.setVisibility(GONE);
                } else {
                    holder.mButton.setVisibility(View.VISIBLE);
                }

                holder.mBirthdayImage.setVisibility(View.VISIBLE);
                holder.mPhoneImage.setVisibility(View.VISIBLE);
                holder.mMailImage.setVisibility(View.VISIBLE);
                holder.mAboutString.setVisibility(View.VISIBLE);
                holder.mContactsString.setVisibility(View.VISIBLE);
                holder.mGroupsString.setVisibility(View.VISIBLE);
                holder.mAccountsString.setVisibility(View.VISIBLE);

            } else {
                holder.mAva.setVisibility(View.INVISIBLE);
                holder.mFullName.setVisibility(GONE);
                holder.mMainGroup.setVisibility(GONE);
                holder.mBirthday.setVisibility(GONE);
                holder.mGroups.setVisibility(GONE);
                holder.mPhone.setVisibility(GONE);
                holder.mMail.setVisibility(GONE);
                holder.mAccounts.setVisibility(GONE);

                holder.mBirthdayImage.setVisibility(GONE);
                holder.mPhoneImage.setVisibility(GONE);
                holder.mMailImage.setVisibility(GONE);
                holder.mAboutString.setVisibility(GONE);
                holder.mContactsString.setVisibility(GONE);
                holder.mGroupsString.setVisibility(GONE);
                holder.mAccountsString.setVisibility(GONE);

                holder.mAbout.setVisibility(View.VISIBLE);
                holder.mAbout.setText(R.string.http_failed);
                holder.mAbout.setTextSize(20);
                holder.mAbout.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                if (isOther) {
                    holder.mButton.setVisibility(GONE);
                } else {
                    holder.mButton.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mAva;
        protected TextView mFullName;
        protected TextView mMainGroup;
        protected RecyclerView mGroups;
        protected TextView mAbout;
        protected TextView mBirthday;
        protected TextView mPhone;
        protected TextView mMail;
        protected RecyclerView mAccounts;
        protected Button mButton;

        protected ImageView mBirthdayImage;
        protected ImageView mPhoneImage;
        protected ImageView mMailImage;
        protected TextView mAboutString;
        protected TextView mGroupsString;
        protected TextView mContactsString;
        protected TextView mAccountsString;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            mAva = itemView.findViewById(R.id.photo);
            mFullName = itemView.findViewById(R.id.full_name);
            mMainGroup = itemView.findViewById(R.id.main_group);
            mGroups = itemView.findViewById(R.id.subgroups);
            mAbout = itemView.findViewById(R.id.about);
            mBirthday = itemView.findViewById(R.id.birthday);
            mPhone = itemView.findViewById(R.id.phone);
            mMail = itemView.findViewById(R.id.mail);
            mAccounts = itemView.findViewById(R.id.accounts_recycler);

            mButton = itemView.findViewById(R.id.logOut);
            mButton.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog dialog = new DialogLogOut().getDialog(context);
                    dialog.show();
                }
            }));

            mPhone.setOnLongClickListener((new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(150);
                    }

                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("", MyViewHolder.this.mPhone.getText());
                    clipboard.setPrimaryClip(clip);

                    Snackbar.make(itemView, R.string.copied, Snackbar.LENGTH_LONG)
                            .show();
                    return false;
                }
            }));

            mMail.setOnLongClickListener((new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(150);
                    }

                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("", MyViewHolder.this.mMail.getText());
                    clipboard.setPrimaryClip(clip);

                    Snackbar.make(itemView, R.string.copied, Snackbar.LENGTH_LONG)
                            .show();
                    return false;
                }
            }));

            mBirthdayImage = itemView.findViewById(R.id.birthday_image);
            mPhoneImage = itemView.findViewById(R.id.phone_image);
            mMailImage = itemView.findViewById(R.id.mail_image);
            mAboutString = itemView.findViewById(R.id.about_string);
            mGroupsString = itemView.findViewById(R.id.groups);
            mContactsString = itemView.findViewById(R.id.contacts_string);
            mAccountsString = itemView.findViewById(R.id.accounts_string);
        }

    }


    private class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {

        private List<UserProfile.Subgroup> mGroup = new ArrayList<>();

        public void setGroup(List<UserProfile.Subgroup> group) {
            mGroup = group;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new GroupViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_group, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
            final UserProfile.Subgroup group = mGroup.get(position);
            holder.mGroup.setText(group.getName());
        }

        @Override
        public int getItemCount() {
            return mGroup.size();
        }

    }

    class GroupViewHolder extends RecyclerView.ViewHolder {

        protected TextView mGroup;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            mGroup = itemView.findViewById(R.id.group_profile);
            mGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = GroupViewHolder.this.getAdapterPosition();
                    Integer id = groupAdapter.mGroup.get(pos).getId();
                    ((Router) context).onGroupSelected(id);
                }
            });
        }
    }


    private class AccountAdapter extends RecyclerView.Adapter<AccountViewHolder> {

        private List<UserProfile.Account> mAccount = new ArrayList<>();

        public void setGroup(List<UserProfile.Account> group) {
            mAccount = group;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AccountViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.account_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
            final UserProfile.Account group = mAccount.get(position);
            if (group.getName().equals("vkontakte")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vk_logo));
            }
            if (group.getName().equals("odnoklassniki")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ok_logo));
            }
            if (group.getName().equals("github")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.github_logo));
            }
            if (group.getName().equals("facebook")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.facebook_logo));
            }
            if (group.getName().equals("agent")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.mailru_agent_logo));
            }
            if (group.getName().equals("telegram")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.telegram_logo));
            }
            if (group.getName().equals("tamtam")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.tam_tam_logo));
            }
            if (group.getName().equals("skype")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.skype_logo));
            }
            if (group.getName().equals("icq")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icq_logo));
            }
            if (group.getName().equals("bitbucket")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.bitbucket_logo));
            }
            if (group.getName().equals("myworld")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.my_world_logo));
            }
            if (group.getName().equals("linkedin")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.linkedin_logo));
            }
            if (group.getName().equals("jabber")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.jabber_logo));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.mAccount.setText(Html.fromHtml(group.getValue(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.mAccount.setText(Html.fromHtml(group.getValue()));
            }
            holder.mAccount.setMovementMethod(LinkMovementMethod.getInstance());

        }

        @Override
        public int getItemCount() {
            return mAccount.size();
        }

    }

    class AccountViewHolder extends RecyclerView.ViewHolder {

        protected TextView mAccount;
        protected ImageView mImage;

        public AccountViewHolder(@NonNull final View itemView) {
            super(itemView);
            mAccount = itemView.findViewById(R.id.account);
            mImage = itemView.findViewById(R.id.account_image);

            mAccount.setOnLongClickListener((new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(150);
                    }

                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("", AccountViewHolder.this.mAccount.getText());
                    clipboard.setPrimaryClip(clip);

                    Snackbar.make(itemView, R.string.copied, Snackbar.LENGTH_LONG)
                            .show();

                    return false;
                }
            }));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(getLogTag(), "onAttach");
        adapter.setProfile(null);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(getLogTag(), "onActivityCreated");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(getLogTag(), "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(getLogTag(), "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getLogTag(), "onStop");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(getLogTag(), "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(getLogTag(), "onDetach");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(getLogTag(), "onSaveInstanceState");
    }

    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(getLogTag(), "onViewStateRestored");
    }

    protected String getLogTag() {
        return getClass().getSimpleName();
    }
}
