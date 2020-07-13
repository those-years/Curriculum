package com.thoseyears.curriculum.util;

import net.sf.json.JSONArray;
import  net.sf.json.JSONObject;
import java.util.List;

public class ResponseUtil {
    public static JSONObject ResponseDell(String msg,List<Object> lists,int code){
        JSONObject result = new JSONObject();
        System.out.println(lists.get(0));
        JSONArray results = JSONArray.fromObject(lists.get(0));
        result.put("msg", msg);
        result.put("code", code);
        result.put("count", results.size());
        result.put("data", lists.get(0));
        return result;
    }
}
