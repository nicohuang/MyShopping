package hwz.com.myshopping.fragment;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hwz.com.myshopping.R;
import hwz.com.myshopping.fragment.product.ProductListFragment;
import hwz.com.myshopping.model.Category;

public class ClassifyItemNestFragment extends BaseFragment {
	private ListView list;
	private ArrayList<Category> data;
	private int id;
	private int[] mycount;
	private View view;
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		if(view !=null){
			ViewGroup parent=(ViewGroup) view.getParent();
			if(parent != null){
				parent.removeView(view);
			}
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view == null){
			view =inflater.inflate(R.layout.fragment_classifylistitemnest, null);
			list=(ListView) view.findViewById(R.id.list);
			//获取传递过来的数据
			Bundle bundle=getArguments();
			data =(ArrayList<Category>) bundle.getSerializable("data");
			id=bundle.getInt("id");
			
			mycount=new int[20];
			//自定义适配器
			BaseAdapter adapter=new BaseAdapter() {		
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					ViewHolder holder =null;
					if(convertView==null){
						
						convertView=View.inflate(getActivity(),R.layout.list_classifylistitemnest, null);
						holder=new ViewHolder();
						holder.text=(TextView) convertView.findViewById(R.id.txt_title);
						convertView.setTag(holder);//绑定控件
					}else {
						holder=	(ViewHolder) convertView.getTag();
					}
					holder.text.setText(data.get(mycount[position]).name);
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
					int count=0;
					for (int i = 0,j=0; i < data.size(); i++) {
						if(data.get(i).parent_id==id){
							mycount[j]=i;
							j++;
							count++;
						}
					}

					return count;
				}
			};
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					ProductListFragment fragment=new ProductListFragment();//新建片段
					FragmentTransaction ft=getFragmentManager().beginTransaction();//开启事务
					//bundle传递数据
					Bundle bundle=new Bundle();
					bundle.putInt("id", data.get(mycount[position]).id);
					fragment.setArguments(bundle);
					//替换片段
					ft.replace(R.id.fragmentcontent, fragment);
					ft.addToBackStack(null);//把当前片段放入后台栈
					ft.commit();//提交事务
				}
			});
		}

		return view;
	}

	class ViewHolder{
		TextView text;
	}

}
