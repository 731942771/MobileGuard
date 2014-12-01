package com.cuiweiyou.mobileguard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.cuiweiyou.mobileguard.ui.SettingItemRelativeLayout;
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
	/** 手势识别器 **/
	private android.view.GestureDetector gd;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_for_security_activity2);
		
		// 创建GesTureDetector对象
		gd = new GestureDetector(GuideForSecurityActivity2.this, new SimpleOnGestureListener() {
			
			// ------------用户按下触摸屏、快速移动后松开-------------
			// e1 手势起点的移动事件。得到手指点下的位置
			// e2 当前手势点的移动事件
			// velocityX 每秒x轴方向移动的像素
			// velocityY 每秒y轴方向移动的像素
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				int FLING_MIN_DISTANCE/*最短移动距离*/ = 100, FLING_MIN_VELOCITY/*速度，每秒最小移动距离*/ = 200;
				
				// 手指从右向左滑动，打开右侧（下一个）的界面
				if(e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
					
					// 强制设置安全号码
					if (TextUtils.isEmpty(sp.getString("sim_guard_phone", null))){
						Toast.makeText(GuideForSecurityActivity2.this, "尚未设置安全号码，请返回上一步操作", 0).show();
						return false;
					}
					
					Intent intent = new Intent(GuideForSecurityActivity2.this, GuideForSecurityActivity3.class);
					startActivity(intent);
					finish();
					
					return true;
				} else
				// 手指从屏幕左侧点下，向右滑动
				if (e2.getX()-e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) >FLING_MIN_VELOCITY) {

					//切换Activity
					Intent intent = new Intent(GuideForSecurityActivity2.this, GuideForSecurityActivity1.class);
					startActivity(intent);
					finish();
					
					return true;
				}
				
				return false;
			}

		});
		
		// 按钮 下一页
		Button btn_agsa2_next = (Button) findViewById(R.id.btn_agsa2_next);
		btn_agsa2_next.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			public void onClick(View v) {
				
				// 强制设置安全号码
				if (TextUtils.isEmpty(sp.getString("sim_guard_phone", null))){
					Toast.makeText(GuideForSecurityActivity2.this, "尚未设置安全号码，请返回上一步操作", 0).show();
					return ;
				}
				
				Intent i = new Intent(GuideForSecurityActivity2.this, GuideForSecurityActivity3.class);
				startActivity(i);
				
				// 动画，进入下一个界面
				//overridePendingTransition(R.anim.anim_translate_activity_in, R.anim.anim_translate_activity_out);
				
				finish();
			}
		});
		
		// 上一页
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
	
	/** 这里是Activity的Touch事件 **/
	public boolean onTouchEvent(MotionEvent event) {
		gd.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
