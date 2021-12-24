package com.primus.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${spring.emailHost}")
    String emailHost;

    @Value("${spring.emailUser}")
    String emailUser;

    @Value("${spring.emailPassword}")
    String emailPassword;

    public String getEmailHost() {
        return emailHost;
    }

    public void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }
}
