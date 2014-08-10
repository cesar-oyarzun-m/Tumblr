package com.example.tumblr.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.tumblr.fragment.FeedListFragment;
import com.example.tumblr.model.FeedVO;
import com.example.tumblr.model.TumblrModel;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.PhotoSize;
import com.tumblr.jumblr.types.Post;

/**
 * Load Tumblr url blogs post
 * @author Cesar Oyarzun
 *
 */
public class TumblrLoadTask extends AsyncTask<String, Void, List<FeedVO>> {
	private static final String LOADING = "Loading....";
	private static final String HREF = "href";
	private static final String A_HREF = "a[href]";
	private static final String P_HTML = "p";
	private static final String LIMIT_SIZE = "10";
	private static final String LIMIT = "limit";
	private static final String OFFSET = "offset";
	private static final String STRONG = "strong";
	public boolean parsingComplete = true;
	private FeedListFragment rssListFragment;
	private ProgressDialog dialog;
	
	public TumblrLoadTask(FeedListFragment rssListFragment) {
		this.rssListFragment=rssListFragment;
		dialog = new ProgressDialog(rssListFragment.getActivity());
	}
	
	@Override
	protected void onPreExecute() {
		this.dialog.setMessage(LOADING);
        this.dialog.show();
	}
	
	@Override
	protected List<FeedVO> doInBackground(String... params) {
		// Authenticate via OAuth
		JumblrClient client = new JumblrClient(
		  "upOiHxQOt1Hjbpp1jp4cvxlRIzfjiKlQA2aMG2kQ70ez62qizT",
		  "b62mUCiADy3C301d0FOLQvnEOXYwbeqgLmqvTHukaL5DEov5oJ"
		);
		client.setToken(
		  "6ZeiYLpTXvqakyDknhl6mb6s5R0K1IrV9iHOOKX4IWTwX2beUD",
		  "NucxlQuS4Hm1tqF9S4BRSkZJJds5HVM7ME29dhMeLaKZPQn1Fu"
		);

		Map<String, String> options = new HashMap<String, String>();
		options.put(LIMIT, LIMIT_SIZE);
		if(params.length==2){
			options.put(OFFSET, params[1]);	
		}
		
		List<Post> blogPosts = client.blogPosts(params[0],options);
		for (Post post : blogPosts) {
			if(post instanceof PhotoPost){
				PhotoPost photoPost=(PhotoPost) post;
				FeedVO rssVO=new FeedVO();
				Document doc = Jsoup.parse(photoPost.getCaption().toString());
				Element strong = doc.select(STRONG).first();
				Element p = doc.select(P_HTML).first();
				if(strong!=null){
					rssVO.setTitle(strong.text());
				}else if(p!=null){
					rssVO.setTitle(p.text());
				}
				
				Element href = doc.select(A_HREF).first();
				if(href!=null){
					String hrefVal = href.attr(HREF);
					rssVO.setHref(hrefVal);
				}
				rssVO.setLink(post.getShortUrl());
				Photo photo = photoPost.getPhotos().get(0);
				PhotoSize photoSize = photo.getSizes().get(0);
				String url = photoSize.getUrl();
				rssVO.setUrlImage(url);
				TumblrModel.getInstance().getListRss().add(rssVO);
			}
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(List<FeedVO> result) {
		this.rssListFragment.setupAdapter();
		if (dialog.isShowing()) {
          dialog.dismiss();
      }
	}
}
