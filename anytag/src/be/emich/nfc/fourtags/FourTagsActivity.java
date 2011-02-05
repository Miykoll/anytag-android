package be.emich.nfc.fourtags;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import be.emich.nfc.fourtags.adapter.TagsAdapter;
import be.emich.nfc.fourtags.provider.FourTagsProvider;

public class FourTagsActivity extends ListActivity {
	private static final int DIALOG_ABOUT=25301;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TagsAdapter adapter = new TagsAdapter(this, this.getContentResolver().query(Uri.withAppendedPath(FourTagsProvider.CONTENT_URI, FourTagsProvider.TABLE_TAGMAP),null,null,null,null));
		setListAdapter(adapter);
		setContentView(R.layout.main);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Cursor c = ((CursorAdapter)getListAdapter()).getCursor();
		c.moveToPosition(position);
		int _id = c.getInt(c.getColumnIndex("_id"));
		
		Intent i = new Intent(this,FourTagsLinkActivity.class);
		i.putExtra(FourTagsLinkActivity.EXTRA_ID, _id);
		startActivity(i);
		
		//super.onListItemClick(l, v, position, id);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id){
		case DIALOG_ABOUT:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.about);
			builder.setMessage(R.string.aboutmessage);
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					removeDialog(DIALOG_ABOUT);
				}
			});
			return builder.create();
		
		}
		return super.onCreateDialog(id);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,DIALOG_ABOUT,0,R.string.about).setIcon(android.R.drawable.ic_menu_info_details);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case DIALOG_ABOUT:
			showDialog(DIALOG_ABOUT);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void about(View v){
		showDialog(DIALOG_ABOUT);
	}
}
