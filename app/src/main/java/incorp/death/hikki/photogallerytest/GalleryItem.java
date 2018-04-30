package incorp.death.hikki.photogallerytest;

import android.net.Uri;

public class GalleryItem {
    public String url_l;
    public String title;
    public int pos;
    public String owner;
    public String id;

    public String getPos() {
        return Integer.toString(pos);
    }
    public Uri getPhotoPageUri(){
        return Uri.parse("https://www.flickr.com/photos/")
                .buildUpon()
                .appendPath(owner)
                .appendPath(id)
                .build();
    }
}