package com.innocruts.medidrusti;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import com.bumptech.glide.Glide;
import com.pushbots.push.Pushbots;
import com.wunderlist.slidinglayer.SlidingLayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




public class MyActivity extends Activity {

    private static final String TAG = "RecyclerViewExample";
    public static int condition = 1;
    private List<FeedItem> feedsList;
    final String url = "http://mauryainfrastructure.com/statusme/get_medical_data.php";
    String urlme="http://medidrushti.16mb.com/Backend/getSliderAds.php";
    private RecyclerView mRecyclerView;
    private MyActivityAdapter adapter;
    private ProgressBar progressBar;
    int backButtonCount=0;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SlidingLayer slidingLayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

       // Pushbots.sharedInstance().init(this);
        ////////////////////////////////////////////////////////////////
         slidingLayer = (SlidingLayer) findViewById(R.id.slidingLayer1);

        slidingLayer.setShadowDrawable(R.drawable.siderbar_shadow);
      //  slidingLayer.setShadowSizeRes(R.dimen.shadow_size);
        slidingLayer.setOffsetDistanceRes(R.dimen.offset_distance);
        slidingLayer.setPreviewOffsetDistanceRes(R.dimen.preview_offset_distance);
        slidingLayer.setStickTo(SlidingLayer.STICK_TO_LEFT);
        slidingLayer.setChangeStateOnTap(true);

       // slidingLayer.addView(new Button(this));
        ////////////////////////////////////////////////////////////////////////
        getActionBar();

//        getActionBar().setIcon(R.drawable.townlocallogo);
//        getActionBar().setDisplayUseLogoEnabled(true);


        pref=getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        TextView username=(TextView)findViewById(R.id.username);
        TextView usermobile=(TextView)findViewById(R.id.usermobile);

        username.setText(pref.getString("UserName",null));
        usermobile.setText(pref.getString("MobileNumber",null));

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_slider);

//        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL); // (int spanCount, int orientation)
//        mRecyclerView.setLayoutManager(mLayoutManager);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(horizontalLayoutManagaer);

        // mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //  mRecyclerView.setLayoutManager(layoutManager);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Downloading data from below url
      // final RippleView rippleView = (RippleView) findViewById(R.id.more);
//        rippleView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(getApplicationContext(),"Click here",Toast.LENGTH_SHORT).show();
//            }
//        });

//        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
//
//
//
//                @Override
//
//                public void onComplete (RippleView rippleView){
//                Log.d("Sample", "Ripple completed");
//                try {
//
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//
//
//
//        });
        new AsyncHttpTask().execute();
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            setProgressBarIndeterminateVisibility(true);
            Log.i("In Pre:", "Yes");
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            Log.i("In DOIN:","Yes");
            try {


                URL url = new URL(urlme);


                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            progressBar.setVisibility(View.GONE);

            if (result == 1) {

                adapter = new MyActivityAdapter(MyActivity.this, feedsList);
                mRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(MyActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //
    private void parseResult(String result) {
        Log.i("InParse","Yes");
        try {
            Log.i("InParseIn","Yes");

            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("result");

            feedsList = new ArrayList();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                FeedItem item = new FeedItem();
                item.setSliderImage(post.optString("image"));
                feedsList.add(item);
                Log.i("SLiderImage;",(post.optString("image")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FeedItem item = new FeedItem();
        //  item.setSliderImageTest(R.drawable.academy);

    }

    public void Popup()
    {
        Log.i("In Popup","Yes");
        ImageView popup=(ImageView)findViewById(R.id.popupimage);
        Glide
                .with(this)
                .load(feedsList.get(0))
                .centerCrop()
                .placeholder(R.drawable.imageplaceholder)
                .crossFade()
                .into(popup);
        PopupWindow menuPopup;
        View menuView=getLayoutInflater().inflate(R.layout.activity_popup_image, null);
        menuPopup=new PopupWindow(menuView, 500, 500, false);
        menuPopup.showAtLocation(menuView, Gravity.CENTER | Gravity.CENTER, 0, 0);
    }

    public void Agriculture(View v)
    {

        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","1");

        startActivity(intent);
    }
    public void Hospital(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","2");
        startActivity(intent);
    }
    public void Architecture(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","3");
        startActivity(intent);
    }
    public void Automotive(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","4");
        startActivity(intent);
    }
    public void Electronics(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","5");
        startActivity(intent);
    }
    public void Enegry(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","6");
        startActivity(intent);
    }
    public void Computer(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","7");
        startActivity(intent);
    }
    public void Academy(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","8");
        startActivity(intent);
    }
    public void Herbal(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","10");
        startActivity(intent);
//        new SnackBar.Builder(this)
//                .withMessage("This library is awesome!") // OR
//                .withTextColorId(Color.BLUE)
//                .show();


//        new BottomSheet.Builder(this).title("title").sheet(R.menu.menu).listener(new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case R.id.help:
//                        Toast.makeText(getApplicationContext(),"Help Me",Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }).show();

    }
    public void Doctor(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","1");
        startActivity(intent);
    }
    public void Specilist(View v)
    {
        Intent intent=new Intent(this,GetPostData.class);
        intent.putExtra("Sid","2");
        startActivity(intent);
    }
    public void Event(View v)
    {
        Intent intent=new Intent(this,GetPostData.class);
        intent.putExtra("Sid","3");
        startActivity(intent);
    }
    public void Tips(View v)
    {
        Intent intent=new Intent(this,GetPostData.class);
        intent.putExtra("Sid","4");
        startActivity(intent);
    }
    public void Industrial(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","15");
        startActivity(intent);
    }
    public void Mobile(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","16");
        startActivity(intent);
    }
    public void Gift(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","17");
        startActivity(intent);
    }
    public void Wedding(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","18");
        startActivity(intent);
    }
    public void Traval(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","19");
        startActivity(intent);
    }
    public void Furniture(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","20");
        startActivity(intent);
    }
    public void Financial(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","21");
        startActivity(intent);
    }
    public void Gold(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","22");
        startActivity(intent);
    }
    public void Printing(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","24");
        startActivity(intent);
    }


    public void home(View v)
    {
        slidingLayer.openLayer(true);
    }
    public void Bus(View v)
    {
        String urlsend="https://msrtc.maharashtra.gov.in/index.php/bus_timetable/";
        Intent intent=new Intent(this,WebViewActivity.class);
        intent.putExtra("UrlToLoad",urlsend);
        startActivity(intent);
    }
    public void Rail(View v)
    {
        String urlsend="https://www.railyatri.in";
        Intent intent=new Intent(this,WebViewActivity.class);
        intent.putExtra("UrlToLoad",urlsend);
        startActivity(intent);
    }
    public void Job(View v)
    {
        Intent intent=new Intent(this,GetSubCategory.class);
        intent.putExtra("SubCatTitle","23");
        startActivity(intent);
    }
    public void socialmedia(View v)
    {
      //  String urlsend="http://uccricket.ucweb.com/";
        Intent intent=new Intent(this,SocialMedia.class);
       // intent.putExtra("UrlToLoad",urlsend);
        startActivity(intent);
    }
    public void SellRent(View v)
    {
        Intent intent=new Intent(this,Buy_sell_activity.class);
        // intent.putExtra("SubCatTitle","9");
        startActivity(intent);
    }
    public void event(View v)
    {
        Intent intent=new Intent(this,Offer_Event.class);
        // intent.putExtra("SubCatTitle","9");
        startActivity(intent);
    }


    public void News(View view)
    {
//        Intent intent=new Intent(this,News.class);
//        startActivity(intent);
        String urlsend="http://www.indianexpress.com";
        Intent intent=new Intent(this,WebViewActivity.class);
        intent.putExtra("UrlToLoad",urlsend);
        startActivity(intent);
    }

    public void AmbulanceCall(View view)
    {

//    Intent callIntent = new Intent(Intent.ACTION_CALL);
//    callIntent.setData(Uri.parse("tel:"+PhoneNo));
//    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    startActivity(callIntent);
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:102"));
            startActivity(intent);
        }
        catch (android.content.ActivityNotFoundException e){

            Toast.makeText(getApplicationContext(),"Network Is Week",Toast.LENGTH_LONG).show();
        }
    }
    public void FireCall(View view)
    {

//    Intent callIntent = new Intent(Intent.ACTION_CALL);
//    callIntent.setData(Uri.parse("tel:"+PhoneNo));
//    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    startActivity(callIntent);
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:101"));
            startActivity(intent);
        }
        catch (android.content.ActivityNotFoundException e){

            Toast.makeText(getApplicationContext(),"Network Is Week",Toast.LENGTH_LONG).show();
        }
    }
    public void PoliceCall(View view)
    {

//    Intent callIntent = new Intent(Intent.ACTION_CALL);
//    callIntent.setData(Uri.parse("tel:"+PhoneNo));
//    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    startActivity(callIntent);
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:100"));
            startActivity(intent);
        }
        catch (android.content.ActivityNotFoundException e){

            Toast.makeText(getApplicationContext(),"Network Is Week",Toast.LENGTH_LONG).show();
        }
    }

    public void Music(View view)
    {
        String urlsend="http://music.uodoo.com/";
        Intent intent=new Intent(this,WebViewActivity.class);
        intent.putExtra("UrlToLoad",urlsend);
        startActivity(intent);
    }

    public void bookmark(View view)
    {
        Intent intent=new Intent(this,OfflineData.class);
        startActivity(intent);
    }



    public void ContactUs(View view)
    {
        Intent intent=new Intent(this,ContactUs.class);
        startActivity(intent);
    }
    public void AboutUs(View view)
    {
        Intent intent=new Intent(this,AboutUs.class);
        startActivity(intent);
    }
    public void ShareApp(View view)
    {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Find best service near you!";
        String sharePhone="Download Town Local App from PlayStore";
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"TownLocal App");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody+" "+sharePhone+" "+"https://goo.gl/2da43o");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void onBackPressed()
    {

        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
