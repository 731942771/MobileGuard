package com.cuiweiyou.mobileguard;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.animation.AlphaAnimation;

/**
 * ~~~第3次提交，新的界面~~~
 * 应用程序的主界面
 */
public class HomeActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		/** 
 		启动欢迎界面时使用动画
 		**/
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(5000);
		findViewById(R.id.layout_home).startAnimation(aa);
	}
}
