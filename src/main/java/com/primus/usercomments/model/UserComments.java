package com.primus.usercomments.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="USER_COMMENTS")
public class UserComments {

    long id;

    String bseCode;

    String security;

    Boolean isPrediction;

    Date predDate ;

    Double predPrice;

    String comments;

    String screenName;

    String email;

    Date commentedDate ;

    String fullMessage ;


    @Column(name  ="ID")
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name  ="BSE_CODE")
    public String getBseCode() {
        return bseCode;
    }

    public void setBseCode(String bseCode) {
        this.bseCode = bseCode;
    }

    @Column(name  ="SECURITY_NAME")
    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    @Column(name  ="IS_PREDICTION")
    public Boolean getPrediction() {
        return isPrediction;
    }

    public void setPrediction(Boolean prediction) {
        isPrediction = prediction;
    }

    @Column(name  ="PRED_DATE")
    public Date getPredDate() {
        return predDate;
    }

    public void setPredDate(Date predDate) {
        this.predDate = predDate;
    }

    @Column(name  ="PRED_PRICE")
    public Double getPredPrice() {
        return predPrice;
    }

    public void setPredPrice(Double predPrice) {
        this.predPrice = predPrice;
    }

    @Column(name  ="COMMENTS")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Column(name  ="USER_SCR")
    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @Column(name  ="USER_EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name  ="COMMENTED_DATE")
    public Date getCommentedDate() {
        return commentedDate;
    }

    public void setCommentedDate(Date commentedDate) {
        this.commentedDate = commentedDate;
    }


}
