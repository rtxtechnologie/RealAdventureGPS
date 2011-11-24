package id.rtx.realadventure.gps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class GPSMarker extends Overlay{
	private GPSRealAdventureActivity gpsaa;

	
	GPSMarker(GPSRealAdventureActivity gpsaa){
		this.gpsaa = gpsaa;
	}
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {


		Point myScreenCoords = new Point();
		
		Paint paintbmp = new Paint();
		paintbmp.setAntiAlias(true);
		paintbmp.setStrokeWidth(4);
		paintbmp.setARGB(255, 0, 0, 0);
		paintbmp.setStyle(Paint.Style.FILL);
		
		mapView.getProjection().toPixels(this.gpsaa.geopoint, myScreenCoords);
		Bitmap bmp = BitmapFactory.decodeResource(this.gpsaa.getResources(), R.drawable.marker);		
		canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paintbmp);

		/*//We use a layout to contain the button (or any view you want to draw)
        LinearLayout ll = new LinearLayout(gpsaa.getApplicationContext());
        ll.setOrientation(LinearLayout.VERTICAL);

        //We set the layout parameters
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
             LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //SET THE MARGIN HERE
        layoutParams.setMargins(0, canvas.getHeight(), 100, 100);


        //Declare a new view (here a button)
        Button button=new Button(gpsaa.getApplicationContext());
        
        
        button.setText("TEXT");
        
        //Add it to our linear layout
        ll.addView(button, layoutParams);
        
        //Measure and layout the linear layout before drawing it
        ll.measure(MeasureSpec.getSize(ll.getMeasuredWidth()), MeasureSpec.getSize(ll.getMeasuredHeight()));
        ll.layout(0, 0, canvas.getHeight(), MeasureSpec.getSize(button.getMeasuredHeight()));
        //Finally draw the linear layout on the canvas
        ll.draw(canvas);*/
		
		return true;
	}
}
