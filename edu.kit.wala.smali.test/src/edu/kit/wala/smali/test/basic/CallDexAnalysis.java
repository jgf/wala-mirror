package edu.kit.wala.smali.test.basic;

import org.jf.baksmali.Interface.BakSmaliAnalysis;
import org.jf.dexlib.Interface.DexAnalysis.DexAnalysisException;
import org.jf.dexlib.Interface.DexProgram;

public class CallDexAnalysis {
	public static void main(String[] args) throws DexAnalysisException {
		final BakSmaliAnalysis.Config conf = new BakSmaliAnalysis.Config();
		System.out.println(conf);
		final BakSmaliAnalysis analysis = new BakSmaliAnalysis(conf);
		final DexProgram prog = analysis.analyze("data/JLex.dex");
		System.out.println(prog);
	}
}
