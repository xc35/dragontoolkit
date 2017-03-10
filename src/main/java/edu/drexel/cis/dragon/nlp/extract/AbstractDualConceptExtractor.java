/*    */ package edu.drexel.cis.dragon.nlp.extract;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.DocumentParser;
/*    */ import edu.drexel.cis.dragon.onlinedb.Article;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public abstract class AbstractDualConceptExtractor
/*    */   implements DualConceptExtractor
/*    */ {
/*    */   protected ArrayList firstConceptList;
/*    */   protected ArrayList secondConceptList;
/*    */   protected DocumentParser parser;
/*    */ 
/*    */   public AbstractDualConceptExtractor()
/*    */   {
/* 21 */     this.parser = new EngDocumentParser();
/*    */   }
/*    */ 
/*    */   public boolean extractFromDoc(Article article) {
/* 25 */     return extractFromDoc(getArticleContent(article));
/*    */   }
/*    */ 
/*    */   public boolean extractFromDoc(String doc) {
/* 29 */     return extractFromDoc(this.parser.parse(doc));
/*    */   }
/*    */ 
/*    */   public ArrayList getFirstConceptList() {
/* 33 */     return this.firstConceptList;
/*    */   }
/*    */ 
/*    */   public ArrayList getSecondConceptList() {
/* 37 */     return this.secondConceptList;
/*    */   }
/*    */ 
/*    */   public boolean isExtractionMerged() {
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean supportConceptName() {
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean supportConceptEntry() {
/* 49 */     return false;
/*    */   }
/*    */ 
/*    */   public void initDocExtraction() {
/* 53 */     this.firstConceptList = new ArrayList(100);
/* 54 */     this.secondConceptList = new ArrayList(100);
/*    */   }
/*    */ 
/*    */   protected String getArticleContent(Article article)
/*    */   {
/* 59 */     StringBuffer sb = new StringBuffer();
/*    */ 
/* 61 */     if ((article.getTitle() != null) && (article.getTitle().length() >= 5)) {
/* 62 */       if (sb.length() > 0) {
/* 63 */         sb.append("\n\n");
/*    */       }
/* 65 */       sb.append(article.getTitle());
/*    */     }
/* 67 */     if ((article.getMeta() != null) && (article.getMeta().length() >= 5)) {
/* 68 */       if (sb.length() > 0) {
/* 69 */         sb.append("\n\n");
/*    */       }
/* 71 */       sb.append(article.getMeta());
/*    */     }
/* 73 */     if ((article.getAbstract() != null) && (article.getAbstract().length() >= 5)) {
/* 74 */       if (sb.length() > 0) {
/* 75 */         sb.append("\n\n");
/*    */       }
/* 77 */       sb.append(article.getAbstract());
/*    */     }
/* 79 */     if ((article.getBody() != null) && (article.getBody().length() >= 5)) {
/* 80 */       if (sb.length() > 0) {
/* 81 */         sb.append("\n\n");
/*    */       }
/* 83 */       sb.append(article.getBody());
/*    */     }
/* 85 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   public DocumentParser getDocumentParser() {
/* 89 */     return this.parser;
/*    */   }
/*    */ 
/*    */   public void setDocumentParser(DocumentParser parser) {
/* 93 */     this.parser = parser;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.AbstractDualConceptExtractor
 * JD-Core Version:    0.6.2
 */