package pubsub;

import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PublisherDemo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProjectTopicName topicName = ProjectTopicName.of("thanh-ngoc-20101990", "topic01sidang");
        Publisher publisher =Publisher.newBuilder(topicName).setCredentialsProvider(new CredentialsProvider() {
            public Credentials getCredentials() throws IOException {
                return GoogleCredentials.fromStream(
                        getClass().getClassLoader().getResourceAsStream("pubsub-key.json"));
            }
        }).build();
        ByteString byteString = ByteString.copyFromUtf8(req.getParameter("msg"));
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(byteString).build();
        publisher.publish(pubsubMessage);
        resp.getWriter().println("post message success!!!");
    }
}
