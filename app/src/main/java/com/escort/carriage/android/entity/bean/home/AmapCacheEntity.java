package com.escort.carriage.android.entity.bean.home;

public class AmapCacheEntity {
    public long sid;//serviceID
    public long tid;//terminalId
    public long trid;//trackId

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public long getTrid() {
        return trid;
    }

    public void setTrid(long trid) {
        this.trid = trid;
    }
}
