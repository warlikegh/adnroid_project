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
    private static FragmentManager fragmentManager = null;
    static SharedPreferences mSettings;
    static Context context;
    static SharedPreferences.Editor mEditor;
    static String AUTH_TOKEN = "auth_token";
    static String LOGIN = "login";
    static String PASSWORD = "password";
    RecyclerView recycler;
    final MyAdapter adapter = new MyAdapter();
    int id;
    String username;
    Boolean isOther;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        context = getContext();
        mProfileViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(ProfileViewModel.class);

        mSettings = Objects.requireNonNull(getContext()).getSharedPreferences("createFirst", Context.MODE_PRIVATE);
        mEditor = mSettings.edit();

        id = getArguments().getInt("id");
        username = getArguments().getString("username");
        if (id == -1) {
            mProfileViewModel.refreshMe();
            isOther = false;
        } else {
            mProfileViewModel.refresh(username, id);
            isOther = true;
        }
    }

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(Integer id, String username) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("username", username);
        fragment.setArguments(bundle);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        recycler = view.findViewById(R.id.profile);
        recycler.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(linearLayoutManager);


        Observer<UserProfile> observer = new Observer<UserProfile>() {
            @Override
            public void onChanged(UserProfile profile) {
                if (profile != null) {
                    adapter.setProfile(profile);
                    mProfile = profile;
                }
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
                holder.mFullName.setText(mProfile.getFullname());
                holder.mMainGroup.setText(mProfile.getMainGroup());

                final GroupAdapter groupAdapter = new GroupAdapter();
                groupAdapter.setGroup(mProfile.getSubgroups());
                holder.mGroups.setAdapter(groupAdapter);
                holder.mGroups.setLayoutManager(new LinearLayoutManager(getContext()));

                holder.mAbout.setText(mProfile.getAbout());
                holder.mBirthday.setText(mProfile.getBirthdate());
                holder.mPhone.setText(mProfile.getContacts().get(0).getValue());
                holder.mMail.setText(mProfile.getContacts().get(1).getValue());

                final AccountAdapter accountAdapter = new AccountAdapter();
                accountAdapter.setGroup(mProfile.getAccounts());
                holder.mAccounts.setAdapter(accountAdapter);
                holder.mAccounts.setLayoutManager(new LinearLayoutManager(getContext()));

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
                            .apply();
                    context.startActivity(new Intent(context, AuthActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }));
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
