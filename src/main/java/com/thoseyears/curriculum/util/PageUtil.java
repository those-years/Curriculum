package com.thoseyears.curriculum.util;

import java.util.ArrayList;
import java.util.List;

import com.thoseyears.curriculum.entity.Competition;
import net.sf.json.JSONArray;

public class PageUtil {
	public static List<Competition> createPage(List<Competition> lists, String pageNunmber, String pageSize) {
		List<Competition> page = new ArrayList<Competition>();
		int x = Integer.parseInt(pageNunmber);
		int y = Integer.parseInt(pageSize);
		if ((x * y - y) >= lists.size()) {
			boolean flag = false;
			String msg = "请求无数据";
			return null;
		}
		int minflag = (Integer.parseInt(pageNunmber) - 1) * Integer.parseInt(pageSize) - 1;
		int maxflag = Integer.parseInt(pageNunmber) * Integer.parseInt(pageSize) - 1;
		Competition competition;
		for (int i = 0; i < lists.size(); i++) {
			if (i > minflag && i <= maxflag) {
				competition = lists.get(i);
				page.add(competition);
			}
		}
		return page;
	}
}
