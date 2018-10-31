package entry.easyentry.dao;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import entry.easyentry.models.Resident;
import entry.easyentry.models.Visitor;

public class FirebaseResidentDao implements Dao<Resident> {

    private DatabaseReference database;
    private Context context;
    private static final String TAG = "FirebaseResidentDao";

    public FirebaseResidentDao(Context context){
        this.context = context;
        database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void writeRecord(Resident resident) {

    }

    @Override
    public Resident readRecord() {

        return null;
    }

    @Override
    public void updateRecord(Resident resident) {

    }

    @Override
    public void deleteRecord(Resident resident) {

    }

    public void getResidentPhoneNumber(String flatNumber, String society){
        Query query = database.child("residents/"+society+"/"+flatNumber);//.orderByChild("flatNumber").equalTo("1");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent intent = new Intent("send-resident-details");
                Toast.makeText(context, "Query successful", Toast.LENGTH_SHORT).show();
                Resident resident = dataSnapshot.getValue(Resident.class);
                if (dataSnapshot.getValue()!=null){
                    intent.putExtra("name",resident.getName());
                    intent.putExtra("phoneNumber",resident.getPhoneNumber());
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Query unsuccessful",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
