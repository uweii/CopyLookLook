package com.example.copylooklook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class NewsInfoActivity extends AppCompatActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);
        mWebView = findViewById(R.id.webview);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        /*String data = "<html>" +
                "<title>12</title>" +
                "<head>" +
                "<meta name=\"content-type\" content=\"text/html; charset=utf-8\">"+
                "    <link rel=\"stylesheet\" href=\"http://news-at.zhihu.com/css/news_qa.auto.css?v=4b3e3\">" +
                "</head>" +
                "<body>" +
                "<div class=\"main-wrap content-wrap\">" +
                "    <div class=\"headline\">" +
                "        <div class=\"img-place-holder\">" +
                "<img src=\"https://pic4.zhimg.com/v2-9bd4fdcb5222d8baee0dd42dc53866a7.jpg\">" +
                "        </div>" +
                "    </div>" +
                "    <div class=\"content-inner\">" +
                "        <div class=\"question\"><h2 class=\"question-title\">把一只驯化的鸡从十几层楼上扔下去会摔死吗？</h2>" +
                "            <div class=\"answer\">" +
                "                <div class=\"meta\"><img class=\"avatar\" src=\"http://pic1.zhimg.com/da8e974dc_is.jpg\"><span" +
                "                        class=\"author\">知乎用户，</span><span class=\"bio\">生物学民工。</span>" +
                "                </div>" +
                "" +
                "                <div class=\"content\"><p>不知道。只能讲讲我知道的类似的情况。</p><p>我没见过被从楼上丢下的鸡。只听说过从楼上扔出来的肉鸽。</p><p>" +
                "                    故事是这样的，那是很多年前的事。</p><p>朋友看到一笼肉鸽，觉得可怜，便买了下来。</p>" +
                "" +
                "                    <p>记得小学那些宣传画吗，大概这种的：</p><p><img" +
                "                            class=\"content-image\"" +
                "                            src=\"http://pic3.zhimg.com/70/v2-75a51c03d3bedbe4a68f4a459a47080a_b.jpg\" alt=\"\"></p>" +
                "                    <p>" +
                "                        嗯……朋友买的肉鸽长这样型的，朋友从公寓 5 楼阳台放生，</p><p><img class=\"content-image\"" +
                "                                                                   src=\"http://pic2.zhimg.com/70/v2-0dd92e6b71190fc195853c09d7a5da7d_b.jpg\"" +
                "                                                                   alt=\"\"></p><p>本以为放出去会是这样的，</p><p><img" +
                "                            class=\"content-image\"" +
                "                            src=\"http://pic2.zhimg.com/70/v2-2fcd434e0f8c53b264c1526128e5f7e1_b.jpg\" alt=\"\"></p>" +
                "                    <p>" +
                "                        但其实是这样的，</p><p><img class=\"content-image\"" +
                "                                              src=\"http://pic2.zhimg.com/70/v2-9ead726abd2805b4487a3d29e780ddf9_b.jpg\"" +
                "                                              alt=\"\"></p><p>那肥鸽子像老母鸡一样扑腾翅膀，</p><p><img class=\"content-image\"" +
                "                                                                                             src=\"http://pic3.zhimg.com/70/v2-67195b1658afc4709c6b7085acc4a992_b.jpg\"" +
                "                                                                                             alt=\"\"></p><p>" +
                "                        螺旋形匀速落下……俯视图是这样的，</p><p><img class=\"content-image\"" +
                "                                                       src=\"http://pic1.zhimg.com/70/v2-9e7e7517e3dfd643425b7f23cdeb9614_b.jpg\"" +
                "                                                       alt=\"\"></p><p>侧视图是这样的，</p><p><img class=\"content-image\"" +
                "                                                                                               src=\"http://pic2.zhimg.com/70/v2-d4630d39a4d5b4d6b4894e43a2d91635_b.jpg\"" +
                "                                                                                               alt=\"\"></p><p>" +
                "                        最后一筐鸽子安然无恙地落地了……</p><p><img class=\"content-image\"" +
                "                                                      src=\"http://pic2.zhimg.com/70/v2-c49c3d9c8ae5e38c14984d5b88c4be55_b.jpg\"" +
                "                                                      alt=\"\"></p><p>估计是一辈子都活在笼子里，也不知道逃跑，就原地待着……</p><p>" +
                "                        据说后来卖鸽子的人来了，也不知道最后怎么样了……</p><p>真是个胖子们的悲伤故事。</p>" +
                "                </div>" +
                "" +
                "            </div>" +
                "" +
                "            <div class=\"view-more\"><a href=\"http://www.zhihu.com/question/265854135\">查看知乎讨论<span" +
                "                    class=\"js-question-holder\"></span></a></div>" +
                "" +
                "        </div>" +
                "" +
                "    </div>" +
                "" +
                "</div>" +
                "<script type=“text/javascript”>window.daily=true</script>" +
                "</body>" +
                "</html>";*/
    mWebView.loadDataWithBaseURL(null,data,"text/html","utf-8",null);
    }
}
