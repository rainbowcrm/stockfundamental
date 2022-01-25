package com.primus.common;

import com.primus.user.model.User;
import com.primus.user.model.UserPreferences;
import com.primus.user.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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

    User user;

    UserPreferences userPreferences ;



    public static BusinessContext getBusinessContent()
    {
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BusinessContext businessContext= new BusinessContext();
        businessContext.setUserEmail(email);
        UserService userService = SpringContext.getBean(UserService.class);
        User user = userService.getUserByEmail(email);
        UserPreferences userPreferences = userService.getPreferences(email);
        businessContext.setUserPreferences(userPreferences);
        businessContext.setUser(user);
        return businessContext;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }
}
