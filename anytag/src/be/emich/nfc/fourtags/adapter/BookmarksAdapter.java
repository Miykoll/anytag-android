package be.emich.nfc.fourtags.adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.Browser;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class BookmarksAdapter extends CursorAdapter {

	public BookmarksAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context arg1, Cursor c) {
		TextView t1 = (TextView)view.findViewById(android.R.id.text1);
		TextView t2 = (TextView)view.findViewById(android.R.id.text2);
		
		t1.setText(c.getString(c.getColumnIndex(Browser.BookmarkColumns.TITLE)));
		t2.setText(c.getString(c.getColumnIndex(Browser.BookmarkColumns.URL)));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		return inflater.inflate(android.R.layout.two_line_list_item, null);
	}

}
