package org.cocos2dx.cpp;
import com.unity3d.ads.android.UnityAds;
import android.app.Activity;
import android.util.Log;
import com.unity3d.ads.android.IUnityAdsListener;
/**
 * Created by solomonli on 4/25/16.
 */
public class UnityAdsNDK {

    public static native void reward(String placementId);

    public static boolean canUnityAdsShow() {
        Log.d("[Ads Test]", "canUnityAdsShow");
        return UnityAds.canShow();
    }

    public static void showUnityAds() {
        Log.d("[Ads Test]", "showUnityAds");
        UnityAds.show();
    }
}
