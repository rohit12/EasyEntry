package entry.easyentry.dao;

import entry.easyentry.models.Visitor;

public interface Dao<T> {

    void write(T t);

    T readVisitor();

    void updateVisitor(T t);

    void deleteVisitor(T t);
}
