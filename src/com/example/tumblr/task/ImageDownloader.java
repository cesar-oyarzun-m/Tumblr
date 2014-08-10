package com.example.tumblr.task;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.example.tumblr.model.TumblrModel;

/**
 * Image Downloader Thread
 * @author Cesar Oyarzun
 *
 * @param <Token>
 */
public class ImageDownloader<Token> extends HandlerThread {
	private static final String TAG = "ThumbnailDownloader";
	private static final int MESSAGE_DOWNLOAD = 0;
	private Handler mHandler;
	private Map<Token, String> requestMap = Collections
			.synchronizedMap(new HashMap<Token, String>());
	private Handler mResponseHandler;
	private Listener<Token> mListener;

	/**
	 * Listtener to update UI
	 * @author Cesar Oyarzun
	 *
	 * @param <Token>
	 */
	public interface Listener<Token> {
		void onThumbnailDownloaded(Token token, Bitmap thumbnail);
	}
	
	/**
	 * Set listner
	 * @param listener
	 */
	public void setListener(Listener<Token> listener) {
		mListener = listener;
	}

	@TargetApi(12)
	public ImageDownloader(Handler responseHandler) {
		super(TAG);
		mResponseHandler = responseHandler;
		
	}
	
	/**
	 * Add the to the que
	 * @param token
	 * @param url
	 */
	public void queueThumbnail(Token token, String url) {
		requestMap.put(token, url);
		mHandler.obtainMessage(MESSAGE_DOWNLOAD, token).sendToTarget();
	}
	
	/**
	 * Hanle the request on the thread
	 * @param token
	 */
	private void handleRequest(final Token token) {
		try {
			final String url = requestMap.get(token);
			if (url == null)
				return;
			if (getBitmapFromMemCache(url) == null) {
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				byte[] bitmapBytes = new ImageFetch().getUrlBytes(url);
				BitmapFactory.decodeByteArray(bitmapBytes, 0,bitmapBytes.length,options);
				
				// Calculate inSampleSize
			    options.inSampleSize = calculateInSampleSize(options, 200, 200);
			    
			 // Decode bitmap with inSampleSize set
			    options.inJustDecodeBounds = false;
			    byte[] bitmapBytes1 = new ImageFetch().getUrlBytes(url);
				Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes1, 0,bitmapBytes1.length,options);
				addBitmapToMemoryCache(url, bitmap);
			}
			mResponseHandler.post(new Runnable() {
				public void run() {
					if (requestMap.get(token) != url)
						return;
					requestMap.remove(token);
					mListener.onThumbnailDownloaded(token,
							getBitmapFromMemCache(url));
				}
			});
		} catch (IOException ioe) {
			Log.e(TAG, "Error downloading image", ioe);
		}
	}

	
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onLooperPrepared() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == MESSAGE_DOWNLOAD) {
					@SuppressWarnings("unchecked")
					Token token = (Token) msg.obj;
					handleRequest(token);
				}
			}
		};
	}
	
	/**
	 * Clear message queue
	 */
	public void clearQueue() {
		mHandler.removeMessages(MESSAGE_DOWNLOAD);
		requestMap.clear();
	}

	/**
	 * Add bitmap to memory cache
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			TumblrModel.getInstance().getmMemoryCache().put(key, bitmap);
		}
	}

	/**
	 * Get bitmap from memory cache
	 * @param key
	 * @return
	 */
	public Bitmap getBitmapFromMemCache(String key) {
		return TumblrModel.getInstance().getmMemoryCache().get(key);
	}
	
	/**
	 * Calculate Image size 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}
	
}