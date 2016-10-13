package util;

import java.util.*;

import org.json.*;

public class Information {
	private String type;
	private String fromAdd;
	private String content;
	private long date;
	private boolean isPub;
	
	public String getType() {
		return type;
	}
	public String getFromAdd() {
		return fromAdd;
	}
	public String getContent() {
		return content;
	}
	public long getDate() {
		return date;
	}
	public boolean isPub() {
		return isPub;
	}
	public Information(String json) {
		try {
			JSONObject Json = new JSONObject(json);
			Map<String, Object> result = new HashMap<String, Object>();
			Iterator iterator = Json.keys();
			String key;
			while(iterator.hasNext()) {
				key = (String)iterator.next();
				result.put(key, Json.get(key));
			}
			type = result.get("type").toString();
			fromAdd = result.get("fromAdd").toString();
			date = (Long) result.get("date");
			isPub = (Boolean) result.get("isPub");
			//content = new String("hello");
			if(type.equals("session")) {
				content = result.get("content").toString();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public Information(String type, String fromAdd, String content, boolean isPub) {
		this.type = type;
		this.fromAdd = fromAdd;
		this.content = content;
		this.date = (new Date()).getTime();
		this.isPub = isPub;
	}
	public static Information createRaiseHand(String fromAdd) {
		return new Information("raisehand", fromAdd, null, true);
	}
	public static Information createSession(String fromAdd, String content) {
		return new Information("session", fromAdd, content, true);
	}
	public String toString() {
		JSONObject json = new JSONObject();
		try {
			json.put("type", type);
			json.put("fromAdd", fromAdd);
			json.put("content", content);
			json.put("date", date);
			json.put("isPub", isPub);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}
//	public static void main(String[] args) throws UnknownHostException {
//		System.out.println(createRaiseHand(InetAddress.getLocalHost().toString()));
//		System.out.println(createSession(InetAddress.getLocalHost().toString(), "我要上天啦"));
//	}
}

