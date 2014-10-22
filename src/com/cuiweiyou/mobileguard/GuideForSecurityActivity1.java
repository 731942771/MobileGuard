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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 手机防盗设置第1个向导界面
 * SIM卡绑定
 * @author Administrator
 */
public class GuideForSecurityActivity1 extends Activity {

	/** 保存小数据 **/
	private SharedPreferences sp;
	/** 对话框控制体 **/
	private AlertDialog tempDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_for_security_activity1);
		
		// ------------------ 按钮，进入下一个界面 -------------------
		Button btn_agsa1_next = (Button) findViewById(R.id.btn_agsa1_next);
		btn_agsa1_next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(GuideForSecurityActivity1.this, GuideForSecurityActivity2.class);
				startActivity(i);
				finish();
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
}