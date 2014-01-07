package com.pressx.thedevice;

import java.util.List;

import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;

import android.app.Application;

public class FacebookApplication extends Application {
	private List<GraphUser> selectedUsers;
	private GraphPlace selectedPlace;
	
	public List<GraphUser> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<GraphUser> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public GraphPlace getSelectedPlace() {
		return selectedPlace;
	}

	public void setSelectedPlace(GraphPlace selectedPlace) {
		this.selectedPlace = selectedPlace;
	}
}