/*    */ package edu.drexel.cis.dragon.onlinedb.pubmed;
/*    */ 
/*    */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*    */ 
/*    */ public class PubMedArticle extends BasicArticle
/*    */ {
/*    */   private String[] arrMeSH;
/*    */ 
/*    */   public PubMedArticle()
/*    */   {
/* 18 */     this.arrMeSH = null;
/*    */   }
/*    */ 
/*    */   public PubMedArticle(String rawText) {
/* 22 */     this.arrMeSH = null;
/* 23 */     parseDef(rawText);
/*    */   }
/*    */ 
/*    */   private boolean parseDef(String content)
/*    */   {
/* 30 */     int start = content.indexOf("pmid") + 4;
/* 31 */     int end = content.indexOf(",", start);
/* 32 */     this.key = content.substring(start, end).trim();
/*    */ 
/* 35 */     start = end;
/* 36 */     start = content.indexOf("title {");
/* 37 */     if (start >= 0) {
/* 38 */       start = content.indexOf('"', start) + 1;
/* 39 */       end = content.indexOf("\"\n", start);
/* 40 */       this.title = content.substring(start, end).replace('\n', ' ');
/*    */     }
/*    */ 
/* 44 */     start = end;
/* 45 */     start = content.indexOf("abstract \"");
/* 46 */     if (start >= 0) {
/* 47 */       start += 10;
/* 48 */       end = content.indexOf("\",\n    ", start);
/* 49 */       this.abt = content.substring(start, end);
/*    */     }
/*    */ 
/* 53 */     start = end;
/* 54 */     start = content.indexOf("mesh {");
/* 55 */     if (start >= 0) {
/* 56 */       start += 6;
/* 57 */       end = content.indexOf("\n    },", start);
/* 58 */       String mesh = content.substring(start, end);
/* 59 */       start = mesh.indexOf("term \"");
/* 60 */       while (start >= 0)
/*    */       {
/* 62 */         start += 6;
/* 63 */         end = mesh.indexOf("\"", start);
/* 64 */         if (this.meta == null)
/* 65 */           this.meta = mesh.substring(start, end);
/*    */         else
/* 67 */           this.meta = (this.meta + "," + mesh.substring(start, end));
/* 68 */         start = mesh.indexOf("term \"", end);
/*    */       }
/* 70 */       if (this.meta != null)
/* 71 */         this.arrMeSH = this.meta.split(",");
/*    */     }
/* 73 */     return true;
/*    */   }
/*    */ 
/*    */   public int getMeSHNum() {
/* 77 */     if (this.arrMeSH == null) {
/* 78 */       return 0;
/*    */     }
/* 80 */     return this.arrMeSH.length;
/*    */   }
/*    */ 
/*    */   public String getMainHeading(int index)
/*    */   {
/* 85 */     return this.arrMeSH[index].toString();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.pubmed.PubMedArticle
 * JD-Core Version:    0.6.2
 */