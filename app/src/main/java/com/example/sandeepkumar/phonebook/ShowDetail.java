package com.example.sandeepkumar.phonebook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

public class ShowDetail extends AppCompatActivity {
    TextView name, phoneNumber;
    String s1, s2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        ImageView image=(ImageView)findViewById(R.id.image1);
        name = (TextView) findViewById(R.id.names);
        phoneNumber = (TextView) findViewById(R.id.PhoneNumber);
        s1 = getIntent().getExtras().getString("names");
        s2 = getIntent().getExtras().getString("PhoneNumber");
        ColorGenerator generator=ColorGenerator.MATERIAL;
        int randomColor=generator.getRandomColor();
        TextDrawable.IBuilder builder=TextDrawable.builder().beginConfig()
                .withBorder(2)
                .endConfig()
                .round();
        //get first character from chat dialog title for create dialog image
        TextDrawable drawable=builder.build(s1.toString().substring(0,1).toUpperCase(),randomColor);
        image.setImageDrawable(drawable);
        name.setText(s1);
        phoneNumber.setText(s2);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+s2));//change the number

                if (ActivityCompat.checkSelfPermission(ShowDetail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(callIntent);

            }

        });



    }
}
