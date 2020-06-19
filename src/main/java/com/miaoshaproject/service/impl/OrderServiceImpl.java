package com.miaoshaproject.service.impl;

import com.miaoshaproject.bean.OrderInfo;
import com.miaoshaproject.bean.SequenceInfo;
import com.miaoshaproject.dao.OrderInfoMapper;
import com.miaoshaproject.dao.SequenceInfoMapper;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBussinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;

/**
 * @program: miaoshaproject
 * @description:
 * @author: Mr.li
 * @create: 2020-04-22 19:02
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private SequenceInfoMapper sequenceInfoMapper;


    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId,Integer promoId, Integer amount) throws BusinessException {
        //1.校验下单状态，下单的商品是否存在，用户是否合法，购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if(itemModel==null){
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"商品信息不存在");
        }
        UserModel userModel = userService.getUserById(userId);
        if(userModel==null){
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"用户信息不存在");
        }
        if(amount<=0||amount>99){
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"数量信息不正确");
        }

        //校验活动信息
        if(promoId!=null){
            //1.校验对应活动是否存在这个对应商品
            if(promoId.intValue()!=itemModel.getPromoModel().getId()){
                throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"活动信息不正确");
                //2.校验活动是否在进行中
            }else if(itemModel.getPromoModel().getStatus()!=2){
                throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"活动还未开始");
            }
        }

        //2.落单减库存
        boolean result = itemService.decreaseStock(itemId,amount);
        if(!result){
            throw new BusinessException(EmBussinessError.STOCK_NOT_ENOUGH);
        }
        //3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        if(promoId!=null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else {
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setPromoId(promoId);
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));


        //生成交易流水号
        try {
            orderModel.setId(generateOrderNo());
        }catch (Exception e){
            e.printStackTrace();
        }
        OrderInfo orderInfo = convertfromOrderMode(orderModel);
        try {
            orderInfoMapper.insertSelective(orderInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
//4.返回前端
        //加上商品的销量
        itemService.increaseSales(itemId,amount);
        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)//开启新的事务将前面生成的提交掉，不影响当前回滚事务生成的单号，直接跳过
    public String generateOrderNo(){
        //订单号有16位
        StringBuilder stringBuilder = new StringBuilder();
        //前8位为时间信息，年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);
        //中间6位为自增序列
        //获取当前sequence
        int sequence = 0;
        SequenceInfo sequenceInfo = sequenceInfoMapper.getSequenceByName("order_info");

        sequence = sequenceInfo.getCurrentValue();

        sequenceInfo.setCurrentValue(sequenceInfo.getCurrentValue()+sequenceInfo.getStep());
        sequenceInfoMapper.updateByPrimaryKeySelective(sequenceInfo);
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0; i < 6-sequenceStr.length(); i++) {//不足六位的填充
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);
        //最后2位为分库分表
        stringBuilder.append("00");
        return stringBuilder.toString();
    }

    private OrderInfo convertfromOrderMode(OrderModel orderModel){
        if(orderModel==null){
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderModel,orderInfo);
        orderInfo.setItemPrice(orderModel.getItemPrice().doubleValue());//因为前面的使用BigDecimal来修饰数据，然后mapper层使用的是Double来进行映射因此要将数据进行转化
        orderInfo.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderInfo;
    }
}
