package hwz.com.myshopping.fragment;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hwz.com.myshopping.R;
import hwz.com.myshopping.activity.MyUrl;
import hwz.com.myshopping.bean.NewProductBase;
import hwz.com.myshopping.net.HttpUtil;
import hwz.com.myshopping.util.ImageLoaderHelper;


public class NewProductFragment extends BaseFragment {
	@InjectView(R.id.list)
	ListView list;
	private String url= MyUrl.URLHEAD+MyUrl.NEWPRODUCT;
	private String resul="";
	private View view;
    private DisplayImageOptions imageOptions;
	
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
		if(view == null){
		view=inflater.inflate(R.layout.fragment_newproduct, null);
		ButterKnife.inject(this,view);
		//--------------------设置图片加载参数----------------------------
            //--------------------设置图片加载参数----------------------------
            imageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)//缓存到内存
                    .cacheOnDisk(true) //缓存到手机sdcard
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .build();
		//--------------获取数据-----------------------
		new AsyncTask<Void, Void, Void>() {
			NewProductBase data=null;
			@SuppressWarnings("static-access")
			@Override
			protected Void doInBackground(Void... params) {
				HttpUtil httpUtil=new HttpUtil();
				try {
					resul=httpUtil.get(url);
					Gson gson=new Gson();
					data=gson.fromJson(resul, NewProductBase.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			protected void onPostExecute(Void result) {
				showView(data);
			};
		}.execute();
		}
		return view;
	}
	private void showView(final NewProductBase data) {
		BaseAdapter adapter=new BaseAdapter() {		
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder =null;
				if(convertView==null){
					convertView=View.inflate(getActivity(),R.layout.list_newproduct, null);
					holder=new ViewHolder();
					holder.txt_name=(TextView) convertView.findViewById(R.id.txt_name);
					holder.pic=(ImageView) convertView.findViewById(R.id.pic);
					holder.price=(TextView) convertView.findViewById(R.id.price);
					holder.marketprice=(TextView) convertView.findViewById(R.id.marketprice);
					
					convertView.setTag(holder);
				}else {
					holder=	(ViewHolder) convertView.getTag();
				}
				//操作控件
				holder.txt_name.setText(data.productlist.get(position).name);
				String imgurl=data.productlist.get(position).pic.replace("http://192.168.1.105:8080/ECServer_D",MyUrl.URLHEAD);
                ImageLoaderHelper.getInstance(getActivity()).displayImage(url, holder.pic, imageOptions);
				holder.price.setText(data.productlist.get(position).price);
				holder.marketprice.setText(data.productlist.get(position).marketprice);
				holder.marketprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				//当前选择的列表项id
				return position;
			}

			@Override
			public Object getItem(int position) {
				//列表项
				return data.productlist.get(position);
			}

			@Override
			public int getCount() {
				//列表数量
				return data.productlist.size();
			}
		};
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				ProductInfoFragment fragment=new ProductInfoFragment();
				Bundle bundle=new Bundle();
				bundle.putString("name",data.productlist.get(position).name);
				fragment.setArguments(bundle);
				ft.replace(R.id.fragmentcontent, fragment);
				ft.addToBackStack(null);
				ft.commit();
			}
		});
	}
	//列表绑定类
	class ViewHolder{
		ImageView pic;
		TextView txt_name;
		TextView marketprice;
		TextView price;
		
	}

}
