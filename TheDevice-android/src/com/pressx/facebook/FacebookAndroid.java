//package com.pressx.facebook;
//
//import java.util.Arrays;
//import java.util.List;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//
//import com.facebook.Session;
//import com.facebook.SessionState;
//import com.facebook.UiLifecycleHelper;
//import com.facebook.Session.StatusCallback;
//import com.pressx.thedevice.MainActivity;
//
//public class FacebookAndroid extends Fragment implements FacebookInterface {
//	private Session s = Session.getActiveSession();
//	private List<String> permissions = Arrays.asList("publish_actions");
//	private MainActivity main;
//	private Bundle bundle = null;
//	
//	private UiLifecycleHelper uiHelper;
//	
//	private boolean resume = false;
//	private boolean publishReauth = false;
//	
//	String access= null;
//	
//	public FacebookAndroid() {
//		
//	}
//	
//	public FacebookAndroid (MainActivity mainActivity, Bundle bun) {
//		main = mainActivity;
//		bundle = bun;
//		if(Session.getActiveSession() == null) {
//			s = new Session(main);
//			Session.setActiveSession(s);
//		}
//		List<String> perm = s.getPermissions();
//		if(!perm.containsAll(permissions)) {
//			publishReauth = true;
//		}
//		
//		access = s.getAccessToken();
//	}
//	
//	private StatusCallback call = new Session.StatusCallback() {
//		@Override
//		public void call(Session session, SessionState state, Exception exception) {
//			onSessionStateChange(session, state, exception);
//		}
//	};
//	
//	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
//    	if(resume) {
//    		if(state.isOpened()) {
//    			System.out.println();
//    		}
//    	}
//    }
//	
//	public void onCreate(Bundle savedInstanceState) {
//		uiHelper = new UiLifecycleHelper(main, call);
//		uiHelper.onCreate(savedInstanceState);
//	}
//	
//	public void onResume() {
//		resume = true;
//		if(s.isClosed() || s.isOpened())
//			onSessionStateChange(s, s.getState(), null);
//		uiHelper.onResume();
//	}
//	
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		uiHelper.onActivityResult(requestCode, resultCode, data);
//	}
//	
//	public void onPause() {
//		resume = false;
//		uiHelper.onPause();
//	}
//	
//	public void onDestroy() {
//		uiHelper.onDestroy();
//	}
//	
//	public void onSaveInstanceState(Bundle outState) {
//		uiHelper.onSaveInstanceState(outState);
//	}
//}