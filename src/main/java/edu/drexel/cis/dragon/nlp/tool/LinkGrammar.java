/*    */ package edu.drexel.cis.dragon.nlp.tool;
/*    */ 
/*    */ import edu.drexel.cis.dragon.util.EnvVariable;
/*    */ import edu.drexel.cis.dragon.util.FileUtil;
/*    */ import edu.cmu.cs.linkgrammar.Constituent;
/*    */ import edu.cmu.cs.linkgrammar.Dictionary;
/*    */ import edu.cmu.cs.linkgrammar.Linkage;
/*    */ import edu.cmu.cs.linkgrammar.ParseOptions;
/*    */ import edu.cmu.cs.linkgrammar.Sentence;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class LinkGrammar
/*    */ {
/*    */   private Dictionary lgd;
/*    */   private ParseOptions lgp;
/*    */   private Sentence lgs;
/*    */ 
/*    */   public LinkGrammar()
/*    */   {
/* 20 */     this(EnvVariable.getDragonHome() + "/nlpdata/linkgrammar", 100, 1);
/*    */   }
/*    */ 
/*    */   public LinkGrammar(int maxLinkage, int maxParseTime) {
/* 24 */     this(EnvVariable.getDragonHome() + "/nlpdata/linkgrammar", maxLinkage, maxParseTime);
/*    */   }
/*    */ 
/*    */   public LinkGrammar(String workDir, int maxLinkage, int maxParseTime) {
/* 28 */     if ((!FileUtil.exist(workDir)) && (FileUtil.exist(EnvVariable.getDragonHome() + "/" + workDir)))
/* 29 */       workDir = EnvVariable.getDragonHome() + "/" + workDir;
/* 30 */     this.lgd = new Dictionary(workDir + "/4.0.dict", "4.0.knowledge", "4.0.constituent-knowledge", "4.0.affix");
/* 31 */     this.lgp = new ParseOptions();
/* 32 */     this.lgp.parseOptionsSetShortLength(10);
/* 33 */     this.lgp.parseOptionsSetMaxNullCount(10);
/* 34 */     this.lgp.parseOptionsSetLinkageLimit(maxLinkage);
/* 35 */     this.lgp.parseOptionsSetMaxParseTime(maxParseTime);
/*    */   }
/*    */ 
/*    */   public static void main(String[] args) {
/* 39 */     LinkGrammar lg = new LinkGrammar();
/* 40 */     String example = "";
/*    */ 
/* 42 */     for (int i = 0; i < args.length; i++) example = example + " " + args[i];
/* 43 */     if (example.length() == 0)
/* 44 */       example = "I love you";
/* 45 */     Sentence lgs = lg.parse(example);
/*    */ 
/* 47 */     if (lgs.sentenceNumLinkagesFound() < 1)
/*    */     {
/* 49 */       lgs.sentenceDelete();
/* 50 */       System.out.println("No linkage was found.");
/*    */     }
/*    */     else
/*    */     {
/* 54 */       Linkage link = lg.getLinkage(0);
/* 55 */       System.out.println(link.linkagePrintDiagram());
/* 56 */       System.out.println(lg.getConstituentTree(link, 1));
/* 57 */       System.out.println(lg.getConstituentTree(link, 2));
/* 58 */       link.linkageDelete();
/* 59 */       lgs.sentenceDelete();
/*    */     }
/* 61 */     lg.close();
/*    */   }
/*    */ 
/*    */   public Sentence parse(String sent)
/*    */   {
/* 67 */     this.lgs = new Sentence(sent, this.lgd);
/* 68 */     this.lgs.parse(this.lgp);
/* 69 */     boolean resource_exhausted = this.lgp.parseOptionsResourcesExhausted() > 0;
/* 70 */     if (resource_exhausted)
/* 71 */       this.lgp.parseOptionsResetResources();
/* 72 */     return this.lgs;
/*    */   }
/*    */ 
/*    */   public Linkage getLinkage(int index)
/*    */   {
/* 77 */     return new Linkage(index, this.lgs, this.lgp);
/*    */   }
/*    */ 
/*    */   public String getConstituentTree(Linkage linkage, int mode)
/*    */   {
/* 90 */     Constituent lgc = new Constituent(linkage);
/* 91 */     String tree = lgc.printConstituentTree(linkage, mode);
/* 92 */     lgc.linkageFreeConstituentTree();
/* 93 */     return tree;
/*    */   }
/*    */ 
/*    */   public void close() {
/* 97 */     this.lgd.dictionaryDelete();
/* 98 */     this.lgp.parseOptionsDelete();
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.LinkGrammar
 * JD-Core Version:    0.6.2
 */