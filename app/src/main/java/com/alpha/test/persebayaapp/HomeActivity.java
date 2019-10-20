package com.alpha.test.persebayaapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import oogbox.api.odoo.client.helper.data.OdooRecord;
import oogbox.api.odoo.client.helper.data.OdooResult;
import oogbox.api.odoo.client.listeners.IOdooResponse;

import static com.alpha.test.persebayaapp.CommonUtils.getSaldo;


public class HomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private Toolbar toolbar;
    public FloatingActionButton fabBtn;
    SharedPrefManager sharedPrefManager;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = HomeActivity.class.getSimpleName();
    Context context;
    boolean doubleBackToExitPressedOnce = false;
    StoreReloadCalled rcStoreListener;
    LelangReloadCalled rcLelangListener;

    public interface StoreReloadCalled{
        void onReloadCalled();
    }

    public void setReloadCallback(StoreReloadCalled rcStoreListener){
        this.rcStoreListener = rcStoreListener;
    }

    public interface LelangReloadCalled {
        void onReloadCalled();
    }

    public void setLelangReloadCallback(LelangReloadCalled rcLelangListener){
        this.rcLelangListener = rcLelangListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        viewPager = findViewById(R.id.view_pager);
        HomeFragmentPageAdapter adapter = new HomeFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 2:
                        fabBtn.hide();
                        fabBtn.setImageResource(R.drawable.ic_add);
                        fabBtn.show();
                        break;
                    case 3:
                        fabBtn.hide();
                        fabBtn.setImageResource(R.drawable.ic_edit);
                        fabBtn.show();
                        break;
                    default:
                        fabBtn.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        disableShiftMode(navigation);
        fabBtn = (FloatingActionButton) findViewById(R.id.shared_fab);
        fabBtn.hide();
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fabIntent = new Intent();
                if (sharedPrefManager.getSpFab().equalsIgnoreCase("Account")){
                    fabIntent = new Intent(HomeActivity.this,AccountEditActivity.class);
                    startActivity(fabIntent);
                }else {
                    getSaldo(context, new IOdooResponse() {
                        @Override
                        public void onResult(OdooResult result) {
                            OdooRecord[] Records = result.getRecords();
                            Intent fabIntent = new Intent();
                            Integer requestCode=0;
                            if (sharedPrefManager.getSpFab().equalsIgnoreCase("Store")) {
                                fabIntent = new Intent(HomeActivity.this, StoreAddActivity.class);
                                fabIntent.putExtra("id", "false");
                                requestCode = 1;
                            } else {
                                fabIntent = new Intent(HomeActivity.this, LelangAddActivity.class);
                                fabIntent.putExtra("id", "false");
                                requestCode = 0;
                            }
                            for (final OdooRecord record : Records) {
                                if (record.getString("state").equalsIgnoreCase("draft")) {
                                    Toast.makeText(getBaseContext(), "Update your profile first!", Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivityForResult(fabIntent,requestCode);
                                }
                            }
                        }
                    });
                }
            }
        });
        sharedPrefManager =  new SharedPrefManager(this);
//        initTitle();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(config.TOPIC_GLOBAL);

                } else if (intent.getAction().equals(config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, " Home Activity " + requestCode + " - " +resultCode);
        rcStoreListener.onReloadCalled();
//                rcLelangListener.onReloadCalled();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            toolbar.setTitle(item.getTitle());
            switch (item.getItemId()) {
                case R.id.action_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.action_berita:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.action_belanja:
                    fabBtn.setImageResource(R.drawable.ic_add);
                    viewPager.setCurrentItem(2);
                    String Belanja = sharedPrefManager.getSpFabBelanja();
                    sharedPrefManager.saveSPString(sharedPrefManager.SP_FAB,Belanja);
                    return true;
                case R.id.action_account:
                    fabBtn.setImageResource(R.drawable.ic_edit);
                    viewPager.setCurrentItem(3);
                    sharedPrefManager.saveSPString(sharedPrefManager.SP_FAB,"Account");
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        Drawable ticket = menu.findItem(R.id.ticket).getIcon();
        Drawable checkout = menu.findItem(R.id.checkout).getIcon();
        Drawable logout = menu.findItem(R.id.logout).getIcon();
        DrawableCompat.setTint(ticket, ContextCompat.getColor(this,R.color.colorTrueState));
        DrawableCompat.setTint(checkout, ContextCompat.getColor(this,R.color.colorTrueState));
        DrawableCompat.setTint(logout, ContextCompat.getColor(this,R.color.colorTrueState));
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ticket:
                Intent ticketIntent = new Intent(HomeActivity.this, TicketActivity.class);
                startActivity(ticketIntent);
                break;
            case R.id.checkout :
                Intent CheckoutIntent = new Intent(HomeActivity.this,CheckoutActivity.class);
                startActivity(CheckoutIntent);
                break;
            case R.id.donasi :
                Intent DonationIntent = new Intent(HomeActivity.this,DonationActivity.class);
                startActivity(DonationIntent);
                break;
            case R.id.notifikasi :
                Intent NotifikasiIntent = new Intent(HomeActivity.this,NotifikasiActivity.class);
                startActivity(NotifikasiIntent);
                break;
            case R.id.logout :
                //Kode disini akan di eksekusi saat tombol search di klik
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Do You Want to  Logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    public static void disableShiftMode(BottomNavigationView view) {
//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
//        try {
//            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
//            shiftingMode.setAccessible(true);
//            shiftingMode.setBoolean(menuView, false);
//            shiftingMode.setAccessible(false);
//            for (int i = 0; i < menuView.getChildCount(); i++) {
//                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
//                item.setShifting(false);
////                item.setShiftingMode(false);
//                // set once again checked value, so view will be updated
//                item.setChecked(item.getItemData().isChecked());
//            }
//        } catch (NoSuchFieldException e) {
//            //Timber.e(e, "Unable to get shift mode field");
//        } catch (IllegalAccessException e) {
//            //Timber.e(e, "Unable to change value of shift mode");
//        }
//    }


    private static class HomeFragmentPageAdapter extends FragmentPagerAdapter {


        public HomeFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance();
                case 1:
                    return BeritaFragment.newInstance();
                case 2:
                    return BelanjaFragment.newInstance();
                case 3:
                    return AccountFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
