package com.rd.recsdk.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rd.recsdk.demo.R;


public class VideoCaptureStopWindow extends LinearLayout {

	// 上下文
	private Context context;

	// 布局文件
	private View view;

	// 停止录制按钮
	private TextView pauseTextView;

	// 接口
	private StopCaptureListener stopCaptureListener;

	public VideoCaptureStopWindow(Context context,
			StopCaptureListener stopCaptureListener) {
		super(context);

		this.context = context;
		this.stopCaptureListener = stopCaptureListener;

		inflateLayout();
	}

	// 填充布局文件
	private void inflateLayout() {
		// 填充布局
		view = LayoutInflater.from(context).inflate(
				R.layout.float_video_capture_stop_layout, null);

		// 停止录制
		view.findViewById(R.id.tv_stop_capture).setOnClickListener(
				clickListener);
		view.findViewById(R.id.tv_pause_capture).setOnClickListener(
				clickListener);
		// 取消按钮
		view.findViewById(R.id.tv_cancel_stop)
				.setOnClickListener(clickListener);

		// 为浮窗添加布局
		addView(view);
	}

	// 相应点击事件
	private View.OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (stopCaptureListener != null) {
				int id = v.getId();
				if (id == R.id.tv_stop_capture) {
					stopCaptureListener.onStopCapture();
				} else if (id == R.id.tv_cancel_stop) {
					stopCaptureListener.onCancelStop();
				} else if (id == R.id.tv_pause_capture) {
					stopCaptureListener.onPauseCapture((TextView) v);
				}
			}
		}
	};

	/**
	 * 停止接口
	 * 
	 * @author jeck
	 * 
	 */
	public interface StopCaptureListener {

		// 停止录制
		public void onStopCapture();

		// 取消
		public void onCancelStop();

		/**
		 * 暂停录制
		 * 
		 * @param tvPause
		 */
		public void onPauseCapture(TextView tvPause);

	}
}
