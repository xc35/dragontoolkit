/*    */ package edu.drexel.cis.dragon.onlinedb;
/*    */ 
/*    */ public class BasicArticleParser
/*    */   implements ArticleParser
/*    */ {
/*    */   public Article parse(String line)
/*    */   {
/*    */     try
/*    */     {
/* 18 */       if ((line == null) || (line.trim().length() == 0))
/* 19 */         return null;
/* 20 */       String[] arrField = line.split("\t");
/* 21 */       Article cur = new BasicArticle();
/* 22 */       cur.setKey(arrField[0]);
/* 23 */       if (arrField.length >= 2)
/* 24 */         cur.setTitle(arrField[1]);
/* 25 */       if (arrField.length >= 3)
/* 26 */         cur.setMeta(arrField[2]);
/* 27 */       if (arrField.length >= 4)
/* 28 */         cur.setAbstract(arrField[3]);
/* 29 */       if (arrField.length >= 5)
/* 30 */         cur.setBody(arrField[4]);
/* 31 */       if (arrField.length >= 6)
/* 32 */         cur.setCategory(Integer.parseInt(arrField[5]));
/*    */       else
/* 34 */         cur.setCategory(-1);
/* 35 */       return cur;
/*    */     }
/*    */     catch (Exception e) {
/* 38 */       e.printStackTrace();
/* 39 */     }return null;
/*    */   }
/*    */ 
/*    */   public String assemble(Article article)
/*    */   {
/*    */     try
/*    */     {
/* 47 */       StringBuffer sb = new StringBuffer(10240);
/* 48 */       sb.append(article.getKey());
/* 49 */       sb.append('\t');
/* 50 */       if (article.getTitle() != null)
/* 51 */         sb.append(processText(article.getTitle()));
/* 52 */       sb.append('\t');
/* 53 */       if (article.getMeta() != null)
/* 54 */         sb.append(processText(article.getMeta()));
/* 55 */       sb.append('\t');
/* 56 */       if (article.getAbstract() != null)
/* 57 */         sb.append(processText(article.getAbstract()));
/* 58 */       sb.append('\t');
/* 59 */       if (article.getBody() != null)
/* 60 */         sb.append(processText(article.getBody()));
/* 61 */       if (article.getCategory() >= 0) {
/* 62 */         sb.append('\t');
/* 63 */         sb.append(article.getCategory());
/*    */       }
/* 65 */       return sb.toString();
/*    */     }
/*    */     catch (Exception e) {
/* 68 */       e.printStackTrace();
/* 69 */     }return null;
/*    */   }
/*    */ 
/*    */   protected String processText(String text)
/*    */   {
/* 74 */     text = text.replaceAll("[\t\r\n]", " ");
/* 75 */     return text.replaceAll("\\s\\s+", " ");
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.BasicArticleParser
 * JD-Core Version:    0.6.2
 */