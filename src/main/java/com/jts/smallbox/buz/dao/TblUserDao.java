package com.jts.smallbox.buz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jts.smallbox.buz.entity.TblUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jts
 */
public interface TblUserDao extends BaseMapper<TblUser> {
    /**
     * query all tblUser info
     * @return List<TblUser> all tblUser info
     */
    @Select("SELECT ID,NAME,AGE,EMAIL FROM TBL_USER ")
    List<TblUser> findAll();

    /**
     * query a tblUser info by id
     * @param id tblUser's id
     * @return TblUser return a tblUser info
     */
    @Select("SELECT ID,NAME,AGE,EMAIL FROM TBL_USER WHERE id = #{id}")
    TblUser getById(int id);

    /**
     * insert a new
     * @param tblUser tblUser info
     * @return int be added count
     */
    @Insert("INSERT INTO TBL_USER(ID,NAME,AGE,EMAIL) VALUES(#{id},#{name},#{age},#{email})")
    int insertOne(TblUser tblUser);
}
