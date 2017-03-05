package com.example.huanxintest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/5.
 */
public class ContactActivity extends AppCompatActivity implements EaseContactListFragment.EaseContactListItemClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        String username = getIntent().getStringExtra("username");
        setTitle(username);
        setContentView(R.layout.activity_contact);
        initFragment();
    }

    List<String> usernames = new ArrayList<>();

    private void initFragment() {
        new Thread() {
            @Override
            public void run() {
                try {
                    List<String> list = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    if (list != null)
                        usernames.addAll(list);
                    handler.sendEmptyMessage(1);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                Log.e("TAG", usernames.toString());
            }
        }.start();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    EaseContactListFragment fragment = new EaseContactListFragment();
                    Map<String, EaseUser> map = new HashMap<>();
                    for (int i = 0; i < usernames.size(); i++) {
                        EaseUser user = new EaseUser(usernames.get(i));
                        user.setNickname("张三");
                        user.setAvatar("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
                        map.put(i + "", user);
                    }
                    fragment.setContactsMap(map);
                    fragment.setContactListItemClickListener(ContactActivity.this);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fl_content, fragment);
                    ft.commit();
                    break;
            }
        }
    };

    @Override
    public void onListItemClicked(EaseUser user) {
        EaseChatFragment fragment = new EaseChatFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        bundle.putString(EaseConstant.EXTRA_USER_ID, user.getUsername());
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        initFragment();
    }
}
