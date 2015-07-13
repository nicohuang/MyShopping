package hwz.com.myshopping.page.address;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hwz.com.myshopping.R;
import hwz.com.myshopping.dao.AddressDao;
import hwz.com.myshopping.model.AddressInfo;
import hwz.com.myshopping.page.BaseFragment;
import hwz.com.myshopping.page.address.adapter.AddressListAdapter;
import hwz.com.myshopping.page.checkout.CheckOutFragment;
import hwz.com.myshopping.page.checkout.check.CheckInfo;
import hwz.com.myshopping.util.HttpClientApplication;

public class AddressListFragment extends BaseFragment
{
    //地址列表
    @InjectView(R.id.list)
    private ListView list;
    //fragment布局
    private View view;
    //选择地址
    @InjectView(R.id.btn_select)
    private Button select;
    //添加地址
    @InjectView(R.id.btn_add)
    private Button add;
    //地址数据库
    private AddressDao addressDao;
    private HttpClientApplication application;

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (view != null)
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
            {
                parent.removeView(view);
            }
        }
    }

    public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (view == null)
        {
            ButterKnife.inject(this, view);
            view = inflater.inflate(R.layout.fragment_addresslist, null);
            application = (HttpClientApplication) getActivity().getApplication();
            addressDao = new AddressDao(getActivity());
            //获取全局变量
            if ((!application.username.equals("")) && (!application.password.equals("")))
            {
                showView();
            }
        }
        return view;
    }

    private void showView()
    {
        final List<AddressInfo> info = addressDao.selectAddress();
        //显示列表
        final AddressListAdapter adapter = new AddressListAdapter(getActivity(), info);
        list.setAdapter(adapter);

        //长按删除地址
        list.setOnItemLongClickListener(new OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.mipmap.dialog_image);
                builder.setTitle("删除");
                builder.setMessage("确定要删除地址吗？");
                final int id = position;
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        addressDao.delectAddress(info.get(id).id);
                        List<AddressInfo> info = addressDao.selectAddress();
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                //通过构建器 创建对话框
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return false;
            }
        });
        //选择地址
        list.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id)
            {
                for (int i = 0; i < info.size(); i++)
                {
                    ((ImageView) list.findViewWithTag(i)).setVisibility(View.INVISIBLE);
                }
                ImageView imageView = (ImageView) list.findViewWithTag(position);
                imageView.setVisibility(View.VISIBLE);

                CheckInfo.name = info.get(position).name;
                CheckInfo.phone = info.get(position).phonenumber;
                CheckInfo.address = info.get(position).areadetail;

            }
        });
        if (application.addressid == 1)
        {
            select.setVisibility(View.VISIBLE);
        } else
        {
            select.setVisibility(View.INVISIBLE);
        }
        //选择地址
        select.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                CheckOutFragment checkOutFragment = new CheckOutFragment();
                ft.replace(R.id.fragmentcontent, checkOutFragment);
                ft.commit();
            }
        });
        //添加地址
        add.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddAddressFragment addfragment = new AddAddressFragment();
                ft.replace(R.id.fragmentcontent, addfragment);
                ft.addToBackStack("address");
                ft.commit();
            }
        });


    }

}
