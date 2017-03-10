/*    */ package edu.drexel.cis.dragon.nlp.ontology.mesh;
/*    */ 
/*    */ public class MeshNode
/*    */   implements Comparable
/*    */ {
/*    */   private String path;
/*    */   private String name;
/*    */   private int freq;
/*    */   private int descendantNum;
/*    */   private double weight;
/*    */ 
/*    */   public MeshNode(String name, String path)
/*    */   {
/* 19 */     this.name = name;
/* 20 */     this.path = path;
/* 21 */     this.freq = 0;
/* 22 */     this.weight = -1.0D;
/* 23 */     this.descendantNum = -1;
/*    */   }
/*    */ 
/*    */   public MeshNode(String path) {
/* 27 */     this.name = null;
/* 28 */     this.path = path;
/* 29 */     this.freq = 0;
/* 30 */     this.weight = -1.0D;
/* 31 */     this.descendantNum = -1;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj)
/*    */   {
/* 37 */     String objValue = ((MeshNode)obj).getPath();
/* 38 */     return this.path.compareTo(objValue);
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 42 */     return this.name;
/*    */   }
/*    */ 
/*    */   public String getPath() {
/* 46 */     return this.path;
/*    */   }
/*    */ 
/*    */   public void setName(String name) {
/* 50 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public void setPath(String path) {
/* 54 */     this.path = path;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 58 */     return this.path;
/*    */   }
/*    */ 
/*    */   public void setDescendantNum(int num) {
/* 62 */     this.descendantNum = num;
/*    */   }
/*    */ 
/*    */   public int getDescendantNum() {
/* 66 */     return this.descendantNum;
/*    */   }
/*    */ 
/*    */   public void addFrequency(int count) {
/* 70 */     this.freq += count;
/*    */   }
/*    */ 
/*    */   public void setFrequency(int count) {
/* 74 */     this.freq = count;
/*    */   }
/*    */ 
/*    */   public int getFrequency() {
/* 78 */     return this.freq;
/*    */   }
/*    */ 
/*    */   public void setWeight(double weight) {
/* 82 */     this.weight = weight;
/*    */   }
/*    */ 
/*    */   public double getWeight() {
/* 86 */     return this.weight;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.ontology.mesh.MeshNode
 * JD-Core Version:    0.6.2
 */