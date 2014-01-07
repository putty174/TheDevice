package com.pressx.facebook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.model.OpenGraphAction;
import com.facebook.widget.ProfilePictureView;
import com.pressx.thedevice.FacebookApplication;
import com.pressx.thedevice.PickerActivity;
import com.pressx.thedevice.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SelectionFragment extends Fragment {
	private ProfilePictureView profilePictureView;
	private TextView userNameView;
	
	private static final int REAUTH_ACTIVITY_CODE = 100;
	private static final String TAG = "SelectionFragment";
	private boolean pendingAnnounce;
	private static final String PENDING_ANNOUNCE_KEY = "pendingAnnounce";
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	
	private ListView listView;
	private List<BaseListElement> listElements;
	
	private Button announceButton;
	
	private static final Uri M_FACEBOOK_URL = Uri.parse("http://m.facebook.com");
	
	private static final String POST_ACTION_PATH = "me/thedevice:hit";
	private ProgressDialog progressDialog;
	
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	
	private interface GameGraphObject extends GraphObject {
		public String getId();
		public void setId();

		public String getUrl();
		public void setUrl(String url);
	}

	private interface HitAction extends OpenGraphAction {
		public GameGraphObject getGame();
		public void setGame(GameGraphObject game);
	}
	
	private class ActionListAdapter extends ArrayAdapter<BaseListElement> {
		private List<BaseListElement> listElements;
		
		public ActionListAdapter(Context context, int resouceId, List<BaseListElement> listElements) {
			super(context, resouceId, listElements);
			this.listElements = listElements;
			for (int i = 0; i < listElements.size(); i++)
				listElements.get(i).setAdapter(this);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.listitem, null);
			}
			
			BaseListElement listElement = listElements.get(position);
			if(listElement != null) {
				view.setOnClickListener(listElement.getOnClickListener());
				ImageView icon = (ImageView) view.findViewById(R.id.icon);
				TextView text1 = (TextView) view.findViewById(R.id.text1);
				TextView text2 = (TextView) view.findViewById(R.id.text2);
				if(icon != null)
					icon.setImageDrawable(listElement.getIcon());
				if(text1 != null)
					text1.setText(listElement.getText1());
				if(text2 != null)
					text2.setText(listElement.getText2());
			}
			return view;
		}
	}
	
	private class PeopleListElement extends BaseListElement {
		private List<GraphUser> selectedUsers;
		private static final String FRIENDS_KEY = "friends";
		
		public PeopleListElement(int requestCode) {
			super(getActivity().getResources().getDrawable(R.drawable.action_people), getActivity().getResources().getString(R.string.action_people), getActivity().getResources().getString(R.string.action_people_default), requestCode);
		}
		
		@Override
		protected View.OnClickListener getOnClickListener() {
			return new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startPickerActivity(PickerActivity.FRIEND_PICKER, getRequestCode());
				}
			};
		}
		
		@Override
		protected void onActivityResult(Intent data) {
			selectedUsers = ((FacebookApplication) getActivity().getApplication()).getSelectedUsers();
			setUsersText();
			notifyDataChanged();
		}
		
		@Override
		protected void onSaveInstanceState(Bundle bundle) {
			if (selectedUsers != null)
				bundle.putByteArray(FRIENDS_KEY, getByteArray(selectedUsers));
		}
		
		@Override
		protected boolean restoreState(Bundle savedState) {
			byte[] bytes = savedState.getByteArray(FRIENDS_KEY);
			if (bytes != null) {
				selectedUsers = restoreByteArray(bytes);
				setUsersText();
				return true;
			}
			return false;
		}
		
		@Override
		protected void populateOGAction(OpenGraphAction action) {
			if(selectedUsers != null)
				action.setTags(selectedUsers);
		}
		
		private void setUsersText() {
			String text = null;
			if(selectedUsers != null) {
				if(selectedUsers.size() == 1)
					text = String.format(getResources().getString(R.string.single_user_selected), selectedUsers.get(0).getName());
				else if(selectedUsers.size() == 2)
					text = String.format(getResources().getString(R.string.two_users_selected), selectedUsers.get(0).getName(), selectedUsers.get(1).getName());
				else if(selectedUsers.size() > 2)
					text = String.format(getResources().getString(R.string.multiple_users_selected), selectedUsers.get(0).getName(), selectedUsers.size() - 1);
			}
			if (text == null)
				text = getResources().getString(R.string.action_people_default);
			setText2(text);
		}
		
		private byte[] getByteArray(List<GraphUser> users) {
			List<String> usersAsString = new ArrayList<String>(users.size());
			for (GraphUser user : users)
				usersAsString.add(user.getInnerJSONObject().toString());
			try {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				new ObjectOutputStream(outputStream).writeObject(usersAsString);
				return outputStream.toByteArray();
			} catch (IOException e) {
				Log.e(TAG, "Unable to serialize users.", e);
			}
			return null;
		}
		
		private List<GraphUser> restoreByteArray(byte[] bytes) {
			try {
				@SuppressWarnings("unchecked")
				List<String> usersAsString = (List<String>) (new ObjectInputStream(new ByteArrayInputStream(bytes))).readObject();
				if(usersAsString != null) {
					List<GraphUser> users = new ArrayList<GraphUser>(usersAsString.size());
					for(String user : usersAsString) {
						GraphUser graphUser = GraphObject.Factory.create(new JSONObject(user), GraphUser.class);
						users.add(graphUser);
					}
					return users;
				}
			} catch (ClassNotFoundException e) {
				Log.e(TAG, "Unable to deserialize users.", e);
			} catch (IOException e) {
				Log.e(TAG, "Unable to deserialize users.", e);
			} catch (JSONException e) {
				Log.e(TAG, "Unable to deserialize users.", e);
			}
			return null;
		}
	}
	
	private class LocationListElement extends BaseListElement {
		private GraphPlace selectedPlace = null;
		private static final String PLACE_KEY = "place";
		
		public LocationListElement (int requestCode) {
			super(getActivity().getResources().getDrawable(R.drawable.action_location), getActivity().getResources().getString(R.string.action_location), getActivity().getResources().getString(R.string.action_location_default), requestCode);
		}
		
		@Override
		protected View.OnClickListener getOnClickListener() {
			return new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startPickerActivity(PickerActivity.PLACE_PICKER, getRequestCode());
				}
			};
		}
		
		@Override
		protected void onActivityResult(Intent data) {
			selectedPlace = ((FacebookApplication) getActivity().getApplication()).getSelectedPlace();
			setPlaceText();
			notifyDataChanged();
		}
		
		@Override
		protected void onSaveInstanceState(Bundle bundle) {
			if(selectedPlace != null)
				bundle.putString(PLACE_KEY, selectedPlace.getInnerJSONObject().toString());
		}
		
		@Override
		protected boolean restoreState(Bundle savedState) {
			String place = savedState.getString(PLACE_KEY);
			if(place != null) {
				try {
					selectedPlace = GraphObject.Factory.create(new JSONObject(place), GraphPlace.class);
					setPlaceText();
					return true;
				} catch (JSONException e) {
					Log.e(TAG, "Unable to deserialize place.", e);
				}
			}
			return false;
		}
		
		@Override
		protected void populateOGAction(OpenGraphAction action) {
			if(selectedPlace != null)
				action.setPlace(selectedPlace);
		}
		
		private void setPlaceText() {
			String text = null;
			if (selectedPlace != null)
				text = selectedPlace.getName();
			if (text == null)
				text = getResources().getString(R.string.action_location_default);
			setText2(text);
		}
	}
	
	private class HitListElement extends BaseListElement {
		private final String[] enemies;
		private final String[] enemyUrls;
		private String enemyChoice = null;
		private String enemyChoiceUrl = null;
		private static final String ENEMY_KEY = "enemy";
		private static final String ENEMY_URL_KEY = "enemy_url";
		
		public HitListElement(int requestCode) {
			super(getActivity().getResources().getDrawable(R.drawable.action_eating), getActivity().getResources().getString(R.string.action_hitting), getActivity().getResources().getString(R.string.action_hitting_default), requestCode);
			enemies = getActivity().getResources().getStringArray(R.array.enemies);
			enemyUrls = getActivity().getResources().getStringArray(R.array.enemy_urls);
		}
		
		@Override
		protected View.OnClickListener getOnClickListener() {
			return new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showEnemyOptions();
				}
			};
		}
		
		@Override
		protected void onSaveInstanceState(Bundle bundle) {
			if(enemyChoice != null && enemyChoiceUrl != null) {
				bundle.putString(ENEMY_KEY, enemyChoice);
				bundle.putString(ENEMY_URL_KEY, enemyChoiceUrl);
			}
		}
		
		@Override
		protected boolean restoreState(Bundle savedState) {
			String enemy = savedState.getString(ENEMY_KEY);
			String enemyUrl = savedState.getString(ENEMY_URL_KEY);
			if(enemy != null && enemyUrl != null) {
				enemyChoice = enemy;
				enemyChoiceUrl = enemyUrl;
				setEnemyText();
				return true;
			}
			return false;
		}
		
		@Override
		protected void populateOGAction(OpenGraphAction action) {
			if(enemyChoiceUrl != null) {
				HitAction hitAction = action.cast(HitAction.class);
				GameGraphObject game = GraphObject.Factory.create(GameGraphObject.class);
				game.setUrl(enemyChoiceUrl);
				hitAction.setGame(game);
			}
		}
		
		private void showEnemyOptions() {
			String title= getActivity().getResources().getString(R.string.select_target);
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(title).setCancelable(true).setItems(enemies, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int i) {
					enemyChoice = enemies[i];
					enemyChoiceUrl = enemyUrls[i];
					setEnemyText();
					notifyDataChanged();
				}
			});
			builder.show();
		}
		
		private void setEnemyText() {
			if(enemyChoice != null && enemyChoiceUrl != null) {
				setText2(enemyChoice);
				announceButton.setEnabled(true);
			}
			else {
				setText2(getActivity().getResources().getString(R.string.action_hitting_default));
				announceButton.setEnabled(false);
			}
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.selection, container, false);

        profilePictureView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
        profilePictureView.setCropped(true);
        userNameView = (TextView) view.findViewById(R.id.selection_user_name);
        announceButton = (Button) view.findViewById(R.id.announce_button);
        listView = (ListView) view.findViewById(R.id.selection_list);

        announceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAnnounce();
            }
        });

        init(savedInstanceState);

        return view;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REAUTH_ACTIVITY_CODE)
			uiHelper.onActivityResult(requestCode, resultCode, data);
		else if (resultCode == Activity.RESULT_OK && requestCode >= 0 && requestCode < listElements.size())
			listElements.get(requestCode).onActivityResult(data);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}
	
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		for(BaseListElement listElement : listElements)
			listElement.onSaveInstanceState(bundle);
		bundle.putBoolean(PENDING_ANNOUNCE_KEY, pendingAnnounce);
		uiHelper.onSaveInstanceState(bundle);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	public void onSessionStateChange(final Session session, SessionState state, Exception e) {
		if(session != null && session.isOpened()) {
			if(state.equals(SessionState.OPENED_TOKEN_UPDATED))
				tokenUpdated();
			else
				makeMeRequest(session);
		}
	}
	
	private void makeMeRequest(final Session session) {
		Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response) {
				if(session == Session.getActiveSession()) {
					if(user != null) {
						profilePictureView.setProfileId(user.getId());
						userNameView.setText(user.getName());
					}
				}
				if(response.getError() != null) {
					handleError(response.getError());
				}
			}
		});
		request.executeAsync();
	}
	
	private void startPickerActivity(Uri data, int requestCode) {
		Intent intent = new Intent();
		intent.setData(data);
		intent.setClass(getActivity(), PickerActivity.class);
		startActivityForResult(intent, requestCode);
	}
	
	private void requestPublishPermissions(Session session) {
		if(session != null) {
			Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, PERMISSIONS).setRequestCode(REAUTH_ACTIVITY_CODE);
			session.requestNewPublishPermissions(newPermissionsRequest);
		}
	}
	
	private void handleAnnounce() {
		pendingAnnounce = false;
		Session session = Session.getActiveSession();
		if(session == null || !session.isOpened())
			return;
		
		List<String> permissions = session.getPermissions();
		if(!permissions.containsAll(PERMISSIONS)) {
			pendingAnnounce = true;
			requestPublishPermissions(session);
			return;
		}
		progressDialog = ProgressDialog.show(getActivity(), "", getActivity().getResources().getString(R.string.progress_dialog_text), true);
		AsyncTask<Void, Void, Response> task = new AsyncTask<Void, Void, Response>() {
			@Override
			protected Response doInBackground(Void... voids) {
				HitAction hitAction = GraphObject.Factory.create(HitAction.class);
				for(BaseListElement element : listElements) {
					element.populateOGAction(hitAction);
				}
				Request request = new Request(Session.getActiveSession(), POST_ACTION_PATH, null, HttpMethod.POST);
				request.setGraphObject(hitAction);
				return request.executeAndWait();
			}
			
			@Override
			protected void onPostExecute(Response response) {
				onPostActionResponse(response);
			}
		};
		task.execute();
	}
	
	private void tokenUpdated() {
		if(pendingAnnounce) {
			handleAnnounce();
		}
	}
	
	private void handleError(FacebookRequestError error) {
		DialogInterface.OnClickListener listener = null;
		String dialogBody = null;
		
		if(error == null)
			dialogBody = getString(R.string.error_dialog_default_text);
		else {
			switch (error.getCategory()) {
			case AUTHENTICATION_RETRY:
				String userAction = (error.shouldNotifyUser()) ? "" : getString(error.getUserActionMessageId());
				dialogBody = getString(R.string.error_authentication_retry, userAction);
				listener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_VIEW, M_FACEBOOK_URL);
						startActivity(intent);
					}
				};
				break;
				
			case AUTHENTICATION_REOPEN_SESSION:
				dialogBody = getString(R.string.error_authentication_reopen);
				listener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Session session = Session.getActiveSession();
						if(session != null && !session.isClosed())
							session.closeAndClearTokenInformation();
					}
				};
				break;
				
			case PERMISSION:
				dialogBody = getString(R.string.error_permission);
				listener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						pendingAnnounce = true;
						requestPublishPermissions(Session.getActiveSession());
					}
				};
				break;
				
			case SERVER:
			case THROTTLING:
				dialogBody = getString(R.string.error_server);
				break;
				
			case BAD_REQUEST:
				dialogBody = getString(R.string.error_bad_request, error.getErrorMessage());
				break;
				
			case OTHER:
			case CLIENT:
				dialogBody = getString(R.string.error_unknown, error.getErrorMessage());
				break;
			}
		}
		
		new AlertDialog.Builder(getActivity()).setPositiveButton(R.string.error_dialog_button_text, listener).setTitle(R.string.error_dialog_title).setMessage(dialogBody).show();
	}
	
	private interface PostResponse extends GraphObject {
		String getId();
	}
	
	private void onPostActionResponse(Response response) {
		if(progressDialog != null) {
			progressDialog.dismiss();
            progressDialog = null;
		}
		
		if(getActivity() == null)
			return;
		
		PostResponse postResponse = response.getGraphObjectAs(PostResponse.class);
		if(postResponse != null && postResponse.getId() != null) {
			String dialogBody = String.format(getString(R.string.result_dialog_text), postResponse.getId());
			new AlertDialog.Builder(getActivity()).setPositiveButton(R.string.result_dialog_button_text, null).setTitle(R.string.result_dialog_title).setMessage(dialogBody).show();
			init(null);
		}
		else
			handleError(response.getError());
	}
	
	private void init(Bundle savedInstanceState) {
		announceButton.setEnabled(false);
		
		listElements = new ArrayList<BaseListElement>();
		listElements.add(new HitListElement(0));
		listElements.add(new LocationListElement(1));
		listElements.add(new PeopleListElement(2));
		
		if(savedInstanceState != null) {
			for(BaseListElement listElement : listElements)
				listElement.restoreState(savedInstanceState);
			pendingAnnounce = savedInstanceState.getBoolean(PENDING_ANNOUNCE_KEY, false);
		}
		
		listView.setAdapter(new ActionListAdapter(getActivity(), R.id.selection_list, listElements));
		
		Session session = Session.getActiveSession();
		if(session != null && session.isOpened())
			makeMeRequest(session);
	}
}