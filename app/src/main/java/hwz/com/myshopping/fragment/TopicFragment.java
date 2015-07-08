package hwz.com.myshopping.fragment;

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
import hwz.com.myshopping.bean.TopicBase;
import hwz.com.myshopping.net.HttpUtil;
import hwz.com.myshopping.util.ImageLoaderHelper;


public class TopicFragment extends BaseFragment {
	@InjectView(R.id.list)
	ListView list;
	private String url= MyUrl.URLHEAD+MyUrl.TOPIC;
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
		view=inflater.inflate(R.layout.fragment_topic, null);
		ButterKnife.inject(this,view);
            //--------------------设置图片加载参数----------------------------
            imageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)//缓存到内存
                    .cacheOnDisk(true) //缓存到手机sdcard
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .build();
            //加载图片
		//--------------获取数据-----------------------
		new AsyncTask<Void, Void, Void>() {
			TopicBase data=null;
			@SuppressWarnings("static-access")
			@Override
			protected Void doInBackground(Void... params) {
				HttpUtil httpUtil=new HttpUtil();
				try {
					resul=httpUtil.get(url);
					Gson gson=new Gson();
					data=gson.fromJson(resul, TopicBase.class);
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
	private void showView(final TopicBase data) {
		BaseAdapter adapter=new BaseAdapter() {		
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder =null;
				if(convertView==null){
					convertView=View.inflate(getActivity(),R.layout.list_topic, null);
					holder=new ViewHolder();
					holder.txt_name=(TextView) convertView.findViewById(R.id.txt_name);
					holder.pic=(ImageView) convertView.findViewById(R.id.pic);
					convertView.setTag(holder);
				}else {
					holder=	(ViewHolder) convertView.getTag();
				}
				//操作控件
				holder.txt_name.setText(data.topic.get(position).name);
				String imgurl=data.topic.get(position).pic.replace("http://192.168.1.105:8080/ECServer_D",MyUrl.URLHEAD);
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
				return data.topic.get(position);
			}

			@Override
			public int getCount() {
				//列表数量
				return data.topic.size();
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
				bundle.putString("name",data.topic.get(position).name);
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
		
	}

}
