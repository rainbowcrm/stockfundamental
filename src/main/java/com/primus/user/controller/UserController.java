package com.primus.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.primus.common.BusinessContext;
import com.primus.common.PrimusError;
import com.primus.user.model.User;
import com.primus.user.model.UserPreferences;
import com.primus.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "https://localhost:20452", maxAge = 3600)
@RequestMapping("/stockapi/user")
@RestController
public class UserController {

    @Autowired
    UserService userService;


    @RequestMapping(value = "/getLoggedinUser", method = RequestMethod.GET)
    public ResponseEntity<Map> getLoggedInUser()
    {
        Map<String,Object> map = new HashMap<>() ;
        try {
            BusinessContext businessContext = BusinessContext.getBusinessContent();
            User selUser = businessContext.getUser();
            ObjectMapper objectMapper = new ObjectMapper();
            map = objectMapper.convertValue(selUser,Map.class);
            map.put("Result","Success");
            map.remove("friendsFamily");
            map.remove("password");

        }catch (Exception error)
        {
            map.put("Result","Failure");
            map.put("Error",error.getMessage());
        }
        ResponseEntity entity =  new ResponseEntity<Map>(map, HttpStatus.OK);
        return  entity;
    }

    @RequestMapping(value = "/sendPassword", method = RequestMethod.GET)
    public ResponseEntity<Map> sendPassword(@RequestParam String userEmail)
    {
        Map<String,Object> map = new HashMap<>() ;
        try {
            userService.sendPasswordReminder(userEmail);
            map.put("Result","Success");
        }catch (PrimusError error)
        {
            map.put("Result","Failure");
            map.put("Error",error.getMessage());
        }
        ResponseEntity entity =  new ResponseEntity<Map>(map, HttpStatus.OK);
        return  entity;
    }

    @RequestMapping(value = "/setOTP", method = RequestMethod.GET)
    public ResponseEntity<Map> createUser(@RequestParam String inputData)
    {
        Map<String,Object> map = new HashMap<>() ;
        try {
            userService.extractUserInfo(inputData);
            map.put("Result","Success");
        }catch (PrimusError error)
        {
            map.put("Result","Failure");
            map.put("Error",error.getMessage());
        }
        ResponseEntity entity =  new ResponseEntity<Map>(map, HttpStatus.OK);
        return  entity;
    }

    @RequestMapping(value = "/saveOTP", method = RequestMethod.GET)
    public ResponseEntity<Map> saveOTP(@RequestParam String phoneNumber)
    {
        userService.sendOTP(phoneNumber);
        Map<String,Object> map = new HashMap<>() ;
        map.put("Result","Success");
        ResponseEntity entity =  new ResponseEntity<Map>(map, HttpStatus.OK);
        return  entity;
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public ResponseEntity<Map> resetPassword(@RequestParam String updatedPWD)
    {
        BusinessContext businessContext  = BusinessContext.getBusinessContent();
        userService.updatePassword(businessContext.getUser(),updatedPWD);
        Map<String,Object> map = new HashMap<>() ;
        map.put("Result","Success");
        ResponseEntity entity =  new ResponseEntity<Map>(map, HttpStatus.OK);
        return  entity;
    }

    @RequestMapping(value = "/updateNames", method = RequestMethod.GET)
    public ResponseEntity<Map> resetPassword(@RequestParam String firstName,@RequestParam String lastName )
    {
        BusinessContext businessContext  = BusinessContext.getBusinessContent();
        userService.updateNames(businessContext.getUser(),firstName,lastName);
        Map<String,Object> map = new HashMap<>() ;
        map.put("Result","Success");
        ResponseEntity entity =  new ResponseEntity<Map>(map, HttpStatus.OK);
        return  entity;
    }

    @RequestMapping(value = "/getUserPref", method = RequestMethod.GET)
    public ResponseEntity<UserPreferences> getUserPreference()
    {
        BusinessContext businessContext  = BusinessContext.getBusinessContent();
        UserPreferences userPreferences = userService.getPreferences(businessContext);
        ResponseEntity entity =  new ResponseEntity<UserPreferences>(userPreferences, HttpStatus.OK);
        return  entity;

    }

    @RequestMapping(value = "/updateUserPref", method = RequestMethod.POST)
    public ResponseEntity<UserPreferences> updateUserPreference(@RequestBody UserPreferences userPreference)
    {
        BusinessContext businessContext  = BusinessContext.getBusinessContent();
        userService.updatePreferences(userPreference,businessContext);
        ResponseEntity entity =  new ResponseEntity<UserPreferences>(userPreference, HttpStatus.OK);
        return  entity;

    }



}
