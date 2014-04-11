package org.hackillinois.android.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.hackillinois.android.MainActivity;
import org.hackillinois.android.R;
import org.hackillinois.android.models.people.Person;

import java.io.IOException;

/**
 * Execute the OAuthRequestTokenTask to retrieve the request, and authorize the request.
 * After the request is authorized by the user, the callback URL will be intercepted here.
 */
public class OAuthAccessFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Person> {

    private static final String TAG = "OAuthAccessFragment";
    private ProgressDialog progressDialog;
    private WebView webview;
    OAuth2Helper mOAuth2Helper;

    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i(TAG, url);
            if (url.startsWith(Oauth2Params.GOOGLE_PLUS.getRedirectUri())) {
                webview.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                getLoaderManager().initLoader(0, bundle, OAuthAccessFragment.this).forceLoad();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        assert rootView != null;
        webview = (WebView) rootView.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mOAuth2Helper = new OAuth2Helper(prefs);
        String authorizationUrl = mOAuth2Helper.getAuthorizationUrl();
        webview.setWebViewClient(webViewClient);
        webview.loadUrl(authorizationUrl);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webview.saveState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        webview.restoreState(savedInstanceState);
    }

    @Override
    public Loader<Person> onCreateLoader(int id, Bundle args) {
        return new ProcessTokenLoader(getActivity(), args.getString("url"));
    }

    @Override
    public void onLoadFinished(Loader<Person> loader, Person data) {
        if (data != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Intent intent = new Intent(getString(R.string.broadcast_login));
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(getString(R.string.pref_splash_viewed), true);
            editor.commit();

            intent.putExtra("person", data);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            });

            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        } else {
            //TODO fix bad login
            try {
                mOAuth2Helper.clearCredentials();
            } catch (IOException e) {
                e.printStackTrace();
            }
            webview.clearCache(true);
            String authorizationUrl = mOAuth2Helper.getAuthorizationUrl();
            webview.loadUrl(authorizationUrl);
        }
    }

    @Override
    public void onLoaderReset(Loader<Person> loader) {
    }
}
