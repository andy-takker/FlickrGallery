package incorp.death.hikki.photogallerytest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemFragment extends Fragment {
    private static final String TAG = "ItemFragment";
    public static final String ARG_ITEM_ID = "item_id";
    private static final String DEFAULT_URL = "https://cdnb.artstation.com/p/assets/images/images/005/740/075/large/adrian-art-ryuk-1.jpg";

    private Toolbar mToolbar;
    private ImageView mImageView;
    private ImageButton mFlickr;
    private ImageButton mDownload;

    private int mItemId;
    private static List<GalleryItem> sGalleryItems;
    private boolean isShownToolbar = true;
    private String mUrl;

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
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_item, container,false);

        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mToolbar.setSubtitle(mItemId+1 + " of " + sGalleryItems.size());

        int endInd = sGalleryItems.get(mItemId).title.length() < 25 ? sGalleryItems.get(mItemId).title.length() : 24 ;
        String tmpTitle = sGalleryItems.get(mItemId).title.substring(0,endInd);
        tmpTitle = tmpTitle.length() == 0 || tmpTitle.equals(new String(" ")) ? "Untitled image" : tmpTitle;
        mToolbar.setTitle(tmpTitle);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_up_button);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mFlickr = (ImageButton) v.findViewById(R.id.flickrButton);
        mFlickr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = PhotoPageActivity.newIntent(getActivity(), sGalleryItems.get(mItemId).getPhotoPageUri());
                startActivity(i);
            }
        });
        mDownload = (ImageButton) v.findViewById(R.id.downloadButton);
        mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mImageView = (ImageView) v.findViewById(R.id.imageDetail);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShownToolbar) {
                    mToolbar.animate().translationY(-mToolbar.getBottom()).setInterpolator(new AccelerateInterpolator(2)).start();
                    mFlickr.animate().translationY(mFlickr.getBottom()).setInterpolator(new AccelerateInterpolator(2)).start();
                    mDownload.animate().translationY(mFlickr.getBottom()).setInterpolator(new AccelerateInterpolator(2)).start();
                    isShownToolbar = false;
                } else{
                    mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                    mFlickr.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                    mDownload.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                    isShownToolbar = true;
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
