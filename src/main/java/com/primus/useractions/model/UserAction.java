package com.primus.useractions.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name ="user_actions")
public class UserAction {

    long id;

    String email;

    String pageAccessed;

    String supportInfo ;

    Date accessedTime;

    @Column(name  ="ID")
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name  ="EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name  ="PAGE_ACCESSED")
    public String getPageAccessed() {
        return pageAccessed;
    }

    public void setPageAccessed(String pageAccessed) {
        this.pageAccessed = pageAccessed;
    }

    @Column(name  ="SUPPORT_INFO")
    public String getSupportInfo() {
        return supportInfo;
    }

    public void setSupportInfo(String supportInfo) {
        this.supportInfo = supportInfo;
    }

    @Column(name  ="ACCESSED_TIME")
    public Date getAccessedTime() {
        return accessedTime;
    }

    public void setAccessedTime(Date accessedTime) {
        this.accessedTime = accessedTime;
    }
}
