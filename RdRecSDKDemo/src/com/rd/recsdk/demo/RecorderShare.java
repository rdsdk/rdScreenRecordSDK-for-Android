package com.rd.recsdk.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;

public class RecorderShare {

	private SharedPreferences sp;

	public RecorderShare(Context context) {
		sp = context.getSharedPreferences("share_recorder_info",
				Context.MODE_PRIVATE);

	}

	private final String RECORDER_TYPE = "recordertype";
	private final String RECORDER_LOCAL = "recorderlocal";
	private final String RECORDER_RTMP = "recordertmp";
	private final String RECORDER_UID = "recorderuid";

	/**
	 * 
	 * @param type
	 *            // 0 保存到本地 ， 1 rtmp 直播 ，2 uid 直播
	 * @param local
	 * @param rtmp
	 * @param uid
	 */
	public void save(int type, String local, String rtmp, String uid) {

		if (null != sp) {
			Editor editor = sp.edit();
			editor.putInt(RECORDER_TYPE, type);
			editor.putString(RECORDER_LOCAL, local);
			editor.putString(RECORDER_RTMP, rtmp);
			editor.putString(RECORDER_UID, uid);
			editor.commit();
		}
	}

	/**
	 * 
	 * @return    0 保存到本地 ， 1 rtmp 直播 ，2 uid 直播
	 */
	public int getRecorderType() {
		if (null != sp) {
			return sp.getInt(RECORDER_TYPE, 0);
		}
		return 0;
	}

	public String[] getRecorderOutPaths() {
		String[] out = new String[3];
		if (null != sp) {
			out[0] = sp.getString(RECORDER_LOCAL,
					Environment.getExternalStorageDirectory() + "/zzzTest.mp4");
			out[1] = sp.getString(RECORDER_RTMP, "rtmp://");
			out[2] = sp.getString(RECORDER_UID, "aabbcc");
		}
		return out;
	}

}
