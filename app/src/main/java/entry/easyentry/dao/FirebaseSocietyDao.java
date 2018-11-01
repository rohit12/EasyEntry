package entry.easyentry.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import entry.easyentry.models.Society;

public class FirebaseSocietyDao {

    private DatabaseReference database;
    private Context context;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public FirebaseSocietyDao(Context context){
        this.context = context;
        database = FirebaseDatabase.getInstance().getReference();
        settings = this.context.getSharedPreferences("entry.easyentry.preferences", Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public void getSocietyInformation(String email){
        Query query = database.child("society").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {
                    Iterable<DataSnapshot> it = dataSnapshot.getChildren();
                    Society society = new Society();
                    for (DataSnapshot d : it){
                        society = d.getValue(Society.class);
                    }
                    editor.putString("SocietyName", society.getName());
                    editor.apply();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
