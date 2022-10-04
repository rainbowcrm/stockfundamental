package com.primus.stock.master.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="DIVIDENT_HISTORY")
public class DividentHistory {


    Long id;



    String bseCode;





    Date exDate;


    Double divident;


    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name  ="BSE_CODE")
    public String getBseCode() {
        return bseCode;
    }

    public void setBseCode(String bseCode) {
        this.bseCode = bseCode;
    }


    @Column(name  ="EX_DATE")
    public Date getExDate() {
        return exDate;
    }

    public void setExDate(Date exDate) {
        this.exDate = exDate;
    }

    @Column(name  ="DIVIDENT")
    public Double getDivident() {
        return divident;
    }

    public void setDivident(Double divident) {
        this.divident = divident;
    }
}
