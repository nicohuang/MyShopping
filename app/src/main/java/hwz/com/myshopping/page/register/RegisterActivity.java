package hwz.com.myshopping.page.register;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hwz.com.myshopping.R;
import hwz.com.myshopping.activity.MyUrl;

public class RegisterActivity extends Activity
{

    private String response;
    //注册按钮
    @InjectView(R.id.myregister)
    private Button myRegister;
    //输入用户名
    @InjectView(R.id.account_edt)
    EditText name;
    //输入密码
    @InjectView(R.id.pwd_edt)
    EditText pwd;
    //输入确认密码
    @InjectView(R.id.repwd_edt)
    EditText repwd;

    private String url = MyUrl.URLHEAD + MyUrl.REGISTER;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState)
    {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.fragment_register);
        ButterKnife.inject(this);
        //注册
        myRegister.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String myname = name.getText().toString().trim();
                final String mypwd = pwd.getText().toString().trim();
                final String myrepwd = repwd.getText().toString().trim();
                //判断密码是否一致,用户名密码不为空
                if (myname.equals(""))
                {
                    Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                }
                else if (mypwd.equals(""))
                {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                else if (!mypwd.equals(myrepwd))
                {
                    Toast.makeText(RegisterActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //处理登陆请求
                    AsyncHttpClient client = new AsyncHttpClient();

                    AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler()
                    {

                        @Override
                        public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
                        {
                            Toast.makeText(RegisterActivity.this, "网络失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int arg0, Header[] arg1, byte[] bytes)
                        {
                            try
                            {
                                String result = new String(bytes, "UTF-8");
                                JSONObject obj;
                                obj = new JSONObject(result);
                                //使用org.json解析json数据
                                response = obj.getString("response");//获取返回参数，判断是否登陆成功
                                if (response.equals("register"))
                                {
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    //跳转到登陆界面
                                    finish();
                                    //获取用户信息
                                } else
                                {
                                    Toast.makeText(RegisterActivity.this, "注册失败，请换个用户名", Toast.LENGTH_SHORT).show();
                                }
                            } catch (UnsupportedEncodingException e)
                            {
                                e.printStackTrace();
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    };
                    RequestParams params = new RequestParams();
                    params.put("username", myname);
                    params.put("password", mypwd);
                    client.post(url, params, responseHandler);
                }
            }
        });
    }

}

