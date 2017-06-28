package com.innocruts.medidrusti;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LocationSearch extends AppCompatActivity {

    Spinner spinner,spinner2;
    String selectedItem,SelectedLocation;
    String []arraySpinner;
    String []Nashik;
    String []Pune;
    String SubCat;
    List <String> CityList;
    ArrayAdapter<String> NashikArrayAdapter;
    ArrayAdapter<String> PuneArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);



        Intent i=getIntent();
        SubCat=i.getStringExtra("Sid");

        spinner=(Spinner)findViewById(R.id.spinner1);
        spinner2=(Spinner)findViewById(R.id.spinner2);
//
        spinner2.setSelection(0, true);
        View v = spinner2.getSelectedView();
        ((TextView)v).setTextColor(Color.BLACK);

        arraySpinner = new String[] {
                "Nashik", "Pune"
        };
        Nashik = new String[] {
                "Yeola", "Nashik", "Sinnar", "Nandgaon"
        };
        Pune = new String[] {
                "Hinjewadi", "Karvenagar"
        };

      //  CityList.add(1,"Mumbai");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, arraySpinner);
        spinner.setAdapter(adapter);

         NashikArrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(),R.layout.spinner_item,Nashik);

        NashikArrayAdapter.setDropDownViewResource(R.layout.spinner_item);

        PuneArrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(),R.layout.spinner_item,Pune);

        PuneArrayAdapter.setDropDownViewResource(R.layout.spinner_item);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),selectedItem,Toast.LENGTH_SHORT).show();
                if(selectedItem.compareTo("Nashik")==0)
                {
                    spinner2.setAdapter(NashikArrayAdapter);
                }
                else  if(selectedItem.compareTo("Pune")==0)
                {
                    spinner2.setAdapter(PuneArrayAdapter);
                }
               // SetSpinner2();



            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }

        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                SelectedLocation = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),selectedItem,Toast.LENGTH_SHORT).show();




            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }

        });

        findViewById(R.id.FindNow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "Welcome To Sell Market", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),GetPostData.class);
                i.putExtra("Location",SelectedLocation);
                i.putExtra("Sid",SubCat);
                startActivity(i);
            }
        });
    }


   public void Find(View v)
   {
       Intent i=new Intent(this,GetPostData.class);
       i.putExtra("Location",SelectedLocation);
       i.putExtra("Sid",SubCat);
       startActivity(i);
   }
}
