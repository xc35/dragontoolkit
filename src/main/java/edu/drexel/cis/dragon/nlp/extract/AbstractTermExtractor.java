/*     */ package edu.drexel.cis.dragon.nlp.extract;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.Ontology;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public abstract class AbstractTermExtractor extends AbstractConceptExtractor
/*     */   implements TermExtractor
/*     */ {
/*     */   protected Ontology ontology;
/*     */   protected Tagger tagger;
/*     */   protected Lemmatiser lemmatiser;
/*     */   protected boolean semanticCheck_enabled;
/*     */   protected boolean coordinatingTermPredict_enabled;
/*     */   protected boolean compoundTermPredict_enabled;
/*     */   protected boolean attributeCheck_enabled;
/*     */   protected boolean coordinatingCheck_enabled;
/*     */   protected boolean abbreviation_enabled;
/*     */   protected AttributeChecker attrChecker;
/*     */   protected CoordinatingChecker paraChecker;
/*     */   protected Abbreviation abbrChecker;
/*     */   protected CompoundTermFinder compTermFinder;
/*     */ 
/*     */   public AbstractTermExtractor(Ontology ontology, Tagger tagger, Lemmatiser lemmatiser)
/*     */   {
/*  35 */     this.tagger = tagger;
/*  36 */     this.ontology = ontology;
/*  37 */     this.lemmatiser = lemmatiser;
/*  38 */     this.attrChecker = null;
/*  39 */     this.paraChecker = new CoordinatingChecker();
/*  40 */     this.abbrChecker = new Abbreviation();
/*  41 */     this.compTermFinder = new CompoundTermFinder();
/*  42 */     this.attrChecker = null;
/*  43 */     this.attributeCheck_enabled = false;
/*  44 */     this.semanticCheck_enabled = true;
/*  45 */     this.coordinatingTermPredict_enabled = false;
/*  46 */     this.compoundTermPredict_enabled = false;
/*  47 */     this.abbreviation_enabled = true;
/*  48 */     this.coordinatingCheck_enabled = true;
/*     */   }
/*     */ 
/*     */   public boolean isExtractionMerged() {
/*  52 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean supportConceptName() {
/*  56 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean supportConceptEntry() {
/*  60 */     return true;
/*     */   }
/*     */ 
/*     */   public Ontology getOntology() {
/*  64 */     return this.ontology;
/*     */   }
/*     */ 
/*     */   public Tagger getPOSTagger() {
/*  68 */     return this.tagger;
/*     */   }
/*     */ 
/*     */   public Lemmatiser getLemmatiser() {
/*  72 */     return this.lemmatiser;
/*     */   }
/*     */ 
/*     */   public void setLemmatiser(Lemmatiser lemmatiser) {
/*  76 */     this.lemmatiser = lemmatiser;
/*     */   }
/*     */ 
/*     */   public void setSubConceptOption(boolean option) {
/*  80 */     this.subconcept_enabled = option;
/*  81 */     if (this.compTermFinder != null)
/*  82 */       this.compTermFinder.setSubTermOption(option);
/*     */   }
/*     */ 
/*     */   public void setCoordinatingCheckOption(boolean option) {
/*  86 */     this.coordinatingCheck_enabled = option;
/*     */   }
/*     */ 
/*     */   public boolean getCoordinatingCheckOption() {
/*  90 */     return this.coordinatingCheck_enabled;
/*     */   }
/*     */ 
/*     */   public void setAbbreviationOption(boolean option) {
/*  94 */     this.abbreviation_enabled = option;
/*     */   }
/*     */ 
/*     */   public boolean getAbbreviationOption() {
/*  98 */     return this.abbreviation_enabled;
/*     */   }
/*     */ 
/*     */   public void setAttributeCheckOption(boolean option) {
/* 102 */     this.attributeCheck_enabled = option;
/*     */   }
/*     */ 
/*     */   public boolean getAttributeCheckOption() {
/* 106 */     return this.attributeCheck_enabled;
/*     */   }
/*     */ 
/*     */   public boolean enableAttributeCheckOption(AttributeChecker checker) {
/* 110 */     this.attrChecker = checker;
/* 111 */     this.attributeCheck_enabled = true;
/* 112 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean getSemanticCheckOption() {
/* 116 */     return this.semanticCheck_enabled;
/*     */   }
/*     */ 
/*     */   public void setSemanticCheckOption(boolean option) {
/* 120 */     this.semanticCheck_enabled = option;
/*     */   }
/*     */ 
/*     */   public boolean getCoordinatingTermPredictOption() {
/* 124 */     return this.coordinatingTermPredict_enabled;
/*     */   }
/*     */ 
/*     */   public void setCoordinatingTermPredictOption(boolean option) {
/* 128 */     this.coordinatingTermPredict_enabled = option;
/*     */   }
/*     */ 
/*     */   public boolean getCompoundTermPredictOption() {
/* 132 */     return this.compoundTermPredict_enabled;
/*     */   }
/*     */ 
/*     */   public void setCompoundTermPredictOption(boolean option) {
/* 136 */     this.compoundTermPredict_enabled = option;
/*     */   }
/*     */ 
/*     */   public boolean enableCompoundTermPredictOption(String suffixList) {
/* 140 */     this.compTermFinder = new CompoundTermFinder(suffixList);
/* 141 */     this.compoundTermPredict_enabled = true;
/* 142 */     return true;
/*     */   }
/*     */ 
/*     */   public void initDocExtraction() {
/* 146 */     if (this.abbrChecker != null)
/* 147 */       this.abbrChecker.clearCachedAbbr();
/*     */   }
/*     */ 
/*     */   public void print(PrintWriter out, ArrayList list)
/*     */   {
/*     */     try
/*     */     {   int j;
/* 157 */       for (int i = 0; i < list.size(); i++) {
/* 158 */         Term term = (Term)list.get(i);
/* 159 */         out.write(term.toString());
/* 160 */         for ( j = 0; j < term.getAttributeNum(); j++) {
/* 161 */           out.write(47);
/* 162 */           out.write(term.getAttribute(j).toString());
/*     */         }
/* 164 */         out.write(40);
/* 165 */         out.write(String.valueOf(term.getFrequency()));
/* 166 */         out.write(41);
/* 167 */         String[] arrStr = term.getCandidateTUI();
/* 168 */         if (arrStr != null) {
/* 169 */           out.write(": ");
/* 170 */           for (j = 0; j < arrStr.length; j++) {
/* 171 */             out.write(arrStr[j]);
/* 172 */             if (j == arrStr.length - 1) {
/* 173 */               out.write(" (");
/*     */             }
/*     */             else {
/* 176 */               out.write(59);
/*     */             }
/*     */           }
/* 179 */           arrStr = term.getCandidateCUI();
/* 180 */           for (j = 0; j < arrStr.length; j++) {
/* 181 */             out.write(arrStr[j]);
/* 182 */             if ((term.getCUI() != null) && (term.getCUI().equalsIgnoreCase(arrStr[j]))) {
/* 183 */               out.write("*");
/*     */             }
/* 185 */             if (j == arrStr.length - 1) {
/* 186 */               out.write(41);
/*     */             }
/*     */             else {
/* 189 */               out.write(44);
/*     */             }
/*     */           }
/*     */         }
/* 193 */         if (term.isPredicted()) {
/* 194 */           out.write("(Predicted)");
/*     */         }
/* 196 */         out.write("\r\n");
/*     */       }
/* 198 */       out.flush();
/*     */     }
/*     */     catch (Exception e) {
/* 201 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void extractTermFromFile(String filename)
/*     */   {
/* 209 */     PrintWriter out1 = FileUtil.getPrintWriter(filename + ".term");
/* 210 */     PrintWriter out2 = FileUtil.getPrintWriter(filename + ".mergedterm");
/* 211 */     ArrayList list = extractFromDoc(FileUtil.readTextFile(filename));
/*     */     try
/*     */     {
/* 215 */       print(out1, list);
/* 216 */       out1.close();
/* 217 */       print(out2, mergeConceptByName(list));
/* 218 */       out1.close();
/*     */     }
/*     */     catch (Exception e) {
/* 221 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected ArrayList filter(ArrayList termList)
/*     */   {
/* 228 */     for (int i = 0; i < termList.size(); i++) {
/* 229 */       Term term = (Term)termList.get(i);
/* 230 */       if (!this.cf.keep(term)) {
/* 231 */         term.getStartingWord().setAssociatedConcept(null);
/* 232 */         termList.remove(i);
/* 233 */         i--;
/*     */       }
/*     */     }
/* 236 */     return termList;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.extract.AbstractTermExtractor
 * JD-Core Version:    0.6.2
 */