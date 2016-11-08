package util;

import java.util.*;

import org.json.*;

public class Information {
	private String type;
	private String fromAdd;
	private String toAdd;
	private String content;
	private long date;
	
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
	public Information(String json) {
		try {
			JSONObject Json = new JSONObject(json);
			Map<String, Object> result = new HashMap<String, Object>();
			Iterator<?> iterator = Json.keys();
			String key;
			while(iterator.hasNext()) {
				key = (String)iterator.next();
				result.put(key, Json.get(key));
			}
			type = result.get("type").toString();
			fromAdd = result.get("fromAdd").toString();
			date = (Long) result.get("date");
			toAdd = result.get("toAdd").toString();
			//content = new String("hello");
			if(!type.equals("raisehand")) {
				content = result.get("content").toString();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public Information(String type, String fromAdd, String content, String toAdd) {
		this.type = type;
		this.fromAdd = fromAdd;
		this.content = content;
		this.date = (new Date()).getTime();
		this.toAdd = toAdd;
	}
	public static Information createRaiseHand(String fromAdd) {
		return new Information("raisehand", fromAdd, null, "Server");
	}
	public static Information createOperator(String type, String cmd) {
		return new Information(type, "Server", cmd, "Server");
	}
	public static Information createSession(String fromAdd, String content, String to) {
		return new Information("session", fromAdd, content, to);
	}
	public String toString() {
		JSONObject json = new JSONObject();
		try {
			json.put("type", type);
			json.put("fromAdd", fromAdd);
			json.put("toAdd", toAdd);
			json.put("content", content);
			json.put("date", date);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}
	public String getToAdd() {
		return toAdd;
	}
	public void setToAdd(String toAdd) {
		this.toAdd = toAdd;
	}
}

