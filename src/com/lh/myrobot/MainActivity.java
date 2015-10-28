package com.lh.myrobot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.lh.myrobot.adapter.ListViewAdapter;
import com.lh.myrobot.entity.Chat;
import com.lh.myrobot.tool.HttpUrils;
import com.lh.myrobot.tool.Type;

public class MainActivity extends Activity {
	ListView listView;
	EditText chat_edit;
	List<Chat> list;
	ListViewAdapter adapter;
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			Chat chat=(Chat) msg.obj;
			list.add(chat);
			adapter.notifyDataSetChanged();
			//定位到listView的底部
			listView.setSelection(listView.getBottom());
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		listView=(ListView) findViewById(R.id.mid_listView);
		chat_edit=(EditText) findViewById(R.id.edit_content);
		list=new ArrayList<Chat>();
		adapter=new ListViewAdapter(this, list);
		listView.setAdapter(adapter);
		
		
		Chat c1=new Chat();
		c1.setDate(new Date());
		c1.setName("若白");
		c1.setText("你好我是若白");
		c1.setType(Type.INCOMING);
		
		list.add(c1);
		
		//为编辑内容框设置事件监听
		chat_edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				listView.setSelection(listView.getBottom());
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		chat_edit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				listView.setSelection(listView.getBottom());
			}
		});
	}
	//点击发送内容按钮
	public void sendButton(View v){
		final String msg=chat_edit.getText().toString();
		if(TextUtils.isEmpty(msg)){
			return;
		}
		
		Chat c2=new Chat();
		c2.setDate(new Date());
		c2.setName("淡");
		c2.setText(msg);
		c2.setType(Type.OUTCOMING);
		list.add(c2);
		
		adapter.notifyDataSetChanged();
		listView.setSelection(listView.getBottom());
		
		chat_edit.setText("");
		
		//关闭输入法的软键盘
		InputMethodManager im=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromInputMethod(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		new Thread(){
			public void run(){
				Chat c3=HttpUrils.deGet(msg);
				Message m=Message.obtain();
				m.obj=c3;
				handler.sendMessage(m);
				
			}
		}.start();
	}
}
