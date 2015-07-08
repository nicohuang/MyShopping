package hwz.com.myshopping.activity;

public interface MyUrl {
	String URLHEAD="http://10.0.2.2:8080/ECServer";//访问路径
//	String URLHEAD="http://10.13.23.254:8080/ECServer";//访问路径
	String HOME="/home";//主页
	String CATEGORY="/category";//分类请求
	String PRODUCTLIST="/productlist?id=";//商品列表
	String PRODUCT="/product?name=";//商品详情
	String LOGIN="/login?";//登陆
	String REGISTER="/register?";//注册
	String USERINFO="/userinfo";//用户信息
	String ADDRESSLIST="/addresslist";//获取地址列表
	String ORDERSUMBIT="/ordersumbit";//提交结算
	String ORDERLIST="/orderlist?";//订单列表
	String ORDERDETAIL="/orderdetail?orderid=";//订单详情
	String ADDRESSSAVE="/addresssave";//添加地址
	String TOPIC="/topic";//促销快报
	String SEARCH_RECOMMEND="/search/recommend";//搜索热词
	String SEARCH="/search?keyword=";//搜索
	String NEWPRODUCT="/newproduct";//新品上架
	String HotProduct="/hotproduct";//热门单品

}
