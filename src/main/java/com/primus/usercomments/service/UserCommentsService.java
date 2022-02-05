package com.primus.usercomments.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.primus.common.BusinessContext;
import com.primus.usercomments.dao.UserCommentsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.primus.usercomments.model.UserComments;
import java.util.List;
import java.util.Map;

@Service
public class UserCommentsService {

    @Autowired
    UserCommentsDAO userCommentsDAO;

    public UserComments createUserComments(BusinessContext context ,Map<String,Object> commentMap) {
        ObjectMapper objectMapper= new ObjectMapper();
        UserComments userComments  = objectMapper.convertValue(commentMap,UserComments.class);
        userComments.setEmail(context.getUserEmail());
        userComments.setScreenName(context.getUser().getScreenName());
        userComments.setCommentedDate(new java.util.Date());
        userCommentsDAO.update(userComments);
        return userComments;

    }


    public List<UserComments> getUserComments(String bseCode) {
        return  userCommentsDAO.getCommentsForStock(bseCode);
    }

}
