package com.rd.recsdk.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.rd.recsdk.demo.R;
import com.rd.recsdk.demo.RecSdkConfigActivity;

public class CaptureSettingBar extends LinearLayout implements
		View.OnClickListener {

	public CaptureSettingBar(Context context) {
		super(context);
		View.inflate(context, R.layout.item_capture_setting_bar, this);
		LinearLayout lCaptureSetting = (LinearLayout) findViewById(R.id.ll_capture_setting);
		lCaptureSetting.setOnClickListener(this);
	}

	public CaptureSettingBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		View.inflate(context, R.layout.item_capture_setting_bar, this);
		LinearLayout lCaptureSetting = (LinearLayout) findViewById(R.id.ll_capture_setting);
		lCaptureSetting.setOnClickListener(this);
	}

	public CaptureSettingBar(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		View.inflate(context, R.layout.item_capture_setting_bar, this);
		LinearLayout lCaptureSetting = (LinearLayout) findViewById(R.id.ll_capture_setting);
		lCaptureSetting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		getContext().startActivity(
				new Intent(getContext(), RecSdkConfigActivity.class));
	}
}