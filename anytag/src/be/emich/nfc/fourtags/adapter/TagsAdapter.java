package be.emich.nfc.fourtags.adapter;

import be.emich.nfc.fourtags.provider.FourTagsProvider;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TagsAdapter extends CursorAdapter {

	public TagsAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder)view.getTag();
		if(holder!=null){
			holder.textView1.setText(cursor.getString(cursor.getColumnIndex(FourTagsProvider.COLUMN_TAGMAP_TAGID)));
			holder.textView2.setText(cursor.getString(cursor.getColumnIndex(FourTagsProvider.COLUMN_TAGMAP_URLACTION)));
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(android.R.layout.two_line_list_item, null);
		ViewHolder holder = new ViewHolder();
		holder.textView1 = (TextView)v.findViewById(android.R.id.text1);
		holder.textView2 = (TextView)v.findViewById(android.R.id.text2);
		v.setTag(holder);
		return v;
	}
	
	public class ViewHolder {
		TextView textView1;
		TextView textView2;
	}

}
