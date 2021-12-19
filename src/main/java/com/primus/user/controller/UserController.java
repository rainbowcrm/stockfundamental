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

@CrossOrigin(origins = "http://localhost:20452", maxAge = 3600)
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    UserService userService;


    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public ResponseEntity<Map> createUser(@RequestBody Map<String,Object> userData)
    {
        Map<String,Object> map = new HashMap<>() ;
        try {
            userService.createUser(userData);
        }catch (PrimusError error)
        {
            map.put("Result","Failure");
            map.put("Error",error.getMessage());
        }
        ResponseEntity entity =  new ResponseEntity<Map>(map, HttpStatus.OK);
        return  entity;
    }

}
