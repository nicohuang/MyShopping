package hwz.com.myshopping.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import hwz.com.myshopping.R;
import hwz.com.myshopping.activity.MyUrl;
import hwz.com.myshopping.bean.Category;
import hwz.com.myshopping.bean.CategoryBase;
import hwz.com.myshopping.net.HttpUtil;



public class ClassifyFragment extends BaseFragment {

	private ListView list;
	private CategoryBase data=null;
	private String url= MyUrl.URLHEAD+MyUrl.CATEGORY;
	private String resul="";
	private int[] classify=new int[]{
			R.mipmap.category1,
			R.mipmap.category2,
			R.mipmap.category3,
			R.mipmap.category4,
			R.mipmap.category5,
			R.mipmap.category5,
			R.mipmap.category5
	};
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
	public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view ==null){
		view=inflater.inflate(R.layout.fragment_classifylist, null);
		list=(ListView) view.findViewById(R.id.list);
		new AsyncTask<Void, Void, Void>() {

			private int count;
			@SuppressWarnings("static-access")
			@Override
			protected Void doInBackground(Void... params) {
				HttpUtil httpUtil=new HttpUtil();
				try {
					resul=httpUtil.get(url);
					Gson gson=new Gson();
					data=gson.fromJson(resul, CategoryBase.class);
					
					for (int i = 0; i < data.category.size(); i++) {
						if(data.category.get(i).parent_id==0){
							count++;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			protected void onPostExecute(Void result) {
				showView(data,count);
			};
		}.execute();
		}
		return view;
	}
	//--------------------------------显示数据
	private void showView(final CategoryBase data,final int count) {
		BaseAdapter adapter=new BaseAdapter() {		
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder =null;
				if(convertView==null){
					convertView=View.inflate(getActivity(),R.layout.list_classifylist, null);
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
		};
		list.setAdapter(adapter);
		final ArrayList<Category> cateOne=new ArrayList<Category>();
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				for (int i = 0; i < data.category.size(); i++) {
					if(data.category.get(i).parent_id!=0){
						cateOne.add(data.category.get(i));
					}
				}
				return null;
			}
			protected void onPostExecute(Void result) {

			};
		}.execute();
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ClassifyItemFragment fragment=new ClassifyItemFragment();
				FragmentManager fm=getFragmentManager();
				FragmentTransaction ft=fm.beginTransaction();//开启事务
				Bundle bundle=new Bundle();
				bundle.putSerializable("cateOne", cateOne);
				bundle.putInt("id",data.category.get(position).id);
				fragment.setArguments(bundle);
				ft.replace(R.id.fragmentcontent, fragment);
				ft.addToBackStack(null);
				ft.commit();//提交事务
			}
		});

	}
	class ViewHolder{
		ImageView pic;
		TextView text;
		TextView tag;
	}

}
