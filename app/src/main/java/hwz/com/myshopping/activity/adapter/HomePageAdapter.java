package hwz.com.myshopping.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import hwz.com.myshopping.page.classify.ClassifyFragment;
import hwz.com.myshopping.page.main.MainFragment;
import hwz.com.myshopping.page.more.MoreFragment;
import hwz.com.myshopping.page.search.SearchFragment;
import hwz.com.myshopping.page.shoppingcart.ShoppingCartFragment;

public class HomePageAdapter extends FragmentPagerAdapter{
	private ArrayList<Fragment> pages=new ArrayList<Fragment>();
	public HomePageAdapter(FragmentManager fm) {
		super(fm);
		pages.add(new MainFragment());
		pages.add(new SearchFragment());
		pages.add(new ClassifyFragment());
		pages.add(new ShoppingCartFragment());
		pages.add(new MoreFragment());
	}

	@Override
	public Fragment getItem(int index) {
		return pages.get(index);
	}

	@Override
	public int getCount() {
		return pages.size();
	}

}
