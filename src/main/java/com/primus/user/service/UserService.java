package com.primus.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.primus.common.AES;
import com.primus.common.BusinessContext;
import com.primus.common.CommonErrorCodes;
import com.primus.common.PrimusError;
import com.primus.user.dao.UserDAO;
import com.primus.user.dao.UserOTPDAO;
import com.primus.user.dao.UserPreferencesDAO;
import com.primus.user.model.User;
import com.primus.user.model.UserOTP;
import com.primus.user.model.UserPreferences;
import com.primus.utils.EmailService;
import com.primus.utils.SMSService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class UserService {


    @Autowired
    UserDAO userDAO;

    @Autowired
    UserOTPDAO userOTPDAO ;

    @Autowired
    UserPreferencesDAO userPreferencesDAO;

    final String secretKey= "Dubai";

    @Autowired
    EmailService emailService ;

    @Autowired
    SMSService smsService;

    public void updatePreferences(UserPreferences userPreferences, BusinessContext businessContext)
    {
        userPreferences.setEmailId(businessContext.getUserEmail());
        userPreferencesDAO.update(userPreferences);
    }

    public UserPreferences getPreferences(BusinessContext businessContext)
    {
        UserPreferences userPreferences =  userPreferencesDAO.getByEmail(businessContext.getUserEmail());
        if( userPreferences == null) {
            userPreferences = getDefaultValue(businessContext.getUserEmail());
        }
        return  userPreferences ;
    }

    private UserPreferences getDefaultValue(String email)
    {
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setEmailId(email);
        userPreferences.setDashboardDays(30);
        userPreferences.setValidationAlgo("Default");
        userPreferences.setLandingPage("Default");
        userPreferences.setTechDays(180);
        return userPreferences;
    }
    public UserPreferences getPreferences(String  email)
    {
        UserPreferences userPreferences =  userPreferencesDAO.getByEmail(email);
        if( userPreferences == null) {
            userPreferences = getDefaultValue(email);
        }
        return  userPreferences ;
    }

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

    public User getUserByEmail(String email)
    {
        return userDAO.getByEmail(email);
    }

    public boolean isEmailPresent(String email)
    {
        return userDAO.getByEmail(email)!=null?true:false;
    }

    public boolean isPhonePresent(String phone)
    {
        return userDAO.getByPhone(phone)!=null?true:false;
    }

    public void sendOTP(String phone)
    {
        UserOTP userOTP = new UserOTP();
        String otp = RandomStringUtils.randomNumeric(4);
        userOTP.setOtp("9091");
        userOTP.setPhoneNumber(phone);
        userOTPDAO.update(userOTP);
        //smsService.sendSMS(phone,otp);
    }

    public void updatePassword(User user,String encodedPwd)
    {

        byte[] byteArray = Base64.decodeBase64(encodedPwd);
        String newPassword = new String(byteArray);
        String encrPwd = AES.encrypt(newPassword,secretKey);
        user.setPassword(encrPwd);
        userDAO.update(user);

    }


    public void updateNames(User user,String firstName, String lastName)
    {

        user.setFirstName(firstName);
        user.setLastName(lastName);
        userDAO.update(user);

    }








}
