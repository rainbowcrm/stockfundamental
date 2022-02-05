package com.primus.usercomments.controller;

import com.primus.common.BusinessContext;
import com.primus.ui.model.StockCompleteData;
import com.primus.usercomments.model.UserComments;
import com.primus.usercomments.service.UserCommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:20452", maxAge = 3600)
@RequestMapping("/stockapi/usercomments")
public class UserCommentsController {

    @Autowired
    UserCommentsService userCommentsService;

    @RequestMapping(value = "/getUserComments", method = RequestMethod.GET)
    public ResponseEntity<List<UserComments>> getUserComments(@RequestParam String bseCode)
    {
        List<UserComments> comments =  userCommentsService.getUserComments(bseCode);
        ResponseEntity entity =  new ResponseEntity<List<UserComments>>(comments, HttpStatus.OK);
        return  entity;
    }


    @RequestMapping(value = "/saveUserComments", method = RequestMethod.POST)
    public ResponseEntity<UserComments> getUserComments(@RequestBody Map<String,Object> useComment){
        BusinessContext businessContext= BusinessContext.getBusinessContent();
        UserComments curComment = userCommentsService.createUserComments(businessContext,useComment);
        ResponseEntity entity =  new ResponseEntity<UserComments>(curComment, HttpStatus.OK);
        return  entity;
    }


}
