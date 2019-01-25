package cn.northpark.utils;/**
 * Created by Administrator on 2017/5/10.
 */

 import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.common.collect.Lists;

/**
 * @author caomin
 * @Date 2017-05-10 16:57
 * @Version 1.0
 */
public class HttpGetUtils {
    /**
     * get 方法
     *
     * @param url
     * @return
     */
    public static String getDataResult(String url) {
        String result = "";
        try {
            //获取httpclient实例
            CloseableHttpClient httpclient = HttpClients.createDefault();
            //获取方法实例。GET
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
            httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
            httpGet.setHeader("Keep-Alive", "3000");
            httpGet.setHeader("Connection", "Keep-Alive");
            httpGet.setHeader("Cache-Control", "no-cache");
            //httpGet.setHeader("Referer", referer);
            //执行方法得到响应
            CloseableHttpResponse response = httpclient.execute(httpGet);
            try {
                //如果正确执行而且返回值正确，即可解析

                if (response != null
                        && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                    //设置编码
                    Header[] headers = response.getHeaders("Content-Type");
                    String charset = "utf-8";
                    String contentType = headers[0].getValue();

                    int i = contentType.indexOf("=");
                    if (i != -1) {
                        charset = contentType.substring(i + 1, contentType.length());
                    }
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    result = IOUtils.toString(content, StandardCharsets.UTF_8.toString());
                }
            } finally {
                httpclient.close();
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("内容出错");
            return "";
        }
        return result;
    }


    
    /**
	    *    发送不带参数的HttpPost请求
	 * @param url
	 * @return
	 */
	public static String sendPost(String url) {
		//1.获得一个httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		//2.生成一个post请求
		HttpPost httppost = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {
			//3.执行get请求并返回结果
			response = httpclient.execute(httppost);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//4.处理结果，这里将结果返回为字符串
		HttpEntity entity = response.getEntity();
		String result = null;
		try {
			result = EntityUtils.toString(entity);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		return result;
	}
    
    /**
	   *    发送HttpPost请求，参数为map
	 * @param url
	 * @param map
	 * @return
	 */
    public static String sendPost(String url, Map<String, String> map) {
    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	List<NameValuePair> formparams = Lists.newArrayList();
    	for (Map.Entry<String, String> entry : map.entrySet()) {
    		//给参数赋值
    		formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
    	}
    	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
    	HttpPost httppost = new HttpPost(url);
    	httppost.setEntity(entity);
    	CloseableHttpResponse response = null;
    	try {
    		response = httpclient.execute(httppost);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	HttpEntity entity1 = response.getEntity();
    	String result = null;
    	try {
    		result = EntityUtils.toString(entity1);
    	} catch (ParseException | IOException e) {
    		e.printStackTrace();
    	}
    	return result;
    }
    
    
    /**
     * 下载网络文件到本地
     * @param url
     * @param dir
     * @param fileName
     * @return
     */
    public static String downloadHttpUrl(String urlString, String dir, String fileName) throws Exception {
    	
    	 URL url = new URL(urlString);// 构造URL
         URLConnection con = url.openConnection();// 打开连接
         InputStream is = con.getInputStream();// 输入流
         String code = con.getHeaderField("Content-Encoding");
         if ((null != code) && code.equals("gzip")) {
             GZIPInputStream gis = new GZIPInputStream(is);
             // 1K的数据缓冲
             byte[] bs = new byte[1024];
             // 读取到的数据长度
             int len;
             // 输出的文件流
             OutputStream os = new FileOutputStream(dir+fileName);
             // 开始读取
             while ((len = gis.read(bs)) != -1) {
                 os.write(bs, 0, len);
             }
             // 完毕，关闭所有链接
             gis.close();
             os.close();
             is.close();
         } else {
             // 1K的数据缓冲
             byte[] bs = new byte[1024];
             // 读取到的数据长度
             int len;
             // 输出的文件流
             OutputStream os = new FileOutputStream(dir+fileName);
             // 开始读取
             while ((len = is.read(bs)) != -1) {
                 os.write(bs, 0, len);
             }
             // 完毕，关闭所有链接
             os.close();
             is.close();
         }
    	
    	
        return dir + fileName;  
    }  

    
    

    
    public static void main(String[] args) {
    	System.out.println(StandardCharsets.UTF_8.toString());
    	System.out.println(StandardCharsets.UTF_8.name());
    	
	}
}

