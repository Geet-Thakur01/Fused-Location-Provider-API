# Fused-Location-Provider-API example
3 Simple steps to use Fused-Location-Provider-API in android.

 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fused_example);
        FusedLocationTracker.getTrackerinstance().buildTracker(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FusedLocationTracker.getTrackerinstance().connectGoogleapiClient();

    }
    @Override
    protected void onResume() {
        super.onResume();
        FusedLocationTracker.getTrackerinstance().checkServices();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FusedLocationTracker.getTrackerinstance().disconnectService();
    }
