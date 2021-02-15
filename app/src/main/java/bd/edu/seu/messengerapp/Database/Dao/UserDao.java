package bd.edu.seu.messengerapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import bd.edu.seu.messengerapp.Database.Entity.UserSqlite;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<UserSqlite> userSqlites);

    @Query("SELECT * FROM user")
    List<UserSqlite> getAll();

    @Query("SELECT * FROM user WHERE user_id = (:id)")
    UserSqlite getOne(String id);

    @Update
    void update(UserSqlite userSqlite);
    @Delete
    void delete(UserSqlite userSqlite);
}
