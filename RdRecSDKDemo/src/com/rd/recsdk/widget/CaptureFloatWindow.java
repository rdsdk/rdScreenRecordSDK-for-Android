package com.rd.recsdk.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rd.recsdk.demo.R;
import com.rd.recsdk.utils.Global;

/**
 * 悬浮球
 * 
 * @author abreal
 * 
 */
public class CaptureFloatWindow extends LinearLayout {

	public static final String TAG = "CaptureFloatWindow";

	// 窗口管理器
	private WindowManager windowManager;

	private float startXInView, startYInView;
	private float currentXInScreen, currentYInScreen;
	private float startXInScreen, startYInScreen;

	// 悬浮球的参数
	private WindowManager.LayoutParams layoutParams;
	private OnClickListener clickListener;

	// 是否可以移动
	private boolean bMove = true;

	// 屏幕的宽带
	private int screenWidth;

	// private int screenHeight;

	// 悬浮图标的宽度
	private static final int FLOAT_WINDOW_WIDTH = 100;

	// 浮窗最后的Y坐标
	private int viewYCoordinate;

	// 浮窗最后的X坐标
	private int viewXCoordinate;

	// 悬浮球背景图片
	private TextView textView;

	private Context context;

	public CaptureFloatWindow(Context context) {
		super(context);

		this.context = context;
		// 初始化布局
		initLayout(context);
	}

	private void initLayout(Context context) {

		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		setGravity(Gravity.CENTER);

		screenWidth = windowManager.getDefaultDisplay().getWidth();

 
		layoutParams = Global.WIN_LAYOUT_PARAMS;

		// 添加一张图片
		textView = new TextView(context);

 

		textView.setBackgroundResource(R.drawable.screen_capture_icon_background);

		textView.setGravity(Gravity.CENTER);
		textView.setText("");
		textView.setTextColor(Color.BLACK);
		addView(textView);

	}

	// 获取悬浮球的控件对象
	public TextView getTextView() {

		return textView;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Rect frame = new Rect();
		getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		currentXInScreen = event.getRawX();
		currentYInScreen = event.getRawY() - statusBarHeight;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startXInView = event.getX();
			startYInView = event.getY();
			startXInScreen = currentXInScreen;
			startYInScreen = currentYInScreen;
			break;
		case MotionEvent.ACTION_MOVE:

			updateViewPosition();
			break;
		case MotionEvent.ACTION_UP:
			setViewPosition();
			/**
			 * 如果移动距离小于5，则认为触发了单击事件。
			 */
			if ((currentXInScreen - startXInScreen) < 5
					&& (currentYInScreen - startYInScreen) < 5) {
				if (clickListener != null) {
					clickListener.onClick(textView);
				}
			}
			break;
		}
		return true;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		clickListener = l;
	}

	private void updateViewPosition() {
		if (bMove) {
			layoutParams.x = (int) (currentXInScreen - startXInView);
			layoutParams.y = (int) (currentYInScreen - startYInView);
			windowManager.updateViewLayout(this, layoutParams);
		}
	}

	int getRotateState(Context context) {
		WindowManager wmManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return wmManager.getDefaultDisplay().getRotation();
	}

	// 设置最终的位置
	private void setViewPosition() {

		if (bMove) {
			// 竖屏的时候
			if (getRotateState(context) == Surface.ROTATION_0
					|| getRotateState(context) == Surface.ROTATION_180) {

				// 如果移动到屏幕的右边，靠右对其
				if ((currentXInScreen - startXInView) > (screenWidth / 2)) {

					layoutParams.x = screenWidth - textView.getWidth();
				} else {

					// 否则靠左对其
					layoutParams.x = 0;
				}

				// 设置浮窗最后的X坐标
				viewXCoordinate = layoutParams.x;

				// 设置浮窗显示的纵坐标，
				layoutParams.y = viewYCoordinate = (int) (currentYInScreen - startYInView);
			} else {

				// 如果移动到屏幕的右边，靠右对其
				if ((currentXInScreen - startXInView) > (screenWidth / 2)) {

					layoutParams.x = screenWidth - textView.getWidth();
				} else {

					// 否则靠左对其
					layoutParams.x = 0;
				}

				// 设置浮窗最后的X坐标
				viewXCoordinate = layoutParams.x;

				// 设置浮窗显示的纵坐标，
				layoutParams.y = viewYCoordinate = (int) (currentYInScreen - startYInView);
			}

			windowManager.updateViewLayout(this, layoutParams);
		}
	}

	// 设置移动标识
	public void setMoveFlag(boolean flag) {
		this.bMove = flag;
	}

	// 获取浮窗的纵坐标
	public Point getViewCoordinate() {
		return new Point(viewXCoordinate, viewYCoordinate);
	}

	/**
	 * 获取悬浮球的宽度
	 * 
	 * @return
	 */
	public int getFloatWindowWidth() {
		if (textView != null) {

			return textView.getWidth();
		} else {
			return FLOAT_WINDOW_WIDTH;
		}
	}

}
