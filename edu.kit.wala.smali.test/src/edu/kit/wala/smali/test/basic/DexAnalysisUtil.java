package edu.kit.wala.smali.test.basic;

import java.io.FileNotFoundException;

import org.jf.dexlib.Code.Analysis.graphs.GraphDumper;
import org.jf.dexlib.Interface.DexClass;
import org.jf.dexlib.Interface.DexMethod;
import org.jf.dexlib.Interface.DexProgram;

/**
 * 
 * @author Juergen Graf <juergen.graf@gmail.com>
 *
 */
public class DexAnalysisUtil {
	
	private DexAnalysisUtil() {}
	
	public static void printProgram(final DexProgram prog) {
		System.out.println(prog);
		
		for (final DexClass cls : prog.getClasses()) {
			System.out.println(cls);
			
			for (final DexMethod method : cls.getMethods()) {
				System.out.print(method);
				
				System.out.print(" - computing graphs");
				method.getControlFlowGraph(false);
				System.out.print(".");
				method.getControlFlowGraph(true);
				System.out.print(".");
				method.getDominanceFrontiers(false);
				System.out.print(".");
				method.getDominanceFrontiers(true);
				System.out.print(".");
				method.getDominators(false);
				System.out.print(".");
				method.getDominators(true);
				System.out.print(".");
				method.getDominationTree(false);
				System.out.print(".");
				method.getDominationTree(true);
				System.out.print(".");
				method.getControlDependenceGraph(false);
				System.out.print(".");
				method.getControlDependenceGraph(true);
				System.out.println("done.");
			}
		}
	}
	
	public static void dumpGraphsTo(final DexProgram dexProg, final String toDir) throws FileNotFoundException {
		final GraphDumper gDumpNoExc = new GraphDumper(toDir, "no-exc-", true, true, true, false);
		dexProg.dumpGraphs(gDumpNoExc);
		final GraphDumper gDumpWithExc = new GraphDumper(toDir, "with-exc-", true, true, true, true);
		dexProg.dumpGraphs(gDumpWithExc);
	}

}
