package com.zeyuan.kyq.utils.Secret;


import java.net.URLEncoder;
import java.nio.ByteBuffer;


/**
 * Created by Administrator on 2016/4/9.
 *
 *
 * @author wwei
 */
public class MovingSecretUtils {

    public static class TEA {
        public static int fn = 0;//在解密服务端返回的数据时，fn需要置0
        private final static int[] KEY = new int[]{//加密解密所用的KEY
                0x1223311, 0x889922, 0x33, 0x44
        };

        public static void setKey(int[] key){
            KEY[0] = key[0];
            KEY[1] = key[1];
            KEY[2] = key[2];
            KEY[3] = key[3];
        }

        public static void setKey(String keyStr){
            if (keyStr!=null&&keyStr.length()==16){
                int[] keys = DefaultSecretUtils.getKey(keyStr.getBytes(),0);
                setKey(keys);
            }
        }

        public static int[] getKey(){
            return KEY;
        }

        private static long BYTE_1 = 0xFFL;
        private static long BYTE_2 = 0xFF00L;
        private static long BYTE_3 = 0xFF0000L;
        private static long BYTE_4 = 0xFF000000L;

        public static Long UIFILTER = Long.decode("0xffffffff");
        //加密
        private static byte[] encrypt(byte[] content, int offset, int[] key, int times){//times为加密轮数
            long[] tempInt = byteToInt(content, offset);
            long y = Long.decode(tempInt[0]+""), z = Long.decode(tempInt[1]+""),sum = 0,  i;
            long delta=Long.decode("0x9e3779b9"); //这是算法标准给的值
            int a = key[0], b = key[1], c = key[2], d = key[3];
            for (i = 0; i < 16; i++) {
                sum += delta;
                sum &= UIFILTER;
                y += ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
                y &= UIFILTER;
                z += ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
                z &= UIFILTER;
            }
            return LongToByte(new long[]{y,z}, 0);
        }

        //解密
        private static byte[] decrypt(byte[] encryptContent, int offset, int[] key, int times){

            long[] tempInt = byteToInt(encryptContent, offset);
            long y = Long.decode(tempInt[0]+""), z = Long.decode(tempInt[1]+""),sum = Long.decode("0xE3779B90"),  i;
            long delta=Long.decode("0x9e3779b9"); //这是算法标准给的值
            int a = key[0], b = key[1], c = key[2], d = key[3];
            for(i = 0; i < times; i++) {
                z += (~(((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d)) + 1);
                z &= UIFILTER;
                y += (~(((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b)) + 1);
                y &= UIFILTER;
                sum += ((~delta) + 1);
                sum &= UIFILTER;
            }
            return LongToByte(new long[]{y,z}, 0);
        }
        //byte[]型数据转成int[]型数据
        private static long[] byteToInt(byte[] content, int offset){
            if(content==null||content.length==0){
                return new long[]{};
            }
            long[] result = new long[content.length >> 2];
            for(int i = 0, j = 0; j < content.length; i++, j += 4){
                result[i] = (content[j]) & BYTE_1| (content[j + 1]) << 8 & BYTE_2|
                        (content[j + 2]) << 16 &BYTE_3 | (content[j + 3]) << 24 & BYTE_4;
            }
            return result;
        }

        private static byte[] LongToByte(long[] content, int offset){
            if(content==null||content.length==0){
                return new byte[]{};
            }
            byte[] result = new byte[content.length << 2];//乘以2的n次方 == 左移n位 即 content.length * 4 == content.length << 2
            for(int i = 0, j = offset; j < result.length; i++, j += 4){
                result[j + 3] = (byte) ((content[i] >> 24) & UIFILTER);
                result[j + 2] = (byte) ((content[i] >> 16) & UIFILTER);
                result[j + 1] = (byte) ((content[i] >> 8) & UIFILTER);
                result[j] = (byte) ((content[i]) & UIFILTER);
            }
            return result;
        }

        //通过TEA算法加密信息
        public static String encryptByTea(String info){
            if(info==null||info.length()==0){
                return "";
            }
            byte[] temp = info.getBytes();
            int n = 8 - temp.length % 8;//若temp的位数不足8的倍数,需要填充的位数

            byte[] encryptStr = new byte[temp.length + n];
            System.arraycopy(temp, 0, encryptStr, 0, temp.length);
            ByteBuffer eBuffer = ByteBuffer.allocate(encryptStr.length);
            byte[] temparray = new byte[8];
            for(int offset = 0; offset < encryptStr.length; offset += 8){
                System.arraycopy(encryptStr, offset, temparray,0 , 8);
                byte[] tempEncrpt = encrypt(temparray, 0, KEY, 16);
                eBuffer.put(tempEncrpt);
            }
            byte[] result = eBuffer.array();
            eBuffer.clear();
            return toHexAscii(result);
        }

        //通过TEA算法解密信息
        public static  String decryptByTea(String dec){
            if(dec==null||dec.length()==0){
                return "";
            }
            byte[] secretInfo = change(dec);
            byte[] decryptStr = null;
            ByteBuffer tempBuffer = ByteBuffer.allocate(secretInfo.length);
            byte[] temparray = new byte[8];
            for(int offset = 0; offset < secretInfo.length; offset += 8){
                System.arraycopy(secretInfo, offset, temparray, 0, 8);
                decryptStr = decrypt(temparray, 0, KEY, 16);
                tempBuffer.put(decryptStr);
            }
            byte[] tempDecrypt = tempBuffer.array();
            tempBuffer.clear();
            String s = new String(tempDecrypt, 0, tempDecrypt.length);
            return s.trim();
        }

        /*16进制字符串转byte[]*/
        public static byte[] change(String inputStr) {
            byte[] result = new byte[inputStr.length() / 2];
            for (int i = 0; i < inputStr.length() / 2; ++i)
                result[i] = (byte)(Integer.parseInt(inputStr.substring(i * 2, i * 2 +2), 16) & 0xff);
            return result;
        }

        /*byte[]转16进制字符串*/
        public static String toHexAscii(byte[] data){
            String[] temp = new String[data.length];
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<data.length;i++){
                int t = (int)data[i];
                temp[i] = Integer.toHexString(t & 0xFF).toUpperCase();
                if(temp[i].length()==1) temp[i] = "0" + temp[i];
                sb.append(temp[i]);
            }
            return sb.toString();
        }
    }

    public static byte[] getParamString(String[] args){
//        LogCustom.i(Const.TAG.ZY_HTTP, "请求参数是：" + Arrays.toString(args) + " + " + Arrays.toString(ZYApplication.appendParams));
        StringBuffer sb = new StringBuffer();
        if(args==null||args.length%2!=0){
            return new byte[0];
        }
        for(int i = 0; i<args.length ;i+=2){
            try {
                args[i] = URLEncoder.encode(args[i], "UTF-8");
                args[i+1] = URLEncoder.encode(args[i+1],"UTF-8");
            }catch (Exception e){
                try {
                    args[i] = URLEncoder.encode(args[i],"UTF-8");
                    args[i+1] = URLEncoder.encode(args[i+1],"UTF-8");
                }catch (Exception ex){
                    try {
                        args[i] = URLEncoder.encode(args[i],"UTF-8");
                        args[i+1] = URLEncoder.encode(args[i+1],"UTF-8");
                    }catch (Exception exx){

                    }
                }
            }
            sb.append(args[i]).append("=").append(args[i+1]).append("&");
            if (i == args.length-2){
                sb.append(args[i]).append("=").append(args[i+1]);
            }else {
                sb.append(args[i]).append("=").append(args[i+1]).append("&");
            }
        }
//        sb.append(ZYApplication.addParams);
//        LogCustom.i(Const.TAG.ZY_HTTP, "请求参数是：" + sb.toString());
        String temp = TEA.encryptByTea(sb.toString());
        byte[] result = TEA.change(temp);
        return result;
    }



    //byte[]型数据转成int[]型数据
    public static int[] getKey(byte[] content, int offset){
        if(content==null||content.length==0){
            return new int[]{};
        }
        int[] result = new int[content.length >> 2];
        for(int i = 0, j = 0; j < content.length; i++, j += 4){
            result[i] = (content[j])| (content[j + 1]) << 8|
                    (content[j + 2]) << 16 | (content[j + 3]) << 24;
        }
        return result;
    }

    public static void main(String[] args){
        String t1 = "abcdefghijklmn";
        String k1 = "aaaabbbbccccdddd";
        MovingSecretUtils.TEA.setKey(k1);
        System.out.println("key：" + k1);
        System.out.println("加密前：" + t1);
        String e1 = MovingSecretUtils.TEA.encryptByTea(t1);
        System.out.println("加密后：" + e1);
        String d1 = MovingSecretUtils.TEA.decryptByTea(e1);
        System.out.println("解密后：" + d1);
    }

}
