package hwz.com.myshopping.fragment;

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
import hwz.com.myshopping.util.HttpClientApplication;

public class DeliveryFragment extends BaseFragment {

	private View view;
	//周一至周五送货
	@InjectView(R.id.relative1)
	RelativeLayout relative1;
	@InjectView(R.id.image1)
	ImageView image1;
	//双休日及公众假期送货
	@InjectView(R.id.relative2)
	RelativeLayout relative2;
	@InjectView(R.id.image2)
	ImageView image2;
	//时间不限，工作日双休日及公众假期均可送货
	@InjectView(R.id.relative3)
	RelativeLayout relative3;
	@InjectView(R.id.image3)
	ImageView image3;
	//选择按钮
	@InjectView(R.id.btn_select)
	Button btn_select;
	
	
	
	
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
			view=inflater.inflate(R.layout.fragment_delivery, null);
			//奶油刀注解库
			ButterKnife.inject(this, view);
			//获取全局变量
			final HttpClientApplication application=(HttpClientApplication) getActivity().getApplication();
			//--------------------------------
			relative1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					image1.setVisibility(View.VISIBLE);
					image2.setVisibility(View.INVISIBLE);
					image3.setVisibility(View.INVISIBLE);
					application.deliveryid=1;
					application.deliverytime="周一至周五送货";
				}
			});
			relative2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					image2.setVisibility(View.VISIBLE);
					image1.setVisibility(View.INVISIBLE);
					image3.setVisibility(View.INVISIBLE);
					application.deliveryid=2;
					application.deliverytime="双休日及公众假期送货";
				}
			});
			relative3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					image3.setVisibility(View.VISIBLE);
					image2.setVisibility(View.INVISIBLE);
					image1.setVisibility(View.INVISIBLE);
					application.deliveryid=3;
					application.deliverytime="时间不限，工作日双休日及公众假期均可送货";
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
