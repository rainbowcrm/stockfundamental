package com.primus.common;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class BusinessContext {

    String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }



    public static BusinessContext getBusinessContent()
    {
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BusinessContext businessContext= new BusinessContext();
        businessContext.setUserEmail(email);
        return businessContext;
    }
}
