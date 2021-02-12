package bd.edu.seu.messengerapp.Notifications;

public class Notification {
    private String title;
    private String body;
    private String image;

    public Notification() {
    }
    private static Notification notification = null;

    public static synchronized Notification getInstance(){
        if (notification == null){
            notification = new Notification();
        }
        return notification;
    }


    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Notification(String title, String body, String image) {
        this.title = title;
        this.body = body;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
