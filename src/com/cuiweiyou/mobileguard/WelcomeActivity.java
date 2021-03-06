package com.cuiweiyou.mobileguard;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
/**
 * 欢迎界面
 * @author Administrator
 */
public class WelcomeActivity extends Activity {
	/** 新版本apk文件路径 **/
	private String apkurl;

	/** 更新信息显示控件 **/
	private TextView tv_download_info;

	/** 消息代码,进入主界面 **/
	protected static final int ENTER_HOME = 0;
	/** 消息代码,弹出更新提示Dialog **/
	protected static final int UPDATE_DIALOG = 1;
	/** 消息代码,网络路径无效错误 **/
	protected static final int URL_EXCEPTION_DIALOG = 2;
	/** 消息代码,数据传输协议不匹配而导致无法与远程方进行通信 **/
	protected static final int PROTOCOL_EXCEPTION_DIALOG = 3;
	/** 消息代码,网络读写错误 **/
	protected static final int IO_EXCEPTION_DIALOG = 4;
	/** 消息代码,JSON数据错误 **/
	protected static final int JSON_EXCEPTION_DIALOG = 5;
	
	/**
	 * 异步的消息处理器，根据消息作出反应：
	 * 直接进入主界面 / 提示更新 / 提示错误信息 
	 **/
	Handler handler = new Handler(){
		// 处理消息
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch(msg.what){
				case ENTER_HOME:	// 进入应用主界面
					enterHomeActivity();
				break;
				case UPDATE_DIALOG:	// 更新提示
					
					/** 1.对话框的创建器 */
					AlertDialog.Builder dialog = new Builder(WelcomeActivity.this);
					
					/** 2.标题 */
					dialog.setTitle("升级提示");
					
					/** 3.信息内容 */
					dialog.setMessage("现在有新版本，立刻下载更新");
					
					/** 4.确定按钮（文本，事件） */
					dialog.setPositiveButton("确定", new OnClickListener() {
						/** dialog-本提示框，which-点击的按钮 */
						public void onClick(DialogInterface dialog, int which) {
							/** 获取外部存储-TF卡的状态，如果可用 */
							if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
								// 创建工具
								HttpUtils http = new HttpUtils();
								// 最大开启线程数量，一般3、4个
								http.configRequestThreadPoolSize(1);
								// 执行下载
								http.download(
									// 源路径
									apkurl,
									
									// 目标路径
									//Environment.getExternalStorageDirectory().getAbsolutePath() + "MobilGuard.apk",	// 错误：IOException: open failed: EROFS (Read-only file system)
									Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobilGuard.apk",	// 注意加斜杠
									// "/mnt/sdcard/MobilGuard.apk",													// 4.4.2对应 /storage/sdcard/MobilGuard.apk，4.1.2对应/mnt/sdcard/Mo---
									
									// 回调，每秒一次
									new RequestCallBack<File>(){

										// 下载失败
										public void onFailure(HttpException exception, String msg) {
											exception.printStackTrace();
											Toast.makeText(getApplicationContext(), "下载失败：" + msg, 0).show();
										}
										
										// 下载进行中---
										public void onLoading(long total, long current, boolean isUploading) {
											System.err.println("总量：" + total);
											System.err.println("排量：" + current);
											
											// 下载比例
											long progress = current * 100 / total;
											if ( ! isUploading){
												tv_download_info.setText("当前下载：" + progress + "%");
											}
										}

										// 下载成功
										public void onSuccess(ResponseInfo<File> info) {
											tv_download_info.setText("下载结束了");
											// 要使用info.result.getPath()，须 extends Activity
											System.err.println("文件：" + info.result.getPath() + ".");
											
											// 1.创建意图
											Intent intent = new Intent(Intent.ACTION_VIEW);	// 同上
											// 2.意图实施对象（文件，文件类型）
											intent.setDataAndType(
													Uri.parse("file://"+ Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobilGuard.apk"),
													"application/vnd.android.package-archive");
											// 3.实施。弹出安装应用程序界面
											startActivity(intent);
											
											/**
											 * 安卓应用的数字签名
											 * http://www.cuiweiyou.com/583.html
											 */
										}
									});
							} else {
								Toast.makeText(getApplicationContext(), "内存卡读取失败，请重新安装后尝试", 0).show();
								return;
							}
						}
					});
					
					/** 5.取消按钮（文本，事件） **/
					dialog.setNegativeButton("取消", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							/** ~~~第5次提交~~~ **/
							enterHomeActivity();
						}
					});
					
					/** 6.禁用系统的返回功能 **/
					//dialog.setCancelable(false);	//~~~第5次提交~~~ 禁用返回键有效，强制升级未必会有好体验
					/** 
					 * ~~~第5次提交~~~ 
					 * 禁用返回键有效，强制升级未必会有好体验 
					 */
					dialog.setOnCancelListener(new OnCancelListener(){
						@Override
						public void onCancel(DialogInterface dialog) {
							// 销毁对话框
							dialog.dismiss();
							// 进入下一界面
							enterHomeActivity();
						}
					});
					
					/** 7.显示对话框 **/
					dialog.show();	// 其中调用create方法
				break;
				case URL_EXCEPTION_DIALOG:	// 异常情况提示
					Toast.makeText(getApplicationContext(), "网络路径无效错误，下次更新吧", 5).show();
					enterHomeActivity();
				break;
				case PROTOCOL_EXCEPTION_DIALOG:	// 异常情况提示
					Toast.makeText(getApplicationContext(), "数据传输协议不匹配而导致无法与远程方进行通信，下次更新吧", 5).show();
					enterHomeActivity();
					break;
				case IO_EXCEPTION_DIALOG:	// 异常情况提示
					Toast.makeText(getApplicationContext(), "网络读写错误，下次更新吧", 5).show();
					enterHomeActivity();
					break;
				case JSON_EXCEPTION_DIALOG:	// 异常情况提示
					Toast.makeText(getApplicationContext(), "JSON数据错误，下次更新吧", 5).show();
					enterHomeActivity();
					break;
			}
			
		}
	};

	/**
	 * 创建欢迎界面
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		// 获取控件
		TextView tv_version = (TextView) findViewById(R.id.tv_version);
		System.err.println("IIIFF:" + tv_version.getId());
		String versionName = getVersionName();
		// 当前版本
		tv_version.setText("版本 " + versionName);
		
		// 更新信息
		tv_download_info = (TextView) findViewById(R.id.tv_download_info);

		/**
 		先获取配置信息，根据autoupdate的配置绝对是否检查更新
		 **/
		// 1.获取配置存储器（文件，模式）
		SharedPreferences sp = getSharedPreferences("mobileguard_setting_config", MODE_PRIVATE);
		// 2.读取配置（key，读不到时的默认值）。读取失败，默认是检测更新
		boolean autoupdate = sp.getBoolean("autoupdate", true);
		// 3.根据配置决定是否检查更新
		if(autoupdate){				// 更新
			getVersionUpdate();
		} else {					// 不更新
			// 如果配置不更新，仍然显示欢迎界面2秒，然后自动进入主界面
			handler.postDelayed(new Runnable(){
				@Override
				public void run() {
					enterHomeActivity();
				}}, 2000);
		}
		
	}
	
	/**
	 * 检查版本升级 
	 */
	private void getVersionUpdate() {
		
		new Thread(){
			@Override
			public void run() {
				
				// Messge包含必要的描述和属性数据，并且此对象可以被发送给android.os.Handler处理
				Message message = Message.obtain();
				
				// 启动时间
				long startTime = System.currentTimeMillis();
			
				try {
					// R.string.updateurl。在/res/values/里新建config.xml，保存更新请求网址
					URL updateurl = new URL(getString(R.string.updateurl));
					// 新建一个到远程目标的连接
					HttpURLConnection urlConnection = (HttpURLConnection) updateurl.openConnection();
					// 使用GET请求，全大写
					urlConnection.setRequestMethod("GET");
					// 请求超时
					urlConnection.setConnectTimeout(4000);
					// 返回状态码
					int responseCode = urlConnection.getResponseCode();
					// 200，正常
					if (responseCode == 200){
						// 远程连接的输入流
						InputStream inputStream = urlConnection.getInputStream();
						// 捕获内存缓冲区的数据,转换成字节数组
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						// 缓冲区
						byte[] buffer = new byte[1024];
						int len = 0;
						// 将缓冲区数据写入数组
						while((len = inputStream.read(buffer)) != -1){
							baos.write(buffer, 0, len);
						}
						// 读取结束后关闭输入流
						inputStream.close();
						// 转字节为可用字符串
						String updateinfo = baos.toString();
						// 关闭输出流
						baos.close();
						
						/****
						 * 05-26 13:34:14.447: W/System.err(2888): }
						 * 05-26 13:34:14.447: W/System.err(2888): org.json.JSONException: 
						 * 	Value ﻿ of type java.lang.String cannot be converted to JSONObject
						 * 解决：用notepad++将json文件以UTF-8无BOM格式编码
						 */
						/** JSON数据处理 **/
						JSONObject jsonObject = new JSONObject(updateinfo);
						// 根据key获取数据
						String version = (String) jsonObject.get("version");			// 新的版本
						String description = (String) jsonObject.get("description");	// 新版描述
						apkurl = (String) jsonObject.get("apkurl");						// 新版apk下载路径
						
						// 判断版本
						if(getVersionName().equals(version)){
							// 消息
							message.what=ENTER_HOME;	// 版本相同，无更新，进入应用界面
						}else{
							message.what=UPDATE_DIALOG;	// 版本不同，有更新，弹出更新对话框
						}
					}
				} catch (MalformedURLException e) {
					message.what=URL_EXCEPTION_DIALOG;	// URL无效错误
					e.printStackTrace();
				} catch (ProtocolException e) {
					message.what=PROTOCOL_EXCEPTION_DIALOG;	// 数据传输协议不匹配而导致无法与远程方进行通信
					e.printStackTrace();
				} catch (IOException e) {
					message.what=IO_EXCEPTION_DIALOG;	// 网络读写错误
					e.printStackTrace();
				} catch (JSONException e) {
					message.what=JSON_EXCEPTION_DIALOG;	// JSON数据错误
					e.printStackTrace();
				} finally{
					// 网络访问结束时间
					long endTime = System.currentTimeMillis();
					// 耗时-也就是欢迎界面的显示时长
					long distanceTime = endTime - startTime;
					// 欢迎界面至少显示2秒
					if( distanceTime < 2000){
						try {
							// 不到2s就等一会儿
							Thread.sleep(2000 - distanceTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					/** 向处理器发送消息代码 **/
					handler.sendMessage(message);
				}
			}
		}.start();

	}
	
	/**
	 * 进入应用的主界面
	 */
	private void enterHomeActivity() {
		// 1.新建意图（上下文，目标界面）
		Intent intent = new Intent(this, HomeActivity.class);
		// 2.实施意图
		startActivity(intent);
		
		// 3.关闭本界面
		finish();
	}

	/***
	 * ~~~第2次提交~~~
	 * 
	 * 动态获取当前应用版本
	 */
	private String getVersionName() {
		// 1.应用包管理器，管理应用程序包,通过它就可以获取应用程序信息
		PackageManager packageManager = getPackageManager();

		try {
			// 2.应用包信息，获取AndroidManifest.xml中的配置信息
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

			// 3.版本名
			String versionName = packageInfo.versionName;

			// 4.return
			return versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "读取失败";
	}

}
