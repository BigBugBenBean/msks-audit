package com.pccw.sc2.audit.log;

import com.alibaba.fastjson.JSON;

public class ExceptionLogVO {

    private String actiontype;

    private String kskid;

    private String dtaction;

    private String dtlstupd;

    private String dtual;

    private String excptcd;

    private String lstupid;

    private String mcdno;

    private String remk;

    public String getActiontype() {
        return actiontype;
    }

    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }

    public String getKskid() {
        return kskid;
    }

    public void setKskid(String kskid) {
        this.kskid = kskid;
    }

    public String getDtaction() {
        return dtaction;
    }

    public void setDtaction(String dtaction) {
        this.dtaction = dtaction;
    }

    public String getDtlstupd() {
        return dtlstupd;
    }

    public void setDtlstupd(String dtlstupd) {
        this.dtlstupd = dtlstupd;
    }

    public String getDtual() {
        return dtual;
    }

    public void setDtual(String dtual) {
        this.dtual = dtual;
    }

    public String getExcptcd() {
        return excptcd;
    }

    public void setExcptcd(String excptcd) {
        this.excptcd = excptcd;
    }

    public String getLstupid() {
        return lstupid;
    }

    public void setLstupid(String lstupid) {
        this.lstupid = lstupid;
    }

    public String getMcdno() {
        return mcdno;
    }

    public void setMcdno(String mcdno) {
        this.mcdno = mcdno;
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
//        return tsb.append("excptcd",this.excptcd).append("kskid",kskid)
//                  .append("actiontype",actiontype).append("mcdno",mcdno)
//                  .append("dtaction",dtaction).append("lstupid",lstupid)
//                  .append("dtlstupd",dtlstupd).toString();
//                  .append("remk",remk).append("dtual",dtual)
    }
}
