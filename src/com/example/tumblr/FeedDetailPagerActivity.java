package com.example.tumblr;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.tumblr.fragment.FeedDetailFragment;
import com.example.tumblr.model.FeedVO;

/**
 * Detail Feed Pager Activity to swipe on the list
 * 
 * @author Cesar Oyarzun
 * 
 */
public class FeedDetailPagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<FeedVO> feedModel;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		feedModel = (ArrayList<FeedVO>) getIntent().getSerializableExtra(
				FeedDetailFragment.MODEL);
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return feedModel.size();
			}

			@Override
			public Fragment getItem(int pos) {
				FeedVO word = feedModel.get(pos);
				return FeedDetailFragment.newInstance(word);
			}
		});

		FeedVO feed = (FeedVO) getIntent().getSerializableExtra(
				FeedDetailFragment.SELECTED_FEED);
		getActionBar().setTitle(feed.getTitle());
		for (int i = 0; i < feedModel.size(); i++) {
			if (feedModel.get(i).getTitle().equals(feed.getTitle())) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}

		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					public void onPageScrollStateChanged(int state) {
					}

					public void onPageScrolled(int pos, float posOffset,
							int posOffsetPixels) {
					}

					public void onPageSelected(int pos) {
						FeedVO word = feedModel.get(pos);
						if (word.getTitle() != null) {
							setTitle(word.getTitle());
							getActionBar().setTitle(word.getTitle());
						}
					}
				});
	}

}
