package bd.edu.seu.messengerapp.Notifications;

public class Data {
    private String userId;
    private String userProfilePic;
    private String username;

    private static Data data = null;

    public static synchronized Data getInstance(){
        if (data == null){
            data = new Data();
        }
        return data;
    }

    public Data() {
    }

    public Data(String userId, String userProfilePic, String username) {
        this.userId = userId;
        this.userProfilePic = userProfilePic;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
