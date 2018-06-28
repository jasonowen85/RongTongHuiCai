package com.shyx.rthc.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by apple on 16/9/22.
 */
public class PrintJson {

    public static String TAG = PrintJson.class.getSimpleName();
    private static String tab = "\t";
    private static String printTAB = "";

    public static void print(JSONObject jo ){
        print(jo, TAG);
    }

    public static void print(JSONObject jo, String tag){
        if(!L.IS_DEBUG) return;
        printTAB = "";
        TAG = tag;

        L.i(TAG, " print() ");
        print(jo, false);

        TAG = PrintJson.class.getSimpleName();
    }

    /**
     * @param jo
     * @param isAll 是否打印lists所有option, false 只打印第一条数据; true打印所有数据。
     */
    public static void print(JSONObject jo, boolean isAll){
        if(!L.IS_DEBUG || jo == null) return;
        L.i(TAG, printTAB +tab +" {");
        printKeyValue(jo);
        printTAB += tab;

        Iterator<?> its = jo.keys();
        while (its.hasNext()) {// 遍历JSONObject
            String key =  its.next().toString();
            try {
                if(jo.optJSONObject(key) != null){
                    L.i(TAG, printTAB +tab + key );
                    print(jo.optJSONObject(key), isAll);
                }else if(jo.optJSONArray(key) != null){
                    JSONArray array = jo.getJSONArray(key);
                    L.i(TAG, printTAB +tab + key + " = " + array.length()+"条数据");
                    if(isAll){ //是否打印所有
                        for(int n = 0; n< array.length(); n++){
                            print(array.optJSONObject(n), isAll);
                        }
                    }else{
                        print(array.optJSONObject(0), isAll);
                    }
                }

            } catch (Exception e) {
                System.err.println(".printJson() ex "+e.getMessage());
                e.printStackTrace();
            }
        }

        StringBuilder buildTab = new StringBuilder(printTAB);
        buildTab.delete(0, tab.length());
        printTAB = buildTab.toString();
        L.i(TAG, printTAB +tab +" }");
    }

    private static void printKeyValue(JSONObject jo ){
        Iterator<?> it = jo.keys();
        Set<String> treeSet = new TreeSet<String>();

        while (it.hasNext()) {// 遍历JSONObject
            String key = it.next().toString();
            if(jo.optJSONObject(key) != null || jo.optJSONArray(key) != null){
                continue;
            }
            String value = jo.optString(key);
            String str = key + " : " + value;
            treeSet.add(str);
        }

        if (treeSet.size() > 0){
            for(String msg: treeSet){
                L.i(TAG, printTAB +tab+tab + msg);
            }
        }
    }

}
