package pubsub;

import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SubscriberDemo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProjectSubscriptionName subscriptionName =ProjectSubscriptionName.of("thanh-ngoc-20101990","sub01");
        Subscriber subscriber =Subscriber.newBuilder(subscriptionName, new MessageReceiver() {
            public void receiveMessage(PubsubMessage pubsubMessage, AckReplyConsumer ackReplyConsumer) {
                System.out.println("Publisher said: "+pubsubMessage.getData().toStringUtf8());
                ackReplyConsumer.ack();
            }
        }).setCredentialsProvider(new CredentialsProvider() {
            public Credentials getCredentials() throws IOException {
                return GoogleCredentials.fromStream(
                        getClass().getClassLoader().getResourceAsStream("pubsub-key.json"));
            }
        }).build();
        subscriber.startAsync();
        resp.getWriter().println("listen to topic successs");
    }
}
