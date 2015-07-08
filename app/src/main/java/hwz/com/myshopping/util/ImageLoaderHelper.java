package hwz.com.myshopping.util;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import hwz.com.myshopping.activity.MyMainActivity;

/**
 * Created by jan on 15/7/2.
 */
public class ImageLoaderHelper
{
    //上下文
    private Context context;
    //图片加载
    private static ImageLoader imageLoader;
    public  ImageLoaderHelper(Context context)
    {
        this.context = context;
    }
    public static ImageLoader getInstance(Context context)
    {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3)                          // default
                .threadPriority(Thread.NORM_PRIORITY - 1)   // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(20 * 1024 * 1024))
                .memoryCacheSize(20 * 1024 * 1024)          // 缓冲大小
                .memoryCacheSizePercentage(13)              // default
                .discCache(new UnlimitedDiskCache(MyMainActivity.cacheDir))// 设置缓存文件目录
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileCount(100)                // 缓冲文件数目
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .imageDecoder(new BaseImageDecoder(false)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        //单例ImagerLoader类初始化
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        return imageLoader;
    }
}
