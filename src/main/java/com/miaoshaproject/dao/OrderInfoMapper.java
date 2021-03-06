package com.miaoshaproject.dao;

import com.miaoshaproject.bean.OrderInfo;
import org.springframework.stereotype.Component;

@Component
public interface OrderInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Wed Apr 22 18:57:17 CST 2020
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Wed Apr 22 18:57:17 CST 2020
     */
    int insert(OrderInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Wed Apr 22 18:57:17 CST 2020
     */
    int insertSelective(OrderInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Wed Apr 22 18:57:17 CST 2020
     */
    OrderInfo selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Wed Apr 22 18:57:17 CST 2020
     */
    int updateByPrimaryKeySelective(OrderInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Wed Apr 22 18:57:17 CST 2020
     */
    int updateByPrimaryKey(OrderInfo record);
}