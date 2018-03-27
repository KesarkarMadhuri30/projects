package d.newnavigationapp.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import d.newnavigationapp.Fragment.ImageListFragment;
import d.newnavigationapp.R;

public class OpenActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i=getIntent();
        String title=i.getStringExtra("name");
        String name=i.getStringExtra("nm");
        String email=i.getStringExtra("email");
        getSupportActionBar().setTitle(title);


        ImageListFragment imageListFragment=new ImageListFragment();
        Bundle bundle=new Bundle();
        bundle.putString("name",title);
        bundle.putString("nm",name);
        bundle.putString("email",email);
        imageListFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, imageListFragment);
        fragmentTransaction.commit();


    }
}
