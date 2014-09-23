package com.cuiweiyou.mobileguard;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

/**
~~~第2次提交~~~

 * 第一个界面的作用：
 * 1.展示产品Logo
 * 2.应用程序初始化（元数据-数据库、配置信息、尺寸、登录信息）
 * 3.检查新版本
 * 4.检查当前程序的注册信息、版权
 * 5.检查网络
 * 6.。。。。。。
 */

/**
~~~第2次提交~~~

 * 创建项目时，Theme为None。本Activity继承自ActionBarActivity，测试时报错： Unable to start
 * activity ComponentInfo : java.lang.IllegalStateException: You need to use a
 * Theme.AppCompat theme (or descendant) with this activity.
 */
public class WelcomeActivity extends Activity {

	// 消息代码,进入主界面
	protected static final int ENTER_HOME = 0;
	// 消息代码,更新提示
	protected static final int UPDATE_DIALOG = 1;
	// 消息代码,异常状况
	protected static final int Exception_DIALOG = 2;
	
	/**
	~~~第3次提交~~~
	
	 * 异步的消息处理器，根据消息作出反应 
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
					Toast.makeText(getApplicationContext(), "有新版本啦", 0).show();
					enterHomeActivity();
				break;
				case Exception_DIALOG:	// 异常情况提示
					Toast.makeText(getApplicationContext(), "有点意外，下次更新吧", 0).show();
					enterHomeActivity();
				break;
			}
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		// 获取控件
		TextView tv_version = (TextView) findViewById(R.id.tv_version);
		// 更新文字
		tv_version.setText("版本 " + getVersionName());

		/** 
		~~~第3次提交~~~
		
 		检查版本升级
 		**/
		getVersionUpdate();
	}
	
	/** 
	 * ~~~第3次提交~~~
	 * 
	 * 检查版本升级 
	 */
	private void getVersionUpdate() {
		/**
		 * JSON数据（编码：UTF-8） 
		 * version：版本
		 * description：描述 
		 * apkurl：文件地址
		 * 可以将此文件保存为html放在网站上，如：
		 * http://www.cuiweiyou.com/wp-content/uploads/2014/09/updateinfo.html
		 */
		
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
						
						/** JSON数据处理 **/
						JSONObject jsonObject = new JSONObject(updateinfo);
						// 根据key获取数据
						String version = (String) jsonObject.get("version");
						String description = (String) jsonObject.get("description");
						String apkurl = (String) jsonObject.get("apkurl");
						
						// 判断版本
						if(getVersionName().equals(version)){
							// 消息
							message.what=ENTER_HOME;	// 进入应用界面
						}else{
							message.what=UPDATE_DIALOG;	// 更新对话框
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					message.what=Exception_DIALOG;	// 异常情况提示
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
					
					/** 向处理器发送消息 **/
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
	 * 动态获取应用版本
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
