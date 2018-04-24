package incorp.death.hikki.photogallerytest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class ItemFragment extends Fragment {
    private static final String TAG = "ItemFragment";
    public static final String ARG_ITEM_ID = "item_id";
    private ImageView mImageView;
    private TextView mTextView;
    private int mItemId;

    public static ItemFragment newInstance(int itemId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_ID, itemId);
        Log.i(TAG, "" + itemId);
        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemId =(int) getArguments().getSerializable(ARG_ITEM_ID);
        Log.i(TAG,"Pos was received: " + mItemId);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_item, container,false);
        mImageView = (ImageView) v.findViewById(R.id.imageDetail);
        Picasso.get()
                .load("https://cdnb.artstation.com/p/assets/images/images/005/740/075/large/adrian-art-ryuk-1.jpg")
                .into(mImageView);
        mTextView = (TextView) v.findViewById(R.id.textDetail);
        mTextView.setText("" + mItemId);
        return v;
    }

}
