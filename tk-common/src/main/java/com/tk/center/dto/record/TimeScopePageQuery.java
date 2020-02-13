package com.tk.center.dto.record;

import com.magic.springboot.dto.PageQuery;

import java.util.Date;

/**
 * Created by Mingkun on 2020/02/03.
 */
public class TimeScopePageQuery extends PageQuery {
    protected Date start;
    protected Date finish;
    protected int[] time;
    protected boolean asc;
    protected String timeField;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getFinish() {
        return finish;
    }

    public void setFinish(Date finish) {
        this.finish = finish;
    }

    public int[] getTime() {
        return time;
    }

    public void setTime(int[] time) {
        this.time = time;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public String getTimeField() {
        return timeField;
    }

    public void setTimeField(String timeField) {
        this.timeField = timeField;
    }
}
