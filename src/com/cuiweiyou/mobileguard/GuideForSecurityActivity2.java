package com.cuiweiyou.mobileguard;

import com.cuiweiyou.mobileguard.ui.SettingItemRelativeLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 手机防盗设置第2个向导界面
 * GPS定位
 * @author Administrator
 */
public class GuideForSecurityActivity2 extends Activity {

	/** 保存小数据 **/
	private SharedPreferences sp;
	/** 对话框控制体 **/
	private AlertDialog tempDialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_for_security_activity2);
		
		Button btn_agsa2_next = (Button) findViewById(R.id.btn_agsa2_next);
		btn_agsa2_next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(GuideForSecurityActivity2.this, GuideForSecurityActivity3.class);
				startActivity(i);
				finish();
			}
		});
		
		Button btn_agsa2_prev = (Button) findViewById(R.id.btn_agsa2_prev);
		btn_agsa2_prev.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(GuideForSecurityActivity2.this, GuideForSecurityActivity1.class);
				startActivity(i);
				finish();
			}
		});
		
		//----------------------------------------------------------------------------------------------------
		
		sp = getSharedPreferences("mobileguard_setting_config", MODE_PRIVATE);
		
		/**------------------ 1. 复选框，控制GPS跟踪功能的开闭 -----------------**/
		final SettingItemRelativeLayout chbox_agsa2_gps_power = (SettingItemRelativeLayout) findViewById(R.id.chbox_agsa2_gps_power);
		
		// 查看上一次配置，更新控件
		boolean gps_power = sp.getBoolean("gps_power", false);
		if(gps_power){
			chbox_agsa2_gps_power.setChecked(true);
		} else {
			chbox_agsa2_gps_power.setChecked(false);
		}

		/** 2.点击此控件，操作子控件 **/
		chbox_agsa2_gps_power.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Editor edit = sp.edit();
				/** 4.关闭GPS跟踪 **/
				if(chbox_agsa2_gps_power.isChecked()){
					chbox_agsa2_gps_power.setChecked(false);
					edit.putBoolean("gps_power", false);
				}else{
					chbox_agsa2_gps_power.setChecked(true);
					edit.putBoolean("gps_power", true);
				}
				// (4)提交数据！！！
				edit.commit();
			}
		});
	}

}
