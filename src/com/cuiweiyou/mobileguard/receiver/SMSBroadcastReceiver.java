package com.cuiweiyou.mobileguard.receiver;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cuiweiyou.mobileguard.R;
import com.cuiweiyou.mobileguard.service.MyLocationService;

/**
 * 接收安全号码发来的短信指令 ，根据指令，锁定本手机，或其它动作 本例仍然安装在4.1.2上测试成功
 **/
@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.FROYO)
public class SMSBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "SMS";

	/** 设备管理器 **/
	private DevicePolicyManager dpm;

	@SuppressLint("NewApi")
	public void onReceive(Context context, Intent intent) {

		SharedPreferences sp = context.getSharedPreferences( "mobileguard_setting_config", Context.MODE_PRIVATE);
		String sim_guard_phone = sp.getString("sim_guard_phone", null);

		Object[] objects = (Object[]) intent.getExtras().get("pdus");

		for (Object object : objects) {
			// 信息对象
			SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
			// 发短信来的号码
			String address = message.getOriginatingAddress();
			// 短信内容
			String body = message.getMessageBody();

			// 如果来信者是安全号码，并且是某个指令
			// if(address.equals(sim_guard_phone) && body.equals("#*sim*#")){
			if (body.equals("#*sim*#")) {
				// 终止本广播，放弃被系统的默认短信应用或其它应用接收到
				abortBroadcast();
				
				// 执行指令
				Toast.makeText(context, "播放报警！！！", 0).show();
				Log.i(TAG, "播放报警！！");
				MediaPlayer player = MediaPlayer.create(context, R.raw.warmring);
				player.start();

			} else if (body.equals("#*gps*#")) {

				// 终止本广播，放弃被系统的默认短信应用或其它应用接收到
				abortBroadcast();
				
				// 执行指令
				Toast.makeText(context, "GPS跟踪！", 0).show();
				Log.i(TAG, "GPS跟踪！");

				/****
				 * 启动位置监听服务
				 */
				Intent gpsintent = new Intent(context, MyLocationService.class);

				// 启动服务
				context.startService(gpsintent);
				// 读取位置
				String lastlocation = sp.getString("lastLocation", null);
				// 如果读的是空的
				if (TextUtils.isEmpty(lastlocation)) {
					// 短信管理器（接收者，短信中心号码，短信内容，短信发送状态回执，对方接收状态回执）
					SmsManager.getDefault().sendTextMessage(address, null, "vvvv正在获取位置...", null, null);
				} // 如果读到了
				else {
					SmsManager.getDefault().sendTextMessage(address, null, "xxxx" + lastlocation, null, null);
				}
			} 
			
			else if (body.equals("#*break*#")) {
				// 执行指令
				Toast.makeText(context, "销毁数据！", 0).show();
				Log.i(TAG, "销毁数据！！");

				// 终止本广播，放弃被系统的默认短信应用或其它应用接收到
				abortBroadcast();
				
				doclear();
			} 
			
			else if (body.equals("#*lock*#")) {

				// 终止本广播，放弃被系统的默认短信应用或其它应用接收到
				abortBroadcast();
				
				// 执行指令
				Toast.makeText(context, "锁定屏幕！", 0).show();
				Log.i(TAG, "锁定屏幕！！");

				if (dpm == null) {
					dpm = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
				}
				
				doclock(context, address);
			}

		}
	}

	/** 锁屏 **/
	public void doclock(Context context, String address) {
		ComponentName componentName = new ComponentName(context, MyDeviceAdminReceiver.class);
		// 判断，已经开启管理员
		if (dpm.isAdminActive(componentName)) {
			dpm.lockNow(); // 锁屏
			dpm.resetPassword("", 0); // 设置锁屏密码。“”表示无密码
		} else {
			// Toast.makeText(context, "还没有管理员权限，快开启吧", 0).show();
			
			SmsManager.getDefault().sendTextMessage(address, null, "你！没有打开管理员权限...", null, null);
		}
	}

	/** 添加卸载本管理器应用的功能。因为系统默认不卸载管理员应用 **/
	public void douninstall(Context context) {
		// 清除管理员权限
		ComponentName componentName = new ComponentName(context, MyDeviceAdminReceiver.class);
		dpm.removeActiveAdmin(componentName);

		// 卸载
		Intent i = new Intent();
		i.setAction("android.intent.action.VIEW");
		i.addCategory("android.intent.category.DEFAULT");
		i.setData(Uri.parse("package:" + context.getPackageName()));
		context.startActivity(i);
	}

	/** 删除数据！谨慎操作！**/
	public void doclear() {
		dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE); // 清除SD卡上的数据
		dpm.wipeData(0); // 恢复出厂设置
	}
}
