/*     */ package edu.drexel.cis.dragon.nlp.ontology.mesh;
/*     */ 
/*     */ import edu.drexel.cis.dragon.matrix.DoubleSuperSparseMatrix;
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.compare.AlphabetaComparator;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.SimilarityMetric;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class MeshSimilarity
/*     */   implements SimilarityMetric
/*     */ {
/*     */   public static final int MIN_LEN = 0;
/*     */   public static final int WU_PALMER = 1;
/*     */   public static final int LI = 2;
/*     */   public static final int LEACOCK = 3;
/*     */   public static final int JING = 4;
/*     */   public static final int LIN = 5;
/*     */   public static final int JIANG = 6;
/*     */   public static final int RESINK = 7;
/*     */   public static final int MAO = 8;
/*     */   public static final int FEATURE = 9;
/*     */   public static final int KNAPPE = 10;
/*     */   private MeshNodeList meshNodeList;
/*     */   private SortedArray maxTaxoLenList;
/*     */   private int similarityMode;
/*     */ 
/*     */   public MeshSimilarity(String hierFile, int similarityMode)
/*     */   {
/*  38 */     this.meshNodeList = new MeshNodeList(hierFile);
/*  39 */     if (similarityMode == 3)
/*  40 */       this.maxTaxoLenList = genMaxTaxoLenList();
/*     */     else
/*  42 */       this.maxTaxoLenList = null;
/*  43 */     this.similarityMode = similarityMode;
/*     */   }
/*     */ 
/*     */   public String getParent(String path) {
/*  47 */     if (path.indexOf(".") < 0)
/*  48 */       return null;
/*  49 */     return path.substring(0, path.lastIndexOf("."));
/*     */   }
/*     */ 
/*     */   public String[] getAncestors(String path)
/*     */   {
/*  57 */     if (path.indexOf(".") < 0) {
/*  58 */       String[] ancestors = new String[1];
/*  59 */       ancestors[0] = path;
/*  60 */       return ancestors;
/*     */     }
/*     */ 
/*  63 */     ArrayList parents = new ArrayList(5);
/*  64 */     while (path.indexOf(".") > 0) {
/*  65 */       path = path.substring(0, path.lastIndexOf("."));
/*  66 */       parents.add(path);
/*     */     }
/*  68 */     String[] ancestors = new String[parents.size()];
/*  69 */     for (int i = 0; i < ancestors.length; i++) {
/*  70 */       ancestors[i] = ((String)parents.get(i));
/*     */     }
/*  72 */     return ancestors;
/*     */   }
/*     */ 
/*     */   public String[] getSharedAncestors(String path1, String path2)
/*     */   {
/*  80 */     if (path1.equals(path2))
/*  81 */       return getAncestors(path1);
/*  82 */     String[] ancestors1 = getAncestors(path1);
/*  83 */     String[] ancestors2 = getAncestors(path2);
/*  84 */     ArrayList sharedParents = new ArrayList(5);
/*  85 */     for (int i = 0; i < ancestors1.length; i++) {
/*  86 */       for (int j = 0; j < ancestors2.length; j++) {
/*  87 */         if (ancestors1[i].equals(ancestors2[j]))
/*  88 */           sharedParents.add(ancestors1[i]);
/*     */       }
/*     */     }
/*  91 */     if (sharedParents.size() == 0) return null;
/*  92 */     String[] sharedAncestors = new String[sharedParents.size()];
/*  93 */     for (int i = 0; i < sharedAncestors.length; i++)
/*  94 */       sharedAncestors[i] = ((String)sharedParents.get(i));
/*  95 */     return sharedAncestors;
/*     */   }
/*     */ 
/*     */   public String[] getUnionAncestors(String path1, String path2)
/*     */   {
/* 103 */     if (path1.equals(path2))
/* 104 */       return getAncestors(path1);
/* 105 */     String[] ancestors1 = getAncestors(path1);
/* 106 */     String[] ancestors2 = getAncestors(path2);
/* 107 */     SortedArray unionParents = new SortedArray(new AlphabetaComparator());
/* 108 */     for (int i = 0; i < ancestors1.length; i++)
/* 109 */       unionParents.add(ancestors1[i]);
/* 110 */     for (int i = 0; i < ancestors2.length; i++) {
/* 111 */       unionParents.add(ancestors2[i]);
/*     */     }
/* 113 */     String[] unionAncestors = new String[unionParents.size()];
/* 114 */     for (int i = 0; i < unionAncestors.length; i++)
/* 115 */       unionAncestors[i] = ((String)unionParents.get(i));
/* 116 */     return unionAncestors;
/*     */   }
/*     */ 
/*     */   public String getRoot(String path) {
/* 120 */     if (path.indexOf(".") < 0)
/* 121 */       return path;
/* 122 */     return path.substring(0, path.indexOf("."));
/*     */   }
/*     */ 
/*     */   public boolean isRoot(String path) {
/* 126 */     if (path.indexOf(".") < 0)
/* 127 */       return true;
/* 128 */     return false;
/*     */   }
/*     */ 
/*     */   public String getCommonParent(String path1, String path2)
/*     */   {
/* 133 */     if (!getRoot(path1).equals(getRoot(path2))) {
/* 134 */       return null;
/*     */     }
/* 136 */     if (getDepth(path2) > getDepth(path1)) {
/* 137 */       String temp = path1;
/* 138 */       path1 = path2;
/* 139 */       path2 = temp;
/*     */     }
/* 141 */     while (path1 != null) {
/* 142 */       while (path2 != null) {
/* 143 */         if (path1.indexOf(path2) >= 0)
/* 144 */           return path2;
/* 145 */         if (path2.indexOf(path1) >= 0)
/* 146 */           return path1;
/* 147 */         path2 = getParent(path2);
/*     */       }
/* 149 */       path1 = getParent(path1);
/*     */     }
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */   public int getDescendantNum(String path)
/*     */   {
/* 157 */     MeshNode cur = this.meshNodeList.lookup(path);
/* 158 */     if (cur == null) {
/* 159 */       return 0;
/*     */     }
/* 161 */     return cur.getDescendantNum();
/*     */   }
/*     */ 
/*     */   public ArrayList getDescendant(String path)
/*     */   {
/* 169 */     int pos = this.meshNodeList.binarySearch(new MeshNode(path));
/* 170 */     ArrayList nodeList = new ArrayList();
/* 171 */     MeshNode cur = (MeshNode)this.meshNodeList.get(pos);
/* 172 */     while (pos <= this.meshNodeList.size() - 2) {
/* 173 */       pos++;
/* 174 */       MeshNode child = (MeshNode)this.meshNodeList.get(pos);
/* 175 */       if (!child.getPath().startsWith(cur.getPath()))
/*     */         break;
/* 177 */       nodeList.add(child);
/*     */     }
/* 179 */     return nodeList;
/*     */   }
/*     */ 
/*     */   public int getMaxPathLen(String path)
/*     */   {
/* 186 */     ArrayList nodeList = getDescendant(path);
/* 187 */     if (nodeList.size() == 0) {
/* 188 */       return getDepth(path);
/*     */     }
/* 190 */     int max = 0;
/* 191 */     for (int i = 0; i < nodeList.size(); i++) {
/* 192 */       int length = getDepth(((MeshNode)nodeList.get(i)).getPath());
/* 193 */       if (max < length)
/* 194 */         max = length;
/*     */     }
/* 196 */     return max;
/*     */   }
/*     */ 
/*     */   public int getMinLen(String path1, String path2)
/*     */   {
/* 202 */     String coParent = getCommonParent(path1, path2);
/* 203 */     if (coParent == null)
/* 204 */       return -1;
/* 205 */     int parLen = getDepth(coParent);
/* 206 */     return getDepth(path1) + getDepth(path2) - 2 * parLen;
/*     */   }
/*     */ 
/*     */   public int getMinLen(String path1, String path2, String coParent)
/*     */   {
/* 211 */     if (coParent == null)
/* 212 */       return -1;
/* 213 */     int parLen = getDepth(coParent);
/* 214 */     return getDepth(path1) + getDepth(path2) - 2 * parLen;
/*     */   }
/*     */ 
/*     */   public int getDepth(String path)
/*     */   {
/* 219 */     if (path.indexOf(".") < 0)
/* 220 */       return 1;
/* 221 */     int count = 0;
/* 222 */     while (getParent(path) != null) {
/* 223 */       path = getParent(path);
/* 224 */       count++;
/*     */     }
/* 226 */     count++;
/* 227 */     return count;
/*     */   }
/*     */ 
/*     */   private double getWuPalmerSimilarity(String path1, String path2)
/*     */   {
/* 233 */     String coParent = getCommonParent(path1, path2);
/* 234 */     if (coParent == null)
/* 235 */       return -1.0D;
/* 236 */     int parLen = getDepth(coParent);
/* 237 */     return 2 * parLen / (getDepth(path1) - parLen + getDepth(path2) - parLen + 2 * parLen);
/*     */   }
/*     */ 
/*     */   private double getLiSimilarity(String path1, String path2, double minPathScaler, double parentDepthScaler)
/*     */   {
/* 244 */     String coParent = getCommonParent(path1, path2);
/* 245 */     if (coParent == null)
/* 246 */       return -1.0D;
/* 247 */     int parentDepth = getDepth(coParent);
/* 248 */     int path1Depth = getDepth(path1);
/* 249 */     int path2Depth = getDepth(path2);
/* 250 */     double minLen = path1Depth + path2Depth - 2 * parentDepth;
/* 251 */     double dist = Math.exp(-minPathScaler * minLen);
/* 252 */     dist *= (Math.exp(parentDepthScaler * parentDepth) - Math.exp(-parentDepthScaler * parentDepth));
/* 253 */     dist /= (Math.exp(parentDepthScaler * parentDepth) + Math.exp(-parentDepthScaler * parentDepth));
/* 254 */     return dist;
/*     */   }
/*     */ 
/*     */   private double getFeatureSimilarity(String path1, String path2)
/*     */   {
/* 260 */     String[] sharedAncestors = getSharedAncestors(path1, path2);
/* 261 */     if (sharedAncestors == null) return 0.0D;
/* 262 */     double join = sharedAncestors.length;
/* 263 */     double union = getUnionAncestors(path1, path2).length;
/* 264 */     return (join + 1.0D) / (union + 1.0D);
/*     */   }
/*     */ 
/*     */   private double getKnappeSimilarity(String path1, String path2)
/*     */   {
/* 271 */     if (path1.equals(path2)) return 1.0D;
/* 272 */     String[] ancestors1 = getAncestors(path1);
/* 273 */     String[] ancestors2 = getAncestors(path2);
/* 274 */     String[] sharedAncestors = getSharedAncestors(path1, path2);
/* 275 */     if (sharedAncestors == null) return 0.0D;
/* 276 */     double join = sharedAncestors.length;
/* 277 */     double gen = ancestors1.length;
/* 278 */     double speci = ancestors2.length;
/* 279 */     return 0.5D * (join / gen) + 0.5D * (join / speci);
/*     */   }
/*     */ 
/*     */   private double getLeacockSimilarity(String path1, String path2)
/*     */   {
/* 287 */     String coParent = getCommonParent(path1, path2);
/* 288 */     if (coParent == null)
/* 289 */       return -1.0D;
/* 290 */     int minLen = getMinLen(path1, path2, coParent);
/* 291 */     String root = getRoot(coParent);
/* 292 */     int pos = this.maxTaxoLenList.binarySearch(root);
/* 293 */     Token token = (Token)this.maxTaxoLenList.get(pos);
/* 294 */     double value = (minLen + 1) / 2.0D / token.getWeight();
/* 295 */     return -Math.log(value);
/*     */   }
/*     */ 
/*     */   private double getJingSimilarity(String path1, String path2)
/*     */   {
/* 302 */     String coParent = getCommonParent(path1, path2);
/* 303 */     if (coParent == null) return -1.0D;
/* 304 */     double coParentDepth = getDepth(coParent);
/* 305 */     double dist = 1.0D / coParentDepth;
/* 306 */     dist *= 0.5D;
/* 307 */     dist *= ((getDepth(path1) - coParentDepth) / getMaxPathLen(path1) + (getDepth(path2) - coParentDepth) / getMaxPathLen(path2));
/* 308 */     return 1.0D - dist;
/*     */   }
/*     */ 
/*     */   private double getLinSimilarity(String path1, String path2)
/*     */   {
/* 313 */     if (path1.equals(path2))
/* 314 */       return 1.0D;
/* 315 */     String coParent = getCommonParent(path1, path2);
/* 316 */     if (coParent == null) return -1.0D;
/* 317 */     return 2.0D * this.meshNodeList.lookup(coParent).getWeight() / (this.meshNodeList.lookup(path1).getWeight() + this.meshNodeList.lookup(path2).getWeight());
/*     */   }
/*     */ 
/*     */   private double getJiangSimilarity(String path1, String path2)
/*     */   {
/* 323 */     String coParent = getCommonParent(path1, path2);
/* 324 */     if (coParent == null)
/* 325 */       return -1.0D;
/* 326 */     return -this.meshNodeList.lookup(path1).getWeight() - this.meshNodeList.lookup(path2).getWeight() + 
/* 327 */       2.0D * this.meshNodeList.lookup(coParent).getWeight();
/*     */   }
/*     */ 
/*     */   private double getResinkSimilarity(String path1, String path2)
/*     */   {
/* 334 */     if (path1.equals(path2)) {
/* 335 */       double dist = -this.meshNodeList.lookup(path1).getWeight();
/* 336 */       if (dist == 0.0D)
/* 337 */         dist = 0.11D;
/* 338 */       return dist;
/*     */     }
/* 340 */     String coParent = getCommonParent(path1, path2);
/* 341 */     if (coParent == null) return -1.0D;
/* 342 */     double dist = -this.meshNodeList.lookup(coParent).getWeight();
/* 343 */     if (dist == 0.0D)
/* 344 */       dist = 0.11D;
/* 345 */     return dist;
/*     */   }
/*     */ 
/*     */   private double getMaoSimilarity(String path1, String path2)
/*     */   {
/* 352 */     double len = getMinLen(path1, path2);
/* 353 */     if (len == 0.0D)
/* 354 */       return 1.0D;
/* 355 */     if (len == -1.0D)
/* 356 */       return -1.0D;
/* 357 */     int c1 = getDescendantNum(path1);
/* 358 */     int c2 = getDescendantNum(path2);
/* 359 */     return 0.9D / len / Math.log(2 + c1 + c2);
/*     */   }
/*     */ 
/*     */   public MeshNodeList getMeshNodeList() {
/* 363 */     return this.meshNodeList;
/*     */   }
/*     */ 
/*     */   private SortedArray genMaxTaxoLenList()
/*     */   {
/* 374 */     SortedArray maxTaxoLenList = new SortedArray(107, new AlphabetaComparator());
/* 375 */     String oldRoot = "A01";
/* 376 */     String maxPath = "A01";
/* 377 */     double max = 0.0D;
/* 378 */     for (int i = 0; i < this.meshNodeList.size(); i++) {
/* 379 */       MeshNode meshNode = (MeshNode)this.meshNodeList.get(i);
/* 380 */       String curRoot = getRoot(meshNode.getPath());
/* 381 */       if (!curRoot.equals(oldRoot)) {
/* 382 */         Token token = new Token(getRoot(maxPath));
/* 383 */         token.setWeight(max);
/* 384 */         maxTaxoLenList.add(token);
/* 385 */         max = 0.0D;
/* 386 */         oldRoot = curRoot;
/*     */       }
/* 388 */       int depth = getDepth(meshNode.getPath());
/* 389 */       if (max < depth) {
/* 390 */         max = depth;
/* 391 */         maxPath = meshNode.getPath();
/*     */       }
/* 393 */       oldRoot = curRoot;
/*     */     }
/* 395 */     maxTaxoLenList.add(new Token(getRoot(maxPath)));
/* 396 */     ((Token)maxTaxoLenList.get(maxTaxoLenList.size() - 1)).setWeight(max);
/*     */     Token token;
/* 397 */     for (int i = 0; i < maxTaxoLenList.size(); i++) {
/* 398 */       token = (Token)maxTaxoLenList.get(i);
/*     */     }
/* 400 */     return maxTaxoLenList;
/*     */   }
/*     */ 
/*     */   public void trainWeightByInformationContent(ArrayList trainings)
/*     */   {
/* 409 */     for (int i = 0; i < this.meshNodeList.size(); i++) {
/* 410 */       MeshNode node = (MeshNode)this.meshNodeList.get(i);
/* 411 */       node.setFrequency(0);
/*     */     }
/*     */ 
/* 414 */     for (int i = 0; i < trainings.size(); i++) {
/* 415 */       MeshNode node = (MeshNode)trainings.get(i);
/* 416 */       String cur = node.getPath();
/* 417 */       this.meshNodeList.lookup(cur).addFrequency(node.getFrequency());
/* 418 */       while (getParent(cur) != null) {
/* 419 */         cur = getParent(cur);
/* 420 */         this.meshNodeList.lookup(cur).addFrequency(node.getFrequency());
/*     */       }
/*     */     }
/*     */ 
/* 424 */     for (int i = 0; i < this.meshNodeList.size(); i++) {
/* 425 */       MeshNode node = (MeshNode)this.meshNodeList.get(i);
/* 426 */       if (node.getFrequency() == 0) {
/* 427 */         node.setWeight(0.0D);
/*     */       } else {
/* 429 */         double ic = node.getFrequency() / this.meshNodeList.lookup(getRoot(node.getPath())).getFrequency();
/* 430 */         node.setWeight(Math.log(ic));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void genSimilarityMatrix(String matrixFile, double threshold)
/*     */   {
/* 441 */     int start = 0;
/* 442 */     int end = 0;
/* 443 */     DoubleSuperSparseMatrix matrix = new DoubleSuperSparseMatrix(matrixFile, false, false);
/* 444 */     for (int i = 0; i < this.meshNodeList.size(); i++) {
/* 445 */       MeshNode meshNodeI = (MeshNode)this.meshNodeList.get(i);
/* 446 */       if (meshNodeI.getPath().indexOf(".") < 0) {
/* 447 */         start = i;
/* 448 */         end = i + meshNodeI.getDescendantNum();
/* 449 */         System.out.println(new Date() + " " + start + " " + end);
/*     */       }
/* 451 */       for (int j = start; j < end; j++) {
/* 452 */         MeshNode meshNodeJ = (MeshNode)this.meshNodeList.get(j);
/* 453 */         double dist = getSimilarity(meshNodeI.getPath(), meshNodeJ.getPath(), this.similarityMode);
/* 454 */         if (dist > threshold) {
/* 455 */           matrix.add(i, j, dist);
/* 456 */           matrix.add(j, i, dist);
/*     */         }
/*     */       }
/*     */     }
/* 460 */     matrix.finalizeData();
/*     */   }
/*     */ 
/*     */   private double getSimilarity(String path1, String path2, int mode) {
/* 464 */     if (mode == 0) {
/* 465 */       return getMinLen(path1, path2);
/*     */     }
/* 467 */     if (mode == 1) {
/* 468 */       return getWuPalmerSimilarity(path1, path2);
/*     */     }
/* 470 */     if (mode == 2) {
/* 471 */       return getLiSimilarity(path1, path2, 0.6D, 0.2D);
/*     */     }
/* 473 */     if (mode == 3) {
/* 474 */       return getLeacockSimilarity(path1, path2);
/*     */     }
/* 476 */     if (mode == 4) {
/* 477 */       return getJingSimilarity(path1, path2);
/*     */     }
/* 479 */     if (mode == 5) {
/* 480 */       return getLinSimilarity(path1, path2);
/*     */     }
/* 482 */     if (mode == 6) {
/* 483 */       return getJiangSimilarity(path1, path2);
/*     */     }
/* 485 */     if (mode == 7) {
/* 486 */       return getResinkSimilarity(path1, path2);
/*     */     }
/* 488 */     if (mode == 8) {
/* 489 */       return getMaoSimilarity(path1, path2);
/*     */     }
/* 491 */     if (mode == 9) {
/* 492 */       return getFeatureSimilarity(path1, path2);
/*     */     }
/* 494 */     if (mode == 10) {
/* 495 */       return getKnappeSimilarity(path1, path2);
/*     */     }
/* 497 */     return -1.0D;
/*     */   }
/*     */ 
/*     */   public double getSimilarity(String path1, String path2) {
/* 501 */     return getSimilarity(path1, path2, this.similarityMode);
/*     */   }
/*     */ 
/*     */   public double getSimilarity(Term a, Term b) {
/* 505 */     return getSimilarity(a.getCUI(), b.getCUI(), this.similarityMode);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.mesh.MeshSimilarity
 * JD-Core Version:    0.6.2
 */