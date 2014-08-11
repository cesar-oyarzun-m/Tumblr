package com.example.tumblr.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tumblr.R;
import com.example.tumblr.model.FeedVO;
import com.example.tumblr.model.TumblrModel;

/**
 * Feed Detail fragment
 * @author Cesar Oyarzun
 *
 */
public class FeedDetailFragment extends Fragment {
	public static final String SELECTED_FEED = "selected_feed";
	public static final String MODEL = "model";
	
	/**
	 * New Rss Detail Fragment
	 * @param feed
	 * @return
	 */
	public static FeedDetailFragment newInstance(FeedVO feed) {
		Bundle args = new Bundle();
		args.putSerializable(SELECTED_FEED, feed);
		FeedDetailFragment fragment = new FeedDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_rss_detail, container,false);
		FeedVO feedVO = (FeedVO) getArguments().getSerializable(FeedDetailFragment.SELECTED_FEED);
		TextView titleTextView = (TextView) rootView.findViewById(R.id.title);
		titleTextView.setText(feedVO.getTitle());
		ImageView image = (ImageView) rootView.findViewById(R.id.imageViewDetail);
		Bitmap bitmap = TumblrModel.getInstance().getmMemoryCache().get(feedVO.getUrlImage());
		image.setImageBitmap(bitmap);
		TextView descriptionTextView = (TextView) rootView.findViewById(R.id.desc);
		descriptionTextView.setText(feedVO.getDesc());
		TextView postTextView = (TextView) rootView.findViewById(R.id.post);
		postTextView.setText(feedVO.getLink());
		TextView hRefTextView = (TextView) rootView.findViewById(R.id.href);
		hRefTextView.setText(feedVO.getHref());
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		TextView tagsTextView = (TextView) rootView.findViewById(R.id.tags);
		tagsTextView.setText(feedVO.getTags().toString());
		
		return rootView;
	}

}
