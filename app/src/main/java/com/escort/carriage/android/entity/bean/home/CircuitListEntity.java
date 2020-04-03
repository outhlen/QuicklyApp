package com.escort.carriage.android.entity.bean.home;

import java.util.List;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-18 14:31
 */
public class CircuitListEntity {

    /**
     * isOpen : 0
     * isDeposit : 0
     * list : [{"id":"1239476175464480770","userLoginId":"1238440074241159170","startProvinceCode":"370000","startCityCode":"370100","startAreaCode":"370102","endProvinceCode":"420000","endCityCode":"420100","endAreaCode":"420106","isDelete":"0","ststus":"1","startProvinceName":"山东省","startCitName":"济南市","startArea":"历下区","endProvinceName":"湖北省","endCitName":"武汉市","endtArea":"武昌区","list":[{"id":"1239476175674195970","cityCode":"371300","cityName":"临沂市"}]}]
     */

    public int isOpen;//是否开启智能匹配(0 否 1 是) 这个是进入接单设置页面之后，判断的状态，是否接单
    public int cityDistribute;//是否开启城市配送(0 否 1 是) 这个是进入接单设置之后，判断的状态，是否开启城市配送
    public int isDeposit;//是否开启必须交押金功能(0 否 1 是) 这个是进入接单设置之后，判断的状态，是否交押金
    public String provinceName;//配送省份
    public String cityName;//配送城市
    public List<CircuitItemEntity> list;

    public List<CircuitItemEntity> getList() {
        return list;
    }

    public void setList(List<CircuitItemEntity> list) {
        this.list = list;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public int getCityDistribute() {
        return cityDistribute;
    }

    public void setCityDistribute(int cityDistribute) {
        this.cityDistribute = cityDistribute;
    }

    public int getIsDeposit() {
        return isDeposit;
    }

    public void setIsDeposit(int isDeposit) {
        this.isDeposit = isDeposit;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public static class CircuitItemEntity {
        /**
         * id : 1239476175464480770
         * userLoginId : 1238440074241159170
         * startProvinceCode : 370000
         * startCityCode : 370100
         * startAreaCode : 370102
         * endProvinceCode : 420000
         * endCityCode : 420100
         * endAreaCode : 420106
         * isDelete : 0
         * ststus : 1
         * startProvinceName : 山东省
         * startCitName : 济南市
         * startArea : 历下区
         * endProvinceName : 湖北省
         * endCitName : 武汉市
         * endtArea : 武昌区
         * list : [{"id":"1239476175674195970","cityCode":"371300","cityName":"临沂市"}]
         */

        public String id;
        public String userLoginId;
        public String startProvinceCode;
        public String startCityCode;
        public String startAreaCode;
        public String endProvinceCode;
        public String endCityCode;
        public String endAreaCode;
        public String isDelete;
        public int ststus;
        public String startProvinceName;
        public String startCitName;
        public String startArea;
        public String endProvinceName;
        public String endCitName;
        public String endtArea;
        public List<ListBean> list;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserLoginId() {
            return userLoginId;
        }

        public void setUserLoginId(String userLoginId) {
            this.userLoginId = userLoginId;
        }

        public String getStartProvinceCode() {
            return startProvinceCode;
        }

        public void setStartProvinceCode(String startProvinceCode) {
            this.startProvinceCode = startProvinceCode;
        }

        public String getStartCityCode() {
            return startCityCode;
        }

        public void setStartCityCode(String startCityCode) {
            this.startCityCode = startCityCode;
        }

        public String getStartAreaCode() {
            return startAreaCode;
        }

        public void setStartAreaCode(String startAreaCode) {
            this.startAreaCode = startAreaCode;
        }

        public String getEndProvinceCode() {
            return endProvinceCode;
        }

        public void setEndProvinceCode(String endProvinceCode) {
            this.endProvinceCode = endProvinceCode;
        }

        public String getEndCityCode() {
            return endCityCode;
        }

        public void setEndCityCode(String endCityCode) {
            this.endCityCode = endCityCode;
        }

        public String getEndAreaCode() {
            return endAreaCode;
        }

        public void setEndAreaCode(String endAreaCode) {
            this.endAreaCode = endAreaCode;
        }

        public String getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(String isDelete) {
            this.isDelete = isDelete;
        }

        public int getStstus() {
            return ststus;
        }

        public void setStstus(int ststus) {
            this.ststus = ststus;
        }

        public String getStartProvinceName() {
            return startProvinceName;
        }

        public void setStartProvinceName(String startProvinceName) {
            this.startProvinceName = startProvinceName;
        }

        public String getStartCitName() {
            return startCitName;
        }

        public void setStartCitName(String startCitName) {
            this.startCitName = startCitName;
        }

        public String getStartArea() {
            return startArea;
        }

        public void setStartArea(String startArea) {
            this.startArea = startArea;
        }

        public String getEndProvinceName() {
            return endProvinceName;
        }

        public void setEndProvinceName(String endProvinceName) {
            this.endProvinceName = endProvinceName;
        }

        public String getEndCitName() {
            return endCitName;
        }

        public void setEndCitName(String endCitName) {
            this.endCitName = endCitName;
        }

        public String getEndtArea() {
            return endtArea;
        }

        public void setEndtArea(String endtArea) {
            this.endtArea = endtArea;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 1239476175674195970
             * cityCode : 371300
             * cityName : 临沂市
             */

            public String id;
            public String cityCode;
            public String cityName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCityCode() {
                return cityCode;
            }

            public void setCityCode(String cityCode) {
                this.cityCode = cityCode;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }
        }
    }
}
