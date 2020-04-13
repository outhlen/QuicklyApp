package com.androidybp.basics.cache.externaldb.model;

import android.os.Environment;

import com.androidybp.basics.configuration.BaseProjectConfiguration;
import com.androidybp.basics.utils.file.FileOperationUtils;
import com.androidybp.basics.utils.file.FileUtil;

import java.io.File;

public class FileNameModel {
    /**
     * 保存外部数据库的二级文件夹 名称
     */
    public static final String EXTERNAL_FILE_FOLDER = "cache";

    /**
     * 数据库文件的具体路径
     */
    public static final String EXTERNAL_NAME_TWO_FOLDER = FileUtil.getProjectDocumentsPath() + File.separator
            + BaseProjectConfiguration.BASE_FOLDER + "/" + EXTERNAL_FILE_FOLDER ;


    public String getExternalFilePath(){
        //这个方法只是将用于构建二级目录的   里面的一级目录是全局统一的
        FileOperationUtils.getTwoFolder(EXTERNAL_FILE_FOLDER);
        return EXTERNAL_NAME_TWO_FOLDER + "/" + BaseProjectConfiguration.EXTERNAL_NAME;
    }

}
