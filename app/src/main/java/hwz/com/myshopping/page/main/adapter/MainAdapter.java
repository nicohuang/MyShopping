package hwz.com.myshopping.page.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import hwz.com.myshopping.R;
import hwz.com.myshopping.model.Home;

/**
 * Created by jan on 15/7/13.
 */
public class MainAdapter extends BaseAdapter
{
    private Context context;
    private Home data;
    private int[] home_classify = new int[]{//类型的图片
            R.mipmap.home_classify_01,
            R.mipmap.home_classify_02,
            R.mipmap.home_classify_03,
            R.mipmap.home_classify_04,
            R.mipmap.home_classify_05
    };

    public MainAdapter(Context context, Home data)
    {
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = View.inflate(context, R.layout.list_main, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.txt_title);
            holder.pic = (ImageView) convertView.findViewById(R.id.pic);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(data.home_classlist.get(position).title);
        holder.pic.setImageResource(home_classify[position]);
        return convertView;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public Object getItem(int position)
    {
        return data.home_classlist.get(position);
    }

    @Override
    public int getCount()
    {
        return data.home_classlist.size();
    }

    class ViewHolder
    {
        ImageView pic;
        TextView text;
    }
}
