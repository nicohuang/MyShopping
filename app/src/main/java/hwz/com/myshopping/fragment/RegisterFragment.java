package hwz.com.myshopping.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import hwz.com.myshopping.R;
import hwz.com.myshopping.activity.MyUrl;

public class RegisterFragment extends Fragment {

	private View view;//布局
	private String response;
	private Button myRegister;
	private String url= MyUrl.URLHEAD+MyUrl.REGISTER;
	//"username=xiaowen&&password=123456";

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
			view=inflater.inflate(R.layout.fragment_register, null);
			myRegister=(Button) view.findViewById(R.id.myregister);
			final EditText name=(EditText) view.findViewById(R.id.account_edt);
			final EditText pwd=(EditText) view.findViewById(R.id.pwd_edt);
			final EditText repwd=(EditText) view.findViewById(R.id.repwd_edt);
			//按钮
			myRegister.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final String myname=name.getText().toString().trim();
					final String mypwd=pwd.getText().toString().trim();
					final String myrepwd=repwd.getText().toString().trim();
					//判断密码是否一致,用户名密码不为空
					if(myname.equals("")){
						Toast.makeText(getActivity(),"用户名不能为空",Toast.LENGTH_SHORT).show();
					}else if(mypwd.equals("")){
						Toast.makeText(getActivity(), "密码不能为空",Toast.LENGTH_SHORT).show();
					}else if(!mypwd.equals(myrepwd)){
						System.out.println(mypwd+"---"+myrepwd);
						Toast.makeText(getActivity(), "密码不一致",Toast.LENGTH_SHORT).show();
					}else{
						//处理登陆请求
						AsyncHttpClient client=new AsyncHttpClient();

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
									if(response.equals("register")){
										Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
										//跳转到登陆界面
										FragmentTransaction ft=getFragmentManager().beginTransaction();
										LoginFragment loginFragment=new LoginFragment();
										ft.replace(R.id.fragmentcontent,loginFragment);
										ft.commit();
										//获取用户信息								
									}else{
										Toast.makeText(getActivity(),"注册失败，请换个用户名",Toast.LENGTH_SHORT).show();
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
				}
			});
		}
		return view;
	}
}

