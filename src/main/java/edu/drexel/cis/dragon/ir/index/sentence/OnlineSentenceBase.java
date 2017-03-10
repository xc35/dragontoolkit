/*    */ package edu.drexel.cis.dragon.ir.index.sentence;
/*    */ 
/*    */ import java.util.TreeMap;
/*    */ 
/*    */ public class OnlineSentenceBase
/*    */ {
/*    */   private TreeMap map;
/*    */ 
/*    */   public OnlineSentenceBase()
/*    */   {
/* 17 */     this.map = new TreeMap();
/*    */   }
/*    */ 
/*    */   public boolean add(String sentence, String sentKey) {
/* 21 */     if (this.map.containsKey(sentKey))
/* 22 */       return false;
/* 23 */     this.map.put(sentKey, sentence);
/* 24 */     return true;
/*    */   }
/*    */ 
/*    */   public String get(String sentKey) {
/* 28 */     return (String)this.map.get(sentKey);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ir.index.sentence.OnlineSentenceBase
 * JD-Core Version:    0.6.2
 */