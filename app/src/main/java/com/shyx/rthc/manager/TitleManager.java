package com.shyx.rthc.manager;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shyx.rthc.R;

/**
 * 标题栏管理类
 */
public class TitleManager {

    private Activity ac;
    private View view;
    Resources rs;
    public TitleManager(Activity ac){
        this.ac = ac;
        rs = ac.getResources();
    }
    public TitleManager(View view){
        this.view = view;
        rs = view.getResources();
    }

    public interface RightLayoutListener{
        public void rightOnListener();
    }
    public interface LeftLayoutListener{
        public void leftOnListener();
    }

    public void setLeftLayout(int leftTxtId,int leftImgId){

        ImageView mLeftImg;
        TextView mLeftTxt;
        LinearLayout layout = null;
        if(ac == null){
            mLeftImg = (ImageView) view.findViewById(R.id.left_img);
            mLeftTxt = (TextView) view.findViewById(R.id.left_txt);

        }else{
            layout = (LinearLayout)ac.findViewById(R.id.left_layout);
            mLeftImg = (ImageView) ac.findViewById(R.id.left_img);
            mLeftTxt = (TextView) ac.findViewById(R.id.left_txt);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ac.overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
                    ac.finish();
                }
            });
            layout.setVisibility(View.VISIBLE);
        }
        if(leftTxtId>0){
            mLeftTxt.setText(rs.getString(leftTxtId));
        }
        if(leftImgId>0){
            mLeftImg.setBackgroundResource(leftImgId);
        }


    }

    /**
     * 设置标题栏标题文本
     * @param rId
     */
    public void setTitleTxt(int rId){
        TextView mTitleTxt = null;
        if(ac == null){
            mTitleTxt = (TextView) view.findViewById(R.id.title_txt);
        }else{
            mTitleTxt = (TextView) ac.findViewById(R.id.title_txt);
        }
        mTitleTxt.setText(rs.getString(rId));
    }

    /**
     * 设置标题栏标题文本
     * @param txt
     */
    public void setTitleTxt(String txt){
        TextView mTitleTxt = null;
        if(ac == null){
            mTitleTxt = (TextView) view.findViewById(R.id.title_txt);
        }else{
            mTitleTxt = (TextView) ac.findViewById(R.id.title_txt);
        }
        mTitleTxt.setText(txt);
    }

    /**
     * 设置右侧图片文字
     */
    public void setRightLayout(int rightTxtId,int rightImgId,final RightLayoutListener listener){

        LinearLayout layout = null;
        TextView rightTxt = null;
        ImageView rightImg = null;
        if(ac == null){
            rightImg = (ImageView) view.findViewById(R.id.right_img);
            rightTxt = (TextView) view.findViewById(R.id.right_txt);
            layout = (LinearLayout)view.findViewById(R.id.right_layout);
        }else{
            layout = (LinearLayout)ac.findViewById(R.id.right_layout);
            rightImg = (ImageView) ac.findViewById(R.id.right_img);
            rightTxt = (TextView) ac.findViewById(R.id.right_txt);
        }

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightOnListener();
            }
        });

        if(rightTxtId>0){
            rightTxt.setText(rs.getString(rightTxtId));
        }
        if(rightImgId>0){
            rightImg.setVisibility(View.VISIBLE);
            rightImg.setBackgroundResource(rightImgId);
        }else{
            rightImg.setVisibility(View.GONE);
        }

    }


    /**
     * 设置标题栏左边图标
     * @param rId
     */
    public ImageView setLeftImg(int rId){
        ImageView mLeftImg = null;
        if(ac == null){
            mLeftImg = (ImageView) view.findViewById(R.id.left_img);
        }else{
            mLeftImg = (ImageView) ac.findViewById(R.id.left_img);
        }
        mLeftImg.setBackgroundResource(rId);
        return mLeftImg;
    }
    /**
     * 设置标题栏左边文本
     * @param rId
     */
    public void setLeftTxt(int rId){
        TextView mLeftTxt = null;
        if(ac == null){
            mLeftTxt = (TextView) view.findViewById(R.id.left_txt);
        }else{
            mLeftTxt = (TextView) ac.findViewById(R.id.left_txt);
        }

        mLeftTxt.setText(rs.getString(rId));
    }

    public void setLeftImageClickListener(int rId, final LeftLayoutListener leftListener){
        ImageView mLeftImg = null;
        LinearLayout leftView = null;
        if(ac == null){
            mLeftImg = (ImageView) view.findViewById(R.id.left_img);
            leftView = (LinearLayout) view.findViewById(R.id.left_layout);
        }else{
            mLeftImg = (ImageView) ac.findViewById(R.id.left_img);
            leftView = (LinearLayout) ac.findViewById(R.id.left_layout);
        }
        if (rId>0) mLeftImg.setBackgroundResource(rId);
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leftListener!=null) leftListener.leftOnListener();
            }
        });

    }

    /**
     * 设置标题栏右边图标
     * @param rId
     */
    public void setRightImg(int rId){
        ImageView mRightImg = null;
        if(ac == null){
            mRightImg = (ImageView) view.findViewById(R.id.right_img);
        }else{
            mRightImg = (ImageView) ac.findViewById(R.id.right_img);
        }
        mRightImg.setBackgroundResource(rId);
    }
    /**
     * 设置标题栏右边文本
     * @param rId
     */
    public void setRightTxt(int rId){
        TextView mRightTxt = null;
        if(ac == null){
            mRightTxt = (TextView) view.findViewById(R.id.right_txt);
        }else{
            mRightTxt = (TextView) ac.findViewById(R.id.right_txt);
        }
        mRightTxt.setText(rs.getString(rId));
    }



    /**
     * 设置标题栏点击右布局响应事件
     * @param listener
     */
    public void setRightLayoutListener(final RightLayoutListener listener){
        LinearLayout layout = null;
        if(ac == null){
            layout = (LinearLayout)view.findViewById(R.id.right_layout);
        }else{
            layout = (LinearLayout)ac.findViewById(R.id.right_layout);
        }

        layout.setVisibility(View.VISIBLE);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightOnListener();
            }
        });
    }

    public void setTitleBgColor(int color){
        RelativeLayout layout = null;
        if(ac == null){
            layout = (RelativeLayout) view.findViewById(R.id.title_view_group);
        }else{
            layout = (RelativeLayout)ac.findViewById(R.id.title_view_group);
        }
        if (color>0) layout.setBackgroundResource(color);
        layout.setVisibility(View.GONE);
    }
}
