package com.cuiweiyou.mobileguard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.AvoidXfermode;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cuiweiyou.mobileguard.utils.Md5Tool;

/**  应用程序的主界面 */
public class HomeActivity extends Activity {
	
	/** 9宫格名称 **/
	private String[] item_names = {
			"缓存清理", "手机杀毒", "高级工具", 
			"流量统计", "通讯卫士", "进程管理", 
			"软件管理", "手机防盗", "设置中心"
			};
	
	/** 9宫格名称 **/
	private int[] item_imgs = {
			R.drawable.ic_app_01, R.drawable.ic_app_02, R.drawable.ic_app_03,  
			R.drawable.ic_app_04, R.drawable.ic_app_05, R.drawable.ic_app_06,  
			R.drawable.ic_app_07, R.drawable.ic_app_08, R.drawable.ic_app_09  
	};

	/** 主界面的9宫格 **/
	private GridView gv_home;

	/** 配置读写器 **/
	private SharedPreferences sp;
	
	/** 对话框替身 **/
	private AlertDialog tempDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		// 目录：/data/data/应用包/shared_prefs/mobileguard_setting_config.xml
		sp = getSharedPreferences("mobileguard_setting_config", MODE_PRIVATE);

		/**  启动欢迎界面时使用动画 **/
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(2000);
		findViewById(R.id.layout_home).startAnimation(aa);

		/** ~~~ 主界面9宫格实现~~~ */
		gv_home = (GridView) findViewById(R.id.gv_home);
		
		/** 网格控件的适配器 **/
		GVHomeAdapter adapter = new GVHomeAdapter();
		
		/** 网格控件通过适配器创建格子 **/
		gv_home.setAdapter(adapter);
		
		/** 网格控件的格子被点击事件监听（格子点击监听器） **/
		gv_home.setOnItemClickListener(new OnItemClickListener() {
			/** 被点击的处理
			 * parent: 格子所属的网格控件
			 * view: 被点击的格子
			 * position: 格子索引，始于0
			 * id: 格子的id **/
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//TextView item = (TextView) view.findViewById(R.id.tv_grid_item);	// 获得格子中的文本控件
				//Toast.makeText(HomeActivity.this, "name:" + item.getText() + ",position:" + position, 0).show();
				
				// 匹配被点击的格子的位置
				switch(position){
					case 0:
						
						break;
					
					case 1:
						break;
						
					case 2:
						break;
						
					case 3:
						break;
						
					case 4:
						break;
						
					case 5:
						break;
						
					case 6:
						break;
						
					case 7:
						/**
						 * ~~~  第6次提交  ~~~
						 *  进入 手机防盗界面
						 */
						watchPhoneGurdPswdDialog();
						break;
						
					case 8:	/** ~~~ 进入 设置中心界面  ~~~ */
						Intent i_grid = new Intent(HomeActivity.this, SettingActivity.class);
						startActivity(i_grid);
						break;
				}
			}
		});
	}
	
	/**
	 * ~~~  第6次提交  ~~~<br/>
	 * 检查密码是否设置
	 */
	protected void watchPhoneGurdPswdDialog() {
		// 读取配置文件中的密码配置，没有时，返回null-false
		String guardPswd = sp.getString("phoneguardpswd", null);
		// TODO 验证密码是不应再读配置文件
		
		// 如果有密码，输入并确认
		if(!TextUtils.isEmpty(guardPswd)){	// null时，TextUtils.isEmpty(guardPswd)判断是空的，为true
			confirmPhoneGurdPswd(guardPswd);
		}
		// 如果没有，则新建密码并保存
		else {
			createPhoneGurdPswd();
		}
	}

	/**
	 *  输入并确认“手机防盗”密码
	 */
	private void confirmPhoneGurdPswd(final String guardPswd) {
		
		// 1.创建弹出式对话框
		final AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(HomeActivity.this);	// 系统默认Dialog没有输入框
		
		// 2.获取自定义的布局（上下文-因为这个对话框仅在此界面使用所以为this，源，被替换者）。/res/layout/自定义布局文件
		View alertDialogView = View.inflate(HomeActivity.this, R.layout.activity_dialog_confirmphoneguardpswd, null);
		
		// 1）密码框
		final EditText et_dialog_confirmphoneguardpswd = (EditText) alertDialogView.findViewById(R.id.et_dialog_confirmphoneguardpswd);
		
		// 2）确认按钮，确认验证密码
		Button btn_dialog_resolve_confirmphoneguardpswd = (Button) alertDialogView.findViewById(R.id.btn_dialog_resolve_confirmphoneguardpswd);
		btn_dialog_resolve_confirmphoneguardpswd.setOnClickListener(new OnClickListener() {
			// 点击按钮处理
			public void onClick(View v) {
				// 提取文本框中输入的文本密码
				String str_confirmphoneguardpswd = et_dialog_confirmphoneguardpswd.getText().toString();
				
				// 比对
				if (!TextUtils.isEmpty(str_confirmphoneguardpswd) && Md5Tool.domd5(str_confirmphoneguardpswd).equals(guardPswd)) {
					tempDialog.dismiss();
					// Toast.makeText(HomeActivity.this, "密码验证成功", 0).show();
					
					/** 如果密码验证成，进入“手机防盗”界面 **/
					Intent i = new Intent(HomeActivity.this, GuideForSecurityActivity1.class);
					startActivity(i);
					
				} else {
					et_dialog_confirmphoneguardpswd.setText("");
					Toast.makeText(HomeActivity.this, "密码验证错误", 0).show();
				}
			}
		});
		// 3）取消按钮，不验证密码
		Button btn_dialog_cancel_confirmphoneguardpswd = (Button) alertDialogView.findViewById(R.id.btn_dialog_cancel_confirmphoneguardpswd);
		btn_dialog_cancel_confirmphoneguardpswd.setOnClickListener(new OnClickListener() {
			// 点击按钮处理
			public void onClick(View v) {
				tempDialog.dismiss();
			}
		});
		
		// 3.替换对话框的默认布局
		//alertDialog.setView(alertDialogView);
		//tempDialog = alertDialog.show();
		
		tempDialog = alertDialog.create();
		tempDialog.setView(alertDialogView, 0, 0, 0, 0);
		
		/** 自动弹出软键盘 **/
		tempDialog.setOnShowListener(new OnShowListener() {
		    public void onShow(DialogInterface dialog) {
		        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		        imm.showSoftInput(et_dialog_confirmphoneguardpswd, InputMethodManager.SHOW_IMPLICIT);
		    }
		});
		
		tempDialog.show();
	}
	
	/**
	 *  新建密码并保存“手机防盗”密码
	 */
	private void createPhoneGurdPswd() {

		// 1.创建弹出式对话框
		AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(HomeActivity.this);	// 系统默认Dialog没有输入框
		
		// 2.获取自定义的布局（上下文-因为这个对话框仅在此界面使用所有为this，源，被替换者）。/res/layout/自定义布局文件
		View alertDialogView = View.inflate(HomeActivity.this, R.layout.activity_dialog_createphoneguardpswd, null);
		
		// 1）获得自定义Dialog布局中的子控件
		//    密码框，第1次输入
		final EditText et_dialog_createphoneguardpswd = (EditText) alertDialogView.findViewById(R.id.et_dialog_createphoneguardpswd);
		// 2）密码框， 第2次输入
		final EditText et_dialog_confirmphoneguardpswd = (EditText) alertDialogView.findViewById(R.id.et_dialog_confirmphoneguardpswd);
		
		
/*		// 读取配置文件，如果已经有存储密码，显示在控件上
		String phoneGuardPswd = sp.getString("phoneguardpswd", null);
		if(!TextUtils.isEmpty(phoneGuardPswd)){
			et_dialog_createphoneguardpswd.setText(phoneGuardPswd);
			et_dialog_confirmphoneguardpswd.setText(phoneGuardPswd);
		}*/
		
		
		// 3）确认按钮，创建密码
		Button btn_dialog_resolve_createphoneguardpswd = (Button) alertDialogView.findViewById(R.id.btn_dialog_resolve_createphoneguardpswd);
		btn_dialog_resolve_createphoneguardpswd.setOnClickListener(new OnClickListener() {
			// 如果此按钮被点击了
			public void onClick(View v) {
				String str_createphoneguardpswd = et_dialog_createphoneguardpswd.getText().toString();
				String str_confirmphoneguardpswd = et_dialog_confirmphoneguardpswd.getText().toString();

				// 检查两个密码框是否为空，且两次输入密码是否一致，如果一致，保存
				if((!TextUtils.isEmpty(str_createphoneguardpswd)) && (str_createphoneguardpswd.equals(str_confirmphoneguardpswd))){
					Editor edit = sp.edit();
					
					// 加密
					String domd5 = Md5Tool.domd5(str_confirmphoneguardpswd);
					
					/* 保存密码密文到配置文件（Key，Value） */
					edit.putString("phoneguardpswd", domd5);
					
					edit.commit();
					
					/* 通过替身控制对话框 */
					tempDialog.dismiss();
					Toast.makeText(HomeActivity.this, "保存密码成功", 0).show();
				} else {
					et_dialog_createphoneguardpswd.setText("");
					et_dialog_confirmphoneguardpswd.setText("");
					Toast.makeText(HomeActivity.this, "两次输入不一致", 0).show();
				}
			}
		});
		
		// 4）取消按钮，不创建密码
		Button btn_dialog_cancel_createphoneguardpswd = (Button) alertDialogView.findViewById(R.id.btn_dialog_cancel_createphoneguardpswd);
		btn_dialog_cancel_createphoneguardpswd.setOnClickListener(new OnClickListener() {
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
	}

	/** 主界面9宫格适配器 **/
	private class GVHomeAdapter extends BaseAdapter{

		/** 格子数量 **/
		@Override
		public int getCount() {
			// 根据已有的更能数量创建格子
			return item_names.length;
		}

		/** 格子<br/>
		 * position, 		：每个格子对应的位置
		 * convertView, 	：每个格子对应的缓存
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
/*------------ 优化----------------------------------
 * 			View grid_item;
			// 判断缓存是否存在。没有，就创建
			if(convertView == null){
				// 1.把xml表述的layout转化为View对象
				grid_item = View.inflate(HomeActivity.this, R.layout.grid_item_home, null);
			} else {	// 又缓存，使用
				grid_item = convertView;
			}
			
			// 2.获取格子的图片控件
			ImageView iv_grid_item = (ImageView) grid_item.findViewById(R.id.iv_grid_item);
			iv_grid_item.setImageResource(item_imgs[position]);
			
			// 3.获取格子的文本控件
			TextView tv_grid_item = (TextView) grid_item.findViewById(R.id.tv_grid_item);
			// 根据当前格子的位置，对应的取功能名
			tv_grid_item.setText(item_names[position]);
			
			return grid_item;*/
			
			View grid_item;
			ViewHolder holder=null;
			// 判断缓存是否存在。没有，就创建
			if(convertView == null){
				holder=new ViewHolder();
				// 1.把xml表述的layout转化为View对象
				convertView = View.inflate(HomeActivity.this, R.layout.grid_item_home, null);
				// 保存
				holder.iv_grid_item=(ImageView) convertView.findViewById(R.id.iv_grid_item);
				holder.tv_grid_item = (TextView) convertView.findViewById(R.id.tv_grid_item);
				// 把查找的view缓存起来方便多次重，不用重新构建
				convertView.setTag(holder);
			} else {	// 有缓存，使用
				holder=(ViewHolder)convertView.getTag();
			}
			
			// 2.获取格子的图片控件
			// iv_grid_item = (ImageView) grid_item.findViewById(R.id.iv_grid_item);
			holder.iv_grid_item.setImageResource(item_imgs[position]);
			
			// 根据当前格子的位置，对应的取功能名
			holder.tv_grid_item.setText(item_names[position]);
			
			return convertView;
		}
		
		/**
		 * ListView优化 
		 */
		class ViewHolder{
			ImageView iv_grid_item;
			TextView tv_grid_item;
		}

		/** 格子 **/
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		/** 格子 **/
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}

}
