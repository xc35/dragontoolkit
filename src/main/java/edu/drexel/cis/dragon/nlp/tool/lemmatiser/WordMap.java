/*    */ package edu.drexel.cis.dragon.nlp.tool.lemmatiser;
/*    */ 
/*    */ public class WordMap
/*    */   implements Comparable
/*    */ {
/*    */   String master;
/*    */   String slave;
/*    */ 
/*    */   public WordMap(String master, String slave)
/*    */   {
/* 16 */     this.master = master;
/* 17 */     this.slave = slave;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj) {
/* 21 */     return this.master.compareToIgnoreCase(((WordMap)obj).getMasterWord());
/*    */   }
/*    */ 
/*    */   public String getMasterWord() {
/* 25 */     return this.master;
/*    */   }
/*    */ 
/*    */   public String getSlaveWord() {
/* 29 */     return this.slave;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.lemmatiser.WordMap
 * JD-Core Version:    0.6.2
 */