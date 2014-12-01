package com.cuiweiyou.mobileguard.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;
/**
 * 开机检测SIM卡变更
 * 版本 4.1.2版测试成功，4.4.测试失败 
 **/
public class SimBroadcastReceiver extends BroadcastReceiver {
	
	private SharedPreferences sp;
	private TelephonyManager tm;

	@SuppressLint("NewApi")
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("mobileguard_setting_config", Context.MODE_PRIVATE);
		// 安全号码
		String sim_serial_number_save = sp.getString("sim_serial_number", null);
		
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String simSerialNumber_get = tm.getSimSerialNumber() + "hah";
		
		if(sim_serial_number_save.equals(simSerialNumber_get)){
			Toast.makeText(context, "SIM卡验证成功", 0).show();
		}
		
		/** 4.1.2版测试成功，4.4.测试失败 **/
		else {
			
			Toast.makeText(context, "SIM卡验证错误\n向安全号码发送信息！！！", 0).show();

			// 短信管理器
			SmsManager smsManager = SmsManager.getDefault();
			// 发送短信
			smsManager.sendTextMessage(sim_serial_number_save, null, "手机的SIM卡发生改变，请确认手机所在！", null, null);
		}
	}
}
