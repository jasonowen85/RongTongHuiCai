package com.shyx.rthc.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.shyx.rthc.Login.LoginActivity;
import com.shyx.rthc.R;
import com.shyx.rthc.common.Constant;
import com.shyx.rthc.manager.UiManager;
import com.shyx.rthc.utils.CPResourceUtil;
import com.shyx.rthc.utils.L;
import com.shyx.rthc.utils.SpUtils;
import com.shyx.rthc.widget.loopindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;


/**
 * 引导页
 * Created by new7 on 2016/6/2.
 * Email:gaojinming@eims.com.cn
 */
public class GuideActivity extends Activity {

    /*是否显示最后一页的进入按钮 */
    private static boolean SHOW_ENTER_BUTTON = true;
    /*是否显示当前页指示点 */
    private static boolean SHOW_INDY = true;

    //非标准分辨率
    private static boolean NO_STANDARD = false;
    private List<View> viewList = new ArrayList<>();
    public static final int PAGE_SIZE = 4;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ViewPager vp = (ViewPager) findViewById(R.id.viewpager);
        for (int i = 0 ; i < PAGE_SIZE ; i ++ ){
            viewList.add(View.inflate(context, R.layout.item_guide, null));
        }
        getScreenWH();

        vp.setAdapter(new GuidePageAdapter());

        CirclePageIndicator indy = (CirclePageIndicator)findViewById(R.id.indy);
        indy.setCentered(true);
        indy.setViewPager(vp);
        if (!SHOW_INDY){
            indy.setVisibility(View.GONE);
        }
    }

    private void getScreenWH() {

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        double screenWidth = display.getWidth();
        double screenHeight = display.getHeight();
//        screenWidth = 1080.0;
//        screenHeight = 1920;
        L.d("guide", "width * height = "+ screenWidth + " * " + screenHeight);
        double standard = 720.0/1280.0;
        double currDevice = screenWidth/screenHeight;
        if (currDevice != standard){
            //非标准分辨率
            L.e("guide", "非标准分辨率");
            NO_STANDARD = true;
        }
    }

    class GuidePageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View layout = viewList.get(position);
            ImageView imgGuide = (ImageView) layout.findViewById(R.id.imgGuide);
            imgGuide.setImageResource(CPResourceUtil.getDrawableId(context, "guide_page0" + (position + 1)));
            if (NO_STANDARD ){  //非标分辨率设置为CENTER_CROP 否则图片会变形
                imgGuide.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            TextView btnEnter = (TextView) layout.findViewById(R.id.btnEnter);
            btnEnter.setVisibility(position == getCount() - 1 ? View.VISIBLE : View.GONE);
            if (!SHOW_ENTER_BUTTON ){
                btnEnter.setCompoundDrawables(null, null, null, null);
            }
            btnEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpUtils.put(context, Constant.FLAG_FIRST, false);
                    UiManager.switcher(GuideActivity.this, LoginActivity.class);
                    finish();
                }
            });

            container.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }
}
