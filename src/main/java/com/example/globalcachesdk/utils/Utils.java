package com.example.globalcachesdk.utils;

import com.example.globalcachesdk.ExecutePrivilege;
import com.example.globalcachesdk.exception.GlobalCacheSDKException;

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
            case USER:
                return "globalcachesdk";
            default:
                throw new GlobalCacheSDKException("未知的用户权限");
        }
    }
}
