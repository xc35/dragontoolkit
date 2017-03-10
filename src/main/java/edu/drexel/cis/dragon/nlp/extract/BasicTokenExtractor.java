/*    */ package edu.drexel.cis.dragon.nlp.extract;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.DocumentParser;
/*    */ import edu.drexel.cis.dragon.nlp.Sentence;
/*    */ import edu.drexel.cis.dragon.nlp.Token;
/*    */ import edu.drexel.cis.dragon.nlp.Word;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class BasicTokenExtractor extends AbstractTokenExtractor
/*    */ {
/*    */   public BasicTokenExtractor(Lemmatiser lemmatiser)
/*    */   {
/* 18 */     super(lemmatiser);
/*    */   }
/*    */ 
/*    */   public BasicTokenExtractor(Lemmatiser lemmatiser, String stoplistFile) {
/* 22 */     super(lemmatiser);
/* 23 */     setConceptFilter(new BasicConceptFilter(stoplistFile));
/*    */   }
/*    */ 
/*    */   public ArrayList extractFromDoc(String content)
/*    */   {
/* 31 */     ArrayList list = this.parser.parseTokens(content);
/* 32 */     this.conceptList = new ArrayList();
/* 33 */     if (list == null)
/* 34 */       return this.conceptList;
/* 35 */     for (int i = 0; i < list.size(); i++) {
/* 36 */       String value = (String)list.get(i);
/* 37 */       addToken(value, this.conceptList);
/*    */     }
/* 39 */     list.clear();
/* 40 */     return this.conceptList;
/*    */   }
/*    */ 
/*    */   public ArrayList extractFromSentence(Sentence sent)
/*    */   {
/* 47 */     ArrayList tokenList = new ArrayList();
/* 48 */     Word cur = sent.getFirstWord();
/* 49 */     while (cur != null) {
/* 50 */       if (cur.getType() != 4)
/* 51 */         addToken(new String(cur.getContent()), tokenList);
/* 52 */       cur = cur.next;
/*    */     }
/* 54 */     return tokenList;
/*    */   }
/*    */ 
/*    */   private Token addToken(String value, ArrayList tokenList)
/*    */   {
/* 60 */     if (this.lemmatiser != null)
/* 61 */       value = this.lemmatiser.lemmatize(value);
/* 62 */     if ((this.conceptFilter_enabled) && (!this.cf.keep(value))) return null;
/*    */ 
/* 64 */     Token token = new Token(value);
/* 65 */     tokenList.add(token);
/* 66 */     return token;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.BasicTokenExtractor
 * JD-Core Version:    0.6.2
 */