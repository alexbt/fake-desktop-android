package com.alexbt.random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeDesktopActivity extends Activity {
    private CountDownTimer timer;
    private int lastDisplayedIndex;
    private List<AdView> adViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                View root = getRoot();
                adViewList.add(initAd(root, R.id.adViewTop1));
                adViewList.add(initAd(root, R.id.adViewTop2));
                adViewList.add(initAd(root, R.id.adViewTop3));
                adViewList.add(initAd(root, R.id.adViewMiddle1));
                adViewList.add(initAd(root, R.id.adViewMiddle2));
                adViewList.add(initAd(root, R.id.adViewMiddle3));
                adViewList.add(initAd(root, R.id.adViewBottom1));
                adViewList.add(initAd(root, R.id.adViewBottom2));
                adViewList.add(initAd(root, R.id.adViewBottom3));

                lastDisplayedIndex = adViewList.size() / 2;
                //adViewList.get(lastDisplayedIndex).setVisibility(View.VISIBLE);
                //adViewList.get(lastDisplayedIndex).resume();

                final Random random = new Random();
                timer = new CountDownTimer(5000, 20) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        View root = getRoot();
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

                    private AdView getRandomAd(List<AdView> adViewList) {
                        int nextInt = random.nextInt(adViewList.size());
                        boolean atLeastOneDistance = Math.abs(lastDisplayedIndex - nextInt) > 1;

                        int i = 0;
                        while (!atLeastOneDistance && i < 5) {
                            nextInt = random.nextInt(adViewList.size());
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

    private View getRoot() {
        View root = findViewById(android.R.id.content).getRootView();
        if (root == null) {
            root = getWindow().getDecorView().findViewById(android.R.id.content);
        }
        return root;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
