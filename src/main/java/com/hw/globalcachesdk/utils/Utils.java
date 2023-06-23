package com.hw.globalcachesdk.utils;

import com.hw.globalcachesdk.ExecutePrivilege;
import com.hw.globalcachesdk.exception.GlobalCacheSDKException;

import java.io.*;

/**
 * 常用工具类
 * @author 章睿彬
 */
public class Utils {
    /**
     * 根据枚举类型获得用户名
     *
     * @param executePrivilege 执行权限枚举类型
     * @return 用户名
     * @throws GlobalCacheSDKException 当枚举类型不存在时抛出此异常
     */
    public static String enumExecutePrivilegeName(ExecutePrivilege executePrivilege) throws GlobalCacheSDKException {
        switch (executePrivilege) {
            case ROOT:
                return "root";
            case GLOBAL_CACHE_OP:
                return "globalcacheop";
            default:
                throw new GlobalCacheSDKException("unknown execute privilege");
        }
    }

    /**
     * 将以下划线命名的类枚举类型名称反转为完整类路径
     *
     * @param prefix 类前缀
     * @param enumName 枚举名称
     * @return 完整类路径
     */
    public static String enumFullClassPath(String prefix, String enumName) {
        StringBuilder stringBuilder = new StringBuilder(prefix);
        boolean isUpper = true;
        for (int i = 0; i < enumName.length(); ++i) {
            Character c = enumName.charAt(i);
            if (isUpper) {
                stringBuilder.append(c);
                isUpper = false;
            } else {
                if (c == '_') {
                    isUpper = true;
                } else {
                    stringBuilder.append(Character.toLowerCase(c));
                }
            }
        }

        return stringBuilder.toString();
    }

    public static void copyFile(String src, String dst) throws GlobalCacheSDKException {
        File sourceFile = new File(src);
        File destFile = new File(dst);

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFile);
            FileOutputStream outputStream = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new GlobalCacheSDKException("file not found", e);
        } catch (IOException e) {
            throw new GlobalCacheSDKException("file write failed!", e);
        }
    }
}
