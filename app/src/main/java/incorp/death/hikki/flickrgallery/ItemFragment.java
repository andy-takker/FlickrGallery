package incorp.death.hikki.flickrgallery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ItemFragment extends Fragment {
    private static final String TAG = "ItemFragment";
    public static final String ARG_ITEM_ID = "item_id";
    private static final String DEFAULT_URL = "https://cdnb.artstation.com/p/assets/images/images/005/740/075/large/adrian-art-ryuk-1.jpg";

    private Toolbar mToolbar;
    private ImageButton mFlickr;
    private ImageButton mDownload;
    private ProgressDialog mProgressDialog;
    private int i = 0;
    private int mItemId;
    private GalleryItem mGalleryItem;
    private boolean isShownToolbar = true;
    private int mSize = 0;

    public static ItemFragment newInstance(int itemId){
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
        mGalleryItem = Galleries.get(getContext()).getGalleryItems().get(mItemId);
        mSize = Galleries.get(getContext()).getCount();
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Download image: ");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_item, container,false);

        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mToolbar.setSubtitle(mItemId+1 + " of " + mSize);

        int endInd = mGalleryItem.title.length() < 25 ? mGalleryItem.title.length() : 24 ;
        String tmpTitle = mGalleryItem.title.substring(0, endInd);
        tmpTitle = tmpTitle.length() == 0 || tmpTitle.equals(" ") ? "Untitled image" : tmpTitle;
        mToolbar.setTitle(tmpTitle);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        mToolbar.setTitleTextColor(Color.WHITE);

        mToolbar.setNavigationIcon(R.drawable.up_button);
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
                Intent i = PhotoPageActivity.newIntent(getActivity(), mGalleryItem.getPhotoPageUri());
                startActivity(i);
            }
        });
        mDownload = (ImageButton) v.findViewById(R.id.downloadButton);
        mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"FlickrGallery");
                directory.mkdirs();
                new DownloadTask(getContext()).execute(mGalleryItem.url_l);

            }
        });
        TouchImageView imageView = (TouchImageView) v.findViewById(R.id.imageDetail);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(i == 1){
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
                        } else if(i == 2){
                            //На всякий случай может приготится
                        }
                        i = 0;
                    }
                }, 500);
            }

        });
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });

        String url = mGalleryItem.url_l;
        Picasso.get()
                .load(url == null ? DEFAULT_URL : url)
                .into(imageView);
        return v;
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {
        // Скачивание файла в фоновом потоке
        private Context context;
        private PowerManager.WakeLock mWakeLock;
        private InputStream input;
        private OutputStream output;
        private HttpURLConnection connection;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            input = null;
            output = null;
            connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                input = connection.getInputStream();
                //Расположение нового файла
                output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/FlickrGallery/"+ sUrl[0].substring(sUrl[0].lastIndexOf("/")));

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Отображение прогресса
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // перехватываем работу процессора при нажатии на кнопку питания
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null){
                Toast.makeText(context,"Download error: " + result, Toast.LENGTH_LONG).show();
                Log.i("ProgressBar", result);
            }
            else {
                Toast.makeText(context, "File has been downloaded successful to the FlickrGallery folder in the internal storage", Toast.LENGTH_LONG).show();
            }
        }
    }
}
