package hwz.com.myshopping.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;

import hwz.com.myshopping.R;
import hwz.com.myshopping.adapter.HomePageAdapter;

public class MyMainActivity extends FragmentActivity {
	private int[] btn_image=new int[]{
			R.drawable.tab_home_btn,
			R.drawable.tab_search_btn,
			R.drawable.tab_class_btn,
			R.drawable.tab_shopping_btn,
			R.drawable.tab_more_btn
			
	};
	private LinearLayout bottonbar;//底部按钮布局
	private ArrayList<View> items=new ArrayList<View>();
	private WindowManager wManager;//窗口管理器
	private int currPage =0;//初始页
	private int width=80;
	private ViewPager viewPager;
	private FrameLayout fragmentcontent;
    public static File cacheDir;
	@Override
	protected void onCreate(Bundle arg0) {
		
		super.onCreate(arg0);
		setContentView(R.layout.activity_mymain);
        cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
		//----------------
		viewPager=(ViewPager) findViewById(R.id.tabcontent);
		fragmentcontent=(FrameLayout) findViewById(R.id.fragmentcontent);
		//获取屏幕宽度
		wManager=getWindowManager();
		Display display=wManager.getDefaultDisplay();
		bottonbar=(LinearLayout) findViewById(R.id.tabs);
		width=display.getWidth()/5;
		//创建底部按钮
		for(int i=0;i<5;i++){
			//生成布局
			View tab=View.inflate(this, R.layout.item_tab_btn, null);
			//设置布局控件
			ImageView imageView=(ImageView) tab.findViewById(R.id.btn_img);
			imageView.setImageResource(btn_image[i]);
			tab.setId(i);
			items.add(tab);
			//选项卡的显示 width获取窗口屏幕的宽度，LinearLayout.LayoutParams.MATCH_PARENT获取控件的大小
			bottonbar.addView(tab,width,LinearLayout.LayoutParams.MATCH_PARENT);
			//选项卡的点击方法
			tab.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int id=v.getId();
					if(id==currPage){//如果选择的与当前的显示的是同一个界面则不作处理

					}else{
						//把帧布局修全部remove掉
						fragmentcontent.removeAllViews();
						//显示选中页面
						viewPager.setCurrentItem(v.getId());
					}
				}
			});

		}		
		items.get(currPage).setSelected(true);
		initpages();
	}
	private HomePageAdapter adapter=null;
	private void initpages() {
		//viewpager适配器
		adapter=new HomePageAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		//当页面改变回调该方法
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				//改变选项卡的选中位置
				items.get(currPage).setSelected(false);
				currPage=position;
				items.get(position).setSelected(true);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		//显示选中页面
		viewPager.setCurrentItem(currPage);
	}
}
