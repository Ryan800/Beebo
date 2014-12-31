package org.zarroboogs.weibo.dao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.zarroboogs.util.net.HttpUtility;
import org.zarroboogs.util.net.WeiboException;
import org.zarroboogs.util.net.HttpUtility.HttpMethod;
import org.zarroboogs.utils.AppLoggerUtils;
import org.zarroboogs.utils.WeiBoURLs;
import org.zarroboogs.weibo.bean.MessageReCmtCountBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: qii Date: 13-2-14
 */
public class TimeLineReCmtCountDao {

	private String getJson() throws WeiboException {
		String url = WeiBoURLs.TIMELINE_RE_CMT_COUNT;

		Map<String, String> map = new HashMap<String, String>();
		map.put("access_token", access_token);
		StringBuilder idsBuilder = new StringBuilder();
		for (String id : msgIds) {
			idsBuilder.append(",").append(id);
		}

		map.put("ids", idsBuilder.toString());

		return HttpUtility.getInstance().executeNormalTask(HttpMethod.Get, url, map);
	}

	public List<MessageReCmtCountBean> get() throws WeiboException {

		String json = getJson();

		List<MessageReCmtCountBean> value = null;
		try {
			value = new Gson().fromJson(json, new TypeToken<List<MessageReCmtCountBean>>() {
			}.getType());
		} catch (JsonSyntaxException e) {

			AppLoggerUtils.e(e.getMessage());
		}

		return value;
	}

	private String access_token;
	private List<String> msgIds;

	public TimeLineReCmtCountDao(String access_token, List<String> msgIds) {
		if (msgIds == null)
			throw new IllegalArgumentException("msgIds cant be null");
		int size = (msgIds.size() >= 100 ? 99 : msgIds.size());
		this.msgIds = new ArrayList<String>();
		this.access_token = access_token;
		this.msgIds.addAll(msgIds.subList(0, size));
	}
}
