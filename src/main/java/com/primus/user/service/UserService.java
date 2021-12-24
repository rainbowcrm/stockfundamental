package com.primus.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.primus.common.AES;
import com.primus.common.CommonErrorCodes;
import com.primus.common.PrimusError;
import com.primus.user.dao.UserDAO;
import com.primus.user.dao.UserOTPDAO;
import com.primus.user.model.User;
import com.primus.user.model.UserOTP;
import com.primus.utils.EmailService;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class UserService {


    @Autowired
    UserDAO userDAO;

    @Autowired
    UserOTPDAO userOTPDAO ;

    final String secretKey= "Dubai";

    @Autowired
    EmailService emailService ;

    public  void extractUserInfo(String encodedStr) throws PrimusError
    {
        try {
            byte[] byteArray = Base64.decodeBase64(encodedStr);
            String origValue = new String(byteArray);
            System.out.println(origValue);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> userIP = objectMapper.readValue(origValue, Map.class);
            String otp = userIP.get("otp");
            userIP.remove("otp");
            createUser(userIP, otp);
        }catch (IOException ex){
            throw new PrimusError(CommonErrorCodes.INTERNAL_ERROR,"Input error");
        }
    }

    public  void sendPasswordReminder(String userEmail) throws PrimusError
    {

        User existUser =  userDAO.getByEmail(userEmail);
        if(existUser == null) {
            throw new PrimusError(CommonErrorCodes.USER_NOT_EXIST,"Email not exist");
        }else
        {
            String basePassword = AES.decrypt(existUser.getPassword(),secretKey);
            emailService.sendPasswordEmail(existUser.getEmailId(),basePassword);
        }

    }

    public  void createUser(Map<String,String> userData,String otp) throws PrimusError
    {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = objectMapper.convertValue(userData, User.class);
        User existUser =  userDAO.getByEmail(newUser.getEmailId());
        if(existUser != null) {
            throw new PrimusError(CommonErrorCodes.USER_ALREADY_EXISTS,"Email already in use. Please check your password and relogin");
        }else
        {
            UserOTP userOTP = userOTPDAO.getByMobile(newUser.getPhoneNumber());
            if (!userOTP.getOtp().equalsIgnoreCase(otp)) {
                throw new PrimusError(CommonErrorCodes.OTP_WRONG,"OTP not matching, please retry!!");
            }
            newUser.setVerified(true);
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

    public void sendOTP(String phone)
    {
        UserOTP userOTP = new UserOTP();
        userOTP.setOtp("9091");
        userOTP.setPhoneNumber(phone);
        userOTPDAO.update(userOTP);
    }







}
