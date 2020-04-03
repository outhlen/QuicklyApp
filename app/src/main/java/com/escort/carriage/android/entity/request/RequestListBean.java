package com.escort.carriage.android.entity.request;

import java.util.List;

public class RequestListBean {
    /**
     * page : {"pageNumber":0,"pageSize":10,"orders":[{"field":"id","direction":"DESC"}]}
     */

    private PageBean page;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }


}
