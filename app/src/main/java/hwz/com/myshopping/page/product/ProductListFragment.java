package hwz.com.myshopping.page.product;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.IOException;

import hwz.com.myshopping.R;
import hwz.com.myshopping.activity.MyUrl;
import hwz.com.myshopping.page.BaseFragment;
import hwz.com.myshopping.model.ProductListBase;
import hwz.com.myshopping.net.HttpUtil;
import hwz.com.myshopping.util.ImageLoaderHelper;


public class ProductListFragment extends BaseFragment
{

	private GridView gridView;
	private String url= MyUrl.URLHEAD+MyUrl.PRODUCTLIST;//"http://10.0.2.2:8080/ECServer/productlist?id=";
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
		view=inflater.inflate(R.layout.fragment_productlist, null);
            //--------------------设置图片加载参数----------------------------
            imageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)//缓存到内存
                    .cacheOnDisk(true) //缓存到手机sdcard
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .build();
            //加载图片
		//------------------------------------------------
		Bundle bundle=getArguments();
		int id=bundle.getInt("id");
		gridView=(GridView) view.findViewById(R.id.gridView1);
		final String urlString=url+id;
		//--------------获取数据-----------------------
		new AsyncTask<Void, Void, Void>() {
			ProductListBase data=null;
			@SuppressWarnings("static-access")
			@Override
			protected Void doInBackground(Void... params) {
				HttpUtil httpUtil=new HttpUtil();
				try {
					resul=httpUtil.get(urlString);
					Gson gson=new Gson();
					data=gson.fromJson(resul, ProductListBase.class);
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
	private void showView(final ProductListBase data) {
		BaseAdapter adapter=new BaseAdapter() {		
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder =null;
				if(convertView==null){
					convertView=View.inflate(getActivity(),R.layout.list_productlist, null);
					holder=new ViewHolder();
					holder.txt_name=(TextView) convertView.findViewById(R.id.txt_name);
					holder.txt_price=(TextView) convertView.findViewById(R.id.txt_price);
					holder.pic=(ImageView) convertView.findViewById(R.id.pic);
					convertView.setTag(holder);
				}else {
					holder=	(ViewHolder) convertView.getTag();
				}
				//操作控件
				holder.txt_name.setText(data.productlist.get(position).name);
				holder.txt_price.setText(data.productlist.get(position).price+"");
				String imgurl=data.productlist.get(position).pic.replace("http://192.168.1.105:8080/ECServer_D/", "http://10.0.2.2:8080/ECServer/");
                ImageLoaderHelper.getInstance(getActivity()).displayImage(url, holder.pic, imageOptions);
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
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

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
		TextView txt_price;
	}

}
