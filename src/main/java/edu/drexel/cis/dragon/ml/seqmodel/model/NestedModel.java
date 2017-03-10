/*     */ package edu.drexel.cis.dragon.ml.seqmodel.model;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*     */ import java.io.PrintStream;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class NestedModel extends AbstractModel
/*     */ {
/*     */   private int _numStates;
/*     */   private int _numEdges;
/*     */   private int[] nodeOffsets;
/*     */   private ModelGraph[] inner;
/*     */   private ModelGraph outer;
/*     */   private int[] startStates;
/*     */   private int[] endStates;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/*  26 */       System.out.println(args[0]);
/*  27 */       System.out.println(args[1]);
/*  28 */       AbstractModel model = new NestedModel(Integer.parseInt(args[0]), args[1]);
/*  29 */       System.out.println(model.getStateNum());
/*  30 */       System.out.println(model.getEdgeNum());
/*  31 */       System.out.println(model.getStartStateNum());
/*  32 */       System.out.println(model.getEndStateNum());
/*  33 */       EdgeIterator edgeIter = model.getEdgeIterator();
/*  34 */       for (int edgeNum = 0; edgeIter.hasNext(); edgeNum++) {
/*  35 */         boolean edgeIsOuter = edgeIter.nextIsOuter();
/*  36 */         Edge e = edgeIter.next();
/*  37 */         System.out.println(e.getStart() + "(" + model.getLabel(e.getStart()) + ")" + " -> " + e.getEnd() + ":" + edgeIsOuter + ";");
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  41 */       System.out.println(e.getMessage());
/*  42 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public NestedModel(int labelNum, String specs) throws Exception {
/*  47 */     super(labelNum, "Nested");
/*     */ 
/*  54 */     this.nodeOffsets = new int[this.numLabels];
/*  55 */     this.inner = new AbstractModel[this.numLabels];
/*     */ 
/*  57 */     StringTokenizer start = new StringTokenizer(specs, ",");
/*  58 */     this.outer = getNewBaseModel(this.numLabels, start.nextToken());
/*  59 */     String commonStruct = null;
/*  60 */     for (int i = 0; i < this.numLabels; i++) {
/*  61 */       String thisStruct = commonStruct;
/*  62 */       if (thisStruct == null) {
/*  63 */         thisStruct = start.nextToken();
/*  64 */         if (thisStruct.endsWith("*")) {
/*  65 */           thisStruct = thisStruct.substring(0, thisStruct.length() - 1);
/*  66 */           commonStruct = thisStruct;
/*     */         }
/*     */       }
/*  69 */       this.inner[i] = new GenericModel(thisStruct, i);
/*     */     }
/*     */ 
/*  72 */     this._numEdges = 0;
/*  73 */     this._numStates = 0;
/*  74 */     for (int l = 0; l < this.numLabels; l++) {
/*  75 */       this.nodeOffsets[l] += this._numStates;
/*  76 */       this._numStates += this.inner[l].getStateNum();
/*  77 */       this._numEdges += this.inner[l].getEdgeNum();
/*     */     }
/*     */ 
/*  80 */     EdgeIterator outerIter = this.outer.getEdgeIterator();
/*  81 */     while (outerIter.hasNext()) {
/*  82 */       Edge e = outerIter.next();
/*  83 */       this._numEdges += this.inner[e.getEnd()].getStartStateNum() * this.inner[e.getStart()].getEndStateNum();
/*     */     }
/*     */      int i;
/*  86 */     int numStart = 0;
/*  87 */     for (i = 0; i < this.outer.getStartStateNum(); i++) {
/*  88 */       numStart += this.inner[this.outer.getStartState(i)].getStartStateNum();
/*     */     }
/*  90 */     this.startStates = new int[numStart];
/*  91 */     int index = 0;
/*  92 */     for (i = 0; i < this.outer.getStartStateNum(); i++) {
/*  93 */       for (int j = 0; j < this.inner[this.outer.getStartState(i)].getStartStateNum(); j++) {
/*  94 */         this.startStates[(index++)] = (this.inner[this.outer.getStartState(i)].getStartState(j) + this.nodeOffsets[this.outer.getStartState(i)]);
/*     */       }
/*     */     }
/*     */ 
/*  98 */     int numEnd = 0;
/*  99 */     for (i = 0; i < this.outer.getEndStateNum(); i++) {
/* 100 */       numEnd += this.inner[this.outer.getEndState(i)].getEndStateNum();
/*     */     }
/* 102 */     this.endStates = new int[numEnd];
/* 103 */     index = 0;
/* 104 */     for (i = 0; i < this.outer.getEndStateNum(); i++)
/* 105 */       for (int j = 0; j < this.inner[this.outer.getEndState(i)].getEndStateNum(); j++)
/* 106 */         this.endStates[(index++)] = (this.inner[this.outer.getEndState(i)].getEndState(j) + this.nodeOffsets[this.outer.getEndState(i)]);
/*     */   }
/*     */ 
/*     */   public EdgeIterator getEdgeIterator()
/*     */   {
/* 112 */     return new NestedEdgeIterator(this);
/*     */   }
/*     */ 
/*     */   public EdgeIterator innerEdgeIterator() {
/* 116 */     return new NestedEdgeIterator(this, false);
/*     */   }
/*     */ 
/*     */   public int getStateNum() {
/* 120 */     return this._numStates;
/*     */   }
/*     */ 
/*     */   public int getEdgeNum() {
/* 124 */     return this._numEdges;
/*     */   }
/*     */ 
/*     */   public int getLabel(int stateNum)
/*     */   {
/* 129 */     for (int i = 0; i < this.nodeOffsets.length; i++) {
/* 130 */       if (stateNum < this.nodeOffsets[i])
/* 131 */         return i - 1;
/*     */     }
/* 133 */     return this.nodeOffsets.length - 1;
/*     */   }
/*     */ 
/*     */   public int getStartStateNum() {
/* 137 */     return this.startStates.length;
/*     */   }
/*     */ 
/*     */   public int getEndStateNum() {
/* 141 */     return this.endStates.length;
/*     */   }
/*     */ 
/*     */   public int getStartState(int i) {
/* 145 */     return i < getStartStateNum() ? this.startStates[i] : -1;
/*     */   }
/*     */ 
/*     */   public int getEndState(int i) {
/* 149 */     return i < getEndStateNum() ? this.endStates[i] : -1;
/*     */   }
/*     */ 
/*     */   public boolean isEndState(int i)
/*     */   {
/* 154 */     for (int k = 0; k < this.endStates.length; k++)
/* 155 */       if (this.endStates[k] == i)
/* 156 */         return true;
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isStartState(int i)
/*     */   {
/* 162 */     for (int k = 0; k < this.startStates.length; k++)
/* 163 */       if (this.startStates[k] == i)
/* 164 */         return true;
/* 165 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean mapStateToLabel(DataSequence dataSeq)
/*     */   {
/* 171 */     int dataLen = dataSeq.length();
/* 172 */     if (dataLen == 0) {
/* 173 */       return true;
/*     */     }
/* 175 */     int segStart = 0; for (int segEnd = 0; segStart < dataLen; segStart = segEnd + 1) {
/* 176 */       for (segEnd = segStart; segEnd < dataLen; segEnd++) {
/* 177 */         if (getLabel(dataSeq.getLabel(segStart)) != getLabel(dataSeq.getLabel(segEnd))) {
/* 178 */           segEnd--;
/* 179 */           System.out.println("WARNING: label ending in a state not marked as a End-state");
/*     */         }
/*     */         else {
/* 182 */           if (isEndState(dataSeq.getLabel(segEnd)))
/*     */             break;
/*     */         }
/*     */       }
/* 186 */       if (segEnd == dataLen) {
/* 187 */         System.out.println("WARNING: End state not found until the last position");
/* 188 */         System.out.println(dataSeq);
/* 189 */         segEnd = dataLen - 1;
/*     */       }
/* 191 */       dataSeq.setSegment(segStart, segEnd, getLabel(dataSeq.getLabel(segStart)));
/*     */     }
/* 193 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean mapLabelToState(DataSequence data)
/*     */   {
/* 200 */     if (data.length() == 0)
/* 201 */       return true;
/* 202 */     for (int lstart = 0; lstart < data.length(); ) {
/* 203 */       int lend = data.getSegmentEnd(lstart) + 1;
/* 204 */       if (lend == 0)
/* 205 */         return false;
/* 206 */       int label = data.getLabel(lstart);
/* 207 */       this.inner[label].mapLabelToState(data, lend - lstart, lstart);
/* 208 */       for (int k = lstart; k < lend; k++) {
/* 209 */         data.setLabel(k, this.nodeOffsets[label] + data.getLabel(k));
/*     */       }
/* 211 */       lstart = lend;
/*     */     }
/* 213 */     return true;
/*     */   }
/*     */ 
/*     */   private ModelGraph getNewBaseModel(int numLabels, String modelSpecs) throws Exception
/*     */   {
/* 218 */     if ((modelSpecs.equalsIgnoreCase("naive")) || (modelSpecs.equalsIgnoreCase("semi-markov")))
/* 219 */       return new CompleteModel(numLabels);
/* 220 */     if (modelSpecs.equalsIgnoreCase("noEdge")) {
/* 221 */       return new NoEdgeModel(numLabels);
/*     */     }
/* 223 */     throw new Exception("Base model can be one of {naive, noEdge, semi-Markov}"); } 
/*     */   private class NestedEdgeIterator implements EdgeIterator { private NestedModel model;
/*     */     private int label;
/*     */     private Edge edge;
/*     */     private EdgeIterator[] edgeIter;
/*     */     private EdgeIterator outerEdgeIter;
/*     */     private Edge outerEdge;
/*     */     private boolean outerEdgesSent;
/*     */     private int index1;
/*     */     private int index2;
/*     */     private boolean sendOuter;
/*     */ 
/* 237 */     public NestedEdgeIterator(NestedModel m) { this(m, true); }
/*     */ 
/*     */     public NestedEdgeIterator(NestedModel m, boolean sendOuter)
/*     */     {
/* 241 */       this.model = m;
/* 242 */       this.edge = new Edge();
/* 243 */       this.edgeIter = new EdgeIterator[this.model.numLabels];
/* 244 */       for (int l = 0; l < this.model.numLabels; l++) {
/* 245 */         this.edgeIter[l] = this.model.inner[l].getEdgeIterator();
/*     */       }
/* 247 */       this.outerEdgeIter = this.model.outer.getEdgeIterator();
/* 248 */       this.sendOuter = sendOuter;
/* 249 */       start();
/*     */     }
/*     */ 
/*     */     public void start() {
/* 253 */       this.label = 0;
/* 254 */       for (int l = 0; l < this.model.numLabels; l++) {
/* 255 */         this.edgeIter[l].start();
/*     */       }
/* 257 */       this.outerEdgeIter.start();
/* 258 */       this.outerEdge = this.outerEdgeIter.next();
/*     */ 
/* 261 */       if ((this.outerEdge == null) || (!this.sendOuter))
/* 262 */         this.outerEdgesSent = true;
/*     */       else
/* 264 */         this.outerEdgesSent = false;
/* 265 */       this.index1 = (this.index2 = 0);
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 269 */       return (this.label < this.model.numLabels) || (!this.outerEdgesSent);
/*     */     }
/*     */ 
/*     */     public Edge nextOuterEdge() {
/* 273 */       this.edge.setStart(this.model.inner[this.outerEdge.getStart()].getEndState(this.index1) + this.model.nodeOffsets[this.outerEdge.getStart()]);
/* 274 */       this.edge.setEnd(this.model.inner[this.outerEdge.getEnd()].getStartState(this.index2) + this.model.nodeOffsets[this.outerEdge.getEnd()]);
/* 275 */       this.index2 += 1;
/* 276 */       if (this.index2 == this.model.inner[this.outerEdge.getEnd()].getStartStateNum()) {
/* 277 */         this.index2 = 0;
/* 278 */         this.index1 += 1;
/* 279 */         if (this.index1 == this.model.inner[this.outerEdge.getStart()].getEndStateNum()) {
/* 280 */           if (this.outerEdgeIter.hasNext()) {
/* 281 */             this.outerEdge = this.outerEdgeIter.next();
/* 282 */             this.index1 = (this.index2 = 0);
/*     */           } else {
/* 284 */             this.outerEdgesSent = true;
/*     */           }
/*     */         }
/*     */       }
/* 288 */       return this.edge;
/*     */     }
/*     */ 
/*     */     public Edge nextInnerEdge() {
/* 292 */       Edge edgeToRet = this.edgeIter[this.label].next();
/* 293 */       this.edge.setStart(edgeToRet.getStart());
/* 294 */       this.edge.setEnd(edgeToRet.getEnd());
/* 295 */       this.edge.setStart(this.edge.getStart() + this.model.nodeOffsets[this.label]);
/* 296 */       this.edge.setEnd(this.edge.getEnd() + this.model.nodeOffsets[this.label]);
/* 297 */       if (!this.edgeIter[this.label].hasNext())
/* 298 */         this.label += 1;
/* 299 */       return this.edge;
/*     */     }
/*     */ 
/*     */     public Edge next() {
/* 303 */       if (!nextIsOuter()) {
/* 304 */         return nextInnerEdge();
/*     */       }
/* 306 */       return nextOuterEdge();
/*     */     }
/*     */ 
/*     */     public boolean nextIsOuter()
/*     */     {
/* 311 */       return this.label >= this.model.numLabels;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.model.NestedModel
 * JD-Core Version:    0.6.2
 */