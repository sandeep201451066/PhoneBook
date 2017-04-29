package com.example.sandeepkumar.phonebook;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.HashMap;
public class MainActivity extends AppCompatActivity {

    ArrayList<HashMap<String, Object>> searchResults;
    ArrayList<HashMap<String, Object>> originalValues;
    LayoutInflater inflater;
    Cursor cursor ;
    String name, number ;
    HashMap<String , Object> temp;
    public  static final int RequestPermissionCode  = 1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText searchBox=(EditText) findViewById(R.id.searchBox);
        ListView playerListView=(ListView) findViewById(android.R.id.list);
        inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        originalValues=new ArrayList<HashMap<String,Object>>();

        GetContactsDeatailInArrayList();

        searchResults=new ArrayList<HashMap<String,Object>>(originalValues);
        final CustomAdapter adapter=new CustomAdapter(this, R.layout.searched_result,searchResults);
        playerListView.setAdapter(adapter);

        searchBox.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //get the text in the EditText
                String searchString=searchBox.getText().toString();
                int textLength=searchString.length();
                searchResults.clear();
                // check input is there or not in list
                for(int i=0;i<originalValues.size();i++)
                {
                    String playerName=originalValues.get(i).get("PhoneNumber").toString();
                    if(textLength<=playerName.length()){
                        //compare the String in EditText with Names in the ArrayList
                        if(searchString.equalsIgnoreCase(playerName.substring(0,textLength)))
                            searchResults.add(originalValues.get(i));
                    }
                }

                adapter.notifyDataSetChanged();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void GetContactsDeatailInArrayList(){

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
        int i=0;
        while (cursor.moveToNext()) {
            // get all detail from your phone like name and phone number and store in hash map
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            temp=new HashMap<String, Object>();
            temp.put("names", name);
            temp.put("PhoneNumber", number);
            ColorGenerator generator=ColorGenerator.MATERIAL;
            int randomColor=generator.getRandomColor();
            TextDrawable.IBuilder builder=TextDrawable.builder().beginConfig()
                    .withBorder(2)
                    .endConfig()
                    .round();
            //get first character from chat dialog title for create dialog image
            TextDrawable drawable=builder.build(name.toString().substring(0,1).toUpperCase(),randomColor);
            temp.put("letter",drawable);
            originalValues.add(temp);
        }
        cursor.close();
    }


    private class CustomAdapter extends ArrayAdapter<HashMap<String, Object>>
    {

        public CustomAdapter(Context context, int textViewResourceId,
                             ArrayList<HashMap<String, Object>> Strings) {

            //let android do the initializing :)
            super(context, textViewResourceId, Strings);
        }
    private class ViewHolder
        {
            LinearLayout linear;
            ImageView image1;
            TextView names,PhoneNumber;
            TextView mobileCall;
        }

        ViewHolder viewHolder;


        public View getView(final int position, View convertView, ViewGroup parent) {

            if(convertView==null)
            {
                // get all ids that you want to display
                convertView=inflater.inflate(R.layout.searched_result, null);
                viewHolder=new ViewHolder();
                viewHolder.image1=(ImageView)convertView.findViewById(R.id.image1);
              viewHolder.mobileCall=(TextView) convertView.findViewById(R.id.mobileCall);
                viewHolder.linear=(LinearLayout) convertView.findViewById(R.id.linear);
                viewHolder.names=(TextView) convertView.findViewById(R.id.names);
                viewHolder.PhoneNumber=(TextView) convertView.findViewById(R.id.PhoneNumber);
                convertView.setTag(viewHolder);
            }
            else
            // set attribute according to views
                viewHolder=(ViewHolder) convertView.getTag();
            viewHolder.image1.setImageDrawable((Drawable) searchResults.get(position).get("letter"));
            viewHolder.names.setText(searchResults.get(position).get("names").toString());
            viewHolder.PhoneNumber.setText(searchResults.get(position).get("PhoneNumber").toString());

            // click on mobile and call it
            viewHolder.mobileCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s= searchResults.get(position).get("PhoneNumber").toString();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+s));
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        //change the number
                        return;
                    }
                    startActivity(callIntent);
                }
            });

            // get detail on other page
            viewHolder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this,ShowDetail.class);
                    intent.putExtra("names",searchResults.get(position).get("names").toString());
                    intent.putExtra("PhoneNumber",searchResults.get(position).get("PhoneNumber").toString());
                    startActivity(intent);
                }
            });
            //return the view to be displayed
            return convertView;
        }

    }

}