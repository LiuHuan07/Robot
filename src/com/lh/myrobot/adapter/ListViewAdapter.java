package com.lh.myrobot.adapter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lh.myrobot.R;
import com.lh.myrobot.entity.Chat;
import com.lh.myrobot.tool.Type;

public class ListViewAdapter extends BaseAdapter{
	Context context;
	List<Chat> list;
	
	public ListViewAdapter(Context context,List<Chat> list){
		this.context=context;
		this.list=list;
	}
	@Override
	public int getCount() {
		int count=0;
		if(list!=null){
			count=list.size();
		}
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Chat chat=list.get(position);
		View view=null;
		if(convertView!=null){
			view=convertView;
		}else{
			if(getItemViewType(position)==0){
				view=LayoutInflater.from(context).inflate(R.layout.listview_layout_from, parent,false);
				ChatHandler handler=new ChatHandler();
				handler.chat_text=(TextView) view.findViewById(R.id.chat_text_from);
				handler.data_text=(TextView) view.findViewById(R.id.data_text_from);
				handler.image=(ImageView) view.findViewById(R.id.image_from);
				view.setTag(handler);
			}else{
				view=LayoutInflater.from(context).inflate(R.layout.listview_layout_to, parent,false);
				ChatHandler handler=new ChatHandler();
				handler.chat_text=(TextView) view.findViewById(R.id.chat_text_to);
				handler.data_text=(TextView) view.findViewById(R.id.data_text_to);
				handler.image=(ImageView) view.findViewById(R.id.image_to);
				view.setTag(handler);
			}
			
		}
		ChatHandler handler2=(ChatHandler) view.getTag();
		
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		handler2.data_text.setText(df.format(chat.getDate()));
		int code=chat.getTypeCode();
		if(code==100000){
			handler2.chat_text.setText(chat.getName()+":"+chat.getText());
		}else if(code==200000){
			handler2.chat_text.setText(chat.getName()+":"+chat.getText()+chat.getUrl());
		}
//			else if(code==302000){
//			handler2.chat_text.setText(chat.getText()+chat.getUrl());
//			Log.i("NEWS", chat.getText()+chat.getUrl());
//			
//		}
		else{
			handler2.chat_text.setText(chat.getName()+":"+chat.getText());
			Log.i("ELSE", chat.getText());
			
		}
		
		return view;
	}
	@Override
	public int getItemViewType(int position) {
		return list.get(position).getType()==Type.INCOMING?0:1;
	}
	@Override
	public int getViewTypeCount() {
		
		return 2;
	}
	class ChatHandler{
		TextView data_text,chat_text;
		ImageView image;
		
	}
	

}
