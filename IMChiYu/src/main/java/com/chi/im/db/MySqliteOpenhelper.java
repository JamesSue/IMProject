package com.chi.im.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.chi.im.model.MessageYu;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

import online.green.dao.Msg;

/**
 * Created by Administrator on 2016/6/2.
 */
public class MySqliteOpenhelper extends SQLiteOpenHelper {
    private String TABLE_NAME = "chiyuim";
    private SQLiteDatabase sqLiteDatabase;


    public MySqliteOpenhelper(Context context) {
        super(context, "chiyu", null, 1, null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        this.sqLiteDatabase = db;

//       //创建数据库的表
//        String  create_table="create table  "+TABLE_NAME
//                +"( " +
//                "id integer primary key autoincrement," +
//                "from  text,"+
//                "to text,"+
//                "type varchar(5),"+
//                "body text,"+
//                "date text"
//                +")";
        // while compiling: create table  chiyuim( id integer primary key autoincrement,froms  text,tos text,type varchar(5),body text,date text)
        String create_table = "create table chattable(_id integer primary key autoincrement,comes text,tos text,type text,body text,date text)";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //增加数据（插入数据）

    public void insetData(MessageYu msg) {


        SQLiteDatabase db = this.getWritableDatabase();
        String insert = "insert into chattable values"
                + " ("
                + null + ","
                + "'" + msg.getFrom() + "'" + ","
                + "'" + msg.getTo() + "'" + ","
                + "'" + msg.getType() + "'" + ","
                + "'" + msg.getBody() + "'" + ","
                + "'" + msg.getDate() + "'"
                + ")";
        db.execSQL(insert);
        // dbDao.exeDO("insert into xiangqiao_table values(null,'xiangqiao','xiangqiao 的内容','2011-11-10 12:10:11')");

    }

    //查找数据
    public List<MessageYu> getAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        //  String getAllSql="select * from  chattable order by _id desc";
        String getAllSql = "select * from  chattable";

        List<MessageYu> all = new ArrayList<MessageYu>();

        Cursor cursor = db.rawQuery(getAllSql, null);
        all.clear();
        while (cursor.moveToNext()) {
//            String  ss=cursor.getString(1).toString();
//            Log.d("msg",cursor.getInt(0)+"------"+cursor.getString(4)+"\t\t\t\t"+cursor.getString(1)+"----->"+cursor.getString(2));
//
//            cursor.getString(0);
            String body = cursor.getString(4);
            if (!body.equals("null")) {
                MessageYu messageYuItem = new MessageYu(cursor.getString(5), cursor.getString(3), cursor.getString(4), cursor.getString(2), cursor.getString(1));
                all.add(messageYuItem);
            }

        }
        return all;

    }


}
