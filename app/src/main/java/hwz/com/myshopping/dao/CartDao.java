package hwz.com.myshopping.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hwz.com.myshopping.model.AddressInfo;
import hwz.com.myshopping.model.CartInfo;

public class CartDao {
	private Context context;//上下文
	private int mVersion=9;
	private SQLOpenHelper mMyOpenHelper;

	public CartDao(Context context) {
		super();
		this.context = context;
		mMyOpenHelper=new SQLOpenHelper(context);
	}

	//--------------------购物车-------------------------
	public void inser(CartInfo info,String username) {
		SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
		db.execSQL("insert into cart(pudid,inventory,name,num,url,price,username) values (?,?,?,?,?,?,?);",new String[]{info.pudid,info.inventory,info.name,info.num+"",info.url,info.price+"",username});
		db.close();		
	}
	public List<CartInfo> queryAll(String username){
		SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
		List<CartInfo> list=new ArrayList<CartInfo>();
		//游标，返回的结果集，默认指向第一行以上的位置
		Cursor cursor=db.rawQuery("select * from cart where username=?",new String[]{username});
		while (cursor.moveToNext()) {//移动数据的选中位置
			CartInfo info = new CartInfo();	
			info.id=cursor.getInt(0);
			info.pudid=cursor.getString(1);
			info.inventory=cursor.getString(2);
			info.name=cursor.getString(3);
			info.num=cursor.getInt(4);
			info.url=cursor.getString(5);
			info.price=cursor.getInt(6);
			list.add(info);
		}
		cursor.close();
		db.close();
		return list;
	}
	//删除操作
	public void delect(String username){
		SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
		db.execSQL("delete from cart where username=?;",new String []{username});
		db.close();
	}
	//删除操作
	public void delect(int id){
		SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
		db.execSQL("delete from cart where id=?;",new String []{id+""});
		db.close();
	}
	//更新操作
	public void update(String name,int num){
		SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
		db.execSQL("update cart set num=? where name=?);",new String[]{num+"",name});
		db.close();
	}
	//------------------收货地址-----------------------
	//保存地址
	public void saveAddress(AddressInfo info) {
		SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
		db.execSQL("insert into address(name,phonenumber,fixedtel,areaid,areadetail,zipcode,username) values (?,?,?,?,?,?,?);",
				new String[]{info.name,info.phonenumber,info.fixedtel,info.areaid,info.areadetail,info.zipcode,info.username});
		db.close();		
	}
	//显示地址
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
	//删除地址
	public void delectAddress(int id){
		SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
		db.execSQL("delete from address where id=?;",new String []{id+""});
		db.close();
	}
	//---------------------搜索历史--------------------
	//获取搜索历史
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
	//删除历史
	public void delecthistory(){
		SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
		db.execSQL("delete from shistory;");
		db.close();
	}
	public void inserhistory(String nameString) {
		SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
		db.execSQL("insert into shistory(name) values (?);",new String[]{nameString});
		db.close();		
	}
	//---------------------搜索提示--------------------
	//获取搜索提示
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
		public void hintinser() {
			SQLiteDatabase db=mMyOpenHelper.getWritableDatabase();
			db.execSQL("insert into shint(name) values (?);",new String[]{"奶粉"});
			db.close();		
		}
}
