package com.zic.installfaker;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.net.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		findViewById(R.id.mainButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View p1) {
				Toast.makeText(MainActivity.this, getString(R.string.fake_this), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=" + getString(R.string.working)));
				startActivity(intent);
			}
		});
    }
}
