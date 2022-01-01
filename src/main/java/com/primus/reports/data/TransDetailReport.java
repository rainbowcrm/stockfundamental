package com.primus.reports.data;

import java.util.*;

public class TransDetailReport  extends  TransReportData{

    List<TransLine> transLineList ;
    public TransDetailReport(String security, String industry, String group, String marketCapGroup, String sector) {
        super(security, industry, group, marketCapGroup, sector);
    }

    public TransDetailReport() {
        super();
    }

    public List<TransLine> getTransLineList() {
        return transLineList;
    }

    public void setTransLineList(List<TransLine> transLineList) {
        this.transLineList = transLineList;
    }

    public void addTransLine(TransLine transLine) {
        if (transLineList == null)
            transLineList = new ArrayList<>();
        transLineList.add(transLine);

    }
}
