package com.escort.carriage.android.entity.request;

import java.util.List;

public class PageBean {
    /**
     * pageNumber : 0
     * pageSize : 10
     * orders : [{"field":"id","direction":"DESC"}]
     */

    private int pageNumber;
    private int pageSize;
    private List<OrdersBean> orders;

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

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

    public static class OrdersBean {
        /**
         * field : id
         * direction : DESC
         */

        private String field;
        private String direction;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }
    }
}
