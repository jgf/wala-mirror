package edu.kit.wala.smali.test.basic;

import java.io.FileNotFoundException;

import org.jf.dexlib.Interface.DexAnalysis.DexAnalysisException;
import org.jf.dexlib.Interface.DexProgram;
import org.jf.smali.Interface.SmaliAnalysis;

/**
 * 
 * @author Juergen Graf <juergen.graf@gmail.com>
 *
 */
public class CallSmaliAnalysis {
	
	public static void main(final String[] args) throws DexAnalysisException, FileNotFoundException {
		System.out.println("========= SMALI ============");
		final DexProgram smaliProg = testSmali();
		DexAnalysisUtil.printProgram(smaliProg);
		System.out.print("Dumping graphs... ");
		DexAnalysisUtil.dumpGraphsTo(smaliProg, "./out/smali/");
		System.out.println("done.");
	}

	private static DexProgram testSmali() throws DexAnalysisException {
		final SmaliAnalysis.SmaliConfig conf = new SmaliAnalysis.SmaliConfig();
		System.out.println(conf);
		final SmaliAnalysis analysis = new SmaliAnalysis(conf);
		final SmaliAnalysis.SmaliInput input = new SmaliAnalysis.SmaliInput();
		input.addFile("./data/smali-src");
		final DexProgram prog = analysis.analyze(input);
		
		return prog;
	}
	
}
