package hwz.com.myshopping.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jan on 15/7/10.
 */
public class SQLOpenHelper extends SQLiteOpenHelper
{
    private static int version=1;//数据库版本

    //创建数据库
    public SQLOpenHelper(Context context)
    {
        super(context,"shopping", null,version);
    }
    //创建表
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //购物车
        String sql="create table cart(id integer primary key autoincrement,pudid test,inventory test,name test,num integer,url test,price test,username test);";
        db.execSQL(sql);
        //收货地址
        String address="create table address(id integer primary key autoincrement," +
                "name test,phonenumber test,fixedtel test,areaid test,areadetail test,zipcode test,username test);";
        db.execSQL(address);
        //搜索历史
        String search_history="create table shistory(id integer primary key autoincrement,name test);";
        db.execSQL(search_history);
        //搜索提示
        String search_hint="create table shint(id integer primary key autoincrement,name test);";
        db.execSQL(search_hint);
    }
    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //购物车
        db.execSQL("drop table if exists cart");
        String sql="create table cart(id integer primary key autoincrement,pudid test,inventory test,name test,num integer,url test,price test,username test);";
        db.execSQL(sql);
        //收货地址
        db.execSQL("drop table if exists address");
        String address="create table address(id integer primary key autoincrement," +
                "name test,phonenumber test,fixedtel test,areaid test,areadetail test,zipcode test,username test);";
        db.execSQL(address);

        //搜索历史
        db.execSQL("drop table if exists shistory");
        String search_history="create table shistory(id integer primary key autoincrement,name test);";
        db.execSQL(search_history);
        //搜索提示
        db.execSQL("drop table if exists shint");
        String search_hint="create table shint(id integer primary key autoincrement,name test);";
        db.execSQL(search_hint);
    }
}
