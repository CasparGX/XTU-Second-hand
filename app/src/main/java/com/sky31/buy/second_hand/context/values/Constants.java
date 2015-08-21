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
                API_GOODS_CLASSIFY_GET = "http://buy.sky31.com/fleaapi/index.php/Admin/Goods/getClassify";


        public static final String
                API_USER_LOGIN_POST = "http://buy.sky31.com/fleaapi/index.php/Admin/User/login";
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
        public static final String KEY_TYPE= "type";
        public static final String KEY_TITLE= "title";

        //缓存
        public static final String KEY_CACHE_HOME_CHECK = "checkCacheHome";
        public static final String KEY_CACHE_HOME_FIRST_CONTENT = "CacheHomeFirstContent";
        public static final String KEY_CACHE_CLASSIFY_CHECK = "checkCacheClassify";
        public static final String KEY_CACHE_CLASSIFY_CONTENT = "CacheClassifyContent";

        //用户
        public static final String KEY_USERNAME = "username";
        public static final String KEY_PASSWORD = "password";

    }

    public final class Values {
        public static final String VALUE_MIN_SIZE_PRE = "s_";
        public static final String VALUE_MIDDLE_SIZE_PRE = "m_";

    }
}
