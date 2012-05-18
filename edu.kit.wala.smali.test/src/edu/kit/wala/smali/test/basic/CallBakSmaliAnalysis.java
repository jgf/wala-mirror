package edu.kit.wala.smali.test.basic;

import java.io.FileNotFoundException;

import org.jf.baksmali.Interface.BakSmaliAnalysis;
import org.jf.dexlib.Interface.DexAnalysis.DexAnalysisException;
import org.jf.dexlib.Interface.DexProgram;

/**
 * 
 * @author Juergen Graf <juergen.graf@gmail.com>
 *
 */
public class CallBakSmaliAnalysis {
	
	public static void main(final String[] args) throws DexAnalysisException, FileNotFoundException {
		System.out.println("========= BAKSMALI =========");
		final DexProgram bakSmaliProg = testBakSmali();
		DexAnalysisUtil.printProgram(bakSmaliProg);
		System.out.print("Dumping graphs... ");
		DexAnalysisUtil.dumpGraphsTo(bakSmaliProg, "./out/baksmali/");
		System.out.println("done.");
	}

	private static DexProgram testBakSmali() throws DexAnalysisException {
		final BakSmaliAnalysis.BakSmaliConfig conf = new BakSmaliAnalysis.BakSmaliConfig();
		System.out.println(conf);
		final BakSmaliAnalysis analysis = new BakSmaliAnalysis(conf);
		final DexProgram prog = analysis.analyze("data/JLex.dex");
		
		return prog;
	}
	
}
