package com.zic.installfaker;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.net.*;
import android.os.*;
import android.util.*;
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
	private String assetsCopyDir;
	private String workDir;
	private String xmlSamplePath;
	private String xmlPath;
	private String apkPath;
	private String pkgName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

		assetsCopyDir = Environment.getExternalStorageDirectory().getAbsolutePath();
		workDir = assetsCopyDir + "/Zickie";
		xmlSamplePath = workDir + "/sample.xml";
		xmlPath = workDir + "/AndroidManifest.xml";
		apkPath = workDir + "/sample.apk";

		// check first run
		firstRun();
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

	private boolean firstRun() {
		SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        if (prefs.getBoolean("first_run", true)) {
            Toast.makeText(NewAPK.this, "First Run - Checking Necessary File!", Toast.LENGTH_SHORT).show();
			//copy assets
			copyFileOrDir("Zickie");
            prefs.edit().putBoolean("first_run", false).commit();
			return true;
        }else {
			return false;
		}
    }

	private void copyFileOrDir(String path) {
		AssetManager assetManager = this.getAssets();
		String assets[] = null;
		try {
			assets = assetManager.list(path);
			if (assets.length == 0) {
				copyFile(path);
			} else {
				String fullPath = assetsCopyDir + "/" + path;
				File dir = new File(fullPath);
				if (!dir.exists())
					dir.mkdir();
				for (int i = 0; i < assets.length; ++i) {
					copyFileOrDir(path + "/" + assets[i]);
				}
			}
		} catch (IOException ex) {
			Log.e("tag", "I/O Exception", ex);
		}
	}

	private void copyFile(String filename) {
		AssetManager assetManager = this.getAssets();

		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(filename);
			String newFileName = assetsCopyDir + "/" + filename;
			out = new FileOutputStream(newFileName);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			Log.e("tag", e.getMessage());
		}
	}

}
