package bd.edu.seu.messengerapp.Database.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "user")
public class UserSqlite {

    @PrimaryKey
    @ColumnInfo(name = "user_id")
    @NonNull
    private String userId;
    @ColumnInfo(name = "username")
    private String userName;
    private String password;
    private String email;
    @ColumnInfo(name = "profile_pic")
    private String profilePic;
    @ColumnInfo(name = "last_message")
    private String token;
    private String about = "";

    public UserSqlite() {
    }

    public UserSqlite(String userId, String userName, String password, String email, String profilePic, String token, String about) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.profilePic = profilePic;
        this.token = token;
        this.about = about;
    }

    public UserSqlite(String userName, String password, String email, String profilePic, String token, String about) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.profilePic = profilePic;
        this.token = token;
        this.about = about;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }


    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
