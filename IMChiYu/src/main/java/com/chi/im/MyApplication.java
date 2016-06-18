package com.chi.im;

import android.app.Application;

import com.chi.im.db.MySqliteOpenhelper;

/**
 * Created by Administrator on 2016/6/18.
 */
public class MyApplication extends Application {

    public static MySqliteOpenhelper sqliteOpenhelper;

    @Override
    public void onCreate() {
        super.onCreate();
        sqliteOpenhelper = new MySqliteOpenhelper(this);
    }

    public MySqliteOpenhelper getMySqliteOpenhelper() {
        if (sqliteOpenhelper == null) {
            sqliteOpenhelper = new MySqliteOpenhelper(this);
        }
        return sqliteOpenhelper;
    }


}
