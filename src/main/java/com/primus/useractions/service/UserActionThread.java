package com.primus.useractions.service;


public class UserActionThread  extends Thread{

    UserActionService userActionService;
    String email;
    String page;
    String support;

    public UserActionThread(UserActionService uaService, String email, String page, String support) {
        userActionService = uaService;
        this.email = email;
        this.page  = page ;
        this.support = support;

    }

    public void run()
    {
        userActionService.save(email,page,support);
    }

}
