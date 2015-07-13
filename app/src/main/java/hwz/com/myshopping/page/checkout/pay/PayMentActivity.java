package hwz.com.myshopping.page.checkout.pay;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hwz.com.myshopping.R;
import hwz.com.myshopping.page.BaseFragment;
import hwz.com.myshopping.page.checkout.CheckOutFragment;
import hwz.com.myshopping.page.checkout.check.CheckInfo;

public class PayMentActivity extends BaseFragment
{

	private View view;
	//到付-现金
	@InjectView(R.id.relative1)
	RelativeLayout relative1;
	@InjectView(R.id.image1)
	ImageView image1;
	//到付-pos
	@InjectView(R.id.relative2)
	RelativeLayout relative2;
	@InjectView(R.id.image2)
	ImageView image2;
	//支付宝
	@InjectView(R.id.relative3)
	RelativeLayout relative3;
	@InjectView(R.id.image3)
	ImageView image3;
	//选择按钮
	@InjectView(R.id.btn_select)
	Button btn_select;

	public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view ==null){
			view=inflater.inflate(R.layout.fragment_payment, null);
			//奶油刀注解库
			ButterKnife.inject(this, view);
			//--------------------------------
			relative1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					image1.setVisibility(View.VISIBLE);
					image2.setVisibility(View.INVISIBLE);
					image3.setVisibility(View.INVISIBLE);
					CheckInfo.payid=1;
                    CheckInfo.paytype="到付-现金";
				}
			});
			relative2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					image2.setVisibility(View.VISIBLE);
					image1.setVisibility(View.INVISIBLE);
					image3.setVisibility(View.INVISIBLE);
                    CheckInfo.payid=2;
                    CheckInfo.paytype="到付-pos";
				}
			});
			relative3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					image3.setVisibility(View.VISIBLE);
					image2.setVisibility(View.INVISIBLE);
					image1.setVisibility(View.INVISIBLE);
                    CheckInfo.payid=3;
                    CheckInfo.paytype="支付宝";
				}
			});
			btn_select.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					FragmentTransaction ft=getFragmentManager().beginTransaction();
					CheckOutFragment checkOutFragment=new CheckOutFragment();
					ft.replace(R.id.fragmentcontent, checkOutFragment);
					ft.commit();

				}
			});
		}
		return view;
	}


}
