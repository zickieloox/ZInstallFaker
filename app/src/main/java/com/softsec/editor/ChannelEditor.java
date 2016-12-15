package com.softsec.editor;

import java.util.List;

import com.softsec.editor.decode.AXMLDoc;
import com.softsec.editor.decode.BTagNode;
import com.softsec.editor.decode.BXMLNode;
import com.softsec.editor.decode.StringBlock;
import com.softsec.editor.decode.BTagNode.Attribute;
import com.softsec.editor.utils.TypedValue;

public class ChannelEditor {

	private String newPackageName_value = "";
	private String newAppLabel_value = "";

	private int channel_package;
	private int namespace;
	private int tag_app;  //<application>
	private int attr_label;//<application>   android:label
	private int channel_label;//  <application>   android:label

	private int attr_package;
	private int package_name;

	private String Package_Name;

	private AXMLDoc doc;

	public ChannelEditor(AXMLDoc doc){
		this.doc = doc;
	}

	//add resource and get mapping id for "android:package"
	private void registStringBlock2(StringBlock sb){
		channel_package = sb.putString(newPackageName_value);
	}

	//change package name 
	private void changePackageName(StringBlock sb){
		attr_package=sb.putString("package");
		BTagNode manifest = (BTagNode) doc.getManifestNode();
		Attribute attr_of_manifest[]=manifest.getAttribute();
		for(int i=0;i<attr_of_manifest.length;i++){
			if(attr_of_manifest[i].mName==attr_package){
				package_name=attr_of_manifest[i].mValue;
				attr_of_manifest[i].setValue(TypedValue.TYPE_STRING, channel_package);//change the value of attribute android:pakage
				break;
			}	
		}
		Package_Name=sb.getStringFor(package_name);
		//System.out.println("*******************old_package_name="+Package_Name);
		//System.out.println("*******************new_package_name="+newPackageName_value);
	}

	//add resource and get mapping ids for <application> "android:label"
	private void registStringBlock1(StringBlock sb){
		namespace = sb.putString("http://schemas.android.com/apk/res/android");
		attr_label = sb.putString("label");
		tag_app = sb.putString("application");
		channel_label = sb.putString(newAppLabel_value);
	}

	//change the value of android:label in tag <application>
	private void editNode1(AXMLDoc doc,StringBlock sb){
		BXMLNode manifest = doc.getManifestNode(); //manifest node
		List<BXMLNode> children = manifest.getChildren();
		BTagNode softsec_application = null;	
		end:for(BXMLNode node : children){
			BTagNode m = (BTagNode)node;
			if(tag_app == m.getName()) {
				softsec_application = m;
				break end;
			}
		}	
		if(softsec_application != null){
			Attribute attr[]=softsec_application.getAttribute();
			for(int i=0;i<attr.length;i++){
				if(attr[i].mName==attr_label){
					attr[i].setValue(TypedValue.TYPE_STRING, channel_label);//change the value of attribute android:label
					break;
				}
			}
			//System.out.println("*******************new_app_label="+newAppLabel_value);
		}
	}

	public void commit(String newPackageName, String newAppLabel) {
		this.newPackageName_value = newPackageName;
		if(newAppLabel.equals("")) {
			this.newAppLabel_value = "_Z_" + newPackageName;
		}else{
			this.newAppLabel_value = newAppLabel;
		}
		registStringBlock2(doc.getStringBlock());
		changePackageName(doc.getStringBlock());
		registStringBlock1(doc.getStringBlock());
		editNode1(doc,doc.getStringBlock());	
	}
}
