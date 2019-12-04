package com.hbmcc.wangsen.netsupport.ui.fragment.forth;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbmcc.wangsen.netsupport.App;
import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.base.BaseBackFragment;
import com.loveplusplus.update.AppUtils;
import com.loveplusplus.update.UpdateChecker;


public class AboutFragment extends BaseBackFragment {
    private ImageView img_show;
    private Bitmap bitmap;
    private static final String TAG = "AboutFragment";
    private static final String ARG_TITLE = "arg_title";
    private String mTitle;
    private static AboutFragment fragment;

    private Toolbar mToolbar;
    private ImageView imageviewUpdateurl;

    private TextView textViewFragmentAboutCurrentVersion;

    public static AboutFragment newInstance(String title) {
        if (fragment == null){
            fragment = new AboutFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container,
                false);
        initView(view);
        return attachToSwipeBack(view);
    }

    private void initView(View view) {
        mToolbar = view.findViewById(R.id.toolbar);
        imageviewUpdateurl = view.findViewById(R.id.imageview_updateurl);
        textViewFragmentAboutCurrentVersion = view.findViewById(R.id.textView_fragment_about_currentVersion);
        mToolbar.setTitle(mTitle);
        initToolbarNav(mToolbar);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        initDelayView();
    }

    private void initDelayView() {
        imageviewUpdateurl.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri uri = Uri.parse(App.getContext().getString(R.string.newerVersionDownloadUrl));
                intent.setData(uri);
                startActivity(intent);
            }
        });

        imageviewUpdateurl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) _mActivity.getSystemService(_mActivity.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", App.getContext().getString(R.string.newerVersionDownloadUrl));
                cm.setPrimaryClip(mClipData);
                Toast.makeText(_mActivity, "下载链接已复制", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        textViewFragmentAboutCurrentVersion.setText(AppUtils.getVersionName(_mActivity));
        UpdateChecker.checkForDialog(_mActivity, App.getContext().getString(R.string.autoUpdateJsonUrl));
    }
}


