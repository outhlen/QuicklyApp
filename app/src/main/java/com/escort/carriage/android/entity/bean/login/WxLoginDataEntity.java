package com.escort.carriage.android.entity.bean.login;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-16 16:39
 */
public class WxLoginDataEntity {
    /**
     * openid : o7HBZtySwylMNRm8a2rtFnH3hz2g
     * nickname : 后身
     * sex : 1
     * province : Shandong
     * city :
     * country : CN
     * headimgurl : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIQYr4cYev5Z9rAy0VzZBWbXuf000tgJCoicpyfFC3ycgX89cDzcunNaWHkbguEWQNficibhhkfnDVeg/132
     * privilege : []
     * unionid : oul2L6gmqV4jFCy9dJbL7LiHueNU
     */

    private String openid;
    private String nickname;
    private String sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private String privilege;
    private String unionid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

}
