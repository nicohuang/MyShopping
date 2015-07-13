package hwz.com.myshopping.page.address.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hwz.com.myshopping.R;
import hwz.com.myshopping.model.AddressInfo;

/**
 * Created by jan on 15/7/10.
 */
public class AddressListAdapter extends BaseAdapter
{
    private Context context;
    private List<AddressInfo> info;
    public AddressListAdapter(Context context,List<AddressInfo> info)
    {
        this.context = context;
        this.info = info;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder =null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.list_addresslist, null);
            holder=new ViewHolder();
            holder.txt_name=(TextView) convertView.findViewById(R.id.txt_name);
            holder.txt_phone=(TextView) convertView.findViewById(R.id.txt_phone);
            holder.txt_address=(TextView) convertView.findViewById(R.id.txt_address);
            holder.image_select=(ImageView) convertView.findViewById(R.id.image_select);
            convertView.setTag(holder);
        }else {
            holder=	(ViewHolder) convertView.getTag();
        }
        holder.txt_name.setText(info.get(position).name);
        holder.txt_phone.setText(info.get(position).phonenumber);
        holder.txt_address.setText(info.get(position).areaid+info.get(position).areadetail);
        holder.image_select.setTag(position);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return info.get(position);
    }

    @Override
    public int getCount() {

        return info.size();
    }

    class ViewHolder{
        TextView txt_name;
        TextView txt_phone;
        TextView txt_address;
        ImageView image_select;
    }
}
