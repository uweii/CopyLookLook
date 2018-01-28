package com.uwei.NetUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.sharepres.SharePres;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.uwei.po.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by uwei on 2018/1/18.
 */

public class Connect {
    public static String LATEST_URI = "http://news-at.zhihu.com/api/4/news/latest";
    private static final String baseUri="http://news-at.zhihu.com/api/4/news/before/";  //获取之前news的url前缀
    private static final String baseDetailNewsUri  = "http://news-at.zhihu.com/api/4/news/";//news详细页面的url前缀
    private static final String  meiriBase = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/";  //每日看看的url前缀
    private static List<News> mNews = new ArrayList<>();
    private static List<String> imgUrls = new ArrayList<>();  //每日看看的url
    private static String TAG = "connect";
    private static String latestNews_date;  //最新news里面对应的日期
    public static List<News> getLatestNews(Context context,String url) {
        OkHttpClient client = new OkHttpClient();  //创建okHttp对象
        Request request = new Request.Builder()    //创建request对象
                .url(url).build();
        try {
            Response response = client.newCall(request).execute();//得到Response对对象
            //response.
            if (response.isSuccessful()) {
                String result = response.body().string();
                JSONObject resultObj = new JSONObject(result);
                latestNews_date = resultObj.getString("date");
                Log.d(TAG,"latest date is==> " + latestNews_date );
                JSONArray storiesArr = resultObj.getJSONArray("stories");
                Log.d(TAG,"有"+storiesArr.length()+"个故事");
                JSONObject firstStory = new JSONObject(storiesArr.get(0).toString());
                Log.d("latest_remote_id===>",firstStory.getString("id"));
                Log.d("pre_news_id=>",SharePres.getLatestNewsId(context));
                if(firstStory.getString("id").equals(SharePres.getLatestNewsId(context))){
                    if(mNews.size() > 0){
                        return mNews;
                    }
                }
                mNews.clear();
                SharePres.setLatestNewsId(context,firstStory.getString("id"));
                for(int i=0;i<storiesArr.length();i++){
                    Log.d(TAG,"mNEws.size()===> " + mNews.size());
                    JSONObject stotyObj = new JSONObject( storiesArr.get(i).toString());
                    String id = stotyObj.getString("id");
                    String imgUrl = (String) stotyObj.getJSONArray("images").get(0);
                    String title = stotyObj.getString("title");
                    //Log.d(TAG,"img====> " + img + "\n id===> " + id + "\n title==> "+title);
                    Bitmap bitmap=getUrlBitmap(imgUrl);
                    News news = new News(id,title,bitmap);
                    mNews.add(news);
                }
                    return mNews;
            }
        } catch (IOException e) {
            Log.d(TAG, "连接失败", e);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap getUrlBitmap(String imgUrl){
        Log.d("getBitmap===>", imgUrl);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(imgUrl).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<News> getPreNews(){
        String preUrl = baseUri + latestNews_date;
        OkHttpClient client = new OkHttpClient();  //创建okHttp对象
        Request request = new Request.Builder()   //创建request对象
                .url(preUrl).build();
        try {
            Response response = client.newCall(request).execute();//得到Response对对象
            if (response.isSuccessful()) {
                String result = response.body().string();
                JSONObject resultObj = new JSONObject(result);
                latestNews_date = resultObj.getString("date");
                Log.d(TAG," date is==> " + latestNews_date );
                JSONArray storiesArr = resultObj.getJSONArray("stories");
                Log.d(TAG,"有"+storiesArr.length()+"个故事");
                for(int i=0;i<storiesArr.length();i++){
                    JSONObject stotyObj = new JSONObject( storiesArr.get(i).toString());
                    String id = stotyObj.getString("id");
                    String imgUrl = (String) stotyObj.getJSONArray("images").get(0);
                    String title = stotyObj.getString("title");
                    //Log.d(TAG,"img====> " + img + "\n id===> " + id + "\n title==> "+title);
                    Bitmap bitmap=getUrlBitmap(imgUrl);
                    News news = new News(id,title,bitmap);
                    mNews.add(news);
                }
                return mNews;
            }
        } catch (IOException e) {
            Log.d(TAG, "连接失败", e);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String newsDetail(News news){
        String url = baseDetailNewsUri+news.getId();
        String response = getRemoteResponse(url);
        if(response!=null){
            try {
                JSONObject responseObject = new JSONObject(response);
                String body = responseObject.getString("body");
                String[] preBody = body.split("<div class=\"img-place-holder\">");
                preBody[0] = preBody[0] + "<div class=\"img-place-holder\">";
                //System.out.println("前缀==>："+preBody[0]+"\n后缀===》" + preBody[1]);
                String imgUrl = responseObject.getString("image");
                String css = responseObject.getJSONArray("css").get(0).toString();
                String finalBody = preBody[0] +"<img src=\"" + imgUrl + "\"/>" + preBody[1];
                String head = "<head>" +
                        "<link rel=\"stylesheet\" href = \"" + css +
                        "\">" +
                        "</head>"
                        ;
                String data = "<html>"+head+"<body>"+finalBody+"</body></html>";
                return data;
               // System.out.println("data==> " +data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String getRemoteResponse(String url){
        Log.i(TAG,"new url====> " + url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                return response.body().string();
            }
        } catch (IOException e) {
            Log.i(TAG,"connect error",e);
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getMeiriImgUrls(Context context,String url)  {
       // System.out.println(meiriBase+url);
        String response = getRemoteResponse((meiriBase+url));
        try {
            JSONObject  responseObject = new JSONObject(response);
            JSONArray imgArr = responseObject.getJSONArray("results");
            JSONObject firstObj = imgArr.getJSONObject(0);
            String id = firstObj.getString("_id");
            String localId = SharePres.getLatestImgId(context);
            if(id.equals(localId)){
                if(imgUrls.size()>0){
                    return imgUrls;
                }
            }
            SharePres.setLatestImgId(context,id);
            imgUrls.clear();
            //System.out.println("长度===》: "+imgArr.length());
            for(int i=0;i<imgArr.length();i++){
                JSONObject imgObj = imgArr.getJSONObject(i);
               // System.out.println(imgObj);
                String imgUrl = imgObj.getString("url");
                imgUrls.add(imgUrl);
            }
            System.out.println(imgUrls.size());
            return imgUrls;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getPreMeiriUrls(String uri){
        String response = getRemoteResponse((meiriBase+uri));
      //  Log.i(TAG,"new url===》： "+ response);
        try {
            JSONObject  responseObject = new JSONObject(response);
            JSONArray imgArr = responseObject.getJSONArray("results");
            System.out.println("长度===》: "+imgArr.length());
            for(int i=0;i<imgArr.length();i++){
                JSONObject imgObj = imgArr.getJSONObject(i);
                // System.out.println(imgObj);
                String imgUrl = imgObj.getString("url");
                imgUrls.add(imgUrl);
            }
            System.out.println(imgUrls.size());
            return imgUrls;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
