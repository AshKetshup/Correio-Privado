package com.cp.correioprivadosite.dados;

import java.util.Date;

public class Filter {

    private Date startDate;
    private Date endDate;
    private long topic;

    public Filter () {    }

    public Filter (Date startDate, Date endDate, long topic){
        this.startDate = startDate;
        this.endDate = endDate;
        this.topic = topic;
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getTopic() {
        return topic;
    }

    public void setTopic(long topic) {
        this.topic = topic;
    }
}
