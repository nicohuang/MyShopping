package hwz.com.myshopping.model;

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
