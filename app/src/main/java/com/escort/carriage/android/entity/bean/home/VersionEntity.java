package com.escort.carriage.android.entity.bean.home;

public class VersionEntity {
    /**
     id	int	id
     terminalId	int	[0:IOS, 1:Android]
     groupId	int	[0:发货方, 1:承接方]
     compulsory	int	是否强制更新
     updateUrl	string	更新url
     versionCode	string	版本号
     */

    public String id;
    public int terminalId;
    public int groupId;
    public int compulsory;
    public String updateUrl;
    public int versionCode;
    public String versionName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getCompulsory() {
        return compulsory;
    }

    public void setCompulsory(int compulsory) {
        this.compulsory = compulsory;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }


    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
