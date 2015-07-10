package hwz.com.myshopping.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jan on 15/7/10.
 */
public class SearchDao
{
    private Context context;
    private SQLOpenHelper mMyOpenHelper;
    public SearchDao(Context context)
    {
        this.context = context;
        mMyOpenHelper=new SQLOpenHelper(context);
    }

    /**
     * 搜索历史列表
     * @return 搜索列表集合
     */
    public List<String> selecthistory(){
        SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
        List<String> list=new ArrayList<String>();
        //游标，返回的结果集，默认指向第一行以上的位置
        Cursor cursor=db.rawQuery("select * from shistory",new String[]{});
        while (cursor.moveToNext()) {//移动数据的选中位置
            String name=cursor.getString(1);
            list.add(name);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 删除搜索历史
     */
    public void delecthistory(){
        SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
        db.execSQL("delete from shistory;");
        db.close();
    }

    /**
     * 添加搜索历史
     * @param nameString 搜索内容
     */
    public void inserhistory(String nameString) {
        SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
        db.execSQL("insert into shistory(name) values (?);",new String[]{nameString});
        db.close();
    }

    /**
     * 搜索提示
     * @param nameString
     * @return
     */
    public List<String> selecthhint(String nameString){
        SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
        List<String> list=new ArrayList<String>();
        //游标，返回的结果集，默认指向第一行以上的位置
        Cursor cursor=db.rawQuery("select * from shint where name like ?;",new String[]{"'%"+nameString+"%'"});
        while (cursor.moveToNext()) {//移动数据的选中位置
            String name=cursor.getString(1);
            list.add(name);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     *添加搜索提示
     */
    public void hintinser() {
        SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
        db.execSQL("insert into shint(name) values (?);",new String[]{"奶粉"});
        db.close();
    }
}
