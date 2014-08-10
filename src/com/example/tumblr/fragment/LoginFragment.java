package com.example.tumblr.fragment;

import android.content.Context;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tumblr.R;
import com.example.tumblr.FeedListActivity;
import com.example.tumblr.model.TumblrModel;

/**
 * Load Info from User or URL feed
 * @author Cesar Oyarzun
 *
 */
public class LoginFragment extends Fragment {
	public static final String URL = "url";
	Button loadButton;
	TextView userNameTextView;
	TextView urlTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		urlTextView = (TextView) rootView.findViewById(R.id.urlTextField);
		loadButton = (Button) rootView.findViewById(R.id.loadButton);
		loadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isOnline()) {
					TumblrModel.getInstance().clearListFeed();
					Intent intent = new Intent(getActivity().getApplicationContext(), FeedListActivity.class);
					intent.putExtra(URL,urlTextView.getText().toString());
					startActivity(intent);
				} else {
					showToastMessage(getResources().getString(R.string.no_network_connection_toast));
				}
			}
		});
		return rootView;
	}

	/**
	 * Check if there is internet connection
	 * @return {@link Boolean}
	 */
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
		return isConnected;
	}

	/**
	 * Show toast message
	 * @param message
	 */
	private void showToastMessage(String message) {
		Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
	}
}
