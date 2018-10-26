import java.util.Date;

public class Publisher extends Thread {
    private String subject;
    private Client pubClient1;

    public Publisher(String subject) {
        this.subject = subject;
    }

    public void run() {
        try {
            publish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void publish() throws InterruptedException {
        pubClient1 = new Client("test-cluster", "pubClient1");
        pubClient1.connect();
        while (true){
            Thread.sleep(1000);
            pubClient1.publish(subject, String.valueOf(new Date().getTime()));

        }
    }



}
