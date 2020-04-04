package com.escort.carriage.android.entity.request.home;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-17 16:06
 */
public class HistoryOrderListRequestEnity {

    public String cargoName;//货物名称
    public String startCity;//开始城市
    public String endCity;//目的城市
    public String order_place_time;//时间 排序,参数携带
    public String endWeight;//货物最大重量
    public String startWeight;//货物最小重量
    public String endVolume;//货物最大体积
    public String startVolume;//货物最小体积
    public String orderPlaceTime;//下单时间开始时间 yyyy-MM-dd HH:mm:ss
    public String endDate;//结束时间 yyyy-MM-dd HH:mm:ss
    public String orderType;//货运类型 0 发布货源单 1 货源大厅匹配订单 2实时（智能供配） 3专线直达) 4 系统录入 这个参数，就按照这个传
    public int orderStatus = 8;//货运类型 0 发布货源单 1 货源大厅匹配订单 2实时（智能供配） 3专线直达) 4 系统录入 这个参数，就按照这个传
    public PageEntity page;



    public static class PageEntity{
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
