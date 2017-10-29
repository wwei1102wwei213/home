package com.zeyuan.kyq.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/8/9.
 *
 *
 * @author wwei
 */
public class FileUtils {

    public static String readTxtFile(String filePath){
        try {
            StringBuilder sb = new StringBuilder();
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);

                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    sb.append(lineTxt);
                }
                read.close();

            }else{
                System.out.println("找不到指定的文件");
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return null;
    }

}
