package com.yuangee.consumer.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Description: SharedPreferences存储，支持对象加密存储
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 2016-12-19 15:12
 */
public class SpCache {
    private SharedPreferences sp;

    public SpCache(Context context) {
        this(context, "sp_cache");
    }

    public SpCache(Context context, String fileName) {
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSp() {
        return sp;
    }

    public void put(String key, Object ser) {
        try {
            if (ser == null) {
                sp.edit().remove(key).apply();
            } else {
                byte[] bytes = ByteUtil.objectToByte(ser);
                bytes = BASE64.encode(bytes);
                put(key, HexUtil.encodeHexStr(bytes));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object get(String key) {
        try {
            String hex = get(key, (String) null);
            if (hex == null) return null;
            byte[] bytes = HexUtil.decodeHex(hex.toCharArray());
            bytes = BASE64.decode(bytes);
            Object obj = ByteUtil.byteToObject(bytes);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean contains(String key) {
        return sp.contains(key);
    }

    public void remove(String key) {
        sp.edit().remove(key).apply();
    }

    public void clear() {
        sp.edit().clear().apply();
    }

    public void put(String key, String value) {
        if (value == null) {
            sp.edit().remove(key).apply();
        } else {
            sp.edit().putString(key, value).apply();
        }
    }

    public void put(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    public void put(String key, float value) {
        sp.edit().putFloat(key, value).apply();
    }

    public void put(String key, long value) {
        sp.edit().putLong(key, value).apply();
    }

    public void putInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    public String get(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public boolean get(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public float get(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public long get(String key, long defValue) {
        return sp.getLong(key, defValue);
    }
}
