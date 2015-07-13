package hwz.com.myshopping.page.classify;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hwz.com.myshopping.R;
import hwz.com.myshopping.model.Category;
import hwz.com.myshopping.page.BaseFragment;
import hwz.com.myshopping.page.classify.adapter.ClassfyItemNessAdapter;
import hwz.com.myshopping.page.product.ProductListFragment;

public class ClassifyItemNestFragment extends BaseFragment
{
	private ListView list;
	private ArrayList<Category> data;
	private int id;
	public static List<Integer> mycount;
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
            mycount = new ArrayList<>();
			//获取传递过来的数据
			Bundle bundle=getArguments();
			data =(ArrayList<Category>) bundle.getSerializable("data");
			id=bundle.getInt("id");
            ClassfyItemNessAdapter adapter = new ClassfyItemNessAdapter(getActivity(),data,id);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					ProductListFragment fragment=new ProductListFragment();//新建片段
					FragmentTransaction ft=getFragmentManager().beginTransaction();//开启事务
					//bundle传递数据
					Bundle bundle=new Bundle();
					bundle.putInt("id", data.get(mycount.get(position)).id);
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

}
