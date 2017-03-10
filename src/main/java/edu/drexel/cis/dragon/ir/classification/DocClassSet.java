/*    */ package edu.drexel.cis.dragon.ir.classification;
/*    */ 
/*    */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*    */ 
/*    */ public class DocClassSet
/*    */ {
/*    */   private int classNum;
/*    */   private DocClass[] arrClass;
/*    */ 
/*    */   public DocClassSet(int classNum)
/*    */   {
/* 20 */     this.classNum = classNum;
/* 21 */     this.arrClass = new DocClass[classNum];
/* 22 */     for (int i = 0; i < classNum; i++)
/* 23 */       this.arrClass[i] = new DocClass(i);
/*    */   }
/*    */ 
/*    */   public DocClass getDocClass(int classID) {
/* 27 */     return this.arrClass[classID];
/*    */   }
/*    */ 
/*    */   public int getClassNum() {
/* 31 */     return this.classNum;
/*    */   }
/*    */ 
/*    */   public boolean addDoc(int classID, IRDoc curDoc) {
/* 35 */     if ((classID >= this.classNum) || (classID < 0))
/* 36 */       return false;
/* 37 */     return this.arrClass[classID].addDoc(curDoc);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.classification.DocClassSet
 * JD-Core Version:    0.6.2
 */