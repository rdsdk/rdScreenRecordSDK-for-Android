package com.rd.recsdk.demo;

import android.app.Application;
import android.content.Context;

import com.rd.recsdk.RecSdkManager;

public class AppImp extends Application {
	/**
	 * 锐动云控平台申请的appKey,Secret<br>
	 * 锐动云控平台地址：<a href="http://dianbook.17rd.com/business/app/myapp">
	 * http://dianbook.17rd.com/business/app/myapp</a><br>
	 */
	private static final String APPKEY = "a358230e8a715ce8";
	private static final String APPSECRET = "48cead88b9d17ba321b6c538d34d4d6fXCmy21UPwAOlmjzG8HbRmRKKvjDz7VYdHOwgg9P7+Q2S51RnqoeIGYjPcQFD51j85oB2zdAR+Yzt6J2XAObPj6CQCZ9HfnSpuZudAFtzcNC8TPGKtzU0CWo+zQJPDobGBb7Wq77QHpT/nVPLpsfx1ZDAEnWZbhP9kkvsmQ7L07zbHXjk8fUQVbSwz92/wEXfVzO8Y72IeDDUpjWZr8w6V3WVifSMnBfjEgVJ/5dcFoiXJlfpyUcjp6LqaycPtH189onWpRtNUv1G6ML7pBvl13tYQkaXuvwQf87gT11gFSeEjOWr37g4Uia2JdenisV8nuIrbGzOBJiUeCMBKS+GUMuO4pWdpKTOSB1MT/tzoq8k8IqJ9+RXwjKTjyIDlzG8CNl6CSpT6Bb28uZ6/FPTMONGfpHowNIuZPn3eDEbU+HsUtCaBnuMI8rvEyKGbrPXEhBv30Use23fOvgmVUWWQQPqlBQaJhlOM/pbxPAqtj2A3YSHoDoEWqU2+Z6LG7RttU//fJlIK3d6ZFP8k0kypw==";

	@Override
	public void onCreate() {
		super.onCreate();
		/**
		 * 确保程序崩溃后再次初始化sdk环境
		 */
		init(this);

	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	static void init(Context context) {
		RecSdkManager.initilize(context, APPKEY, APPSECRET, true);
	}

}
