package com.shyx.rthc.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shyx.rthc.R;


/**
 * 带删除按钮的EditText
 *
 * @author zyh
 */
public class AccountEditText2 extends LinearLayout implements View.OnFocusChangeListener{

    private EditText inputEdt;
    private ImageView deleteBtn;
    private TextView viewTitle;

    /**
     * 监听EditText文本变化
     */
    public interface EdtListener {
        void onTxtChangeListener(String s);
    }

    public AccountEditText2(Context context) {
        super(context);
    }

    public AccountEditText2(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_register_login, this, true);
        inputEdt = (EditText) view.findViewById(R.id.input_edt);
        viewTitle = (TextView) view.findViewById(R.id.title_icon);
        deleteBtn = (ImageView) view.findViewById(R.id.delete_icon);
        inputEdt.setOnFocusChangeListener(this);
        deleteBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                inputEdt.setText("");
            }
        });

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String s = inputEdt.getText().toString();
        deleteBtn.setVisibility(hasFocus && s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        if (hasFocus) {
            inputEdt.setSelection(s.length());
        }
    }


    /**
     * 监听EditText文本
     *
     * @param edtListener
     */
    public void addEdtTextChangeListener(final EdtListener edtListener) {
        inputEdt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (edtListener != null) {
                    edtListener.onTxtChangeListener(s.toString());
                }
                deleteBtn.setVisibility(inputEdt.isFocused() && s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    /**
     * @return 获取输入框中的内容
     */
    public String getText() {
        return inputEdt.getText().toString().trim();
    }

    /**
     * @param str 设置输入框内容
     */
    public void setText(String str) {
        inputEdt.setText(str);
    }

    public void setHint(String s) {
        inputEdt.setHint(s);
    }

    public void setInputType(String title,String s){
        inputEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputEdt.setHint(s);
        viewTitle.setText(title);
    }

}
