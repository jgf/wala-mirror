package edu.kit.wala.smali.test.basic;

import org.jf.dexlib.Interface.DexAnalysis;
import org.jf.dexlib.Interface.DexAnalysis.DexAnalysisException;
import org.jf.dexlib.Interface.DexProgram;

public class CallDexAnalysis {
	public static void main(String[] args) throws DexAnalysisException {
		final DexAnalysis.Config conf = new DexAnalysis.Config();
		System.out.println(conf);
		final DexAnalysis analysis = new DexAnalysis(conf);
		final DexProgram prog = analysis.analyze("data/JLex.dex");
		System.out.println(prog);
	}
}
