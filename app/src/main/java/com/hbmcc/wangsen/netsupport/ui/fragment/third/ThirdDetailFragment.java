package com.hbmcc.wangsen.netsupport.ui.fragment.third;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbmcc.wangsen.netsupport.App;
import com.hbmcc.wangsen.netsupport.R;
import com.hbmcc.wangsen.netsupport.adapter.DetailAdapter;
import com.hbmcc.wangsen.netsupport.base.BaseBackFragment;
import com.hbmcc.wangsen.netsupport.ui.fragment.third.WirelessData.ThridDetailData;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class ThirdDetailFragment extends BaseBackFragment {
    private ThirdTabFragment thirdTabFragment;
    private static final String ARG_TITLE = "arg_title";
    private String mTitle;

    private RecyclerView recyclerViewFragmentThridTabRecentRecord3;
    private List<ThridDetailData> detaildatalist;
    private List<ThridDetailData> detaildatalistquery;
    private ThridDetailData thridDetailData;
    public ThirdTabFragment thridTabFragment;
    private DetailAdapter detailAdapter;
    private ImageView img_show;
    private Bitmap bitmap;
    private static final String TAG = "AboutFragment";
    private TextView textViewdetail;
    private Toolbar mToolbar;
    private ImageView imageviewUpdateurl;
    DecimalFormat df = new DecimalFormat("#.00");


    public static ThirdDetailFragment newInstance(String title) {

        ThirdDetailFragment fragment = new ThirdDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thrid_detail, container,
                false);
        initView(view);
        return attachToSwipeBack(view);
    }

    private void initView(View view) {
        recyclerViewFragmentThridTabRecentRecord3 = view.findViewById(R.id.recyclerView_fragment_third_tab_recent_record3);
        textViewdetail = view.findViewById(R.id.textView_fragment_thrid_detail1);
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
        initView();
    }

    public void initView() {
        detaildatalist = new ArrayList<>();
        textViewdetail.setText(ThirdTabFragment.detailname + "");
            detaildatalistquery = LitePal.where("eci = ?", ThirdTabFragment.eci+"").find(ThridDetailData.class);
            if (detaildatalistquery.size() != 0) {
                for (ThridDetailData thridDetailData:detaildatalistquery) {
                    detaildatalist.add(new ThridDetailData(thridDetailData.getEci(),thridDetailData.getTime()+"点",
                            Float.parseFloat(df.format(thridDetailData.getConnected()*100)),
                            Float.parseFloat(df.format(thridDetailData.getPaging()*100)),
                            Float.parseFloat(df.format(thridDetailData.getHandover()*100)),
                            Float.parseFloat(df.format(thridDetailData.getRelease()*100)),
                            Float.parseFloat(df.format(thridDetailData.getCover()*100)),
                            thridDetailData.getUsercount(), Float.parseFloat(df.format(thridDetailData.getPrbuseup()*100)),
                            Float.parseFloat(df.format(thridDetailData.getPrbusedown()*100)),
                            Float.parseFloat(df.format(thridDetailData.getVoconnected()*100)),
                            Float.parseFloat(df.format(thridDetailData.getVoerabconnet()*100)),
                            Float.parseFloat(df.format(thridDetailData.getVodelay()*100))));
                }
            }else {
                Toast.makeText(App.getContext(), "未采集到该小区指标数据", Toast.LENGTH_LONG).show();
//                for (int j=0;j<25;j++){
//                detaildatalist.add(new ThridDetailData(110,String.valueOf(j), (float)1, (float)1,
//                        (float)1, (float)1, (float)1,
//                        (int)1, (float)1, (float)1,
//                        (float)1, (float)1, (float)1));
//                }
            }
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFragmentThridTabRecentRecord3.setLayoutManager(layoutManager);
        detailAdapter = new DetailAdapter(detaildatalist);
        recyclerViewFragmentThridTabRecentRecord3.setAdapter(detailAdapter);
    }
}
