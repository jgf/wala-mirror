package com.ibm.wala.cast.js.ipa.modref;

import java.util.Collection;
import java.util.Iterator;

import com.ibm.wala.cast.ipa.callgraph.AstHeapModel;
import com.ibm.wala.cast.ipa.modref.AstModRef;
import com.ibm.wala.cast.js.ssa.JSInstructionVisitor;
import com.ibm.wala.cast.js.ssa.JavaScriptCheckReference;
import com.ibm.wala.cast.js.ssa.JavaScriptInstanceOf;
import com.ibm.wala.cast.js.ssa.JavaScriptInvoke;
import com.ibm.wala.cast.js.ssa.JavaScriptPropertyRead;
import com.ibm.wala.cast.js.ssa.JavaScriptPropertyWrite;
import com.ibm.wala.cast.js.ssa.JavaScriptTypeOfInstruction;
import com.ibm.wala.cast.js.ssa.JavaScriptWithRegion;
import com.ibm.wala.cast.js.ssa.PrototypeLookup;
import com.ibm.wala.cast.js.ssa.SetPrototype;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerAnalysis;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.ipa.modref.ExtendedHeapModel;

public class JavaScriptModRef extends AstModRef {

  protected static class JavaScriptRefVisitor extends AstRefVisitor implements JSInstructionVisitor {

    protected JavaScriptRefVisitor(CGNode n, Collection<PointerKey> result, PointerAnalysis<InstanceKey> pa, ExtendedHeapModel h) {
      super(n, result, pa, (AstHeapModel)h);
    }

    @Override
    public void visitJavaScriptInvoke(JavaScriptInvoke instruction) {
      // do nothing
    }

    @Override
    public void visitTypeOf(JavaScriptTypeOfInstruction instruction) {
      // do nothing
    }

    @Override
    public void visitJavaScriptPropertyRead(JavaScriptPropertyRead instruction) {
      PointerKey obj = h.getPointerKeyForLocal(n, instruction.getObjectRef());
      PointerKey prop = h.getPointerKeyForLocal(n, instruction.getMemberRef());
      for(InstanceKey o : pa.getPointsToSet(obj)) {
        for(InstanceKey p : pa.getPointsToSet(prop)) {
          for(Iterator<PointerKey> keys = h.getPointerKeysForReflectedFieldRead(o, p); keys.hasNext(); ) {
            PointerKey x = keys.next();
            assert x != null : instruction;
            result.add(x);
          }
        }
      }
    }

    @Override
    public void visitJavaScriptPropertyWrite(JavaScriptPropertyWrite instruction) {
      // do nothing
    }

    @Override
    public void visitJavaScriptInstanceOf(JavaScriptInstanceOf instruction) {
      // do nothing
    }

    @Override
    public void visitWithRegion(JavaScriptWithRegion instruction) {
      // do nothing
    }

    @Override
    public void visitCheckRef(JavaScriptCheckReference instruction) {
      // do nothing
    }

    @Override
    public void visitSetPrototype(SetPrototype instruction) {
      // do nothing
    }

    @Override
    public void visitPrototypeLookup(PrototypeLookup instruction) {
      // TODO Auto-generated method stub
      
    }

  }

  @Override
  protected RefVisitor makeRefVisitor(CGNode n, Collection<PointerKey> result, PointerAnalysis<InstanceKey> pa, ExtendedHeapModel h) {
    return new JavaScriptRefVisitor(n, result, pa, h);
  }

  protected static class JavaScriptModVisitor extends AstModVisitor implements JSInstructionVisitor {

    protected JavaScriptModVisitor(CGNode n, Collection<PointerKey> result, ExtendedHeapModel h, PointerAnalysis<InstanceKey> pa) {
      super(n, result, (AstHeapModel)h, pa);
    }

    @Override
    public void visitJavaScriptInvoke(JavaScriptInvoke instruction) {
      // do nothing
    }

    @Override
    public void visitTypeOf(JavaScriptTypeOfInstruction instruction) {
      // do nothing
    }

    @Override
    public void visitJavaScriptPropertyRead(JavaScriptPropertyRead instruction) {
      // do nothing
    }

    @Override
    public void visitJavaScriptPropertyWrite(JavaScriptPropertyWrite instruction) {
      PointerKey obj = h.getPointerKeyForLocal(n, instruction.getObjectRef());
      PointerKey prop = h.getPointerKeyForLocal(n, instruction.getMemberRef());
      for(InstanceKey o : pa.getPointsToSet(obj)) {
        for(InstanceKey p : pa.getPointsToSet(prop)) {
          for(Iterator<PointerKey> keys = h.getPointerKeysForReflectedFieldWrite(o, p); keys.hasNext(); ) {
            PointerKey x = keys.next();
            assert x != null : instruction;
            result.add(x);
          }
        }
      }
    }

    @Override
    public void visitJavaScriptInstanceOf(JavaScriptInstanceOf instruction) {
      // do nothing
    }

    @Override
    public void visitWithRegion(JavaScriptWithRegion instruction) {
      // do nothing
    }

    @Override
    public void visitCheckRef(JavaScriptCheckReference instruction) {
      // do nothing
    }

    @Override
    public void visitSetPrototype(SetPrototype instruction) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void visitPrototypeLookup(PrototypeLookup instruction) {
      // do nothing
    }

  }

  @Override
  protected ModVisitor makeModVisitor(CGNode n, Collection<PointerKey> result, PointerAnalysis<InstanceKey> pa, ExtendedHeapModel h, boolean ignoreAllocHeapDefs) {
    return new JavaScriptModVisitor(n, result, h, pa);
  }
}
