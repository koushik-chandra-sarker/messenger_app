package bd.edu.seu.messengerapp.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;


import bd.edu.seu.messengerapp.Database.Dao.UserDao;
import bd.edu.seu.messengerapp.Database.Entity.UserSqlite;

@Database(entities = {UserSqlite.class}, version = 1,exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}