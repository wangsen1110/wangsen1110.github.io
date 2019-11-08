package com.hbmcc.wangsen.netsupport.ui.fragment.forth.basestationdatabase;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.hbmcc.wangsen.netsupport.App;
import com.hbmcc.wangsen.netsupport.base.BaseMainFragment;
import com.hbmcc.wangsen.netsupport.database.LteBasesCustom;
import com.hbmcc.wangsen.netsupport.database.LteBasesGrid;
import com.hbmcc.wangsen.netsupport.database.LteBasesTrack;
import com.hbmcc.wangsen.netsupport.database.LteBasestationCell;
import com.hbmcc.wangsen.netsupport.ui.fragment.forth.ForthTabFragment;
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


public class DataImport extends BaseMainFragment {
    ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
    private long startTime; //起始时间
    private long endTime;//结束时间
    private File lteDatabaseFile;
    public static int csvFileLineCell;

    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ForthTabFragment.fragment.arrayListCount.add(msg.obj.toString());
            ForthTabFragment.fragment.textCount(ForthTabFragment.fragment.arrayListCount);
        }
    };

    public void importData() {
        csvFileLineCell = 0;
        if (ForthTabFragment.fragment.hashSetCount.contains(ForthTabFragment.fragment.Cell)) {
            File lteDatabaseFile = new File(FileUtils.getLteInputFile());//获得文件对象name="lteBasestationDatabaseTemplate">4G工参(模板).csv
            csvFileLineCell = csvFileLineCell + com.blankj.utilcode.util.FileUtils.getFileLines(lteDatabaseFile);
        }
        if (ForthTabFragment.fragment.hashSetCount.contains(ForthTabFragment.fragment.Custom)) {
            File lteDatabaseFile = new File(com.hbmcc.wangsen.netsupport.util.FileUtils.getLteInputFilecustom());//获得文件对象规划自定义(模板).csv
            csvFileLineCell = csvFileLineCell + com.blankj.utilcode.util.FileUtils.getFileLines(lteDatabaseFile) / 2;
        }
        if (ForthTabFragment.fragment.hashSetCount.contains(ForthTabFragment.fragment.Track)) {
            if (ForthTabFragment.logChooicePath == "null") {
                lteDatabaseFile = new File(com.hbmcc.wangsen.netsupport.util.FileUtils.getLteInputFiletrack());//获得文件对象规划自定义(模板).csv
            } else {
                lteDatabaseFile = new File(FileUtils.getAppPath() + ForthTabFragment.logChooicePath);
            }
            csvFileLineCell = csvFileLineCell + com.blankj.utilcode.util.FileUtils.getFileLines(lteDatabaseFile) / 2;
        }
        if (ForthTabFragment.fragment.hashSetCount.contains(ForthTabFragment.fragment.Grid)) {
            File lteDatabaseFile = new File(com.hbmcc.wangsen.netsupport.util.FileUtils.getLteInputFileGrid());//获得文件对象规划自定义(模板).csv
            if (csvFileLineCell > 40000) {
                csvFileLineCell = csvFileLineCell + com.blankj.utilcode.util.FileUtils.getFileLines(lteDatabaseFile) / 2;
            } else {
                csvFileLineCell = csvFileLineCell + com.blankj.utilcode.util.FileUtils.getFileLines(lteDatabaseFile);
            }
        }
        ForthTabFragment.fragment.progressBarVisibleCell();

        for (String i : ForthTabFragment.fragment.hashSetCount) {
            switch (i) {
                case "Cell":
                    importLteDatabase();
                    break;
                case "Custom":
                    importCustom();
                    break;
                case "Track":
                    importTrack();
                    break;
                case "Grid":
                    importGrid();
                    break;
                default:
                    break;
            }
        }
        ForthTabFragment.fragment.hashSetCount.clear();
    }

    //导入工参
    public boolean importLteDatabase() {
        startTime = System.currentTimeMillis();
        if (FileUtils.isFileExist(FileUtils.getLteInputFile())) {
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    File lteDatabaseFile = new File(FileUtils.getLteInputFile());//获得文件对象name="lteBasestationDatabaseTemplate">4G工参(模板).csv
                    LteBasestationCell lteBasestationCell;//获取工参实体类的实例
                    List<LteBasestationCell> lteBasestationCellList = new ArrayList<>();//创建实体类列表
                    String inString;
                    int i = 0;
                    try {
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(new FileInputStream(lteDatabaseFile), "GBK"));//获得输入流
                        while ((inString = reader.readLine()) != null) {//一行一行读，判断是否为空
                            String[] inStringSplit = inString.split(",");
                            if (inStringSplit.length != 18) {
                                Looper.prepare();
                                Toast.makeText(App.getContext(), "导入的工参数据格式不对", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                                return;
                            }
                            i++;
                            if (i > 2) {
                                lteBasestationCell = new LteBasestationCell();
                                if (inStringSplit[0].length() > 0) {
                                    lteBasestationCell.setEci(Long.parseLong(inStringSplit[0]));
                                }
                                if (inStringSplit[1].length() > 0) {
                                    lteBasestationCell.setName(inStringSplit[1]);
                                }
                                if (inStringSplit[2].length() > 0) {
                                    lteBasestationCell.setCity(inStringSplit[2]);
                                }
                                if (inStringSplit[3].length() > 0) {
                                    lteBasestationCell.setLng(Float.parseFloat(inStringSplit[3]));
                                }
                                if (inStringSplit[4].length() > 0) {
                                    lteBasestationCell.setLat(Float.parseFloat(inStringSplit[4]));
                                }
                                if (inStringSplit[5].length() > 0) {
                                    lteBasestationCell.setAntennaHeight(Float.parseFloat(inStringSplit[5]));
                                }
                                if (inStringSplit[6].length() > 0) {
                                    lteBasestationCell.setAltitude(Float.parseFloat
                                            (inStringSplit[6]));
                                }
                                if (inStringSplit[7].length() > 0) {
                                    lteBasestationCell.setIndoorOrOutdoor(Integer.parseInt
                                            (inStringSplit[7]));
                                }
                                if (inStringSplit[8].length() > 0) {
                                    lteBasestationCell.setCoverageType(Integer.parseInt
                                            (inStringSplit[8]));
                                }
                                if (inStringSplit[9].length() > 0) {
                                    lteBasestationCell.setCoverageScene(Integer.parseInt
                                            (inStringSplit[9]));
                                }
                                if (inStringSplit[10].length() > 0) {
                                    lteBasestationCell.setEnbCellAzimuth(Float.parseFloat
                                            (inStringSplit[10]));
                                }
                                if (inStringSplit[11].length() > 0) {
                                    lteBasestationCell.setMechanicalDipAngle(Float.parseFloat
                                            (inStringSplit[11]));
                                }
                                if (inStringSplit[12].length() > 0) {
                                    lteBasestationCell.setElectronicDipAngle(Float.parseFloat
                                            (inStringSplit[12]));
                                }
                                if (inStringSplit[13].length() > 0) {
                                    lteBasestationCell.setCounty(inStringSplit[13]);
                                }
                                if (inStringSplit[14].length() > 0) {
                                    lteBasestationCell.setManufactoryName(inStringSplit[14]);
                                }
                                if (inStringSplit[15].length() > 0) {
                                    lteBasestationCell.setTac(Integer.parseInt
                                            (inStringSplit[15]));
                                }
                                if (inStringSplit[16].length() > 0) {
                                    lteBasestationCell.setPci(Integer.parseInt
                                            (inStringSplit[16]));
                                }
                                if (inStringSplit[17].length() > 0) {
                                    lteBasestationCell.setLteEarFcn(Integer.parseInt
                                            (inStringSplit[17]));
                                }

                                lteBasestationCell.setEnbId((int) (lteBasestationCell.getEci() / 256));
                                lteBasestationCell.setEnbCellId((int) (lteBasestationCell.getEci() %
                                        256));
                                lteBasestationCellList.add(lteBasestationCell);
                            }
                        }
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        final int cellNums = i;
                        Looper.prepare();
                        Toast.makeText(App.getContext(), "第" + cellNums + "行数据异常，请处理", Toast.LENGTH_LONG).show();
                        ForthTabFragment.fragment.timerCell.cancel();
                        Looper.loop();
                    } finally {
                        LitePal.saveAll(lteBasestationCellList);
                        endTime = System.currentTimeMillis();
                        final long usedTime = (int) ((endTime - startTime) / 1000);
                        final int cellNums = i;
                        Looper.prepare();
                        Toast.makeText(App.getContext(), "共导入" + (cellNums - 2) + "行数据，用时" + String.format
                                ("%d " +
                                        "s", usedTime), Toast.LENGTH_LONG).show();
                        Message msg = new Message();
                        msg.obj = "Cell";
                        mhandler.sendMessage(msg);
                        Looper.loop();
                    }
                }
            });
        } else {
            Looper.prepare();
            Toast.makeText(App.getContext(), "4G基站数据库文件不存在", Toast.LENGTH_LONG).show();
            Looper.loop();
            return false;
        }
        return true;
    }


    public boolean importCustom() {
        startTime = System.currentTimeMillis();
        if (com.hbmcc.wangsen.netsupport.util.FileUtils.isFileExist(com.hbmcc.wangsen.netsupport.util.FileUtils.getLteInputFilecustom())) {
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    File lteDatabaseFile = new File(com.hbmcc.wangsen.netsupport.util.FileUtils.getLteInputFilecustom());//获得文件对象规划自定义(模板).csv
                    LteBasesCustom lteBasesCustom;//获取工参实体类的实例
                    List<LteBasesCustom> lteBasesCustomList = new ArrayList<>();//创建实体类集合
                    String inString;
                    int i = 0;
                    try {
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(new FileInputStream(lteDatabaseFile), "GBK"));//获得输入流
                        while ((inString = reader.readLine()) != null) {//一行一行读，判断是否为空
                            String[] inStringSplit = inString.split(",");
//                            if (inStringSplit.length != 5) {
//                                _mActivity.runOnUiThread(new Runnable() {//开启子线程进行提示
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(App.getContext(), "导入的工参数据格式不对", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                                return;
//                            }
                            i++;

                            if (i > 2) {
                                lteBasesCustom = new LteBasesCustom();
                                if (inStringSplit[0].length() > 0) {
                                    lteBasesCustom.setName(inStringSplit[0]);
                                }
                                if (inStringSplit[1].length() > 0) {
                                    lteBasesCustom.setCity(inStringSplit[1]);
                                }
                                if (inStringSplit[2].length() > 0) {
                                    lteBasesCustom.setLng(Float.parseFloat(inStringSplit[2]));
                                }
                                if (inStringSplit[3].length() > 0) {
                                    lteBasesCustom.setLat(Float.parseFloat
                                            (inStringSplit[3]));
                                }
                                if (inStringSplit[4].length() > 0) {
                                    lteBasesCustom.setRemark(inStringSplit[4]);
                                } else {
                                    lteBasesCustom.setRemark(" ");
                                }
                                lteBasesCustomList.add(lteBasesCustom);
                            }
                        }
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        final int cellNums = i;
                        Looper.prepare();
                        Toast.makeText(App.getContext(), "第" + cellNums + "行数据异常，请处理", Toast.LENGTH_LONG).show();
                        ForthTabFragment.fragment.timerCell.cancel();
                        Looper.loop();
                    } finally {
                        LitePal.saveAll(lteBasesCustomList);
                        endTime = System.currentTimeMillis();
                        final long usedTime = (int) ((endTime - startTime) / 1000);
                        final int cellNums = i;
                        Looper.prepare();
                        Toast.makeText(App.getContext(), "共导入" + (cellNums - 2) + "行数据，用时" + String.format("%d " + "s", usedTime), Toast.LENGTH_LONG).show();
                        Message msg = new Message();
                        msg.obj = "Custom";
                        mhandler.sendMessage(msg);
                        Looper.loop();
                    }
                }
            });
        } else {
            Looper.prepare();
            Toast.makeText(App.getContext(), "规划自定义文件不存在", Toast.LENGTH_LONG).show();
            Looper.loop();
            return false;
        }
        return true;
    }

    public boolean importTrack() {
        startTime = System.currentTimeMillis();
        if (com.hbmcc.wangsen.netsupport.util.FileUtils.isFileExist(com.hbmcc.wangsen.netsupport.util.FileUtils.getLteInputFiletrack())) {
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    if (ForthTabFragment.logChooicePath == "null") {
                        lteDatabaseFile = new File(com.hbmcc.wangsen.netsupport.util.FileUtils.getLteInputFiletrack());//获得文件对象规划自定义(模板).csv
                    } else {
                        lteDatabaseFile = new File(FileUtils.getAppPath() + ForthTabFragment.logChooicePath);
                    }
                    LteBasesTrack lteBasesTrack;//获取工参实体类的实例
                    List<LteBasesTrack> lteBasesTrackList = new ArrayList<>();//创建实体类集合
                    String inString;
                    int i = 0;
                    try {

                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(new FileInputStream(lteDatabaseFile), "GBK"));//获得输入流
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
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        final int cellNums = i;
                        Looper.prepare();
                        Toast.makeText(App.getContext(), "第" + cellNums + "行数据异常，请处理", Toast.LENGTH_LONG).show();
                        ForthTabFragment.fragment.timerCell.cancel();
                        Looper.loop();
                    } finally {
                        LitePal.saveAll(lteBasesTrackList);
                        endTime = System.currentTimeMillis();
                        final long usedTime = (int) ((endTime - startTime) / 1000);
                        final int cellNums = i;
                        Looper.prepare();
                        Toast.makeText(App.getContext(), "共导入" + (cellNums - 2) + "行数据，用时" + String.format("%d " + "s", usedTime), Toast.LENGTH_LONG).show();
                        Message msg = new Message();
                        msg.obj = "Track";
                        mhandler.sendMessage(msg);
                        Looper.loop();
                    }
                }
            });
        } else {
            Looper.prepare();
            Toast.makeText(App.getContext(), "测试log轨迹数据库文件不存在", Toast.LENGTH_LONG).show();
            Looper.loop();
            return false;
        }
        return true;
    }


    public boolean importGrid() {
        startTime = System.currentTimeMillis();
        if (com.hbmcc.wangsen.netsupport.util.FileUtils.isFileExist(com.hbmcc.wangsen.netsupport.util.FileUtils.getLteInputFileGrid())) {
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    File lteDatabaseFile = new File(com.hbmcc.wangsen.netsupport.util.FileUtils.getLteInputFileGrid());//获得文件对象规划自定义(模板).csv
                    LteBasesGrid lteBasesGrid;//获取工参实体类的实例
                    List<LteBasesGrid> lteBasesGridList = new ArrayList<>();//创建实体类集合
                    String inString;
                    int i = 0;
                    try {
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(new FileInputStream(lteDatabaseFile), "GBK"));//获得输入流
                        while ((inString = reader.readLine()) != null) {//一行一行读，判断是否为空
                            String[] inStringSplit = inString.split(",");
//                            if (inStringSplit.length != 15) {
//                                _mActivity.runOnUiThread(new Runnable() {//开启子线程进行提示
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(App.getContext(), "导入的工参数据格式不对", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                                return;
//                            }
                            i++;

                            if (i > 2) {
                                lteBasesGrid = new LteBasesGrid();
                                if (inStringSplit[0].length() > 0) {
                                    lteBasesGrid.setGrid_name(inStringSplit[0]);
                                }
                                if (inStringSplit[1].length() > 0) {
                                    lteBasesGrid.setGrid_id(inStringSplit[1]);
                                }
                                if (inStringSplit[2].length() > 0) {
                                    lteBasesGrid.setLng(Float.parseFloat(inStringSplit[2]));
                                }
                                if (inStringSplit[3].length() > 0) {
                                    lteBasesGrid.setLat(Float.parseFloat(inStringSplit[3]));
                                }
                                if (inStringSplit[4].length() > 0) {
                                    lteBasesGrid.setLng1(Float.parseFloat(inStringSplit[4]));
                                }
                                if (inStringSplit[5].length() > 0) {
                                    lteBasesGrid.setLat1(Float.parseFloat(inStringSplit[5]));
                                }
                                if (inStringSplit[6].length() > 0) {
                                    lteBasesGrid.setLng2(Float.parseFloat(inStringSplit[6]));
                                }
                                if (inStringSplit[7].length() > 0) {
                                    lteBasesGrid.setLat2(Float.parseFloat(inStringSplit[7]));
                                }
                                if (inStringSplit[8].length() > 0) {
                                    lteBasesGrid.setLng3(Float.parseFloat(inStringSplit[8]));
                                }
                                if (inStringSplit[9].length() > 0) {
                                    lteBasesGrid.setLat3(Float.parseFloat(inStringSplit[9]));
                                }
                                if (inStringSplit[10].length() > 0) {
                                    lteBasesGrid.setLng4(Float.parseFloat(inStringSplit[10]));
                                }
                                if (inStringSplit[11].length() > 0) {
                                    lteBasesGrid.setLat4(Float.parseFloat(inStringSplit[11]));
                                }
                                if (inStringSplit[12].length() > 0) {
                                    lteBasesGrid.setRsrp(Float.parseFloat(inStringSplit[12]));
                                }
                                if (inStringSplit[13].length() > 0) {
                                    lteBasesGrid.setGridcount(Integer.parseInt(inStringSplit[13]));
                                }
                                if (inStringSplit[14].length() > 0 && !inStringSplit[14].isEmpty()) {
                                    lteBasesGrid.setGrid_call(inStringSplit[14]);
                                }
                                lteBasesGridList.add(lteBasesGrid);
                            }
                        }
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        final int cellNums = i;
                        Looper.prepare();
                        Toast.makeText(App.getContext(), "第" + cellNums + "行数据异常，请处理", Toast.LENGTH_LONG)
                                .show();
                        ForthTabFragment.fragment.timerCell.cancel();
                        Looper.loop();
                    } finally {
                        LitePal.saveAll(lteBasesGridList);
                        endTime = System.currentTimeMillis();
                        final long usedTime = (int) ((endTime - startTime) / 1000);
                        final int cellNums = i;
                        Looper.prepare();
                        Toast.makeText(App.getContext(), "共导入" + (cellNums - 2) + "行数据，用时" + String.format("%d " + "s", usedTime), Toast.LENGTH_LONG).show();
                        Message msg = new Message();
                        msg.obj = "Grid";
                        mhandler.sendMessage(msg);
                        Looper.loop();
                    }
                }
            });
        } else {
            Looper.prepare();
            Toast.makeText(App.getContext(), "栅格数据库文件不存在", Toast.LENGTH_LONG).show();
            Looper.loop();
            return false;
        }
        return true;
    }

}
