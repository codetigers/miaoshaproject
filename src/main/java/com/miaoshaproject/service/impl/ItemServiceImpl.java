package com.miaoshaproject.service.impl;

import com.miaoshaproject.bean.Item;
import com.miaoshaproject.bean.ItemStock;
import com.miaoshaproject.bean.Promo;
import com.miaoshaproject.dao.ItemMapper;
import com.miaoshaproject.dao.ItemStockMapper;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBussinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.PromoModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: miaoshaproject
 * @description:
 * @author: Mr.li
 * @create: 2020-04-22 10:30
 **/
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemStockMapper itemStockMapper;

    @Autowired
    private PromoService promoService;

    private Item convertItemFromItemModel(ItemModel itemModel){
        if(itemModel==null){
            return null;
        }
        Item item = new Item();
        BeanUtils.copyProperties(itemModel,item);
        //因为item的price是double，因此需要将price转化一下
        item.setPrice(itemModel.getPrice().doubleValue());
        return item;
    }

    private ItemStock convertItemStockFromItemMode(ItemModel itemModel){
        if(itemModel==null){
            return null;
        }
        ItemStock itemStock = new ItemStock();
        itemStock.setStock(itemModel.getStock());
        itemStock.setItemId(itemModel.getId());
        return itemStock;
    }


    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //校验入参
        ValidationResult result = validator.validate(itemModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //转化itemmodel->bean
        Item item =  this.convertItemFromItemModel(itemModel);
        //写入数据库
        try {
            itemMapper.insertSelective(item);
        }catch (Exception e){
            e.printStackTrace();
        }
        itemModel.setId(item.getId());
        ItemStock itemStock = this.convertItemStockFromItemMode(itemModel);
        itemStockMapper.insertSelective(itemStock);

        //返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<Item> itemList = itemMapper.listItem();
        List<ItemModel> itemModelList = itemList.stream().map(item -> {
            ItemStock itemStock = itemStockMapper.selectByItemId(item.getId());
            ItemModel itemModel = this.convertModelFromDataObject(item,itemStock);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        Item item = itemMapper.selectByPrimaryKey(id);
        if(item==null){
            return null;
        }
        //操作数据库存数量
        ItemStock itemStock = itemStockMapper.selectByItemId(item.getId());
        //将bean->model
        ItemModel itemModel = convertModelFromDataObject(item,itemStock);

        //获取活动商品信息
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if(promoModel!=null&&promoModel.getStatus().intValue()!=3){
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        //返回受影响的行数
        int affectedRow = itemStockMapper.decreaseStock(itemId,amount);//如果库存小于订购的数目，就不会执行sql语句，返回影响的条目数0
        if(affectedRow>0){
            //更新库存成功
            return true;
        }else{
            return false;
        }
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemMapper.increaseSales(itemId,amount);
    }

    private ItemModel convertModelFromDataObject(Item item,ItemStock itemStock){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(item,itemModel);
        itemModel.setPrice(new BigDecimal(item.getPrice()));
        itemModel.setStock(itemStock.getStock());
        return itemModel;
    }
}
