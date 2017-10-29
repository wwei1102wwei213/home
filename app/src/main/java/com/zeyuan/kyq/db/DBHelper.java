package com.zeyuan.kyq.db;

import com.ta.util.db.TASQLiteDatabase;
import com.zeyuan.kyq.Entity.DigitConfEntity;
import com.zeyuan.kyq.Entity.MsgClickEntity;
import com.zeyuan.kyq.Entity.PushNewEntity;
import com.zeyuan.kyq.Entity.StepConfEntity;
import com.zeyuan.kyq.Entity.SyncConfEntity;
import com.zeyuan.kyq.application.ZYApplication;
import com.zeyuan.kyq.utils.Const;
import com.zeyuan.kyq.utils.ExceptionUtils;
import com.zeyuan.kyq.utils.LogCustom;

import java.util.List;

/**
 * Created by Administrator on 2016/3/30.
 * <p>
 * 数据表单帮助类
 *
 * @author wwei
 */
public class DBHelper {

    private DBHelper() {
    }

    private static DBHelper instance = null;

    /***
     * 单例模式，注意要加同步锁
     *
     * @return DBHelper
     */
    public static DBHelper getInstance() {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper();
                }
            }
        }
        return instance;
    }

    public void initCreateSyncTable(ZYApplication application) {
        TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
        if (!tasqLiteDatabase.hasTable(SyncConfEntity.class)) {
            tasqLiteDatabase.creatTable(SyncConfEntity.class);
            LogCustom.i(Const.TAG.ZY_DATA, "SyncConfEntity表已创建");
        }
        application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
    }

    public void initCreateDigitTable(ZYApplication application) {
        TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
        if (!tasqLiteDatabase.hasTable(DigitConfEntity.class)) {
            tasqLiteDatabase.creatTable(DigitConfEntity.class);
            LogCustom.i(Const.TAG.ZY_DATA, "DigitConfEntity表已创建");
        }
        application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
    }

    public void initCreateStepTable(ZYApplication application) {
        TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
        if (!tasqLiteDatabase.hasTable(StepConfEntity.class)) {
            tasqLiteDatabase.creatTable(StepConfEntity.class);
            LogCustom.i(Const.TAG.ZY_DATA, "StepConfEntity表已创建");
        }
        application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
    }


    public void initDropSyncTable(ZYApplication application) {
        TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
        if (tasqLiteDatabase.hasTable(SyncConfEntity.class)) {
            tasqLiteDatabase.dropTable(SyncConfEntity.class);
            LogCustom.i(Const.TAG.ZY_DATA, "SyncConfEntity表已清除");
        }
        application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
    }

    public void initDropDigitTable(ZYApplication application) {
        TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
        if (tasqLiteDatabase.hasTable(DigitConfEntity.class)) {
            tasqLiteDatabase.dropTable(DigitConfEntity.class);
            LogCustom.i(Const.TAG.ZY_DATA, "DigitConfEntity表已清除");
        }
        application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
    }

    public void initDropStepTable(ZYApplication application) {
        TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
        if (tasqLiteDatabase.hasTable(StepConfEntity.class)) {
            tasqLiteDatabase.dropTable(StepConfEntity.class);
            LogCustom.i(Const.TAG.ZY_DATA, "StepConfEntity表已清除");
        }
        application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
    }

    //查询分期数据
    public List<DigitConfEntity> queryDigitConf(ZYApplication myApplication) {
        TASQLiteDatabase tasqLiteDatabase = myApplication.getSQLiteDatabasePool().getSQLiteDatabase();
        List<DigitConfEntity> list = tasqLiteDatabase.query(DigitConfEntity.class, false, null, null, null, null, null);
        myApplication.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        return list;
    }

    //查询基本配置数据
    public List<SyncConfEntity> querySyncConf(ZYApplication myApplication) {
        TASQLiteDatabase tasqLiteDatabase = myApplication.getSQLiteDatabasePool().getSQLiteDatabase();
        List<SyncConfEntity> list = tasqLiteDatabase.query(SyncConfEntity.class, false, null, null, null, null, null);
        myApplication.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        return list;
    }

    //查询阶段数据
    public List<StepConfEntity> queryStepConf(ZYApplication myApplication) {
        TASQLiteDatabase tasqLiteDatabase = myApplication.getSQLiteDatabasePool().getSQLiteDatabase();
        List<StepConfEntity> list = tasqLiteDatabase.query(StepConfEntity.class, false, null, null, null, null, null);
        myApplication.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        return list;
    }

    public List<PushNewEntity> queryPush(ZYApplication myApplication) {
        TASQLiteDatabase tasqLiteDatabase = myApplication.getSQLiteDatabasePool().getSQLiteDatabase();
        List<PushNewEntity> list = tasqLiteDatabase.query(PushNewEntity.class, false, null, null, null, null, null);
        myApplication.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        return list;
    }

    public void initCreatePushTable(ZYApplication application) {
        TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
        if (!tasqLiteDatabase.hasTable(PushNewEntity.class)) {
            tasqLiteDatabase.creatTable(PushNewEntity.class);
            LogCustom.i(Const.TAG.ZY_DATA, "Push表已创建");
        }
        application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
    }

    public void initDropPushTable(ZYApplication application) {
        TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
        if (tasqLiteDatabase.hasTable(PushNewEntity.class)) {
            tasqLiteDatabase.dropTable(PushNewEntity.class);
            LogCustom.i(Const.TAG.ZY_DATA, "Push表已删除");
        }
        application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
    }

    public void updataPushTable(ZYApplication application, PushNewEntity entity) {
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            tasqLiteDatabase.delete(PushNewEntity.class, "time=" + entity.getTime());
            entity.setRead(1);
            tasqLiteDatabase.insert(entity);
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "UpdatePush");
            LogCustom.i("ZYS", "PushEntity更新失败");
        }

    }

    //创建点击事件表
    public void initCreateMsgClickTable(ZYApplication application) {
        TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
        if (!tasqLiteDatabase.hasTable(MsgClickEntity.class)) {
            tasqLiteDatabase.creatTable(MsgClickEntity.class);
            LogCustom.i(Const.TAG.ZY_DATA, "Msg表已创建");
        }
        application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
    }

    //查询所有点击数据
    public List<MsgClickEntity> queryMsgClick(ZYApplication application) {
        TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
        List<MsgClickEntity> list = tasqLiteDatabase.query(MsgClickEntity.class, false, null, null, null, null, null);
        application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        LogCustom.d("zys", "本地保存的点击数据数量为：" + list.size() + "条");
        return list;
    }

    //更新点击数据
    public void updateMsgClickTable(ZYApplication application, MsgClickEntity entity) {
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            tasqLiteDatabase.delete(MsgClickEntity.class, "msgid=" + entity.getMsgid());
            entity.setRead("1");
            tasqLiteDatabase.insert(entity);
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "UpdatePush");
            LogCustom.i("ZYS", "MsgClick更新失败");
        }
    }

    //批量插入点击数据
    public void insertMsgClickList(ZYApplication application, List<MsgClickEntity> msgEntityList) {
        try {
            TASQLiteDatabase tasqLiteDatabase = application.getSQLiteDatabasePool().getSQLiteDatabase();
            for (MsgClickEntity clickEntity : msgEntityList) {
                tasqLiteDatabase.insert(clickEntity);
            }
            application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "insertMsgClickList");
            LogCustom.i("ZYS", "MsgClickList更新失败");
        }
    }

    //清除点击数据
    public void dropMsgClickTable() {
        try {
            TASQLiteDatabase tasqLiteDatabase = ZYApplication.application.getSQLiteDatabasePool().getSQLiteDatabase();
            tasqLiteDatabase.dropTable(MsgClickEntity.class);
            ZYApplication.application.getSQLiteDatabasePool().releaseSQLiteDatabase(tasqLiteDatabase);
            initCreateMsgClickTable(ZYApplication.application);//重新创建表
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "dropMsgClickTable");
            LogCustom.i("ZYS", "MsgClickList删除失败");
        }
    }

}
