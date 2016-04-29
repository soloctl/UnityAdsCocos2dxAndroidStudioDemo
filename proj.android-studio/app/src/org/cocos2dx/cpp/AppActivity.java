/****************************************************************************
Copyright (c) 2015 Chukong Technologies Inc.
 
http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/
package org.cocos2dx.cpp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.chukong.cocosplay.client.CocosPlayClient;
import com.unity3d.ads.android.IUnityAdsListener;
import com.unity3d.ads.android.UnityAds;
import com.unity3d.ads.android.webapp.IUnityAdsWebDataListener;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxEditBoxHelper;
import org.cocos2dx.lib.Cocos2dxHandler;
import org.cocos2dx.lib.Cocos2dxHelper;
import org.cocos2dx.lib.Cocos2dxVideoHelper;
import org.cocos2dx.lib.Cocos2dxWebViewHelper;

public class AppActivity extends Cocos2dxActivity implements IUnityAdsListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAd();
        //UnityAds.changeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UnityAds.changeActivity(this);
    }


    private void initAd(){
        Log.d("[Ads Test]", "initializing ad");
        UnityAds.setTestMode(true);
        UnityAds.init((Activity) this, "1044314", (IUnityAdsListener) this);
    }

    private void showAd(){

        Log.d("[Ads Test]", "clicked on show ad button");

        UnityAds.setZone("rewardedVideo");

        if(UnityAds.canShow()) {
            UnityAds.show();
        }
    }

    public void onHide(){
        Log.d("[Ads Test Lifecycle]", "onHide");
    }

    public void onShow(){
        Log.d("[Ads Test Lifecycle]", "onShow");
    }

    public void onVideoStarted(){
        Log.d("[Ads Test Lifecycle]", "onVideoStarted");
    }

    public  void onVideoCompleted(String itemKey, boolean skipped){
        TestNDK.reward("rewardedVideo");
        Log.d("[Ads Test Lifecycle]", "[onVideoCompleted] with key="+itemKey+", and skipped="+skipped);
    }

    public void onFetchCompleted(){
        Log.d("[Ads Test Lifecycle]", "onFetchCompleted");
        //showAd();
    }

    public void onFetchFailed(){
        Log.d("[Ads Test Lifecycle]", "onFetchFailed");
    }
}
