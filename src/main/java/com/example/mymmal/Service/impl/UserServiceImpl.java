/*
 * @Author: shenzheng
 * @Date: 2020/6/15 23:37
 */

package com.example.mymmal.Service.impl;

import com.alibaba.druid.util.StringUtils;
import com.example.mymmal.Service.UserService;
import com.example.mymmal.Service.model.UserModel;
import com.example.mymmal.config.error.BusinessException;
import com.example.mymmal.config.error.EmBusinessError;
import com.example.mymmal.dao.UserDOMapper;
import com.example.mymmal.dao.UserPasswordDOMapper;
import com.example.mymmal.dataobject.UserDO;
import com.example.mymmal.dataobject.UserPasswordDO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public UserModel getUserById(int id) {
        UserModel userModel = (UserModel) redisTemplate.opsForValue().get("user_validate_" + id);
        if (userModel == null) {
            userModel = this.getUserById(id);
            redisTemplate.opsForValue().set("user_validate_" + id, userModel);
            redisTemplate.expire("user_validate_" + id, 10, TimeUnit.MINUTES);
        }
        return userModel;
    }

    @Override
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (StringUtils.isEmpty(userModel.getName()) ||
                userModel.getAge() == null || userModel.getGender() == null
                || StringUtils.isEmpty(userModel.getTelphone())) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        UserDO userDO = convertFromModel(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.TELPHONE_ERROR);
        }
        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    @Override
    public UserModel login(String telephone, String password) throws BusinessException {

        UserDO userDO = userDOMapper.selectByTelphone(telephone);
        if (userDO == null) {
            throw new BusinessException(EmBusinessError.LOGIN_ERROR);
        }

        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO, userPasswordDO);

        if (!StringUtils.equals(password, userModel.getEncrptPassword())) {
            throw new BusinessException((EmBusinessError.LOGIN_ERROR));
        }

        return userModel;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel) {

        if (userModel == null) return null;
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }

    private UserDO convertFromModel(UserModel userModel) {
        if (userModel == null) return null;
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO) {
        if (userDO == null || userPasswordDO == null) return null;
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);
        userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        return userModel;
    }
}
