package hwz.com.myshopping.test;

import java.util.List;

import com.example.shopping.dao.CartDao;

import junit.framework.TestCase;

public class TestDao extends TestCase {
	public void testwork(){
		
		CartDao dao=new CartDao(null);
		dao.hintinser();
		List<String> hint=dao.selecthhint("奶粉");
		for (int i = 0; i < hint.size(); i++) {
			System.out.println(hint.get(i));
		}
	}
}
