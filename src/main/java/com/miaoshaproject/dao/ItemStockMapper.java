package com.miaoshaproject.dao;

import com.miaoshaproject.bean.ItemStock;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface ItemStockMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Apr 21 22:58:18 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Apr 21 22:58:18 CST 2020
     */
    int insert(ItemStock record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Apr 21 22:58:18 CST 2020
     */
    int insertSelective(ItemStock record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Apr 21 22:58:18 CST 2020
     */
    ItemStock selectByPrimaryKey(Integer id);

    ItemStock selectByItemId(Integer itemId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Apr 21 22:58:18 CST 2020
     */
    int updateByPrimaryKeySelective(ItemStock record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Apr 21 22:58:18 CST 2020
     */
    int updateByPrimaryKey(ItemStock record);

    int decreaseStock(@Param("itemId") Integer itemId,@Param("amount") Integer amount);
}