package incorp.death.hikki.photogallerytest;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemFragment extends Fragment {
    private static final String TAG = "ItemFragment";
    public static final String ARG_ITEM_ID = "item_id";
    private static final String DEFAULT_URL = "https://cdnb.artstation.com/p/assets/images/images/005/740/075/large/adrian-art-ryuk-1.jpg";
    private ImageView mImageView;
    private int mItemId;
    private static List<GalleryItem> sGalleryItems;
    private String mUrl;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;

    public static ItemFragment newInstance(int itemId, List<GalleryItem> items){
        sGalleryItems = items;
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_ID, itemId);
        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemId =(int) getArguments().getSerializable(ARG_ITEM_ID);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_item, container,false);

        mAppBarLayout = (AppBarLayout) v.findViewById(R.id.appBarLayout);

        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mToolbar.setSubtitle(mItemId+1 + " of " + sGalleryItems.size());


        int endInd = sGalleryItems.get(mItemId).title.length() < 25 ? sGalleryItems.get(mItemId).title.length() : 24 ;
        String tmpTitle = sGalleryItems.get(mItemId).title.substring(0,endInd);
        tmpTitle = tmpTitle.length() == 0 || tmpTitle == " " ? "Untitled image" : tmpTitle;
        mToolbar.setTitle(tmpTitle);
        mToolbar.setNavigationIcon(R.drawable.arrow_left);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mImageView = (ImageView) v.findViewById(R.id.imageDetail);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAppBarLayout.getVisibility() == View.INVISIBLE){
                    mAppBarLayout.setVisibility(View.VISIBLE);
                } else{
                    mAppBarLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        mUrl = sGalleryItems.get(mItemId).url_l;
        Picasso.get()
                .load(mUrl == null ? DEFAULT_URL : mUrl)
                .into(mImageView);
        return v;
    }
}
