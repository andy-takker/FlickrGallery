package incorp.death.hikki.flickrgallery.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import incorp.death.hikki.flickrgallery.GalleryItem;
import incorp.death.hikki.flickrgallery.database.GalleryDbSchema.Gallery;

public class GalleryCursorWrapper extends CursorWrapper {
    public GalleryCursorWrapper (Cursor cursor){
        super(cursor);
    }
    public GalleryItem getItem(){
        String id = getString(getColumnIndex(Gallery.Cols.ID));
        String title = getString(getColumnIndex(Gallery.Cols.TITLE));
        String url = getString(getColumnIndex(Gallery.Cols.URL_L));
        String pos = getString(getColumnIndex(Gallery.Cols.POS));
        String owner = getString(getColumnIndex(Gallery.Cols.OWNER));

        GalleryItem item = new GalleryItem();
        item.url_l = url;
        item.id = id;
        item.owner = owner;
        item.pos = Integer.parseInt(pos);
        item.title = title;

        return item;
    }
}
