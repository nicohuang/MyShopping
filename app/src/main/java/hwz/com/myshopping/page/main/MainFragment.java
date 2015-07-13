package hwz.com.myshopping.page.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ImageView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import hwz.com.myshopping.R;
import hwz.com.myshopping.activity.MyUrl;
import hwz.com.myshopping.model.Home;
import hwz.com.myshopping.page.BaseFragment;
import hwz.com.myshopping.page.classify.ClassifyFragment;
import hwz.com.myshopping.page.main.adapter.MainAdapter;
import hwz.com.myshopping.page.main.adapter.ViewPagerAdapter;
import hwz.com.myshopping.page.main.task.MainTask;
import hwz.com.myshopping.page.product.HotProductFragment;
import hwz.com.myshopping.page.product.NewProductFragment;
import hwz.com.myshopping.page.product.TopicFragment;
import hwz.com.myshopping.page.search.SearchFragment;

public class MainFragment extends BaseFragment
{
    private ListView list;
    @InjectViews({R.id.dot1, R.id.dot2, R.id.dot3, R.id.dot4, R.id.dot5})
    List<ImageView> dots;
    @InjectView(R.id.image_search)
    ImageView image_search;

    private DisplayImageOptions imageOptions;
    private String url = MyUrl.URLHEAD + MyUrl.HOME;
    private String resul = "";
    private View view;
    private Home data = null;
    private int currDot = 0;
    FragmentTransaction ft;

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (view != null)
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
            {
                parent.removeView(view);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_main, null);
            ButterKnife.inject(this, view);
            ft = getFragmentManager().beginTransaction();

            //---------------------viewpager--------------------------
            ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
            //获取窗口宽度
            WindowManager windowManager = getActivity().getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            int width = display.getWidth();
            //获取图片加载参数
            imageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)//缓存到内存
                    .cacheOnDisk(true) //缓存到手机sdcard
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .build();
            //加载图片
            //生成view
            final ArrayList<ImageView> bigImage = new ArrayList<ImageView>();
            for (int i = 0; i < 5; i++)
            {
                View item = View.inflate(getActivity(), R.layout.item_viewpager, null);
                item.setId(i);
                bigImage.add((ImageView) item);
            }
            //设置pageradapter
            ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getActivity(),data,imageOptions);
            viewPager.setAdapter(pagerAdapter);
            //第一个点选中
            dots.get(currDot).setSelected(true);
            //页面改变监听
            viewPager.setOnPageChangeListener(new OnPageChangeListener()
            {
                @Override
                public void onPageSelected(int index)
                {
                    //把前一个选择为false
                    dots.get(currDot).setSelected(false);
                    currDot = index;
                    //把当前选择为true
                    dots.get(currDot).setSelected(true);
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2)
                {

                }

                @Override
                public void onPageScrollStateChanged(int arg0)
                {

                }
            });
            //------------------点击进入搜索--------------------------------------
            image_search.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    SearchFragment searchFragment = new SearchFragment();
                    ft.replace(R.id.fragmentcontent, searchFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
            //-------------------------获取网络数据--------------------------------
            //异步任务-线程获取网络数据
            new MainTask(url)
            {
                @Override
                protected void onPostExecute(Home home)
                {
                    super.onPostExecute(home);
                    showView(data);
                }
            }.execute();
        }
        return view;
    }

    //显示数据
    private void showView(final Home data)
    {
        list = (ListView) view.findViewById(R.id.list);
        MainAdapter adapter = new MainAdapter(getActivity(), data);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3)
            {
                switch (position)
                {
                    case 1:
                        TopicFragment topicFragment = new TopicFragment();
                        ft.replace(R.id.fragmentcontent, topicFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                    case 2:
                        NewProductFragment newProductFragment = new NewProductFragment();
                        ft.replace(R.id.fragmentcontent, newProductFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                    case 3:
                        HotProductFragment hotProductFragment = new HotProductFragment();
                        ft.replace(R.id.fragmentcontent, hotProductFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                        break;
                    case 4:
                        ClassifyFragment fragment = new ClassifyFragment();
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

}
