package com.sky31.buy.second_hand.context.values;

/**
 * Created by Caspar on 2015/7/29.
 */
public class Constants {
    public final class Apis {
        public static final String API_URL = "http://buy.sky31.com/fleaapi";

        public static final String
                API_GOODS_LIST_GET = "http://buy.sky31.com/fleaapi/index.php/Admin/Goods/query?";
        public static final String
                API_GOODS_SOLDOUT_GET = "http://buy.sky31.com/fleaapi/index.php/Admin/Goods/soldOut?";
        public static final String
                API_GOODS_CLASSIFY_GET = "http://buy.sky31.com/fleaapi/index.php/Admin/Goods/getClassify";


        public static final String
                API_USER_LOGIN_POST = "http://buy.sky31.com/fleaapi/index.php/Admin/User/login";
        public static final String
                API_USER_CHANGE_INFO_POST = "http://buy.sky31.com/fleaapi/index.php/Admin/User/changeInfo";
        public static final String
                API_GOODS_APPINSERT_POST = "http://buy.sky31.com/fleaapi/index.php/Admin/Goods/appInsert";
        public static final String
                API_GOODS_TESTINSERT_POST = "http://buy.sky31.com/fleaapi/index.php/Admin/Goods/testInsert";


    }

    public final class Keys {
        //商品
        public static final String KEY_PICNAME = "picname";
        public static final String KEY_PICSRC= "picsrc";
        public static final String KEY_LIMITID= "limitID";

        public static final String KEY_TITLE= "title";
        public static final String KEY_DESCRIBE= "describe";
        public static final String KEY_PRICE= "price";
        public static final String KEY_TYPE= "type";
        public static final String KEY_INTERVAL= "interval";
        public static final String KEY_TRADING= "trading";
        public static final String KEY_SELLER= "seller";
        public static final String KEY_BARGAIN= "bargain";
        public static final String KEY_GID= "gid";

        //缓存
        public static final String KEY_CACHE_HOME_CHECK = "checkCacheHome";
        public static final String KEY_CACHE_HOME_FIRST_CONTENT = "CacheHomeFirstContent";
        public static final String KEY_CACHE_CLASSIFY_CHECK = "checkCacheClassify";
        public static final String KEY_CACHE_CLASSIFY_CONTENT = "CacheClassifyContent";
        public static final String KEY_CACHE_USERNAME = "CacheUserName";
        public static final String KEY_CACHE_PASSWORD = "CachePassWord";

        //用户
        public static final String KEY_USERNAME = "username";
        public static final String KEY_PASSWORD = "password";
        public static final String KEY_NICKNAME = "nickname";
        public static final String KEY_QQ = "qq";
        public static final String KEY_PHONE = "phone";
        public static final String KEY_UID = "uid";

    }

    public final class Values {
        public static final String VALUE_MIN_SIZE_PRE = "s_";
        public static final String VALUE_MIDDLE_SIZE_PRE = "m_";

        public static final String VALUE_SERVICE_NO_RESPONSE = "服务器未响应";

        public static final String VALUE_CLASSIFY = "{\"1\":\"\\u56fe\\u4e66\\u6559\\u6750\"," +
                "\"2\":\"\\u6570\\u7801\\u4ea7\\u54c1\"," +
                "\"3\":\"\\u4ee3\\u6b65\\u5de5\\u5177\"," +
                "\"4\":\"\\u8fd0\\u52a8\\u5668\\u6750\"," +
                "\"5\":\"\\u8863\\u7269\\u8863\\u5e3d\"," +
                "\"6\":\"\\u5bb6\\u7528\\u7535\\u5668\"," +
                "\"7\":\"\\u79df\\u8d41\"," +
                "\"8\":\"\\u5176\\u4ed6\"}";

    }
}
