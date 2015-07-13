package hwz.com.myshopping.test;

import junit.framework.TestCase;

import java.util.List;

import hwz.com.myshopping.dao.SearchDao;

public class TestDao extends TestCase {
	public void testwork(){
		
		SearchDao dao=new SearchDao(null);
		dao.hintinser();
		List<String> hint=dao.selecthhint("奶粉");
		for (int i = 0; i < hint.size(); i++) {
			System.out.println(hint.get(i));
		}
	}
}
