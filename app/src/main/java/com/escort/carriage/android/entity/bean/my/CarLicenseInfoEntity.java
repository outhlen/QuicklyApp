package com.escort.carriage.android.entity.bean.my;

import java.util.List;

public class CarLicenseInfoEntity {

    public String total;
    public int pageNum;
    public int pageSize;
    public int size;
    public int startRow;
    public int endRow;
    public int pages;
    public int prePage;
    public int nextPage;
    public boolean isFirstPage;
    public boolean isLastPage;
    public boolean hasPreviousPage;
    public boolean hasNextPage;
    public int navigatePages;
    public int navigateFirstPage;
    public int navigateLastPage;
    public List<ListBean> list;
    public List<Integer> navigatepageNums;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(List<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public static class ListBean {
        /**
         * id : 1239819198920540161
         * licencePlate : 鲁A7B33Q
         * vehicleType : 小型轿车
         * vehicleTypeInfo : 雪佛兰牌SGM7140MTB
         * useNature : 非营运
         * vehicleLicenseFront : http://xeryb.oss-cn-qingdao.aliyuncs.com/jiaozheng/home/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200317111817.jpg
         */

        public String id;
        public String licencePlate;
        public String vehicleType;
        public String vehicleTypeInfo;
        public String useNature;
        public String vehicleLicenseFront;
        public int vehicleUsage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLicencePlate() {
            return licencePlate;
        }

        public void setLicencePlate(String licencePlate) {
            this.licencePlate = licencePlate;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getVehicleTypeInfo() {
            return vehicleTypeInfo;
        }

        public void setVehicleTypeInfo(String vehicleTypeInfo) {
            this.vehicleTypeInfo = vehicleTypeInfo;
        }

        public String getUseNature() {
            return useNature;
        }

        public void setUseNature(String useNature) {
            this.useNature = useNature;
        }

        public String getVehicleLicenseFront() {
            return vehicleLicenseFront;
        }

        public void setVehicleLicenseFront(String vehicleLicenseFront) {
            this.vehicleLicenseFront = vehicleLicenseFront;
        }

        public int getVehicleUsage() {
            return vehicleUsage;
        }

        public void setVehicleUsage(int vehicleUsage) {
            this.vehicleUsage = vehicleUsage;
        }
    }
}
