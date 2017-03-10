/*     */ package edu.drexel.cis.dragon.onlinedb.trec;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.onlinedb.Article;
/*     */ import edu.drexel.cis.dragon.onlinedb.ArticleParser;
/*     */ import edu.drexel.cis.dragon.onlinedb.BasicArticle;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class SgmArticleParser
/*     */   implements ArticleParser
/*     */ {
/*     */   protected SortedArray tagList;
/*     */ 
/*     */   public String assemble(Article article)
/*     */   {
/*  20 */     return null;
/*     */   }
/*     */ 
/*     */   public Article parse(String content)
/*     */   {
/*  26 */     BasicArticle article = null;
/*     */     try {
/*  28 */       this.tagList = collectTagInformation(content);
/*  29 */       if ((this.tagList == null) || (this.tagList.size() == 0)) {
/*  30 */         return null;
/*     */       }
/*  32 */       article = new BasicArticle();
/*     */ 
/*  35 */       article.setKey(extractDocNo(content));
/*     */ 
/*  38 */       article.setTitle(extractTitle(content));
/*     */ 
/*  41 */       article.setAbstract(extractAbstract(content));
/*     */ 
/*  44 */       article.setMeta(extractMeta(content));
/*     */ 
/*  47 */       article.setLength(extractLength(content));
/*     */ 
/*  50 */       article.setDate(extractDate(content));
/*     */ 
/*  53 */       article.setBody(extractBody(content));
/*     */ 
/*  55 */       return article;
/*     */     }
/*     */     catch (Exception e) {
/*  58 */       e.printStackTrace();
/*  59 */       if (article.getKey() != null)
/*  60 */         return article;
/*     */     }
/*  62 */     return null;
/*     */   }
/*     */ 
/*     */   protected int extractLength(String rawText)
/*     */   {
/*  68 */     return 0;
/*     */   }
/*     */ 
/*     */   protected Date extractDate(String rawText)
/*     */   {
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */   protected String extractDocNo(String rawText)
/*     */   {
/*  78 */     Token tag = getDocNoTag();
/*  79 */     if (tag == null) {
/*  80 */       return null;
/*     */     }
/*  82 */     return getTagContent(rawText, tag, false).trim();
/*     */   }
/*     */ 
/*     */   protected Token getDocNoTag()
/*     */   {
/*  88 */     int pos = this.tagList.binarySearch(new Token("DOCNO"));
/*  89 */     if (pos < 0) {
/*  90 */       return null;
/*     */     }
/*  92 */     return (Token)this.tagList.get(pos);
/*     */   }
/*     */ 
/*     */   protected String extractTitle(String rawText)
/*     */   {
/* 101 */     Token tag = getTitleTag();
/* 102 */     if (tag == null) {
/* 103 */       return null;
/*     */     }
/* 105 */     StringBuffer out = new StringBuffer();
/* 106 */     getTagContent(rawText, tag.getName(), tag.getIndex(), out);
/*     */     int start;
/* 107 */     if ((tag.getName().equals("HL")) && ((start = out.indexOf("----")) >= 0))
/* 108 */       out.delete(start, out.length());
/* 109 */     if (out.length() >= 5) {
/* 110 */       if (".!;?".indexOf(out.charAt(out.length() - 1)) < 0)
/* 111 */         out.append('.');
/* 112 */       return out.toString();
/*     */     }
/*     */ 
/* 115 */     return null;
/*     */   }
/*     */ 
/*     */   protected Token getTitleTag()
/*     */   {
/* 121 */     int pos = this.tagList.binarySearch(new Token("HEAD"));
/* 122 */     if (pos < 0)
/* 123 */       pos = this.tagList.binarySearch(new Token("HEADLINE"));
/* 124 */     if (pos < 0)
/* 125 */       pos = this.tagList.binarySearch(new Token("HL"));
/* 126 */     if (pos < 0)
/* 127 */       pos = this.tagList.binarySearch(new Token("TITLE"));
/* 128 */     if (pos < 0)
/* 129 */       pos = this.tagList.binarySearch(new Token("TI"));
/* 130 */     if (pos < 0) {
/* 131 */       return null;
/*     */     }
/* 133 */     return (Token)this.tagList.get(pos);
/*     */   }
/*     */ 
/*     */   protected String extractAbstract(String rawText)
/*     */   {
/* 139 */     Token tag = getAbstractTag();
/* 140 */     if (tag == null) {
/* 141 */       return null;
/*     */     }
/* 143 */     StringBuffer out = new StringBuffer();
/* 144 */     getTagContent(rawText, tag.getName(), tag.getIndex(), out);
/* 145 */     if (out.length() >= 5) {
/* 146 */       if (".!;?".indexOf(out.charAt(out.length() - 1)) < 0)
/* 147 */         out.append('.');
/* 148 */       return out.toString();
/*     */     }
/*     */ 
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */   protected Token getAbstractTag()
/*     */   {
/* 157 */     int pos = this.tagList.binarySearch(new Token("LP"));
/* 158 */     if (pos < 0)
/* 159 */       pos = this.tagList.binarySearch(new Token("LEADPARA"));
/* 160 */     if (pos < 0) {
/* 161 */       return null;
/*     */     }
/* 163 */     return (Token)this.tagList.get(pos);
/*     */   }
/*     */ 
/*     */   protected String extractMeta(String rawText)
/*     */   {
/* 169 */     Token tag = getMetaTag();
/* 170 */     if (tag == null) {
/* 171 */       return null;
/*     */     }
/* 173 */     StringBuffer out = new StringBuffer();
/* 174 */     getTagContent(rawText, tag.getName(), tag.getIndex(), out);
/* 175 */     if (out.length() >= 1) {
/* 176 */       return out.toString();
/*     */     }
/* 178 */     return null;
/*     */   }
/*     */ 
/*     */   protected Token getMetaTag()
/*     */   {
/* 184 */     int pos = this.tagList.binarySearch(new Token("DESCRIPT"));
/* 185 */     if (pos < 0)
/* 186 */       pos = this.tagList.binarySearch(new Token("IN"));
/* 187 */     if (pos < 0) {
/* 188 */       return null;
/*     */     }
/* 190 */     return (Token)this.tagList.get(pos);
/*     */   }
/*     */ 
/*     */   protected String extractBody(String rawText)
/*     */   {
/* 198 */     Token tag = getBodyTag();
/* 199 */     if (tag == null) {
/* 200 */       return null;
/*     */     }
/* 202 */     StringBuffer out = new StringBuffer();
/* 203 */     int start = tag.getIndex();
/* 204 */     int end = getTagContent(rawText, tag.getName(), start, out);
/* 205 */     while (end > start) {
/* 206 */       start = end;
/* 207 */       end = getTagContent(rawText, tag.getName(), start, out);
/*     */     }
/* 209 */     if (out.length() > 40) {
/* 210 */       return out.toString();
/*     */     }
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */   protected Token getBodyTag()
/*     */   {
/* 218 */     int pos = this.tagList.binarySearch(new Token("TEXT"));
/* 219 */     if (pos < 0) {
/* 220 */       return null;
/*     */     }
/* 222 */     return (Token)this.tagList.get(pos);
/*     */   }
/*     */ 
/*     */   protected int getTagContent(String content, String tag, int start, StringBuffer out)
/*     */   {
/* 228 */     start = content.indexOf("<" + tag + ">", start);
/* 229 */     if (start < 0)
/* 230 */       return start;
/* 231 */     start = start + 2 + tag.length();
/* 232 */     int end = content.indexOf("</" + tag + ">", start);
/* 233 */     if (end < 0)
/* 234 */       return start;
/* 235 */     if (out.length() > 0)
/* 236 */       out.append(' ');
/* 237 */     out.append(removeTag(content.substring(start, end)));
/* 238 */     return end + 3 + tag.length();
/*     */   }
/*     */ 
/*     */   protected String getTagContent(String rawText, String tagName, boolean preprocess)
/*     */   {
/*     */     int pos;
/* 246 */     if ((pos = this.tagList.binarySearch(new Token(tagName))) < 0)
/* 247 */       return null;
/* 248 */     Token tag = (Token)this.tagList.get(pos);
/* 249 */     int start = tag.getIndex() + 2 + tag.getName().length();
/* 250 */     int end = rawText.indexOf("</" + tag + ">", start);
/* 251 */     if (end < 0)
/* 252 */       return null;
/* 253 */     String tagContent = rawText.substring(start, end);
/* 254 */     if (preprocess)
/* 255 */       tagContent = removeTag(tagContent);
/* 256 */     return tagContent;
/*     */   }
/*     */ 
/*     */   protected String getTagContent(String rawText, Token tag, boolean preprocess)
/*     */   {
/* 263 */     if (tag == null)
/* 264 */       return null;
/* 265 */     int start = tag.getIndex() + 2 + tag.getName().length();
/* 266 */     int end = rawText.indexOf("</" + tag + ">", start);
/* 267 */     if (end < 0)
/* 268 */       return null;
/* 269 */     String tagContent = rawText.substring(start, end);
/* 270 */     if (preprocess)
/* 271 */       tagContent = removeTag(tagContent);
/* 272 */     return tagContent;
/*     */   }
/*     */ 
/*     */   protected String removeTag(String content)
/*     */   {
/* 279 */     StringBuffer sb = new StringBuffer();
/* 280 */     int start = 0;
/* 281 */     int lastPos = 0;
/* 282 */     while (start >= 0)
/*     */     {
/* 284 */       start = content.indexOf('<', start);
/* 285 */       if (start >= 0) {
/* 286 */         if (start > lastPos) {
/* 287 */           sb.append(processTagContent(content.substring(lastPos, start)));
/* 288 */           sb.append(' ');
/*     */         }
/* 290 */         start = content.indexOf(">", start);
/* 291 */         if (start >= 0)
/* 292 */           lastPos = start + 1;
/*     */       }
/*     */     }
/* 295 */     if (lastPos < content.length())
/* 296 */       sb.append(processTagContent(content.substring(lastPos).trim()));
/* 297 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private String processTagContent(String content) {
/* 301 */     if (content.length() <= 10) {
/* 302 */       return "";
/*     */     }
/* 304 */     if ((content.length() >= 400) && 
/* 305 */       (!containSentence(content))) {
/* 306 */       content = content.replaceAll("\n", ". ");
/*     */     }
/* 308 */     content = replacement(content);
/* 309 */     if ((content.length() > 40) && (".!;?".indexOf(content.charAt(content.length() - 1)) < 0))
/* 310 */       content = content + ".";
/* 311 */     return content;
/*     */   }
/*     */ 
/*     */   private String replacement(String content) {
/* 315 */     content = content.replaceAll("&amp;", "&");
/* 316 */     content = content.replaceAll("''", "\"");
/* 317 */     content = content.replaceAll("``", "\"");
/* 318 */     content = content.replace('\r', ' ');
/* 319 */     return content = content.replace('\n', ' ').trim();
/*     */   }
/*     */ 
/*     */   private boolean containSentence(String content)
/*     */   {
/* 325 */     if (content == null) return false;
/*     */ 
/* 327 */     int start = content.indexOf(". ");
/* 328 */     if ((start >= 0) && (start <= 400)) {
/* 329 */       return true;
/*     */     }
/* 331 */     start = content.indexOf(".\r");
/* 332 */     if ((start >= 0) && (start <= 400)) {
/* 333 */       return true;
/*     */     }
/* 335 */     start = content.indexOf(".\n");
/* 336 */     if ((start >= 0) && (start <= 400)) {
/* 337 */       return true;
/*     */     }
/* 339 */     return false;
/*     */   }
/*     */ 
/*     */   protected SortedArray collectTagInformation(String content)
/*     */   {
/*     */     try
/*     */     {
/* 348 */       SortedArray tagList = new SortedArray(30);
/* 349 */       int start = content.indexOf('<');
/* 350 */       while (start >= 0) {
/* 351 */         if (content.charAt(start + 1) != '/') {
/* 352 */           int end = content.indexOf('>', start);
/* 353 */           Token curToken = new Token(content.substring(start + 1, end), start, 1);
/* 354 */           if (!tagList.add(curToken)) {
/* 355 */             curToken = (Token)tagList.get(tagList.insertedPos());
/* 356 */             curToken.addFrequency(1);
/*     */           }
/* 358 */           start = end + 1;
/*     */         }
/*     */         else {
/* 361 */           start++;
/* 362 */         }start = content.indexOf('<', start);
/*     */       }
/* 364 */       return tagList;
/*     */     }
/*     */     catch (Exception e) {
/* 367 */       System.out.println("Invalid SGM format!");
/* 368 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.onlinedb.trec.SgmArticleParser
 * JD-Core Version:    0.6.2
 */