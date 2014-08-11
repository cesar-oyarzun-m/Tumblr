package com.example.tumblr.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tumblr.R;
import com.example.tumblr.model.FeedVO;
import com.example.tumblr.task.ImageDownloader;

/**
 * Rss List View Adapter
 * 
 * @author Cesar Oyarzun
 * 
 */
public class FeedListViewAdapter extends ArrayAdapter<FeedVO> {

	private List<FeedVO> listRss;
	private Context context;
	private LayoutInflater inflater;
	private ImageDownloader<ImageView> imageThreadDownloader;

	static class ViewHolderItem {
		ImageView imageViewItem;
		TextView textView;
	}

	public FeedListViewAdapter(Context context, int resource,
			List<FeedVO> listRss,
			ImageDownloader<ImageView> imageThreadDownloader) {
		super(context, resource, listRss);
		this.context = context;
		this.listRss = listRss;
		this.imageThreadDownloader = imageThreadDownloader;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Context getContext() {
		return context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		return listRss.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FeedVO getItem(int position) {
		return listRss.get(position);
	}

	/**
	 * Returns the position of the specified item in the array.
	 * 
	 * @param item
	 *            The item to retrieve the position of.
	 * 
	 * @return The position of the specified item.
	 */
	@Override
	public int getPosition(FeedVO item) {
		return listRss.indexOf(item);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	
	public void setListRss(List<FeedVO> listRss) {
		this.listRss = listRss;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView; // trying to reuse a recycled view
		ViewHolderItem holder = null;
		if (vi == null) {
			// The view is not a recycled one: we have to inflate
			vi = inflater.inflate(R.layout.list_row, parent, false);
			holder = new ViewHolderItem();
			holder.imageViewItem = (ImageView) vi.findViewById(R.id.imageRSS);
			holder.textView = (TextView) vi.findViewById(R.id.name);
			vi.setTag(holder);
		} else {
			// no need to inflate no need to findViews by id
			holder = (ViewHolderItem) vi.getTag();
			holder.imageViewItem.setImageDrawable(context.getResources().getDrawable(R.drawable.empty_photo));
		}
		FeedVO item = getItem(position);
		holder.textView.setText(item.getTitle());
		imageThreadDownloader.queueThumbnail(holder.imageViewItem,
				listRss.get(position).getUrlImage());
		return vi;
	}

}
