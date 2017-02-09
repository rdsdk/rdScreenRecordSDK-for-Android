package com.rd.recsdk.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.rd.recsdk.RecSdkConfig;
import com.rd.recsdk.RecSdkManager;
import com.rd.recsdk.demo.R;
import com.rd.recsdk.demo.RecSdkConfigActivity;
import com.rd.recsdk.demo.RecorderShare;
import com.rd.recsdk.widget.CaptureFloatWindow;
import com.rd.recsdk.widget.MainScreenCaptureFloatWindow;
import com.rd.recsdk.widget.MainScreenCaptureFloatWindow.MainWindowListener;
import com.rd.recsdk.widget.VideoCaptureStopWindow;
import com.rd.recsdk.widget.VideoCaptureStopWindow.StopCaptureListener;

/**
 * 悬浮窗界面工具
 * 
 * @author abreal
 * 
 */
public class FloatWindowUtils {
	@SuppressWarnings("unused")
	private static final String TAG = "FloatWindowUtils";
	// 悬浮球默认的显示位置Y
	public static final int FLOAT_WINDOW_DEFAULT_Y = 100;
	// 窗口管理器
	private WindowManager windowManager;
	// 窗口参数
	private WindowManager.LayoutParams windowManagerParams;
	// 录屏悬浮球
	private CaptureFloatWindow captureFloatWindow;
	// 录屏悬浮窗口
	private MainScreenCaptureFloatWindow mainScreenCaptureWindow;
	// 提示对话框窗口
	private VideoCaptureStopWindow videoCaptureStopWindow;
	// 显示和隐藏主窗口
	private boolean bShowMainScreenCaptureWindow = true;
	// 是否可以点击悬浮球
	private boolean bCanClickFloatBubble = true;
	// 标识县浮球是否显示中。。。
	private boolean bCaptureFloatWindowShowing = false;
	// 悬浮球的坐标
	private Point mViewPoint;
	// 悬浮球默认的显示位置X
	private int defaultScreenWidth;
	// 屏幕的高度
	private int defaultScreenHeight;
	// 主窗口的X坐标
	private int mainWindowXCoordinate;
	// 主窗口的Y坐标
	private int mainWindowYCoordinate = FLOAT_WINDOW_DEFAULT_Y;

	// 视频录屏控制窗口的宽度
	private int videoCaptureStopWindowWidth;
	// 主操作窗口的宽度
	private int mainFloatWindowWidth;
	private Context context;
	private boolean isRecording, isPausing;
	// 开始录制时的时间
	private long timerCount = 0;
	// 暂停时的时间
	private long pauseTimstamp;

	private static FloatWindowUtils instance;

	/**
	 * 获取悬浮窗单例
	 * 
	 * @return
	 */
	public static FloatWindowUtils getInstance() {
		if (instance == null) {
			instance = new FloatWindowUtils();
		}
		return instance;
	}

	/**
	 * 返回悬浮球是否显示中...
	 * 
	 * @return
	 */
	public boolean isFloatWindowShowing() {
		return bCaptureFloatWindowShowing;
	}

	/**
	 * 返回当前是否录制中
	 * 
	 * @return
	 */
	public boolean isRecording() {
		return isRecording;
	}

	private int nType;

	/**
	 * 设置录制状态
	 * 
	 * @param type
	 *            参数:0 保存到本地 ， 1 rtmp 直播 ，2 uid 直播
	 * @param isRecording
	 */
	public void setRecording(int type, boolean isRecording) {
		this.isRecording = isRecording;
		nType = type;
		if (isRecording) {
			// 0 保存到本地 ， 1 rtmp 直播 ，2 uid 直播
			startTimer();
			if (nType == 1 || nType == 2) {
				captureFloatWindow.getTextView().setText("直播中");
			}

			hideMainFloatWindow();
		} else {
			timer.cancel();
		}
		isPausing = false;
	}

	/**
	 * 显示悬浮球
	 */
	public void showFloatWindow(Context context) {

		// 初始化窗口参数
		initWindowParams(context);

		// 创建悬浮球
		createFloatBall();

		// 显示悬浮球
		showFloatingBall();
	}

	/**
	 * 隐藏悬浮球
	 * 
	 * @param context
	 */
	public void hideFloatWindow(Context context, boolean bForce) {
		if (isRecording && !bForce) {
			return;
		}
		if (isRecording) {
			setRecording(-1, false);
		}
		hideMainFloatBubble();
	}

	/**
	 * 响应开始屏幕录制
	 */
	private void onStartVideoCapture(final int type, String videoOutPath) {
		// 响应开始屏幕录制
		RecSdkConfig config = new RecSdkConfig();
		config.loadFromSharedPreferences(context);
		config.setSavePath(videoOutPath);
		RecSdkManager.startRec(context, config,
				new RecSdkManager.RecSdkMessageListener() {

					@Override
					public void onRequestRootFail() {

						onFailed();
						Log.e(this.toString(), "onRequestRootFail->请求root权限失败");
						onToast("请求root权限失败！");
					}

					@Override
					public void onRecordSuccess(String strRecordFile) {
						onFailed();
						if (nType == 0) {
							onToast(String.format("录屏成功，文件保存在%s！",
									strRecordFile));
						} else {
							onToast("录屏直播成功");
						}
					}

					/**
					 * 响应失败
					 */
					private void onFailed() {
						Log.e("onFailed--", this.toString());
						setRecording(-1, false);
						captureFloatWindow.getTextView().setText("");
						captureFloatWindow.getTextView().setBackgroundResource(
								R.drawable.screen_capture_icon_background);

						captureFloatWindow.setMoveFlag(true);
					}

					@Override
					public void onRecordFailed(int nResult, String msg) {
						Log.e(this.toString(), "onRecordFailed->" + nResult
								+ "--->" + msg);
						if (nResult < 0) {
							onFailed();
						}

						onToast(String.format("录屏失败，错误为:%d！", nResult));
					}

					@Override
					public void onRecordBegin() {
						Log.d(this.toString(), "开始录制。");
						setRecording(type, true);
					}
				});

		// 预处理UI调整为录制成功RecSdkMessageListener.onRecordBegin()时的UI
		// setRecording(type, true);

	}

	private void onToast(String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 响应停止屏幕录制
	 */
	protected void onStopVideoCapture() {
		// TODO:响应停止屏幕录制
		RecSdkManager.endRec(context);
		setRecording(-1, false);
		windowManager.removeView(videoCaptureStopWindow);
		bCanClickFloatBubble = true;
		if (captureFloatWindow != null) {
			onCalculatePosition();
			captureFloatWindow.getTextView().setText("");
			captureFloatWindow.getTextView().setBackgroundResource(
					R.drawable.screen_capture_icon_background);

			captureFloatWindow.setMoveFlag(true);
		}
	}

	protected void onPauseVideoCapture() {
		if (isPausing) {
			RecSdkManager.continueRec(context);
			timerCount += (System.currentTimeMillis() - pauseTimstamp); // 开始录制时间调整，增加暂停所用的时间
			pauseTimstamp = 0;
		} else {
			RecSdkManager.pauseRec(context);
			pauseTimstamp = System.currentTimeMillis();
		}
		isPausing = !isPausing;
		handleCancelClick();
	}

	// 处理高级编辑点击
	private void handleSeniorEditorClick() {
		// 隐藏主操作界面
		hideMainFloatWindow();
	}

	/**
	 * 初始化参数
	 */
	@SuppressWarnings("deprecation")
	private void initWindowParams(Context context) {
		this.context = context.getApplicationContext();
		if (windowManager == null) {
			windowManager = (WindowManager) this.context
					.getApplicationContext().getSystemService(
							Context.WINDOW_SERVICE);

			defaultScreenWidth = windowManager.getDefaultDisplay().getWidth();
			defaultScreenHeight = windowManager.getDefaultDisplay().getHeight();

			// 如果是横屏
			if (defaultScreenWidth < defaultScreenHeight) {

				mainFloatWindowWidth = (int) (defaultScreenWidth * 0.63);
				videoCaptureStopWindowWidth = (int) (defaultScreenWidth * 0.5);
			} else {

				mainFloatWindowWidth = (int) (defaultScreenHeight * 0.63);
				videoCaptureStopWindowWidth = (int) (defaultScreenHeight * 0.5);
			}
		}

		if (windowManagerParams == null) {

			windowManagerParams = Global.WIN_LAYOUT_PARAMS;
			windowManagerParams.type = 2003;
			windowManagerParams.flags = 40;
			windowManagerParams.format = 1;
			windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;
			windowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
			windowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		}
	}

	/**
	 * 创建悬浮球
	 */
	private void createFloatBall() {
		if (captureFloatWindow == null) {
			captureFloatWindow = new CaptureFloatWindow(
					context.getApplicationContext());
		}

		captureFloatWindow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				onFloatBubbleClick();
			}
		});
	}

	// 显示悬浮球
	private void showFloatingBall() {
		windowManagerParams.x = defaultScreenWidth;
		windowManagerParams.y = FLOAT_WINDOW_DEFAULT_Y;
		if (windowManager != null && captureFloatWindow != null) {
			windowManager.addView(captureFloatWindow, windowManagerParams);
		}
		bCaptureFloatWindowShowing = true;
	}

	// 相应悬浮球的点击事件
	private void onFloatBubbleClick() {
		// 先确定是否可以点击悬浮球
		if (bCanClickFloatBubble) {
			// 如果正在录制
			if (isRecording()) {
				// 显示提示窗口
				showStopNotifycationWindow();
			} else {
				// 如果要显示
				if (bShowMainScreenCaptureWindow) {
					showMainFloatWindow();
				} else {
					hideMainFloatWindow();
				}
				// 切换一下开关
				bShowMainScreenCaptureWindow = !bShowMainScreenCaptureWindow;
			}
		}
	}

	// 显示主窗口
	private void showMainFloatWindow() {

		onCalculatePosition();

		// 显示主窗口
		showMainScreenCaptureWindow(mainWindowXCoordinate,
				mainWindowYCoordinate);

		// 显示主窗口之后就禁止移动
		captureFloatWindow.setMoveFlag(false);
	}

	// 隐藏主窗口
	private void hideMainFloatWindow() {
		if (windowManager != null && mainScreenCaptureWindow != null) {
			// 隐藏主窗口
			windowManager.removeView(mainScreenCaptureWindow);
			mainScreenCaptureWindow = null;
		}
		if (captureFloatWindow != null) {
			// 隐藏主窗口之后，悬浮球就可以移动了
			captureFloatWindow.setMoveFlag(true);
		}
		bShowMainScreenCaptureWindow = false;
	}

	// 计算浮窗需要显示的初始位置
	private void onCalculatePosition() {

		mViewPoint = captureFloatWindow.getViewCoordinate();

		if (mViewPoint != null) {

			// 计算主窗口显示的X坐标
			if (mViewPoint.x == 0) {
				mainWindowXCoordinate = captureFloatWindow
						.getFloatWindowWidth();
			} else {
				mainWindowXCoordinate = defaultScreenWidth
						- captureFloatWindow.getFloatWindowWidth();
			}

			if (mViewPoint.y == 0) {

				// 主窗口Y坐标
				mainWindowYCoordinate = FLOAT_WINDOW_DEFAULT_Y;
			} else {

				// 主窗口Y坐标
				mainWindowYCoordinate = mViewPoint.y;
			}
		} else {

			mainWindowXCoordinate = captureFloatWindow.getFloatWindowWidth();
			mainWindowYCoordinate = FLOAT_WINDOW_DEFAULT_Y;
		}
	}

	// 显示操作主窗口
	private void showMainScreenCaptureWindow(int viewXCoordinate,
			int viewYCoordinate) {

		if (mainScreenCaptureWindow == null) {
			mainScreenCaptureWindow = new MainScreenCaptureFloatWindow(
					context.getApplicationContext(), new MainWindowListener() {

						@Override
						public void onVideoEditor() {

							// 编辑
							handleSeniorEditorClick();
						}

						@Override
						public void onVideoCaptureConfiguration() {

							// 设置
							handleSettingClick();
						}

						@Override
						public void onVideoCapture() {

							// 录制
							doPrepare();
						}

						@Override
						public void onCloseWindow() {
							// 关闭窗口
							handleCloseWindow();
						}

					});

		}

		if (viewXCoordinate == captureFloatWindow.getFloatWindowWidth()) {
			// 悬浮球靠左对齐的时候
			windowManagerParams.x = viewXCoordinate;
			// 向左显示
			mainScreenCaptureWindow.showMainWindowLeft();
		} else {
			// 悬浮球靠右对齐的时候(x = 屏幕宽度-悬浮球宽度-主窗口宽度)
			windowManagerParams.x = viewXCoordinate - mainFloatWindowWidth;
			// 向右显示
			mainScreenCaptureWindow.showMainWindowRight();
		}

		windowManagerParams.y = viewYCoordinate;
		if (windowManager != null && mainScreenCaptureWindow != null) {
			windowManager.addView(mainScreenCaptureWindow, windowManagerParams);
		}
	}

	/**
	 * 显示提示对话框
	 * 
	 */
	private void createStopNotifycationWindow(int viewXCoordinate,
			int viewYCoordinate) {
		if (videoCaptureStopWindow == null) {
			videoCaptureStopWindow = new VideoCaptureStopWindow(
					context.getApplicationContext(), new StopCaptureListener() {

						@Override
						public void onStopCapture() {
							// 停止录制
							onStopVideoCapture();
						}

						@Override
						public void onCancelStop() {
							// 取消
							handleCancelClick();
						}

						@Override
						public void onPauseCapture(TextView tvPause) {
							onPauseVideoCapture();
							tvPause.setText(isPausing ? R.string.float_video_capture_continue_button_text
									: R.string.float_video_capture_pause_button_text);
						}
					});
		}

		if (viewXCoordinate == captureFloatWindow.getFloatWindowWidth()) {
			// 悬浮球靠左对齐的时候
			windowManagerParams.x = viewXCoordinate;
		} else {

			// 悬浮球靠右对齐的时候(x = 屏幕宽度-悬浮球宽度-主窗口宽度)
			windowManagerParams.x = viewXCoordinate
					- videoCaptureStopWindowWidth;

		}

		windowManagerParams.y = viewYCoordinate;

		if (windowManager != null && videoCaptureStopWindow != null) {

			windowManager.addView(videoCaptureStopWindow, windowManagerParams);

		}
	}

	private void showStopNotifycationWindow() {
		// 计算要显示的位置
		onCalculatePosition();
		// 显示
		createStopNotifycationWindow(mainWindowXCoordinate,
				mainWindowYCoordinate);
		// 悬浮球不可以点击
		bCanClickFloatBubble = false;
		captureFloatWindow.setMoveFlag(false);
	}

	/**
	 * 取消
	 */
	private void handleCancelClick() {
		if (videoCaptureStopWindow != null) {
			// 把提示窗口隐藏
			windowManager.removeView(videoCaptureStopWindow);
		}
		bCanClickFloatBubble = true;
		if (null != captureFloatWindow) {
			captureFloatWindow.setMoveFlag(true);
		}
	}

	private void doPrepare() {

		RecorderShare config = new RecorderShare(
				captureFloatWindow.getContext());

		int type = config.getRecorderType();
		String[] outs = config.getRecorderOutPaths();

		String outPath = outs[type];

		if (!TextUtils.isEmpty(outPath)) {
			Log.d("out:", outPath + "--");
			onStartVideoCapture(type, outPath);
		} else {
			Toast.makeText(context, "输出地址为null", 1000).show();
		}

	}

	/**
	 * 隐藏悬浮球
	 */
	private void hideMainFloatBubble() {
		hideMainFloatWindow();
		// 现在可以点击悬浮球停止录屏
		bCanClickFloatBubble = true;
		if (captureFloatWindow != null) {
			windowManager.removeView(captureFloatWindow);
			captureFloatWindow = null;
		}
		bCaptureFloatWindowShowing = false;
	}

	// 处理设置点击
	private void handleSettingClick() {
		hideMainFloatBubble();
		// 返回到主界面
		Intent intent = new Intent(context, RecSdkConfigActivity.class);
		// 传递给设置界面
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	// 处理从主操作面返回点击
	private void handleCloseWindow() {
		bShowMainScreenCaptureWindow = false;
		// 返回到悬浮球界面
		hideMainFloatWindow();
	}

	/**
	 * 开始计时
	 */
	private void startTimer() {
		// 开始计时
		timerCount = System.currentTimeMillis();
		timer.start();
	}

	// 录屏计时器
	private CountDownTimer timer = new CountDownTimer(Integer.MAX_VALUE, 1000) {
		// 计数器
		private int count = 0;

		@Override
		public void onTick(long millisUntilFinished) {
			if (captureFloatWindow != null) {
				count++;
				if (!isPausing) {
					// 处理闪烁
					captureFloatWindow
							.getTextView()
							.setBackgroundResource(
									count % 2 == 0 ? R.drawable.video_capture_background_stop_n
											: R.drawable.video_capture_background_stop_p);

					if (nType == 0) {
						captureFloatWindow.getTextView().setText(
								stringForTime(System.currentTimeMillis()
										- timerCount));
					} else {
						captureFloatWindow.getTextView().setText("直播中");
					}
				} else {
					captureFloatWindow.getTextView().setBackgroundResource(
							R.drawable.video_capture_background_stop_n);
					if (nType == 1 || nType == 2) {
						captureFloatWindow.getTextView().setText("暂停中");
					}
				}
			}
		}

		@Override
		public void onFinish() {
			// Log.d(TAG, "计时结束");
		}

		/**
		 * 毫秒数转换为时间格式化字符串
		 * 
		 * @param timeMs
		 * @return
		 */
		public String stringForTime(long timeMs) {
			return stringForTime(timeMs, false);
		}

		/**
		 * 毫秒数转换为时间格式化字符串 支持是否显示小时
		 * 
		 * @param timeMs
		 * @return
		 */
		public String stringForTime(long timeMs, boolean existsHours) {

			return DateTimeUtils.stringForTime(timeMs, existsHours, true, true,
					false);

		}
	};

	private FloatWindowUtils() {
	}
}
