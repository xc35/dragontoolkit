/*    */ package edu.drexel.cis.dragon.onlinedb;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.EnvVariable;
/*    */ import java.io.File;
/*    */ import java.io.RandomAccessFile;
/*    */ 
/*    */ public class BasicCollectionWriter
/*    */   implements CollectionWriter
/*    */ {
/*    */   protected RandomAccessFile rafCollection;
/*    */   protected BasicArticleIndex articleIndex;
/*    */   protected ArticleParser parser;
/*    */   protected String charSet;
/*    */ 
/*    */   public BasicCollectionWriter(String collectionFile, String indexFile, boolean append)
/*    */   {
/*    */     try
/*    */     {
/* 25 */       this.charSet = EnvVariable.getCharSet();
/* 26 */       this.parser = new BasicArticleParser();
/* 27 */       if (!append) {
/* 28 */         File file = new File(collectionFile);
/* 29 */         if (file.exists())
/* 30 */           file.delete();
/* 31 */         file = new File(indexFile);
/* 32 */         if (file.exists())
/* 33 */           file.delete();
/*    */       }
/* 35 */       this.rafCollection = new RandomAccessFile(collectionFile, "rw");
/* 36 */       if (this.rafCollection.length() > 0L) this.rafCollection.seek(this.rafCollection.length());
/* 37 */       this.articleIndex = new BasicArticleIndex(indexFile, true);
/*    */     }
/*    */     catch (Exception e) {
/* 40 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public ArticleParser getArticleParser() {
/* 45 */     return this.parser;
/*    */   }
/*    */ 
/*    */   public void setArticleParser(ArticleParser parser) {
/* 49 */     this.parser = parser;
/*    */   }
/*    */ 
/*    */   public boolean add(Article article)
/*    */   {
/*    */     try
/*    */     {
/* 56 */       if ((article == null) || (article.getKey() == null))
/* 57 */         return false;
/* 58 */       if (!this.articleIndex.add(article.getKey(), this.rafCollection.getFilePointer()))
/* 59 */         return false;
/* 60 */       String line = this.parser.assemble(article);
/*    */ 
/* 62 */       if (this.charSet != null)
/* 63 */         this.rafCollection.write(line.getBytes(this.charSet));
/*    */       else
/* 65 */         this.rafCollection.write(line.getBytes());
/* 66 */       if (line.charAt(line.length() - 1) != '\n')
/* 67 */         this.rafCollection.writeByte(10);
/* 68 */       return true;
/*    */     }
/*    */     catch (Exception e) {
/* 71 */       e.printStackTrace();
/* 72 */     }return false;
/*    */   }
/*    */ 
/*    */   public void close()
/*    */   {
/*    */     try {
/* 78 */       this.articleIndex.setCollectionFileSize(this.rafCollection.getFilePointer());
/* 79 */       this.rafCollection.close();
/* 80 */       this.articleIndex.close();
/*    */     }
/*    */     catch (Exception e) {
/* 83 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.BasicCollectionWriter
 * JD-Core Version:    0.6.2
 */