/******************************************************************************
 * Copyright (c) 2002 - 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *****************************************************************************/
package com.ibm.wala.cast.test;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;

import com.ibm.wala.cast.loader.AstMethod;
import com.ibm.wala.cast.tree.CAstSourcePositionMap.Position;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.core.tests.util.WalaTestCase;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.util.collections.NonNullSingletonIterator;

public abstract class TestCallGraphShape extends WalaTestCase {

  protected void verifySourceAssertions(CallGraph CG, Object[][] assertionData) {
    for(Object[] dat : assertionData) {
      String function = (String) dat[0];
      for(CGNode N : getNodes(CG, function)) {
        if (N.getMethod() instanceof AstMethod) {
          AstMethod M = (AstMethod) N.getMethod();
          SSAInstruction[] insts = N.getIR().getInstructions();
          insts: for(int i = 0; i < insts.length; i++) {
            SSAInstruction inst = insts[i];
            if (inst != null) {
              Position pos = M.getSourcePosition(i);
              if (pos != null) {
                String fileName = pos.getURL().toString();
                if (fileName.lastIndexOf('/') >= 0) {
                  fileName = fileName.substring(fileName.lastIndexOf('/')+1);
                }
                for(int j = 0; j < assertionData.length; j++) {
                  String file = (String) assertionData[j][1];
                  if (file.indexOf('/') >= 0) {
                    file = file.substring(file.lastIndexOf('/') + 1);
                  }
                  if (file.equalsIgnoreCase(fileName)) {
                    if (pos.getFirstLine() >= (Integer) assertionData[j][2]
                                           &&
                        pos.getLastLine() <= (Integer) assertionData[j][3]) {
                      System.err.println("found " + inst + " of " + M + " at expected position " + pos);
                      continue insts;
                    }
                  }
                }

                Assert.assertTrue("unexpected location " + pos + " for " + inst + " of " + M, false);
              }
            }
          }
        }
      }
    }
  }
  
  protected static class Name {
    String name;

    int instructionIndex;

    int vn;

    public Name(int vn, int instructionIndex, String name) {
      this.vn = vn;
      this.name = name;
      this.instructionIndex = instructionIndex;
    }
  }

  protected void verifyNameAssertions(CallGraph CG, Object[][] assertionData) {
    for (int i = 0; i < assertionData.length; i++) {
      Iterator NS = getNodes(CG, (String) assertionData[i][0]).iterator();
      while (NS.hasNext()) {
        CGNode N = (CGNode) NS.next();
        IR ir = N.getIR();
        Name[] names = (Name[]) assertionData[i][1];
        for (int j = 0; j < names.length; j++) {

          System.err.println("looking for " + names[j].name + ", " + names[j].vn + " in " + N);

          String[] localNames = ir.getLocalNames(names[j].instructionIndex, names[j].vn);

          boolean found = false;
          for (int k = 0; k < localNames.length; k++) {
            if (localNames[k].equals(names[j].name)) {
              found = true;
            }
          }

          Assert.assertTrue("no name " + names[j].name + " for " + N, found);
        }
      }
    }
  }

  protected void verifyGraphAssertions(CallGraph CG, Object[][] assertionData) {
    // System.err.println(CG);

    if (assertionData == null) {
      return;
    }
    
    for (int i = 0; i < assertionData.length; i++) {

      check_target: for (int j = 0; j < ((String[]) assertionData[i][1]).length; j++) {
        Iterator srcs = (assertionData[i][0] instanceof String) ? getNodes(CG, (String) assertionData[i][0]).iterator()
            : new NonNullSingletonIterator<CGNode>(CG.getFakeRootNode());

        Assert.assertTrue("cannot find " + assertionData[i][0], srcs.hasNext());

        while (srcs.hasNext()) {
          CGNode src = (CGNode) srcs.next();
          for (Iterator sites = src.iterateCallSites(); sites.hasNext();) {
            CallSiteReference sr = (CallSiteReference) sites.next();

            Iterator dsts = getNodes(CG, ((String[]) assertionData[i][1])[j]).iterator();
            Assert.assertTrue("cannot find " + ((String[]) assertionData[i][1])[j], dsts.hasNext());

            while (dsts.hasNext()) {
              CGNode dst = (CGNode) dsts.next();
              for (Iterator tos = CG.getPossibleTargets(src, sr).iterator(); tos.hasNext();) {
                if (tos.next().equals(dst)) {
                  System.err.println(("found expected " + src + " --> " + dst + " at " + sr));
                  continue check_target;
                }
              }
            }
          }
        }

        Assert.assertTrue("cannot find edge " + assertionData[i][0] + " ---> " + ((String[]) assertionData[i][1])[j], false);
      }
    }
  }


  /**
   * Verifies that none of the nodes that match the source description has an edge to any of the nodes that match the destination
   * description. (Used for checking for false connections in the callgraph)
   * 
   * @param CG
   * @param sourceDescription
   * @param destDescription
   */
  protected void verifyNoEdges(CallGraph CG, String sourceDescription, String destDescription) {
    Collection sources = getNodes(CG, sourceDescription);
    Collection dests = getNodes(CG, destDescription);
    for (Object source : sources) {
      for (Object dest : dests) {
        for (Iterator<CGNode> i = CG.getSuccNodes((CGNode) source); i.hasNext();) {
          if (i.next().equals(dest)) {
            Assert.fail("Found a link from " + source + " to " + dest);
          }
        }
      }
    }
  }
  
  protected static final Object ROOT = new Object() {
    @Override
    public String toString() {
      return "CallGraphRoot";
    }
  };

  protected abstract Collection<CGNode> getNodes(CallGraph CG, String functionIdentifier);

}
