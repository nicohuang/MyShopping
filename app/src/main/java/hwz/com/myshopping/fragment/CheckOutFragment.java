package hwz.com.myshopping.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hwz.com.myshopping.R;
import hwz.com.myshopping.activity.MyUrl;
import hwz.com.myshopping.model.CartInfo;
import hwz.com.myshopping.dao.CartDao;
import hwz.com.myshopping.util.HttpClientApplication;

public class CheckOutFragment extends Fragment
{
    private View view;
    //收货地址
    @InjectView(R.id.txt_name)
    TextView txt_name;
    @InjectView(R.id.txt_phone)
    TextView txt_phone;
    @InjectView(R.id.txt_address)
    TextView txt_address;
    @InjectView(R.id.relative_name)
    RelativeLayout relative_name;
    //支付方式
    @InjectView(R.id.txt_howcheckout)
    TextView txt_howcheckout;
    @InjectView(R.id.relative_checktype)
    RelativeLayout relative_checktype;
    //送货方式
    @InjectView(R.id.txt_send)
    TextView txt_send;
    //送货时间
    @InjectView(R.id.txt_delivery)
    TextView txt_delivery;
    @InjectView(R.id.relative_delivery)
    RelativeLayout relative_delivery;
    //索取发票
    @InjectView(R.id.txt_invoice)
    TextView txt_invoice;
    @InjectView(R.id.relative_invoice)
    RelativeLayout relative_invoice;
    //商品数量
    @InjectView(R.id.txt_count)
    TextView txt_count;
    //付款金额
    @InjectView(R.id.txt_money)
    TextView txt_money;
    //结账按钮
    @InjectView(R.id.btn_checkout)
    TextView btn_checkout;

    //bundle传递数据
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (view != null)
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
            {
                parent.removeView(view);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_checkout, null);
            //奶油刀注解库
            ButterKnife.inject(this, view);
            //获取购货车传递过来的数据

            final HttpClientApplication application = (HttpClientApplication) getActivity().getApplication();
            //显示地址信息
            txt_name.setText(application.name);
            txt_phone.setText(application.phone);
            txt_address.setText(application.address);
            //显示支付方式
            txt_howcheckout.setText(application.paytype);
            //显示送货时间
            txt_delivery.setText(application.deliverytime);
            //显示订单信息
            txt_count.setText(application.myCount + "");
            txt_money.setText(application.myMoney + "");
            //索取发票
            txt_invoice.setText(application.invoiceinfo + "");
            //获取地址
            relative_name.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    AddressListFragment addressListFragment = new AddressListFragment();
                    ft.replace(R.id.fragmentcontent, addressListFragment);
                    application.addressid = 1;
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            //获取支付方式
            relative_checktype.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    PayMentFragment payMentFragment = new PayMentFragment();
                    application.addressid = 1;
                    ft.replace(R.id.fragmentcontent, payMentFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            //送货时间
            relative_delivery.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    DeliveryFragment deliveryFragment = new DeliveryFragment();
                    ft.replace(R.id.fragmentcontent, deliveryFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            //发票类型
            relative_invoice.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    InvoiceFragment invoiceFragment = new InvoiceFragment();
                    ft.replace(R.id.fragmentcontent, invoiceFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            btn_checkout.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //获取全局变量
                    String username = application.username;
                    String password = application.password;
                    if (application.address.equals(""))
                    {
                        Toast.makeText(getActivity(), "请选择地址", Toast.LENGTH_SHORT).show();
                    } else if (application.deliverytime.equals(""))
                    {
                        Toast.makeText(getActivity(), "请选择收件时间", Toast.LENGTH_SHORT).show();
                    } else if (application.paytype.equals(""))
                    {
                        Toast.makeText(getActivity(), "请选择支付方式", Toast.LENGTH_SHORT).show();
                    } else if (application.invoiceinfo.equals(""))
                    {
                        Toast.makeText(getActivity(), "请选择发票类型", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        //------------获取服务端数据--------------------
                        final String url = MyUrl.URLHEAD + MyUrl.LOGIN + "username=" + username + "&&password=" + password;
                        final AsyncHttpClient client = new AsyncHttpClient();
                        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler()
                        {
                            @Override
                            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
                            {

                            }

                            @Override
                            public void onSuccess(int arg0, Header[] arg1, byte[] bytes)
                            {

                                final CartDao dao = new CartDao(getActivity());
                                try
                                {
                                    String result = new String(bytes, "UTF-8");
                                    JSONObject object = new JSONObject(result);
                                    String response = object.getString("response");
                                    if (response.equals("login"))
                                    {
                                        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler()
                                        {
                                            @Override
                                            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
                                            {

                                            }

                                            @Override
                                            public void onSuccess(int arg0, Header[] arg1, byte[] bytes)
                                            {

                                                try
                                                {
                                                    String result = new String(bytes, "UTF-8");
                                                    System.out.println(result);
                                                    //返回参数{"response":"orderdetail","orderinfo":{"orderid":"1112111111","paymenttype":"1","price":"230"}}
                                                    JSONObject object = new JSONObject(result);
                                                    //获取返回参数
                                                    String response = object.getString("response");
                                                    String orderinfo = object.getString("orderinfo");
                                                    //获取订单信息
                                                    JSONObject object2 = new JSONObject(orderinfo);
                                                    String orderid = object2.getString("orderid");
                                                    application.orderid = orderid;
                                                    //判断时候提交订成功
                                                    if (response.equals("orderdetail"))
                                                    {
                                                        dao.delect(application.username);
                                                        Toast.makeText(getActivity(), "购买成功", Toast.LENGTH_SHORT).show();
                                                        //跳转到购物车
                                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                        CheckOutSuccessFragment success = new CheckOutSuccessFragment();
                                                        ft.replace(R.id.fragmentcontent, success);
                                                        ft.commit();
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
                                        RequestParams form = new RequestParams();
                                        List<CartInfo> info = dao.queryAll(application.username);
                                        String shopping = "";
                                        for (int i = 0; i < info.size(); i++)
                                        {
                                            CartInfo cartInfo = info.get(i);
                                            shopping = shopping + cartInfo.pudid + ":" + cartInfo.num;
                                            if (i < (info.size() - 1))
                                            {
                                                shopping = shopping + "|";
                                            }
                                        }
                                        System.out.println(shopping);
                                        //商品ID:数量|商品ID:数量
                                        form.put("sku", shopping);
                                        //地址簿ID
                                        form.put("addressid", application.addressid);
                                        //支付方式
                                        form.put("paymentid", application.payid);
                                        //送货时间
                                        form.put("deliveryid", application.deliveryid);
                                        //发票类型
                                        form.put("invoicetype", application.invoiceid);
                                        //发票标题
                                        form.put("invoicetitle", "毕业设计");
                                        //发票内容
                                        form.put("invoicecontent", 1);
                                        String urlString = MyUrl.URLHEAD + MyUrl.ORDERSUMBIT;
                                        client.post(urlString, form, responseHandler);
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
                        client.post(url, responseHandler);
                        //--------------------------------
                    }
                }
            });

        }
        return view;
    }
}


