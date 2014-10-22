package com.cuiweiyou.mobileguard;

import com.cuiweiyou.mobileguard.ui.SettingItemRelativeLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 手机防盗设置第4个向导界面
 * 远程锁屏
 * @author Administrator
 */
public class GuideForSecurityActivity4 extends Activity {
	
	/** 保存小数据 **/
	private SharedPreferences sp;
	/** 对话框控制体 **/
	private AlertDialog tempDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_for_security_activity4);
		
		Button btn_agsa4_prev = (Button) findViewById(R.id.btn_agsa4_prev);
		btn_agsa4_prev.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(GuideForSecurityActivity4.this, GuideForSecurityActivity3.class);
				startActivity(i);
				finish();
			}
		});
		
		Button btn_agsa4_finish = (Button) findViewById(R.id.btn_agsa4_finish);
		btn_agsa4_finish.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(GuideForSecurityActivity4.this, HomeActivity.class);
				startActivity(i);
				finish();
			}
		});
		
		//---------------------------------------------------------------------------------------------------
		
		sp = getSharedPreferences("mobileguard_setting_config", MODE_PRIVATE);
		
		final SettingItemRelativeLayout chbox_agsa4_clock_power = (SettingItemRelativeLayout) findViewById(R.id.chbox_agsa4_clock_power);
		
		// 查看上一次配置，更新控件
		boolean clockscreen_power = sp.getBoolean("clockscreen_power", false);
		if(clockscreen_power){
			chbox_agsa4_clock_power.setChecked(true);
		} else {
			chbox_agsa4_clock_power.setChecked(false);
		}

		chbox_agsa4_clock_power.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Editor edit = sp.edit();
				if(chbox_agsa4_clock_power.isChecked()){
					chbox_agsa4_clock_power.setChecked(false);
					edit.putBoolean("clockscreen_power", false);
				}else{
					chbox_agsa4_clock_power.setChecked(true);
					edit.putBoolean("clockscreen_power", true);
				}
				edit.commit();
			}
		});
	}

}
