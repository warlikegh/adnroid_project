package com.example.technoparkmobileproject.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.technoparkmobileproject.R;
import com.example.technoparkmobileproject.SecretData;
import com.example.technoparkmobileproject.auth.AuthActivity;
import com.example.technoparkmobileproject.ui.shedule.ScheduleFragment;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;

public class ProfileFragment extends Fragment {

    private UserProfile mProfile;
    private ProfileViewModel mProfileViewModel;
    static SharedPreferences mSettings;
    static Context context;
    static SharedPreferences.Editor mEditor;
    static String AUTH_TOKEN = "auth_token";
    static String LOGIN = "login";
    static String PASSWORD = "password";
    private static String SITE = "site";
    RecyclerView recycler;
    final MyAdapter adapter = new MyAdapter();
    int id;
    String username;
    Boolean isOther;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        mProfileViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(ProfileViewModel.class);

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
            mProfileViewModel.refresh(username, id);
        }
    }

    public ProfileFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            if (mProfile != null) {
                Glide.with(Objects.requireNonNull(getContext()))
                        .load(mProfile.getAvatarUrl())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.mAva);
                holder.mAva.setVisibility(View.VISIBLE);
                holder.mFullName.setText(mProfile.getFullname());
                holder.mFullName.setVisibility(View.VISIBLE);
                holder.mMainGroup.setText(mProfile.getMainGroup());
                holder.mMainGroup.setVisibility(View.VISIBLE);

                final GroupAdapter groupAdapter = new GroupAdapter();
                groupAdapter.setGroup(mProfile.getSubgroups());
                holder.mGroups.setAdapter(groupAdapter);
                holder.mGroups.setLayoutManager(new LinearLayoutManager(getContext()));
                holder.mGroups.setVisibility(View.VISIBLE);

                holder.mAbout.setText(mProfile.getAbout());
                holder.mAbout.setTextSize(16);
                holder.mAbout.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                holder.mAbout.setVisibility(View.VISIBLE);
                holder.mBirthday.setText(mProfile.getBirthdate());
                holder.mBirthday.setVisibility(View.VISIBLE);
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
                holder.mAbout.setText("Нет соединения");
                holder.mAbout.setTextSize(20);
                holder.mAbout.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
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

        public MyViewHolder(@NonNull View itemView) {
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
                    SharedPreferences mSecretSettings = new SecretData().getSecretData(context);
                    SharedPreferences.Editor mSecretEditor = mSecretSettings.edit();
                    mSecretEditor.remove(LOGIN)
                            .remove(PASSWORD)
                            .remove(AUTH_TOKEN)
                            .remove(SITE)
                            .apply();

                    SharedPreferences mSettings = context.getSharedPreferences("createFirst", Context.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = mSettings.edit();
                    mEditor.putBoolean("isFirstNews", true)
                            .putBoolean("isFirstSchedule", true)
                            .putBoolean("isFirstProfile", true)
                            .apply();

                    context.startActivity(new Intent(context, AuthActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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

    static class GroupViewHolder extends RecyclerView.ViewHolder {

        protected TextView mGroup;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            mGroup = itemView.findViewById(R.id.group_profile);
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
            String string;
            if (group.getName().equals("vkontakte")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vk_logo));
                string = "<a href=\"" + group.getValue() + "\" target=\"_blank\">" + group.getValue() + "</a>";
            }
            if (group.getName().equals("odnoklassniki")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ok_logo));
                string = "<a href=\"" + group.getValue() + "\" target=\"_blank\">" + group.getValue() + "</a>";
            }
            if (group.getName().equals("github")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.github_logo));
                string = "<a href=\"" + group.getValue() + "\" target=\"_blank\">" + group.getValue() + "</a>";
            }
            if (group.getName().equals("facebook")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.facebook_logo));
                string = "<a href=\"" + group.getValue() + "\" target=\"_blank\">" + group.getValue() + "</a>";
            }
            if (group.getName().equals("agent")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.mailru_agent_logo));
                string = "<a href=\"" + group.getValue() + "\" target=\"_blank\">" + group.getValue() + "</a>";
            }
            if (group.getName().equals("telegram")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.telegram_logo));
                string = group.getValue();
            }
            if (group.getName().equals("tamtam")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.tam_tam_logo));
                string = group.getValue();
            }
            if (group.getName().equals("skype")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.skype_logo));
                string = group.getValue();
            }
            if (group.getName().equals("icq")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icq_logo));
                string = group.getValue();
            }
            if (group.getName().equals("bitbucket")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.bitbucket_logo));
                string = group.getValue();
            } else {
                string = group.getValue();
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.mAccount.setText(Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.mAccount.setText(Html.fromHtml(string));
            }
            holder.mAccount.setMovementMethod(LinkMovementMethod.getInstance());
        }

        @Override
        public int getItemCount() {
            return mAccount.size();
        }

    }

    static class AccountViewHolder extends RecyclerView.ViewHolder {

        protected TextView mAccount;
        protected ImageView mImage;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            mAccount = itemView.findViewById(R.id.account);
            mImage = itemView.findViewById(R.id.account_image);
        }
    }

}
