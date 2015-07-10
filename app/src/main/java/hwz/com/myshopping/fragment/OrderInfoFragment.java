package hwz.com.myshopping.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

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
import hwz.com.myshopping.model.OrderListBase;
import hwz.com.myshopping.util.HttpClientApplication;

public class OrderInfoFragment extends BaseFragment {

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
			application=(HttpClientApplication) getActivity().getApplication();
			//奶油刀注解库
			ButterKnife.inject(this, view);
			Bundle bundle=getArguments();
			String orderid=bundle.getString("orderid");
			//初始显示数据
			String url=MyUrl.URLHEAD+MyUrl.ORDERDETAIL+orderid;
			setView(url);
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

								System.out.println("失败");
							}
							@Override
							public void onSuccess(int arg0, Header[] arg1, byte[] bytes) {

								try {
									String result=new String(bytes,"UTF-8");
									System.out.println("result:"+result);
									
								
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
}
