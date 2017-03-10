/*     */ package edu.drexel.cis.dragon.ir.clustering;
/*     */ 
/*     */ public class ClusteringEva
/*     */ {
/*     */   private double entropyScore;
/*     */   private double fScore;
/*     */   private double purityScore;
/*     */   private double nmiScore;
/*     */   private double geoNmiScore;
/*     */   private double miScore;
/*     */   private DocClusterSet human;
/*     */   private DocClusterSet machine;
/*     */   private double[][] matchMatrix;
/*     */   private double[][] recallMatrix;
/*     */   private double[][] precMatrix;
/*     */   private double docSum;
/*     */   private int clusterNum;
/*     */   private int classNum;
/*     */ 
/*     */   public boolean evaluate(DocClusterSet machine, DocClusterSet human)
/*     */   {
/*  31 */     this.human = preprocessDocClusterSet(human);
/*  32 */     this.machine = preprocessDocClusterSet(machine);
/*  33 */     stat();
/*  34 */     this.matchMatrix = compClassMatch();
/*  35 */     compPrecRecall();
/*  36 */     compEntropy();
/*  37 */     compFScore();
/*  38 */     compPurityScore();
/*  39 */     compNMI();
/*  40 */     return true;
/*     */   }
/*     */ 
/*     */   public double[][] getMatchMatrix() {
/*  44 */     return this.matchMatrix;
/*     */   }
/*     */ 
/*     */   public double getMI() {
/*  48 */     return this.miScore;
/*     */   }
/*     */ 
/*     */   public double getEntropy() {
/*  52 */     return this.entropyScore;
/*     */   }
/*     */ 
/*     */   public double getFScore() {
/*  56 */     return this.fScore;
/*     */   }
/*     */ 
/*     */   public double getPurity() {
/*  60 */     return this.purityScore;
/*     */   }
/*     */ 
/*     */   public double getGeometryNMI() {
/*  64 */     return this.geoNmiScore;
/*     */   }
/*     */ 
/*     */   public double getNMI() {
/*  68 */     return this.nmiScore;
/*     */   }
/*     */ 
/*     */   private void stat()
/*     */   {
/*  74 */     int docSum = 0;
/*  75 */     for (int i = 0; i < this.machine.getClusterNum(); i++) {
/*  76 */       docSum += this.machine.getDocCluster(i).getDocNum();
/*     */     }
/*  78 */     this.docSum = docSum;
/*  79 */     this.clusterNum = this.machine.getClusterNum();
/*  80 */     this.classNum = this.machine.getClusterNum();
/*     */   }
/*     */ 
/*     */   private void compFScore()
/*     */   {
/*  87 */     double max = 0.0D;
/*  88 */     double sum = 0.0D;
/*  89 */     for (int i = 0; i < this.recallMatrix.length; i++) {
/*  90 */       max = 0.0D;
/*  91 */       for (int j = 0; j < this.recallMatrix[0].length; j++) {
/*  92 */         if (max < compFScore(i, j))
/*  93 */           max = compFScore(i, j);
/*     */       }
/*  95 */       sum += this.human.getDocCluster(i).getDocNum() / this.docSum * max;
/*     */     }
/*  97 */     this.fScore = sum;
/*     */   }
/*     */ 
/*     */   private double compFScore(int i, int j)
/*     */   {
/* 102 */     if ((this.recallMatrix[i][j] == 0.0D) && (this.recallMatrix[i][j] == 0.0D))
/* 103 */       return 0.0D;
/* 104 */     return 2.0D * this.recallMatrix[i][j] * this.precMatrix[i][j] / (this.precMatrix[i][j] + this.recallMatrix[i][j]);
/*     */   }
/*     */ 
/*     */   private void compPurityScore()
/*     */   {
/* 110 */     double sum = 0.0D;
/* 111 */     for (int i = 0; i < this.machine.getClusterNum(); i++) {
/* 112 */       sum += compPurityScore(this.machine.getDocCluster(i).getClusterID()) * (this.machine.getDocCluster(i).getDocNum() / this.docSum);
/*     */     }
/* 114 */     this.purityScore = sum;
/*     */   }
/*     */ 
/*     */   private double compPurityScore(int clusterID)
/*     */   {
/* 121 */     if (this.machine.getDocCluster(clusterID).getDocNum() == 0) {
/* 122 */       return 0.0D;
/*     */     }
/* 124 */     double max = 4.9E-324D;
/* 125 */     for (int i = 0; i < this.classNum; i++) {
/* 126 */       if (max < this.matchMatrix[clusterID][i]) {
/* 127 */         max = this.matchMatrix[clusterID][i];
/*     */       }
/*     */     }
/* 130 */     return max / this.machine.getDocCluster(clusterID).getDocNum();
/*     */   }
/*     */ 
/*     */   private void compEntropy()
/*     */   {
/* 136 */     double sum = 0.0D;
/*     */ 
/* 138 */     for (int i = 0; i < this.clusterNum; i++) {
/* 139 */       sum += this.machine.getDocCluster(i).getDocNum() * compEntropy(i) / this.docSum;
/*     */     }
/* 141 */     this.entropyScore = sum;
/*     */   }
/*     */ 
/*     */   private double compEntropy(int clusterID)
/*     */   {
/* 148 */     double sum = 0.0D;
/* 149 */     for (int j = 0; j < this.clusterNum; j++) {
/* 150 */       if (this.precMatrix[clusterID][j] == 0.0D)
/* 151 */         sum += -Math.log(1.0D / this.docSum) * (1.0D / this.docSum);
/*     */       else
/* 153 */         sum += -Math.log(this.precMatrix[clusterID][j]) * this.precMatrix[clusterID][j];
/*     */     }
/* 155 */     return sum;
/*     */   }
/*     */ 
/*     */   private double[][] compClassMatch()
/*     */   {
/* 163 */     double[][] match = new double[this.clusterNum][this.classNum];
/* 164 */     for (int i = 0; i < this.clusterNum; i++) {
/* 165 */       DocCluster dc1 = this.machine.getDocCluster(i);
/* 166 */       for (int j = 0; j < this.classNum; j++) {
/* 167 */         DocCluster dc2 = this.human.getDocCluster(j);
/* 168 */         for (int k = 0; k < dc2.getDocNum(); k++)
/* 169 */           if (dc1.containDoc(dc2.getDoc(k)))
/* 170 */             match[i][j] += 1.0D;
/*     */       }
/*     */     }
/* 173 */     return match;
/*     */   }
/*     */ 
/*     */   private void compPrecRecall()
/*     */   {
/* 179 */     this.precMatrix = new double[this.matchMatrix.length][this.matchMatrix[0].length];
/* 180 */     this.recallMatrix = new double[this.matchMatrix.length][this.matchMatrix[0].length];
/* 181 */     for (int i = 0; i < this.clusterNum; i++) {
/* 182 */       DocCluster dc1 = this.machine.getDocCluster(i);
/* 183 */       for (int j = 0; j < this.classNum; j++) {
/* 184 */         DocCluster dc2 = this.human.getDocCluster(j);
/* 185 */         this.precMatrix[i][j] = (this.matchMatrix[i][j] / dc1.getDocNum());
/* 186 */         this.recallMatrix[i][j] = (this.matchMatrix[i][j] / dc2.getDocNum());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void compNMI()
/*     */   {
/* 195 */     double sumJoint = 0.0D;
/* 196 */     double sumX = 0.0D;
/* 197 */     double sumY = 0.0D;
/*     */ 
/* 199 */     for (int i = 0; i < this.clusterNum; i++) {
/* 200 */       for (int j = 0; j < this.classNum; j++)
/*     */       {
/*     */         double jointProb;
/* 201 */         if (this.matchMatrix[i][j] == 0.0D)
/* 202 */           jointProb = 1.0D / this.docSum;
/*     */         else
/* 204 */           jointProb = this.matchMatrix[i][j] / this.docSum;
/* 205 */         double xProb = this.machine.getDocCluster(i).getDocNum() / this.docSum;
/* 206 */         double yProb = this.human.getDocCluster(j).getDocNum() / this.docSum;
/* 207 */         sumJoint += jointProb * Math.log(jointProb / (xProb * yProb));
/*     */       }
/*     */     }
/*     */ 
/* 211 */     for (int i = 0; i < this.clusterNum; i++) {
/* 212 */       double xProb = this.machine.getDocCluster(i).getDocNum() / this.docSum;
/* 213 */       sumX += xProb * Math.log(1.0D / xProb);
/*     */     }
/*     */ 
/* 216 */     for (int i = 0; i < this.clusterNum; i++) {
/* 217 */       double yProb = this.human.getDocCluster(i).getDocNum() / this.docSum;
/* 218 */       sumY += yProb * Math.log(1.0D / yProb);
/*     */     }
/*     */ 
/* 221 */     this.miScore = sumJoint;
/* 222 */     this.nmiScore = (2.0D * sumJoint / (Math.log(this.clusterNum) + Math.log(this.classNum)));
/* 223 */     this.geoNmiScore = (sumJoint / Math.sqrt(sumX * sumY));
/*     */   }
/*     */ 
/*     */   private DocClusterSet preprocessDocClusterSet(DocClusterSet oldSet)
/*     */   {
/* 230 */     int count = 0;
/* 231 */     for (int i = 0; i < oldSet.getClusterNum(); i++) {
/* 232 */       if (oldSet.getDocCluster(i).getDocNum() > 0)
/* 233 */         count++;
/*     */     }
/* 235 */     DocClusterSet newSet = new DocClusterSet(count);
/* 236 */     count = 0;
/* 237 */     for (int i = 0; i < oldSet.getClusterNum(); i++) {
/* 238 */       if (oldSet.getDocCluster(i).getDocNum() > 0) {
/* 239 */         newSet.setDocCluster(oldSet.getDocCluster(i), count);
/* 240 */         count++;
/*     */       }
/*     */     }
/* 243 */     return newSet;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.clustering.ClusteringEva
 * JD-Core Version:    0.6.2
 */