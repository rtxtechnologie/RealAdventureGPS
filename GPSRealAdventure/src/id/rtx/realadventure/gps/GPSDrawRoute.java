package id.rtx.realadventure.gps;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class GPSDrawRoute extends Overlay{
	private GPSRealAdventureActivity gpsaa;

	String[] lat = new String[10];
	String[] lon = new String[10];
	
	
	GPSDrawRoute(GPSRealAdventureActivity gpsaa){
		this.gpsaa = gpsaa;
	}
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);
		paint.setARGB(150, 51, 0, 153);
		paint.setStyle(Paint.Style.FILL_AND_STROKE); 
		
		Path mPath = new Path(); 
	    Projection projection = mapView.getProjection(); 
		
		String[] latT = gpsaa.nextlatitude.split("~");
		String[] lonT = gpsaa.nextlongitude.split("~");
		
		int l = lonT.length;        
        Point p = new Point();
        Point pprev = new Point();
        GeoPoint gp = null;
        double lon = 0.0;
        double lat = 0.0;
        Point pnext = new Point();
        for (int i = 0; i < l; i++) {
        	lon = Double.parseDouble(lonT[i]);
            lat = Double.parseDouble(latT[i]);            
        	if(i == 0){
	        	gp = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));	        	 
	        	projection.toPixels(gp, p); 
	        	mPath.moveTo(p.x, p.y);
	        	pprev = p;
        	}else{
	        	gp = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
	        	pnext = new Point(); 
	        	projection.toPixels(gp, pnext); 
	        	
	        	if(i != 0)
	        		mPath.moveTo(pprev.x, pprev.y);
	        	
	        	mPath.lineTo(pnext.x,pnext.y); 	        	
	        	pprev = pnext;
        	}        	
        }    
        canvas.drawPath(mPath, paint); 
		super.draw(canvas, mapView, shadow);
		return true;	
	}
}
