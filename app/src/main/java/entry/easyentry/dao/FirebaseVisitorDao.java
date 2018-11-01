package entry.easyentry.dao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import entry.easyentry.models.Visitor;

public class FirebaseVisitorDao implements Dao<Visitor> {

    private DatabaseReference database;
    private Context context;
    private static final String TAG = "FirebaseVisitorDao";
    private SharedPreferences settings;

    public FirebaseVisitorDao(Context context){
        database = FirebaseDatabase.getInstance().getReference();
        this.context = context;
        settings = this.context.getSharedPreferences("entry.easyentry.preferences",Context.MODE_PRIVATE);
    }


    @Override
    public void writeRecord(Visitor visitor) {
        database.child("visitors").child(visitor.getSociety()).push().setValue(visitor);
    }

    @Override
    public Visitor readRecord() {
        database = FirebaseDatabase.getInstance().getReference();
        Query query = database.child("visitors").orderByChild("phoneNumber").equalTo("");
        return null;
    }

    @Override
    public void updateRecord(Visitor visitor) {

    }

    @Override
    public void deleteRecord(Visitor visitor) {

    }

    public void checkIfVisitorExists(String phoneNumber){
        String societyName = settings.getString("SocietyName","missing");
        Query query = database.child("visitors/"+societyName).orderByChild("phoneNumber").equalTo(phoneNumber).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent intent = new Intent("visitor-exists");
                Iterable<DataSnapshot> it = dataSnapshot.getChildren();
                Visitor v = null;
                for (DataSnapshot d: it){
                    v = d.getValue(Visitor.class);
                }
                if (dataSnapshot.getValue()!=null){
                    Log.d(TAG, "onDataChange: DataSnapshot exists");
                    intent.putExtra("phoneNumber",true);
                    intent.putExtra("visitor",v);
                }
                else {
                    Log.d(TAG, "onDataChange: DataSnapshot does not exists");
                    intent.putExtra("phoneNumber",false);
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
