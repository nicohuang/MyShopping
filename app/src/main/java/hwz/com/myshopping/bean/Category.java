package hwz.com.myshopping.bean;

import java.io.Serializable;

public class Category implements Serializable{


	private static final long serialVersionUID = 1L;
	public Category(){
	}
	
	public int id;
	public boolean isleafnode;
	public String name;
	public int parent_id;
	public String pic;
	public String tag;
	
	
}
