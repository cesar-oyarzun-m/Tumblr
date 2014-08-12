package com.example.tumblr.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;

import com.example.tumblr.fragment.FeedListFragment;
import com.example.tumblr.model.FeedVO;

/**
 * Feed reader from RSS
 * @author Cesar Oyarzun
 *
 */
@Deprecated
public class FeedRssLoadTask extends AsyncTask<String, Void, List<FeedVO>> {
	private static final String IMG_SRC$_JPG = "img[src$=.jpg]";
	private static final String LINK = "link";
	private static final String SRC = "src";
	private static final String DESCRIPTION = "description";
	private static final String TITLE = "title";
	private static final String ITEM = "item";
	public boolean parsingComplete = true;
	private FeedListFragment rssListFragment;
	

	public FeedRssLoadTask(FeedListFragment rssListFragment) {
		this.rssListFragment=rssListFragment;
	}
	
	
	
	@Override
	protected List<FeedVO> doInBackground(String... params) {
		ArrayList<FeedVO> listRss = new ArrayList<FeedVO>();
		try {
			URL url = new URL(params[0]);
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(url.openConnection().getInputStream(), "UTF_8");
			FeedVO rssVO = null;
			boolean insideItem = false;
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {

					if (xpp.getName().equalsIgnoreCase(ITEM)) {
						insideItem = true;
						rssVO = new FeedVO();
					} else if (xpp.getName().equalsIgnoreCase(TITLE)) {
						if (insideItem)
							rssVO.setTitle(xpp.nextText());

					} else if (xpp.getName().equalsIgnoreCase(LINK)) {
					} else if (xpp.getName().equalsIgnoreCase(DESCRIPTION)) {
						if (insideItem) {
							String nextText = xpp.nextText();
							Document doc = Jsoup.parse(nextText);
							Element first = doc.select(IMG_SRC$_JPG).first();
							if (first != null) {
								String imageURL = first.attr(SRC);
								rssVO.setUrlImage(imageURL);
							}
						}
					}

				} else if (eventType == XmlPullParser.END_TAG
						&& xpp.getName().equalsIgnoreCase(ITEM)) {
					insideItem = false;
					rssVO = null;
				}
				eventType = xpp.next(); 
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listRss;
	}
	
	@Override
	protected void onPostExecute(List<FeedVO> result) {
//		this.rssListFragment.setListRss((ArrayList<RssVO>) result);
		this.rssListFragment.setupAdapter();
	}
}
