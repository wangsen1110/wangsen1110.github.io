package com.hbmcc.wangsen.netsupport.ui.fragment.forth;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.base.BaseBackFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.first.FirstTabFragment;
import com.hbmcc.wangsen.netsupport.util.HttpUtil;

import java.util.HashMap;

import static com.hbmcc.wangsen.netsupport.base.EnumHttpQ.LOGIN_LOGIN;

public class LoginFragment extends BaseBackFragment {
    private ImageView img_show;
    private Bitmap bitmap;
    private static final String TAG = "AboutFragment";
    private static final String ARG_TITLE = "arg_title";
    private String mTitle;
    public static LoginFragment fragment;

    private Toolbar mToolbar;
    private ImageView imageviewUpdateurl;

    private TextView textLoginAccount;
    private TextView textLoginPassword;
    private Button btnLoginLogin;
    private TextView textLoginTip;


    private final static String MY_ACCOUNT = "Account";
    private final static String MY_loginState = "loginState";
    private final static String MY_PRE_STRING_KEY = "KEY1";
    private final static String MY_PRE_INT_KEY = "KEY2";
    private Gson gson = new Gson();
    private HttpUtil httpUtil = new HttpUtil();
    public SharedPreferences sp = _mActivity.getSharedPreferences(MY_ACCOUNT, _mActivity.MODE_PRIVATE);

    public static LoginFragment newInstance(String title) {
        if (fragment == null) {
            fragment = new LoginFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forth_login, container,
                false);
        initView(view);
        return attachToSwipeBack(view);
    }

    private void initView(View view) {
        mToolbar = view.findViewById(R.id.toolbar);
        textLoginAccount = view.findViewById(R.id.btn_fragment_forth_login_account);
        textLoginPassword = view.findViewById(R.id.btn_fragment_forth_login_password);
        btnLoginLogin = view.findViewById(R.id.btn_fragment_forth_login_login);
        textLoginTip = view.findViewById(R.id.btn_fragment_forth_login_tip);
//        mToolbar.setTitle(mTitle);
//        initToolbarNav(mToolbar);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        initDelayView();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        textLoginAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (textLoginAccount.getText().length() < 2) {
                    Toast.makeText(_mActivity, "用户名输入有误……", Toast.LENGTH_LONG).show();
                }

            }
        });

//        textLoginPassword.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            //输入时的调用
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.d(TAG, "onTextChanged() returned: ");
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//                Log.d(TAG, "onTextChanged() returned: 2222");
//                Toast.makeText(_mActivity, "密码输入错误，请输入11位数的密码", Toast.LENGTH_LONG).show();
//            }
//        });


        btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirstTabFragment.fragment.recentueStatusRecordList.isEmpty()) {
                    Toast.makeText(_mActivity, "请检查当前网络状态", Toast.LENGTH_LONG).show();
                } else {
                    if (btnLoginLogin.getText().length() != 11) {
                        Toast.makeText(_mActivity, "密码输入错误，请输入11位数密码", Toast.LENGTH_LONG).show();
                    }
                    if (textLoginAccount.getText().length() < 2) {
                        Toast.makeText(_mActivity, "用户名输入有误……", Toast.LENGTH_LONG).show();
                    }
                    if (textLoginAccount.getText().length() > 1 && btnLoginLogin.getText().length() != 11) {
                        login();
                    }
                }
            }
        });

    }


    public void login() {
        HashMap<String, Object> jsonmap = new HashMap<String, Object>();
        if (sp.contains("username")) {
            jsonmap.put("username", sp.getString("username", ""));
        } else {
            jsonmap.put("username", textLoginAccount.getText());
        }
        if (sp.contains("userphone")) {
            jsonmap.put("userphone", sp.getString("userphone", ""));
        } else {
            jsonmap.put("userphone", textLoginPassword.getText());
        }
        jsonmap.put("userimsi", FirstTabFragment.fragment.recentueStatusRecordList.get(0).networkStatus.imsi);
        jsonmap.put("userimei", FirstTabFragment.fragment.recentueStatusRecordList.get(0).networkStatus.imei);
        jsonmap.put("usertype", FirstTabFragment.fragment.recentueStatusRecordList.get(0).networkStatus.hardwareModel);

        Toast.makeText(_mActivity, FirstTabFragment.fragment.recentueStatusRecordList.get(0).networkStatus.imsi, Toast.LENGTH_LONG).show();
        String jsonlaln = gson.toJson(jsonmap);
        String url = "http://192.168.1.133:8082/first/login";
        httpUtil.postJsonRequet(jsonlaln, url, LOGIN_LOGIN);
    }

    public void handle() {
        Message msg = new Message();
        mhandler.sendMessage(msg);
    }

    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!HttpUtil.qresult.equals("成功")) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("loginState", true);
                editor.putString("username", (String) textLoginAccount.getText());
                editor.putString("userphone", (String) textLoginPassword.getText());

                boolean loginState = Boolean.parseBoolean(sp.getString("loginState", ""));
                String strPassword = sp.getString("imei", "");

                Toast.makeText(_mActivity, "登录成功", Toast.LENGTH_LONG).show();
                _mActivity.onBackPressed();
            } else {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                editor.putBoolean("loginState", false);
                Toast.makeText(_mActivity, "登录失败，请重新登录", Toast.LENGTH_LONG).show();
            }
        }
    };

    private void initDelayView() {

    }


}


