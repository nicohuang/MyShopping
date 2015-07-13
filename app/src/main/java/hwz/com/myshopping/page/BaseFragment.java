package hwz.com.myshopping.page;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {

	// 不显示在FragmentActivity会被调用
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// 移除Fragment后还有一它View的在指定的布局里引用
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
		}
	}

	protected View view = null;
}
