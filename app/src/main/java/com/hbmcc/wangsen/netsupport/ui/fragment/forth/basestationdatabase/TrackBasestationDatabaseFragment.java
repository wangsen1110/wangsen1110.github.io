package com.hbmcc.wangsen.netsupport.ui.fragment.forth.basestationdatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hbmcc.wangsen.netsupport.App;
import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.base.BaseBackFragment;
import com.hbmcc.wangsen.netsupport.database.LteBasesTrack;
import com.hbmcc.wangsen.netsupport.util.FileUtils;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrackBasestationDatabaseFragment extends BaseBackFragment {
    private static final String TAG = "BasestationDatabaseFrag";
    ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
    private String logChooicePath = "null";
    private String youyiDir;
    private static final String ARG_TITLE = "arg_title";
    private String mTitle;
    private TabLayout mTab;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TextView trackChoose;
    private TextView trackPath;
    private Button btnFragmentBasestionDatabaseImportDataTrack;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private long startTime; //起始时间
    private long endTime;//结束时间
    private File lteDatabaseFile;

    public static TrackBasestationDatabaseFragment newInstance(String title) {

        TrackBasestationDatabaseFragment fragment = new TrackBasestationDatabaseFragment();
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
        View view = inflater.inflate(R.layout.fragment_track_basestion, container,
                false);
        initView(view);
        return attachToSwipeBack(view);
    }

    private void initView(View view) {
//        mToolbar = view.findViewById(R.id.toolbar);
//        mTab = view.findViewById(R.id.tab_fragment_basestastion_custom);
        mViewPager = view.findViewById(R.id.viewpager_fragment_basestastion_track);
        btnFragmentBasestionDatabaseImportDataTrack = view.findViewById(R.id.btn_fragment_basestion_database_import_track);
        trackChoose = view.findViewById(R.id.text_fragment_basestion_track_choose);
        trackPath = view.findViewById(R.id.text_fragment_basestion_track_path);
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
        trackChoose.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                   intent.setType("*/*.csv");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                   intent.addCategory(Intent.CATEGORY_OPENABLE);
                   startActivityForResult(intent, 1);

//                   //getUrl()获取文件目录，例如返回值为/storage/sdcard1/MIUI/music/mp3_hd/单色冰淇凌_单色凌.mp3
//                   File file = new File("/storage/emulated/0/优易");
//                    //获取父目录
//                   File parentFlie = new File(file.getParent());
//                   Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                   intent.setDataAndType(Uri.fromFile(parentFlie), "*/*");
//                   intent.addCategory(Intent.CATEGORY_OPENABLE);
//                   startActivity(intent);


               }
           }
        );

        btnFragmentBasestionDatabaseImportDataTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(_mActivity);
                alertDialog.setTitle("提示")
                        .setMessage("该操作将清空原有测试log轨迹数据，是否继续")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog = ProgressDialog.show(_mActivity, "提示", "测试log轨迹数据导入中，请稍等...",
                                        true, false);
                                importLteDatabase();
                            }
                        });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
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
                }else {
                    trackPath.setText("/"+youyiDir+"/"+logChooicePath);
                }
            }
        } else {
            Toast.makeText(App.getContext(), "未正确选择文件，请重新选择文件", Toast.LENGTH_SHORT).show();
        }
    }

    //导入工参
    public boolean importLteDatabase() {
        startTime = System.currentTimeMillis();
        if (com.hbmcc.wangsen.netsupport.util.FileUtils.isFileExist(com.hbmcc.wangsen.netsupport.util.FileUtils.getLteInputFiletrack())) {
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    if (logChooicePath == "null"){
                        lteDatabaseFile = new File(com.hbmcc.wangsen.netsupport.util.FileUtils.getLteInputFiletrack());//获得文件对象规划自定义(模板).csv
                    }else {
                        lteDatabaseFile = new File(FileUtils.getAppPath()+logChooicePath);
                    }
                    LteBasesTrack lteBasesTrack;//获取工参实体类的实例
                    List<LteBasesTrack> lteBasesTrackList = new ArrayList<>();//创建实体类集合
                    String inString;
                    int i = 0;
                    try {
                        LitePal.deleteAll(LteBasesTrack.class);//删除LteBasestationCell数据表
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(new FileInputStream(lteDatabaseFile), "UTF-8"));//获得输入流
                        while ((inString = reader.readLine()) != null) {//一行一行读，判断是否为空
                            String[] inStringSplit = inString.split(",");
                            i++;
                            if (i > 2) {
                                lteBasesTrack = new LteBasesTrack();
                                if (inStringSplit[0].length() > 0) {
                                    lteBasesTrack.setName(inStringSplit[0]);
                                }
                                if (inStringSplit[1].length() > 0) {
                                    lteBasesTrack.setLng(Float.parseFloat(inStringSplit[1]));
                                }
                                if (inStringSplit[2].length() > 0) {
                                    lteBasesTrack.setLat(Float.parseFloat(inStringSplit[2]));
                                }
                                if (inStringSplit[3].length() > 1) {
                                    lteBasesTrack.setRsrp(Float.parseFloat(inStringSplit[3]));
                                }
                                lteBasesTrackList.add(lteBasesTrack);
                            }
                        }
                        if (i == 2) {
                            _mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();//如果文件中没有数据，则取消进度框提示
                                    Toast.makeText(App.getContext(), "文件无数据", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        final int cellNums = i;
                        _mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(App.getContext(), "第" + cellNums + "行数据异常，请处理", Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    } finally {
                        LitePal.saveAll(lteBasesTrackList);
                        endTime = System.currentTimeMillis();
                        final long usedTime = (int) ((endTime - startTime) / 1000);
                        final int cellNums = i;
                        _mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();//导入完成后，取消进度对话框显示
                                Toast.makeText(App.getContext(), "共导入" + cellNums + "行数据，用时" + String.format("%d " + "s", usedTime), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        } else {
            progressDialog.dismiss();//如果找不到文件，则取消进度框提示
            Toast.makeText(getContext(), "测试log轨迹数据库文件不存在", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
