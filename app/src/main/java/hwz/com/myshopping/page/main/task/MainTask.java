package hwz.com.myshopping.page.main.task;

import android.os.AsyncTask;

import com.google.gson.Gson;

import hwz.com.myshopping.model.Home;
import hwz.com.myshopping.net.HttpUtil;

/**
 * Created by jan on 15/7/13.
 */
public class MainTask extends AsyncTask<Void, Void, Home>
{
    private String url;

    public MainTask(String url)
    {
        this.url = url;
    }
    @Override
    protected Home doInBackground(Void... params)
    {
        String result="";
        HttpUtil httpUtil = new HttpUtil();
        try
        {
             result = httpUtil.get(url);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Home data = gson.fromJson(result, Home.class);
        return data;
    }
}
