package app.com.example.snehagaur.droughtmap;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final LatLng CENTER = new LatLng(37.708687, -121.724917);
    private GoogleMap mMap;
    Drought droughtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Polygon severeDrought;

        parseData();
        int numOfSevereAreas = droughtData.severe.length;
        int numOfModerateAreas = droughtData.moderate.length;
        int areaCounter;

        for (areaCounter = 0; areaCounter < numOfSevereAreas; areaCounter++) {
            PolygonOptions severeArea = new PolygonOptions();
            for (LatLng currentLatLng : droughtData.severe[areaCounter].coordinates) {
                severeArea
                        .add(currentLatLng)
                        .fillColor(Color.argb(75, 255, 0, 0))
                        .strokeColor(Color.TRANSPARENT);
            }
            mMap.addPolygon(severeArea);
        }

        for (areaCounter = 0; areaCounter < numOfModerateAreas; areaCounter++) {
            PolygonOptions moderateArea = new PolygonOptions();
            for (LatLng currentLatLng : droughtData.moderate[areaCounter].coordinates) {
                moderateArea
                        .add(currentLatLng)
                        .fillColor(Color.argb(200, 255, 255, 100))
                        .strokeColor(Color.TRANSPARENT);
            }

            mMap.addPolygon(moderateArea);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CENTER, 4));



    }

    public void parseData() {
            Gson gson = new Gson();

            JsonReader reader = readFile();
            if (reader != null) {
                Drought droughtJSON = gson.fromJson(reader, Drought.class);
                this.droughtData = droughtJSON;
            }
            else {
                String jsonString = new JSONString().jsonString;
                Drought droughtJSON = gson.fromJson(jsonString, Drought.class);
                this.droughtData = droughtJSON;
            }
    }

    public static JsonReader readFile() {
        try {
            JsonReader reader = new JsonReader(new FileReader("drought.json"));
            return reader;
        }
        catch (FileNotFoundException fileNotFound) {
            Log.d("Parsing JSON: ", "file not found");
        }
        return null;
    }
}
