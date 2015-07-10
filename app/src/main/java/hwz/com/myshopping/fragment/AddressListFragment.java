package hwz.com.myshopping.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import hwz.com.myshopping.R;
import hwz.com.myshopping.activity.MyUrl;
import hwz.com.myshopping.model.AddressInfo;
import hwz.com.myshopping.dao.CartDao;
import hwz.com.myshopping.util.HttpClientApplication;

public class AddressListFragment extends BaseFragment {

	private ListView list;
	private View view;
	private Button select;
	private Button add;
	private CartDao cartDao;
	private HttpClientApplication application;
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(view !=null){
			ViewGroup parent=(ViewGroup) view.getParent();
			if(parent != null){
				parent.removeView(view);
			}
		}
	}
	public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view ==null){
			view=inflater.inflate(R.layout.fragment_addresslist, null);
			application=(HttpClientApplication) getActivity().getApplication();
			list=(ListView) view.findViewById(R.id.list);
			select=(Button) view.findViewById(R.id.btn_select);
			add=(Button) view.findViewById(R.id.btn_add);
			cartDao=new CartDao(getActivity());
			//获取全局变量
			HttpClientApplication application=(HttpClientApplication) getActivity().getApplication();
			String username=application.username;
			String password=application.password;
			//------------获取服务端数据--------------------
			final String url= MyUrl.URLHEAD+MyUrl.LOGIN+"username="+username+"&&password="+password;
			final AsyncHttpClient client=new AsyncHttpClient();
			//------------------请求1-----------------
			AsyncHttpResponseHandler responseHandler=new AsyncHttpResponseHandler(){

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
                {

                }


                @Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

					try {
						String result=new String(responseBody,"UTF-8");
						JSONObject object=new JSONObject(result);
						String response=object.getString("response");
						if(response.equals("login")){
							List<AddressInfo> info=cartDao.selectAddress();
							showView(info);
						}

					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}catch (JSONException e) {
						e.printStackTrace();
					}
				}
			};
			client.post(url, responseHandler);
			//--------------------------------
		}
		return view;
	}
	//--------------------------------显示数据---------------------
	public void myAdapter(final List<AddressInfo> info){
		
		BaseAdapter adapter=new BaseAdapter() {		
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder =null;
				if(convertView==null){
					convertView=View.inflate(getActivity(),R.layout.list_addresslist, null);
					holder=new ViewHolder();
					holder.txt_name=(TextView) convertView.findViewById(R.id.txt_name);
					holder.txt_phone=(TextView) convertView.findViewById(R.id.txt_phone);
					holder.txt_address=(TextView) convertView.findViewById(R.id.txt_address);
					holder.image_select=(ImageView) convertView.findViewById(R.id.image_select);
					convertView.setTag(holder);
				}else {
					holder=	(ViewHolder) convertView.getTag();
				}
				holder.txt_name.setText(info.get(position).name);
				holder.txt_phone.setText(info.get(position).phonenumber);
				holder.txt_address.setText(info.get(position).areaid+info.get(position).areadetail);
				holder.image_select.setTag(position);
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return info.get(position);
			}

			@Override
			public int getCount() {

				return info.size();
			}
		};
		list.setAdapter(adapter);
	}
	private void showView(final List<AddressInfo> info) {
		//显示列表
		myAdapter(info);
		//长按删除地址
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
				builder.setIcon(R.mipmap.dialog_image);
				builder.setTitle("删除");
				builder.setMessage("确定要删除地址吗？");
				final int id=position;
				builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						cartDao.delectAddress(info.get(id).id);
						List<AddressInfo> info=cartDao.selectAddress();
						myAdapter(info);
					}
				});
				builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getActivity(),"取消",Toast.LENGTH_SHORT).show();
					}
				});
				//通过构建器 创建对话框
				AlertDialog alertDialog=builder.create();
				alertDialog.show();
				
				return false;
			}
		});
		//选择地址
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				for (int i = 0; i < info.size(); i++) {
					((ImageView)list.findViewWithTag(i)).setVisibility(View.INVISIBLE);
				}
				ImageView imageView=(ImageView) list.findViewWithTag(position);
				imageView.setVisibility(View.VISIBLE);
				
				application.name=info.get(position).name;
				application.phone=info.get(position).phonenumber;
				application.address=info.get(position).areadetail;
				
			}
		});
		if(application.addressid==1){
			select.setVisibility(View.VISIBLE);
		}else{
			select.setVisibility(View.INVISIBLE);
		}
		//选择地址
		select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				CheckOutFragment checkOutFragment=new CheckOutFragment();
				ft.replace(R.id.fragmentcontent,checkOutFragment);
				ft.commit();
			}
		});
		//添加地址
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				AddAddressFragment addfragment=new AddAddressFragment();
				ft.replace(R.id.fragmentcontent,addfragment);
				ft.addToBackStack(null);
				ft.commit();
			}
		});


	}
	class ViewHolder{
		TextView txt_name;
		TextView txt_phone;
		TextView txt_address;
		ImageView image_select;
	}

}
