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

public class InvoiceFragment extends BaseFragment {

	private View view;
	//个人
	@InjectView(R.id.relative1)
	RelativeLayout relative1;
	@InjectView(R.id.image1)
	ImageView image1;
	//单位
	@InjectView(R.id.relative2)
	RelativeLayout relative2;
	@InjectView(R.id.image2)
	ImageView image2;
	
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
			view=inflater.inflate(R.layout.fragment_invoice, null);
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
					
					application.invoiceid=1;
					application.invoiceinfo="个人";
				}
			});
			relative2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					image2.setVisibility(View.VISIBLE);
					image1.setVisibility(View.INVISIBLE);
					
					application.invoiceid=2;
					application.invoiceinfo="单位";
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
