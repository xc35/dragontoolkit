/*     */ package edu.drexel.cis.dragon.nlp.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.tool.MedPostTagger;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class ClauseFinder
/*     */ {
/*     */   String[] arrConj;
/*     */   int conjNum;
/*     */   Sentence sent;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  23 */     ClauseFinder finder = new ClauseFinder();
/*     */ 
/*  26 */     Sentence sent = new EngDocumentParser().parseSentence("I like him");
/*  27 */     Tagger tagger = new MedPostTagger(System.getProperty("user.dir"));
/*  28 */     tagger.tag(sent);
/*  29 */     finder.clauseIdentify(sent);
/*  30 */     Word curWord = sent.getFirstWord();
/*  31 */     while (curWord != null)
/*     */     {
/*  33 */       System.out.print(curWord.getContent());
/*  34 */       System.out.print(" ");
/*  35 */       System.out.print(curWord.getClauseID());
/*  36 */       System.out.print("\r\n");
/*  37 */       curWord = curWord.next;
/*     */     }
/*     */   }
/*     */ 
/*     */   public ClauseFinder()
/*     */   {
/*  43 */     this.conjNum = 9;
/*  44 */     this.arrConj = new String[this.conjNum];
/*  45 */     this.arrConj[0] = "although";
/*  46 */     this.arrConj[1] = "because";
/*  47 */     this.arrConj[2] = "but";
/*  48 */     this.arrConj[3] = "if";
/*  49 */     this.arrConj[4] = "that";
/*  50 */     this.arrConj[5] = "though";
/*  51 */     this.arrConj[6] = "when";
/*  52 */     this.arrConj[7] = "whether";
/*  53 */     this.arrConj[8] = "while";
/*     */   }
/*     */ 
/*     */   public int clauseIdentify(Sentence sent)
/*     */   {
/*  62 */     int clauseID = 0;
/*  63 */     Word openner = sent.getFirstWord();
/*  64 */     Word cur = sent.getFirstWord();
/*  65 */     while (cur != null)
/*     */     {
/*  67 */       boolean newClause = false;
/*  68 */       int pos = cur.getPOSIndex();
/*  69 */       if (pos == 8)
/*     */       {
/*  71 */         newClause = processConjunction(cur);
/*  72 */         if (newClause) openner = cur;
/*     */       }
/*  74 */       else if ((cur.isPunctuation()) && (cur.getContent().equalsIgnoreCase(",")))
/*     */       {
/*  76 */         newClause = processComma(cur, openner);
/*  77 */         if (newClause) openner = null;
/*     */       }
/*  79 */       else if (((pos == 0) || (pos >= 4)) && 
/*  80 */         (cur.getContent().equalsIgnoreCase("that"))) {
/*  81 */         newClause = processThat(cur);
/*     */       }
/*     */ 
/*  84 */       if (newClause)
/*  85 */         clauseID++;
/*  86 */       cur.setClauseID(clauseID);
/*  87 */       cur = cur.next;
/*     */     }
/*  89 */     return clauseID + 1;
/*     */   }
/*     */ 
/*     */   private boolean processConjunction(Word current)
/*     */   {
/*  94 */     if ((current.prev == null) || (current.next == null)) return false;
/*     */ 
/*  96 */     int index = isConjunctionMarker(current.getContent());
/*  97 */     if (index < 0) return false;
/*     */ 
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean processComma(Word current, Word openner)
/*     */   {
/* 105 */     if ((current.prev == null) || (current.next == null)) return false;
/*     */ 
/* 107 */     if ((current.prev.getPOSIndex() == 9) || (current.next.getPOSIndex() == 9)) {
/* 108 */       return false;
/*     */     }
/* 110 */     if (current.getParallelGroup() >= 0) return false;
/* 111 */     if (openner == null) return false;
/* 112 */     int pos = openner.getPOSIndex();
/* 113 */     if ((pos == 5) || (pos == 8) || (pos == 4)) {
/* 114 */       return true;
/*     */     }
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean processThat(Word current)
/*     */   {
/* 122 */     if ((current.prev == null) || (current.next == null)) return false;
/*     */ 
/* 124 */     int prevPos = current.prev.getPOSIndex();
/* 125 */     if ((prevPos == 2) || (prevPos == 8) || (prevPos == 4))
/* 126 */       return true;
/* 127 */     if (prevPos == 1)
/*     */     {
/* 129 */       current.setAssociatedConcept(current.prev.getAssociatedConcept());
/* 130 */       return true;
/*     */     }
/* 132 */     return false;
/*     */   }
/*     */ 
/*     */   private int isConjunctionMarker(String word)
/*     */   {
/* 139 */     int low = 0;
/* 140 */     int high = this.conjNum - 1;
/* 141 */     while (low <= high)
/*     */     {
/* 143 */       int middle = (low + high) / 2;
/* 144 */       int result = this.arrConj[middle].compareToIgnoreCase(word);
/* 145 */       if (result == 0) return middle;
/* 146 */       if (result > 0)
/* 147 */         high = middle - 1;
/*     */       else
/* 149 */         low = middle + 1;
/*     */     }
/* 151 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.ClauseFinder
 * JD-Core Version:    0.6.2
 */