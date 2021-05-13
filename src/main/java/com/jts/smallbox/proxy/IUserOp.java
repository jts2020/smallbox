package com.jts.smallbox.proxy;

/**
 * @author jts
 */
public interface IUserOp {
    /**
     * get user base info
     * @return String user base info
     */
    default String getInfo(){
        return "";
    }
}
