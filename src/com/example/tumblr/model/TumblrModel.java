package com.example.tumblr.model;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.tumblr.jumblr.JumblrClient;

/**
 * Model singleton class
 * 
 * @author Cesar Oyarzun
 * 
 */
public class TumblrModel {
	private static TumblrModel model = null;
	private LruCache<String, Bitmap> mMemoryCache;
	private JumblrClient client=null;

	@TargetApi(12)
	public TumblrModel() {
		// Get max available VM memory, exceeding this amount will throw an
		// OutOfMemory exception. Stored in kilobytes as LruCache takes an
		// int in its constructor.
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 8;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getByteCount() / 1024;
			}
		};
		// Authenticate via OAuth
		client = new JumblrClient(
				"upOiHxQOt1Hjbpp1jp4cvxlRIzfjiKlQA2aMG2kQ70ez62qizT",
				"b62mUCiADy3C301d0FOLQvnEOXYwbeqgLmqvTHukaL5DEov5oJ");
		client.setToken("6ZeiYLpTXvqakyDknhl6mb6s5R0K1IrV9iHOOKX4IWTwX2beUD",
				"NucxlQuS4Hm1tqF9S4BRSkZJJds5HVM7ME29dhMeLaKZPQn1Fu");
	}

	/**
	 * Get Model Instance
	 * 
	 * @return {@link TumblrModel}
	 */
	public static TumblrModel getInstance() {
		if (model == null) {
			model = new TumblrModel();

		}
		return model;
	}

	/**
	 * Get image chache
	 * 
	 * @return LruCache
	 */
	public LruCache<String, Bitmap> getmMemoryCache() {
		return mMemoryCache;
	}

	
	/**
	 * Get Tumbrl Client
	 * @return
	 */
	public JumblrClient getClient() {
		return client;
	}
}
