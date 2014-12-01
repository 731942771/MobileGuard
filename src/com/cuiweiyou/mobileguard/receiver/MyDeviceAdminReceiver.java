package com.cuiweiyou.mobileguard.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

/***
 * 设备管理员接收器 接收管理员级别的设备相关消息
 */
public class MyDeviceAdminReceiver extends DeviceAdminReceiver {

	/** 没有需要重写的方法，所以可啥也不做。以下代码谨供参看 **/

	@Override
	public void onEnabled(Context context, Intent intent) {
		Toast.makeText(context, "设备管理：可用", 0).show();
	}

	@Override
	public void onDisabled(Context context, Intent intent) {
		Toast.makeText(context, "设备管理：不可用", 0).show();
	}

	@Override
	public CharSequence onDisableRequested(Context context, Intent intent) {
		return "这是一个可选的消息，警告有关禁止用户的请求";
	}

	@Override
	public void onPasswordChanged(Context context, Intent intent) {
		Toast.makeText(context, "设备管理：密码己经改变", 0).show();
	}

	@Override
	public void onPasswordFailed(Context context, Intent intent) {
		Toast.makeText(context, "设备管理：改变密码失败", 0).show();
	}

	@Override
	public void onPasswordSucceeded(Context context, Intent intent) {
		Toast.makeText(context, "设备管理：改变密码成功", 0).show();
	}

}
