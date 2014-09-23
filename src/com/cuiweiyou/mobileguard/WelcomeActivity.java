package com.cuiweiyou.mobileguard;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 第一个界面的作用：
 * 1.展示产品Logo
 * 2.应用程序初始化（元数据-数据库、配置信息、尺寸、登录信息）
 * 3.检查新版本
 * 4.检查当前程序的注册信息、版权
 * 5.检查网络
 * 6.。。。。。。
 */
/**
 * 创建项目时，Theme为None。本Activity继承自ActionBarActivity，测试时报错： Unable to start
 * activity ComponentInfo : java.lang.IllegalStateException: You need to use a
 * Theme.AppCompat theme (or descendant) with this activity.
 */
public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		// 获取控件
		TextView tv_version = (TextView) findViewById(R.id.tv_version);
		// 更新文字
		tv_version.setText("版本 " + getVersionName());
	}

	/***
	 * 动态获取应用版本
	 */
	private String getVersionName() {
		// 1.应用包管理器，管理应用程序包,通过它就可以获取应用程序信息
		PackageManager packageManager = getPackageManager();

		try {
			// 2.应用包信息，获取AndroidManifest.xml中的配置信息
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);

			// 3.版本名
			String versionName = packageInfo.versionName;

			// 4.return
			return versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "读取失败";
	}

}
