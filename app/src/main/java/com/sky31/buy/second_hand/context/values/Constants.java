package com.sky31.buy.second_hand.context.values;

import java.security.PublicKey;

/**
 * Created by Caspar on 2015/7/29.
 */
public class Constants {
    public final class Apis {
        public static final String
                API_GOODS_LIST_GET = "http://buy.sky31.com/fleaapi/index.php/Admin/Goods/query?";
        public static final String API_URL = "http://buy.sky31.com/fleaapi";


    }

    public final class Keys {
        public static final String KEY_PICNAME = "picname";
        public static final String KEY_PICSRC= "picsrc";
        public static final String KEY_LIMITID= "limitID";
        public static final String KEY_TYPE= "type";
        public static final String KEY_TITLE= "title";

        public static final String KEY_CACHE_HOME_CHECK = "checkCacheHome";
        public static final String KEY_CACHE_HOME_FIRST_CONTENT = "CacheHomeFirstContent";
        public static final String KEY_CACHE_SEARCH_CHECK = "checkCacheSearch";

    }

    public final class Values {
        public static final String VALUE_MIN_SIZE_PRE = "s_";
        public static final String VALUE_MIDDLE_SIZE_PRE = "m_";

    }
}
