package hwz.com.myshopping.page.classify.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import hwz.com.myshopping.R;
import hwz.com.myshopping.model.CategoryBase;

/**
 * Created by jan on 15/7/13.
 */
public class ClassifyAdapter extends BaseAdapter
{
    private int[] classify=new int[]{
            R.mipmap.category1,
            R.mipmap.category2,
            R.mipmap.category3,
            R.mipmap.category4,
            R.mipmap.category5,
            R.mipmap.category5,
            R.mipmap.category5
    };
    private CategoryBase data;
    private Context context;
    private int count;
    public ClassifyAdapter(CategoryBase data,Context context,int count)
    {
        this.data =data;
        this.context = context;
        this.count = count;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder =null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.list_classifylist, null);
            holder=new ViewHolder();
            holder.text=(TextView) convertView.findViewById(R.id.txt_title);
            holder.pic=(ImageView) convertView.findViewById(R.id.pic);
            holder.tag=(TextView) convertView.findViewById(R.id.txt_tag);
            convertView.setTag(holder);
        }else {
            holder=	(ViewHolder) convertView.getTag();
        }
        holder.text.setText(data.category.get(position).name);
        holder.tag.setText(data.category.get(position).tag);
        holder.pic.setImageResource(classify[position]);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return data.category.get(position);
    }

    @Override
    public int getCount() {

        return count;
    }

    class ViewHolder{
        ImageView pic;
        TextView text;
        TextView tag;
    }
}
