package com.zic.installfaker;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.widget.*;
import com.softsec.editor.*;
import java.io.*;
import java.util.*;
import net.lingala.zip4j.core.*;
import net.lingala.zip4j.exception.*;
import net.lingala.zip4j.model.*;
import net.lingala.zip4j.util.*;

public class NewAPK extends Activity 
{	
	final String workDir = "/sdcard/Zickie";
	final String xmlSamplePath = workDir + "/sample.xml";
	final String xmlPath = workDir + "/AndroidManifest.xml";
	final String apkPath = workDir + "/sample.apk";
	String pkgName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		Intent callerIntent = this.getIntent();
		Bundle b = callerIntent.getExtras();
		pkgName = (String) b.get("pkgName");
		if(MainAxmlEditor.change(xmlSamplePath, xmlPath, pkgName, "")) {
			copyXmlToApk();
		}else {
			Toast.makeText(this, getString(R.string.error_pkgname), Toast.LENGTH_LONG).show();
		}
		this.finish();
    }
	
	private void copyXmlToApk() {
		try {
			ZipFile zipFile = new ZipFile(apkPath);
			ArrayList filesToAdd = new ArrayList();
			filesToAdd.add(new File(xmlPath));
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			zipFile.addFiles(filesToAdd, parameters);
			
			// install sample apk with new package name
			installApk();
			Toast.makeText(this, pkgName, Toast.LENGTH_SHORT).show();
		} catch (ZipException e) {
			e.printStackTrace();
			Toast.makeText(this, getString(R.string.error_create), Toast.LENGTH_LONG).show();
		}
	}
	
	private void installApk() {
		Intent install = new Intent(Intent.ACTION_VIEW);
		install.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
		install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(install);
	}
}
