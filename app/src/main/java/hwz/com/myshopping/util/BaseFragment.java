package hwz.com.myshopping.util;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {
	private View view;
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(view != null){
			ViewGroup parent=(ViewGroup) view.getParent();
			if(parent != null){
				parent.removeView(getView());
			}
		}
	}

}
