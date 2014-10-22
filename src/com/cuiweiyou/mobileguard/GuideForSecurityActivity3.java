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
 * 手机防盗设置第3个向导界面
 * 远程数据销毁
 * @author Administrator
 */
public class GuideForSecurityActivity3 extends Activity {
	/** 保存小数据 **/
	private SharedPreferences sp;
	/** 对话框控制体 **/
	private AlertDialog tempDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_for_security_activity3);
		
		Button btn_agsa3_next = (Button) findViewById(R.id.btn_agsa3_next);
		btn_agsa3_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(GuideForSecurityActivity3.this, GuideForSecurityActivity4.class);
				startActivity(i);
				finish();
			}
		});
		
		Button btn_agsa3_prev = (Button) findViewById(R.id.btn_agsa3_prev);
		btn_agsa3_prev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(GuideForSecurityActivity3.this, GuideForSecurityActivity2.class);
				startActivity(i);
				finish();
			}
		});
		
		//---------------------------------------------------------------------------------------------------
		
		sp = getSharedPreferences("mobileguard_setting_config", MODE_PRIVATE);
		
		final SettingItemRelativeLayout chbox_agsa3_breakdata_power = (SettingItemRelativeLayout) findViewById(R.id.chbox_agsa3_breakdata_power);
		
		boolean breakdata_power = sp.getBoolean("breakdata_power", false);
		if(breakdata_power){
			chbox_agsa3_breakdata_power.setChecked(true);
		} else {
			chbox_agsa3_breakdata_power.setChecked(false);
		}

		chbox_agsa3_breakdata_power.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Editor edit = sp.edit();
				if(chbox_agsa3_breakdata_power.isChecked()){
					chbox_agsa3_breakdata_power.setChecked(false);
					edit.putBoolean("breakdata_power", false);
				}else{
					chbox_agsa3_breakdata_power.setChecked(true);
					edit.putBoolean("breakdata_power", true);
				}
				edit.commit();
			}
		});
		
	}

}
