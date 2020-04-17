package com.rescreation.composition.util;

import android.content.Context;
import android.os.CountDownTimer;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public class Utils implements RewardedVideoAdListener {

    CountDownTimer cTimer_1 = null;
    CountDownTimer cTimer_2 = null;
    Context mContext=null;
    String interesttialAdID="",rewardedAdID="";
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;

    public Utils(Context mContext, String interesttialAdID, String rewardedAdID, String admobID) {
        this.mContext = mContext;
        this.interesttialAdID = interesttialAdID;
        this.rewardedAdID = rewardedAdID;

        MobileAds.initialize(mContext, admobID);

        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(interesttialAdID);


        //Rewarded Ad
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);
        mRewardedVideoAd.setRewardedVideoAdListener((RewardedVideoAdListener) mContext);


    }

    //start timer function
    public void startTimer_1(int time) {
        cTimer_1 = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {

                ShowAd();
                startTimer_1(60*1000);
            }
        };
        cTimer_1.start();
    }


    //cancel timer
    public void cancelTimer_1() {
        if(cTimer_1!=null)
            cTimer_1.cancel();
    }

    public void startTimer_2(int time) {
        cTimer_2 = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {

                loadRewardedVideo();
                startTimer_2(40*1000);
            }
        };
        cTimer_2.start();
    }


    //cancel timer
    public void cancelTimer_2() {
        if(cTimer_2!=null)
            cTimer_2.cancel();
    }

    public void ShowAd(){
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

    }

    private void loadRewardedVideo() {
        mRewardedVideoAd.loadAd(rewardedAdID,
                new AdRequest.Builder()
                        .build());
    }


    @Override
    public void onRewardedVideoAdLoaded() {
        try {
            if (mRewardedVideoAd.isLoaded()) {
                mRewardedVideoAd.show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    public RewardedVideoAd getmRewardedVideoAd() {
        return mRewardedVideoAd;
    }


}
