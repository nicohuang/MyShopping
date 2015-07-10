package hwz.com.myshopping.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import hwz.com.myshopping.R;
import hwz.com.myshopping.fragment.address.AddressListFragment;
import hwz.com.myshopping.util.HttpClientApplication;

public class MoreFragment extends Fragment{
	private View view;
	private String[] item=new String[]{"登陆","注册","我的订单","注销登陆","收货地址","帮助中心"}; 
	private ListView listView;
	private HttpClientApplication application;
	//找到显示控件
	TextView user_txt;
	TextView ordercount_txt;
	TextView favoritescount_txt;

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(view != null){
			ViewGroup parent=(ViewGroup) view.getParent();
			if(parent != null){
				parent.removeView(view);
			}
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		//显示用户信息
		if(!application.username.equals("")){
			//设置显示控件
			user_txt.setText(application.username);
			ordercount_txt.setText(application.ordercount);
			favoritescount_txt.setText(application.favoritescount);
		}
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//显示用户信息
		if(!application.username.equals("")){
			//设置显示控件
			user_txt.setText(application.username);
			ordercount_txt.setText(application.ordercount);
			favoritescount_txt.setText(application.favoritescount);
		}else{
			user_txt.setText("请登陆");
			ordercount_txt.setText("");
			favoritescount_txt.setText("");
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view == null){
			view=inflater.inflate(R.layout.fragment_more, null);
			application=(HttpClientApplication) getActivity().getApplication();
			//找到显示控件
			user_txt=(TextView) view.findViewById(R.id.user_txt);
			ordercount_txt=(TextView) view.findViewById(R.id.ordercount);
			favoritescount_txt=(TextView) view.findViewById(R.id.favoritescount);

			//列表
			listView=(ListView) view.findViewById(R.id.list);
			//列表适配器
			ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), R.layout.list_more,item){
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					ViewHolder holder;
					if(convertView ==null){
						holder=new ViewHolder();
						convertView=View.inflate(getActivity(), R.layout.list_more,null);
						holder.txt_tag=(TextView) convertView.findViewById(R.id.txt_tag);
						convertView.setTag(holder);
					}else {
						holder=(ViewHolder) convertView.getTag();
					}

					holder.txt_tag.setText(item[position]);
					return convertView;
				}
			};
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,int position, long id) {
					switch (position) {
					case 0:
						//登陆
						FragmentTransaction ft0=getFragmentManager().beginTransaction();
						LoginFragment loginFragment=new LoginFragment();
						ft0.replace(R.id.fragmentcontent, loginFragment);
						ft0.addToBackStack(null);
						ft0.commit();
						break;
					case 1:
						//注册
						FragmentTransaction ft1=getFragmentManager().beginTransaction();
						RegisterFragment registerFragment=new RegisterFragment();
						ft1.replace(R.id.fragmentcontent, registerFragment);
						ft1.addToBackStack(null);
						ft1.commit();
						break;
					case 2:
						//我的订单
						if(application.username.equals("")){
							Toast.makeText(getActivity(),"请登陆", Toast.LENGTH_SHORT).show();
						}else{
							FragmentTransaction ft2=getFragmentManager().beginTransaction();
							OrderListFragment orderMentFragment=new OrderListFragment();
							ft2.replace(R.id.fragmentcontent,orderMentFragment);
							ft2.addToBackStack(null);
							ft2.commit();
						}
						break;
					case 3:
						if(!application.username.equals("")){
							//注销登陆
							AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
							builder.setIcon(R.mipmap.home_classify_04);//设置对话框的图标
							builder.setTitle("注销");//设置标题
							builder.setMessage("确定要注销登陆吗？！");
							//设置积极按钮、确定按钮
							builder.setPositiveButton("注销",new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									application.username="";
									application.password="";
									user_txt.setText("请登陆");
									ordercount_txt.setText("");
									favoritescount_txt.setText("");
								}
							});
							//设置消极按钮、取消按钮
							builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
								}
							});
							AlertDialog alertDialog=builder.create();//通过构建器 创建对话框
							alertDialog.show();//显示对话框
						}else{
							Toast.makeText(getActivity(),"未登陆！！",Toast.LENGTH_SHORT).show();
						}
						break;
					case 4:
						//收货地址
						if(application.username.equals("")){
							Toast.makeText(getActivity(),"请登陆", Toast.LENGTH_SHORT).show();
						}else{
							FragmentTransaction ft4=getFragmentManager().beginTransaction();
							AddressListFragment addressListFragment=new AddressListFragment();
							application.addressid=2;
							ft4.replace(R.id.fragmentcontent, addressListFragment);
							ft4.addToBackStack(null);
							ft4.commit();
						}
						break;
					case 5:
						//帮助中心
						break;
					default:
						break;
					}
				}
			});
			//获取全局数据
		}
		return view;
	}
	class ViewHolder{
		TextView txt_tag;
	}
}


