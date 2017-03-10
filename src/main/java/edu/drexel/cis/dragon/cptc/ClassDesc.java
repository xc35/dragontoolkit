/*    */ package edu.drexel.cis.dragon.cptc;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ 
/*    */ public class ClassDesc
/*    */   implements Comparable
/*    */ {
/*    */   private String categoryName;
/*    */   private SortedArray cptList;
/*    */ 
/*    */   public ClassDesc(String categoryName)
/*    */   {
/* 10 */     this.categoryName = categoryName;
/* 11 */     this.cptList = new SortedArray();
/*    */   }
/*    */ 
/*    */   public String getClassName() {
/* 15 */     return this.categoryName;
/*    */   }
/*    */ 
/*    */   public boolean addConcept(ConceptDesc cptDesc) {
/* 19 */     return this.cptList.add(cptDesc);
/*    */   }
/*    */ 
/*    */   public int getConceptNum() {
/* 23 */     return this.cptList.size();
/*    */   }
/*    */ 
/*    */   public ConceptDesc getConcept(int index) {
/* 27 */     return (ConceptDesc)this.cptList.get(index);
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj) {
/* 31 */     return this.categoryName.compareToIgnoreCase(((ClassDesc)obj).getClassName());
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 38 */     StringBuffer buf = new StringBuffer();
/* 39 */     buf.append("<class>\n");
/* 40 */     buf.append("<name>" + this.categoryName + "</name>\n");
/* 41 */     for (int i = 0; i < this.cptList.size(); i++)
/* 42 */       buf.append(getConcept(i).toString());
/* 43 */     buf.append("</class>\n");
/* 44 */     return buf.toString();
/*    */   }
/*    */ 
/*    */   public static ClassDesc load(String content)
/*    */   {
/* 51 */     int start = content.indexOf("<name>") + 6;
/* 52 */     int end = content.indexOf("</name>", start);
/* 53 */     ClassDesc curClass = new ClassDesc(content.substring(start, end));
/* 54 */     start = content.indexOf("<concept>", end);
/* 55 */     while (start > 0) {
/* 56 */       end = content.indexOf("</concept>", start) + 10;
/* 57 */       curClass.addConcept(ConceptDesc.load(content.substring(start, end)));
/* 58 */       start = content.indexOf("<concept>", end);
/*    */     }
/* 60 */     return curClass;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.cptc.ClassDesc
 * JD-Core Version:    0.6.2
 */