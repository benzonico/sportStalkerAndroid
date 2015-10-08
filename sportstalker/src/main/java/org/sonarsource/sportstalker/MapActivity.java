package org.sonarsource.sportstalker;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends Activity {

    public static final String USER_ID = "USER_ID";
    private String userId;
    private TextView textView;
    private List<Location> locations = new ArrayList<Location>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(new Receiver(), filter);
        userId = intent.getStringExtra(USER_ID);
        setContentView(R.layout.activity_map);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isOnline = isOnline();
            textView.setText(textView.getText() + "Online " + isOnline);
            if (isOnline) {
                sendLocations();
            }
        }
    }

    private void sendLocations() {
        new SendGPSDataTask().execute(locations.toArray(new Location[locations.size()]));
    }

    public class MyLocationListener implements LocationListener {


        public MyLocationListener() {
        }

        @Override
        public void onLocationChanged(Location location) {
            locations.add(location);
            if (isOnline()) {
                sendLocations();
            }
            textView.setText(" " + location.getLatitude() + " " + location.getLongitude());
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class SendGPSDataTask extends AsyncTask<Location, Void, Void> {
        @Override
        protected Void doInBackground(Location... locations) {
            if (isOnline()) {
                for (Location location : locations) {
                    UserServices.INSTANCE.updatePosition(userId, "" + location.getLatitude(), "" + location.getLongitude());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            locations.clear();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
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
    public class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_map, container, false);

            LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            textView = (TextView) rootView.findViewById(R.id.text);
            LocationListener mlocListener = new MyLocationListener();
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, mlocListener);
            return rootView;
        }
    }

}
