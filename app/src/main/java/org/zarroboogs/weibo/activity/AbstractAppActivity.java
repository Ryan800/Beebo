
package org.zarroboogs.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Toast;

import org.apache.http.client.CookieStore;

import org.zarroboogs.weibo.BeeboApplication;
import org.zarroboogs.weibo.bean.AccountBean;
import org.zarroboogs.weibo.setting.SettingUtils;
import org.zarroboogs.weibo.support.asyncdrawable.TimeLineBitmapDownloader;
import org.zarroboogs.weibo.support.utils.ViewUtility;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

import java.lang.reflect.Field;

import org.zarroboogs.util.net.WeiboException;

public class AbstractAppActivity extends WeiboDataProviderActivity {

    protected int theme = 0;
    public AccountBean mAccountBean;
    private static AsyncHttpClient mAsyncHttoClient;
    private static CookieStore cookieStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            theme = SettingUtils.getAppTheme();
        } else {
            theme = savedInstanceState.getInt("theme");
        }
        setTheme(theme);

        super.onCreate(savedInstanceState);

        if (cookieStore == null) {
            cookieStore = new PersistentCookieStore(getApplicationContext());
        }

        if (mAsyncHttoClient == null) {
            mAsyncHttoClient = new AsyncHttpClient();
        }

        mAsyncHttoClient.setCookieStore(cookieStore);

        forceShowActionBarOverflowMenu();
        BeeboApplication.getInstance().setActivity(this);

    }

    public void disPlayHomeAsUp(int toobrID){
    	Toolbar toolbar = ViewUtility.findViewById(this, toobrID);
    	disPlayHomeAsUp(toolbar);
    }
	public void disPlayHomeAsUp(Toolbar toolbar) {
		if (toolbar != null) {
			setSupportActionBar(toolbar);
	        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	        toolbar.setNavigationOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		}
	}

    public AsyncHttpClient getAsyncHttpClient() {
        if (mAsyncHttoClient == null) {
            mAsyncHttoClient = new AsyncHttpClient();
        }
        return mAsyncHttoClient;
    }

    public CookieStore getCookieStore() {
        if (cookieStore == null) {
            cookieStore = new PersistentCookieStore(getApplicationContext());
        }
        return cookieStore;
    }

    public AccountBean getAccount() {
        return mAccountBean;
    }

    @Override
    protected void onResume() {
        super.onResume();
        BeeboApplication.getInstance().setCurrentRunningActivity(this);

        if (theme != SettingUtils.getAppTheme()) {
            reload();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (BeeboApplication.getInstance().getCurrentRunningActivity() == this) {
            BeeboApplication.getInstance().setCurrentRunningActivity(null);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", theme);

    }

    private void forceShowActionBarOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ignored) {

        }
    }

    public void reload() {

        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
        TimeLineBitmapDownloader.refreshThemePictureBackground();
    }

    public TimeLineBitmapDownloader getBitmapDownloader() {
        return TimeLineBitmapDownloader.getInstance();
    }

    protected void dealWithException(WeiboException e) {
        Toast.makeText(this, e.getError(), Toast.LENGTH_SHORT).show();
    }
}
