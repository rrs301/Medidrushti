package com.innocruts.medidrusti;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MobileVerification extends Activity {

    EditText UserName,MobileNumber;
    Context context;
    private List<FeedItem> feedsList;
    static int rand;
    ArrayList<String>city;
    ArrayList<String>state;
    SharedPreferences pref;
    static String selectedItem="Delhi";
    List<String> StateList,CityList;
    static final int READ_BLOCK_SIZE = 1000;
    SharedPreferences.Editor editor;
    Spinner spinner,spinner2;
    String jsontext;
    String Location="Nashik";
    String MobileNo,Msg,User_Name;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);


        city=new ArrayList<String>();
        state=new ArrayList<String>();
        StateList  = new ArrayList<String>();
        CityList=new ArrayList<String>();
        OfflineData();
        pref=getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        editor=pref.edit();

//        spinner=(Spinner)findViewById(R.id.spinner1);
//        spinner2=(Spinner)findViewById(R.id.spinner2);
////
//        spinner2.setSelection(0, true);
//        View v = spinner2.getSelectedView();
        //((TextView)v).setTextColor(Color.BLACK);

        try {
            if (pref.getString("Login", null).compareTo("True") == 0) {
                Intent intent = new Intent(this, MyActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e)
        {

        }

        String[] plants = new String[]{
                "Black birch",
                "European weeping birch"
        };

       // final List<String> plantsList = new ArrayList(Arrays.asList(plants));

        // Initializing an ArrayAdapter
//        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
//                this,R.layout.spinner_item,StateList);
//
//        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
//        spinner.setAdapter(spinnerArrayAdapter);
//

        UserName=(EditText)findViewById(R.id.UserNameEditText);
            MobileNumber=(EditText)findViewById(R.id.MobileNumberEditText);

            Random r = new Random();
            rand = r.nextInt(9999 - 1000) + 1000;

            findViewById(R.id.verify).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(getApplicationContext(), "Welcome To Sell Market", Toast.LENGTH_SHORT).show();
                    MobileVerify();
                }
            });



//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//            {
//                selectedItem = parent.getItemAtPosition(position).toString();
//              //  Toast.makeText(getApplicationContext(),selectedItem,Toast.LENGTH_SHORT).show();
//                SetSpinner2();
//
//
//
//            } // to close the onItemSelected
//            public void onNothingSelected(AdapterView<?> parent)
//            {
//
//            }
//
//        });


    }

//    public void SetSpinner2()
//    {
//        parseResult(jsontext);
//
//        for(int i=0;i<CityList.size();i++)
//        {
//            Log.i("City Is:",CityList.get(i));
//        }
//        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
//                getApplicationContext(),R.layout.spinner_item,CityList);
//
//        spinnerArrayAdapter2.setDropDownViewResource(R.layout.spinner_item);
//        spinner2.setAdapter(spinnerArrayAdapter2);
//
//
//    }

    public void OfflineData()
    {


//        try {
//            FileInputStream fileIn=openFileInput("test.txt");
//            InputStreamReader InputRead= new InputStreamReader(fileIn);
//
//            char[] inputBuffer= new char[READ_BLOCK_SIZE];
//            String s="";
//            int charRead;
//
//            while ((charRead=InputRead.read(inputBuffer))>0) {
//                // char to string conversion
//                String readstring=String.copyValueOf(inputBuffer,0,charRead);
//                s +=readstring;
//            }
//            InputRead.close();
//            Toast.makeText(getBaseContext(), s,Toast.LENGTH_SHORT).show();
//           // parseResult(s);

//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {


            InputStream is = this.getResources().openRawResource(R.raw.city);
            byte[] buffer = new byte[is.available()];
            while (is.read(buffer) != -1);
             jsontext = new String(buffer);

          //  Toast.makeText(getBaseContext(), jsontext,Toast.LENGTH_SHORT).show();
            parseResult(jsontext);

        } catch (Exception e) {

            Log.i("",e.toString());
        }

       // String[] unique = new HashSet<String>(Arrays.<String>asList(String.valueOf(city))).toArray(new String[0]);


        StateList = new ArrayList<String>(new HashSet<String>(state));


       // PostEx();

    }

    private void parseResult(String result) {
        Log.i("InParse",selectedItem);
        city.clear();
        CityList.clear();
        try {
            Log.i("InParseIn","Yes");

            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("result");

            feedsList = new ArrayList();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                FeedItem item = new FeedItem();
               // city.add(post.optString("id"));

                state.add(post.optString("state"));
                if(post.optString("state").compareTo(selectedItem)==0)
                {
                    city.add(post.optString("city"));
                    CityList.add(post.optString("city"));

                 //   Log.i("City;",post.optString("city"));
                }
                //Log.i("City;2",post.optString("city"));
               // feedsList.add(item);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void MobileVerify()
    {

        MobileNo=MobileNumber.getText().toString();
        User_Name=UserName.getText().toString();
//        Location=spinner2.getSelectedItem().toString();


        Msg="This Is Your OTP:"+rand +" for Medidrushti Login";
        if(MobileNo.length()==10 && UserName.getText().toString()!=null) {
            new AsyncHttpTask().execute();
        }
        else
        {
            Toast.makeText(this,"Please fill all record correctly",Toast.LENGTH_SHORT).show();
        }
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(MobileVerification.this);
            pd.setMessage("Verification OTP Sending...");
            pd.show();
            setProgressBarIndeterminateVisibility(true);
            Log.i("In Pre:", "Yes");
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            Log.i("In DOIN:","Yes");
            try {


                URL url = new URL("http://www.townlocal.in/Backend/SMS/sendsms.php?to="+MobileNo+"&msg="+Msg.replace(" ","%20"));


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
                  //  parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
               // Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            //progressBar.setVisibility(View.GONE);
            pd.dismiss();
            if (result == 1) {
                Log.i("This Random", String.valueOf(rand));
                Intent intent=new Intent(getApplicationContext(),OTP_Check.class);
                intent.putExtra("OTPCODE",String.valueOf(rand));
                intent.putExtra("MobileNumber",String.valueOf(MobileNo));
                intent.putExtra("UserName",String.valueOf(User_Name));
                intent.putExtra("Location",String.valueOf(Location));

                startActivity(intent);
            } else {
                Toast.makeText(MobileVerification.this, "Try Again...", Toast.LENGTH_SHORT).show();
            }
        }
    }










    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.mobile_verification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
