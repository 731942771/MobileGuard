package com.cuiweiyou.mobileguard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ~~~第3次提交，新的界面~~~
 * 应用程序的主界面
 */
public class HomeActivity extends ActionBarActivity {
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		/** 
 		启动欢迎界面时使用动画
 		**/
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(2000);
		findViewById(R.id.layout_home).startAnimation(aa);

		/**
		 * ~~~第5次提交，主界面9宫格实现~~~
		 */
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
				TextView item = (TextView) view.findViewById(R.id.tv_grid_item);	// 获得格子中的文本控件
				Toast.makeText(HomeActivity.this, "name:" + item.getText() + ",position:" + position, 0).show();
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
						break;
						
					case 8:
						Intent i_grid = new Intent(HomeActivity.this, SettingActivity.class);
						startActivity(i_grid);
						break;
				}
			}
		});
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
			View grid_item;
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
			
			return grid_item;
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
