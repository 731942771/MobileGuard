package com.cuiweiyou.mobileguard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/** 手机防盗界面 **/
public class SecurityActivity extends Activity {

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_security);
		
		sp = getSharedPreferences("mobileguard_setting_config", MODE_PRIVATE);
		
		// 设置向导 开关。如果已经使用过向导，配置里存储的是true
		boolean setting_guide = sp.getBoolean("security_has_guide", false);
		if (setting_guide) {	// 向导仅使用一次，所以true的情况多
			// 留在此界面
			
			// 是否绑定
			if (sp.getBoolean("sim_power", false)){
				TextView tv_sim_power = (TextView) findViewById(R.id.tv_sim_power);
				tv_sim_power.setText("已开启");
			}
			// 安全号码
			String sim_guard_phone = sp.getString("sim_guard_phone", null);
			if (!TextUtils.isEmpty(sim_guard_phone)){
				TextView tv_sim_guard_phone = (TextView) findViewById(R.id.tv_sim_guard_phone);
				tv_sim_guard_phone.setText(sim_guard_phone);
			}
			// GPS跟踪
			if(sp.getBoolean("gps_power", false)){
				TextView tv_gps_power = (TextView) findViewById(R.id.tv_gps_power);
				tv_gps_power.setText("已开启");
			}
			// 远程数据销毁
			if(sp.getBoolean("breakdata_power", false)){
				TextView tv_breakdata_power = (TextView) findViewById(R.id.tv_breakdata_power);
				tv_breakdata_power.setText("已开启");
			}
			// 远程屏幕锁定
			if(sp.getBoolean("clockscreen_power", false)){
				TextView tv_clockscreen_power = (TextView) findViewById(R.id.tv_clockscreen_power);
				tv_clockscreen_power.setText("已开启");
			}
			
		} else {
			Editor edit = sp.edit();
			edit.putBoolean("security_has_guide", true);
			edit.commit();
			
			// 开始向导
			Intent i = new Intent(SecurityActivity.this, GuideForSecurityActivity1.class);
			startActivity(i);
		}
		
		TextView tvclick_goto_gfsa1 = (TextView) findViewById(R.id.tvclick_goto_gfsa1);
		tvclick_goto_gfsa1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(SecurityActivity.this, GuideForSecurityActivity1.class);
				startActivity(i);
				finish();
			}
		});
	}
}
