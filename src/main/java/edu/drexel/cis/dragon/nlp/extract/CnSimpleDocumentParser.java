/*     */ package edu.drexel.cis.dragon.nlp.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class CnSimpleDocumentParser extends EngDocumentParser
/*     */ {
/*     */   public static final String punctuations = "\r\n\t_-.;,?/\"'`:(){}!+[]><=%$#*@&^~|\\¡ª£¡¡¶¡·£¿¡¢£¬¡££¨£©¡¾¡¿";
/*     */ 
/*     */   public CnSimpleDocumentParser()
/*     */   {
/*  19 */     this.sentDelimitor += "£»£¿¡££¡";
/*     */   }
/*     */ 
/*     */   public Sentence parseSentence(String sentence)
/*     */   {
/*  26 */     int len = 0; int flag = 0; int start = 0;
/*     */ 
/*  28 */     if ((sentence == null) || (sentence.length() == 0)) return null;
/*     */ 
/*  30 */     Sentence newSent = new Sentence();
/*  31 */     boolean checkPeriod = this.wordDelimitor.indexOf('.') < 0;
/*  32 */     sentence = sentence.trim();
/*  33 */     len = sentence.length();
/*  34 */     if (len <= 0) return null;
/*  35 */     if (this.sentDelimitor.indexOf(sentence.charAt(len - 1)) >= 0) {
/*  36 */       newSent.setPunctuation(sentence.charAt(len - 1));
/*     */     } else {
/*  38 */       sentence = sentence + ".";
/*  39 */       newSent.setPunctuation('.');
/*  40 */       len++;
/*     */     }
/*     */ 
/*  43 */     for (int i = 0; i < len - 1; i++)
/*     */     {
/*  50 */       char ch = sentence.charAt(i);
/*  51 */       if (ch > 255)
/*     */       {
/*  53 */         if (flag >= 2) {
/*  54 */           newSent.addWord(parseWord(sentence.substring(start, i)));
/*     */         }
/*  56 */         newSent.addWord(parseWord(sentence.substring(i, i + 1)));
/*  57 */         flag = 0;
/*     */       }
/*     */       else
/*     */       {
/*  61 */         if ((checkPeriod) && (ch == '.'))
/*     */         {
/*  63 */           if (!isPeriodAsWord(i, start, sentence)) {
/*  64 */             if (flag >= 2)
/*  65 */               newSent.addWord(parseWord(sentence.substring(start, i)));
/*  66 */             flag = 2;
/*  67 */             start = i;
/*  68 */             continue;
/*     */           }
/*     */         }
/*     */ 
/*  72 */         if (ch == ' ')
/*     */         {
/*  74 */           if (flag >= 2)
/*     */           {
/*  76 */             newSent.addWord(parseWord(sentence.substring(start, i)));
/*     */           }
/*  78 */           flag = 1;
/*     */         }
/*  80 */         else if (this.wordDelimitor.indexOf(ch) >= 0)
/*     */         {
/*  82 */           if (flag >= 2)
/*     */           {
/*  84 */             newSent.addWord(parseWord(sentence.substring(start, i)));
/*     */           }
/*  86 */           start = i;
/*  87 */           flag = 2;
/*     */         }
/*     */         else
/*     */         {
/*  92 */           if (flag == 2)
/*     */           {
/*  94 */             newSent.addWord(parseWord(sentence.substring(start, i)));
/*  95 */             start = i;
/*     */           }
/*  97 */           else if ((flag == 1) || (flag == 0)) {
/*  98 */             start = i;
/*  99 */           }flag = 3;
/*     */         }
/*     */       }
/*     */     }
/* 102 */     if ((flag >= 2) && (len - 1 > start))
/* 103 */       newSent.addWord(parseWord(sentence.substring(start, len - 1)));
/* 104 */     return newSent;
/*     */   }
/*     */ 
/*     */   protected Word parseWord(String content)
/*     */   {
/* 111 */     Word cur = new Word(content);
/* 112 */     if ((content.charAt(0) <255) && (isNumber(content)))
/* 113 */       cur.setType(2);
/* 114 */     else if ((content.length() == 1) && ("\r\n\t_-.;,?/\"'`:(){}!+[]><=%$#*@&^~|\\¡ª£¡¡¶¡·£¿¡¢£¬¡££¨£©¡¾¡¿".indexOf(content) >= 0))
/* 115 */       cur.setType(4);
/* 116 */     return cur;
/*     */   }
/*     */ 
/*     */   public ArrayList parseTokens(String content)
/*     */   {
/* 126 */     if (content == null)
/* 127 */       return null;
/* 128 */     if ((content = content.trim()).length() == 0)
/* 129 */       return null;
/* 130 */     String cnPunc = "";
/* 131 */     int len = content.length();
/* 132 */     int flag = 0;
/* 133 */     int start = 0;
/* 134 */     ArrayList tokenList = new ArrayList();
/* 135 */     boolean checkPeriod = this.wordDelimitor.indexOf('.') < 0;
/*     */ 
/* 137 */     for (int i = 0; i < len; i++)
/*     */     {
/* 142 */       char ch = content.charAt(i);
/*     */ 
/* 144 */       if (ch > 255)
/*     */       {
/* 146 */         if (flag >= 2)
/* 147 */           tokenList.add(content.substring(start, i));
/* 148 */         if (cnPunc.indexOf(ch) < 0)
/* 149 */           tokenList.add(content.substring(i, i + 1));
/* 150 */         flag = 0;
/*     */       }
/*     */       else
/*     */       {
/* 154 */         if ((checkPeriod) && (ch == '.'))
/*     */         {
/* 156 */           if (!isPeriodAsToken(i, start, content)) {
/* 157 */             if (flag >= 2)
/* 158 */               tokenList.add(content.substring(start, i));
/* 159 */             flag = 1;
/* 160 */             continue;
/*     */           }
/*     */         }
/*     */ 
/* 164 */         if (this.wordDelimitor.indexOf(ch) >= 0)
/*     */         {
/* 166 */           if (flag >= 2)
/* 167 */             tokenList.add(content.substring(start, i));
/* 168 */           flag = 1;
/*     */         }
/* 172 */         else if ((flag == 1) || (flag == 0)) {
/* 173 */           start = i;
/* 174 */           flag = 2;
/*     */         }
/*     */       }
/*     */     }
/* 178 */     if (flag >= 2)
/* 179 */       tokenList.add(content.substring(start, len));
/* 180 */     return tokenList;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.CnSimpleDocumentParser
 * JD-Core Version:    0.6.2
 */