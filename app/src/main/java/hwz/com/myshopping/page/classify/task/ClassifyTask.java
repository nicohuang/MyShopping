package hwz.com.myshopping.page.classify.task;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;

import hwz.com.myshopping.model.CategoryBase;
import hwz.com.myshopping.net.HttpUtil;
import hwz.com.myshopping.page.classify.ClassifyFragment;

/**
 * Created by jan on 15/7/13.
 */
public class ClassifyTask extends AsyncTask<Void, Void, CategoryBase>
{
    private String url;
    public ClassifyTask(String url)
    {
        this.url = url;
    }

    @Override
    protected CategoryBase doInBackground(Void... params)
    {
        HttpUtil httpUtil = new HttpUtil();
        String result = "";
        CategoryBase data;
        try
        {
            result = httpUtil.get(url);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        data = gson.fromJson(result, CategoryBase.class);

        for (int i = 0; i < data.category.size(); i++)
        {
            if (data.category.get(i).parent_id == 0)
            {
                ClassifyFragment.count++;
            }
        }

        return data;
    }
}
