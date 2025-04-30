package com.faceit.usermanagementtool.util;

import org.mindrot.jbcrypt.BCrypt;

public class Util {

    public static String cryptSensibleData(String secret){
        return BCrypt.hashpw(secret, BCrypt.gensalt());
    }

    public class GrammarSign{
        /**
         * The Constant COLON.
         */
        public static final String COLON = ":";

        /**
         * The Constant SLASH.
         */
        public static final String SLASH = "/";
    }
}
