package com.alexbt.random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.lifecycle.LifecycleObserver;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity implements LifecycleObserver {
    private static CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_main);
        setTheme(android.R.style.Theme);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                initAds(R.id.adViewTop1,
                        R.id.adViewTop2,
                        R.id.adViewTop3,
                        R.id.adViewMiddle1,
                        R.id.adViewMiddle2,
                        R.id.adViewMiddle3,
                        R.id.adViewBottom1,
                        R.id.adViewBottom2,
                        R.id.adViewBottom3);

                timer = new CountDownTimer(5000, 20) {
                    private final List<AdView> adViewList = new ArrayList<>();
                    private int lastDisplayedIndex;
                    private final Random RANDOM = new Random();

                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        initList();
                        AdView randomAd = getRandomAd(adViewList);

                        for (AdView v : adViewList) {
                            if (v == randomAd) {
                                continue;
                            }
                            v.pause();
                            v.setVisibility(View.INVISIBLE);
                        }
                        if (randomAd != null) {
                            randomAd.resume();
                            randomAd.setVisibility(View.VISIBLE);
                        }
                        timer.start();
                    }

                    private synchronized void initList() {
                        View root = findViewById(android.R.id.content).getRootView();
                        if (root == null) {
                            root = getWindow().getDecorView().findViewById(android.R.id.content);
                        }
                        adViewList.clear();
                        adViewList.add(initAd(root, R.id.adViewTop1));
                        adViewList.add(initAd(root, R.id.adViewTop2));
                        adViewList.add(initAd(root, R.id.adViewTop3));
                        adViewList.add(initAd(root, R.id.adViewMiddle1));
                        adViewList.add(initAd(root, R.id.adViewMiddle2));
                        adViewList.add(initAd(root, R.id.adViewMiddle3));
                        adViewList.add(initAd(root, R.id.adViewBottom1));
                        adViewList.add(initAd(root, R.id.adViewBottom2));
                        adViewList.add(initAd(root, R.id.adViewBottom3));
                    }

                    private AdView getRandomAd(List<AdView> adViewList) {
                        int nextInt = RANDOM.nextInt(adViewList.size());
                        boolean atLeastOneDistance = Math.abs(lastDisplayedIndex - nextInt) > 1;

                        int i = 0;
                        while (!atLeastOneDistance && i < 5) {
                            nextInt = RANDOM.nextInt(adViewList.size());
                            atLeastOneDistance = Math.abs(lastDisplayedIndex - nextInt) > 1;
                            i++;
                        }
                        lastDisplayedIndex = nextInt;
                        return adViewList.get(lastDisplayedIndex);
                    }
                }.start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.alexbt.random.fake-desktop", Context.MODE_PRIVATE);
        boolean firstTime = sharedPreferences.getBoolean("firstTime", true);
        if (firstTime) {
            sharedPreferences.edit()
                    .putBoolean("firstTime", false)
                    .apply();
            moveTaskToBack(true);
        }
    }

    private AdView initAd(View root, int adViewId) {
        AdView adView = root.findViewById(adViewId);
        if (!adView.isActivated()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.pause();
            adView.setVisibility(View.INVISIBLE);
        }
        return adView;
    }

    private void initAds(int... adViewId) {
        View root = getRoot();
        for (int id : adViewId) {
            initAd(root, id);
        }
    }

    public View getRoot() {
        View root = findViewById(android.R.id.content).getRootView();
        if (root == null) {
            root = getWindow().getDecorView().findViewById(android.R.id.content);
        }
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
