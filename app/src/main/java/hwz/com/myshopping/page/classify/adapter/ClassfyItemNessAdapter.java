package hwz.com.myshopping.page.classify.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hwz.com.myshopping.R;
import hwz.com.myshopping.model.Category;
import hwz.com.myshopping.page.classify.ClassifyItemNestFragment;

/**
 * Created by jan on 15/7/13.
 */
public class ClassfyItemNessAdapter extends BaseAdapter
{
    private ArrayList<Category> data;
    private int id;
    private Context context;

    public ClassfyItemNessAdapter(Context context,ArrayList<Category> data,int id)
    {
        this.context = context;
        this.data = data;
        this.id = id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder =null;
        if(convertView==null){

            convertView=View.inflate(context, R.layout.list_classifylistitemnest, null);
            holder=new ViewHolder();
            holder.text=(TextView) convertView.findViewById(R.id.txt_title);
            convertView.setTag(holder);//绑定控件
        }else {
            holder=	(ViewHolder) convertView.getTag();
        }
        holder.text.setText(data.get(ClassifyItemNestFragment.mycount.get(position)).name);
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
        for (int i = 0,j=0; i < data.size(); i++) {
            if(data.get(i).parent_id==id){
                ClassifyItemNestFragment.mycount.add(i);
                j++;
            }
        }

        return ClassifyItemNestFragment.mycount.size();
    }
    class ViewHolder{
        TextView text;
    }
}
