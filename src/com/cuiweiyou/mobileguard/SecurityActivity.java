package com.cuiweiyou.mobileguard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SecurityActivity extends Activity {

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_security);
		
		sp = getSharedPreferences("mobileguard_setting_config", MODE_PRIVATE);
	}
}
