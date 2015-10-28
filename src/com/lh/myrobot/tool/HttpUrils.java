package com.lh.myrobot.tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.lh.myrobot.entity.Chat;

public class HttpUrils {
	public static final String TURING_URL="http://www.tuling123.com/openapi/api";
	public static final String API_KEY="d5a16d6af4e9b86e8a181d5b4b3e6b92";
	
	public static Chat deGet(String msg){
		String url=setParams(msg);
		if(TextUtils.isEmpty(url)){
			return null;
		}
		Chat chat=new Chat();
		try {
			URL urlNet=new URL(url);
			HttpURLConnection conn=(HttpURLConnection) urlNet.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(3000);
			
			InputStream in=null;
			ByteArrayOutputStream bos=null;
			int code=conn.getResponseCode();
			while(code==200){
				in=conn.getInputStream();
				bos=new ByteArrayOutputStream();
				byte[] buff=new byte[1024];
				int len=0;
				while((len=in.read(buff))!=-1){
					bos.write(buff, 0, len);
				}
				bos.flush();
				String result=new String(bos.toByteArray(),"utf-8");
				JSONObject jsonObject=new JSONObject(result);
				int typeCode=jsonObject.getInt("code");
				chat.setDate(new Date());
				chat.setName("若白");
				chat.setType(Type.INCOMING);
				if(typeCode==100000){
					chat.setTypeCode(typeCode);
					chat.setText(jsonObject.getString("text"));
				}else if(typeCode==200000){
					chat.setTypeCode(typeCode);
					chat.setText(jsonObject.getString("text"));
					chat.setUrl(jsonObject.getString("url"));
//				}else if(typeCode==302000){
//					NewsBean newsBean=gson.fromJson(result, NewsBean.class);
//					chat.setTypeCode(typeCode);
//					chat.setText(newsBean.getText());
//					chat.setUrl(newsBean.getList());
				}
			}
				
		
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return chat;
	}

	//设置发送的url：URL+api_key+msg
	private static String setParams(String msg) {
		String url="";
		try {
			url=TURING_URL+"?key="+API_KEY+"&info="
					+URLEncoder.encode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
}
