package org.hackillinois.android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.hackillinois.android.Utils.Utils;


public class GoogleAuthFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_google_auth, container, false);

        // fill text box with email if they already entered it
        TextView emailTextView = (TextView) fragView.findViewById(R.id.auth_email_address);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String currentEmail = sharedPreferences.getString(getString(R.string.pref_email), "");
        emailTextView.setText(currentEmail);

        // set onClickListener for the login button
        fragView.findViewById(R.id.auth_login_button).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( activity );
                TextView emailView = (TextView) getView().findViewById(R.id.auth_email_address);
                String email = emailView.getText().toString();
                sharedPreferences.edit().putString(getString(R.string.pref_email), email).commit();

                Toast.makeText(activity, "Welcome to HackIllinois!", Toast.LENGTH_SHORT).show();
            }
        });

        return fragView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(4);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utils.setInsets(getActivity(), getView());
    }

}
