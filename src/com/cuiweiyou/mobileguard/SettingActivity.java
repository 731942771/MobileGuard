package com.cuiweiyou.mobileguard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.cuiweiyou.mobileguard.receiver.MyDeviceAdminReceiver;
import com.cuiweiyou.mobileguard.ui.SettingItemRelativeLayout;

/**
 * “设置中心”界面
 */
public class SettingActivity extends Activity {
	
	/** 自定义组合控件，“设置中心”的条目 **/
	private SettingItemRelativeLayout sirl_setting_item;
	/** 配置存储器 **/
	private SharedPreferences sp;
	/** 设备策略管理器 **/
	private DevicePolicyManager dpm;
	/** 管理员按钮组合控件 **/
	private SettingItemRelativeLayout sirl_setting_admin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		// (1)创建私有配置文件（“文件名”， 私有模式）
		sp = getSharedPreferences("mobileguard_setting_config", MODE_PRIVATE);
		
		/** 1.找到自定义类创建的组合控件 **/
		sirl_setting_item = (SettingItemRelativeLayout) findViewById(R.id.sirl_setting_item);
		
		// 查看上一次配置，更新控件
		boolean autoupdate = sp.getBoolean("autoupdate", false);
		if(autoupdate){
			sirl_setting_item.setChecked(true);
		} else {
			sirl_setting_item.setChecked(false);
		}
		
		
		/** 2.点击此控件，操作子控件 **/
		sirl_setting_item.setOnClickListener(new OnClickListener() {
			/** 3.实现点击处理 **/
			@Override
			public void onClick(View v) {
				// (2)得到编辑器
				Editor edit = sp.edit();
				/** 4.切换配置 **/
				if(sirl_setting_item.isChecked()){		// 如果原为true，点击后为false
					sirl_setting_item.setChecked(false);
					// (3)保存配置（“key”，值）
					edit.putBoolean("autoupdate", false);
				}else{									// 如果原为false，点击后为true
					sirl_setting_item.setChecked(true);
					// (3)保存配置
					edit.putBoolean("autoupdate", true);
				}
				// (4)提交数据！！！
				edit.commit();
			}
		});
		
		//------------------------------- 设备管理员功能控制 ---------------------------------
		
		/** 管理员权限组合控件 **/
		sirl_setting_admin = (SettingItemRelativeLayout) findViewById(R.id.sirl_setting_admin);
		sirl_setting_admin.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if(dpm == null){
					dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
				}
				
				// 激活的组件目标：硬件管理员广播接收者
				ComponentName componentName = new ComponentName(getApplicationContext(), MyDeviceAdminReceiver.class);
				// 如果权限已经打开
				if (dpm.isAdminActive(componentName)) {
					// 去除管理员权限。有管理员权限的应用不能卸载
					dpm.removeActiveAdmin(componentName);
					// 设置状态
					sirl_setting_admin.setChecked(false);
				} else {
					// 意图：开启管理员
					Intent i = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
					// 扩展管理员-自定义的接收者
					i.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
					// 提示信息，引导用户开启
					i.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启设备管理员，使用安全锁定功能");
					// 执行
					// 
					startActivityForResult(i, 0);	// 会传回来
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(resultCode == -1){
			sirl_setting_admin.setChecked(true);
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
