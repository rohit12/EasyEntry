package entry.easyentry.activities;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import entry.easyentry.R;
import entry.easyentry.fragments.CommercialFragment;

public class CommercialActivity extends AppCompatActivity
                            implements  CommercialFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commercial);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentHolder,new CommercialFragment());
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
