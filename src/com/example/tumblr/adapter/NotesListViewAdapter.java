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
import com.example.tumblr.model.NoteVO;
import com.example.tumblr.task.ImageDownloader;

/**
 * Rss List View Adapter
 * 
 * @author Cesar Oyarzun
 * 
 */
public class NotesListViewAdapter extends ArrayAdapter<NoteVO> {

	private static final String TUMBLR_COM_AVATAR_48 = ".tumblr.com/avatar/48";
	private static final String HTTP_API_TUMBLR_COM_V2_BLOG = "http://api.tumblr.com/v2/blog/";
	private List<NoteVO> listRss;
	private Context context;
	private LayoutInflater inflater;
	private ImageDownloader<ImageView> imageThreadDownloader;

	static class ViewHolderItem {
		ImageView imageViewItem;
		TextView textView;
	}

	public NotesListViewAdapter(Context context, int resource,
			List<NoteVO> listRss,
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
	public NoteVO getItem(int position) {
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
	public int getPosition(NoteVO item) {
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
	
	public void setListRss(List<NoteVO> listRss) {
		this.listRss = listRss;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView; // trying to reuse a recycled view
		ViewHolderItem holder = null;
		if (vi == null) {
			// The view is not a recycled one: we have to inflate
			vi = inflater.inflate(R.layout.list_row_notes, parent, false);
			holder = new ViewHolderItem();
			holder.imageViewItem = (ImageView) vi.findViewById(R.id.imageRSS);
			holder.textView = (TextView) vi.findViewById(R.id.name);
			vi.setTag(holder);
		} else {
			// no need to inflate no need to findViews by id
			holder = (ViewHolderItem) vi.getTag();
			holder.imageViewItem.setImageDrawable(context.getResources().getDrawable(R.drawable.photo_48));
		}
		NoteVO item = getItem(position);
		holder.textView.setText(item.getName());
		imageThreadDownloader.queueThumbnail(holder.imageViewItem,
				HTTP_API_TUMBLR_COM_V2_BLOG+listRss.get(position).getName()+TUMBLR_COM_AVATAR_48);
		return vi;
	}

}
