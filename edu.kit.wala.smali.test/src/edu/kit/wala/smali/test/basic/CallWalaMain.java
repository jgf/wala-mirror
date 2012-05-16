package edu.kit.wala.smali.test.basic;

import org.jf.baksmali.WalaMain;

public class CallWalaMain {
	public static void main(String[] args) {
		WalaMain.main(new String[] {"data/core.jar:data/ext.jar:data/framework.jar:data/android.policy.jar:data/services.jar","data/JLex.dex"});
		//WalaMain.main(new String[] {"","JLex.dex"});
	}
}
