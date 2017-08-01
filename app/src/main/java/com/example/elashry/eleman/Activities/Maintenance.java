package com.example.elashry.eleman.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.elashry.eleman.Controller;
import com.example.elashry.eleman.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.elashry.eleman.R.array.mySpinner;

public class Maintenance extends AppCompatActivity {
    Button b1;
    EditText cname,cphone,caddress,dtype, dateorder ,marka ,damage;
    Calendar myCalendar;
    Spinner spinner;
    ProgressDialog progressDialog;
    String ss;
     String [] state=new String[]{"داخل الضمان ","خارج الضمان"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        b1= (Button) findViewById(R.id.signUpBtn);
        dateorder= (EditText) findViewById(R.id.date);
        spinner= (Spinner) findViewById(R.id.mySpinner);
        cname= (EditText) findViewById(R.id.fullName);
        cphone= (EditText) findViewById(R.id.mobileNumber);
        caddress= (EditText) findViewById(R.id.location);
        dtype= (EditText) findViewById(R.id.type);
        marka= (EditText) findViewById(R.id.marka);
        damage= (EditText) findViewById(R.id.damage);



//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i =new Intent(Maintenance.this,Category.class);
//                startActivity(i);
//            }
//        });
          myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateorder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Maintenance.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                ss=state[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);


    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateorder.setText(sdf.format(myCalendar.getTime()));
    }

    public void registerOrder(View view) {
        if(   cname.getText().toString().isEmpty()&&cphone.getText().toString().isEmpty()&&
                caddress.getText().toString().isEmpty()&&dtype.getText().toString().isEmpty()&&
                dateorder.getText().toString().isEmpty()&&marka.getText().toString().isEmpty()&&damage.getText().toString().isEmpty())
        {
            Toast.makeText(Maintenance.this,"You Should Enter data",Toast.LENGTH_LONG).show();
        }else
        {



            progressDialog.setMessage("sending "+ cname.getText().toString()+" data to server");
            progressDialog.show();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    "http://semicolonsoft.com/app/api/find/maintenance", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            response, Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("client_name", cname.getText().toString());
                    params.put("client_phone", cphone.getText().toString());
                    params.put("client_location", caddress.getText().toString());
                    params.put("device_type", dtype.getText().toString());
                    params.put("warranty_state", ss);
                    params.put("device_brand", marka.getText().toString());
                    params.put("order_date", dateorder.getText().toString());
                    params.put("damage_type", damage.getText().toString());

                    return params;
                }

            };
            // Adding request to request queue
            Controller.getInstance().addToRequestQueue(strReq,"re");

        }
    }
}
