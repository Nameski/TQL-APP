package com.e.tql;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class BottomMenuView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "BottomMenuView";
    private Context mContext;
    private int imgColor = 0xff009AFF;//点击改变图片颜色
    private int imgDefaultColor = 0xff565656;//默认图片颜色
    private float textSize = 12; //字体大小
    private int imgPadding = 12; //内边距
    private List<BottomItem> bottomItems;//Item列表
    private List<Button> buttons;//button列表
    private int width;
    private int height;
    //回调接口
    private BottomItemOnClickListener bottomItemOnClickListener;

    public BottomMenuView(Context context) {
        super(context);
        initView(context);
    }

    public BottomMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BottomMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        mContext = context;
        buttons = new ArrayList<>();//实例化按钮列表
    }

    /**
     * 传入item列表
     *
     * @param bottomItems
     */
    public void setBottomItem(List<BottomItem> bottomItems) {
        this.bottomItems = bottomItems;
        //循环button按钮
        for (int i = 0; i < bottomItems.size(); i++) {
            //创建button
            Button buttom = new Button(mContext);
            //设置宽和高为MATCH_PARENT
            LayoutParams layoutParams = new LayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            buttom.setLayoutParams(layoutParams);
            //字体居中
            buttom.setGravity(Gravity.CENTER);
            //设置文字
            buttom.setText(bottomItems.get(i).getName());
            //设置文字大小
            buttom.setTextSize(textSize);
            //设置内边距
            buttom.setPadding(imgPadding, imgPadding, imgPadding, imgPadding);
            //去掉button背景
            buttom.setBackground(null);
            //获取图标资源
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), bottomItems.get(i).getIcon());
            //设置图标默认颜色
            Drawable drawable = new BitmapDrawable(getResources(), tintBitmap(bitmap, imgDefaultColor));
            //设置图标
            buttom.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            //将item设置到tag中下一步需要用到
            buttom.setTag(bottomItems.get(i));
            //设置监听
            buttom.setOnClickListener(this);
            //添加到当前布局
            addView(buttom);
            //添加到按钮组里
            buttons.add(buttom);
        }
    }

    private boolean isShow = false;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //计算每一个子button的宽度 并且判断只计算一次
        if (!isShow) {
            for (int i = 0; i < getChildCount(); i++) {
                LinearLayout.LayoutParams ll = (LayoutParams) getChildAt(i).getLayoutParams();
                ll.width = width / bottomItems.size();
                getChildAt(i).setLayoutParams(ll);
            }
            isShow = true;
        }
    }

    /**
     * 手动设置选中的button
     **/
    public void setShowIndex(int index) {
        if (buttons.size() != 0) {
            BottomItem bottomItem = (BottomItem) buttons.get(index).getTag();
            getBitmap(buttons.get(index), bottomItem.getIcon(), imgColor);
            bottomItemOnClickListener.bottomItemOnClick(buttons.get(index), index, bottomItem);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);
        height = View.MeasureSpec.getSize(heightMeasureSpec);
    }

    /**
     * 改变选中颜色
     *
     * @param btn
     * @param img
     * @param color
     */
    public void getBitmap(Button btn, int img, int color) {
        btn.setTextColor(color);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), img);
        Drawable drawable = new BitmapDrawable(mContext.getResources(), tintBitmap(bitmap, color));
        btn.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
    }

    /**
     * 对每一个BUTTON进行监听
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        for (int i = 0; i < buttons.size(); i++) {
            BottomItem bottomItem = (BottomItem) buttons.get(i).getTag();//从tag中获取BottomItem
            getBitmap(buttons.get(i), bottomItem.getIcon(), imgDefaultColor);//重置按钮颜色
            if (buttons.get(i).getTag() == view.getTag()) {//判断点击的是哪个按钮
                getBitmap(buttons.get(i), bottomItem.getIcon(), imgColor);//更改被点击的按钮牙
                if (bottomItemOnClickListener != null) {
                    //通知回调
                    bottomItemOnClickListener.bottomItemOnClick(view, i, bottomItem);
                }
            }
        }

    }

    public BottomItemOnClickListener getBottomItemOnClickListener() {
        return bottomItemOnClickListener;
    }

    public void setBottomItemOnClickListener(BottomItemOnClickListener bottomItemOnClickListener) {
        this.bottomItemOnClickListener = bottomItemOnClickListener;
    }

    /**
     * 点击监听回调接口
     */
    public interface BottomItemOnClickListener {
        void bottomItemOnClick(View view, int i, BottomItem item);
    }

    /**
     * 改变颜色
     *
     * @param inBitmap
     * @param tintColor
     * @return
     */
    public static Bitmap tintBitmap(Bitmap inBitmap, int tintColor) {
        if (inBitmap == null) {
            return null;
        }
        Bitmap outBitmap = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(), inBitmap.getConfig());
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(inBitmap, 0, 0, paint);
        return outBitmap;
    }

    /**
     * get / set
     **/
    public int getImgDefaultColor() {
        return imgDefaultColor;
    }

    public void setImgDefaultColor(int imgDefaultColor) {
        this.imgDefaultColor = imgDefaultColor;
    }

    public int getImgColor() {
        return imgColor;
    }

    public void setImgColor(int imgColor) {
        this.imgColor = imgColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getImgPadding() {
        return imgPadding;
    }

    public void setImgPadding(int imgPadding) {
        this.imgPadding = imgPadding;
    }
}