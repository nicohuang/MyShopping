package hwz.com.myshopping.util;

import android.app.Application;

import com.example.shopping.fragment.InvoiceFragment;
import com.loopj.android.http.AsyncHttpClient;

public class HttpClientApplication extends Application {
	public String username="";//用户名
	public String password="";//密码
	public String ordercount="";//订单数
	public String favoritescount="";//收藏数
	public int myCount=0;//购买数量
	public int myMoney=0;//需付金额
	//----------订单地址-----------
	public int addressid;
	public String name="";
	public String phone="";
	public String address="";
	//支付方式
	public int payid=1;
	public String paytype="";
	//送货时间
	public int deliveryid=1;
	public String deliverytime="";
	//发票类型
	public int invoiceid=1;
	public String invoiceinfo="";
	public String orderid="";
	
	
	
}
