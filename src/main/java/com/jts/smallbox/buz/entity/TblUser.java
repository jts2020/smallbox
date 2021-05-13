package com.jts.smallbox.buz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author jts
 */
@Data
@TableName("TBL_USER")
public class TblUser {
    @TableId
    private Long id;
    @TableField("name")
    private String name;
    private Integer age;
    private String email;
    /**
     * 使用 @TableField(exist = false) ，表示该字段在数据库中不存在 ，所以不会插入数据库中
     * 使用 transient 、 static 修饰属性也不会插入数据库中
     */
    @TableField(exist = false)
    private String tmp;
}
