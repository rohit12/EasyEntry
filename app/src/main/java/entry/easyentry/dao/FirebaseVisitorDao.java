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
    public void write(Visitor visitor) {
        database.child("visitors").child(visitor.getPhoneNumber()).setValue(visitor);
    }

    @Override
    public Visitor readVisitor() {
        return null;
    }

    @Override
    public void updateVisitor(Visitor visitor) {

    }

    @Override
    public void deleteVisitor(Visitor visitor) {

    }
}
