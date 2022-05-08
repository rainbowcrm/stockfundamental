package com.primus.useractions.service;

import com.primus.common.BusinessContext;
import com.primus.useractions.model.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActionFacade {

    @Autowired
    UserActionService userActionService;

    public void saveUserAction(BusinessContext businessContext, String page, Object data)
    {
        UserActionThread userActionThread = new UserActionThread(userActionService,
                businessContext.getUserEmail(),page,data!=null?data.toString():"");
        userActionThread.start();

    }
}
