package com.rd.recsdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rd.recsdk.RecSdkConfig;

/**
 * 录制SDK配置页
 * 
 * @author abreal
 * 
 */
public class RecSdkConfigActivity extends Activity implements OnClickListener {
	public static final String CAN_NOT_BE_USED_IN_CURRENT_SYSTEM = "(当前系统不适用)";

	public static final String VIDEO_DVLS[] = { "480P", "640P", "720P" };
	public static final String VIDEO_HARD_FPS[] = { "30FPS", "25FPS", "20FPS" };
	public static final String VIDEO_SOFT_FPS[] = { "25FPS", "20FPS", "15FPS" };
	public static final String VIDEO_HARD_RATE[] = { "1Mbps", "2Mbps", "4Mbps" };
	public static final String VIDEO_SOFT_RATE[] = { "512Kbps", "1Mbps",
			"2Mbps" };
	public static final String VIDEO_CAP_MODE[] = { "适用系统：4.1以上， 是否需要ROOT：是",
			"适用系统：4.2以上， 是否需要ROOT：是", "适用系统：5.0以上， 是否需要ROOT：否" };

	private LinearLayout llVdl, llVdlGroup;
	private TextView tvVdlInfo;
	private TextView tvVdlLow, tvVdlNormal, tvVdlHigh;
	private LinearLayout llCapMode, llCapModeGroup;
	private TextView tvCapModeInfo;
	private TextView tvCapModeSys, tvCapModeVirtual, tvCapModeLL;
	private LinearLayout llEncodeMode, llEncodeModeGroup;
	private TextView tvEncodeModeHard, tvEncodeModeSoft;
	private LinearLayout llIsAudioRecord, llIsAudioRecordGroup;
	private TextView tvIsAudioRecordYes, tvIsAudioRecordNo;
	private LinearLayout llIsSwapColor, llIsSwapColorGroup;
	private TextView tvIsSwapColorYes, tvIsSwapColorNo;

	private RecSdkConfig config;

	private TextView tvLocal;
	private EditText tvUid, tvRtmp;

	private RecorderShare outConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recsdk_config_layout);

		llVdl = (LinearLayout) findViewById(R.id.ll_vdl);
		llVdlGroup = (LinearLayout) findViewById(R.id.ll_vdl_group);
		tvVdlInfo = (TextView) findViewById(R.id.tv_vdl_info);
		tvVdlLow = (TextView) findViewById(R.id.tv_vdl_low);
		tvVdlNormal = (TextView) findViewById(R.id.tv_vdl_normal);
		tvVdlHigh = (TextView) findViewById(R.id.tv_vdl_high);

		llCapMode = (LinearLayout) findViewById(R.id.ll_cap_mode);
		llCapModeGroup = (LinearLayout) findViewById(R.id.ll_cap_mode_group);
		tvCapModeInfo = (TextView) findViewById(R.id.tv_cap_mode_info);
		tvCapModeSys = (TextView) findViewById(R.id.tv_cap_mode_sys);
		tvCapModeVirtual = (TextView) findViewById(R.id.tv_cap_mode_virtual);
		tvCapModeLL = (TextView) findViewById(R.id.tv_cap_mode_ll);

		llEncodeMode = (LinearLayout) findViewById(R.id.ll_encode_mode);
		llEncodeModeGroup = (LinearLayout) findViewById(R.id.ll_encode_mode_group);
		tvEncodeModeHard = (TextView) findViewById(R.id.tv_encode_mode_hard);
		tvEncodeModeSoft = (TextView) findViewById(R.id.tv_encode_mode_soft);

		llIsAudioRecord = (LinearLayout) findViewById(R.id.ll_is_audio_record);
		llIsAudioRecordGroup = (LinearLayout) findViewById(R.id.ll_is_audio_record_group);
		tvIsAudioRecordYes = (TextView) findViewById(R.id.tv_is_audio_record_yes);
		tvIsAudioRecordNo = (TextView) findViewById(R.id.tv_is_audio_record_no);

		llIsSwapColor = (LinearLayout) findViewById(R.id.ll_is_swap_color);
		llIsSwapColorGroup = (LinearLayout) findViewById(R.id.ll_is_swap_color_group);
		tvIsSwapColorYes = (TextView) findViewById(R.id.tv_is_swap_color_yes);
		tvIsSwapColorNo = (TextView) findViewById(R.id.tv_is_swap_color_no);

		config = new RecSdkConfig();
		config.loadFromSharedPreferences(this);
		outConfig = new RecorderShare(this);
		updateUI();
		setListener();
		tvLocal = (TextView) findViewById(R.id.out_local);
		tvUid = (EditText) findViewById(R.id.out_uid);
		tvRtmp = (EditText) findViewById(R.id.out_rtmp);

		out_type = outConfig.getRecorderType();
		linear_local = (LinearLayout) findViewById(R.id.recorder_type_local);
		linear_rtmp = (LinearLayout) findViewById(R.id.recorder_type_rtmp);
		linear_uid = (LinearLayout) findViewById(R.id.recorder_type_uid);
		linear_uid.setOnClickListener(mlinearListener);
		linear_rtmp.setOnClickListener(mlinearListener);
		linear_local.setOnClickListener(mlinearListener);

		String[] arr = outConfig.getRecorderOutPaths();
		tvLocal.setText(arr[0]);
		tvRtmp.setText(arr[1]);
		tvUid.setText(arr[2]);
		if (out_type == 1) {
			checkRtmp();
			tvRtmp.requestFocus();
		} else if (out_type == 2) {
			checkUid();
			tvUid.requestFocus();
		} else {
			checkLocal();
		}
		new Handler().postDelayed(new Runnable() { // 防止oncreate() 焦点干扰

					@Override
					public void run() {
						tvUid.setOnClickListener(mlinearListener);
						tvUid.setOnFocusChangeListener(new OnFocusChangeListener() {

							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if (hasFocus) {
									checkUid();
								}

							}
						});
						tvRtmp.setOnClickListener(mlinearListener);
						tvRtmp.setOnFocusChangeListener(new OnFocusChangeListener() {

							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if (hasFocus) {
									checkRtmp();
								}
							}
						});
					}
				}, 1000);
	}

	private void checkLocal() {
		tvLocal.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.select_music_scan_content_select_all_c, 0);
		tvUid.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvRtmp.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		out_type = 0;
	}

	private void checkRtmp() {
		tvLocal.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvUid.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvRtmp.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.select_music_scan_content_select_all_c, 0);
		out_type = 1;
	}

	private void checkUid() {
		tvLocal.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvUid.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.select_music_scan_content_select_all_c, 0);
		tvRtmp.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		out_type = 2;
	}

	private OnClickListener mlinearListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.recorder_type_local:
				tvRtmp.clearFocus();
				tvUid.clearFocus();
				checkLocal();
				break;

			case R.id.recorder_type_rtmp:
			case R.id.out_rtmp:

				checkRtmp();
				break;

			case R.id.recorder_type_uid:
			case R.id.out_uid:
				checkUid();
				break;

			default:
				break;
			}

		}
	};
	private LinearLayout linear_local, linear_rtmp, linear_uid;

	private int out_type = 0;

	private String tempLocal, tempRtmp, tempUid;

	@Override
	protected void onPause() {
		super.onPause();
		tempUid = tvUid.getText().toString().trim();
		tempRtmp = tvRtmp.getText().toString().trim();
		tempLocal = tvLocal.getText().toString().trim();

	}

	@Override
	protected void onDestroy() {
		saveSetting();
		outConfig.save(out_type, tempLocal, tempRtmp, tempUid);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.ll_vdl) {
			changeVisible(llVdlGroup);
		} else if (id == R.id.ll_cap_mode) {
			changeVisible(llCapModeGroup);
		} else if (id == R.id.ll_encode_mode) {
			changeVisible(llEncodeModeGroup);
		} else if (id == R.id.ll_is_audio_record) {
			changeVisible(llIsAudioRecordGroup);
		} else if (id == R.id.ll_is_swap_color) {
			changeVisible(llIsSwapColorGroup);
		}
	}

	private void setListener() {
		llVdl.setOnClickListener(this);
		llCapMode.setOnClickListener(this);
		llEncodeMode.setOnClickListener(this);
		llIsAudioRecord.setOnClickListener(this);
		llIsSwapColor.setOnClickListener(this);

		tvVdlLow.setOnClickListener(infoItemListener);
		tvVdlNormal.setOnClickListener(infoItemListener);
		tvVdlHigh.setOnClickListener(infoItemListener);
		tvCapModeSys.setOnClickListener(infoItemListener);
		if (android.os.Build.VERSION.SDK_INT > 16) {
			tvCapModeVirtual.setOnClickListener(infoItemListener);
		} else {
			tvCapModeVirtual.setText(tvCapModeVirtual.getText().toString()
					+ CAN_NOT_BE_USED_IN_CURRENT_SYSTEM);
		}
		if (android.os.Build.VERSION.SDK_INT > 20) {
			tvCapModeLL.setOnClickListener(infoItemListener);
		} else {
			tvCapModeLL.setText(tvCapModeLL.getText().toString()
					+ CAN_NOT_BE_USED_IN_CURRENT_SYSTEM);
		}
		tvEncodeModeHard.setOnClickListener(infoItemListener);
		tvEncodeModeSoft.setOnClickListener(infoItemListener);
		tvIsAudioRecordYes.setOnClickListener(infoItemListener);
		tvIsAudioRecordNo.setOnClickListener(infoItemListener);
		tvIsSwapColorYes.setOnClickListener(infoItemListener);
		tvIsSwapColorNo.setOnClickListener(infoItemListener);
	}

	public void restoreSetting(View view) {
		config.setDefaultConfig();
		config.saveToSharedPreferences(this);
		updateUI();
	}

	public void saveSetting() {
		config.saveToSharedPreferences(this);
	}

	private void updateUI() {
		tvVdlLow.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvVdlNormal.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvVdlHigh.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvCapModeSys.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvCapModeVirtual.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvCapModeLL.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvEncodeModeHard.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvEncodeModeSoft.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvIsAudioRecordYes.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvIsAudioRecordNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvIsSwapColorYes.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tvIsSwapColorNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

		int vdl = config.getRecVideoDefinition();
		if (vdl == RecSdkConfig.VIDEO_DEFINITION_LOW) {
			tvVdlLow.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.select_music_scan_content_select_all_c, 0);
		} else if (vdl == RecSdkConfig.VIDEO_DEFINITION_NORMAL) {
			tvVdlNormal.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.select_music_scan_content_select_all_c, 0);
		} else if (vdl == RecSdkConfig.VIDEO_DEFINITION_HIGH) {
			tvVdlHigh.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.select_music_scan_content_select_all_c, 0);
		}

		switch (config.getCapMode()) {
		case RecSdkConfig.CAPTURE_SYSTEM:
			tvCapModeSys.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.select_music_scan_content_select_all_c, 0);
			break;
		case RecSdkConfig.CAPTURE_VIRTUAL:
			tvCapModeVirtual.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.select_music_scan_content_select_all_c, 0);
			break;
		case RecSdkConfig.CAPTURE_LL_HIGH:
			tvCapModeLL.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.select_music_scan_content_select_all_c, 0);
			break;
		}

		switch (config.getRecEncodeMode()) {
		case RecSdkConfig.ENCODER_HARD:
			tvEncodeModeHard.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.select_music_scan_content_select_all_c, 0);
			break;
		case RecSdkConfig.ENCODER_SOFT:
			tvEncodeModeSoft.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.select_music_scan_content_select_all_c, 0);
			break;
		}

		if (config.isRecAudio()) {
			tvIsAudioRecordYes.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.select_music_scan_content_select_all_c, 0);
		} else {
			tvIsAudioRecordNo.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.select_music_scan_content_select_all_c, 0);
		}

		if (config.isSwapColor()) {
			tvIsSwapColorYes.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.select_music_scan_content_select_all_c, 0);
		} else {
			tvIsSwapColorNo.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.select_music_scan_content_select_all_c, 0);
		}
		setVdlAndCapModeInfo();
	}

	private void setVdlAndCapModeInfo() {
		int vdl = config.getRecVideoDefinition();
		boolean isHardEnc = config.getRecEncodeMode() == RecSdkConfig.ENCODER_HARD;
		tvVdlInfo.setText("分辨率：" + VIDEO_DVLS[vdl] + " 码率："
				+ (isHardEnc ? VIDEO_HARD_RATE[vdl] : VIDEO_SOFT_RATE[vdl])
				+ " 帧率："
				+ (isHardEnc ? VIDEO_HARD_FPS[vdl] : VIDEO_SOFT_FPS[vdl]));
		tvCapModeInfo.setText(VIDEO_CAP_MODE[config.getCapMode()]);
	}

	private void changeVisible(View view) {
		view.setVisibility((view.getVisibility() == View.GONE) ? View.VISIBLE
				: View.GONE);
	}

	private OnClickListener infoItemListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.tv_vdl_low) {
				config.setRecVideoDefinition(RecSdkConfig.VIDEO_DEFINITION_LOW);
			} else if (id == R.id.tv_vdl_normal) {
				config.setRecVideoDefinition(RecSdkConfig.VIDEO_DEFINITION_NORMAL);
			} else if (id == R.id.tv_vdl_high) {
				config.setRecVideoDefinition(RecSdkConfig.VIDEO_DEFINITION_HIGH);
			} else if (id == R.id.tv_cap_mode_sys) {
				config.setCapMode(RecSdkConfig.CAPTURE_SYSTEM);
			} else if (id == R.id.tv_cap_mode_virtual) {
				config.setCapMode(RecSdkConfig.CAPTURE_VIRTUAL);
			} else if (id == R.id.tv_cap_mode_ll) {
				config.setCapMode(RecSdkConfig.CAPTURE_LL_HIGH);
			} else if (id == R.id.tv_encode_mode_hard) {
				config.setRecEncodeMode(RecSdkConfig.ENCODER_HARD);
			} else if (id == R.id.tv_encode_mode_soft) {
				config.setRecEncodeMode(RecSdkConfig.ENCODER_SOFT);
			} else if (id == R.id.tv_is_audio_record_yes) {
				config.setRecAudio(true);
			} else if (id == R.id.tv_is_audio_record_no) {
				config.setRecAudio(false);
			} else if (id == R.id.tv_is_swap_color_yes) {
				config.setSwapColor(true);
			} else if (id == R.id.tv_is_swap_color_no) {
				config.setSwapColor(false);
			}
			updateUI();
		}
	};
}