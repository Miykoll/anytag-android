package be.emich.nfc.fourtags;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import be.emich.nfc.fourtags.provider.FourTagsProvider;

public class FourTagsLinkActivity extends Activity implements OnClickListener {
    public static final String EXTRA_ID = "_id";
    
    public static final String EXTRA_URL = "url";
    
    public static final int REQUEST_BOOKMARK = 25301;
    
    private Integer id;
    
    private Button buttonSave;
    private Button buttonDelete;
    private Button buttonBookmark;
    private EditText editTextTagContents;
    private EditText editTextTagUrl;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fourtagslinkactivity);
        
        editTextTagContents = (EditText)findViewById(R.id.editTextTagId);
        editTextTagUrl = (EditText)findViewById(R.id.editTextRedirectTo);
        
        buttonSave = (Button)findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
        
        buttonDelete = (Button)findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(this);
        
        buttonBookmark = (Button)findViewById(R.id.buttonBookmarks);
        buttonBookmark.setOnClickListener(this);
        
        editTextTagUrl.requestFocus();
        
        Cursor c = managedQuery(Uri.parse("content://browser/bookmarks"), null, null, null, null);
        c.moveToFirst();
        String[] str = c.getColumnNames();
        for(String string:str){
        	Log.v(getClass().getSimpleName(),"Str: "+string);
        }
        
        resolveIntent(getIntent());
    }
    
    protected void resolveIntent(Intent intent){
    	String action = intent.getAction();
    	
    	id = intent.getIntExtra(EXTRA_ID,-1);
    	
    	if(id!=null && id!=-1){
    		Cursor c = managedQuery(Uri.withAppendedPath(FourTagsProvider.CONTENT_URI, FourTagsProvider.TABLE_TAGMAP), null, "_id="+id, null, null);
    		c.moveToFirst();
    		String id = c.getString(c.getColumnIndex(FourTagsProvider.COLUMN_TAGMAP_TAGID));
    		String url = c.getString(c.getColumnIndex(FourTagsProvider.COLUMN_TAGMAP_URLACTION));
    		editTextTagContents.setText(id);
    		editTextTagUrl.setText(url);
    		buttonDelete.setVisibility(View.VISIBLE);
    	}
    	
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            // When a tag is discovered we send it to the service to be save. We
            // include a PendingIntent for the service to call back onto. This
            // will cause this activity to be restarted with onNewIntent(). At
            // that time we read it from the database and view it.
            byte[] byte_id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
             
            
            
    	  String hexStr = "";
    	  for (int i=0; i < byte_id.length; i++) {
    	    hexStr +=
    	          Integer.toString( ( byte_id[i] & 0xff ) + 0x100, 16).substring( 1 );
    	  }
            
            hexStr=hexStr.toUpperCase();
            
            Log.v(getClass().getSimpleName(),"Tag ID: "+hexStr);
            
            ((EditText)findViewById(R.id.editTextTagId)).setText(hexStr);
                	
        	Cursor c = managedQuery(Uri.withAppendedPath(FourTagsProvider.CONTENT_URI, FourTagsProvider.TABLE_TAGMAP), null, FourTagsProvider.COLUMN_TAGMAP_TAGID+"='"+hexStr+"'", null, null);
        	
        	if(c.getCount()!=0){
        		c.moveToFirst();
        		String url = c.getString(c.getColumnIndex(FourTagsProvider.COLUMN_TAGMAP_URLACTION));
        		Intent i = new Intent(Intent.ACTION_VIEW);
            	i.setData(Uri.parse(url));
            	startActivity(i);
            	finish();
        	}
            
        }
    }

	public void onClick(View v) {
		if(v==buttonSave){
			String tagId = editTextTagContents.getText().toString();
			String tagUrl = editTextTagUrl.getText().toString();
	
			ContentValues cv = new ContentValues();
			cv.put(FourTagsProvider.COLUMN_TAGMAP_TAGID,tagId);
			cv.put(FourTagsProvider.COLUMN_TAGMAP_URLACTION, tagUrl);
			
			if(id==-1){
				if(getContentResolver().insert(Uri.withAppendedPath(FourTagsProvider.CONTENT_URI,FourTagsProvider.TABLE_TAGMAP), cv)!=null){
					Toast t = Toast.makeText(this, getString(R.string.savesuccess), Toast.LENGTH_SHORT);
					t.show();
					finish();
				}
				else{
					Toast t = Toast.makeText(this, getString(R.string.savefailed), Toast.LENGTH_SHORT);
					t.show();
				}
			}
			else{
				if(getContentResolver().update(Uri.withAppendedPath(FourTagsProvider.CONTENT_URI,FourTagsProvider.TABLE_TAGMAP), cv, "_id="+id, null)!=0){
					Toast t = Toast.makeText(this, getString(R.string.savesuccess), Toast.LENGTH_SHORT);
					t.show();
					finish();
				}
				else{
					Toast t = Toast.makeText(this, getString(R.string.savefailed), Toast.LENGTH_SHORT);
					t.show();
				}
			}
		}
		else if(v==buttonDelete){
			if(getContentResolver().delete(Uri.withAppendedPath(FourTagsProvider.CONTENT_URI, FourTagsProvider.TABLE_TAGMAP), "_id="+id, null)!=0){
				Toast t = Toast.makeText(this, getString(R.string.deletesuccess), Toast.LENGTH_SHORT);
				t.show();
				finish();
			}
			else{
				Toast t = Toast.makeText(this, getString(R.string.deletefailed), Toast.LENGTH_SHORT);
				t.show();
			}
		}
		else if(v==buttonBookmark){
			Intent i = new Intent(this,PickBookmarkActivity.class);
			startActivityForResult(i, REQUEST_BOOKMARK);
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode!=RESULT_OK)return;
		switch(requestCode){
		case REQUEST_BOOKMARK:
			String url = data.getStringExtra(EXTRA_URL);
			editTextTagUrl.setText(url);
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}

		
	}
}