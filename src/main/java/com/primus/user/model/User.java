package com.primus.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="USERS")
public class User {

    String emailId;
    String firstName;
    String lastName;
    String phoneNumber;
    Boolean verified;
    Boolean friendsFamily;
    String password;


    @Column(name  ="EMAIL")
    @Id
    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Column(name  ="FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name  ="LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name  ="MOBILE_NO")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Column(name  ="VERIFIED")
    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @Column(name  ="FRIENDS_FAMILY")
    public Boolean getFriendsFamily() {
        return friendsFamily;
    }

    public void setFriendsFamily(Boolean friendsFamily) {
        this.friendsFamily = friendsFamily;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name  ="U_PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
