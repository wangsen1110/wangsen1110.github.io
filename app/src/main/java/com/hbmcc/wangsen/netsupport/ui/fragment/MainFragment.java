package com.hbmcc.wangsen.netsupport.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.support.v4.app.FragmentManager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.hbmcc.wangsen.netsupport.App;
import com.hbmcc.wangsen.netsupport.MainActivity;
import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.event.TabSelectedEvent;
import com.hbmcc.wangsen.netsupport.ui.fragment.fifth.FifthTabFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.first.FirstTabFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.forth.ForthTabFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.second.SecondTabFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.ThirdTabFragment;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.SupportFragment;

public class MainFragment extends SupportFragment {
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FORTH = 3;
    private static final int REQ_MSG = 10;
    public static int lastSelectedPosition = 0;
    public static int newSelectedPosition = 0;
    @Nullable
    TextBadgeItem numberBadgeItem;
    @Nullable
    ShapeBadgeItem shapeBadgeItem;
    public static SupportFragment[] mFragments = new SupportFragment[4];
    public static BottomNavigationBar bottomNavigationBar;
    public static MainFragment fragment;
    private FragmentManager mFragmentManager;

    private GestureDetector mGestureDetector;
    private int verticalMinistance = 100;            //水平最小识别距离
    private int minVelocity = 10;            //最小识别速度

    public static MainFragment newInstance() {

        Bundle args = new Bundle();
        if (fragment == null) {
            fragment = new MainFragment();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_fragment, container, false);

        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment firstFragment = findChildFragment(FirstTabFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = FirstTabFragment.newInstance();
//            mFragments[SECOND] = SecondTabFragment.newInstance();
            mFragments[SECOND] = FifthTabFragment.newInstance();
            mFragments[THIRD] = ThirdTabFragment.newInstance();
            mFragments[FORTH] = ForthTabFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FORTH]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
//            mFragments[SECOND] = findChildFragment(SecondTabFragment.class);
            mFragments[SECOND] = findChildFragment(FifthTabFragment.class);
            mFragments[THIRD] = findChildFragment(ThirdTabFragment.class);
            mFragments[FORTH] = findChildFragment(ForthTabFragment.class);
        }
    }

    private void initView(View view) {
        getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//        transaction.setCustomAnimations( R.xml.from_left,R.xml.out_right);

        mGestureDetector = new GestureDetector(App.getContext(), new LearnGestureListener());
//        为fragment添加OnTouchListener监听器
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }

        });


        numberBadgeItem = new TextBadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.blue)
                .setText("" + lastSelectedPosition)
                .setHideOnSelect(true);

        shapeBadgeItem = new ShapeBadgeItem()
                .setShape(ShapeBadgeItem.SHAPE_OVAL)
                .setShapeColorResource(R.color.teal)
                .setGravity(Gravity.TOP | Gravity.END)
                .setHideOnSelect(true);

        bottomNavigationBar = view.findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);
//        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp,
                        "首页").setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.ic_problem_128,
                        "问题").setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.ic_index_zhibiao,
                        "指标").setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.ic_settings_grey_500_24dp,
                        "管理").setActiveColorResource(R.color.orange))
                .setFirstSelectedPosition(0)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (lastSelectedPosition != position) {
                    showHideFragment(mFragments[position], mFragments[lastSelectedPosition]);
                    lastSelectedPosition = bottomNavigationBar.getCurrentSelectedPosition();
                    newSelectedPosition = lastSelectedPosition;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                EventBusActivityScope.getDefault(_mActivity).post(new TabSelectedEvent(position));
            }
        });
    }


    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_MSG && resultCode == RESULT_OK) {

        }
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }

    class ScrollView extends FrameLayout {

        public ScrollView(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            //TODOAuto-generatedmethodstub
            mGestureDetector.onTouchEvent(ev); //让GestureDetector响应触碰事件
//            super.dispatchTouchEvent(ev); //让Activity响应触碰事件
            return false;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return super.onInterceptTouchEvent(ev);
        }


        }

        //设置手势识别监听器
        class LearnGestureListener extends GestureDetector.SimpleOnGestureListener {

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                scrollBy((int) distanceX, getScrollY());
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() - e2.getX() > verticalMinistance && Math.abs(velocityX) > minVelocity) {

                    if (newSelectedPosition >= 3) {
                        newSelectedPosition = 0;
                    } else {
                        newSelectedPosition = newSelectedPosition + 1;
                    }

                    showHideFragment(mFragments[newSelectedPosition], mFragments[lastSelectedPosition]);
                    bottomNavigationBar.setFirstSelectedPosition(newSelectedPosition)
                            .initialise();
                    lastSelectedPosition = newSelectedPosition;

                } else if (e2.getX() - e1.getX() > verticalMinistance && Math.abs(velocityX) > minVelocity) {
                    if (newSelectedPosition <= 0) {
                        newSelectedPosition = 3;
                    } else {
                        newSelectedPosition = newSelectedPosition - 1;
                    }

                    showHideFragment(mFragments[newSelectedPosition], mFragments[lastSelectedPosition]);
                    bottomNavigationBar.setFirstSelectedPosition(newSelectedPosition)
                            .initialise();
                    lastSelectedPosition = newSelectedPosition;
                }
                return true;
            }

            //此方法必须重写且返回真，否则onFling不起效
//        @Override
//        public boolean onDown(MotionEvent e) {
//            return true;
//        }
        }

        public void showToast(String text) {
            Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT).show();
        }

        //在Fragment中注册事件
//    ((MainActivity)getActivity()).registerMyOnTouchListener(myOnTouchListener);

//    public MainActivity.MyOnTouchListener myOnTouchListener = new MainActivity.MyOnTouchListener() {
//        @Override
//        public boolean onTouch(MotionEvent ev) {
//
//            //让GestureDetector先响应事件
//            boolean event = mGestureDetector.onTouchEvent(ev);
//            return event;
//        }
//    };

    }
