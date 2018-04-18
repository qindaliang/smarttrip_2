/**
 * Copyright 2018 bejson.com
 */
package com.qin.pojo.gas;

import java.util.List;

/**
 * Auto-generated: 2018-03-31 13:34:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Result {

    private List<Data> data;
    private Pageinfo pageinfo;

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return data;
    }

    public void setPageinfo(Pageinfo pageinfo) {
        this.pageinfo = pageinfo;
    }

    public Pageinfo getPageinfo() {
        return pageinfo;
    }

}