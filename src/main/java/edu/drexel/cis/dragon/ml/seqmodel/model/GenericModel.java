/*     */ package edu.drexel.cis.dragon.ml.seqmodel.model;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ml.seqmodel.data.DataSequence;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class GenericModel extends AbstractModel
/*     */ {
/*     */   private int _numStates;
/*     */   private Edge[] _edges;
/*     */   private int[] edgeStart;
/*     */   private int[] startStates;
/*     */   private int[] endStates;
/*     */   private int myLabel;
/*     */ 
/*     */   public GenericModel(String spec, int thisLabel)
/*     */     throws Exception
/*     */   {
/*  24 */     super(1, spec);
/*  25 */     this.myLabel = thisLabel;
/*     */ 
/*  27 */     if ((spec.endsWith("-chain")) || (spec.endsWith("-long"))) {
/*  28 */       StringTokenizer tok = new StringTokenizer(spec, "-");
/*  29 */       int len = Integer.parseInt(tok.nextToken());
/*  30 */       this._numStates = len;
/*  31 */       this.startStates = new int[1];
/*  32 */       this.startStates[0] = 0;
/*  33 */       this.edgeStart = new int[this._numStates];
/*  34 */       if (len == 1) {
/*  35 */         this._edges = new Edge[1];
/*  36 */         this._edges[0] = new Edge(0, 0);
/*  37 */         this.endStates = new int[1];
/*  38 */         this.endStates[0] = 0;
/*  39 */         this.edgeStart[0] = 0;
/*     */       } else {
/*  41 */         this._edges = new Edge[2 * (len - 1)];
/*  42 */         for (int i = 0; i < len - 1; i++) {
/*  43 */           this._edges[(2 * i)] = new Edge(i, i + 1);
/*  44 */           this._edges[(2 * i + 1)] = new Edge(i, len - 1);
/*  45 */           this.edgeStart[i] = (2 * i);
/*     */         }
/*  47 */         this._edges[(this._edges.length - 1)] = new Edge(len - 2, len - 2);
/*  48 */         this.endStates = new int[2];
/*  49 */         this.endStates[0] = 0;
/*  50 */         this.endStates[1] = (len - 1);
/*     */       }
/*     */     }
/*  53 */     else if (spec.endsWith("parallel")) {
/*  54 */       StringTokenizer tok = new StringTokenizer(spec, "-");
/*  55 */       int len = Integer.parseInt(tok.nextToken());
/*  56 */       this._numStates = (len * (len + 1) / 2);
/*  57 */       this._edges = new Edge[len * (len - 1) / 2 + 1];
/*  58 */       this.edgeStart = new int[this._numStates];
/*  59 */       this.startStates = new int[len];
/*  60 */       this.endStates = new int[len];
/*  61 */       int node = 0;
/*  62 */       int e = 0;
/*  63 */       for (int i = 0; i < len; i++) {
/*  64 */         node += i;
/*  65 */         for (int j = 0; j < i; j++) {
/*  66 */           this._edges[(e++)] = new Edge(node + j, node + j + 1);
/*  67 */           this.edgeStart[(node + j)] = (e - 1);
/*     */         }
/*  69 */         this.startStates[i] = node;
/*  70 */         this.endStates[i] = (node + i);
/*     */       }
/*  72 */       node += len;
/*  73 */       this._edges[(e++)] = new Edge(this._numStates - 2, this._numStates - 2);
/*     */     }
/*  75 */     else if (spec.equals("boundary"))
/*     */     {
/*  82 */       this._numStates = 4;
/*  83 */       this._edges = new Edge[4];
/*  84 */       this._edges[0] = new Edge(1, 2);
/*  85 */       this._edges[1] = new Edge(1, 3);
/*  86 */       this._edges[2] = new Edge(2, 2);
/*  87 */       this._edges[3] = new Edge(2, 3);
/*  88 */       this.startStates = new int[2];
/*  89 */       this.startStates[0] = 0;
/*  90 */       this.startStates[1] = 1;
/*  91 */       this.endStates = new int[2];
/*  92 */       this.endStates[0] = 0;
/*  93 */       this.endStates[1] = 3;
/*  94 */       this.edgeStart = new int[this._numStates];
/*  95 */       this.edgeStart[0] = 4;
/*  96 */       this.edgeStart[1] = 0;
/*  97 */       this.edgeStart[2] = 2;
/*  98 */       this.edgeStart[3] = 4;
/*     */     }
/*     */     else {
/* 101 */       throw new Exception("Unknown graph type: " + spec);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getLabel(int s) {
/* 106 */     return this.myLabel == -1 ? s : this.myLabel;
/*     */   }
/*     */ 
/*     */   public GenericModel(int numNodes, int numEdges) throws Exception {
/* 110 */     super(numNodes, "");
/* 111 */     this._numStates = numNodes;
/* 112 */     this._edges = new Edge[numEdges];
/*     */   }
/*     */ 
/*     */   public int getStateNum() {
/* 116 */     return this._numStates;
/*     */   }
/*     */ 
/*     */   public int getEdgeNum() {
/* 120 */     return this._edges.length;
/*     */   }
/*     */ 
/*     */   public int getStartStateNum() {
/* 124 */     return this.startStates.length;
/*     */   }
/*     */ 
/*     */   public int getStartState(int i) {
/* 128 */     return i < getStartStateNum() ? this.startStates[i] : -1;
/*     */   }
/*     */ 
/*     */   public int getEndStateNum() {
/* 132 */     return this.endStates.length;
/*     */   }
/*     */ 
/*     */   public int getEndState(int i) {
/* 136 */     return i < getEndStateNum() ? this.endStates[i] : -1;
/*     */   }
/*     */ 
/*     */   public boolean isEndState(int i)
/*     */   {
/* 141 */     for (int k = 0; k < this.endStates.length; k++)
/* 142 */       if (this.endStates[k] == i)
/* 143 */         return true;
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isStartState(int i)
/*     */   {
/* 149 */     for (int k = 0; k < this.startStates.length; k++)
/* 150 */       if (this.startStates[k] == i)
/* 151 */         return true;
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */   public EdgeIterator getEdgeIterator() {
/* 156 */     return new GenericEdgeIterator(this._edges);
/*     */   }
/*     */ 
/*     */   public boolean mapLabelToState(DataSequence data, int len, int start) {
/* 160 */     for (int i = 0; i < getStartStateNum(); i++) {
/* 161 */       if (pathToEnd(data, getStartState(i), len - 1, start + 1)) {
/* 162 */         data.setLabel(start, getStartState(i));
/* 163 */         return true;
/*     */       }
/*     */     }
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean pathToEnd(DataSequence data, int s, int lenLeft, int start)
/*     */   {
/* 172 */     if (lenLeft == 0) {
/* 173 */       return isEndState(s);
/*     */     }
/* 175 */     for (int e = this.edgeStart[s]; (e < getEdgeNum()) && (this._edges[e].getStart() == s); e++) {
/* 176 */       int child = this._edges[e].getEnd();
/* 177 */       if (pathToEnd(data, child, lenLeft - 1, start + 1)) {
/* 178 */         data.setLabel(start, child);
/* 179 */         return true;
/*     */       }
/*     */     }
/* 182 */     return false;
/*     */   }
/*     */   private class GenericEdgeIterator implements EdgeIterator {
/*     */     private int edgeNum;
/*     */     private Edge[] edges;
/*     */ 
/*     */     public GenericEdgeIterator(Edge[] e) {
/* 190 */       this.edges = e;
/* 191 */       start();
/*     */     }
/*     */ 
/*     */     public void start() {
/* 195 */       this.edgeNum = 0;
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 199 */       return this.edgeNum < this.edges.length;
/*     */     }
/*     */ 
/*     */     public Edge next() {
/* 203 */       this.edgeNum += 1;
/* 204 */       return this.edges[(this.edgeNum - 1)];
/*     */     }
/*     */ 
/*     */     public boolean nextIsOuter() {
/* 208 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.model.GenericModel
 * JD-Core Version:    0.6.2
 */