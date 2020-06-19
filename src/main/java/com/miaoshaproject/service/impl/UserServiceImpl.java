package com.miaoshaproject.service.impl;

import com.miaoshaproject.bean.UserInfo;
import com.miaoshaproject.bean.UserPassword;
import com.miaoshaproject.dao.UserInfoMapper;
import com.miaoshaproject.dao.UserPasswordMapper;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBussinessError;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

/**
 * @program: miaoshaproject
 * @description:
 * @author: Mr.li
 * @create: 2020-04-20 10:47
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserPasswordMapper userPasswordMapper;
    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        if(userInfo==null){
            return null;
        }
        //通过用户id获取用户加密的密码
        UserPassword userPassword = userPasswordMapper.selectByUserId(userInfo.getId());
        return convertFromDataObject(userInfo,userPassword);
    }

    @Override
    @Transactional//事务，确保执行全部
    public void register(UserModel userModel) throws BusinessException {
        if(userModel==null){
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR);
        }
//        if(StringUtils.isEmpty(userModel.getName())
//        ||userModel.getAge()==null
//        ||userModel.getGender()==null
//        ||StringUtils.isEmpty(userModel.getTelephone())
//        ||StringUtils.isEmpty(userModel.getRegisterMode())){
//            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR);
//        }
        ValidationResult result = validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        UserInfo userInfo = convertFromModel(userModel);
        try {//手机号相同时，会报错
            userInfoMapper.insertSelective(userInfo);//使用这个而不是用insert是因为如果字段为null，就不会覆盖数据库中的值，这种方法在update方法中十分有效
            //思考：在数据库中一般尽量规定字段默认为not null，这样可以要求此字段必须有值，但是当用户需要用唯一索引确定一个值时，如不要求用户一定要有手机号，如果指定为not null,那么唯一索引就无法加上去了，
            //如果创建唯一索引，字段中的多个空值会认定为重复值，不能创建唯一索引。如果为多个Null就没事，因此如果字段中没有值，就将插入的值设定为Null
        }catch (DuplicateKeyException e){
            throw new BusinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,"手机号已被注册！");
        }
        userModel.setId(userInfo.getId());   //要将mapper层的xml定义中设置自增id不然读出的值为0不会增加，useGeneratedKeys="true" keyProperty="id
        UserPassword userPassword = convertPasswordFromModel(userModel);
        userPasswordMapper.insertSelective(userPassword);
        return;
    }

    @Override
    public UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException {
        //通过用户的手机获取用户信息
        UserInfo userInfo = userInfoMapper.selectByTelephone(telephone);
        if(userInfo==null){
            throw new BusinessException(EmBussinessError.USER_LOGIN_FAIL);
        }
        UserPassword userPassword = userPasswordMapper.selectByUserId(userInfo.getId());
        UserModel userModel = convertFromDataObject(userInfo,userPassword);
        //对比用户信息内加密的密码是否和传输过来的密码相匹配
        if(!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw new BusinessException(EmBussinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    private UserPassword convertPasswordFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserPassword userPassword = new UserPassword();
        userPassword.setEncrptPassword(userModel.getEncrptPassword());
        userPassword.setUserId(userModel.getId());
        return userPassword;
    }


    private UserInfo convertFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userModel,userInfo);
        return userInfo;
    }

    private UserModel convertFromDataObject(UserInfo userInfo, UserPassword userPassword){
        if(userInfo==null){
            return null;
        }
        UserModel userModel=new UserModel();
        BeanUtils.copyProperties(userInfo,userModel);
        if(userModel!=null){
            userModel.setEncrptPassword(userPassword.getEncrptPassword());
        }
        return userModel;
    }
}
