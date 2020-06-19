package com.miaoshaproject.dao;

import com.miaoshaproject.bean.UserPassword;
import org.springframework.stereotype.Component;

@Component
public interface UserPasswordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Apr 21 19:06:53 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Apr 21 19:06:53 CST 2020
     */
    int insert(UserPassword record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Apr 21 19:06:53 CST 2020
     */
    int insertSelective(UserPassword record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Apr 21 19:06:53 CST 2020
     */
    UserPassword selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Apr 21 19:06:53 CST 2020
     */
    int updateByPrimaryKeySelective(UserPassword record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Apr 21 19:06:53 CST 2020
     */
    int updateByPrimaryKey(UserPassword record);

    UserPassword selectByUserId(Integer id);
}