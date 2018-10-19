package entry.easyentry.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import entry.easyentry.models.Visitor;

public class FirebaseVisitorDao implements Dao<Visitor> {

    private DatabaseReference database;

    public FirebaseVisitorDao(){
        database = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public void writeRecord(Visitor visitor) {
        database.child("visitors").push().setValue(visitor);
    }

    @Override
    public Visitor readRecord() {
        return null;
    }

    @Override
    public void updateRecord(Visitor visitor) {

    }

    @Override
    public void deleteRecord(Visitor visitor) {

    }
}
