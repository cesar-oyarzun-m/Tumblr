package com.example.tumblr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.tumblr.fragment.FeedListFragment;

/**
 * Feed List from User Activity
 * @author Cesar Oyarzun
 *
 */
public class FeedUserListActivity extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);
		if (fragment == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new FeedListFragment()).commit();
		}
		
	}
	
	
	
}
