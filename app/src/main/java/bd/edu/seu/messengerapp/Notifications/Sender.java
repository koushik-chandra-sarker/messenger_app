package bd.edu.seu.messengerapp.Notifications;

public class Sender {
    private String to;// receiver token
    private Data data;
    private Notification notification;

    public Sender() {
    }
    private static Sender sender = null;

    public static synchronized Sender getInstance(){
        if (sender == null){
            sender = new Sender();
        }
        return sender;
    }

    public Sender(String to, Data data, Notification notification) {
        this.to = to;
        this.data = data;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
