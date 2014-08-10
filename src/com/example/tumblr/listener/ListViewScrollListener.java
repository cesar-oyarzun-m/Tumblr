package com.example.tumblr.listener;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * Scroll listener
 * @author Cesar Oyarzun
 *
 */
public abstract class ListViewScrollListener implements OnScrollListener {
    private int visibleThreshold = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;

    public ListViewScrollListener() {
    }

    public ListViewScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount) 
        {
        if (totalItemCount < previousTotalItemCount) {
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) { this.loading = true; } 
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold)) {
            onLoadMore(totalItemCount+1);
            loading = true;
        }
    }

    public abstract void onLoadMore(int totalItemsCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}