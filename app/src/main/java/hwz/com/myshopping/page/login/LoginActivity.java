package hwz.com.myshopping.page.login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import hwz.com.myshopping.page.register.RegisterActivity;
import hwz.com.myshopping.util.HttpClientApplication;

/**
 * 登陆
 */
public class LoginActivity extends Activity
{

	private String response="";
    //登陆按钮
    @InjectView(R.id.imageButton1)
	private ImageButton myLogin;
    //注册按钮
    @InjectView(R.id.imageButton2)
	private ImageButton myRegister;
    //用户名
    @InjectView(R.id.account_edt)
    EditText name;
    //密码
    @InjectView(R.id.pwd_edt)
    EditText pwd;
    //url
	private String url= MyUrl.URLHEAD+MyUrl.LOGIN;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState)
    {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.fragment_login);
        ButterKnife.inject(this);
        //登陆按钮
        myLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String myname=name.getText().toString().trim();
                final String mypwd=pwd.getText().toString().trim();
                final HttpClientApplication application=(HttpClientApplication) getApplication();
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
                                                finish();
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
                                Toast.makeText(LoginActivity.this,"登陆失败，请验证用户名密码",Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}

