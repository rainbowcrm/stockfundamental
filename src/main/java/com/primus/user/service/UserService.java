package com.primus.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.primus.common.AES;
import com.primus.common.CommonErrorCodes;
import com.primus.common.PrimusError;
import com.primus.user.dao.UserDAO;
import com.primus.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {


    @Autowired
    UserDAO userDAO;

    final String secretKey= "Dubai";

    public  void createUser(Map<String,Object> userData) throws PrimusError
    {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = objectMapper.convertValue(userData, User.class);
        User existUser =  userDAO.getByEmail(newUser.getEmailId());
        if(existUser != null) {
            throw new PrimusError(CommonErrorCodes.USER_ALREADY_EXISTS,"Email already in use. Please check your password and relogin");
        }else
        {
            String encryptedPWD = AES.encrypt(newUser.getPassword(),secretKey);
            newUser.setPassword(encryptedPWD);
            userDAO.update(newUser);
        }

    }

    public User getLogin(String userEmail,String password)
    {
        String encrPwd = AES.encrypt(password,secretKey);
        User existUser =  userDAO.getByEmailAndPassword(userEmail,encrPwd);
        return existUser;

    }





}
