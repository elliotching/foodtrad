package kali.foodtrad;

/**
 * Created by elliotching on 11-Apr-17.
 */
//
//class ActivityMaps_v22 extends AppCompatActivity
//        implements OnMapReadyCallback,
//        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
//        LocationListener {
//
//    private GoogleMap map;
//    private LocationRequest mLocationRequest;
//    private GoogleApiClient mGoogleApiClient;
//    private Location lastLocation;
//    private Marker marker;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()){
//
//            buildGoogleApiClient();
//            mGoogleApiClient.connect();
//
//        }
//
//        if (map == null) {
//            MapFragment mapFragment = (MapFragment) getFragmentManager()
//                    .findFragmentById(R.id.map);
//
//            mapFragment.getMapAsync(this);
//        }
//    }
//
//    @Override
//    public void onMapReady(GoogleMap retMap) {
//
//        map = retMap;
//
//        setUpMap();
//
//    }
//
//    public void setUpMap(){
//
//        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        map.setMyLocationEnabled(true);
//    }
//
//    @Override
//    protected void onPause(){
//        super.onPause();
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        }
//    }
//
//    protected synchronized void buildGoogleApiClient() {
//        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        Toast.makeText(this,"onConnected", Toast.LENGTH_SHORT).show();
//
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//
//        //mLocationRequest.setSmallestDisplacement(0.1F);
//
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        lastLocation = location;
//
//        //remove previous current location Marker
//        if (marker != null){
//            marker.remove();
//        }
//
//        double dLatitude = lastLocation.getLatitude();
//        double dLongitude = lastLocation.getLongitude();
//        marker = map.addMarker(new MarkerOptions().position(new LatLng(dLatitude, dLongitude))
//                .title("My Location").icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLatitude, dLongitude), 8));
//
//    }
//
//}
