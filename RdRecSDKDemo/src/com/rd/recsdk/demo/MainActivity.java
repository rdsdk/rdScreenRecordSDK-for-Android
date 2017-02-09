package com.rd.recsdk.demo;

import java.util.HashMap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.rd.recsdk.RecSdkManager;
import com.rd.recsdk.utils.AppUtil;
import com.rd.recsdk.utils.FloatWindowUtils;
import com.rd.recsdk.utils.PermissionUtils;
import com.rd.recsdk.utils.PermissionUtils.IPermissionListener;

/**
 * RdRecSdk演示首页
 * 
 * @author abreal
 * 
 */
public class MainActivity extends Activity {
	private TextView etLivingUid, etLivingRtmp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (AppUtil.hasM()) {
			checkPermission();
		} else {
			init();
		}
	}

	private void init() {
		AppImp.init(this);
		initView();
	}

	private final int REQUSET_PERMISSION = 101;

	@SuppressLint("NewApi")
	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUSET_PERMISSION) {
			doNext(requestCode, grantResults, permissions);
		}
	}

	@SuppressLint("NewApi")
	private void doNext(int requestCode, int[] grantResults,
			String[] permissions) {
		PermissionUtils.doNext(this, grantResults, permissions,
				new com.rd.recsdk.utils.PermissionUtils.IPermissionListener() {

					@Override
					public void onPermission(int permissionResult) {
						if (permissionResult == PackageManager.PERMISSION_GRANTED) {
							init();
						} else if (permissionResult == PackageManager.PERMISSION_DENIED) {
							checkPermission();
						}

					}
				});

	}

	private void checkPermission() {
		PermissionUtils.checkPermission(
				MainActivity.this,
				REQUSET_PERMISSION,
				new IPermissionListener() {
					@Override
					public void onPermission(int permissionResult) {
						if (permissionResult == PackageManager.PERMISSION_GRANTED) {
							init();
						}
					}
				}, Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.RECORD_AUDIO);
	}

	/**
	 * 响应点击
	 * 
	 * @param view
	 */
	public void onClick(View view) {

	}

	@Override
	protected void onDestroy() {
		FloatWindowUtils.getInstance().hideFloatWindow(this, true);
		RecSdkManager.exit(this);
		super.onDestroy();
	}

	/**
	 * 显示悬浮窗
	 */
	@SuppressLint("NewApi")
	private void handleFloatWindow() {
		if (!FloatWindowUtils.getInstance().isFloatWindowShowing()) {
			if (AppUtil.hasM() && !Settings.canDrawOverlays(this)) {
				Toast.makeText(MainActivity.this, "当前无权限，请授权！",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(
						Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
						Uri.parse("package:" + getPackageName()));
				startActivityForResult(intent, 105);
			} else {
				FloatWindowUtils.getInstance().showFloatWindow(this);
			}

		} else {
			FloatWindowUtils.getInstance().hideFloatWindow(this, false);
		}
	}

	private void initView() {

		etLivingUid = (TextView) findViewById(R.id.etLivingUid);
		etLivingRtmp = (TextView) findViewById(R.id.etLivingRtmp);

		findViewById(R.id.mBtnPlayLiving).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						final String uid = etLivingUid.getText().toString();
						if (TextUtils.isEmpty(uid)) {
							onToast("正在直播的Uid为NULL");
						} else {
							int aType = RecSdkManager.getAuthType();
							if (aType == RecSdkManager.AT_UID
									|| aType == RecSdkManager.AT_URL_OR_UID) {
								RecSdkManager.getLivingUid(uid,
										new RecSdkManager.ILivingListener() {

											@Override
											public void getLiving(
													HashMap<String, String> maps) {
												if (maps.containsKey("error")) {
													onToast(maps.get("error"));
												} else {
													String rtmp = maps
															.get("rtmp");
													String m3u8 = maps
															.get("m3u8");
													Log.d("getdata", "rtmp->"
															+ rtmp);
													Log.d("getdata", "m3u8->"
															+ m3u8);
													PlayActivity.gotoPlay(
															MainActivity.this,
															rtmp,
															maps.get("thumb"),
															maps.get("title"),
															maps.get("liveId"));
												}

											}
										});
							} else {
								onToast("Uid直播功能已过期！");
							}
						}
					}
				});

		findViewById(R.id.mBtnPlayLivingRtmp).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						final String lrtmp = etLivingRtmp.getText().toString();
						if (TextUtils.isEmpty(lrtmp)) {
							onToast("正在直播的流为NULL");
						} else {
							PlayActivity.gotoPlay(MainActivity.this, lrtmp, "",
									"未知", null);
						}
					}
				});

		findViewById(R.id.show_float_window_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						handleFloatWindow();
					}
				});
		findViewById(R.id.show_configuration_button).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(MainActivity.this,
								RecSdkConfigActivity.class));

					}
				});

	}

	private void onToast(String msg) {
		Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
	}
}
