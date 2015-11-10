package com.example.commadersherpard.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Commader on 2015/10/24.
 */
public class  GoogleMapObj extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public GoogleApiClient mGoogleApiClient;
    int PLACE_PICKER_REQUEST=1;
    String TAG="P";
    TextView text;
    private AutoCompleteAdapter mAdapter;
    private AutoCompleteTextView mPredictTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        text=(TextView) findViewById(R.id.textView);
        mPredictTextView = (AutoCompleteTextView) findViewById( R.id.predicttextview );
        mAdapter = new AutoCompleteAdapter( this );
        mPredictTextView.setAdapter(mAdapter);

        List<String> list1 = new ArrayList<String>();
        for(int i=0;i<Aevee.ShopList.length;i++)
        {
            list1.add(Aevee.ShopList[i]);
        }
        ListView lv= (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list1 );

        lv.setAdapter(arrayAdapter);

        mPredictTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AutoCompletePlace place = (AutoCompletePlace) parent.getItemAtPosition(position);
                findPlaceById(place.getId());

            }
        });

        inti(this);
        ListView listView= (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(getApplicationContext(), position + "mmm", Toast.LENGTH_SHORT);
                Log.d("PPPP", position + "=====================");
                List<String> list2 = new ArrayList<String>();
                if(position==1)
                {
                    for(int i=0;i<Aevee.ProductList2.length;i++)
                    {
                        list2.add(Aevee.ProductList2[i]);
                    }
                }
                else
                {
                    for(int i=0;i<Aevee.ProductList.length;i++)
                    {
                        list2.add(Aevee.ProductList[i]);
                    }
                }

                ListView lv2= (ListView) findViewById(R.id.listView2);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getBaseContext(),
                        android.R.layout.simple_list_item_1,
                        list2 );
                Log.d("PPP",list2.toString());
                lv2.setAdapter(arrayAdapter);
                ViewFlipper v=(ViewFlipper)findViewById(R.id.VF);
                v.showNext();
                v.showNext();
            }
        });


        ListView l2=(ListView)findViewById(R.id.listView2);

        l2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getApplicationContext(), position + "mmm", Toast.LENGTH_SHORT);
                if(!Aevee.swi) {
                    add(Aevee.ProductList[position]);
                    Aevee.curPrice+=Aevee.ProductList1Price[position];
                }
                else {
                    add(Aevee.ProductList2[position]);
                    Aevee.curPrice+=Aevee.ProductList2Price[position];
                }
                TextView t=(TextView)findViewById(R.id.listCount);
                t.setText(""+Aevee.currentlist.size());
            }
        });
        ListView l3=(ListView)findViewById(R.id.listView3);
        l3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getApplicationContext(), position + "mmm", Toast.LENGTH_SHORT);
                 String temp=Aevee.currentlist.get(position);
                 Aevee.currentlist.remove(position);
                 Aevee.curPrice-=Aevee.priceCheck(temp);
                ListView lv2= (ListView) findViewById(R.id.listView3);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getBaseContext(),
                        android.R.layout.simple_list_item_1,
                        Aevee.currentlist );
                TextView t2=(TextView)findViewById(R.id.price);
                lv2.setAdapter(arrayAdapter);
                t2.setText(Aevee.curPrice+"");


            }
        });

        //text=(TextView) findViewById(R.id.button);
       // text2=(TextView) findViewById(R.id.button2);
    }

    public void confirmlist(View v)
    {
        ViewFlipper n=(ViewFlipper)findViewById(R.id.VF);
        n.showPrevious();
        n.showPrevious();
        n.showPrevious();
    }
    public void next(View v)
    {
        ViewFlipper n=(ViewFlipper)findViewById(R.id.VF);
        ListView lv2= (ListView) findViewById(R.id.listView3);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getBaseContext(),
                android.R.layout.simple_list_item_1,
                Aevee.currentlist );
        TextView t2=(TextView)findViewById(R.id.price);
        lv2.setAdapter(arrayAdapter);
        t2.setText(Aevee.curPrice+"");
        n.showNext();
    }
    public void inti(Context _context)
    {
        mGoogleApiClient = new GoogleApiClient
                .Builder(_context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) _context)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mGoogleApiClient.connect();
        if( mAdapter != null )
            mAdapter.setGoogleApiClient( mGoogleApiClient );


    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.disconnect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {


    }

    public void Reg(View v)
    {
        ViewFlipper Vf=(ViewFlipper)findViewById(R.id.VF);
        Vf.showNext();
    }
    public void Register(View v)
    {
        if(Aevee.Current==null)
            Aevee.Current=new user();

        EditText edtmp=(EditText)findViewById(R.id.name);
        Aevee.Current.usrname=edtmp.getText()+"";
        edtmp=(EditText)findViewById(R.id.surname);
        Aevee.Current.surname=edtmp.getText()+"";
        edtmp=(EditText)findViewById(R.id.email);
        Aevee.Current.email=edtmp.getText()+"";
        edtmp=(EditText)findViewById(R.id.password);
        Aevee.Current.usrpassword=edtmp.getText()+"";
        edtmp=(EditText)findViewById(R.id.billingAddress);
        Aevee.Current.fullAdress.streetName=edtmp.getText()+"";
        edtmp=(EditText)findViewById(R.id.area);
        Aevee.Current.fullAdress.city=edtmp.getText()+"";
        edtmp=(EditText)findViewById(R.id.postalCode);
        Aevee.Current.fullAdress.code=edtmp.getText()+"";
        edtmp=(EditText)findViewById(R.id.province);
        Aevee.Current.fullAdress.Province=edtmp.getText()+"";
    }

    public void Login(View v)
    {
        Luser l=new Luser();
       EditText edtmp=(EditText)findViewById(R.id.lgn_name);
        l.usrname=edtmp.getText()+"";
        edtmp=(EditText)findViewById(R.id.lgn_password);
        l.usrpassword=edtmp.getText()+"";
    }
    public void add(String item) {
        Aevee.currentlist.add(item);

    }
    public void Log(View v)
    {
        ViewFlipper Vf=(ViewFlipper)findViewById(R.id.VF);
        Vf.showNext();
        Vf.showNext();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mAdapter.setGoogleApiClient( null );
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    public void currentloc(View v)
    {
        //text.setText("CurrentLocation");

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace( mGoogleApiClient, null );
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {

                PlaceLikelihood placeLikelihood = likelyPlaces.get(0);
                String content = "";
                if (placeLikelihood != null && placeLikelihood.getPlace() != null && !TextUtils.isEmpty(placeLikelihood.getPlace().getName()))
                    content = "Most likely place: " + placeLikelihood.getPlace().getName() + "\n";
                if (placeLikelihood != null)
                    content += "Percent change of being there: " + (int) (placeLikelihood.getLikelihood() * 100) + "%";
                text.setText(content);

                likelyPlaces.release();
            }
        });


        //text.setText("DONE CURRENT VIEW");


    }

    public void placepicker(View v)
    {
        text.setText("PlacePicker");
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                displayPlace( PlacePicker.getPlace( data, this ) );
            }
        }
    }

    private void displayPlace( Place place ) {
        if( place == null )
            return;

        String content = "";
        if( !TextUtils.isEmpty( place.getName() ) ) {
            content += "Name: " + place.getName() + "\n";
        }
        if( !TextUtils.isEmpty( place.getAddress() ) ) {
            content += "Address: " + place.getAddress() + "\n";
        }
        if( !TextUtils.isEmpty( place.getPhoneNumber() ) ) {
            content += "Phone: " + place.getPhoneNumber();
        }

        text.setText( content );
    }

    private void findPlaceById( String id ) {
        if( TextUtils.isEmpty( id ) || mGoogleApiClient == null || !mGoogleApiClient.isConnected() )
            return;

        Places.GeoDataApi.getPlaceById( mGoogleApiClient, id ) .setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {


                if (places.getStatus().isSuccess()) {
                    Place place = places.get(0);
                    displayPlace(place);
                    mPredictTextView.setText("");
                    mAdapter.clear();
                }

                //Release the PlaceBuffer to prevent a memory leak
                places.release();
            }
        });
    }
}
