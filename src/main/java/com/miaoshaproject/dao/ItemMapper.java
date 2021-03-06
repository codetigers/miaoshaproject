package com.miaoshaproject.dao;

import com.miaoshaproject.bean.Item;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Tue Apr 21 22:58:18 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Tue Apr 21 22:58:18 CST 2020
     */
    int insert(Item record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Tue Apr 21 22:58:18 CST 2020
     */
    int insertSelective(Item record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Tue Apr 21 22:58:18 CST 2020
     */
    Item selectByPrimaryKey(Integer id);

    List<Item> listItem();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Tue Apr 21 22:58:18 CST 2020
     */
    int updateByPrimaryKeySelective(Item record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Tue Apr 21 22:58:18 CST 2020
     */
    int updateByPrimaryKey(Item record);

    int increaseSales(@Param("id") Integer id,@Param("amount")Integer amount);
}