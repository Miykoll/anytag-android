package be.emich.nfc.fourtags.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class FourTagsProvider extends ContentProvider {

	public static final String CONTENT_AUTHORITY = "be.emich.nfc.fourtags.provider.ContentProvider";
	public static final Uri CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
	
	public static final int TYPE_TAG = 15;
	
	public static final String TABLE_TAGMAP = "tagmap";
	public static final String COLUMN_TAGMAP_TAGID = "tagid";
	public static final String COLUMN_TAGMAP_URLACTION = "urlaction";
	
	public static final String DB_NAME = "be.emich.nfc.fourtags.db";
	public static final int DB_VERSION = 1;
	
	private SQLiteDatabase db;
	
	public static final UriMatcher matcher;
	static {
		matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(CONTENT_AUTHORITY, TABLE_TAGMAP, TYPE_TAG);
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if(matcher.match(uri)==TYPE_TAG){
			
			int retVal = db.delete(TABLE_TAGMAP, selection, selectionArgs);
			if(retVal!=0)getContext().getContentResolver().notifyChange(uri,null);
			return retVal;
		}
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		if(matcher.match(uri)==TYPE_TAG){
			return "vnd.be.emich.dir/tagmap";
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(matcher.match(uri)==TYPE_TAG){
			long rowId = db.insert(TABLE_TAGMAP, "", values);
			Uri returnUri = Uri.withAppendedPath(uri, rowId+"");
			getContext().getContentResolver().notifyChange(returnUri, null);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uri.withAppendedPath(uri, rowId+"");
		}
		return null;
	}

	@Override
	public boolean onCreate() {
		FourTagHelper helper = new FourTagHelper(this.getContext());
		db = helper.getWritableDatabase();
		return db!=null;
	}
	
	 protected class FourTagHelper extends SQLiteOpenHelper{

		public FourTagHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table tagmap(_id integer primary key,tagid text null,urlaction text null)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table tagmap");
			onCreate(db);
		}
		
	};

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		if(matcher.match(uri)==TYPE_TAG){
			Cursor c = db.query(TABLE_TAGMAP,projection,selection,selectionArgs,null,null,sortOrder);
			if(c!=null)c.setNotificationUri(getContext().getContentResolver(), uri);
			return c;
		}
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		if(matcher.match(uri)==TYPE_TAG){
			int numberOfRowsAffected = db.update(TABLE_TAGMAP, values,selection, selectionArgs);
			if(numberOfRowsAffected>0)getContext().getContentResolver().notifyChange(uri, null);
			return numberOfRowsAffected;
		}
		return 0;
	}

}
