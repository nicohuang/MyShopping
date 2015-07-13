package hwz.com.myshopping.page.classify;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;

import hwz.com.myshopping.R;
import hwz.com.myshopping.activity.MyUrl;
import hwz.com.myshopping.model.Category;
import hwz.com.myshopping.model.CategoryBase;
import hwz.com.myshopping.page.BaseFragment;
import hwz.com.myshopping.page.classify.adapter.ClassifyAdapter;
import hwz.com.myshopping.page.classify.task.ClassifyTask;


public class ClassifyFragment extends BaseFragment
{

    private ListView list;
    private CategoryBase data = null;
    private String url = MyUrl.URLHEAD + MyUrl.CATEGORY;
    public static int count = 0;
    private View view;
    private FragmentManager fm ;
    private FragmentTransaction ft;//开启事务

    @Override
    public void onDestroyView()
    {
        // TODO Auto-generated method stub
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
            view = inflater.inflate(R.layout.fragment_classifylist, null);
            list = (ListView) view.findViewById(R.id.list);
            fm = getFragmentManager();
            ft = fm.beginTransaction();//开启事务
            new ClassifyTask(url)
            {
                @Override
                protected void onPostExecute(CategoryBase categoryBase)
                {
                    super.onPostExecute(categoryBase);
                    showView(categoryBase);
                }
            }.execute();
        }
        return view;
    }

    //--------------------------------显示数据
    private void showView(final CategoryBase data)
    {
        ClassifyAdapter adapter = new ClassifyAdapter(data, getActivity(), count);
        list.setAdapter(adapter);
        final ArrayList<Category> cateOne = new ArrayList<Category>();
        new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected Void doInBackground(Void... params)
            {
                for (int i = 0; i < data.category.size(); i++)
                {
                    if (data.category.get(i).parent_id != 0)
                    {
                        cateOne.add(data.category.get(i));
                    }
                }
                return null;
            }
        }.execute();

        list.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id)
            {
                ClassifyItemFragment fragment = new ClassifyItemFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("cateOne", cateOne);
                bundle.putInt("id", data.category.get(position).id);
                fragment.setArguments(bundle);
                ft.replace(R.id.fragmentcontent, fragment);
                ft.addToBackStack(null);
                ft.commit();//提交事务
            }
        });

    }

}
