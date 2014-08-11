package com.example.tumblr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.graphics.Bitmap;

/**
 * Feed  VO
 * @author Cesar Oyarzun
 *
 */
public class FeedVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private Bitmap image;
	private String urlImage;
	private String notes_count;
	private List<String> tags;
	private List<NoteVO> reblog;
	
	
	public FeedVO (){
		reblog=new ArrayList<NoteVO>();
	}
	
	public List<NoteVO> getReblog() {
		return reblog;
	}


	public String getNotes_count() {
		return notes_count;
	}
	public void setNotes_count(String notes_count) {
		this.notes_count = notes_count;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public String getUrlImage() {
		return urlImage;
	}
	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTagsToString(){
		return  StringUtils.join(tags.toArray(), ",");
	}
}
