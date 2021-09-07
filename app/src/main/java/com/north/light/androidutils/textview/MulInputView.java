package com.north.light.androidutils.textview;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.north.light.androidutils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzt
 * time 2020/5/29
 * 描述：密码输入自定义控件
 * <p>
 * 原理：
 * 底部一个edittext
 * 上面覆盖了
 */
public class MulInputView extends FrameLayout {
    private static final String TAG = MulInputView.class.getName();
    //输入的edittext
    private EditText mInput;
    //添加linearlayout
    private LinearLayout mTxLayout;
    //默认输入六个字符
    private int mInputCount = 6;
    //显示控件集合
    private List<TextView> mTxList = new ArrayList<>();
    //输入监听
    private OnInputListener mInputListener;

    public MulInputView(@NonNull Context context) {
        this(context, null);
    }

    public MulInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MulInputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //添加输入框
        mInput = new EditText(getContext());
        addView(mInput);
        mInput.setBackground(null);
        //游标不可见
        mInput.setCursorVisible(false);
        //限制输入数字
        mInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        //限制输入长度
        mInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mInputCount)});
        LayoutParams inputParams = (LayoutParams) mInput.getLayoutParams();
        inputParams.width = LayoutParams.MATCH_PARENT;
        inputParams.height = LayoutParams.MATCH_PARENT;
        mInput.setLayoutParams(inputParams);
        //添加linearlayout
        mTxLayout = new LinearLayout(getContext());
        addView(mTxLayout);
        LayoutParams linearParams = (LayoutParams) mTxLayout.getLayoutParams();
        linearParams.width = LayoutParams.MATCH_PARENT;
        linearParams.height = LayoutParams.MATCH_PARENT;
        mTxLayout.setLayoutParams(linearParams);
        //linearlayout horizonal
        mTxLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTxLayout.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
        initInputTextView();
    }

    /**
     * 设置Linearlayout里面的textview
     */
    private void initInputTextView() {
        if (mTxLayout != null) {
            mTxLayout.removeAllViews();
        }
        mTxList.clear();
        //设置textview
        for (int i = 0; i < mInputCount; i++) {
            TextView tv = new TextView(getContext());
            mTxLayout.addView(tv);
            LinearLayout.LayoutParams tvParams = (LinearLayout.LayoutParams) tv.getLayoutParams();
            tvParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            tvParams.width = 0;
            tvParams.weight = 1;
            tv.setLayoutParams(tvParams);
            tv.setTextColor(getResources().getColor(R.color.color_000000));
            tv.setTextSize(24f);
            tv.setGravity(Gravity.CENTER);
            mTxList.add(tv);
            //设置分割线
            if (i != mInputCount - 1) {
                LinearLayout mSpaceLine = new LinearLayout(getContext());
                mTxLayout.addView(mSpaceLine);
                LinearLayout.LayoutParams spaceParams = (LinearLayout.LayoutParams) mSpaceLine.getLayoutParams();
                spaceParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
                spaceParams.width = 1;
                mSpaceLine.setLayoutParams(spaceParams);
                mSpaceLine.setBackgroundResource(R.color.color_a8a8a8);
            }
        }
        //设置监听事件
        mInput.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //edit text 光标始终在最后设置
                String text = mInput.getText().toString();
                if(!TextUtils.isEmpty(text)){
                    mInput.setSelection(text.length());
                }
            }
        });
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged");
                //文字改变后的监听
                int txLength = s.length();
                for (int i = 0; i < mTxList.size(); i++) {
                    if (i < txLength) {
                        mTxList.get(i).setText(s.subSequence(i, i + 1));
                    } else {
                        mTxList.get(i).setText("");
                    }
                }
                if (txLength == mTxList.size()) {
                    if (mInputListener != null) {
                        mInputListener.finish();
                    }
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        this.mInputListener = null;
        super.onDetachedFromWindow();
    }

    //提供外部调用的方法--------------------------------------------------

    public void setOnInputListener(OnInputListener listener) {
        this.mInputListener = listener;
    }

    //输入完成监听
    public interface OnInputListener {
        void finish();
    }

    /**
     * 设置文字的颜色，大小（单位sp）
     */
    public void setTextSize(int color, int spSize) {
        for (TextView tv : mTxList) {
            tv.setTextColor(getResources().getColor(color));
            tv.setTextSize(spSize);
        }
    }

    /**
     * 设置父布局弹窗的背景
     */
    public void setTxLayoutBg(int res) {
        if (mTxLayout != null) {
            mTxLayout.setBackgroundResource(res);
        }
    }

    /**
     * 设置多少个输入文字
     */
    public void setInputCount(int mInputCount) {
        if (mInputCount <= 0) {
            Toast.makeText(getContext().getApplicationContext(), "长度设置错误", Toast.LENGTH_SHORT).show();
            return;
        }
        this.mInputCount = mInputCount;
        mInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mInputCount)});
        initInputTextView();
    }

    /**
     * 获取输入的文字
     */
    public String getInputText() {
        return this.mInput.getText().toString();
    }

    /**
     * 置空inputtext
     */
    public void reSetInputTxt() {
        if (this.mInput != null) {
            this.mInput.setText("");
        }
    }
}
