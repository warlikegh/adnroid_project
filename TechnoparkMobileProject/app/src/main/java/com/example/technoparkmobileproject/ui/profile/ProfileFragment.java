package com.example.technoparkmobileproject.ui.profile;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.widget.ProgressBar;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.VIBRATOR_SERVICE;
import static android.view.View.GONE;

public class ProfileFragment extends Fragment {
    static GroupAdapter groupAdapter;
    private UserProfile mProfile;
    private ProfileRepo.ProfileProgress mProfileProgress;
    private ProfileViewModel mProfileViewModel;
    static SharedPreferences mSettings;
    static Context context;
    static SharedPreferences.Editor mEditor;

    RecyclerView recycler;
    int id;
    String username;
    Boolean isOther;
    static FragmentManager fragmentManager;

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
    protected ProgressBar mProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(getLogTag(), "onCreate");
        super.onCreate(savedInstanceState);
        context = getContext();
        mProfileViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(ProfileViewModel.class);
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
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mProgressBar = view.findViewById(R.id.progress_bar);
        mAva = view.findViewById(R.id.photo);
        mFullName = view.findViewById(R.id.full_name);
        mMainGroup = view.findViewById(R.id.main_group);
        mGroups = view.findViewById(R.id.subgroups);
        mAbout = view.findViewById(R.id.about);
        mBirthday = view.findViewById(R.id.birthday);
        mPhone = view.findViewById(R.id.phone);
        mMail = view.findViewById(R.id.mail);
        mAccounts = view.findViewById(R.id.accounts_recycler);

        mButton = view.findViewById(R.id.logOut);
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
                clipAndVibrate(view, mPhone);
                return false;
            }
        }));

        mMail.setOnLongClickListener((new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clipAndVibrate(view, mMail);
                return false;
            }
        }));

        mBirthdayImage = view.findViewById(R.id.birthday_image);
        mPhoneImage = view.findViewById(R.id.phone_image);
        mMailImage = view.findViewById(R.id.mail_image);
        mAboutString = view.findViewById(R.id.about_string);
        mGroupsString = view.findViewById(R.id.groups);
        mContactsString = view.findViewById(R.id.contacts_string);
        mAccountsString = view.findViewById(R.id.accounts_string);

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

        Observer<UserProfile> observer = new Observer<UserProfile>() {
            @Override
            public void onChanged(UserProfile profile) {
                mProfile = profile;
                if (mProfile != null) {
                    mProgressBar.setVisibility(GONE);
                    Glide.with(Objects.requireNonNull(getContext()))
                            .load(mProfile.getAvatarUrl())
                            .placeholder(R.mipmap.profile)
                            .apply(RequestOptions.circleCropTransform())
                            .into(mAva);
                    mAva.setVisibility(View.VISIBLE);
                    mFullName.setText(mProfile.getFullname());
                    mFullName.setVisibility(View.VISIBLE);
                    mFullName.setTextIsSelectable(true);
                    mMainGroup.setText(mProfile.getMainGroup());
                    mMainGroup.setVisibility(View.VISIBLE);
                    mMainGroup.setTextIsSelectable(true);
                    groupAdapter = new GroupAdapter();
                    groupAdapter.setGroup(mProfile.getSubgroups());
                    mGroups.setAdapter(groupAdapter);
                    mGroups.setLayoutManager(new LinearLayoutManager(getContext()));
                    mGroups.setVisibility(View.VISIBLE);

                    mAbout.setTextSize(16);
                    mAbout.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    mAbout.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mAbout.setText(Html.fromHtml(mProfile.getAbout(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        mAbout.setText(Html.fromHtml(mProfile.getAbout()));
                    }
                    mAbout.setMovementMethod(LinkMovementMethod.getInstance());
                    mAbout.setTextIsSelectable(true);


                    mBirthday.setText(mProfile.getBirthdate());
                    mBirthday.setVisibility(View.VISIBLE);
                    mBirthday.setTextIsSelectable(true);

                    mPhone.setText(mProfile.getContacts().get(0).getValue());
                    mPhone.setVisibility(View.VISIBLE);

                    mMail.setText(mProfile.getContacts().get(1).getValue());
                    mMail.setVisibility(View.VISIBLE);

                    final AccountAdapter accountAdapter = new AccountAdapter();
                    accountAdapter.setGroup(mProfile.getAccounts());
                    mAccounts.setAdapter(accountAdapter);
                    mAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
                    mAccounts.setVisibility(View.VISIBLE);

                    mBirthdayImage.setVisibility(View.VISIBLE);
                    mPhoneImage.setVisibility(View.VISIBLE);
                    mMailImage.setVisibility(View.VISIBLE);
                    mAboutString.setVisibility(View.VISIBLE);
                    mContactsString.setVisibility(View.VISIBLE);
                    mGroupsString.setVisibility(View.VISIBLE);
                    mAccountsString.setVisibility(View.VISIBLE);

                }
                if (isOther) {
                    mButton.setVisibility(GONE);
                } else {
                    mButton.setVisibility(View.VISIBLE);
                }
            }
        };

        Observer<ProfileRepo.ProfileProgress> observerProgress = new Observer<ProfileRepo.ProfileProgress>() {
            @Override
            public void onChanged(ProfileRepo.ProfileProgress profileProgress) {
                if (profileProgress != null) {
                    mProfileProgress = profileProgress;
                    if (mProfileProgress == ProfileRepo.ProfileProgress.FAILED_NET) {
                        visibleGone();
                        mProgressBar.setVisibility(GONE);
                        mAbout.setText(R.string.http_failed);
                    } else if (mProfileProgress == ProfileRepo.ProfileProgress.IN_PROGRESS) {
                        visibleGone();
                        mAbout.setVisibility(GONE);
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        mProfileViewModel
                .getProfile()
                .observe(getViewLifecycleOwner(), observer);

        mProfileViewModel
                .getProfileProgress()
                .observe(getViewLifecycleOwner(), observerProgress);

        return view;
    }

    void visibleGone() {
        mAva.setVisibility(View.INVISIBLE);
        mFullName.setVisibility(GONE);
        mMainGroup.setVisibility(GONE);
        mBirthday.setVisibility(GONE);
        mGroups.setVisibility(GONE);
        mPhone.setVisibility(GONE);
        mMail.setVisibility(GONE);
        mAccounts.setVisibility(GONE);

        mBirthdayImage.setVisibility(GONE);
        mPhoneImage.setVisibility(GONE);
        mMailImage.setVisibility(GONE);
        mAboutString.setVisibility(GONE);
        mContactsString.setVisibility(GONE);
        mGroupsString.setVisibility(GONE);
        mAccountsString.setVisibility(GONE);

        mAbout.setVisibility(View.VISIBLE);
        mAbout.setTextSize(20);
        mAbout.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    void clipAndVibrate(View view, TextView textView) {
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(150);
        }

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", textView.getText());
        clipboard.setPrimaryClip(clip);

        Snackbar.make(view, R.string.copied, Snackbar.LENGTH_LONG)
                .show();
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


    protected String getLogTag() {
        return getClass().getSimpleName();
    }
}
