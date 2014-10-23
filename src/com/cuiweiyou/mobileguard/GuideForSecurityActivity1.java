package com.cuiweiyou.mobileguard;

import com.cuiweiyou.mobileguard.ui.SettingItemRelativeLayout;
import com.cuiweiyou.mobileguard.utils.Md5Tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
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
/**
 * 手机防盗设置第1个向导界面
 * SIM卡绑定
 * @author Administrator
 * 实现 滑动切换(从网上找的办法)
 * 1.implements OnTouchListener,OnGestureListener 
 * 2.实例化手势识别器
 * 3.获得当前界面的布局ll_gfsa1，然后
 * ll_gfsa1.setOnTouchListener(this);
 * ll_gfsa1.setLongClickable(true);
 * 4.实现方法
 */
public class GuideForSecurityActivity1 extends Activity implements OnTouchListener,OnGestureListener {

	/** 保存小数据 **/
	private SharedPreferences sp;
	/** 对话框控制体 **/
	private AlertDialog tempDialog;
	/** 手势识别器 android.view.GestureDetector **/
	private GestureDetector gd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_for_security_activity1);
		
		// 创建GesTureDetector对象（创建时必须实现OnGestureListnener监听器实例）GestureDetector gd;
		gd = new GestureDetector((OnGestureListener)this);
		
		// 为当前Acitivity的布局页面添加setOnTouchListener事件
		LinearLayout ll_gfsa1 = (LinearLayout) findViewById(R.id.ll_gfsa1);
		ll_gfsa1.setOnTouchListener(this);
		ll_gfsa1.setLongClickable(true);
		
		// ------------------ 按钮，进入下一个界面 -------------------
		Button btn_agsa1_next = (Button) findViewById(R.id.btn_agsa1_next);
		btn_agsa1_next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(GuideForSecurityActivity1.this, GuideForSecurityActivity2.class);
				startActivity(i);
				finish();
				
				// 进入下一关界面时的切换动画（下一个界面进入的动画，本界面移出的动画）
				overridePendingTransition(R.anim.anim_translate_activity_in, R.anim.anim_translate_activity_out);
				
			}
		});
		
		sp = getSharedPreferences("mobileguard_setting_config", MODE_PRIVATE);
		
		/**------------------ 1. 复选框，控制SIM卡锁定功能的开闭 -----------------**/
		final SettingItemRelativeLayout chbox_agsa1_sim_power = (SettingItemRelativeLayout) findViewById(R.id.chbox_agsa1_sim_power);
		
		// 查看上一次配置，更新控件
		boolean sim_power = sp.getBoolean("sim_power", false);
		if(sim_power){
			chbox_agsa1_sim_power.setChecked(true);
		} else {
			chbox_agsa1_sim_power.setChecked(false);
		}

		/** 2.点击此控件，操作子控件 **/
		chbox_agsa1_sim_power.setOnClickListener(new OnClickListener() {
			/** 3.实现点击处理 **/
			public void onClick(View v) {
				// (2)得到编辑器
				Editor edit = sp.edit();
				/** 4.关闭SIM卡锁定 **/
				if(chbox_agsa1_sim_power.isChecked()){		// 如果原为true，点击后为false
					chbox_agsa1_sim_power.setChecked(false);
					// 保存状态为关闭
					edit.putBoolean("sim_power", false);
					// 关闭SIM卡绑定，同时消除安全号码
					// edit.putString("sim_guard_phone", null);
				}else{									// 如果原为false，点击后为true
					
					/**--------------- 弹出设置安全号码的对话框 -----------------------**/
					// 1.创建弹出式对话框
					AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(GuideForSecurityActivity1.this);	// 系统默认Dialog没有输入框
					
					// 2.获取自定义的布局（上下文-因为这个对话框仅在此界面使用所有为this，源，被替换者）。/res/layout/自定义布局文件
					View alertDialogView = View.inflate(GuideForSecurityActivity1.this, R.layout.style_dialog_sim_phone_guard, null);
					
					// 1）获得自定义Dialog布局中的子控件
					// 电话号码输入
					final EditText et_dialog_sim_guard_phone = (EditText) alertDialogView.findViewById(R.id.et_dialog_sim_guard_phone);
					
					// 读取配置文件，如果已经有存储密码，显示在控件上
					String tmp_sim_guard_phone = sp.getString("sim_guard_phone", null);
					if(!TextUtils.isEmpty(tmp_sim_guard_phone)){
						et_dialog_sim_guard_phone.setHint("已设置安全号：" + tmp_sim_guard_phone + "继续输入重置");
					}
					
					// 3）从通讯录选择联系人 按钮
					Button btn_dialog_sim_addresslist = (Button) alertDialogView.findViewById(R.id.btn_dialog_sim_addresslist);
					btn_dialog_sim_addresslist.setOnClickListener(new OnClickListener() {
						// 如果此按钮被点击了
						public void onClick(View v) {
			/*				String str_sim_guard_phone = et_dialog_sim_guard_phone.getText().toString();

							Editor edit = sp.edit();
							
							 保存密码密文到配置文件（Key，Value） 
							edit.putString("sim_guard_phone", str_sim_guard_phone);
							
							edit.commit();
							
							 通过替身控制对话框 
							tempDialog.dismiss();*/
							
							Toast.makeText(GuideForSecurityActivity1.this, "选择了通讯录，写入到EditText", 0).show();
						}
					});
					
					// 4）确定 按钮
					Button btn_dialog_sim_guard_phone = (Button) alertDialogView.findViewById(R.id.btn_dialog_sim_guard_phone);
					btn_dialog_sim_guard_phone.setOnClickListener(new OnClickListener() {
						// 如果此按钮被点击了
						public void onClick(View v) {
							String str_sim_guard_phone = et_dialog_sim_guard_phone.getText().toString();
							Editor edit = sp.edit();
							if(!TextUtils.isEmpty(str_sim_guard_phone)){
								// 保存到配置文件（Key，Value） 
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
							
							// 通过替身控制对话框 
							tempDialog.dismiss();
							
							// (3)保存配置状态为开启
							edit.putBoolean("sim_power", true);
							
							// 设置控件为开启
							chbox_agsa1_sim_power.setChecked(true);
						}
					});
					
					// 4）取消按钮，不创建安全号码
					Button btn_dialog_sim_guard_cnacle = (Button) alertDialogView.findViewById(R.id.btn_dialog_sim_guard_cnacle);
					btn_dialog_sim_guard_cnacle.setOnClickListener(new OnClickListener() {
						// 关闭对话框 
						public void onClick(View v) {
							/* 5.通过替身控制对话框 */
							tempDialog.dismiss();
						}
					});
					
					/* 4.获取此对话框， */
					tempDialog = alertDialog.create();
					// 替换对话框的默认布局（上下左右边距，为了照顾低版本）
					tempDialog.setView(alertDialogView, 0, 0, 0, 0);
					// 显示
					tempDialog.show();
					
					/**--------------- 弹出设置安全号码的对话框 -----------------------**/
				}
				// (4)提交数据！！！
				edit.commit();
			}
		});
	}
	
	
	// ------------用户按下触摸屏、快速移动后松开-------------
	// e1 手势起点的移动事件。得到手指点下的位置
	// e2 当前手势点的移动事件
	// velocityX 每秒x轴方向移动的像素
	// velocityY 每秒y轴方向移动的像素
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		
		int FLING_MIN_DISTANCE/*最短移动距离*/ = 100, FLING_MIN_VELOCITY/*速度，每秒最小移动距离*/ = 200;
		
		// 手指从右向左滑动，打开右侧的界面
		if(e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			Intent intent = new Intent(GuideForSecurityActivity1.this, GuideForSecurityActivity2.class);
			startActivity(intent);
		}/* else
			// 手指从屏幕左侧点下，向右滑动
		if (e2.getX()-e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) >FLING_MIN_VELOCITY) {

			//切换Activity
			Intent intent = new Intent(GuideForSecurityActivity1.this, SecurityActivity.class);
			startActivity(intent);
		}*/

		return false;
	}

	// 注意：若不加setLongClickable(true)的话OnFling会失效，如果不写这句的话OnGestureListener的重写方法OnDown方法返回true也可以。
	// 只有这样，view才能够处理不同于Tap（轻触）的hold（即ACTION_MOVE，或者多个ACTION_DOWN），
	// 我们同样可以通过layout定义中的android:longClickable来做到这一点。   
	// 将Acityvity的TouchEvent事件交给GestureDetector处理
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return gd.onTouchEvent(event);
	}

	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	// 用户（轻触触摸屏后）松开
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	// 用户按下触摸屏，并拖动
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	// 用户长按触摸屏
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
}