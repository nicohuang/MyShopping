package hwz.com.myshopping.fragment.product;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.IOException;
import java.util.ArrayList;

import hwz.com.myshopping.R;
import hwz.com.myshopping.activity.MyUrl;
import hwz.com.myshopping.fragment.LoginFragment;
import hwz.com.myshopping.model.CartInfo;
import hwz.com.myshopping.model.ProductInfoBase;
import hwz.com.myshopping.dao.CartDao;
import hwz.com.myshopping.net.HttpUtil;
import hwz.com.myshopping.util.HttpClientApplication;
import hwz.com.myshopping.util.ImageLoaderHelper;

public class ProductInfoFragment extends Fragment {
	private String url= MyUrl.URLHEAD+MyUrl.PRODUCT;//"http://10.0.2.2:8080/ECServer/product?name=";//网络访问url
	private String resul="";//返回数据集合
	private String urlStr="";
	private View view;//布局
	private ProductInfoBase data;//数据
	private RatingBar ratingBar;//评分条
	private Button addtocart;
	private TextView productName;
	private TextView price;
	private EditText count;
	private TextView limit;
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
		if(view==null){
			view=inflater.inflate(R.layout.fragment_productinfo, null);
			productName=(TextView) view.findViewById(R.id.name);
			price=(TextView) view.findViewById(R.id.price_txt);
			count=(EditText) view.findViewById(R.id.count);
			limit=(TextView) view.findViewById(R.id.limit);
			ratingBar=(RatingBar) view.findViewById(R.id.ratingbar);
			//--------------------------------------------
			addtocart=(Button) view.findViewById(R.id.toincart);
			//获取传递过来的id
			Bundle bundle=getArguments();
			String name=bundle.getString("name");
			urlStr=url+name;
			//-------------------------获取网络数据--------------------------------
			//异步任务-线程获取网络数据
			new AsyncTask<Void, Void, Void>() {
				@SuppressWarnings("static-access")
				@Override
				protected Void doInBackground(Void... params) {
					HttpUtil httpUtil=new HttpUtil();
					try {
						resul=httpUtil.get(urlStr);
						Gson gson=new Gson();
						data=gson.fromJson(resul, ProductInfoBase.class);
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
	//显示控件
	private void showView(final ProductInfoBase data) {
		/**
		 * 商品评价等级：
		 * 1.差  
		 * 2.及格  
		 * 3.一般
		 * 4.良好
		 * 5.优
		 */
		//设置显示商品信息
		productName.setText(data.product.name);//显示商品名称
		price.setText(data.product.price+"");//显示商品价格
		float scroe=(float)2.5;//商品评价
		if(data.product.score.equals("差")){
			scroe=(float) 1.0;
		}else if(data.product.score.equals("及格")){
			scroe=(float) 2.0;
		}else if(data.product.score.equals("一般")){
			scroe=(float) 3.0;
		}else if(data.product.score.equals("良好")){
			scroe=(float) 4.0;
		}else if(data.product.score.equals("优")){
			scroe=(float) 5.0;
		}
		ratingBar.setRating(scroe);//显示评价
		limit.setText(data.product.buyLimit+"");//显示库存
		//---------------------viewpager--------------------------
		ViewPager viewPager =(ViewPager) view.findViewById(R.id.viewpager);
		
		//获取窗口宽度
		WindowManager windowManager=getActivity().getWindowManager();
		Display display=windowManager.getDefaultDisplay();
		int width=display.getWidth();
        //--------------------设置图片加载参数----------------------------
        imageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//缓存到内存
                .cacheOnDisk(true) //缓存到手机sdcard
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .build();
		final ArrayList<ImageView> bigImage=new ArrayList<ImageView>();
		//设置图片的加载的View
		for (int i = 0; i < 2; i++) {
			View item=View.inflate(getActivity(), R.layout.item_productviewpager,null);
			item.setId(i);
			bigImage.add((ImageView) item);
		}
		//设置pagerAdapter
		PagerAdapter pagerAdapter=new PagerAdapter() {
			//官方默认
			@Override
			public boolean isViewFromObject(View view, Object obj) {
				return view==obj;
			}
			//移除view
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				ImageView imageView=bigImage.get(position);
				container.removeView(imageView);
			}
			//显示view
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				ImageView imageView=bigImage.get(position);
				if(data != null){

					String imageurl=data.product.pic.get(position).replace("http://192.168.1.105:8080/ECServer_D",MyUrl.URLHEAD);
                    ImageLoaderHelper.getInstance(getActivity()).displayImage(url,imageView, imageOptions);
				}
				container.addView(imageView);
				return imageView;
			}
			//view的页数
			@Override
			public int getCount() {
				return 2;
			}
		};
		//adapter设置给viewpager
		viewPager.setAdapter(pagerAdapter);	
		//-------------------------加入购货车-------------------------------
		addtocart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String countString=count.getText().toString().trim();
				int buycount=0;
				if(countString.equals("")){//输入数量为空，吐司提醒
					Toast.makeText(getActivity(),"数量不能为空",Toast.LENGTH_SHORT).show();
					count.setText("1");
				}else{//输入数量不为空时，取得数量
					buycount=Integer.parseInt(countString);
				}
				//输入购买数量小于库存，并且数量不等于0
				if(buycount>data.product.buyLimit && buycount != 0){
					Toast.makeText(getActivity(), "购买数量不能大于库存",Toast.LENGTH_SHORT).show();
				}else{
					//判断用户时候登陆
					HttpClientApplication application=(HttpClientApplication) getActivity().getApplication();

					if(application.username.equals("")){//如果用户未登陆，则进入登陆界面
						FragmentTransaction ft=getFragmentManager().beginTransaction();
						LoginFragment fragment=new LoginFragment();
						ft.replace(R.id.fragmentcontent, fragment);
						ft.addToBackStack(null);
						ft.commit();
					}else{//如果用户已经登陆，加入购货车
						CartDao dao=new CartDao(getActivity());
						CartInfo info=new CartInfo();
						info.name=data.product.name;
						info.num=buycount;
						info.url=data.product.pic.get(0);
						info.price=data.product.price;
						info.pudid=data.product.id;
						info.inventory=data.product.inventory_area;
						dao.inser(info,application.username);
						Toast.makeText(getActivity(), "加入购货车",Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	class ViewHolder{
		ImageView pic;
		TextView text;
	}

}
