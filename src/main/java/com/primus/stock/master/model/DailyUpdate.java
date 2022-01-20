package com.primus.stock.master.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name ="DAILY_UPDATE")
public class DailyUpdate {
    long id;
    String groupX ;
    Date updatedDate ;


    @Column(name  ="ID")
    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name  ="GROUPX")
    public String getGroupX() {
        return groupX;
    }

    public void setGroupX(String groupX) {
        this.groupX = groupX;
    }

    @Column(name  ="UPDATED_DATE")
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public DailyUpdate(String groupX, Date updatedDate) {
        this.groupX = groupX;
        this.updatedDate = updatedDate;
    }

    public DailyUpdate() {
    }
}
