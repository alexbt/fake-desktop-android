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
    private static int NB = 1;
    private CountDownTimer timer;
    private View root;
    private int lastDisplayedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                root = findViewById(android.R.id.content).getRootView();
                List<AdView> adViewList = new ArrayList<>();
                adViewList.add(setup(R.id.adViewTop1));
                adViewList.add(setup(R.id.adViewTop2));
                adViewList.add(setup(R.id.adViewTop3));
                adViewList.add(setup(R.id.adViewMiddle1));
                adViewList.add(setup(R.id.adViewMiddle2));
                adViewList.add(setup(R.id.adViewMiddle3));
                adViewList.add(setup(R.id.adViewBottom1));
                adViewList.add(setup(R.id.adViewBottom2));
                adViewList.add(setup(R.id.adViewBottom3));

                lastDisplayedIndex = adViewList.size()/2;

                final Random random = new Random();
                timer = new CountDownTimer(5000, 20) {
                    @Override
                    public void onTick(long l) {
                    }
                    @Override
                    public void onFinish() {
                        AdView randomAd = getRandomAd(adViewList);

                        for (AdView v : adViewList) {
                            if (v == randomAd) {
                                continue;
                            }
                            v.pause();
                            v.setVisibility(View.INVISIBLE);
                        }
                        randomAd.resume();
                        randomAd.setVisibility(View.VISIBLE);
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

            private AdView setup(int adView) {
                AdView mAdView1 = root.findViewById(adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                if (!mAdView1.isActivated()) {
                    mAdView1.loadAd(adRequest);
                    mAdView1.pause();
                    mAdView1.setVisibility(View.INVISIBLE);
                }
                return mAdView1;
            }
        });
    }

    //public void onClick(View v) {
    //Toast.makeText(getApplicationContext(), "Gotcha #" + NB++ + "!", Toast.LENGTH_LONG).show();
    //}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
