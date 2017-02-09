package com.rd.recsdk.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

public class AppUtil {

	/**
	 * 调转到应用权限设置
	 * 
	 * @param context
	 * @param packagename
	 */

	public static void gotoAppInfo(Context context, String packagename) {
		try {
			Uri packageURI = Uri.parse("package:" + packagename);
			Intent intent = new Intent(
					Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Support Android M
	 * 
	 * @return
	 */
	public static boolean hasM() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

}
