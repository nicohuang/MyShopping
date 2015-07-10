package hwz.com.myshopping.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import hwz.com.myshopping.R;
import hwz.com.myshopping.activity.MyUrl;
import hwz.com.myshopping.model.Home;
import hwz.com.myshopping.model.Home_banner;
import hwz.com.myshopping.net.HttpUtil;
import hwz.com.myshopping.util.ImageLoaderHelper;

public class MainFragment extends BaseFragment {
	private ListView list;
	@InjectViews({R.id.dot1, R.id.dot2, R.id.dot3, R.id.dot4, R.id.dot5})
	List<ImageView> dots;
	@InjectView(R.id.image_search)
	ImageView image_search;
    private DisplayImageOptions imageOptions;
	private String url= MyUrl.URLHEAD+MyUrl.HOME;//"http://10.0.2.2:8080/ECServer/home";//网络访问url
	private String resul="";
	private int[] home_classify=new int[]{//类型的图片
			R.mipmap.home_classify_01,
			R.mipmap.home_classify_02,
			R.mipmap.home_classify_03,
			R.mipmap.home_classify_04,
			R.mipmap.home_classify_05
	};
	private View view;
	private Home data=null;
	private int currDot=0;
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
			view=inflater.inflate(R.layout.fragment_main, null);
			ButterKnife.inject(this, view);
			//---------------------viewpager--------------------------
			ViewPager viewPager =(ViewPager) view.findViewById(R.id.viewpager);
			//获取窗口宽度
			WindowManager windowManager=getActivity().getWindowManager();
			Display display=windowManager.getDefaultDisplay();
			int width=display.getWidth();
			//获取图片加载参数
            imageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)//缓存到内存
                    .cacheOnDisk(true) //缓存到手机sdcard
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .build();
            //加载图片
			//生成view
			final ArrayList<ImageView> bigImage=new ArrayList<ImageView>();
			for (int i = 0; i < 5; i++) {
				View item=View.inflate(getActivity(), R.layout.item_viewpager,null);
				item.setId(i);
				bigImage.add((ImageView) item);
			}
			//设置pageradapter
			PagerAdapter pagerAdapter=new PagerAdapter() {

				@Override
				public boolean isViewFromObject(View view, Object obj) {
					return view==obj;
				}
				@Override
				public void destroyItem(ViewGroup container, int position,
						Object object) {
					ImageView imageView=bigImage.get(position);
					container.removeView(imageView);
				}
				@Override
				public Object instantiateItem(ViewGroup container, int position) {
					ImageView imageView=bigImage.get(position);
					if(data != null){
						Home_banner banner=data.home_banner.get(position);
						String imageurl=banner.pic.replace("http://192.168.1.105:8080/ECServer_D",MyUrl.URLHEAD);
						//加载图片
                        ImageLoaderHelper.getInstance(getActivity()).displayImage(url,imageView, imageOptions);
					}
					container.addView(imageView);
					return imageView;
				}
				@Override
				public int getCount() {
					return 5;
				}
			};
			//pageradapter设置给viewPager
			viewPager.setAdapter(pagerAdapter);
			//第一个点选中
			dots.get(currDot).setSelected(true);
			//页面改变监听
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int index) {
					//把前一个选择为false
					dots.get(currDot).setSelected(false);
					currDot=index;
					//把当前选择为true
					dots.get(currDot).setSelected(true);
				}
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
						
				}
				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});
			//------------------点击进入搜索--------------------------------------
			image_search.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					FragmentTransaction ft=getFragmentManager().beginTransaction();
					SearchFragment searchFragment=new SearchFragment();
					ft.replace(R.id.fragmentcontent,searchFragment);
					ft.addToBackStack(null);
					ft.commit();
				}
			});
			//-------------------------获取网络数据--------------------------------
			//异步任务-线程获取网络数据
			new AsyncTask<Void, Void, Void>() {
				@SuppressWarnings("static-access")
				@Override
				protected Void doInBackground(Void... params) {
					HttpUtil httpUtil=new HttpUtil();
					try {
						resul=httpUtil.get(url);
						Gson gson=new Gson();
						data=gson.fromJson(resul, Home.class);
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
	//显示数据
	private void showView(final Home data) {
		list=(ListView) view.findViewById(R.id.list);
		BaseAdapter adapter=new BaseAdapter() {		
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder =null;
				if(convertView==null){
					convertView=View.inflate(getActivity(),R.layout.list_main, null);
					holder=new ViewHolder();
					holder.text=(TextView) convertView.findViewById(R.id.txt_title);
					holder.pic=(ImageView) convertView.findViewById(R.id.pic);	
					convertView.setTag(holder);
				}else {
					holder=	(ViewHolder) convertView.getTag();
				}
				holder.text.setText(data.home_classlist.get(position).title);
				holder.pic.setImageResource(home_classify[position]);
				return convertView;
			}
			@Override
			public long getItemId(int position) {
				return position;
			}
			@Override
			public Object getItem(int position) {
				return data.home_classlist.get(position);
			}
			@Override
			public int getCount() {
				return data.home_classlist.size();
			}
		};
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				switch (position) {
				case 1:
					FragmentTransaction ft1=getFragmentManager().beginTransaction();
					TopicFragment topicFragment=new TopicFragment();
					ft1.replace(R.id.fragmentcontent, topicFragment);
					ft1.addToBackStack(null);
					ft1.commit();
					break;
				case 2:
					FragmentTransaction ft2=getFragmentManager().beginTransaction();
					NewProductFragment newProductFragment=new NewProductFragment();
					ft2.replace(R.id.fragmentcontent,newProductFragment);
					ft2.addToBackStack(null);
					ft2.commit();
					break;
				case 3:
					FragmentTransaction ft3=getFragmentManager().beginTransaction();
					HotProductFragment hotProductFragment=new HotProductFragment();
					ft3.replace(R.id.fragmentcontent,hotProductFragment);
					ft3.addToBackStack(null);
					ft3.commit();
					break;
				case 4:
					ClassifyFragment fragment=new ClassifyFragment();
					FragmentManager fm=getFragmentManager();
					FragmentTransaction ft=fm.beginTransaction();
					ft.replace(R.id.fragmentcontent, fragment);
					ft.addToBackStack(null);
					ft.commit();
					break;
				default:
					break;
				}
			}
		});

	}
	class ViewHolder{
		ImageView pic;
		TextView text;
	}

}
