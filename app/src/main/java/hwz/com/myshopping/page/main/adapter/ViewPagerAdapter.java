package hwz.com.myshopping.page.main.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import hwz.com.myshopping.activity.MyUrl;
import hwz.com.myshopping.model.Home;
import hwz.com.myshopping.model.Home_banner;
import hwz.com.myshopping.util.ImageLoaderHelper;

/**
 * Created by jan on 15/7/13.
 */
public class ViewPagerAdapter extends PagerAdapter
{
    private Context context;
    private Home data;
    private DisplayImageOptions imageOptions;
    private ArrayList<ImageView> bigImage = new ArrayList<ImageView>();
    public ViewPagerAdapter(Context context,Home data,DisplayImageOptions imageOptions)
    {
        this.context = context;
        this.data = data;
        this.imageOptions = imageOptions;
    }
    @Override
    public boolean isViewFromObject(View view, Object obj)
    {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object)
    {
        ImageView imageView = bigImage.get(position);
        container.removeView(imageView);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        ImageView imageView = bigImage.get(position);
        if (data != null)
        {
            Home_banner banner = data.home_banner.get(position);
            String imageurl = banner.pic.replace("http://192.168.1.105:8080/ECServer_D", MyUrl.URLHEAD);
            //加载图片
            ImageLoaderHelper.getInstance(context).displayImage(imageurl, imageView, imageOptions);
        }
        container.addView(imageView);
        return imageView;
    }

    @Override
    public int getCount()
    {
        return 5;
    }
}
