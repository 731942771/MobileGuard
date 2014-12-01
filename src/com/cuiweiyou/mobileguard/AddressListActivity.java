package com.cuiweiyou.mobileguard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cuiweiyou.mobileguard.interfaces.ICallBack;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AddressListActivity extends Activity {
	
	private static ICallBack icb;
	
	public static void setCallBack(ICallBack cb){
		icb = cb;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addresslist_select);
		
		ListView lv_addresslist = (ListView) findViewById(R.id.lv_addresslist);
		
		// 获取联系人
		final List<Map<String, String>> data = getAddressList();
		
		// Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to
		// 上下文，数据，单元格条目，数据中的key，单元格上的对应控件id。
		lv_addresslist.setAdapter(
			new SimpleAdapter(
				AddressListActivity.this, 
				data, 
				R.layout.grid_item_addresslist, 
				new String[]{"name", "phone"}, 
				new int[]{R.id.tv_grid_item_contact_name, R.id.tv_grid_item_contact_phone}
			)
		);
		
		// 为每个联系人条目添加点击事件
		lv_addresslist.setOnItemClickListener(new OnItemClickListener() {
			// 根据点击的条目的索引，得到联系人信息
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String name = data.get(position).get("name");
				String phone = data.get(position).get("phone");
				
				Intent i = new Intent();
				i.putExtra("name", name);
				i.putExtra("phone", phone);
				
				
				/*** 方式1：向打开此界面的上一个界面，传递数据 **/
				//setResult(0, i);
				/*** 方式2：通过回调往上一个界面传递数据  **/
				icb.callback(phone);
				
				finish();
				
				System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZzz");
			}
		});
	}

	/** 得到手机中的联系人表单 **/
	private List<Map<String, String>> getAddressList() {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		
		// 保存联系人信息的两个表
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");	// 索引表：保存联系人索引
		Uri dataUri = Uri.parse("content://com.android.contacts/data");		// 信息表：保存联系人姓名、电话
		
		// 内容解析器，访问ContentProvider提供的数据。查询（表，列，条件，条件参数，排序列）
		Cursor cursor = getContentResolver().query(uri, new String[]{"contact_id"}, null, null, null);
		
		// 如果得到的索引数量有效
		if(cursor != null && cursor.getCount() > 0){
			Map<String, String> contact;
			
			// 遍历索引数据
			while (cursor.moveToNext()){
				// 保存每个联系人的数据
				contact = new HashMap<String, String>();
				
				int id = cursor.getInt(0);						// 得到联系人的索引
				String selection = "raw_contact_id = ?";		// 在信息表里的查询条件
				String[] selectionArgs = {String.valueOf(id)};	// 查询参数
				
				// 查询信息表（表，列{信息内容列，信息内容的类型标识列}，条件，参数，排序）
				Cursor datas = getContentResolver().query(dataUri, new String[]{"data1", "mimetype"}, selection, selectionArgs, null);
				
				// 如果得到的信息数据有效
				if(datas != null && datas.getCount() >0){
					// 遍历信息条目
					while(datas.moveToNext()){
						String mimeType = datas.getString(1);	// 条目里的 信息类型
						String datal = datas.getString(0);		// 条目里的 信息内容
						// 如果是“电话”类型的标识列
						if("vnd.android.cursor.item/phone_v2".equals(mimeType)){
							// 那么本条目中的信息内容就是电话号码
							contact.put("phone", datal);
						}
						// 如果是“姓名”类型的标识列
						if("vnd.android.cursor.item/name".equals(mimeType)){
							// 那么本条目中的信息内容就是联系人姓名
							contact.put("name", datal);
						}
					}
					datas.close();
				}
				data.add(contact);
			}
			cursor.close();
		}
		
		return data;
	}
}
