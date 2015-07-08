package hwz.com.myshopping.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpUtil {
	private static String JSESSIONID; //定义一个静态的字段，保存sessionID  
	public static String get(String url) throws IOException {
		String result = "";
		// 创建client对象
		HttpClient client = new DefaultHttpClient();
		// 创建get请求
		HttpGet get = new HttpGet(url);

		HttpResponse reponse = client.execute(get);

		if (200 == reponse.getStatusLine().getStatusCode()) {
			InputStream input = reponse.getEntity().getContent();

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			// 边读边写
			byte[] buffer = new byte[1024];

			int len = 0;
			while ((len = input.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

			out.flush();
			out.close();
			input.close();

			byte[] data = out.toByteArray();
			result = new String(data, "UTF-8");
		}

		return result;
	}

	public static String post(String url, Map<String, String> params) throws IOException {

		String result = "";
		// 创建client对象
		HttpClient client = new DefaultHttpClient();
		// 创建post请求

		HttpPost post = new HttpPost(url);

		Set<Entry<String, String>> set = params.entrySet();

		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		for (Entry<String, String> entry : set) {

			// BasicNameValuePair Entry key value
			BasicNameValuePair nv = new BasicNameValuePair(entry.getKey(), entry.getValue());
			list.add(nv);
		}
		// UrlEncodedFormEntity 在httpClient表单
		HttpEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
		// 设置表单对象
		post.setEntity(entity);
		HttpResponse reponse = client.execute(post);
		HttpGet httpRequest = new HttpGet(url);
		if (200 == reponse.getStatusLine().getStatusCode()) {
			InputStream input = reponse.getEntity().getContent();

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			CookieStore cookieStore = ((AbstractHttpClient) client).getCookieStore();  
			List<Cookie> cookies = cookieStore.getCookies();  
			if(null != JSESSIONID){    
				httpRequest.setHeader("Cookie", "JSESSIONID="+JSESSIONID);    
			} 
			for(int i=0;i<cookies.size();i++){  
				if("JSESSIONID".equals(cookies.get(i).getName())){  
					JSESSIONID = cookies.get(i).getValue();  
					break;  
				}  
			}
			// 边读边写
			byte[] buffer = new byte[1024];

			int len = 0;
			while ((len = input.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

			out.flush();
			out.close();
			input.close();

			byte[] data = out.toByteArray();
			result = new String(data, "UTF-8");
		}

		return result;
	}
}
