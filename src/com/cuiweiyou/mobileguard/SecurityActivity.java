package com.cuiweiyou.mobileguard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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
		} else {
			// 开始向导
			Intent i = new Intent(SecurityActivity.this, GuideForSecurityActivity1.class);
			startActivity(i);
		}
	}
}
