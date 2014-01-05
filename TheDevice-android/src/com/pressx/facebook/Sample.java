package com.pressx.facebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;

public class Sample extends Activity {

    private static final String PERMISSION_FRIENDS_HOMETOWN = "friends_hometown";
    private static final String PREFERENCE_ACCESS_TOKEN = "facebookAccessToken";
    private static final String PREFERENCE_EXPIRATION_DATE = "facebookAccessTokenExpires";
    private ProgressDialog mProgress;
    private SharedPreferences mPrefs;

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            startFacebookRequest();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE)
        {
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
            Session session = Session.getActiveSession(); 

            if (session != null && !session.isClosed()) 
            {
                startFacebookRequest();
            }
        }
    }

    public interface FacebookConnectHandler {
        public void onSuccess();
        public void onFailure();
    }

    public void startFacebookRequest() {
        connectToFacebook(new FacebookConnectHandler() {
            @Override
            public void onSuccess() {
                // safety check
                if (isFinishing()) { return; }

                showProgressDialog("Connecting to Facebook ...");

                // check for publish permissions

                final List<String> permissions_required = Arrays.asList(PERMISSION_FRIENDS_HOMETOWN);

                if (Session.getActiveSession().getPermissions() == null || !Session.getActiveSession().getPermissions().containsAll(permissions_required)) {
                    // need to make a Session.openActiveSessionFromCache(...) call
                    // because of a bug in the Facebook sdk
                    // where a second call to get permissions
                    // won't result in a session callback when the token is updated

                    if (Session.openActiveSessionFromCache(Sample.this) == null) {
                        showToast("Could not connect to Facebook! (3)");
                        return;
                    }

                    Session.getActiveSession().addCallback(new Session.StatusCallback() {
                        @Override
                        public void call(Session session, SessionState state, Exception exception) {
                            if (exception != null || state.equals(SessionState.CLOSED) || state.equals(SessionState.CLOSED_LOGIN_FAILED)) {
                                // didn't get required permissions

                                session.removeCallback(this);

                                // safety check
                                if (!isFinishing()) {
                                    showToast("Could not connect to Facebook! (4)");
                                }
                            }
                            else if (state.equals(SessionState.OPENED_TOKEN_UPDATED) && session.getPermissions().containsAll(permissions_required)) {
                                // got required permissions

                                session.removeCallback(this);

                                // safety check
                                if (!isFinishing()) {
                                    startFacebookRequest();
                                }
                            }
                        }
                    });

                    NewPermissionsRequest req = new Session.NewPermissionsRequest(Sample.this, permissions_required);
                    req.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
                    Session.getActiveSession().requestNewReadPermissions(req);
                    return;
                }
                startGraphRequest(Session.getActiveSession());
            }


            @Override
            public void onFailure() {
                cancelProgressDialog();
                showToast("Could not connect to Facebook! (1)");
            }
        });
    }

    private void startGraphRequest(Session session) {
        Request.executeGraphPathRequestAsync(session, "me/friends/?access_token="+session.getAccessToken()+"&fields=id,name,hometown&limit=500", new Request.Callback() 
        {
            @Override
            public void onCompleted(Response response) {
                if (response != null) {
                    try {
                        JSONArray jarr = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
                        JSONObject entry;
                        StringBuilder backupStr = new StringBuilder();
//                        int nExistingEntries = existingEntries.length;
                        for (int i = 0; i < jarr.length(); i++) {
                            entry = jarr.getJSONObject(i);
                            if (!entry.isNull("id") && !entry.isNull("name") && !entry.isNull("hometown")) {
                                System.out.println(entry.getString("hometown"));
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        showToast("Unexpected error!");
                    }

                    showToast("Friends list populated");
                    cancelProgressDialog();
                }
            }
        });
    }

    private void connectToFacebook(final FacebookConnectHandler handler) {

        // check whether the user already has an active session
        // and try opening it if we do

        // (note: making a Session.openActiveSessionFromCache(...) call
        // instead of simply checking whether the active session is opened
        // because of a bug in the Facebook sdk
        // where successive calls to update a token
        // (requesting additional permissions etc)
        // don't result in a session callback)

        if (Session.getActiveSession() != null && Session.openActiveSessionFromCache(this) != null) {
            handler.onSuccess();
            return;
        }

        // initialise the session status callback

        Session.StatusCallback callback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {

                if (isFinishing()) { return; }

                // check session state

                if (state.equals(SessionState.CLOSED) || state.equals(SessionState.CLOSED_LOGIN_FAILED)) {

                    clearFacebookSharedPreferences();

                    // specific action for when the session is closed
                    // because an open-session request failed
                    if (state.equals(SessionState.CLOSED_LOGIN_FAILED)) {
                        cancelProgressDialog();
                        handler.onFailure();
                    }
                }
                else if (state.equals(SessionState.OPENED)) {
                    cancelProgressDialog();

                    saveFacebookSharedPreferences(session.getAccessToken(), session.getExpirationDate().getTime());

                    showToast("Succeeded connecting to Facebook");

                    handler.onSuccess();
                }
            }
        };

        // make the call to open the session

        showProgressDialog("Connecting to Facebook...");

        if (Session.getActiveSession() == null && mPrefs.contains(PREFERENCE_ACCESS_TOKEN) && mPrefs.contains(PREFERENCE_EXPIRATION_DATE)) {
            // open a session from the access token info
            // saved in the app's shared preferences

            String accessTokenString = mPrefs.getString(PREFERENCE_ACCESS_TOKEN, "");

            Date accessTokenExpires = new Date(mPrefs.getLong(PREFERENCE_EXPIRATION_DATE, 0));

            AccessToken accessToken = AccessToken.createFromExistingAccessToken(accessTokenString, accessTokenExpires, null, null, null);

            Session.openActiveSessionWithAccessToken(this, accessToken, callback);
        }
        else {
            // open a new session, logging in if necessary
            OpenRequest op = new Session.OpenRequest(this);

            op.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
            op.setCallback(null);

            List<String> permissions = new ArrayList<String>();
            permissions.add(PERMISSION_FRIENDS_HOMETOWN);
            op.setPermissions(permissions);

            Session session = new Builder(this).build();
            Session.setActiveSession(session);
            session.openForRead(op);
        }
    }

    private void saveFacebookSharedPreferences(final String token, final long expiration) {
        if (mPrefs != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(PREFERENCE_ACCESS_TOKEN, token);
            editor.putLong(PREFERENCE_EXPIRATION_DATE, expiration);
            editor.commit();
        }
    }

    private void clearFacebookSharedPreferences() {
        if (mPrefs != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.remove(PREFERENCE_ACCESS_TOKEN);
            editor.remove(PREFERENCE_EXPIRATION_DATE);
            editor.commit();
        }
    }

    private void showProgressDialog(final String text) {
        cancelProgressDialog();
        mProgress = ProgressDialog.show(Sample.this, "Facebook", text, true, false);
    }

    private void cancelProgressDialog() {
        if (mProgress != null) {
            if (mProgress.isShowing()) {
                mProgress.dismiss();
            }
            mProgress = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelProgressDialog();
    }

}