package com.primus.user.controller;

import com.primus.common.PrimusError;
import com.primus.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "https://localhost:20452", maxAge = 3600)
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    UserService userService;


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


}
