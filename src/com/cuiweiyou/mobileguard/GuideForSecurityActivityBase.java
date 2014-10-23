package com.cuiweiyou.mobileguard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public abstract class GuideForSecurityActivityBase extends Activity {

	/** 手势识别器 **/
	private android.view.GestureDetector gd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 创建GesTureDetector对象
		gd = new GestureDetector(this, new SimpleOnGestureListener() {
			// ------------用户按下触摸屏、快速移动后松开-------------
			// e1 手势起点的移动事件。得到手指点下的位置
			// e2 当前手势点的移动事件
			// velocityX 每秒x轴方向移动的像素
			// velocityY 每秒y轴方向移动的像素
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				int FLING_MIN_DISTANCE/* 最短移动距离 */= 100, FLING_MIN_VELOCITY/*
																		 * 速度，
																		 * 每秒最小移动距离
																		 */= 200;

				// 手指从右向左滑动，打开右侧的界面
				if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
					showNextActivity();
				} else
				// 手指从屏幕左侧点下，向右滑动
				if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
					showPrevActivity();
				}

				return false;
			}
		});
	}
	private void showPrevActivity() {
		goPrevActivity();
	}

	private void showNextActivity() {
		goNextActivity();
	}

	/** 这里是Activity的Touch事件 **/
	public boolean onTouchEvent(MotionEvent event) {
		gd.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	/** 定义2个抽象方法，让子类实现界面的跳转 **/
	public abstract void goNextActivity();
	/** 定义2个抽象方法，让子类实现界面的跳转 **/
	public abstract void goPrevActivity();

}
