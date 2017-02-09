package com.rd.recsdk.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rd.recsdk.demo.R;


/**
 * 截屏程序的主窗口
 * 
 * @author jeck
 * 
 */
public class MainScreenCaptureFloatWindow extends LinearLayout {

	// 视频录制View
	private TextView mScreenCaptureTextView;

	// 高级编辑
	private TextView mSeniorEditorTextView;

	// 设置
	private TextView mSettingTextView;

	// 关闭浮窗左
	private TextView mCloseWindowLeftTextView;

	// 关闭浮窗右
	private TextView mCloseWindowRightTextView;

	// 标题栏布局
	private LinearLayout mWindowTitleBarLayout;

	// 主体布局
	private LinearLayout mWindowBodyLayout;

	// 事件响应接口
	private MainWindowListener mMainWindowListener;

	// 布局文件
	private View view;

	// 上下文
	private Context context;

	// 构造函数，在new这个窗口对象的时候调用
	public MainScreenCaptureFloatWindow(Context context,
			MainWindowListener mainWindowListener) {
		super(context);

		this.context = context;
		this.mMainWindowListener = mainWindowListener;

		inflactLayout();

	}

	private void inflactLayout() {

		// 布局文件
		view = LayoutInflater.from(context).inflate(
				R.layout.float_main_screen_capture_window_layout, null);

		// 视频录制
		mScreenCaptureTextView = (TextView) view
				.findViewById(R.id.tv_main_window_screen_capture);
		mScreenCaptureTextView.setOnClickListener(viewCliceListener);

		// 高级编辑
		mSeniorEditorTextView = (TextView) view
				.findViewById(R.id.tv_main_window_senior_editor);
		mSeniorEditorTextView.setOnClickListener(viewCliceListener);

		// 设置
		mSettingTextView = (TextView) view
				.findViewById(R.id.tv_main_window_setting);
		mSettingTextView.setOnClickListener(viewCliceListener);

		// 关闭窗口左
		mCloseWindowLeftTextView = (TextView) view
				.findViewById(R.id.tv_video_capture_window_close_left);
		mCloseWindowLeftTextView.setOnClickListener(viewCliceListener);

		// 关闭窗口右
		mCloseWindowRightTextView = (TextView) view
				.findViewById(R.id.tv_video_capture_window_close_right);
		mCloseWindowRightTextView.setOnClickListener(viewCliceListener);

		// 标题栏
		mWindowTitleBarLayout = (LinearLayout) view
				.findViewById(R.id.ll_flaot_vidoe_capture_title_layout);

		// 主体
		mWindowBodyLayout = (LinearLayout) view
				.findViewById(R.id.ll_float_vidoe_capture_body_layout);

		addView(view);
	}

	private View.OnClickListener viewCliceListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mMainWindowListener != null) {

				int id = v.getId();
				if (id == R.id.tv_main_window_screen_capture) {
					mMainWindowListener.onVideoCapture();
				} else if (id == R.id.tv_main_window_senior_editor) {
					mMainWindowListener.onVideoEditor();
				} else if (id == R.id.tv_main_window_setting) {
					mMainWindowListener.onVideoCaptureConfiguration();
				} else if (id == R.id.tv_video_capture_window_close_left
						|| id == R.id.tv_video_capture_window_close_right) {
					mMainWindowListener.onCloseWindow();
				}

			}
		}
	};

	public int getMainWindowWidth() {
		if (view != null) {
			return view.getWidth();
		} else {
			return 0;
		}
	}

	/**
	 * 向右显示
	 */
	public void showMainWindowRight() {

		// 向右显示
		mWindowTitleBarLayout
				.setBackgroundResource(R.drawable.float_video_capture_title_bg_right);
		mWindowBodyLayout
				.setBackgroundResource(R.drawable.float_video_capture_body_right);

		// 隐藏右边的关闭图片
		mCloseWindowRightTextView.setVisibility(View.INVISIBLE);

		// 显示左边的关闭图片
		mCloseWindowLeftTextView.setVisibility(View.VISIBLE);

	}

	/**
	 * 向左显示
	 */
	public void showMainWindowLeft() {

		// 向左显示
		mWindowTitleBarLayout
				.setBackgroundResource(R.drawable.float_video_capture_title_bg_left);
		mWindowBodyLayout
				.setBackgroundResource(R.drawable.float_video_capture_body_left);

		// 隐藏左边的关闭图片
		mCloseWindowLeftTextView.setVisibility(View.INVISIBLE);

		// 显示右边的关闭图片
		mCloseWindowRightTextView.setVisibility(View.VISIBLE);
	}

	/**
	 * 事件响应接口
	 * 
	 * @author jeck
	 * 
	 */
	public interface MainWindowListener {

		// 录制视频
		public void onVideoCapture();

		// 高级编辑
		public void onVideoEditor();

		// 设置
		public void onVideoCaptureConfiguration();

		// 关闭窗口
		public void onCloseWindow();

	}
}
