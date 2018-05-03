package incorp.death.hikki.flickrgallery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import incorp.death.hikki.flickrgallery.database.GalleryBaseHelper;
import incorp.death.hikki.flickrgallery.database.GalleryCursorWrapper;
import incorp.death.hikki.flickrgallery.database.GalleryDbSchema.Gallery;

public class Galleries {
    private static Galleries sGalleries;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static Galleries get(Context context){
        if (sGalleries == null){
            sGalleries = new Galleries(context);
        }
        return sGalleries;
    }
    private Galleries(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new GalleryBaseHelper(mContext).getWritableDatabase();
    }

    public List<GalleryItem> getGalleryItems() {
        List<GalleryItem> items = new ArrayList<>();

        try (GalleryCursorWrapper cursor = queryGalleryItems(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                items.add(cursor.getItem());
                cursor.moveToNext();
            }
        }

        return items;
    }

    private void setGalleryItems(List<GalleryItem> c){
        for (GalleryItem a: c) {
            ContentValues values = getContentValues(a);
            mDatabase.insert(Gallery.NAME, null, values);
        }
    }

    public int getCount(){
        return getGalleryItems().size();
    }

    private static ContentValues getContentValues(GalleryItem item){
        ContentValues values = new ContentValues();
        values.put(Gallery.Cols.URL_L,item.url_l);
        values.put(Gallery.Cols.TITLE,item.title);
        values.put(Gallery.Cols.POS,item.pos);
        values.put(Gallery.Cols.OWNER,item.owner);
        values.put(Gallery.Cols.ID,item.id);
        return values;
    }

    private GalleryCursorWrapper queryGalleryItems(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                Gallery.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new GalleryCursorWrapper(cursor);
    }

    public void resetGalellery(List<GalleryItem> items){
        mDatabase.execSQL("DELETE FROM " + Gallery.NAME);
        mDatabase = new GalleryBaseHelper(mContext).getWritableDatabase();
        setGalleryItems(items);
        Log.i("PhotoGalleryFragment", "Size of Database: " + getCount());
    }
    public GalleryItem getItem(int position){
        Log.i("ItemFragment", position+"");
        GalleryCursorWrapper cursor = queryGalleryItems(
                Gallery.Cols.POS + " = ?",
                new String[]{position+""});
        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getItem();
        } finally {
            cursor.close();
        }
    }
}
