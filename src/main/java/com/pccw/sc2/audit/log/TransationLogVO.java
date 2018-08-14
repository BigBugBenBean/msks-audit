package com.pccw.sc2.audit.log;

import com.alibaba.fastjson.JSON;

public class TransationLogVO {
//SELECT KSKID, DTACTION, ACTIONTYPE, "RESULT", MCDNO, DTUAL, DTLSTUPD, LSTUPID, REMK
//FROM CSLCORE.WKSKTXLG;

    private String kskid;
    private String actiontype;
    private String dtaction;
    private String mcdno;
    private String result;
    private String dtual;
    private String dtlstupd;
    private String lstupid;
    private String remk;

    public String getKskid() {
        return kskid;
    }

    public void setKskid(String kskid) {
        this.kskid = kskid;
    }

    public String getActiontype() {
        return actiontype;
    }

    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }

    public String getDtaction() {
        return dtaction;
    }

    public void setDtaction(String dtaction) {
        this.dtaction = dtaction;
    }

    public String getMcdno() {
        return mcdno;
    }

    public void setMcdno(String mcdno) {
        this.mcdno = mcdno;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDtual() {
        return dtual;
    }

    public void setDtual(String dtual) {
        this.dtual = dtual;
    }

    public String getDtlstupd() {
        return dtlstupd;
    }

    public void setDtlstupd(String dtlstupd) {
        this.dtlstupd = dtlstupd;
    }

    public String getLstupid() {
        return lstupid;
    }

    public void setLstupid(String lstupid) {
        this.lstupid = lstupid;
    }

    public String getRemk() {
        return remk;
    }

    public void setRemk(String remk) {
        this.remk = remk;
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
//        ToStringBuilder tsb = new ToStringBuilder(null);
//        return tsb.append("result",this.result).append("kskid",kskid)
//                .append("actiontype",actiontype).append("mcdno",mcdno)
//                .append("dtaction",dtaction).append("lstupid",lstupid)
//                .append("remk",remk).append("dtual",dtual)
//                .append("dtlstupd",dtlstupd).toString();
    }
}
