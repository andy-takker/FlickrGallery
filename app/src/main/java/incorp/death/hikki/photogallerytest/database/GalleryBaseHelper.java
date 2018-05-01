package incorp.death.hikki.photogallerytest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import incorp.death.hikki.photogallerytest.database.GalleryDbSchema.Gallery;

public class GalleryBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "galleryBase.db";

    public GalleryBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + Gallery.NAME + "(" +
                " _id integer primary key autoincrement, " +
                Gallery.Cols.URL_L + ", " +
                Gallery.Cols.TITLE + ", " +
                Gallery.Cols.POS + ", " +
                Gallery.Cols.OWNER + ", " +
                Gallery.Cols.ID + ")"
        );

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
