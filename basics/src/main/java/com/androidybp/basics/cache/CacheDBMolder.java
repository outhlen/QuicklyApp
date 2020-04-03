package com.androidybp.basics.cache;


import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.db.model.CacheDataEntity;
import com.androidybp.basics.cache.db.model.DataCacheKeyModel;
import com.androidybp.basics.cache.db.publicdb.localdb.CacheDB;
import com.androidybp.basics.entity.UserEntity;
import com.androidybp.basics.entity.UserInfoEntity;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.utils.equipment.ScreenUtil;
import com.androidybp.basics.utils.hint.LogUtils;

/**
 * 这个类用于保存全局信息的类
 * <p>
 * 这是全局保存用户信息的类   里面属性变量都是私有化  不要改成public 防止后期优化修改
 * 获取类只能通过get方法获取
 */

public class CacheDBMolder {

    private static volatile CacheDBMolder cacheDBMolder;

    private UserEntity userBean;//登录后响应的用户信息数据对象
    private UserInfoEntity userInfoBean;//用户详情信息
    private CacheDataEntity mCacheDataEntity;//当前缓存数据对象

    private CacheDB cacheDB;//缓存数据使用的对象

    /**
     * 创建当前的单例对象
     *
     * @return
     */
    public static CacheDBMolder getInstance() {
        if (cacheDBMolder == null) {
            synchronized (CacheDBMolder.class) {
                if (cacheDBMolder == null) {
                    createCacheDBMolder();
                }
            }
        }
        return cacheDBMolder;
    }

    /**
     * 创建对象
     *
     * @return
     */
    private static void createCacheDBMolder() {
        cacheDBMolder = new CacheDBMolder();
    }

    /**
     * 获取保存缓存数据的 数据库操作对象
     *
     * @return
     */
    public CacheDB getCacheDB() {
        if (cacheDB == null) {
            synchronized (CacheDB.class) {
                if (cacheDB == null) {
                    cacheDB = new CacheDB();
                }
            }
        }

        return cacheDB;
    }

    /**
     * 清理掉所有用户缓存数据
     */
    public void clearData() {
        if (cacheDB == null) {
            cacheDB = getCacheDB();
        }
        userBean = null;
        userInfoBean = null;
        setUserInfoCache(cacheDB, null);
        setUserInfoEntityCache(cacheDB, null);
        removeUserToken();
        cacheDB = null;
    }

    protected CacheDBMolder() {

    }

    // =======================================
    // ============ CacheDataEntity ==============
    // =======================================

    // ============ CacheDataEntity  获取部分==============

    /**
     * 获取CacheDataEntity 数据
     *
     * @param cacheDB 查询数据库对象, 可以为null
     * @return 优先取内存中  没有取Cache中 没有  返回null  否则 返回  CacheDataEntity实体对象
     */
    public CacheDataEntity getCacheDataEntity(CacheDB cacheDB) {
        try {
            if (mCacheDataEntity == null) {
                if (cacheDB == null)
                    cacheDB = getCacheDB();
                synchronized (CacheDataEntity.class) {
                    if (mCacheDataEntity == null) {
                        CacheDataEntity cacheDataEntity = cacheDB.questData(DataCacheKeyModel.APP_CACHEDATA, CacheDataEntity.class);
                        if (cacheDataEntity == null) {
                            mCacheDataEntity = initCacheEntity(ApplicationContext.getInstance().application);
                        } else {
                            mCacheDataEntity = cacheDataEntity;
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCacheDataEntity;
    }

    /**
     * 初始化缓存数据
     */
    private CacheDataEntity initCacheEntity(Context context) {
        CacheDataEntity cacheDataEntity = new CacheDataEntity();
        DisplayMetrics displayMetrics = ScreenUtil.getDisplayMetrics(context);
        //设备屏幕高度
        cacheDataEntity.setPhoneHeight(displayMetrics.heightPixels);
        //设备屏幕宽度
        cacheDataEntity.setPhoneWidth(displayMetrics.widthPixels);
        //设备的唯一标识
        cacheDataEntity.setEquipmentid(ScreenUtil.getUniqueId(context));

        setCacheDataEntity(cacheDataEntity, null, null);
        return cacheDataEntity;
    }

    // ============ CacheDataEntity  设置部分==============

    /**
     * 保存UserEntity对象
     *
     * @param cacheDataEntity 为 null 将清掉内存和缓存中的数据
     * @param infoJson        可以为null   如果userEntity 不为null 将会把 userEntity转换成json格式 并赋值
     * @param cacheDB         查询数据库对象, 可以为null
     */
    public synchronized void setCacheDataEntity(CacheDataEntity cacheDataEntity, String infoJson, CacheDB cacheDB) {
        this.mCacheDataEntity = cacheDataEntity;
        //将内容保存到缓存中
        if (TextUtils.isEmpty(infoJson) && cacheDataEntity != null) {
            infoJson = JsonManager.createJsonString(cacheDataEntity);
        }
        setCacheDataEntityCache(cacheDB, infoJson);
    }

    /**
     * 将UserEntity转译保存到文件中
     *
     * @param cacheDB  查询数据库对象, 可以为null
     * @param infoJson 需要缓存的userInfo信息字符串 json格式
     *                 如果为 null 会清掉 本地缓存
     */
    public void setCacheDataEntityCache(CacheDB cacheDB, String infoJson) {
        try {
            setCacheData(DataCacheKeyModel.APP_CACHEDATA, cacheDB, infoJson);
        } catch (Exception e) {
            LogUtils.showE("CacheDBMolder", "setUserInfoCache()   " + e.toString() + "\n" + e.getMessage());
        }

    }


    // =======================================
    // ============ UserInfo ==============
    // =======================================

    // ============ UserInfo  获取部分==============

    /**
     * 获取UserInfo 数据
     *
     * @param cacheDB 查询数据库对象, 可以为null
     * @return 优先取内存中  没有取Cache中 没有  返回null  否则 返回  userEntity实体对象
     */
    public UserEntity getUserInfo(CacheDB cacheDB) {
        try {
            if (userBean == null) {
                if (cacheDB == null)
                    cacheDB = getCacheDB();
                userBean = cacheDB.questData(DataCacheKeyModel.USER_INFO, UserEntity.class);
            }
        } catch (Exception e) {

        }
        return userBean;
    }

     /**
     * 获取UserInfo 详情信息  跟登录返回数据不是一个
     *
     * @param cacheDB 查询数据库对象, 可以为null
     * @return 优先取内存中  没有取Cache中 没有  返回null  否则 返回  userEntity实体对象
     */
    public UserInfoEntity getUserInfoEntity(CacheDB cacheDB) {
        try {
            if (userInfoBean == null) {
                if (cacheDB == null)
                    cacheDB = getCacheDB();
                userInfoBean = cacheDB.questData(DataCacheKeyModel.USER_INFO_ENTITY, UserInfoEntity.class);
            }
        } catch (Exception e) {

        }
        return userInfoBean;
    }



    // ============ UserInfo  设置部分==============

    /**
     * 保存UserEntity对象
     *
     * @param userEntity 为 null 将清掉内存和缓存中的数据
     * @param infoJson   可以为null   如果userEntity 不为null 将会把 userEntity转换成json格式 并赋值
     * @param cacheDB    查询数据库对象, 可以为null
     */
    public void setUserInfo(UserEntity userEntity, String infoJson, CacheDB cacheDB) {
        this.userBean = userEntity;
        //将内容保存到缓存中
        if (TextUtils.isEmpty(infoJson) && userEntity != null) {
            infoJson = JsonManager.createJsonString(userEntity);
        }
        setUserInfoCache(cacheDB, infoJson);
    }
/**
     * 保存UserEntity对象
     *
     * @param userEntity 为 null 将清掉内存和缓存中的数据
     * @param infoJson   可以为null   如果userEntity 不为null 将会把 userEntity转换成json格式 并赋值
     * @param cacheDB    查询数据库对象, 可以为null
     */
    public void setUserInfoEntity(UserInfoEntity userEntity, String infoJson, CacheDB cacheDB) {
        this.userInfoBean = userEntity;
        //将内容保存到缓存中
        if (TextUtils.isEmpty(infoJson) && userEntity != null) {
            infoJson = JsonManager.createJsonString(userEntity);
        }
        setUserInfoEntityCache(cacheDB, infoJson);
    }

    /**
     * 将UserEntity转译保存到文件中
     *
     * @param cacheDB  查询数据库对象, 可以为null
     * @param infoJson 需要缓存的userInfo信息字符串 json格式
     *                 如果为 null 会清掉 本地缓存
     */
    public void setUserInfoCache(CacheDB cacheDB, String infoJson) {
        try {
            setCacheData(DataCacheKeyModel.USER_INFO, cacheDB, infoJson);
        } catch (Exception e) {
            LogUtils.showE("CacheDBMolder", "setUserInfoCache()   " + e.toString() + "\n" + e.getMessage());
        }

    }
/**
     * 将UserInfoEntity转译保存到文件中
     *
     * @param cacheDB  查询数据库对象, 可以为null
     * @param infoJson 需要缓存的userInfo信息字符串 json格式
     *                 如果为 null 会清掉 本地缓存
     */
    public void setUserInfoEntityCache(CacheDB cacheDB, String infoJson) {
        try {
            setCacheData(DataCacheKeyModel.USER_INFO_ENTITY, cacheDB, infoJson);
        } catch (Exception e) {
            LogUtils.showE("CacheDBMolder", "setUserInfoCache()   " + e.toString() + "\n" + e.getMessage());
        }

    }



    // =======================================
    // ============ 综合使用 ==============
    // =======================================


    /**
     * 设置
     *
     * @param name     更新数据的名称
     * @param cacheDB  操作表对象
     * @param infoJson 保存数据
     */
    private void setCacheData(String name, CacheDB cacheDB, String infoJson) {
        if (cacheDB == null)
            cacheDB = getCacheDB();
        if (TextUtils.isEmpty(infoJson)) {
            cacheDB.deleteData(name, -1);
        } else {
            cacheDB.addData(name, infoJson, -1, -1);
        }
    }


    /**
     * 获取当前保存的 sessionId 数据
     * 优先取 数据库缓存中获取  没有在去SP中拿   为了兼容 后期会将SP删除掉
     *
     * @return
     */
    public String getJsessionId() {
        CacheDataEntity cacheDataEntity = getCacheDataEntity(null);
        return cacheDataEntity.getJsessionID();
    }

    /**
     * 删除 缓存的 token  和 jsessionId
     *
     * @param type 0 -- 全部删除   1 -- 只删除Token   2 -- 只删除jsessionId
     */
    public void removeTokenAndJsessionId(int type) {
        CacheDataEntity cacheDataEntity = getCacheDataEntity(null);
        switch (type) {
            case 0://全部删除
                removeUserToken();
                cacheDataEntity.setJsessionID(null);
                setCacheDataEntity(cacheDataEntity, null, getCacheDB());
                break;
            case 1://只删除Token
                removeUserToken();
                break;
            case 2://只删除jsessionId
                cacheDataEntity.setJsessionID(null);
                setCacheDataEntity(cacheDataEntity, null, getCacheDB());
                break;
        }
    }

    /**
     * 退出登录需要调用的方法
     */
    public void logOutUser() {

    }

    // =======================================
    // ============ Token部分    开始==============
    // =======================================

    /**
     * 全局使用保存Token的方法
     *
     * @param token
     */
    public void setUserToken(String token) {
        CacheDB cacheDB = getCacheDB();
        //1、先保存到全局缓存类中
        CacheDataEntity cacheDataEntity = getCacheDataEntity(cacheDB);
        cacheDataEntity.setToken(token);
        setCacheDataEntity(cacheDataEntity, null, cacheDB);
        //2、在将token单独保存到 本地数据库中
        cacheDB.addDataAES(DataCacheKeyModel.USER_TOKEN, token, -1, -1);
//        //3、将Token保存到 外部数据库中
//        ExternalCacheAllTableDB externalCacheAllTableDB = new ExternalCacheAllTableDB();
//        externalCacheAllTableDB.addDataAES(DataCacheKeyModel.USER_TOKEN, token, -1, -1);
    }

    /**
     * 全局使用删除Token的方法
     */
    public void removeUserToken() {
        CacheDB cacheDB = getCacheDB();
        //1、先删除全局缓存类中
        CacheDataEntity cacheDataEntity = getCacheDataEntity(cacheDB);
        cacheDataEntity.setToken(null);
        setCacheDataEntity(cacheDataEntity, null, cacheDB);
        //2、在将token从 本地数据库中删除
        cacheDB.deleteData(DataCacheKeyModel.USER_TOKEN, -1);
//        //3、将Token从 外部数据库中删除
//        ExternalCacheAllTableDB externalCacheAllTableDB = new ExternalCacheAllTableDB();
//        externalCacheAllTableDB.deleteData(DataCacheKeyModel.USER_TOKEN, -1);
    }

    /**
     * 获取 当前保存的token 数据
     * 优先取 数据库缓存中获取  没有在去SP中拿   为了兼容 后期会将SP删除掉
     *
     * @return
     */
    public String getUserToken() {
        String token = null;
        CacheDB cacheDB = getCacheDB();
        //1、现在 全局数据里面取
        CacheDataEntity cacheDataEntity = getCacheDataEntity(cacheDB);
        token = cacheDataEntity.getToken();
        if (token == null || token.equals("")) {
            //全局缓存中没有 那就去 本地数据库中拿
            token = cacheDB.questDataAES(DataCacheKeyModel.USER_TOKEN);
//            if (token == null || token.equals("")) {
//                //本地数据库中为null  那就去外部数据拿
//                ExternalCacheAllTableDB externalCacheAllTableDB = new ExternalCacheAllTableDB();
//                token = externalCacheAllTableDB.questDataAES(DataCacheKeyModel.USER_TOKEN);
//            }
        }
        return token;
    }


    // =======================================
    // ============ Token部分    结束 ==============
    // =======================================



    //   =====================  ↓↓↓↓ 数据一级缓存 ↓↓↓↓　 ===============

    /**
     * 将关键信息进行一级缓存  只保存本地数据库中
     *
     * @param key      缓存数据的key
     * @param value    缓存数据的内容
     * @param priority 缓存数据的优先级    -1 优先级很高  0 - 优先级 中   1 - 优先级 低（默认）
     * @param oldTime  缓存数据的失效时间   时间的int 值  -1是没有失效时间  秒为单位
     */
    public void setProjectCacheData(String key, String value, int priority, int oldTime) {
        CacheDB cacheDB = getCacheDB();
        cacheDB.addDataAES(key, value, priority, oldTime);
    }

    /**
     * 将关键信息进行删除  这个删除为真删
     *
     * @param key 缓存数据的key
     */
    public void removeProjectCacheData(String key) {
        CacheDB cacheDB = getCacheDB();
        //1、在将数据从 本地数据库中删除
        cacheDB.deleteData(key, -1);
    }

    /**
     * 取出保存的关键数据
     * @param key  key
     * @return  保存的Json串
     */
    public String getProjectCacheData(String key) {
        String json = null;
        CacheDB cacheDB = getCacheDB();
        //从本地数据库中取数据
        json = cacheDB.questDataAES(key);

        return json;
    }


    /**
     * 取出保存的关键数据
     * @param key  key
     * @return  解析出对象的
     */
    public <T> T getProjectCacheDataEntity(String key, Class<T> calzz) {
        T t = null;
        if(calzz != null){
            String projectDataEntity = getProjectCacheData(key);
            if(projectDataEntity != null && !projectDataEntity.equals("")){
                t = JsonManager.getJsonBean(projectDataEntity, calzz);
            }
        }

        return t;
    }

    //   =====================  ↑↑↑↑ 数据一级缓存 ↑↑↑↑　 ===============





    //   =====================  ↓↓↓↓ 数据二级缓存 ↓↓↓↓　 ===============

    /**
     * 将关键信息进行2级缓存
     *
     * @param key      缓存数据的key
     * @param value    缓存数据的内容
     * @param priority 缓存数据的优先级    -1 优先级很高  0 - 优先级 中   1 - 优先级 低（默认）
     * @param oldTime  缓存数据的失效时间   时间的int 值  -1是没有失效时间  秒为单位
     */
    public void setProjectDataEntity(String key, String value, int priority, int oldTime) {
        CacheDB cacheDB = getCacheDB();
        //1、在将数据 本地数据库中
        cacheDB.addDataAES(key, value, priority, oldTime);
//        //2、将数据 外部数据库中
//        ExternalCacheAllTableDB externalCacheAllTableDB = new ExternalCacheAllTableDB();
//        externalCacheAllTableDB.addDataAES(key, value, priority, oldTime);
    }

    /**
     * 将关键信息进行删除  这个删除为真删
     *
     * @param key 缓存数据的key
     */
    public void removeProjectDataEntity(String key) {
        CacheDB cacheDB = getCacheDB();
        //1、在将数据从 本地数据库中删除
        cacheDB.deleteData(key, -1);
//        //3、将数据从 外部数据库中删除
//        ExternalCacheAllTableDB externalCacheAllTableDB = new ExternalCacheAllTableDB();
//        externalCacheAllTableDB.deleteData(key, -1);
    }

    /**
     * 取出保存的关键数据
     * @param key  key
     * @return  保存的Json串
     */
    public String getProjectDataEntity(String key) {
        String json = null;
        CacheDB cacheDB = getCacheDB();
        //从本地数据库中取数据
        json = cacheDB.questDataAES(key);
//        if (json == null || json.equals("")) {
//            //本地数据库中为null  那就去外部数据拿
//            ExternalCacheAllTableDB externalCacheAllTableDB = new ExternalCacheAllTableDB();
//            json = externalCacheAllTableDB.questDataAES(key);
//        }

        return json;
    }


    /**
     * 取出保存的关键数据
     * @param key  key
     * @return  解析出对象的
     */
    public <T> T getProjectDataEntity(String key, Class<T> calzz) {
        T t = null;
        if(calzz != null){
            String projectDataEntity = getProjectDataEntity(key);
            if(projectDataEntity != null && !projectDataEntity.equals("")){
               t = JsonManager.getJsonBean(projectDataEntity, calzz);
            }
        }

        return t;
    }

    //   =====================  ↑↑↑↑ 数据二级缓存 ↑↑↑↑　 ===============


    public boolean isNotification(){
        boolean flag = false;

        CacheDataEntity cacheDataEntity = getCacheDataEntity(null);
        flag = cacheDataEntity.isNotification;
        return flag;
    }

    /**
     * 设置推送权限
     * @param flag
     */
    public void setNotification(boolean flag){
        CacheDataEntity cacheDataEntity = getCacheDataEntity(null);
        cacheDataEntity.isNotification = flag;
    }




}
