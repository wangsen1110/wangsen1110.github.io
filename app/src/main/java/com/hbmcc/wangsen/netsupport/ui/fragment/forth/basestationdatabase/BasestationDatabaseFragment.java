package com.hbmcc.wangsen.netsupport.ui.fragment.forth.basestationdatabase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.adapter.BasestationDatabaseFragmentAdapter;
import com.hbmcc.wangsen.netsupport.base.BaseBackFragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasestationDatabaseFragment extends BaseBackFragment {

    private static final String TAG = "BasestationDatabaseFrag";
    ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

    private static final String ARG_TITLE = "arg_title";
    private String mTitle;
    private TabLayout mTab;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private Button btnFragmentBasestionDatabaseImportDatabase;
    private long startTime; //起始时间
    private long endTime;//结束时间

    public static BasestationDatabaseFragment newInstance(String title) {
        BasestationDatabaseFragment fragment = new BasestationDatabaseFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString(ARG_TITLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basestation_database, container,
                false);
        initView(view);
        return attachToSwipeBack(view);
    }

    private void initView(View view) {
        mToolbar = view.findViewById(R.id.toolbar);
        mTab = view.findViewById(R.id.tab_fragment_basestastion_database);
        mViewPager = view.findViewById(R.id.viewpager_fragment_basestastion_database);
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mToolbar.setTitle(mTitle);
        initToolbarNav(mToolbar);
    }

    /**
     * 这里演示:
     * 比较复杂的Fragment页面会在第一次start时,导致动画卡顿
     * Fragmentation提供了onEnterAnimationEnd()方法,该方法会在 入栈动画 结束时回调
     * 所以在onCreateView进行一些简单的View初始化(比如 toolbar设置标题,返回按钮; 显示加载数据的进度条等),
     * 然后在onEnterAnimationEnd()方法里进行 复杂的耗时的初始化 (比如FragmentPagerAdapter的初始化 加载数据等)
     */
    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        initDelayView();
    }

    private void initDelayView() {
        mViewPager.setAdapter(new BasestationDatabaseFragmentAdapter(getChildFragmentManager()
                , "4G", "5G"));
        mTab.setupWithViewPager(mViewPager);
    }
}
