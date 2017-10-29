package com.zeyuan.kyq.utils;

import com.bumptech.glide.load.Key;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * Created by Administrator on 2015/11/20.
 * You can also define your own signature by implementing the Key interface. Be sure to implement equals(), hashCode() and the updateDiskCacheKey() method:
 * glide 的缓存类
 */
public final class IntegerVersionSignature implements Key {

    private int currentVersion;

    public IntegerVersionSignature(int currentVersion) {
        this.currentVersion = currentVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IntegerVersionSignature) {
            IntegerVersionSignature other = (IntegerVersionSignature) o;
            return currentVersion == other.currentVersion;
        }
        return false;
    }

    private int signature = 16 * 1024 * 1024;

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) throws UnsupportedEncodingException {
        messageDigest.update(ByteBuffer.allocate(Integer.SIZE).putInt(signature).array());
    }

    @Override
    public int hashCode() {
        return currentVersion;
    }
}
