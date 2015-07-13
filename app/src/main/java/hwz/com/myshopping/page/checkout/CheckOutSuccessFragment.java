package hwz.com.myshopping.page.checkout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hwz.com.myshopping.R;
import hwz.com.myshopping.page.main.MainFragment;
import hwz.com.myshopping.util.HttpClientApplication;

public class CheckOutSuccessFragment extends Fragment{
	private View view;
	//收货地址
	@InjectView(R.id.txt_order)
	TextView txt_order;
	@InjectView(R.id.txt_money)
	TextView txt_money;
	@InjectView(R.id.txt_payment)
	TextView txt_payment;
	@InjectView(R.id.btn_tobuy)
	TextView btn_tobuy;
	
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view ==null){
			view=inflater.inflate(R.layout.fragment_checkoutsuccess, null);
			//奶油刀注解库
			ButterKnife.inject(this, view);
			HttpClientApplication application=(HttpClientApplication) getActivity().getApplication();
			//设置显示数据
			txt_order.setText(application.orderid);
			txt_money.setText(application.myMoney+"");
			txt_payment.setText(application.paytype);
			//清空订单信息
			application.orderid="";
			application.myMoney=0;
			application.paytype="";
			btn_tobuy.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					FragmentTransaction ft=getFragmentManager().beginTransaction();
					MainFragment mainFragment=new MainFragment();
					ft.replace(R.id.fragmentcontent,mainFragment);
					ft.commit();
				}
			});
		}
		return view;
	}
}


