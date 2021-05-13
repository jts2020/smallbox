package com.jts.smallbox.proxy;

import lombok.Data;

/**
 * @author jts
 */
@Data
public class User implements IUserOp{

    private String id;
    private String name;

    @Override
    public String getInfo() {
        return id+"_"+name;
    }
}
