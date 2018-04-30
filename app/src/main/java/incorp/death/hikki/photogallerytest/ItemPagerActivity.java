package incorp.death.hikki.photogallerytest;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class ItemPagerActivity extends AppCompatActivity {
    private static final String TAG = "ItemPagerActivity";
    public static final String EXTRA_ITEM_POS = "incorp.death.hikki.photogallerytest.pos";
    private static List<GalleryItem> mGalleryItems;
    private ViewPager mViewPager;

    public static Intent newIntent (Context packageContext, int pos, List<GalleryItem> items){
        mGalleryItems = items;
        Intent intent = new Intent(packageContext, ItemPagerActivity.class);
        intent.putExtra(EXTRA_ITEM_POS, pos);
        Log.i(TAG, "Pos was sent: " + pos);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_pager);
        final int posItem = (int) getIntent().getSerializableExtra(EXTRA_ITEM_POS);

        mViewPager = (ViewPager) findViewById(R.id.item_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return ItemFragment.newInstance(position, mGalleryItems);
            }

            @Override
            public int getCount() {
                return mGalleryItems.size();
            }
        });
        mViewPager.setCurrentItem(posItem);

    }
}
