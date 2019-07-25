package taskqueue;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import entity.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class Getqueue extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Queue queue = QueueFactory.getQueue("my-queue-name");
        List<TaskHandle> tasks= queue.leaseTasks(30, TimeUnit.SECONDS,1);
        if(tasks.size()>0){
            TaskHandle task = tasks.get(0);
            String link = new String(task.getPayload());
            System.out.println(link);
            Document document = Jsoup.connect(link).ignoreContentType(true).get();
            String title = document.select(".title_news").text();
            String description =document.select(".short_intro").text();
            String content = document.select(".fck_detail").text();
            String author = document.select(".author_mail strong").text();
            System.out.println(title);
            System.out.println(description);
            System.out.println(content);
            System.out.println(author);
            Article article = new Article(link);
            article.setTitle(title);
            article.setDescription(description);
            article.setContent(content);
            article.setAuthor(author);
            ofy().save().entity(article).now();
            queue.deleteTask(task);
        }


    }
}
