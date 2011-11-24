package id.rtx.realadventure.gps;


import id.rtx.realadventure.gps.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

public class GPSRealAdventureActivity extends MapActivity implements LocationListener {
	
	MapView		googleMapView	= null;	
	boolean 	setsatelite		= false;
	boolean 	settraffic		= false;
	MapController	mapctrl		= null;
	GeoPoint	geopoint		= null;	
	double		latitude		= 0;
	double		longitude 		= 0;
	String		nextlatitude	= null;
	String		nextlongitude 	= null;
	String		prevlatitude	= null;
	String		prevlongitude 	= null;
	double 		altitude		= 0.0;
	int			mapzoom			= 0;
	long		time			= 0;
	float		distance		= 0;
	
	List<Overlay> list			= null;
	
	String xLatitude			= null;
	String xLongitude			= null;
	String xAltitude			= null;
	
	TextView txtLat				= null;	
	TextView txtLon				= null;	
	TextView txtAlt				= null;	
	
	Point pprev					= null;
	Point pnext					= null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Resources resources = this.getResources();
        AssetManager assetManager = resources.getAssets();
        try {
        	InputStream inputStream = assetManager.open("properties/gpsreal.properties");
        	Properties properties = new Properties();
        	properties.load(inputStream);
            time = Integer.parseInt((String) properties.get("TIMEGPS"));
            distance = Integer.parseInt((String) properties.get("DSTGPS"));
            setsatelite = BooleanConstant.ParserBoolean((String) properties.get("SETSAT"));
            setsatelite = BooleanConstant.ParserBoolean((String) properties.get("SETTRAFC"));
            mapzoom = Integer.parseInt((String) properties.get("STDZOOM"));
            latitude = Double.parseDouble((String) properties.get("LAT"));
            longitude = Double.parseDouble((String) properties.get("LON"));	
        } catch (IOException e) {
        	e.printStackTrace();
        }    
        
        prevlatitude = "" + latitude;
        prevlongitude = "" + longitude;
        
        xLatitude = latitude + "";
        xLongitude = longitude + "";
        
        if(xLatitude.length() > 10)
        	xLatitude = (String) xLatitude.subSequence(0, 10);
        
        if(xLongitude.length() > 10)
        	xLongitude = (String) xLongitude.subSequence(0, 10);
        	
        txtLat = (TextView) findViewById(R.id.lat);
        txtLon = (TextView) findViewById(R.id.lon);
        txtAlt = (TextView) findViewById(R.id.alt);
        
		txtLat.setText(xLatitude);
		txtLon.setText(xLongitude);
		txtAlt.setText("0 m");
        
        Criteria ctr = new Criteria();
        ctr.setAccuracy(Criteria.ACCURACY_FINE);
        ctr.setPowerRequirement(Criteria.POWER_HIGH);
        ctr.setAltitudeRequired(true);
        ctr.setBearingRequired(true);
        ctr.setSpeedRequired(true);
        ctr.setCostAllowed(true);
        
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, distance, this);
        
        googleMapView = (MapView) findViewById(R.id.GoogleMap);
        googleMapView.setSatellite(setsatelite);
        googleMapView.setTraffic(settraffic);
        
        geopoint = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
        mapctrl = googleMapView.getController();
        mapctrl.setCenter(geopoint);
        mapctrl.setZoom(mapzoom);  
        
		ZoomControls zoomControls = (ZoomControls) googleMapView.getZoomControls();
		zoomControls.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));		
		googleMapView.addView(zoomControls);
		googleMapView.displayZoomControls(true);
		googleMapView.setSoundEffectsEnabled(true);
		
		GPSMarker marker = new GPSMarker(this);		
        list = googleMapView.getOverlays();
		list.add(marker);
		
		nextlatitude = prevlatitude;
		nextlongitude = prevlongitude;
		
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gpsmenu, menu);
        return true;
    }
    

	public void onLocationChanged(Location location) {
		latitude  = location.getLatitude();
		longitude = location.getLongitude();
		
		nextlatitude = nextlatitude + "~" + latitude;
		nextlongitude = nextlongitude + "~" + longitude;
		
		GPSDrawRoute rt = new GPSDrawRoute(this);
		//list = googleMapView.getOverlays();
		list.add(rt);
		
		altitude  = location.getAltitude();
		
		xLatitude = latitude + "";
	    xLongitude = longitude + "";
	        
        if(xLatitude.length() > 10)
        	xLatitude = (String) xLatitude.subSequence(0, 10);
        
        if(xLongitude.length() > 10)
        	xLongitude = (String) xLongitude.subSequence(0, 10);
        
		txtLat.setText(xLatitude);
		txtLon.setText(xLongitude);
		txtAlt.setText(altitude + " m");
		
		geopoint = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));

		mapctrl.animateTo(geopoint);
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(this.getApplicationContext(),"GPS Disabled",
				Toast.LENGTH_SHORT ).show();
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText( this.getApplicationContext(),"GPS Enabled",
				Toast.LENGTH_SHORT).show();
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}