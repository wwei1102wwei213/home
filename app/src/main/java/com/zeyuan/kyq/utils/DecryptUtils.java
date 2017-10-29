package com.zeyuan.kyq.utils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2016/1/6.
 * 解密 加密
 */
public class DecryptUtils {
    public static String decodeBase64(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        String temp = "";
                try {
                    temp = new String(Base64.decode(content.getBytes(), Base64.DEFAULT));
                } catch (Exception e) {
                    return "";
                }
        return temp.trim();
    }

    /**
     * 加密
     * @param content
     * @return
     */
    public static String encode(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        return new String(Base64.encode(content.getBytes(), Base64.NO_WRAP)).trim();
    }

    public static String URLAnddecodeBase64(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        String temp = "";
        try {
            temp = URLDecoder.decode(content);
            temp = new String(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        } catch (Exception e) {
            return "";
        }
        return temp.trim();
    }

    /**
     * 加密
     * @param content
     * @return
     */
    public static String encodeAndURL(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        String str = new String(Base64.encode(content.getBytes(), Base64.NO_WRAP)).trim();
        return URLEncoder.encode(str);
    }


   public static class Tea {
        private final static int[] KEY = new int[] {// 加密解密所用的KEY
         //       0x789f5645, 0xf68bd5a4, 0x81363ffa, 0x458fac58
//                0x39393938,0x38380000,0x0,0x0
                0x11, 0x22, 0x33, 0x44
        };
//	static Tea tea = new Tea();
//
//	public static void main(String[] args) {
//		String info = "www.blogjava.net/orangehf";
//		System.out.println("原数据：" + info);
////		for (byte i : info.getBytes())
////			System.out.print(i + " ");
//
//		byte[] encryptInfo = encryptByTea(info);
////		System.out.println();
//		System.out.println("加密后的数据：\n");
//		for (byte i : encryptInfo)
//			System.out.print(i + " ");
////		System.out.println();
//
//		String decryptInfo = decryptByTea(encryptInfo);
//		System.out.print("解密后的数据：");
//		System.out.println(decryptInfo);
////		for (byte i : decryptInfo.getBytes())
////			System.out.print(i + " ");
////		System.out.println();
//
//	}

        // 加密
        public static byte[] encrypt(byte[] content, int offset, int[] key, int times) {// times为加密轮数

            int[] tempInt = byteToInt(content, offset);
            int y = tempInt[0], z = tempInt[1], sum = 0, i;
            int delta = 0x9e3779b9; // 这是算法标准给的值
            int a = key[0], b = key[1], c = key[2], d = key[3];

            for (i = 0; i < times; i++) {
                sum += delta;
                y += ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
                z += ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);

            }
            tempInt[0] = y;
            tempInt[1] = z;

            return intToByte(tempInt, 0);
        }

        // 解密
        public static byte[] decrypt(byte[] encryptContent, int offset, int[] key,
                                     int times) {
            int[] tempInt = byteToInt(encryptContent, offset);
            int y = tempInt[0], z = tempInt[1], sum = 0, i;
            int delta = 0x9e3779b9; // 这是算法标准给的值
            int a = key[0], b = key[1], c = key[2], d = key[3];
            if (times == 32)
                sum = 0xC6EF3720; /* delta << 5 */
            else if (times == 16)
                sum = 0xE3779B90; /* delta << 4 */
            else
                sum = delta * times;

            for (i = 0; i < times; i++) {
                z -= ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
                y -= ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
                sum -= delta;
            }
            tempInt[0] = y;
            tempInt[1] = z;

            return intToByte(tempInt, 0);
        }

        // byte[]型数据转成int[]型数据
        public static int[] byteToInt(byte[] content, int offset) {

            int[] result = new int[content.length >> 2];// 除以2的n次方 == 右移n位 即
            // content.length / 4 ==
            // content.length >> 2
            for (int i = 0, j = offset; j < content.length; i++, j += 4) {
                result[i] = transform(content[j + 3])
                        | transform(content[j + 2]) << 8
                        | transform(content[j + 1]) << 16 | (int) content[j] << 24;
            }
//            Log.i("ZYA","content:"+Arrays.toString(content));
            return result;
        }

       public static int[] byteToIntnew(byte[] content, int offset) {

           int[] result = new int[content.length >> 2];// 除以2的n次方 == 右移n位 即
           // content.length / 4 ==
           // content.length >> 2
           for (int i = 0, j = offset; j < content.length; i++, j += 4) {
               result[i] = transform(content[j + 3])
                       | transform(content[j + 2]) << 8
                       | transform(content[j + 1]) << 16 | (int) content[j] << 24;
           }
//            Log.i("ZYA","content:"+Arrays.toString(content));
           return result;
       }

        // int[]型数据转成byte[]型数据
        public static byte[] intToByte(int[] content, int offset) {
            byte[] result = new byte[content.length << 2];// 乘以2的n次方 == 左移n位 即
            // content.length * 4 ==
            // content.length << 2
            for (int i = 0, j = offset; j < result.length; i++, j += 4) {
                result[j + 3] = (byte) (content[i] & 0xff);
                result[j + 2] = (byte) ((content[i] >> 8) & 0xff);
                result[j + 1] = (byte) ((content[i] >> 16) & 0xff);
                result[j] = (byte) ((content[i] >> 24) & 0xff);
            }
            return result;
        }

        // 若某字节为负数则需将其转成无符号正数
        private static int transform(byte temp) {
            int tempInt = (int) temp;
            if (tempInt < 0) {
                tempInt += 256;
            }
            return tempInt;
        }

        public static byte[] encryptByTea(String info) {
            byte[] temp = info.getBytes();
            Log.i("ZYA", Arrays.toString(temp));
            int n = 8 - temp.length % 8;// 若temp的位数不足8的倍数,需要填充的位数
            byte[] encryptStr = new byte[temp.length + n];
            encryptStr[0] = (byte) n;
            System.arraycopy(temp, 0, encryptStr, n, temp.length);
            byte[] result = new byte[encryptStr.length];
            for (int offset = 0; offset < result.length; offset += 8) {
                byte[] tempEncrpt = encrypt(encryptStr, offset, KEY, 4);
                System.arraycopy(tempEncrpt, 0, result, offset, 8);
            }
            return result;
        }

        // 通过TEA算法解密信息
        public static String decryptByTea(byte[] secretInfo) {
            byte[] decryptStr = null;
            byte[] tempDecrypt = new byte[secretInfo.length];
            for (int offset = 0; offset < secretInfo.length; offset += 8) {
                decryptStr = decrypt(secretInfo, offset, KEY, 4);
                System.arraycopy(decryptStr, 0, tempDecrypt, offset, 8);
            }

            int n = tempDecrypt[0];
            return new String(tempDecrypt, n, decryptStr.length - n);
        }
    }

   public static class AES {
        static final String algorithmStr = "AES/ECB/PKCS5Padding";

        private static final Object TAG = "AES";

        static private KeyGenerator keyGen;

        static private Cipher cipher;

        static boolean isInited = false;

        private static  void init() {
            try {
                /**为指定算法生成一个 KeyGenerator 对象。
                 *此类提供（对称）密钥生成器的功能。
                 *密钥生成器是使用此类的某个 getInstance 类方法构造的。
                 *KeyGenerator 对象可重复使用，也就是说，在生成密钥后，
                 *可以重复使用同一 KeyGenerator 对象来生成进一步的密钥。
                 *生成密钥的方式有两种：与算法无关的方式，以及特定于算法的方式。
                 *两者之间的惟一不同是对象的初始化：
                 *与算法无关的初始化
                 *所有密钥生成器都具有密钥长度 和随机源 的概念。
                 *此 KeyGenerator 类中有一个 init 方法，它可采用这两个通用概念的参数。
                 *还有一个只带 keysize 参数的 init 方法，
                 *它使用具有最高优先级的提供程序的 SecureRandom 实现作为随机源
                 *（如果安装的提供程序都不提供 SecureRandom 实现，则使用系统提供的随机源）。
                 *此 KeyGenerator 类还提供一个只带随机源参数的 inti 方法。
                 *因为调用上述与算法无关的 init 方法时未指定其他参数，
                 *所以由提供程序决定如何处理将与每个密钥相关的特定于算法的参数（如果有）。
                 *特定于算法的初始化
                 *在已经存在特定于算法的参数集的情况下，
                 *有两个具有 AlgorithmParameterSpec 参数的 init 方法。
                 *其中一个方法还有一个 SecureRandom 参数，
                 *而另一个方法将已安装的高优先级提供程序的 SecureRandom 实现用作随机源
                 *（或者作为系统提供的随机源，如果安装的提供程序都不提供 SecureRandom 实现）。
                 *如果客户端没有显式地初始化 KeyGenerator（通过调用 init 方法），
                 *每个提供程序必须提供（和记录）默认初始化。
                 */
                keyGen = KeyGenerator.getInstance("AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            // 初始化此密钥生成器，使其具有确定的密钥长度。
            keyGen.init(128); //128位的AES加密
            try {
                // 生成一个实现指定转换的 Cipher 对象。
                cipher = Cipher.getInstance(algorithmStr);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }
            //标识已经初始化过了的字段
            isInited = true;
        }

        private static byte[] genKey() {
            if (!isInited) {
                init();
            }
            //首先 生成一个密钥(SecretKey),
            //然后,通过这个秘钥,返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null。
            return keyGen.generateKey().getEncoded();
        }

        private static byte[] encrypt(byte[] content, byte[] keyBytes) {
            byte[] encryptedText = null;
            if (!isInited) {
                init();
            }
            /**
             *类 SecretKeySpec
             *可以使用此类来根据一个字节数组构造一个 SecretKey，
             *而无须通过一个（基于 provider 的）SecretKeyFactory。
             *此类仅对能表示为一个字节数组并且没有任何与之相关联的钥参数的原始密钥有用
             *构造方法根据给定的字节数组构造一个密钥。
             *此构造方法不检查给定的字节数组是否指定了一个算法的密钥。
             */
            Key key = new SecretKeySpec(keyBytes, "AES");
            try {
                // 用密钥初始化此 cipher。
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            try {
                //按单部分操作加密或解密数据，或者结束一个多部分操作。(不知道神马意思)
                encryptedText = cipher.doFinal(content);
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return encryptedText;
        }

        private static byte[] encrypt(String content, String password) {
            try {
                byte[] keyStr = getKey(password);
                SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
                Cipher cipher = Cipher.getInstance(algorithmStr);//algorithmStr
                byte[] byteContent = content.getBytes("utf-8");
                cipher.init(Cipher.ENCRYPT_MODE, key);//   ʼ
                byte[] result = cipher.doFinal(byteContent);
                return result; //
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static byte[] decrypt(byte[] content, String password) {
            try {
                byte[] keyStr = getKey(password);
                SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
                Cipher cipher = Cipher.getInstance(algorithmStr);//algorithmStr
                cipher.init(Cipher.DECRYPT_MODE, key);//   ʼ
                byte[] result = cipher.doFinal(content);
                return result; //
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static byte[] getKey(String password) {
            byte[] rByte = null;
            if (password!=null) {
                rByte = password.getBytes();
            }else{
                rByte = new byte[24];
            }
            return rByte;
        }

        /**
         * 将二进制转换成16进制
         * @param buf
         * @return
         */
        public static String parseByte2HexStr(byte buf[]) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < buf.length; i++) {
//                Log.i("Aes","buf[i]=>>>>>>>>"+buf[i]);
                String hex = Integer.toHexString(buf[i] & 0xFF);
//                Log.i("Aes","hex=>>>>>>>>>>>"+hex.toUpperCase());
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        }

        /**
         * 将16进制转换为二进制
         * @param hexStr
         * @return
         */
        public static byte[] parseHexStr2Byte(String hexStr) {
            if (hexStr.length() < 1)
                return null;
            byte[] result = new byte[hexStr.length() / 2];
            for (int i = 0; i < hexStr.length() / 2; i++) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                        16);
                result[i] = (byte) (high * 16 + low);
            }
            return result;
        }

        //注意: 这里的password(秘钥必须是16位的)
        private static final String keyBytes = "3939393838380000";
//                "a2cdefgabcdefg15";

        /**
         *加密
         */
        public static String encode(String content){
            //加密之后的字节数组,转成16进制的字符串形式输出
            return parseByte2HexStr(encrypt(content, keyBytes));
        }

        /**
         *解密
         */
        public static String decode(String content){
            //解密之前,先将输入的字符串按照16进制转成二进制的字节数组,作为待解密的内容输入
            byte[] b = decrypt(parseHexStr2Byte(content), keyBytes);
            return new String(b);
        }

        //测试用例
        public static void test1(){
            String content = "123456123";

            String pStr = encode(content );
            Log.i("Aes", "加密前：" + content);
            Log.i("Aes", "加密后：" + pStr);
//            System.out.println("加密前："+content);
//            System.out.println("加密后:" + pStr);

            String postStr = decode(pStr);
            Log.i("Aes", "解密后：" + postStr);
//            System.out.println("解密后："+ postStr );
        }
    }

    public static String ToSpacePrint(String strg){
        String str = "";
        for(int i = 0;i<strg.length();i++){
            int temp = 0;
            if(i<2){
                str = str + strg.charAt(i)+"";
            }else{
                temp = i;
                while (temp>1){
                    temp = temp%2;
                }
                if(temp==0){
                    str = str +" ";
                }
                str = str + strg.charAt(i);
            }
        }
        return str;
    }

    public static String strToAsc(String str){
        char[] str_char = str.toCharArray();
        String nstr = "";
        for(int i=0;i<str_char.length;i++){
            nstr += (int)str_char[i]+" ";
        }
        return nstr;
    }

    public static String ascToStr(String str){
        String[] str_char = str.split(" ");
        String nstr = "";
        for (int i = 0; i < str_char.length; i++) {
            nstr = (char) Integer.parseInt(str_char[i])+"";
        }
        return nstr;
    }

    public static byte[] getAsciiByte(String str){
        byte[] temp = new byte[str.length()];

        for(int i = 0;i <str.length();i++){
//           temp[i] = (byte)Integer.parseInt(Integer.toHexString(Integer.parseInt(str.charAt(i)+""))+"");
        }
        return temp;
    }

}
