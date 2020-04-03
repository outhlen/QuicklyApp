package com.androidybp.basics.utils.file.updata_apk;

import com.androidybp.basics.utils.file.FileUtil;

public class UpdataAPkEntity {
    public int sdkVersion = 0;//SDK 版本
    public String downUrl;//下载apk的地址
    public String filePath = FileUtil.downloadApkPath();//文件保存的全路径
    public int dowloadType;//0-静默下载  1-带进度的下载
    public int updataType;// 0-非强制升级  1-强制升级
}
