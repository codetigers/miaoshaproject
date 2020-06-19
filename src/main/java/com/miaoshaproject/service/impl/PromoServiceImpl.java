package com.miaoshaproject.service.impl;

import com.miaoshaproject.bean.Promo;
import com.miaoshaproject.dao.PromoMapper;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @program: miaoshaproject
 * @description:
 * @author: Mr.li
 * @create: 2020-04-23 16:21
 **/
@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoMapper promoMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        //获取对应商品的秒杀活动信息
        Promo promo = promoMapper.selectByItemId(itemId);

        //bean->model
        PromoModel promoModel = convertFromDataObject(promo);
        if(promoModel==null){
            return null;
        }
        //判断当前时间是否秒杀活动即将开始，正在进行
        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else{
            promoModel.setStatus(2);
        }
        return promoModel;
    }


    private PromoModel convertFromDataObject(Promo promo){
        if(promo==null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promo,promoModel);
        promoModel.setPromoItemPrice(new BigDecimal((promo.getPromoItemPrice())));
        promoModel.setStartDate(new DateTime(promo.getStartDate()));
        promoModel.setEndDate(new DateTime(promo.getEndDate()));
        return promoModel;
    }
}
