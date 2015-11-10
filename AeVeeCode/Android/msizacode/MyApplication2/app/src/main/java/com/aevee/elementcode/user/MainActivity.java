package com.aevee.elementcode.user;

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
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
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

/**
 * Created by Commader on 2015/10/24.
 */
public class MainActivity extends AppCompatActivity implements
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
        setContentView(R.layout.activity_map_object);
        text=(TextView) findViewById(R.id.textView);
        mPredictTextView = (AutoCompleteTextView) findViewById( R.id.predicttextview );
        mAdapter = new AutoCompleteAdapter( this );
        mPredictTextView.setAdapter( mAdapter );


        mPredictTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AutoCompletePlace place = (AutoCompletePlace) parent.getItemAtPosition(position);
                findPlaceById(place.getId());
            }
        });

        inti(this);
        //text=(TextView) findViewById(R.id.button);
        // text2=(TextView) findViewById(R.id.button2);
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


    public void Log(View v)
    {
        /*
        ViewFlipper vf= (ViewFlipper) findViewById(R.id.VF);
        vf.showNext();
        vf.showNext();*/
    }
    public void Reg(View v)
    {
        /*
        ViewFlipper vf= (ViewFlipper) findViewById(R.id.VF);
        vf.showNext();*/

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
                try {
                    PlaceLikelihood placeLikelihood = likelyPlaces.get(0);
                    String content = "";
                    if (placeLikelihood != null && placeLikelihood.getPlace() != null && !TextUtils.isEmpty(placeLikelihood.getPlace().getName()))
                        content = "Most likely place: " + placeLikelihood.getPlace().getName() + "\n";
                    if (placeLikelihood != null)
                        content += "Percent change of being there: " + (int) (placeLikelihood.getLikelihood() * 100) + "%";
                    text.setText(content);

                    likelyPlaces.release();
                }
                catch (Exception e)
                {
                    Log.d("PPPP","OOOOOO"+e.toString());
                    e.printStackTrace();
                }
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

        text.setText(content);
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
