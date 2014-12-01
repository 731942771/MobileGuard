package com.cuiweiyou.mobileguard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cuiweiyou.mobileguard.interfaces.ICallBack;
import com.cuiweiyou.mobileguard.ui.SettingItemRelativeLayout;
/**
 * 手机防盗设置第1个向导界面
 * SIM卡绑定
 */
public class GuideForSecurityActivity1 extends Activity implements OnTouchListener,OnGestureListener {

	/** 保存小数据 **/
	private SharedPreferences sp;

	/** 手势识别器 android.view.GestureDetector **/
	private GestureDetector gd;
	/** 手机管理器 **/
	private TelephonyManager tm;
	/** 弹出式对话框 **/
	private AlertDialog.Builder alertDialog;
	/** 对话框控制体 **/
	private AlertDialog tempDialog;
	/** 弹出式对话框的电话号码框 **/
	private EditText et_dialog_sim_guard_phone;
	/** 对话框上的开关控件 **/
	private SettingItemRelativeLayout chbox_agsa1_sim_power;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_for_security_activity1);
		
		gd = new GestureDetector((OnGestureListener)this);
		
		LinearLayout ll_gfsa1 = (LinearLayout) findViewById(R.id.ll_gfsa1);
		ll_gfsa1.setOnTouchListener(this);
		ll_gfsa1.setLongClickable(true);
		
		// 下一页 按钮
		Button btn_agsa1_next = (Button) findViewById(R.id.btn_agsa1_next);
		btn_agsa1_next.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			public void onClick(View v) {
				Intent i = new Intent(GuideForSecurityActivity1.this, GuideForSecurityActivity2.class);
				startActivity(i);
				
				// 切换界面动画
				//overridePendingTransition(R.anim.anim_translate_activity_in, R.anim.anim_translate_activity_out);
				
				finish();
			}
		});
		
		sp = getSharedPreferences("mobileguard_setting_config", MODE_PRIVATE);
		
		chbox_agsa1_sim_power = (SettingItemRelativeLayout) findViewById(R.id.chbox_agsa1_sim_power);
		
		boolean sim_power = sp.getBoolean("sim_power", false);
		if(sim_power){
			chbox_agsa1_sim_power.setChecked(true);
		} else {
			chbox_agsa1_sim_power.setChecked(false);
		}

		chbox_agsa1_sim_power.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
			
				final Editor edit = sp.edit();
			
				if(chbox_agsa1_sim_power.isChecked()){
					chbox_agsa1_sim_power.setChecked(false);
					edit.putBoolean("sim_power", false);
					
					edit.commit();
					
				}
				// 如果开启绑定
				else{
					
					/** 创建对话框，配置号码 **/
					createAlertDialog();
					
				}
			}

		});
	}

	/** 创建弹出式对话框 **/
	@SuppressLint("NewApi")
	private void createAlertDialog() {
		/*** 方式2：初始化回调函数 **/
		AddressListActivity.setCallBack(new ICallBack() {
			/*** 通过回调函数更新数据 **/
			@Override
			public void callback(String phonenumber) {
				et_dialog_sim_guard_phone.setText(phonenumber);
				System.out.println("a;lsgjl;asdjglkdddddddddddddddddddddddddddddddddddd");
			}
		});
		
		alertDialog = new android.app.AlertDialog.Builder(GuideForSecurityActivity1.this);
		View alertDialogView = View.inflate(GuideForSecurityActivity1.this, R.layout.style_dialog_sim_phone_guard, null);
		
		et_dialog_sim_guard_phone = (EditText) alertDialogView.findViewById(R.id.et_dialog_sim_guard_phone);
		String tmp_sim_guard_phone = sp.getString("sim_guard_phone", null);
		if(!TextUtils.isEmpty(tmp_sim_guard_phone)){
			et_dialog_sim_guard_phone.setHint("原安全号：" + tmp_sim_guard_phone + "，继续输入重置");
		}
		
		// 对话框上的“从通讯录”选择联系人---------------------------------------------- /|\
		Button btn_dialog_sim_addresslist = (Button) alertDialogView.findViewById(R.id.btn_dialog_sim_addresslist);
		btn_dialog_sim_addresslist.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 进入选择联系人界面
				Intent i = new Intent(GuideForSecurityActivity1.this, AddressListActivity.class);
				
				/*////////////////////////////不是 startActivity(i)*/
				/*** 方式1：startActivityForResult回传数据 **/
				/* 须要接收传回的数据 */
				//startActivityForResult(i, 0);
				/*** 使用了回调函数，所以不用Result了 **/
				startActivity(i);
				//finish();
				
				// 不关闭对话框是会报错滴
				//E/WindowManager(1768): android.view.WindowLeaked: Activity com.cuiweiyou.mobileguard.GuideForSecurityActivity1 has leaked window com.android.internal.policy.impl.PhoneWindow$DecorView that was originally added here
				//tempDialog.dismiss();
			}
		});
		
		final Editor edit = sp.edit();
		
		// 确定按钮，保存安全号码
		Button btn_dialog_sim_guard_phone = (Button) alertDialogView.findViewById(R.id.btn_dialog_sim_guard_phone);
		btn_dialog_sim_guard_phone.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				/** 输入的电话号码 */
				String str_sim_guard_phone = et_dialog_sim_guard_phone.getText().toString();
				if(!TextUtils.isEmpty(str_sim_guard_phone)){

					// 得到手机管理器
					tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					// 得到SIM卡串号
					String simSerialNumber = tm.getSimSerialNumber();
					// 保存SIM卡串号
					edit.putString("sim_serial_number", simSerialNumber);
					
					edit.putString("sim_guard_phone", str_sim_guard_phone);
					
					edit.putBoolean("sim_power", true);
					
					edit.commit();

					Toast.makeText(GuideForSecurityActivity1.this, "保存安全号码成功", 0).show();
					
				}else{
					String tmp_sim_guard_phone = sp.getString("sim_guard_phone", null);
					if(!TextUtils.isEmpty(tmp_sim_guard_phone)){
						Toast.makeText(GuideForSecurityActivity1.this, "继续使用上次设置的号码", 0).show();
						edit.putBoolean("sim_power", true);
						
						edit.commit();
						
					}else{
						Toast.makeText(GuideForSecurityActivity1.this, "请输入号码", 0).show();
						return;
					}
				}
				tempDialog.dismiss();
				edit.putBoolean("sim_power", true);
				
				edit.commit();
				
				chbox_agsa1_sim_power.setChecked(true);
			}
		});
		
		// 取消按钮，返回
		Button btn_dialog_sim_guard_cnacle = (Button) alertDialogView.findViewById(R.id.btn_dialog_sim_guard_cnacle);
		btn_dialog_sim_guard_cnacle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tempDialog.dismiss();

				// 保存SIM卡串号为NULL，不锁定
				edit.putString("sim_serial_number", null);
				
				edit.commit();
				
			}
		});
		
		tempDialog = alertDialog.create();
		tempDialog.setView(alertDialogView, 0, 0, 0, 0);

		tempDialog.show();
	}
	
	// ------------用户按下触摸屏、快速移动后松开-------------
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		
		int FLING_MIN_DISTANCE/*最短移动距离*/ = 100, FLING_MIN_VELOCITY/*速度，每秒最小移动距离*/ = 200;
		
		// 手指从右向左滑动，打开右侧的界面
		if(e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			Intent intent = new Intent(GuideForSecurityActivity1.this, GuideForSecurityActivity2.class);
			startActivity(intent);
			finish();
		}

		return false;
	}

	public boolean onTouch(View v, MotionEvent event) {
		return gd.onTouchEvent(event);
	}

	public boolean onDown(MotionEvent e) {
		return false;
	}
	public void onShowPress(MotionEvent e) {
	}
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}
	public void onLongPress(MotionEvent e) {
	}

	// 对话框上的“从通讯录”选择联系人---------------------------------------------- \|/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
		if (data != null){
			
			createAlertDialog();
			
			String name = data.getStringExtra("name");
			String phone = data.getStringExtra("phone").replace("-", "");
			et_dialog_sim_guard_phone.setText(phone);
			
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		} else {
			System.out.println("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
		}
	}
	
}