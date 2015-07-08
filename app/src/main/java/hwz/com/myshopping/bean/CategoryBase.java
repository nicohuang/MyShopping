package hwz.com.myshopping.bean;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CategoryBase implements Serializable {
	public CategoryBase(){

	}
	public String response;
	public ArrayList<Category> category;
	public String version;
}
