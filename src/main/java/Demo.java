import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Demo {

    public static void main(String args[]) throws InterruptedException, IOException, TimeoutException {

        Client subClient1 = new Client("test-cluster", "subClient1");
        Thread publisher = new Publisher("test-subject");
        publisher.start();
        subClient1.connect();
        Thread.sleep(2000);
        //subClient1.subscribe("test-subject");


        subClient1.subscribeFromLastread("test-subject");



    }
}
