package incorp.death.hikki.flickrgallery;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PhotoPageActivity extends SingleFragmentActivity {
    public static Intent newIntent(Context context, Uri photoPageUri){
        Intent i = new Intent(context, PhotoPageActivity.class);
        i.setData(photoPageUri);
        return  i;
    }
    @Override
    protected Fragment createFragment(){
        return PhotoPageFragment.newInstance(getIntent().getData());
    }
}
