package bd.edu.seu.messengerapp.Database;

import java.util.List;

public interface DatabaseActions<T,I> {
    long[] add(List<T> t);
    List<T> getAll();
    T getById(I id);
    void update(T t);
    void delete(T t);
}
