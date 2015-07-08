package hwz.com.myshopping.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hwz.com.myshopping.R;
import hwz.com.myshopping.activity.MyUrl;
import hwz.com.myshopping.bean.OrderListBase;
import hwz.com.myshopping.util.HttpClientApplication;

public class OrderListFragment extends BaseFragment {

	private View view;
	private String urlString= MyUrl.URLHEAD+MyUrl.ORDERLIST;
	private HttpClientApplication application;//app变量
	private OrderListBase data;
	//列表
	@InjectView(R.id.list)
	ListView list;

	//选择按钮
	@InjectView(R.id.btn_order1)
	Button btn_order1;
	//选择按钮
	@InjectView(R.id.btn_order2)
	Button btn_order2;
	//选择按钮
	@InjectView(R.id.btn_order3)
	Button btn_order3;
/**
 *  
 *  参数名称	描述	样例
	type	1/2/3	1=>近一个月订单 2=>一个月前订单 3=>已取消订单
	page	第几页	1
	pageNum	每页个数	10

 */
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
			view=inflater.inflate(R.layout.fragment_order, null);
			//奶油刀注解库
			ButterKnife.inject(this, view);
			//获取全局变量
			application=(HttpClientApplication) getActivity().getApplication();
			//初始显示数据
			String url=urlString+"type=1";
			btn_order1.setSelected(true);
			setView(url);
			//--------------------------------
			btn_order1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String url=urlString+"type=1";
					setView(url);
					btn_order1.setSelected(true);
					btn_order2.setSelected(false);
					btn_order3.setSelected(false);
				}
			});
			btn_order2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String url=urlString+"type=2";
					btn_order1.setSelected(false);
					btn_order2.setSelected(true);
					btn_order3.setSelected(false);
					setView(url);
				}
			});
			btn_order3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String url=urlString+"type=3";
					btn_order1.setSelected(false);
					btn_order2.setSelected(false);
					btn_order3.setSelected(true);
					setView(url);
				}
			});


		}
		return view;
		
	}
	
	public void setView(final String url){
		String username=application.username;
		String password=application.password;
		//------------获取服务端数据--------------------
		final String loginUrl=MyUrl.URLHEAD+MyUrl.LOGIN+"username="+username+"&&password="+password;
		final AsyncHttpClient client=new AsyncHttpClient();
		AsyncHttpResponseHandler responseHandler=new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,Throwable arg3) {

			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] bytes) {

				try {
					String result=new String(bytes,"UTF-8");
					JSONObject object=new JSONObject(result);
					String response=object.getString("response");
					if(response.equals("login")){
						AsyncHttpResponseHandler responseHandler=new AsyncHttpResponseHandler(){
							@Override
							public void onFailure(int arg0, Header[] arg1, byte[] arg2,Throwable arg3) {

							}
							@Override
							public void onSuccess(int arg0, Header[] arg1, byte[] bytes) {

								try {
									String result=new String(bytes,"UTF-8");
									System.out.println(result);
									Gson gson=new Gson();
									data=gson.fromJson(result,OrderListBase.class);
									showList(data);
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
							}
							
						};
						client.get(url,responseHandler);
					}

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		client.post(loginUrl, responseHandler);
		//--------------------------------
	}
	//显示列表
	private void showList(final OrderListBase data) {
		//适配器
		BaseAdapter adapter =new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder=null;
				if (convertView==null) {
					holder=new ViewHolder();
					convertView=View.inflate(getActivity(), R.layout.list_order,null);
					holder.txt_order=(TextView) convertView.findViewById(R.id.txt_order);
					holder.txt_money=(TextView) convertView.findViewById(R.id.txt_money);
					holder.txt_time=(TextView) convertView.findViewById(R.id.txt_time);
					holder.txt_status=(TextView) convertView.findViewById(R.id.txt_status);
					convertView.setTag(holder);
				}else{
					holder=(ViewHolder) convertView.getTag();
				}
				holder.txt_order.setText(data.orderlist.get(position).orderid);
				holder.txt_money.setText(data.orderlist.get(position).price);
				holder.txt_time.setText(data.orderlist.get(position).time);
				holder.txt_status.setText(data.orderlist.get(position).status);
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				return data.orderlist.get(position);
			}
			
			@Override
			public int getCount() {
				return data.orderlist.size();
			}
		};
		//列表添加适配器
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pisition,
					long id) {
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				OrderInfoFragment orderInfoFragment=new OrderInfoFragment();
				String orderid=data.orderlist.get(pisition).orderid;
				Bundle bundle=new Bundle();
				bundle.putString("orderid",orderid);
				orderInfoFragment.setArguments(bundle);
				ft.replace(R.id.fragmentcontent,orderInfoFragment);
				ft.addToBackStack(null);
				ft.commit();
			}
		});
	}
	class ViewHolder{
		TextView txt_order;
		TextView txt_money;
		TextView txt_time;
		TextView txt_status;
	}

}
