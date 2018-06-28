package com.shyx.rthc.utils;


import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StringUtils {

    public static final String TAG = "StringUtils";

    /**
     * 返回具体的格式
     * @param pattern
     * @param input
     * @return
     */
    public static String getSpecificFormat(String pattern,double input){
        DecimalFormat df = new DecimalFormat(pattern);

        return df.format(input);
    }

    /**
     * 字符串是否为空，null也视为空
     */
    public static boolean isEmpty(String string) {
        return null == string || "".equals(string.replace(" ", "")) || "null".equals(string.trim());
    }

    /**
     * 解析double类型字符串
     *
     * @param strNum
     * @return
     */
    public static double parseDouble(String strNum) {
        if (isEmpty(strNum)) {
            return 0;
        }//有的金额带格式符‘,’固先去除此符号。
        else if (NumberUtils.isNumber(strNum.replace(",", "").replace(" ", ""))) {
            return Double.parseDouble(strNum);
        } else {
            return 0;
        }
    }

    public static int parseInteger(String strNum) {
        return (int) parseDouble(strNum);
    }

    /**
     * 字符串长度
     *
     * @param str
     * @return
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * null转换成空字符串
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String) str : str.toString()));
    }

    /**
     * 设置金额，添加元
     *
     * @return
     */
    public static String setAmount(Object amount) {

        if (amount instanceof Double) {
            //格式化double为两位小数
            return formatDouble((Double) amount) + "元";
        }
        return amount + "元";
    }

    /**
     * 格式化为两位小数
     *
     * @param num
     * @return
     */
    public static String formatDouble(double num) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(num);
    }

    /**
     * 格式化为一位小数
     *
     * @param num
     * @return
     */
    public static String formatDouble2(double num) {
        DecimalFormat df = new DecimalFormat("#0.0");
        return df.format(num);
    }

    /**
     * 将将所有的数字、字母及标点全部转为全角字符避免TextView自动换行出现的排版混乱问题
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static Map<String, Object> getMapForJson(String jsonStr) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonStr);
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }
}
