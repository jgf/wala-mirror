package edu.kit.wala.smali.test.basic;

import org.jf.baksmali.Interface.BakSmaliAnalysis;
import org.jf.dexlib.Interface.DexAnalysis.DexAnalysisException;
import org.jf.dexlib.Interface.DexClass;
import org.jf.dexlib.Interface.DexMethod;
import org.jf.dexlib.Interface.DexProgram;

public class CallDexAnalysis {
	public static void main(String[] args) throws DexAnalysisException {
		final BakSmaliAnalysis.Config conf = new BakSmaliAnalysis.Config();
		System.out.println(conf);
		final BakSmaliAnalysis analysis = new BakSmaliAnalysis(conf);
		final DexProgram prog = analysis.analyze("data/JLex.dex");
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
}
