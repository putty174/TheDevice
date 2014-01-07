package com.pressx.thedevice;

import com.facebook.FacebookException;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.PlacePickerFragment;
import com.pressx.thedevice.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class PickerActivity extends FragmentActivity{
	public static final Uri FRIEND_PICKER = Uri.parse("picker://friend");
	public static final Uri PLACE_PICKER = Uri.parse("picker://place");
	private FriendPickerFragment friendPickerFragment;
	private PlacePickerFragment placePickerFragment;
	private LocationListener locationListener;
	private static final Location SAN_FRANCISCO_LOCATION = new Location("") {{ setLatitude(37.7750); setLongitude(-122.4183);}};
	private static final int SEARCH_RADIUS_METERS = 1000;
	private static final int SEARCH_RESULT_LIMIT = 50;
	private static final int LOCATION_CHANGE_THRESHOLD = 50;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pickers);
		
		Bundle args = getIntent().getExtras();
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragmentToShow = null;
		Uri intentUri = getIntent().getData();
		
		if(FRIEND_PICKER.equals(intentUri)) {
			if(savedInstanceState == null)
				friendPickerFragment = new FriendPickerFragment(args);
			else
				friendPickerFragment = (FriendPickerFragment) manager.findFragmentById(R.id.picker_fragment);
			friendPickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
				@Override
				public void onError(PickerFragment<?> fragment, FacebookException error) {
					PickerActivity.this.onError(error);
				}
			});
			friendPickerFragment.setOnDoneButtonClickedListener(new PickerFragment.OnDoneButtonClickedListener() {
				@Override
				public void onDoneButtonClicked(PickerFragment<?> fragment) {
					finishActivity();
				}
			});
			fragmentToShow = friendPickerFragment;
		}
		else if(PLACE_PICKER.equals(intentUri)) {
			if(savedInstanceState == null)
				placePickerFragment = new PlacePickerFragment(args);
			else
				placePickerFragment = (PlacePickerFragment) manager.findFragmentById(R.id.picker_fragment);
			placePickerFragment.setOnSelectionChangedListener(new PickerFragment.OnSelectionChangedListener() {
				@Override
				public void onSelectionChanged(PickerFragment<?> fragment) {
					finishActivity();
				}
			});
			placePickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
				@Override
				public void onError(PickerFragment<?> fragment, FacebookException error) {
					PickerActivity.this.onError(error);
				}
			});
			placePickerFragment.setOnDoneButtonClickedListener(new PickerFragment.OnDoneButtonClickedListener() {
				@Override
				public void onDoneButtonClicked(PickerFragment<?> fragment) {
					finishActivity();
				}
			});
			fragmentToShow = placePickerFragment;
		}
		else {
			setResult(RESULT_CANCELED);
			finish();
			return;
		}
		manager.beginTransaction().replace(R.id.picker_fragment, fragmentToShow).commit();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(FRIEND_PICKER.equals(getIntent().getData()))
		{
			try {
				friendPickerFragment.loadData(false);
			} catch (Exception ex) {
				onError(ex);
			}
		}
		else if (PLACE_PICKER.equals(getIntent().getData())) {
			try {
				Location location = null;
				Criteria criteria = new Criteria();
				LocationManager locationManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				String bestProvider = locationManger.getBestProvider(criteria, false);
				if(bestProvider != null) {
					location = locationManger.getLastKnownLocation(bestProvider);
					if(locationManger.isProviderEnabled(bestProvider) && locationListener == null) {
						locationListener = new LocationListener() {
							@Override
							public void onStatusChanged(String provider, int status, Bundle extras) {
								
							}
							
							@Override
							public void onProviderEnabled(String provider) {
								
							}
							
							@Override
							public void onProviderDisabled(String provider) {
								
							}
							
							@Override
							public void onLocationChanged(Location location) {
								float distance = location.distanceTo(placePickerFragment.getLocation());
								if(distance >= LOCATION_CHANGE_THRESHOLD) {
									placePickerFragment.setLocation(location);
									placePickerFragment.loadData(true);
								}
							}
						};
						locationManger.requestLocationUpdates(bestProvider, 1, LOCATION_CHANGE_THRESHOLD, locationListener, Looper.getMainLooper());
					}
				}
				if(location == null) {
					String model = Build.MODEL;
					if(model.equals("sdk") || model.equals("google_sdk") || model.contains("x86")) {
						location = SAN_FRANCISCO_LOCATION;
					}
				}
				if(location != null) {
					placePickerFragment.setLocation(location);
					placePickerFragment.setRadiusInMeters(SEARCH_RADIUS_METERS);
					placePickerFragment.setResultsLimit(SEARCH_RESULT_LIMIT);
					placePickerFragment.loadData(true);
				}
				else {
					onError(getResources().getString(R.string.no_location_error), true);
				}
			} catch (Exception e){
				onError(e);
			}
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(locationListener != null) {
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationManager.removeUpdates(locationListener);
			locationListener = null;
		}
	}
		
	private void onError(Exception error) {
		onError(error.getLocalizedMessage(), false);
	}
	
	private void onError(String error, final boolean finishActivity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.error_dialog_title).setMessage(error).setPositiveButton(R.string.error_dialog_button_text, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(finishActivity) {
					finishActivity();
				}
			}
		});
		builder.show();
	}
	
	private void finishActivity() {
		FacebookApplication app = (FacebookApplication) getApplication();
		if(FRIEND_PICKER.equals(getIntent().getData())) {
			if(friendPickerFragment != null)
				app.setSelectedUsers(friendPickerFragment.getSelection());
		}
		else if(PLACE_PICKER.equals(getIntent().getData())) {
			if(placePickerFragment != null)
				app.setSelectedPlace(placePickerFragment.getSelection());
		}
		
		setResult(RESULT_OK, null);
		finish();
	}
}