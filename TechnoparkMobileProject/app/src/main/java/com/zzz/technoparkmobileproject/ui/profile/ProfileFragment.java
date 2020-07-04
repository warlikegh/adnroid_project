package com.zzz.technoparkmobileproject.ui.profile;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import com.zzz.technoparkmobileproject.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zzz.technoparkmobileproject.Router;
import com.zzz.technoparkmobileproject.SecretData;
import com.zzz.technoparkmobileproject.network.ApiRepo;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING;
import static android.view.HapticFeedbackConstants.KEYBOARD_TAP;
import static android.view.View.GONE;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.CREATE_FIRST_SETTINGS;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.IS_FIRST_PROFILE;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.PROFILE_ID;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.PROFILE_PATH_URL;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.PROFILE_USERNAME;
import static com.zzz.technoparkmobileproject.TechnoparkApplication.SITE;

public class ProfileFragment extends Fragment {
    static GroupAdapter groupAdapter;
    private UserProfile mProfile;
    private ProfileRepo.ProfileProgress mProfileProgress;
    private ProfileViewModel mProfileViewModel;
    static SharedPreferences mSettings;
    static Context context;
    static SharedPreferences.Editor mEditor;

    String username = "";
    int id;
    Boolean isOther;
    static FragmentManager fragmentManager;

    int backgroundNumber = 0;

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

    protected TextView mBirthdayString;
    protected TextView mPhoneString;
    protected TextView mMailString;
    protected TextView mAboutString;
    protected TextView mGroupsString;
    protected TextView mContactsString;
    protected TextView mAccountsString;
    protected ProgressBar mProgressBar;
    protected View mSeparator1;
    protected View mSeparator2;
    protected View mSeparator3;
    protected RelativeLayout birthdayLayout;
    protected RelativeLayout aboutLayout;
    protected RelativeLayout contactsLayout;
    protected RelativeLayout phoneLayout;
    protected RelativeLayout mailLayout;
    protected RelativeLayout groupLayout;
    protected RelativeLayout accountLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        mProfileViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()))
                .get(ProfileViewModel.class);
        fragmentManager = getChildFragmentManager();
    }

    @Override
    public void onStart() {
        super.onStart();
        mSettings = Objects.requireNonNull(getContext()).getSharedPreferences(CREATE_FIRST_SETTINGS, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();

        id = getArguments().getInt(PROFILE_ID);
        username = getArguments().getString(PROFILE_USERNAME);
        if (id == -1) {
            if (mSettings.getBoolean(IS_FIRST_PROFILE, true)) {
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
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final SharedPreferences mSecretSettings = new SecretData().getSecretData(getContext());

        mSeparator1 = view.findViewById(R.id.separator1);
        mSeparator2 = view.findViewById(R.id.separator2);
        mSeparator3 = view.findViewById(R.id.separator3);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mAva = view.findViewById(R.id.photo);
        mFullName = view.findViewById(R.id.full_name);
        mFullName.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = mSecretSettings.getInt(SITE, 0);
                String base = ApiRepo.from(getContext()).getBaseURL(pos);
                String baseSite = base.substring(0, base.indexOf('/', 9) + 1);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(baseSite + PROFILE_PATH_URL + username + "/"));
                startActivity(browserIntent);
            }
        }));
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

        mBirthdayString = view.findViewById(R.id.birthday_string);
        mPhoneString = view.findViewById(R.id.phone_string);
        mMailString = view.findViewById(R.id.mail_string);
        mAboutString = view.findViewById(R.id.about_string);
        mGroupsString = view.findViewById(R.id.groups);
        mContactsString = view.findViewById(R.id.contacts_string);
        mAccountsString = view.findViewById(R.id.accounts_string);

        birthdayLayout = view.findViewById(R.id.birthday_layout);
        aboutLayout = view.findViewById(R.id.about_layout);
        contactsLayout = view.findViewById(R.id.contacts_layout);
        phoneLayout = view.findViewById(R.id.phone_layout);
        mailLayout = view.findViewById(R.id.mail_layout);
        groupLayout = view.findViewById(R.id.group_layout);
        accountLayout = view.findViewById(R.id.accounts_layout);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorAccent, null), getResources().getColor(R.color.colorBlueBackgroungIS, null),
                getResources().getColor(R.color.colorGradientBottomAuth, null), getResources().getColor(R.color.colorGradientTopAuth, null));
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                assert getArguments() != null;
                id = getArguments().getInt(PROFILE_ID);
                username = getArguments().getString(PROFILE_USERNAME);
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
                    username = mProfile.getUsername();
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
                    if (mProfile.getAbout() != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            mAbout.setText(Html.fromHtml(mProfile.getAbout(), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            mAbout.setText(Html.fromHtml(mProfile.getAbout()));
                        }
                        mAbout.setMovementMethod(LinkMovementMethod.getInstance());
                    } else
                        mAbout.setText(mProfile.getAbout());
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

                    mSeparator1.setVisibility(View.VISIBLE);
                    mSeparator2.setVisibility(View.VISIBLE);
                    mSeparator3.setVisibility(View.VISIBLE);
                    mBirthdayString.setVisibility(View.VISIBLE);
                    mPhoneString.setVisibility(View.VISIBLE);
                    mMailString.setVisibility(View.VISIBLE);
                    mAboutString.setVisibility(View.VISIBLE);
                    mContactsString.setVisibility(View.VISIBLE);
                    mGroupsString.setVisibility(View.VISIBLE);
                    mAccountsString.setVisibility(View.VISIBLE);

                    if (mProfile.getBirthdate() == null)
                        birthdayLayout.setVisibility(GONE);
                    else
                        birthdayLayout.setVisibility(View.VISIBLE);
                    if (mProfile.getAbout() == null)
                        aboutLayout.setVisibility(GONE);
                    else if (mProfile.getAbout().equals("")) {
                        aboutLayout.setVisibility(GONE);
                    } else {
                        aboutLayout.setVisibility(View.VISIBLE);
                    }
                    if (mProfile.getSubgroups().size() == 0)
                        groupLayout.setVisibility(GONE);
                    else
                        groupLayout.setVisibility(View.VISIBLE);
                    if (mProfile.getAccounts().size() == 0)
                        accountLayout.setVisibility(GONE);
                    else
                        accountLayout.setVisibility(View.VISIBLE);
                    if ((mProfile.getContacts().get(0).getValue().equals("") || mProfile.getContacts().get(0).getValue().equals(" ")) &&
                            (mProfile.getContacts().get(1).getValue().equals("") || mProfile.getContacts().get(1).getValue().equals(" "))) {
                        contactsLayout.setVisibility(GONE);
                    } else {
                        contactsLayout.setVisibility(View.VISIBLE);
                        if (mProfile.getContacts().get(0).getValue().equals("") || mProfile.getContacts().get(0).getValue().equals(" "))
                            phoneLayout.setVisibility(GONE);
                        else
                            phoneLayout.setVisibility(View.VISIBLE);
                        if (mProfile.getContacts().get(1).getValue().equals("") || mProfile.getContacts().get(1).getValue().equals(" "))
                            mailLayout.setVisibility(GONE);
                        else
                            mailLayout.setVisibility(View.VISIBLE);
                    }
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
        mSeparator1.setVisibility(GONE);
        mSeparator2.setVisibility(GONE);
        mSeparator3.setVisibility(GONE);
        mAva.setVisibility(View.INVISIBLE);
        mFullName.setVisibility(GONE);
        mMainGroup.setVisibility(GONE);
        mBirthday.setVisibility(GONE);
        mGroups.setVisibility(GONE);
        mPhone.setVisibility(GONE);
        mMail.setVisibility(GONE);
        mAccounts.setVisibility(GONE);

        mBirthdayString.setVisibility(GONE);
        mPhoneString.setVisibility(GONE);
        mMailString.setVisibility(GONE);
        mAboutString.setVisibility(GONE);
        mContactsString.setVisibility(GONE);
        mGroupsString.setVisibility(GONE);
        mAccountsString.setVisibility(GONE);

        mAbout.setVisibility(View.VISIBLE);
        mAbout.setTextSize(20);
        mAbout.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    private void clipAndVibrate(View view, TextView textView) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", textView.getText());
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);

        Snackbar.make(view, R.string.copied, Snackbar.LENGTH_LONG)
                .show();

        view.performHapticFeedback(KEYBOARD_TAP, FLAG_IGNORE_GLOBAL_SETTING);
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
            final UserProfile.Account account = mAccount.get(position);
            if (account.getName().equals("vkontakte")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vk_logo));
            }
            if (account.getName().equals("odnoklassniki")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ok_logo));
            }
            if (account.getName().equals("github")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.github_logo));
            }
            if (account.getName().equals("facebook")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.facebook_logo));
            }
            if (account.getName().equals("agent")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.mailru_agent_logo));
            }
            if (account.getName().equals("telegram")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.telegram_logo));
            }
            if (account.getName().equals("tamtam")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.tam_tam_logo));
            }
            if (account.getName().equals("skype")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.skype_logo));
            }
            if (account.getName().equals("icq")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icq_logo));
            }
            if (account.getName().equals("bitbucket")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.bitbucket_logo));
            }
            if (account.getName().equals("myworld")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.my_world_logo));
            }
            if (account.getName().equals("linkedin")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.linkedin_logo));
            }
            if (account.getName().equals("jabber")) {
                holder.mImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.jabber_logo));
            }

            holder.mAccount.setText(account.getValue());

            if (position == mAccount.size() - 1)
                holder.mSeparator.setVisibility(GONE);

        }

        @Override
        public int getItemCount() {
            return mAccount.size();
        }

    }

    class AccountViewHolder extends RecyclerView.ViewHolder {

        protected TextView mAccount;
        protected ImageView mImage;
        protected View mSeparator;

        public AccountViewHolder(@NonNull final View itemView) {
            super(itemView);
            mAccount = itemView.findViewById(R.id.account);
            mImage = itemView.findViewById(R.id.account_image);
            mSeparator = itemView.findViewById(R.id.separator1);

            mAccount.setOnLongClickListener((new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clipAndVibrate(v, AccountViewHolder.this.mAccount);
                    return true;
                }
            }));
        }

    }


}
