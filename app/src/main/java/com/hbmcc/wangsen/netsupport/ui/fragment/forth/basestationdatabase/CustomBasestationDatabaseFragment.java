package com.hbmcc.wangsen.netsupport.ui.fragment.forth.basestationdatabase;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hbmcc.wangsen.netsupport.App;
import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.base.BaseBackFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridComplainData;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridDetailData;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridFailureData;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridWirelessData;
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

public class CustomBasestationDatabaseFragment extends BaseBackFragment {
    private static final String TAG = "BasestationDatabaseFrag";
    ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

    private static final String ARG_TITLE = "arg_title";
    private String mTitle;

    private TabLayout mTab;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private Button btnFragmentBasestionDatabaseImportDataCustom;
    AlertDialog.Builder alertDialog;
    private long startTime; //起始时间
    private long endTime;//结束时间
//   private Button btnThirdWrielessImport;

    public static CustomBasestationDatabaseFragment newInstance(String title) {

        CustomBasestationDatabaseFragment fragment = new CustomBasestationDatabaseFragment();
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
        View view = inflater.inflate(R.layout.fragment_custom_basestation, container,
                false);
        initView(view);
        return attachToSwipeBack(view);
    }

    private void initView(View view) {
//        mViewPager = view.findViewById(R.id.viewpager_fragment_basestastion_custom);
//        btnThirdWrielessImport = view.findViewById(R.id.btn_fragment_forth_tab_test);
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
        btnFragmentBasestionDatabaseImportDataCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


//    public void btnBaseData(){
//        alertDialog = new AlertDialog.Builder(_mActivity);
//        alertDialog.setTitle("提示")
//                .setMessage("该操作将清空原有规划自定义数据，是否继续")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        progressDialog = ProgressDialog.show(_mActivity, "提示", "规划自定义数据导入中，请稍等...",
//                                true, false);
////                        importLteDatabase();
//                    }
//                });
//        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//
//        });
////        alertDialog.show();
//    }


    public boolean importthirdwrieless() {
        startTime = System.currentTimeMillis();
        if (FileUtils.isFileExist(FileUtils.getLteInputwrieless())) {
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    File lteDatabaseFile = new File(FileUtils.getLteInputwrieless());//获得文件对象name="lteBasestationDatabaseTemplate">4G工参(模板).csv
                    ThridWirelessData thridWirelessData;//获取工参实体类的实例
                    List<ThridWirelessData> thridWirelessDataList = new ArrayList<>();//创建实体类列表
                    String inString;
                    int i = 0;
                    try {
                        LitePal.deleteAll(ThridWirelessData.class);//删除LteBasestationCell数据表
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(new FileInputStream(lteDatabaseFile), "GBK"));//获得输入流
                        while ((inString = reader.readLine()) != null) {//一行一行读，判断是否为空
                            String[] inStringSplit = inString.split(",");

                            i++;
                            if (i > 1) {
                                thridWirelessData = new ThridWirelessData();
                                if (inStringSplit[1].length() > 0) {
                                    thridWirelessData.setEci(Long.valueOf(inStringSplit[1]));
                                }
                                if (inStringSplit[2].length() > 0) {
                                    thridWirelessData.setCellname(inStringSplit[2]);
                                }
                                if (inStringSplit[3].length() > 0) {
                                    thridWirelessData.setLon(Double.parseDouble(inStringSplit[3]));
                                }
                                if (inStringSplit[4].length() > 0) {
                                    thridWirelessData.setLat(Double.parseDouble(inStringSplit[4]));
                                }
                                if (inStringSplit[5].length() > 0) {
                                    thridWirelessData.setConnected(Float.parseFloat(inStringSplit[5]));
                                }
                                if (inStringSplit[6].length() > 0) {
                                    thridWirelessData.setRelease(Float.parseFloat(inStringSplit[6]));
                                }
                                if (inStringSplit[7].length() > 0) {
                                    thridWirelessData.setMrcover(Float.parseFloat(inStringSplit[7]));
                                }
                                if (inStringSplit[8].length() > 0) {
                                    thridWirelessData.setPrbdisturb(Float.parseFloat(inStringSplit[8]));
                                }else {
                                    thridWirelessData.setPrbdisturb((float)-120);
                                }
                                thridWirelessDataList.add(thridWirelessData);
                            }
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
                        LitePal.saveAll(thridWirelessDataList);
                        endTime = System.currentTimeMillis();
                        final long usedTime = (int) ((endTime - startTime) / 1000);
                        final int cellNums = i;
                        _mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(App.getContext(), "共导入" + cellNums + "行数据，用时" + String.format
                                        ("%d " + "s", usedTime), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "没有测试数据", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean importthirdfailure() {
        startTime = System.currentTimeMillis();
        if (FileUtils.isFileExist(FileUtils.getLteInputFailure())) {
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    File lteDatabaseFile = new File(FileUtils.getLteInputFailure());//获得文件对象name="lteBasestationDatabaseTemplate">4G工参(模板).csv
                    ThridFailureData thridFailureData;//获取工参实体类的实例
                    List<ThridFailureData> thridFailureDataList = new ArrayList<>();//创建实体类列表
                    String inString;
                    int i = 0;
                    try {
                        LitePal.deleteAll(ThridFailureData.class);//删除LteBasestationCell数据表
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(new FileInputStream(lteDatabaseFile), "GBK"));//获得输入流
                        while ((inString = reader.readLine()) != null) {//一行一行读，判断是否为空
                            String[] inStringSplit = inString.split(",");

                            i++;
                            if (i > 1) {
                                thridFailureData = new ThridFailureData();
                                if (inStringSplit[0].length() > 0) {
                                    thridFailureData.setName(inStringSplit[0]);
                                }
                                if (inStringSplit[1].length() > 0) {
                                    thridFailureData.setEci(Long.parseLong(inStringSplit[1]));
                                }
                                if (inStringSplit[2].length() > 0) {
                                    thridFailureData.setAlarmvalve(inStringSplit[2]);
                                }
                                if (inStringSplit[3].length() > 0) {
                                    thridFailureData.setTime(inStringSplit[3]);
                                }

                                thridFailureDataList.add(thridFailureData);
                            }
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
                        LitePal.saveAll(thridFailureDataList);
                        endTime = System.currentTimeMillis();
                        final long usedTime = (int) ((endTime - startTime) / 1000);
                        final int cellNums = i;
                        _mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(App.getContext(), "共导入" + cellNums + "行数据，用时" + String.format
                                        ("%d " + "s", usedTime), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "没有测试数据", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }

    public boolean importthirdcomplain() {
        startTime = System.currentTimeMillis();
        if (FileUtils.isFileExist(FileUtils.getLteInputComplain())) {
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    File lteDatabaseFile = new File(FileUtils.getLteInputComplain());//获得文件对象name="lteBasestationDatabaseTemplate">4G工参(模板).csv
                    ThridComplainData thridComplainData;//获取工参实体类的实例
                    List<ThridComplainData> thridComplainDataList = new ArrayList<>();//创建实体类列表
                    String inString;
                    int i = 0;
                    try {
//                        LitePal.deleteAll(ThridComplainData.class);//删除LteBasestationCell数据表
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(new FileInputStream(lteDatabaseFile), "GBK"));//获得输入流
                        while ((inString = reader.readLine()) != null) {//一行一行读，判断是否为空
                            String[] inStringSplit = inString.split(",");

                            i++;
                            if (i > 1) {
                                thridComplainData = new ThridComplainData();
                                if (inStringSplit[0].length() > 0) {
                                    thridComplainData.setTime(inStringSplit[0]);
                                }
                                if (inStringSplit[1].length() > 0) {
                                    thridComplainData.setUserid(inStringSplit[1]);
                                }
                                if (inStringSplit[2].length() > 0) {
                                    thridComplainData.setCategory(inStringSplit[2]);
                                }
                                if (inStringSplit[3].length() > 0) {
                                    thridComplainData.setLon(Double.parseDouble(inStringSplit[3]));
                                }
                                if (inStringSplit[4].length() > 0) {
                                    thridComplainData.setLat(Double.parseDouble(inStringSplit[4]));
                                }
                                if (inStringSplit[5].length() > 0) {
                                    thridComplainData.setAddress(inStringSplit[5]);
                                }
                                thridComplainDataList.add(thridComplainData);
                            }
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
                        LitePal.saveAll(thridComplainDataList);
                        endTime = System.currentTimeMillis();
                        final long usedTime = (int) ((endTime - startTime) / 1000);
                        final int cellNums = i;
                        _mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(App.getContext(), "共导入" + cellNums + "行数据，用时" + String.format
                                        ("%d " + "s", usedTime), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "没有测试数据", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean importthirdetail() {
        startTime = System.currentTimeMillis();
        if (FileUtils.isFileExist(FileUtils.getLteInputDetail())) {
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    File lteDatabaseFile = new File(FileUtils.getLteInputDetail());//获得文件对象name="lteBasestationDatabaseTemplate">4G工参(模板).csv
                    ThridDetailData thridDetailData;//获取工参实体类的实例
                    List<ThridDetailData> thridDetailDataList = new ArrayList<>();//创建实体类列表
                    String inString;
                    int i = 0;
                    try {
//                        LitePal.deleteAll(ThridDetailData.class);
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(new FileInputStream(lteDatabaseFile), "GBK"));//获得输入流
                        while ((inString = reader.readLine()) != null) {//一行一行读，判断是否为空
                            String[] inStringSplit = inString.split(",");
                            i++;
                            if (i > 1) {
                                thridDetailData = new ThridDetailData();
                                if (inStringSplit[0].length() > 0) {
                                    thridDetailData.setTime(inStringSplit[0]);
                                }
                                if (inStringSplit[1].length() > 0) {
                                    thridDetailData.setEci(Long.valueOf(inStringSplit[1]));
                                }
                                if (inStringSplit[2].length() > 0) {
                                    thridDetailData.setName(inStringSplit[2]);
                                }
                                if (inStringSplit[3].length() > 0) {
                                    thridDetailData.setConnected(Float.parseFloat(inStringSplit[3]));
                                }
                                if (inStringSplit[4].length() > 0) {
                                    thridDetailData.setPaging(Float.parseFloat(inStringSplit[4]));
                                }
                                if (inStringSplit[5].length() > 0) {
                                    thridDetailData.setHandover(Float.parseFloat(inStringSplit[5]));
                                }
                                if (inStringSplit[6].length() > 0) {
                                    thridDetailData.setRelease(Float.parseFloat(inStringSplit[6]));
                                }
                                if (inStringSplit[7].length() > 0) {
                                    thridDetailData.setCover(Float.parseFloat(inStringSplit[7]));
                                }
                                if (inStringSplit[8].length() > 0) {
                                    thridDetailData.setUsercount(Integer.valueOf(inStringSplit[8]));
                                }
                                if (inStringSplit[9].length() > 0) {
                                    thridDetailData.setPrbuseup(Float.parseFloat(inStringSplit[9]));
                                }
                                if (inStringSplit[10].length() > 0) {
                                    thridDetailData.setPrbusedown(Float.parseFloat(inStringSplit[10]));
                                }
                                if (inStringSplit[11].length() > 0) {
                                    thridDetailData.setVoconnected(Float.parseFloat(inStringSplit[11]));
                                }
                                if (inStringSplit[12].length() > 0) {
                                    thridDetailData.setVoerabconnet(Float.parseFloat(inStringSplit[12]));
                                }
                                if (inStringSplit[13].length() > 0) {
                                    thridDetailData.setVodelay(Float.parseFloat(inStringSplit[13]));
                                }
                                thridDetailDataList.add(thridDetailData);
                            }
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
                        LitePal.saveAll(thridDetailDataList);
                        endTime = System.currentTimeMillis();
                        final long usedTime = (int) ((endTime - startTime) / 1000);
                        final int cellNums = i;
                        _mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(App.getContext(), "共导入" + cellNums + "行数据，用时" + String.format
                                        ("%d " + "s", usedTime), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "没有测试数据", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }



}