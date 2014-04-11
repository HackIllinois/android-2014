package org.hackillinois.android.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.hackillinois.android.AuthActivity;
import org.hackillinois.android.MainActivity;
import org.hackillinois.android.R;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.utils.Utils;

/**
 * Execute the OAuthRequestTokenTask to retrieve the request, and authorize the request.
 * After the request is authorized by the user, the callback URL will be intercepted here.
 */
public class OAuthAccessFragment extends Fragment implements LoaderManager.LoaderCallbacks<Person> {

    private static final String TAG = "OAuthAccessFragment";
    private ProgressDialog progressDialog;
    private WebView webview;
    private OAuth2Helper mOAuth2Helper;

    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.startsWith(Oauth2Params.GOOGLE_PLUS.getRedirectUri()) && url.contains("code=")) {
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mOAuth2Helper = new OAuth2Helper(prefs);
        String authorizationUrl = mOAuth2Helper.getAuthorizationUrl();
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        assert rootView != null;
        webview = (WebView) rootView.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(webViewClient);
        webview.loadUrl(authorizationUrl);
        Utils.setInsets(getActivity(), webview);
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
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        } else {
            // This is super hacky, but solves a race condition where onload finished is called twice!!
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            if (sharedPreferences.getBoolean(getString(R.string.pref_splash_viewed), false)) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                return;
            }
            CookieSyncManager.createInstance(getActivity());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_LONG).show();

            progressDialog.dismiss();
            startActivity(new Intent(getActivity(), AuthActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onLoaderReset(Loader<Person> loader) {
    }
}
