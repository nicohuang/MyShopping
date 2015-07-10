package hwz.com.myshopping.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hwz.com.myshopping.model.AddressInfo;

/**
 * Created by jan on 15/7/10.
 */
public class AddressDao
{
    private Context context;
    private SQLOpenHelper mMyOpenHelper;
    public AddressDao(Context context)
    {
        this.context = context;
        mMyOpenHelper=new SQLOpenHelper(context);
    }

    /**
     * 保存收货地址
     * @param info 地址信息
     */
    public void saveAddress(AddressInfo info) {
        SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
        db.execSQL("insert into address(name,phonenumber,fixedtel,areaid,areadetail,zipcode,username) values (?,?,?,?,?,?,?);",
                new String[]{info.name,info.phonenumber,info.fixedtel,info.areaid,info.areadetail,info.zipcode,info.username});
        db.close();
    }

    /**
     * 获取收货地址
     * @return 收货地址集合
     */
    public List<AddressInfo> selectAddress(){
        SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
        List<AddressInfo> list=new ArrayList<AddressInfo>();
        //游标，返回的结果集，默认指向第一行以上的位置
        Cursor cursor=db.rawQuery("select * from address",new String[]{});
        while (cursor.moveToNext()) {//移动数据的选中位置
            AddressInfo info = new AddressInfo();
            info.id=cursor.getInt(0);
            info.name=cursor.getString(1);
            info.phonenumber=cursor.getString(2);
            info.fixedtel=cursor.getString(3);
            info.areaid=cursor.getString(4);
            info.areadetail=cursor.getString(5);
            info.zipcode=cursor.getString(6);
            info.username=cursor.getString(7);
            list.add(info);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 删除收货地址
     * @param id 地址id
     */
    public void delectAddress(int id){
        SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
        db.execSQL("delete from address where id=?;",new String []{id+""});
        db.close();
    }

}
