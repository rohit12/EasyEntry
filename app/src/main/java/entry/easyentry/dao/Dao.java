package entry.easyentry.dao;

import entry.easyentry.models.Visitor;

public interface Dao<T> {

    void writeRecord(T t);

    T readRecord();

    void updateRecord(T t);

    void deleteRecord(T t);
}
