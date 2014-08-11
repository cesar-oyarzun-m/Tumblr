package com.example.tumblr.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.tumblr.R;
import com.example.tumblr.FeedDetailPagerActivity;
import com.example.tumblr.adapter.FeedListViewAdapter;
import com.example.tumblr.listener.ListViewScrollListener;
import com.example.tumblr.model.FeedVO;
import com.example.tumblr.model.TumblrModel;
import com.example.tumblr.task.ImageDownloader;
import com.example.tumblr.task.TumblrLoadTask;

/**
 * Feed List fragment
 * 
 * @author Cesar Oyarzun
 * 
 */
public class FeedListFragment extends ListFragment {
	private ListView listView;
	private ImageDownloader<ImageView> imageThread;
	// private static final String TAG = "RssListFragment";
	private TumblrLoadTask tumblrLoadTask = null;
	private String urlFeed = null;
	private FeedListViewAdapter listViewAdapter;
	private ArrayList<FeedVO> listFeed;

	public ArrayList<FeedVO> getListFeed() {
		return listFeed;
	}

	public void setListFeed(ArrayList<FeedVO> listFeed) {
		this.listFeed = listFeed;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		listFeed=new ArrayList<FeedVO>();
		urlFeed = (String) getActivity().getIntent().getExtras()
				.get(LoginFragment.URL);
		tumblrLoadTask = new TumblrLoadTask(this);
		tumblrLoadTask.execute(urlFeed);
		imageThread = new ImageDownloader<ImageView>(new Handler());
		imageThread.setListener(new ImageDownloader.Listener<ImageView>() {
			public void onThumbnailDownloaded(ImageView imageView,
					Bitmap thumbnail) {
				if (isVisible()) {
					imageView.setImageBitmap(thumbnail);
				}
			}
		});
		imageThread.start();
		imageThread.getLooper();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_feed_list,
				container, false);
		listView = (ListView) rootView.findViewById(android.R.id.list);
		listView.setOnScrollListener(new ListViewScrollListener() {
			@Override
			public void onLoadMore(int totalItemsCount) {
				customLoadMoreDataFromApi(totalItemsCount);
			}
		});
		setupAdapter();
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		return rootView;
	}

	/**
	 * Append more data into the adapter
	 * 
	 * @param offset
	 */
	public void customLoadMoreDataFromApi(int offset) {
		String urlFeed = (String) getActivity().getIntent().getExtras()
				.get(LoginFragment.URL);
		String[] myStringArray = { urlFeed, Integer.toString(offset) };
		new TumblrLoadTask(this).execute(myStringArray);
	}

	/**
	 * Setup adapter list
	 */
	public void setupAdapter() {

		if (getActivity() == null || listView == null)
			return;
		if (listView.getAdapter() == null) {
			listViewAdapter = new FeedListViewAdapter(getActivity(),
					android.R.id.list, listFeed,
					this.imageThread);
			listView.setAdapter(listViewAdapter);
		} else {
//			listViewAdapter.clear();
			listViewAdapter.addAll(listFeed);
			listViewAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		FeedVO feedVO = (FeedVO) listView.getAdapter().getItem(position);
		// Start PagerActivity
		Intent i = new Intent(getActivity(), FeedDetailPagerActivity.class);
		i.putExtra(FeedDetailFragment.SELECTED_FEED, feedVO);
		i.putExtra(FeedDetailFragment.MODEL, this.listFeed);
		startActivity(i);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		case R.id.menu_item_refresh:
//			TumblrModel.getInstance().clearListFeed();
			listViewAdapter.clear();
			listFeed=new  ArrayList<FeedVO>();
			new TumblrLoadTask(this).execute(urlFeed);
//			listViewAdapter.setListRss(new ArrayList<FeedVO>());
//			setupAdapter();
//			listViewAdapter.notifyDataSetChanged();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		imageThread.quit();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		imageThread.clearQueue();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

}
