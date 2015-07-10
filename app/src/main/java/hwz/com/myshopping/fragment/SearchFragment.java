package hwz.com.myshopping.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hwz.com.myshopping.R;
import hwz.com.myshopping.activity.MyUrl;
import hwz.com.myshopping.fragment.product.ProductInfoFragment;
import hwz.com.myshopping.model.SearchHotWork;
import hwz.com.myshopping.model.SearchProduct;
import hwz.com.myshopping.dao.CartDao;
import hwz.com.myshopping.util.ImageLoaderHelper;

public class SearchFragment extends Fragment{
	private View view;
	private String urlhotwork= MyUrl.URLHEAD+MyUrl.SEARCH_RECOMMEND;
	private AsyncHttpClient client;
	private CartDao dao;//SQLite
	private BaseAdapter hositryAdapter;
	List<String> hositry;
	private View footView;
    private DisplayImageOptions imageOptions;

	//显示热词
	@InjectView(R.id.gridview)
	GridView gridView;
	//显示搜索数据
	@InjectView(R.id.list_search)
	ListView list_search;
	@InjectView(R.id.list_history)
	ListView list_history;
	@InjectView(R.id.list_hint)
	ListView list_hint;
	//输入框
	@InjectView(R.id.search_edt)
	EditText search_edt;
	//搜索按钮
	@InjectView(R.id.search_btn)
	Button search_btn;

	@InjectView(R.id.relativeLayout2)
	RelativeLayout relativeLayout2;

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(view != null){
			ViewGroup parent=(ViewGroup) view.getParent();
			if(parent != null){
				parent.removeView(view);
			}
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view==null){
			view=inflater.inflate(R.layout.fragment_search, null);
			ButterKnife.inject(this,view);
			dao=new CartDao(getActivity());
			client =new AsyncHttpClient();

			

			//--------------------删除历史-----------------------------------------------
			footView=inflater.inflate(R.layout.itme_list_foot,null);
			Button foot=(Button) footView.findViewById(R.id.button1);
			list_history.addFooterView(footView);
			foot.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
					builder.setTitle("提示");
					builder.setIcon(R.mipmap.tips);
					builder.setMessage("你确定要删除搜索历史吗？");
					builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dao.delecthistory();
							showhistory();
						}
					});
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						
							
						}
					});
					Dialog dialog=builder.create();
					dialog.show();
					

				}
			});
			//------------------------------显示搜索历史-------------------------------------
			showhistory();
			//--------------------获取热词-----------------------------------------------
			AsyncHttpResponseHandler responseHandler=new AsyncHttpResponseHandler(){
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] bytes,
						Throwable arg3) {

				}
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] bytes) {

					try {
						String result=new String(bytes,"UTF-8");
						Gson gson=new Gson();
						SearchHotWork searchHotWork=gson.fromJson(result,SearchHotWork.class);
						showHotWork(searchHotWork);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			};
			client.get(urlhotwork, responseHandler);
			//-----------------------搜索动态提示-----------------------
			search_edt.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					list_hint.setVisibility(View.VISIBLE);
					String nameString=search_edt.getText().toString().trim();
					if(nameString.equals("")){
						relativeLayout2.setVisibility(View.VISIBLE);
						list_search.setVisibility(View.GONE);
						showhistory();
					}
					final List<String> hint=dao.selecthhint("奶粉");
					ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),R.layout.list_searchwork,hint){
						@Override
						public View getView(int position, View convertView,ViewGroup parent) {
							ViewHolder viewHolder=null;
							if(convertView==null){
								viewHolder=new ViewHolder();
								convertView=View.inflate(getActivity(),R.layout.item_hotwork, null);
								viewHolder.hotTextView=(TextView) convertView.findViewById(R.id.name);
								convertView.setTag(viewHolder);

							}else{
								viewHolder=(ViewHolder) convertView.getTag();
							}
							viewHolder.hotTextView.setText(hint.get(position));

							return convertView;
						}
					};
					list_hint.setAdapter(adapter);


				}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			//---------------------显示搜索数据-----------------------------
			search_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//获取输入框数据
					String work=search_edt.getText().toString().trim();
					if(work.equals("")){
						Toast.makeText(getActivity(),"请输入搜索内容",Toast.LENGTH_SHORT).show();
					}else{
						toSearch(work);
						//加入搜索历史
						dao.inserhistory(work);
						hositry=dao.selecthistory();
						if(hositry.size() == 1){
							list_history.addFooterView(footView);
						}
					}
				}
			});
		}
		return view;
	}
	//----------------------------显示搜索---------------------------------
	public void showhistory(){
		hositry=dao.selecthistory();
		if(hositry.size() == 0){
			list_history.removeFooterView(footView);
		}
		
		hositryAdapter =new BaseAdapter() {

			@Override
			public View getView(int position, View convertView,ViewGroup parent) {
				ViewHolder3 viewHolder=null;
				if(convertView==null){
					viewHolder=new ViewHolder3();
					convertView=View.inflate(getActivity(),R.layout.item_hostrywork, null);
					viewHolder.hositryTextView=(TextView) convertView.findViewById(R.id.hostry);
					convertView.setTag(viewHolder);

				}else{
					viewHolder=(ViewHolder3) convertView.getTag();
				}
				viewHolder.hositryTextView.setText(hositry.get(position));
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return hositry.get(position);
			}

			@Override
			public int getCount() {
				return hositry.size();
			}
		};
		list_history.setAdapter(hositryAdapter);
		list_history.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				toSearch(hositry.get(position));
				search_edt.setText(hositry.get(position));
			}
		});

	}
	public void toSearch(String work){
		relativeLayout2.setVisibility(View.GONE);
		list_search.setVisibility(View.VISIBLE);
		//1.获取网络数据
		AsyncHttpResponseHandler responseHandler=new AsyncHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1,byte[] arg2, Throwable arg3) {

			}
			@Override
			public void onSuccess(int arg0, Header[] arg1,byte[] bytes) {

				//2.解析数据
				try {
					String result=new String(bytes,"UTF-8");
					Gson gson=new Gson();
					SearchProduct searchProduct=gson.fromJson(result, SearchProduct.class);
					//3.显示数据
					showResult(searchProduct);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		};
		String url=MyUrl.URLHEAD+MyUrl.SEARCH+work;
		client.get(url, responseHandler);
	}

	//----------------------显示热词-------------------------------------
	public void showHotWork(final SearchHotWork searchHotWork){
		final List<String> hotwrok=searchHotWork.search_keywords;
		ArrayAdapter<String> gridadapter=new ArrayAdapter<String>(getActivity(), R.layout.item_hotwork, hotwrok){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder viewHolder=null;
				if(convertView==null){
					viewHolder=new ViewHolder();
					convertView=View.inflate(getActivity(),R.layout.item_hotwork, null);
					viewHolder.hotTextView=(TextView) convertView.findViewById(R.id.hotwrok);
					convertView.setTag(viewHolder);

				}else{
					viewHolder=(ViewHolder) convertView.getTag();
				}
				viewHolder.hotTextView.setText(hotwrok.get(position));
				return convertView;
			}

		};
		gridView.setAdapter(gridadapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				toSearch(hotwrok.get(position));
				search_edt.setText(hotwrok.get(position));
			}
		});
	}
	class ViewHolder{
		TextView hotTextView;
	}
	class ViewHolder3{
		TextView hositryTextView;
	}
	//----------------------显示搜索结果-----------------------------
	public void showResult(final SearchProduct searchProduct){
        //--------------------设置图片加载参数----------------------------
        imageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//缓存到内存
                .cacheOnDisk(true) //缓存到手机sdcard
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .build();
        //加载图片
		BaseAdapter adapter=new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder2 viewHolder=null;
				if(convertView==null){
					viewHolder=new ViewHolder2();
					convertView=View.inflate(getActivity(),R.layout.list_search, null);
					viewHolder.pic=(ImageView) convertView.findViewById(R.id.pic);
					viewHolder.txt_name=(TextView) convertView.findViewById(R.id.txt_name);
					viewHolder.marketprice=(TextView) convertView.findViewById(R.id.marketprice);
					viewHolder.price=(TextView) convertView.findViewById(R.id.price);
					viewHolder.comment_count=(TextView) convertView.findViewById(R.id.comment_count);
					convertView.setTag(viewHolder);

				}else{
					viewHolder=(ViewHolder2)convertView.getTag();
				}
				String imgurl=searchProduct.productlist.get(position).pic.replace("http://192.168.1.105:8080/ECServer_D",MyUrl.URLHEAD);
                ImageLoaderHelper.getInstance(getActivity()).displayImage(imgurl, viewHolder.pic, imageOptions);
				viewHolder.txt_name.setText(searchProduct.productlist.get(position).name);
				viewHolder.marketprice.setText(searchProduct.productlist.get(position).marketprice+"");
				viewHolder.price.setText(searchProduct.productlist.get(position).price+"");
				viewHolder.comment_count.setText(searchProduct.productlist.get(position).comment_count+"");
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}
			@Override
			public Object getItem(int position) {
				return searchProduct.productlist.get(position);
			}

			@Override
			public int getCount() {
				return searchProduct.productlist.size();
			}
		};
		list_search.setAdapter(adapter);
		list_search.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				FragmentTransaction ft=getFragmentManager().beginTransaction();//开启事务
				ProductInfoFragment fragment=new ProductInfoFragment();
				Bundle bundle=new Bundle();
				bundle.putString("name",searchProduct.productlist.get(position).name);
				fragment.setArguments(bundle);
				ft.replace(R.id.fragmentcontent, fragment);
				ft.addToBackStack(null);
				ft.commit();
			}
		});
	}
	class ViewHolder2{
		ImageView pic;
		TextView txt_name;
		TextView marketprice;
		TextView price;
		TextView comment_count;
	}
}
