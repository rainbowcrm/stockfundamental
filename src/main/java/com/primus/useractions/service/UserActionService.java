package com.primus.useractions.service;

import com.primus.common.BusinessContext;
import com.primus.useractions.dao.UserActionDAO;
import com.primus.useractions.model.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActionService {

    @Autowired
    UserActionDAO userActionDAO ;

    public void save(String email, String page , String data)
    {
        UserAction userAction = new UserAction();
        userAction.setEmail(email);
        userAction.setPageAccessed(page);
        userAction.setSupportInfo(data);
        userAction.setAccessedTime(new java.util.Date());
        userActionDAO.update(userAction);

    }
}
