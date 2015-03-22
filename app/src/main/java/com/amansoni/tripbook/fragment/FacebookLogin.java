package com.amansoni.tripbook.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amansoni.tripbook.R;
import com.amansoni.tripbook.activity.SingleFragmentActivity;

/**
 * Created by Aman on 22/03/2015.
 */
public class FacebookLogin extends BaseFragment {

    private FacebookLogin facebookLogin;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            facebookLogin = new FacebookLogin();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, facebookLogin)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            facebookLogin = (FacebookLogin) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.container);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.facebook_login, container, false);

        return view;
    }
}
