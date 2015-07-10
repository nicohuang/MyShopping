package hwz.com.myshopping.fragment.shoppingcart;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hwz.com.myshopping.R;
import hwz.com.myshopping.dao.CartDao;
import hwz.com.myshopping.fragment.ClassifyFragment;
import hwz.com.myshopping.fragment.LoginFragment;
import hwz.com.myshopping.fragment.checkout.CheckOutFragment;
import hwz.com.myshopping.fragment.checkout.checkinfo.CheckInfo;
import hwz.com.myshopping.model.CartInfo;
import hwz.com.myshopping.util.HttpClientApplication;

public class ShoppingCartFragment extends Fragment{
	private View view;//布局
	private List<CartInfo> list;//商品集合
	private ListView listView;//商品项列表
	private RelativeLayout nullrela;//当没有数据时显示的布局
	private RelativeLayout nonullrela;//当没有数据时显示的布局
	private int myMoney=0;//总金
	private int myCount=0;//数量
	private CartDao dao;//数据操作类
	private TextView count;//数量
	private TextView money;//金额
	private Button buy_btn;//提交按钮
	private Button btn_tobuy;
    private HttpClientApplication application;
	@Override
	public void onDestroyView() {//移除布局
		super.onDestroyView();
		if(view != null){
			ViewGroup parent=(ViewGroup) view.getParent();
			if(parent != null){
				parent.removeView(view);
			}
		}
	}
@Override
public void onStart() {
	if(application.username.equals("")){
		LoginFragment loginFragment=new LoginFragment();
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		ft.replace(R.id.fragmentcontent, loginFragment);
		ft.addToBackStack("shopping");
		ft.commit();
	}
	super.onStart();
}
	//初始化
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_shoppingcart, null);
		count=(TextView) view.findViewById(R.id.count);
		money=(TextView) view.findViewById(R.id.money);
		buy_btn=(Button) view.findViewById(R.id.buy_btn);
		application=(HttpClientApplication) getActivity().getApplication();
		//初始化控件
		nullrela=(RelativeLayout) view.findViewById(R.id.null_rela);
		nonullrela=(RelativeLayout) view.findViewById(R.id.nonull_rela);
		listView=(ListView) view.findViewById(R.id.list);
		btn_tobuy=(Button) view.findViewById(R.id.btn_tobuy);
		//获取数据库数据
		dao=new CartDao(getActivity());
		init();
		//当数据库没有数据时显示

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0,
					View arg1,int position, long arg3) {
				AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
				builder.setIcon(R.mipmap.home_classify_04);//设置对话框的图标
				builder.setTitle("删除");//设置标题
				builder.setMessage("确定要删除当前商品吗？！");
				final int id=position;
				//设置积极按钮、确定按钮
				builder.setPositiveButton("残忍删除",new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dao.delect(list.get(id).id);
						//更新数据
						init();
					}
				});
				//设置消极按钮、取消按钮
				builder.setNegativeButton("再想想", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
					}
				});
				AlertDialog alertDialog=builder.create();//通过构建器 创建对话框
				alertDialog.show();//显示对话框
				return false;
			}
		});
		//提交按钮事件
		buy_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				CheckOutFragment fragment=new CheckOutFragment();
				//bundle传递总金和数量
				CheckInfo.myCount=myCount;
                CheckInfo.myMoney=myMoney;
				ft.replace(R.id.fragmentcontent,fragment);
				ft.addToBackStack(null);
				ft.commit();

			}
		});
		btn_tobuy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				ClassifyFragment classifyFragment=new ClassifyFragment();
				ft.replace(R.id.fragmentcontent,classifyFragment);
				ft.addToBackStack(null);
				ft.commit();

			}
		});

		return view;
	}
	//初始化数据
	public void init(){
		list=dao.queryAll(application.username);
		System.out.println("todothis?!");
		//没有商品的时候显示
		if(list.size() == 0){
			nullrela.setVisibility(View.VISIBLE);
			nonullrela.setVisibility(View.GONE);
		}else{//有商品的时候显示
			nullrela.setVisibility(View.GONE);
			nonullrela.setVisibility(View.VISIBLE);
			myCount=0;
			myMoney=0;
			//获取商品的数量，总金额
			for (int i = 0; i < list.size(); i++) {
				myCount+=list.get(i).num;
				myMoney=myMoney+list.get(i).num*list.get(i).price;
			}
			count.setText(myCount+"");
			money.setText(myMoney+"");
			//设置商品列表
			BaseAdapter adapter=new BaseAdapter() {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					ViewHolder holder;
					if(convertView == null){
						holder=new ViewHolder();
						convertView=View.inflate(getActivity(),R.layout.list_shoppintcart,null);
						holder.pic=(ImageView) convertView.findViewById(R.id.pic);
						holder.name=(TextView) convertView.findViewById(R.id.txt_name);
						holder.count=(TextView) convertView.findViewById(R.id.count);
						holder.price=(TextView) convertView.findViewById(R.id.price);
						holder.money=(TextView) convertView.findViewById(R.id.money);
						convertView.setTag(holder);
					}else{
						holder=(ViewHolder)convertView.getTag();
					}
					//设置控件数据
					holder.name.setText(list.get(position).name);
					holder.count.setText(list.get(position).num+"");
					holder.price.setText(list.get(position).price+"");
					int num=list.get(position).num*list.get(position).price;
					holder.money.setText(num+"");
					return convertView;
				}
				@Override
				public long getItemId(int position) {
					return list.get(position).id;
				}
				@Override
				public Object getItem(int position) {
					return list.get(position);
				}
				@Override
				public int getCount() {
					return list.size();
				}
			};
			listView.setAdapter(adapter);
		}
	}
	//列表绑定类
	class ViewHolder{
		ImageView pic;
		TextView name;
		TextView count;
		TextView price;
		TextView money;
	}
}
