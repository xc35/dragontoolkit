/*    */ package edu.drexel.cis.dragon.ir.classification;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ 
/*    */ public class DocClass
/*    */ {
/*    */   private int classID;
/*    */   private String className;
/*    */   private SortedArray list;
/*    */ 
/*    */   public DocClass(int classID)
/*    */   {
/* 21 */     this.classID = classID;
/* 22 */     this.className = ("Class #" + classID);
/* 23 */     this.list = new SortedArray(new IndexComparator());
/*    */   }
/*    */ 
/*    */   public boolean addDoc(IRDoc doc) {
/* 27 */     return this.list.add(doc);
/*    */   }
/*    */ 
/*    */   public int getDocNum() {
/* 31 */     return this.list.size();
/*    */   }
/*    */ 
/*    */   public IRDoc getDoc(int index) {
/* 35 */     return (IRDoc)this.list.get(index);
/*    */   }
/*    */ 
/*    */   public boolean contains(IRDoc curDoc) {
/* 39 */     return this.list.contains(curDoc);
/*    */   }
/*    */ 
/*    */   public int getClassID() {
/* 43 */     return this.classID;
/*    */   }
/*    */ 
/*    */   public String getClassName() {
/* 47 */     return this.className;
/*    */   }
/*    */ 
/*    */   public void setClassName(String className) {
/* 51 */     this.className = className;
/*    */   }
/*    */ 
/*    */   public void removeAll() {
/* 55 */     this.list.clear();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.DocClass
 * JD-Core Version:    0.6.2
 */