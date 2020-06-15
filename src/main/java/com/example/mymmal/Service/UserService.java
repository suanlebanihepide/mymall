package com.example.mymmal.Service;

import com.example.mymmal.Service.model.UserModel;
import com.example.mymmal.config.error.BusinessException;

public interface UserService {

    UserModel getUserById(int id );

    void register(UserModel userModel) throws BusinessException;
    UserModel login(String telephone ,String password) throws BusinessException;

}
