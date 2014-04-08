package org.hackillinois.android.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.hackillinois.android.R;

/**
 * Execute the OAuthRequestTokenTask to retrieve the request, and authorize the request.
 * After the request is authorized by the user, the callback URL will be intercepted here.
 */
public class OAuthAccessFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = "OAuthAccessFragment";
    private WebView webview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        webview = (WebView) rootView.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        OAuth2Helper oAuth2Helper = new OAuth2Helper(prefs);
        String authorizationUrl = oAuth2Helper.getAuthorizationUrl();

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, url);
                if (url.startsWith(Oauth2Params.GOOGLE_PLUS.getRedirectUri())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    getLoaderManager().initLoader(0, bundle, OAuthAccessFragment.this).forceLoad();
                    dismiss();
                }
            }
        });
        webview.loadUrl(authorizationUrl);
        return rootView;
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new ProcessTokenLoader(getActivity(), args.getString("url"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (data != null) {
            getActivity().sendBroadcast(new Intent("LOGGED_IN"));
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
