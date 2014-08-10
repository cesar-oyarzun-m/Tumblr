package com.example.tumblr.unittest;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.example.tumblr.MainActivity;
import com.example.tumblr.R;

/**
 * URL tumblr test
 * @author Cesar Oyarzun
 *
 */

public class UrlTumblrUnitTest extends ActivityInstrumentationTestCase2<MainActivity>{

	private MainActivity mainActivity;
    private TextView urlTextField;
	
    public UrlTumblrUnitTest() {
        super(MainActivity.class);
    }

	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		 mainActivity = getActivity();
	     urlTextField = (TextView) mainActivity.findViewById(R.id.urlTextField);
	}
	
	public void testPreconditions() {
        assertNotNull("mainActivity is null", mainActivity);
        assertNotNull("urlTextField is null", urlTextField);
    }
	/**
     * Tests the correctness of the initial text.
     */
    public void testMainActivity_urlText() {
        final String actual = urlTextField.getText().toString();
        assertFalse("Url must contain tumblr.com", !actual.contains(".tumblr.com"));
    }

}
