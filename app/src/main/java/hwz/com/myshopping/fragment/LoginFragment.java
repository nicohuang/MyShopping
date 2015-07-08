package hwz.com.myshopping.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import hwz.com.myshopping.R;
import hwz.com.myshopping.activity.MyUrl;
import hwz.com.myshopping.util.HttpClientApplication;

public class LoginFragment extends Fragment {
	/**
	 * 定义一个字段保存sessionid
	 */

	private View view;//布局
	private String response="";
	private ImageButton myLogin;
	private ImageButton myRegister;
	private String url= MyUrl.URLHEAD+MyUrl.LOGIN;

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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view==null){
			view=inflater.inflate(R.layout.fragment_login, null);
			myLogin=(ImageButton) view.findViewById(R.id.imageButton1);
			myRegister=(ImageButton) view.findViewById(R.id.imageButton2);
			final EditText name=(EditText) view.findViewById(R.id.account_edt);
			final EditText pwd=(EditText) view.findViewById(R.id.pwd_edt);
			//登陆按钮
			myLogin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final String myname=name.getText().toString().trim();
					final String mypwd=pwd.getText().toString().trim();
					final HttpClientApplication application=(HttpClientApplication) getActivity().getApplication();
					//处理登陆请求
					final AsyncHttpClient client=new AsyncHttpClient();
					AsyncHttpResponseHandler responseHandler=new AsyncHttpResponseHandler(){
						@Override
						public void onFailure(int arg0, Header[] arg1,byte[] arg2, Throwable arg3) {

						}
						@Override
						public void onSuccess(int arg0, Header[] arg1,byte[] bytes) {

							try {
								String result=new String(bytes,"UTF-8");
								JSONObject obj;
								obj = new JSONObject(result);
								//使用org.json解析json数据
								response=obj.getString("response");//获取返回参数，判断是否登陆成功
								if(response.equals("login")){
									application.username=myname;
									application.password=mypwd;
									//获取用户信息
									AsyncHttpResponseHandler responseHandler1=new AsyncHttpResponseHandler(){

										@Override
										public void onFailure(int arg0, Header[] arg1,byte[] arg2, Throwable arg3) {

										}
										@Override
										public void onSuccess(int arg0, Header[] arg1,byte[] bytes) {

											try {
												String result=new String(bytes,"UTF-8");
												JSONObject obj= new JSONObject(result);
												//	
												String info=obj.getString("response");//获取返回参数，判断是否登陆成功
												//登陆成功
												if(info.equals("userinfo")){
													String userinfo=obj.getString("userinfo");
													JSONObject object=new JSONObject(userinfo);
													application.ordercount=object.getString("ordercount");
													application.favoritescount=object.getString("favoritescount");
													//跳转到用户界面
													FragmentTransaction ft=getFragmentManager().beginTransaction();
													MoreFragment moreFragment=new MoreFragment();
													ft.replace(R.id.fragmentcontent,moreFragment);
													ft.commit();
												}
											} catch (UnsupportedEncodingException e) {
												e.printStackTrace();
											}catch (JSONException e) {
												e.printStackTrace();
											}
										}
									};
									client.get(MyUrl.URLHEAD+MyUrl.USERINFO, responseHandler1);
									
									
								}else{
									Toast.makeText(getActivity(),"登陆失败，请验证用户名密码",Toast.LENGTH_SHORT).show();
								}
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}catch (JSONException e) {
								e.printStackTrace();
							}
						}
					};
					String urlString=url+"username="+myname+"&&password="+mypwd;
					client.post(urlString,responseHandler);
				}
			});
			//注册按钮
			myRegister.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FragmentTransaction ft=getFragmentManager().beginTransaction();
					RegisterFragment fragment=new RegisterFragment();
					ft.replace(R.id.fragmentcontent, fragment);
					ft.addToBackStack(null);
					ft.commit();
				}
			});
		}
		return view;
	}
}

