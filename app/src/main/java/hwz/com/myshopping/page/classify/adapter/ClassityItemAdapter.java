package hwz.com.myshopping.page.classify.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hwz.com.myshopping.R;
import hwz.com.myshopping.model.Category;

/**
 * Created by jan on 15/7/13.
 */
public class ClassityItemAdapter extends BaseAdapter
{
    private ArrayList<Category> data;
    private Context context;
    private List<Integer> itemcount = new ArrayList<Integer>();
    private int id;
    public ClassityItemAdapter(ArrayList<Category> data,Context context ,int id)
    {
        this.data=data;
        this.context = context;
        this.id = id;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder =null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.list_classifylistitem, null);
            holder=new ViewHolder();
            holder.text=(TextView) convertView.findViewById(R.id.txt_title);
            convertView.setTag(holder);
        }else {
            holder=	(ViewHolder) convertView.getTag();
        }
        holder.text.setText(data.get(itemcount.get(position)).name);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        //获取第二级的数据的个数
        for (int i = 0,j=0; i < data.size(); i++) {
            if(data.get(i).parent_id==id){
                itemcount.add(j);
                j++;
            }
        }
        return itemcount.size();
    }
    class ViewHolder{
        TextView text;
    }
}
