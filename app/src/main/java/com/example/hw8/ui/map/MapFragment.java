package com.example.hw8.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hw8.MainActivity;
import com.example.hw8.R;
import com.example.hw8.WeatherData;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

public class MapFragment extends Fragment {

    private MapViewModel mapViewModel;
    private GoogleMap mMap;
    private WeatherData weatherData = WeatherData.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel =
                ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                TileProvider tileProvider = new UrlTileProvider(256, 256) {

                    @Override
                public URL getTileUrl(int x, int y, int zoom) {            /* Define the URL pattern for the tile images */
                    String s = String.format("https://tile.openweathermap.org/map/temp_new/%d/%d/%d.png?appid=e273afec1d1a815c17ea839cc1b9357a", zoom, x, y);
                    if (!checkTileExists(x, y, zoom)) {
                        return null;
                    }            try {
                        return new URL(s);
                    } catch (MalformedURLException e) {
                        throw new AssertionError(e);
                    }
                }        /*
                    * Check that the tile server supports the requested x, y and zoom.
                    * Complete this stub according to the tile range you support.
                    * If you support a limited range of tiles at different zoom levels, then you
                    * need to define the supported x, y range at each zoom level.
                    */
                private boolean checkTileExists(int x, int y, int zoom) {
                    int minZoom = 12;
                    int maxZoom = 16;            return (zoom >= minZoom && zoom <= maxZoom);
                    }
                };
                WeatherData w = WeatherData.getInstance();
                double lat = Double.parseDouble(w.getLat());
                double lon = Double.parseDouble(w.getLon());
                LatLng loc = new LatLng(lat,lon);
                mMap.addMarker(new MarkerOptions().position(loc).title("You are here!"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
                TileOverlay tileOverlay = googleMap.addTileOverlay(new TileOverlayOptions()
                            .tileProvider(tileProvider));

            }
        });


        return root;
    }


    public void onMapReady(GoogleMap googleMap) {

    }

}