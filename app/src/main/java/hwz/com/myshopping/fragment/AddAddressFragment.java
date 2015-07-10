package hwz.com.myshopping.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hwz.com.myshopping.R;
import hwz.com.myshopping.model.AddressInfo;
import hwz.com.myshopping.dao.CartDao;
import hwz.com.myshopping.util.HttpClientApplication;

public class AddAddressFragment extends Fragment {
	//控件
	@InjectView(R.id.name_edt)
	EditText name_edt;
	@InjectView(R.id.phonenumber_edt)
	EditText phonenumber_edt;
	@InjectView(R.id.fixedtel_edt)
	EditText fixedtel_edt;
	@InjectView(R.id.areaid_edt)
	EditText areaid_edt;
	@InjectView(R.id.areadetail_edt)
	EditText areadetail_edt;
	@InjectView(R.id.zipcode_edt)
	EditText zipcode_edt;
	@InjectView(R.id.btn_save)
	Button btn_save;
	private View view;//布局


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
			view=inflater.inflate(R.layout.fragment_addaddress, null);
			ButterKnife.inject(this,view);
			//登陆按钮
			btn_save.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final String name=name_edt.getText().toString().trim();//收件人
					final String phonenumber=phonenumber_edt.getText().toString().trim();//手机号码
					final String fixedtel=fixedtel_edt.getText().toString().trim();//固定电话
					final String areaid=areaid_edt.getText().toString().trim();//省市区
					final String areadetail=areadetail_edt.getText().toString().trim();//详细地址
					final String zipcode=zipcode_edt.getText().toString().trim();//邮编
					//判断密码是否一致,用户名密码不为空
					HttpClientApplication application=(HttpClientApplication) getActivity().getApplication();
					//myname+"&&password="+mypwd
					String myname=application.username;
					if(name.equals("")){
						Toast.makeText(getActivity(),"收件人不能为空",Toast.LENGTH_SHORT).show();
					}else if (phonenumber.equals("")) {
						Toast.makeText(getActivity(),"手机不能为空",Toast.LENGTH_SHORT).show();
					}else if (!(phonenumber.length()==11)) {
						Toast.makeText(getActivity(),"手机号码不符合规格",Toast.LENGTH_SHORT).show();
					}else if(areaid.equals("")){
						Toast.makeText(getActivity(), "省市区不能为空",Toast.LENGTH_SHORT).show();
					}
					else if(areadetail.equals("")){
						Toast.makeText(getActivity(), "详细地址不能为空",Toast.LENGTH_SHORT).show();
					}
					else if(zipcode.equals("")){
						Toast.makeText(getActivity(), "邮编不能为空",Toast.LENGTH_SHORT).show();
					}else{
						CartDao cartDao =new CartDao(getActivity());
						AddressInfo info=new AddressInfo();
						info.name=name;
						info.phonenumber=phonenumber;
						info.fixedtel=fixedtel;
						info.areaid=areaid;
						info.areadetail=areadetail;
						info.zipcode=zipcode;
						info.username=myname;
						cartDao.saveAddress(info);
						ViewGroup parent=(ViewGroup) view.getParent();
						if(parent != null){
							parent.removeView(view);
						}
						
					}
					
					

				}
			});
		}
		return view;
	}
}

