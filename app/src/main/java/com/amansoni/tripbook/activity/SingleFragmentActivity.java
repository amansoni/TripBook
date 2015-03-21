package com.amansoni.tripbook.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.amansoni.tripbook.R;


/**
 * Created by beaumoaj on 13/01/15.
 */
public abstract class SingleFragmentActivity extends ActionBarActivity {
    protected abstract Fragment createFragment ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_frame);

        FragmentManager fm = this.getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            fragment.setArguments(getIntent().getExtras());
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }

    }

}
