package com.zic.installfaker;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import android.os.Build.VERSION;

public class GetPkgName extends Activity {
	@SuppressLint({"NewApi"})
	protected void onCreate(Bundle var1) {
		super.onCreate(var1);
		Intent var2 = this.getIntent();
		String var3 = var2.getDataString();
		if(var3 == null) {
			var3 = var2.getStringExtra("android.intent.extra.TEXT");
		}

		String var4;
		if(var3 == null) {
			var4 = "";
		} else {
			var4 = var3;
		}
		String pkgName;
		
		/* get the pakacge name from urls:
			https://play.gooogle.com/details?id=<pkgName>&<something>
			market://details?id=<pkgName>&<something>
		*/
		
		if(var4.contains("&") && var4.contains("details?id=")) {
			pkgName = var4.substring(var4.indexOf("=") + 1, var4.indexOf("&"));
		}else if(var4.contains("details?id=")){
			pkgName = var4.substring(var4.indexOf("=") + 1);
		}else{
			pkgName = null;
		}
		if(pkgName != null) {
			
			// copy package name to clipboard and toast
//			if(VERSION.SDK_INT >= 11) {
//				((ClipboardManager)this.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("pkgName", pkgName));
//			} else {
//				((android.text.ClipboardManager)this.getSystemService("clipboard")).setText(pkgName);
//			}
//			Toast.makeText(this, pkgName + getString(R.string.copied), Toast.LENGTH_SHORT).show();
			
			// start NewAPK activity
			Intent intent = new Intent(GetPkgName.this, NewAPK.class);
			intent.putExtra("pkgName", pkgName);
			startActivity(intent);
		}else {
			Toast.makeText(this, getString(R.string.error_url), Toast.LENGTH_LONG).show();
		}
		this.finish();
	}
}
