package com.example.tabsapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    SectionsPagerAdapter mSectionsPagerAdapter;
    TabLayout tabLayout;
    NotificationCompat.Builder builder;
    NotificationManager notificationManager;
    ViewPager mViewPager;
    Intent notificationIntent;
    PendingIntent pendingIntent;
    TaskStackBuilder taskStackBuilder;

    private static final int NOTIFY_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.crashcar1);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (!RequestService.status) {
            startService(new Intent(this, RequestService.class));
        } else {
            stopService(new Intent(this, RequestService.class));
        }

        if (!MyService.status) {
            startService(new Intent(this, MyService.class));
        } else {
            stopService(new Intent(this, MyService.class));
        }

        builder = new NotificationCompat.Builder(this).
                setSmallIcon(R.drawable.crashcar)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("CrashService")
                .setContentText("Работаю!")
                .setAutoCancel(false);

        notificationIntent = new Intent(this, MainActivity.class);

        taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(notificationIntent);

        pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        notificationManager.cancel(NOTIFY_ID);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent Settings = new Intent(this, Settings.class);
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                startActivityForResult(Settings, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainFragment();
                case 1:
                    return new InformationFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Information";
                case 1:
                    return "Table";
            }
            return null;
        }
    }
}
