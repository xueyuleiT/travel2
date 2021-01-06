package com.jtcxw.glcxw.utils;

import com.greendao.gen.MessageBeanDao;
import com.jtcxw.glcxw.localbean.MessageBean;

/**
 * 初始化、存放及获取DaoUtils
 */
public class DaoUtilsStore {
    private volatile static DaoUtilsStore instance = new DaoUtilsStore();
    private CommonDaoUtils<MessageBean> mUserDaoUtils;

    public static DaoUtilsStore getInstance() {
        return instance;
    }

    private DaoUtilsStore() {
        DaoManager mManager = DaoManager.getInstance();
        MessageBeanDao messageBeanDao = mManager.getDaoSession().getMessageBeanDao();
        mUserDaoUtils = new CommonDaoUtils(MessageBean.class, messageBeanDao);
    }

    public CommonDaoUtils<MessageBean> getUserDaoUtils() {
        return mUserDaoUtils;
    }

}
