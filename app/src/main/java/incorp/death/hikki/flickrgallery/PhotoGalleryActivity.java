package incorp.death.hikki.flickrgallery;

import android.support.v4.app.Fragment;

import java.util.List;

public class PhotoGalleryActivity extends SingleFragmentActivity{
    private List<GalleryItem> mGalleryItems;
    @Override
    protected Fragment createFragment(){
        return PhotoGalleryFragment.newInstance();
    }
    public void setItems(List<GalleryItem> c){
        mGalleryItems = c;
    }
    public List<GalleryItem> getItems(){
        return mGalleryItems;
    }
}
