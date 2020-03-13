package com.shsxt.base;

public class BaseQuery {
    //当前页
    private Integer page=1;
    //每页显示的条数
    private Integer rows=10;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
