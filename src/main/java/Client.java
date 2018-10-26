import io.nats.streaming.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

public class Client {
    private String cluserId;
    private String clientId;
    private StreamingConnectionFactory streamingConnectionFactory;
    private StreamingConnection streamingConnection;
    private Subscription subscription;
    private final CountDownLatch doneSignal = new CountDownLatch(1);

    public Client(String clusterId, String clientId) {
        this.cluserId = clusterId;
        this.clientId = clientId;
    }

    public void connect() {
        streamingConnectionFactory = new StreamingConnectionFactory(this.cluserId,this.clientId);
        try {
            streamingConnection =  streamingConnectionFactory.createConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void publish(String subjectName, String message){
        try {
            streamingConnection.publish(subjectName,message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String subject) throws InterruptedException, IOException, TimeoutException {
        subscription =  streamingConnection.subscribe( subject, new MessageHandler() {
            public void onMessage(Message m) {
                System.out.printf("Client : " +  clientId +  " Received a message: %s\n", new
                        String(m.getData()));
                doneSignal.countDown();
            }
        }, new SubscriptionOptions.Builder().deliverAllAvailable().build());

        doneSignal.await();
    }

    public void subscribeFromLastread(String subject) throws InterruptedException, IOException, TimeoutException{
        subscription = streamingConnection.subscribe(subject, new MessageHandler() {
            public void onMessage(Message m) {
                System.out.printf("Client : " +  clientId +  " Received a message: %s\n", new
                        String(m.getData()));
                doneSignal.countDown();
            }
        }, new SubscriptionOptions.Builder().startWithLastReceived().build());
    }

    public void unsubscribe() throws IOException {
        subscription.unsubscribe();
    }


}
