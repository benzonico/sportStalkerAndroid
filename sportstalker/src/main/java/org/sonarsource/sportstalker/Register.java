package org.sonarsource.sportstalker;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Register extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment implements View.OnClickListener {

        private Button button;
        private String username;

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_register, container, false);
            button = (Button) rootView.findViewById(R.id.trackButton);
            EditText editText = (EditText) rootView.findViewById(R.id.username);
            username = editText.getText().toString();
            button.setOnClickListener(this);
            return rootView;
        }

        @Override
        public void onClick(View view) {
            ((Button) view).setText("CLICKED!");
            LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener mlocListener = new MyLocationListener(username, button);
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, mlocListener);

        }
    }

    public class MyLocationListener implements LocationListener {

        private final String username;
        private final Button button;

        public MyLocationListener(String username, Button button) {
            this.username = username;
            this.button = button;
        }

        @Override
        public void onLocationChanged(Location location) {
            button.setText(username+" "+location.getLatitude()+" "+location.getLongitude());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

}
