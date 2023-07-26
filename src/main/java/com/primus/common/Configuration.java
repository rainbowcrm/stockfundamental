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

    @Value("${spring.reports.folder}")
    String reportFolder;

    @Value("${textlocal.api.key}")
    String smsAPIKey;


    @Value("${chrome.driver}")
    String chromeDriver;

    public String getChromeDriver() {
        return chromeDriver;
    }

    public void setChromeDriver(String chromeDriver) {
        this.chromeDriver = chromeDriver;
    }

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

    public String getReportFolder() {
        return reportFolder;
    }

    public void setReportFolder(String reportFolder) {
        this.reportFolder = reportFolder;
    }

    public String getSmsAPIKey() {

        return smsAPIKey;
    }

    public void setSmsAPIKey(String smsAPIKey) {
        this.smsAPIKey = smsAPIKey;
    }
}
