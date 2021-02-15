package bd.edu.seu.messengerapp.Database.Repository;

import android.content.Context;

import androidx.room.Room;

import java.util.List;

import bd.edu.seu.messengerapp.Database.DatabaseActions;
import bd.edu.seu.messengerapp.Database.Entity.UserSqlite;
import bd.edu.seu.messengerapp.Database.UserDatabase;
import bd.edu.seu.messengerapp.util.CommonConstrain;

public class UserRepository implements DatabaseActions<UserSqlite, String> {
    private UserDatabase database;
    Context context;

    public UserRepository(Context context) {
        this.context = context;
        //This Database create on Config.class
        database = Room.databaseBuilder(context, UserDatabase.class, CommonConstrain.DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

    }


    @Override
    public long[] add(List<UserSqlite> userSqlite) {
        return database.userDao().insertAll(userSqlite);
    }

    @Override
    public List<UserSqlite> getAll() {
        return database.userDao().getAll();
    }

    @Override
    public UserSqlite getById(String id) {
        return database.userDao().getOne(id);
    }

    @Override
    public void update(UserSqlite userSqlite) {
        database.userDao().update(userSqlite);
    }

    @Override
    public void delete(UserSqlite userSqlite) {
        database.userDao().delete(userSqlite);
    }

}
