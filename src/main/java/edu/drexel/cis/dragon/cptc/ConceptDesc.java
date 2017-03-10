/*    */ package edu.drexel.cis.dragon.cptc;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.SimpleElement;
/*    */ import edu.drexel.cis.dragon.util.SortedArray;
/*    */ 
/*    */ public class ConceptDesc
/*    */   implements Comparable
/*    */ {
/*    */   private String concept;
/*    */   private boolean doneDownload;
/*    */   private SortedArray docList;
/*    */ 
/*    */   public ConceptDesc(String concept)
/*    */   {
/* 12 */     this.concept = concept.replaceAll("\t", "").trim();
/* 13 */     this.doneDownload = false;
/* 14 */     this.docList = new SortedArray();
/*    */   }
/*    */ 
/*    */   public String getConcept() {
/* 18 */     return this.concept;
/*    */   }
/*    */ 
/*    */   public boolean isDoneDownload() {
/* 22 */     return this.doneDownload;
/*    */   }
/*    */ 
/*    */   public void setDoneDownloadFlag(boolean flag) {
/* 26 */     this.doneDownload = flag;
/*    */   }
/*    */ 
/*    */   public boolean addArticle(String articleKey, int rank) {
/* 30 */     return this.docList.add(new SimpleElement(articleKey.toLowerCase(), rank));
/*    */   }
/*    */ 
/*    */   public int getArticleNum() {
/* 34 */     return this.docList.size();
/*    */   }
/*    */ 
/*    */   public String getArticleKey(int index) {
/* 38 */     return ((SimpleElement)this.docList.get(index)).getKey();
/*    */   }
/*    */ 
/*    */   public int getArticleRank(int index) {
/* 42 */     return ((SimpleElement)this.docList.get(index)).getIndex();
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj) {
/* 46 */     return this.concept.compareToIgnoreCase(((ConceptDesc)obj).getConcept());
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 54 */     StringBuffer buf = new StringBuffer();
/* 55 */     buf.append("<concept>\n");
/* 56 */     buf.append("<name>" + this.concept + "</name>\n");
/* 57 */     buf.append("<download>" + this.doneDownload + "</download>\n");
/* 58 */     for (int i = 0; i < this.docList.size(); i++) {
/* 59 */       SimpleElement element = (SimpleElement)this.docList.get(i);
/* 60 */       buf.append("<doc><url>" + element.getKey() + "</url><rank>" + element.getIndex() + "</rank></doc>\n");
/*    */     }
/* 62 */     buf.append("</concept>\n");
/* 63 */     return buf.toString();
/*    */   }
/*    */ 
/*    */   public static ConceptDesc load(String content)
/*    */   {
/* 72 */     int start = content.indexOf("<name>", 0) + 6;
/* 73 */     int end = content.indexOf("</name>", start);
/* 74 */     ConceptDesc concept = new ConceptDesc(content.substring(start, end));
/* 75 */     start = content.indexOf("<download>", end) + 10;
/* 76 */     end = content.indexOf("</download>", start);
/* 77 */     concept.setDoneDownloadFlag(Boolean.valueOf(content.substring(start, end)).booleanValue());
/* 78 */     start = content.indexOf("<doc>", end);
/* 79 */     while (start > 0) {
/* 80 */       start += 5;
/* 81 */       end = content.indexOf("</doc>", start);
/* 82 */       String line = content.substring(start, end);
/* 83 */       start = content.indexOf("<doc>", end);
/*    */ 
/* 85 */       int inStart = line.indexOf("<url>") + 5;
/* 86 */       int inEnd = line.indexOf("</url>", inStart);
/* 87 */       String url = line.substring(inStart, inEnd);
/* 88 */       inStart = line.indexOf("<rank>", inEnd + 6) + 6;
/* 89 */       inEnd = line.indexOf("</rank>", inStart);
/* 90 */       int rank = Integer.parseInt(line.substring(inStart, inEnd));
/* 91 */       concept.addArticle(url, rank);
/*    */     }
/* 93 */     return concept;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.cptc.ConceptDesc
 * JD-Core Version:    0.6.2
 */