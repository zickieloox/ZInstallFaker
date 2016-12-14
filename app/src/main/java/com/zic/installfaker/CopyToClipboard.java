package com.zic.installfaker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.widget.Toast;

public class CopyToClipboard extends Activity {
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
		String packageName;
		if(var4.contains("&") && var4.contains("details?id=")) {
			packageName = var4.substring(var4.indexOf("=") + 1, var4.indexOf("&"));
		}else if(var4.contains("details?id=")){
			packageName = var4.substring(var4.indexOf("=") + 1);
		}else{
			packageName = null;
		}
		if(packageName != null) {
			if(VERSION.SDK_INT >= 11) {
				((ClipboardManager)this.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("copied package name", packageName));
			} else {
				((android.text.ClipboardManager)this.getSystemService("clipboard")).setText(packageName);
			}

			Toast.makeText(this, packageName + " - Copied To Clipboard", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(this, "Error! Invalid Playstore Link", Toast.LENGTH_SHORT).show();
		}
		this.finish();
	}
}
