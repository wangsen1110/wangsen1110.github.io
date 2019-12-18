package com.hbmcc.wangsen.netsupport.ui.fragment.forth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;
import com.hbmcc.wangsen.netsupport.App;
import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.base.BaseMainFragment;
import com.hbmcc.wangsen.netsupport.database.LteBasesCustom;
import com.hbmcc.wangsen.netsupport.database.LteBasesGrid;
import com.hbmcc.wangsen.netsupport.database.LteBasesTrack;
import com.hbmcc.wangsen.netsupport.database.LteBasestationCell;
import com.hbmcc.wangsen.netsupport.event.TabSelectedEvent;
import com.hbmcc.wangsen.netsupport.ui.fragment.MainFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.forth.basestationdatabase.BasestationDatabaseFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.forth.basestationdatabase.DataImport;

import org.greenrobot.eventbus.Subscribe;
import org.litepal.LitePal;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

import static com.blankj.utilcode.util.ViewUtils.runOnUiThread;

public class ForthTabFragment extends BaseMainFragment implements OnProgressBarListener {
    public static ForthTabFragment fragment;
    public String Cell = "Cell";
    public String Custom = "Custom";
    public String Track = "Track";
    public String Grid = "Grid";
    private Button btnFragmentForthTabBasestationDatabase;
    private CheckBox checkBox4GBaseData;
    private CheckBox checkBoxCustom;
    private CheckBox checkBoxTrack;
    private CheckBox checkBoxGrid;
    private TextView textView4GBaseData;
    private TextView textViewCustom;
    private TextView textViewTrack;
    private TextView textViewGrid;
    private Button btnFragmentForthTabImport;
    private Button btnFragmentForthTabClear;
    private Button btnFragmentForthTabAbout;
    private TextView trackChoose;
    public static String logChooicePath = "null";
    private String youyiDir;
    private TextView trackPath;
    public static HashSet<String> arrayListCount;
    public static HashSet<String> hashSetCount;
    private DataImport dataImport;
    private NumberProgressBar bnpCell;
    public Timer timerCell;
    public static Lock lock = new ReentrantLock();

    public static ForthTabFragment newInstance() {
        Bundle args = new Bundle();
        if (fragment == null) {
            fragment = new ForthTabFragment();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forth_tab, container,
                false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        //绑定btn
        EventBusActivityScope.getDefault(_mActivity).register(this);
        btnFragmentForthTabBasestationDatabase = view.findViewById(R.id.btn_fragment_forth_tab_basestation_database);
        checkBox4GBaseData = view.findViewById(R.id.checkbox_fragment_forth_tab_database);
        checkBoxCustom = view.findViewById(R.id.checkbox_fragment_forth_tab_custom);
        checkBoxTrack = view.findViewById(R.id.checkbox_fragment_forth_tab_track);
        checkBoxGrid = view.findViewById(R.id.checkbox_fragment_forth_tab_grid);
        textView4GBaseData = view.findViewById(R.id.show_fragment_forth_database);
        textViewCustom = view.findViewById(R.id.show_fragment_forth_tab_custom);
        textViewTrack = view.findViewById(R.id.show_fragment_forth_tab_track);
        textViewGrid = view.findViewById(R.id.show_fragment_forth_tab_grid);
        btnFragmentForthTabImport = view.findViewById(R.id.btn_fragment_forth_tab_import);
        btnFragmentForthTabClear = view.findViewById(R.id.btn_fragment_forth_tab_clear);
        btnFragmentForthTabAbout = view.findViewById(R.id.btn_fragment_forth_tab_about);
        trackChoose = view.findViewById(R.id.text_fragment_basestion_track_choose);
        trackPath = view.findViewById(R.id.text_fragment_basestion_track_path);
        arrayListCount = new HashSet<String>();
        hashSetCount = new HashSet<String>();
        arrayListCount.add(Cell);
        arrayListCount.add(Custom);
        arrayListCount.add(Track);
        arrayListCount.add(Grid);
        textCount(arrayListCount);

        bnpCell = (NumberProgressBar) view.findViewById(R.id.number_progress_bar_cell);
    }

    public void progressBarVisibleCell() {
        lock.lock();
        bnpCell.setProgress(0);
        bnpCell.setMax(DataImport.csvFileLineCell);
        if (!bnpCell.isShown()) {
            bnpCell.setVisibility(View.VISIBLE);
        }
        bnpCell.setOnProgressBarListener(this);
        timerCell = new Timer();
        timerCell.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bnpCell.incrementProgressBy((DataImport.csvFileLineCell <= 5000 ? 2200 : 3500));
                    }
                });
            }
        }, 10, 1000);//时间为毫秒级 延时多少开始计时，多久增加一次进度。
        lock.unlock();
    }

    @Override
    public void onProgressChange(int current, int max) {
        lock.lock();
        if (current >= max - (DataImport.csvFileLineCell <= 2000 ? 2000 : 3500)) {
            bnpCell.setVisibility(View.GONE);
            bnpCell.setProgress(0);
            timerCell.cancel();
        }
        lock.unlock();

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        btnFragmentForthTabBasestationDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragment) getParentFragment()).startBrotherFragment
                        (BasestationDatabaseFragment.newInstance("基站数据库查询"));
            }
        });
        btnFragmentForthTabAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragment) getParentFragment()).startBrotherFragment
                        (AboutFragment.newInstance("关于"));
            }
        });

        btnFragmentForthTabImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataImport = new DataImport();
                if (!checkBox4GBaseData.isChecked() && !checkBoxCustom.isChecked()
                        && !checkBoxTrack.isChecked() && !checkBoxGrid.isChecked()) {
                    Toast.makeText(App.getContext(), "未选择数据!\n请勾选数据选项后,再导入", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkBox4GBaseData.isChecked()) {
                        hashSetCount.add(Cell);
                    }
                    if (checkBoxCustom.isChecked()) {
                        hashSetCount.add(Custom);
                    }
                    if (checkBoxTrack.isChecked()) {
                        hashSetCount.add(Track);
                    }
                    if (checkBoxGrid.isChecked()) {
                        hashSetCount.add(Grid);
                    }
                    dataImport.importData();
                }
            }
        });

        btnFragmentForthTabClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkBox4GBaseData.isChecked() && !checkBoxCustom.isChecked()
                        && !checkBoxTrack.isChecked() && !checkBoxGrid.isChecked()) {
                    Toast.makeText(App.getContext(), "未选择数据!\n请勾选数据选项后,再清除", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkBox4GBaseData.isChecked()) {
                        LitePal.deleteAll(LteBasestationCell.class);
                        arrayListCount.add(Cell);
                    }
                    if (checkBoxCustom.isChecked()) {
                        LitePal.deleteAll(LteBasesCustom.class);
                        arrayListCount.add(Custom);
                    }
                    if (checkBoxTrack.isChecked()) {
                        LitePal.deleteAll(LteBasesTrack.class);
                        arrayListCount.add(Track);
                    }
                    if (checkBoxGrid.isChecked()) {
                        LitePal.deleteAll(LteBasesGrid.class);
                        arrayListCount.add(Grid);
                    }
                    Toast.makeText(App.getContext(), "缓冲数据已清空", Toast.LENGTH_SHORT).show();
                    textCount(arrayListCount);
                }
            }
        });
    }

    public void textCount(Set<String> ints) {
        for (String i : ints) {
            switch (i) {
                case "Cell":
                    textView4GBaseData.setText("已导入" + LitePal.count(LteBasestationCell.class) + "条");
                    break;
                case "Custom":
                    textViewCustom.setText("已导入" + LitePal.count(LteBasesCustom.class) + "条");
                    break;
                case "Track":
                    textViewTrack.setText("已导入" + LitePal.count(LteBasesTrack.class) + "条");
                    break;
                case "Grid":
                    textViewGrid.setText("已导入" + LitePal.count(LteBasesGrid.class) + "条");
                    break;
                default:
                    break;
            }
        }
        arrayListCount.clear();
    }

    /**
     * Reselected Tab
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position != MainFragment.FORTH) {
            return;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        initDelayView();
    }

    private void initDelayView() {
        trackChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*.csv");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                youyiDir = uri.getPath().split("/")[2];
                logChooicePath = uri.getPath().split("/")[3];
                if (!logChooicePath.contains(".csv")) {
                    Toast.makeText(App.getContext(), "请选择正确的测试log文件，否则无法正常导入", Toast.LENGTH_SHORT).show();
                } else {
                    trackPath.setText("/" + youyiDir + "/" + logChooicePath);
                }
            }
        } else {
            Toast.makeText(App.getContext(), "未正确选择文件，请重新选择文件", Toast.LENGTH_SHORT).show();
        }
    }

}


