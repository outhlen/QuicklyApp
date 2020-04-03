package com.escort.carriage.android.entity.request.home;

public class NewsListReqBean {
    /**
     * newsType : 1
     * page : {"pageNumber":1,"pageSize":10}
     */

    private String newsType;
    private PageBean page;

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public static class PageBean {
        /**
         * pageNumber : 1
         * pageSize : 10
         */

        private int pageNumber;
        private int pageSize;

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }
}
