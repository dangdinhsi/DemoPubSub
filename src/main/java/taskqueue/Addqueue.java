package taskqueue;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Addqueue extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       Queue queue = QueueFactory.getQueue("my-queue-name");
        Document document = Jsoup.connect("https://ione.vnexpress.net/tin-tuc/phim").ignoreContentType(true).get();
        Element newsHome = document.getElementById("news_home");
        Elements elements = newsHome.select(".txt_link");
        for (Element el:elements){
            String link = el.attr("href");
            System.out.println(link);
            queue.add(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL).payload(link));
        }
    }
}
