package com.cuiweiyou.mobileguard.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

/**
 * 当广播接收器收到指定命令的短信，启动此服务获取本手机位置，发送到安全号码
 * @author Administrator
 */
public class MyLocationService extends Service {
	
	// 位置管理器
	private LocationManager lm;
	// 位置监听器
	private MyLocationListener locationListener;
	
	/**
	 * 服务绑定
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 服务创建
	 */
	@Override
	public void onCreate() {
		// 得到位置服务
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		// 得到位置信息的提供者
		List<String> allProviders = lm.getAllProviders();
		
		for (String provider : allProviders) {
			System.out.println(provider);	// passive基站，gps卫星定位
		}
		
		// 条件
		Criteria criteria = new Criteria();
		// 精度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);	//设置为最大精度
		//criteria.setAltitudeRequired(false);			//不要求海拔信息
		//criteria.setBearingRequired(false);			//不要求方位信息 
		//criteria.setCostAllowed(true);				//是否允许付费。蜂窝网络
		//criteria.setPowerRequirement(Criteria.POWER_LOW);//对电量的要求 
		
		// 根据条件获取最好的地址信息提供者
		String provider = lm.getBestProvider(criteria, true);
		
		// 监听器
		locationListener = new MyLocationListener();
		
		// 请求位置更新（定位方式，最短更新时长-ms，最短更新距离-m，监听器）
		lm.requestLocationUpdates(provider, 0, 0, locationListener);
		
		super.onCreate();
	}

	/**
	 * 服务销毁
	 */
	@Override
	public void onDestroy() {
		// 当界面关闭时移除监听
		lm.removeUpdates(locationListener);
		// 清空监听器
		locationListener = null;
		
		super.onDestroy();
	}
	
	/** 自定义的位置监听器 **/
	class MyLocationListener implements LocationListener{

		/**
		 * 火星和地球经纬度转换器
		 */
		private ModifyOffset offset;
		/**
		 * 转换后的地球坐标
		 */
		private PointDouble double1;

		// 位置改变，回调函数
		@Override
		public void onLocationChanged(Location location) {
			// 经度
			double longitude = location.getLongitude();
			// 纬度
			double latitude = location.getLatitude();
			// 精度
			float accuracy = location.getAccuracy();
			
			/*************------------------------\|/
			 * 发短信给安全号码
			 */
			
			// 转换火星坐标到真实坐标
			try {
				// 坐标库
				InputStream is = getAssets().open("axisoffset.dat");
				// 转换器
				offset = ModifyOffset.getInstance(is);
				// 1. 火星 -> 地球 （经度，纬度）
				double1 = offset.s2c(new PointDouble(longitude, latitude));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 2. 得到地球坐标
			// 经度
			String strLongitude = "Longitude:" + String.valueOf(double1.x) + "\n";
			// 纬度
			String strLatitude = "Latitude:" + String.valueOf(double1.y) + "\n";
			// 精度
			String strAccuracy = "Accuracy:" + String.valueOf(accuracy) + "\n";
			
			// 配置文件
			SharedPreferences sp = getSharedPreferences("mobileguard_setting_config", MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.putString("lastLocation", strLongitude + strLatitude + strAccuracy);
			edit.commit();
			
		}

		// 状态改变（设置中开启或关闭GPS），回调方法
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		// 某个位置提供者（基站、gps）可以使用，回调此方法
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		// 某个位置提供者（基站、gps） 不能 使用，回调函数
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
