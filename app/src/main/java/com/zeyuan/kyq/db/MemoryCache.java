package com.zeyuan.kyq.db;


import android.graphics.Bitmap;
import android.util.LruCache;

import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.LogCustom;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2016/5/10.
 *
 * @classname MemoryCache
 *
 * @author wwei
 */
public class MemoryCache {

    // 1,two memory cache 2,file cache 3,network get
    public static final String TAG = "memory";

    // hard cache memory size
    private final static int HARD_MEMORY_SIZE = 32 * 1024 * 1024;// 8m
    // sofe cache num
    private final static int SOFT_CACHE_CAPACITY = 45;
    // hard cache
    private static LruCache<String, Object> mHardBitmapCache;

    public static void openCache(){
        if(mHardBitmapCache==null){
            mHardBitmapCache = new LruCache<String, Object>(HARD_MEMORY_SIZE) {
                @Override
                protected void entryRemoved(boolean evicted, String key,Object oldValue, Object newValue) {
                    LogCustom.v(TAG, "*********hard cache is full , push to soft cache*************");
                    LogCustom.i(Const.TAG.ZY_DATA, "硬引用缓存区满，将 key:"+key+" 推入到软引用缓存区");
                    // 硬引用缓存区满，将一个最不经常使用的oldValue推入到软引用缓存区
                    mSoftBitmapCache.put(key, new SoftReference<>(oldValue));
                }
            };
        }
    }

    @SuppressWarnings("serial")
    private final static LinkedHashMap<String, SoftReference<Object>> mSoftBitmapCache = new LinkedHashMap<String, SoftReference<Object>>(
            SOFT_CACHE_CAPACITY, 0.75f, true) {
        @Override
        public SoftReference<Object> put(String key, SoftReference<Object> value) {
            LogCustom.v(TAG, "*******SoftReference******get put bitmap******");
            return super.put(key, value);
        }

        @Override
        protected boolean removeEldestEntry(
                Entry<String, SoftReference<Object>> eldest) {
            if (size() > SOFT_CACHE_CAPACITY) {
                LogCustom.v(TAG, "Soft Reference  limit , purge one");
                return true;
            }
            return false;
        }

    };

    // 缓存object数据
    public static boolean putObjectInCache(String key, Object object) {

        if (object != null) {
            synchronized (mHardBitmapCache) {

                LogCustom.v(TAG, "putBitmapInCache...HardBitmapCache.size->" + mHardBitmapCache.size() + " key:" + key);
                mHardBitmapCache.put(key, object);
            }
            return true;
        }
        return false;
    }

    // 从缓存中获取bitmap
    public static Object getObjectFromCache(String key) {

        Object object = null;
        synchronized (mHardBitmapCache) {
            object = mHardBitmapCache.get(key);
            if (object != null) {
                LogCustom.v(TAG, "getBitmapFrom  Hard  Cache.............key:" + key);
                return object;
            }
        }
        // 硬引用缓存区间中读取失败，从软引用缓存区间读取
        synchronized (mSoftBitmapCache) {
            SoftReference<Object> bitmapReference = mSoftBitmapCache.get(key);
            if (bitmapReference != null) {
                object =  bitmapReference.get();
                if (object != null) {
                    LogCustom.v(TAG, "getBitmapFrom  Soft Cache..........key:" + key);
                    return object;
                } else {
                    LogCustom.v(TAG, "soft reference is recycle...");
                    mSoftBitmapCache.remove(key);
                }
            }
        }
        return object;
    }

    // 缓存bitmap
    public static boolean putBitmapInCache(String key, Object bitmap) {

        if (bitmap != null) {
            synchronized (mHardBitmapCache) {

                LogCustom.v(TAG, "putBitmapInCache...HardBitmapCache.size->" + mHardBitmapCache.size() + " key:" + key);
                mHardBitmapCache.put(key, bitmap);
            }
            return true;
        }
        return false;
    }

    // 从缓存中获取bitmap
    public static Bitmap getBitmapFromCache(String key) {

        Bitmap bitmap = null;
        synchronized (mHardBitmapCache) {
            bitmap = (Bitmap) mHardBitmapCache.get(key);
            if (bitmap != null) {
                LogCustom.v(TAG, "getBitmapFrom  Hard  Cache.............key:" + key);
                return bitmap;
            }
        }
        // 硬引用缓存区间中读取失败，从软引用缓存区间读取
        synchronized (mSoftBitmapCache) {
            SoftReference<Object> bitmapReference = mSoftBitmapCache.get(key);
            if (bitmapReference != null) {
                bitmap = (Bitmap) bitmapReference.get();
                if (bitmap != null) {
                    LogCustom.v(TAG, "getBitmapFrom  Soft Cache..........key:" + key);
                    return bitmap;
                } else {
                    LogCustom.v(TAG, "soft reference is recycle...");
                    mSoftBitmapCache.remove(key);
                }
            }
        }
        return bitmap;
    }

    public static void freeMemory() {

        LogCustom.v(TAG, "freeMemory.............");

        mHardBitmapCache.evictAll();
        mSoftBitmapCache.clear();
        // Clear the cache, calling entryRemoved(boolean, K, V, V) on each
        // removed entry.
    }
}

