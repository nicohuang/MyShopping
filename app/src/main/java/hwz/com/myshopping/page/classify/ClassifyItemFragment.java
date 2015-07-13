package hwz.com.myshopping.page.classify;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hwz.com.myshopping.R;
import hwz.com.myshopping.model.Category;
import hwz.com.myshopping.page.BaseFragment;
import hwz.com.myshopping.page.classify.adapter.ClassityItemAdapter;

public class ClassifyItemFragment extends BaseFragment
{
	@InjectView(R.id.list)
	ListView list;
	private static ArrayList<Category> data;
	private int id;
	private View view;
	private int count=0;
	
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
		
		view =inflater.inflate(R.layout.fragment_classifylistitem, null);
		ButterKnife.inject(this, view);
		Bundle bundle=getArguments();
		data=(ArrayList<Category>) bundle.getSerializable("cateOne");
		//-------------计算二级数量--------------
		if(count==0){
			System.out.println("count!!");
		}
		id =bundle.getInt("id");
		//定义一个数据记录所获取的id的记录地址
        ClassityItemAdapter adapter = new ClassityItemAdapter(data,getActivity(),id);
		list.setAdapter(adapter);//装配适配器
		//list的点击事件
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ClassifyItemNestFragment fragment=new ClassifyItemNestFragment();
				FragmentManager fm=getFragmentManager();
				FragmentTransaction ft=fm.beginTransaction();//开启事务
				Bundle bundle=new Bundle();
				
				bundle.putSerializable("data", data);
				bundle.putInt("id",data.get(position).id);
				fragment.setArguments(bundle);
				ft.replace(R.id.fragmentcontent, fragment);
				ft.addToBackStack(null);
				ft.commit();//提交事务
			}
		});
		return view;
	}

}
