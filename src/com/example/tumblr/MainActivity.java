package com.example.tumblr;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.tumblr.fragment.LoginFragment;

/**
 * Main Activity load fragment login
 * @author Cesar Oyarzun
 *
 */
public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new LoginFragment()).commit();
		}
	}
}
