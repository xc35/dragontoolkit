/*    */ package edu.drexel.cis.dragon.onlinedb.searchengine;
/*    */ 
/*    */ public class WebLink
/*    */   implements Comparable
/*    */ {
/*    */   private String url;
/*    */   private String host;
/*    */   private String path;
/*    */   private String summary;
/*    */   private String title;
/*    */   private int port;
/*    */ 
/*    */   public WebLink(String url)
/*    */   {
/* 23 */     this.url = url;
/* 24 */     int start = url.indexOf("//");
/* 25 */     int end = url.indexOf("/", start + 2);
/* 26 */     this.host = url.substring(start + 2, end);
/*    */ 
/* 29 */     this.path = url.substring(end);
/*    */ 
/* 32 */     start = this.host.indexOf(":");
/* 33 */     if (start > 0) {
/* 34 */       this.port = Integer.parseInt(this.host.substring(start + 1));
/* 35 */       this.host = this.host.substring(0, start).trim();
/*    */     }
/*    */     else {
/* 38 */       this.port = 80;
/*    */     }
/*    */   }
/*    */ 
/* 42 */   public WebLink(String host, String path) { this(host, 80, path, null); }
/*    */ 
/*    */   public WebLink(String host, String path, String key)
/*    */   {
/* 46 */     this(host, 80, path, key);
/*    */   }
/*    */ 
/*    */   public WebLink(String host, int port, String path, String key) {
/* 50 */     this.host = host;
/* 51 */     this.path = path;
/* 52 */     this.port = port;
/*    */   }
/*    */ 
/*    */   public String getHost() {
/* 56 */     return this.host;
/*    */   }
/*    */ 
/*    */   public String getPath() {
/* 60 */     return this.path;
/*    */   }
/*    */ 
/*    */   public String getTitle() {
/* 64 */     return this.title;
/*    */   }
/*    */ 
/*    */   public void setTitle(String title) {
/* 68 */     this.title = title;
/*    */   }
/*    */ 
/*    */   public String getSummary() {
/* 72 */     return this.summary;
/*    */   }
/*    */ 
/*    */   public void setSummary(String summary) {
/* 76 */     this.summary = summary;
/*    */   }
/*    */ 
/*    */   public int getPort() {
/* 80 */     return this.port;
/*    */   }
/*    */ 
/*    */   public int compareTo(Object obj)
/*    */   {
/* 86 */     int result = this.host.compareToIgnoreCase(((WebLink)obj).getHost());
/* 87 */     if (result != 0) {
/* 88 */       return result;
/*    */     }
/* 90 */     return this.path.compareToIgnoreCase(((WebLink)obj).getPath());
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 94 */     return this.url;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.searchengine.WebLink
 * JD-Core Version:    0.6.2
 */