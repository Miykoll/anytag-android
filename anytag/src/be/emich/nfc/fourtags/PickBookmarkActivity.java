package be.emich.nfc.fourtags;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.view.View;
import android.widget.ListView;
import be.emich.nfc.fourtags.adapter.BookmarksAdapter;

public class PickBookmarkActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setListAdapter(new BookmarksAdapter(this, getContentResolver().query(Uri.parse("content://browser/bookmarks"), null,Browser.BookmarkColumns.BOOKMARK+"=1", null, null)));		
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Cursor c = getContentResolver().query(Uri.parse("content://browser/bookmarks"), null, Browser.BookmarkColumns.BOOKMARK+"=1", null, null);
		c.moveToPosition(position);
		String url = c.getString(c.getColumnIndex("url"));
		c.close();
		Intent i = new Intent();
		i.putExtra(FourTagsLinkActivity.EXTRA_URL, url);
		setResult(Activity.RESULT_OK, i);
		finish();
		super.onListItemClick(l, v, position, id);
	}
}
