/*     */ package edu.drexel.cis.dragon.nlp.tool;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Document;
/*     */ import edu.drexel.cis.dragon.nlp.Paragraph;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Term;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.util.EnvVariable;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import gate.Annotation;
/*     */ import gate.AnnotationSet;
/*     */ import gate.Corpus;
/*     */ import gate.CreoleRegister;
/*     */ import gate.Factory;
/*     */ import gate.FeatureMap;
/*     */ import gate.Gate;
/*     */ import gate.Node;
/*     */ import gate.ProcessingResource;
/*     */ import gate.corpora.DocumentImpl;
/*     */ import gate.creole.ANNIEConstants;
/*     */ import gate.creole.SerialAnalyserController;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class Annie
/*     */   implements NER
/*     */ {
/*     */   private SerialAnalyserController annieController;
/*     */   private Corpus corpus;
/*     */   private Set annotTypesRequired;
/*     */ 
/*     */   public Annie()
/*     */     throws Exception
/*     */   {
/*  26 */     initAnnie(EnvVariable.getDragonHome() + "/nlpdata/gate");
/*     */   }
/*     */ 
/*     */   public Annie(String gateHome) throws Exception {
/*  30 */     if ((!FileUtil.exist(gateHome)) && (FileUtil.exist(EnvVariable.getDragonHome() + "/" + gateHome)))
/*  31 */       gateHome = EnvVariable.getDragonHome() + "/" + gateHome;
/*  32 */     initAnnie(gateHome);
/*     */   }
/*     */ 
/*     */   public void initAnnie(String gateHome) throws Exception
/*     */   {
/*  37 */     Gate.setGateHome(new File(gateHome));
/*  38 */     Gate.setPluginsHome(new File(gateHome));
/*  39 */     Gate.setUserConfigFile(new File(Gate.getGateHome(), "gate.xml"));
/*  40 */     Gate.init();
/*  41 */     Gate.getCreoleRegister().registerDirectories(new File(Gate.getGateHome(), "ANNIE").toURL());
/*     */ 
/*  43 */     System.out.println("Initializing ANNIE...");
/*     */ 
/*  45 */     this.annieController = 
/*  46 */       ((SerialAnalyserController)Factory.createResource("gate.creole.SerialAnalyserController", 
/*  46 */       Factory.newFeatureMap(), Factory.newFeatureMap(), "ANNIE_" + Gate.genSym()));
/*     */ 
/*  50 */     for (int i = 0; i < ANNIEConstants.PR_NAMES.length - 1; i++) {
/*  51 */       FeatureMap params = Factory.newFeatureMap();
/*  52 */       ProcessingResource pr = (ProcessingResource)Factory.createResource(ANNIEConstants.PR_NAMES[i], params);
/*  53 */       this.annieController.add(pr);
/*     */     }
/*  55 */     System.out.println("...ANNIE loaded");
/*     */ 
/*  57 */     this.corpus = ((Corpus)Factory.createResource("gate.corpora.CorpusImpl"));
/*  58 */     this.annotTypesRequired = new HashSet();
/*  59 */     this.annotTypesRequired.add("Person");
/*  60 */     this.annotTypesRequired.add("Location");
/*  61 */     this.annotTypesRequired.add("Organization");
/*     */   }
/*     */ 
/*     */   public void setAnnotationTypes(String[] arrType)
/*     */   {
/*  67 */     if ((arrType == null) || (arrType.length == 0)) return;
/*     */ 
/*  69 */     this.annotTypesRequired = new HashSet();
/*  70 */     for (int i = 0; i < arrType.length; i++)
/*  71 */       this.annotTypesRequired.add(arrType[i]);
/*     */   }
/*     */ 
/*     */   public void close() {
/*  75 */     this.annieController.cleanup();
/*     */   }
/*     */ 
/*     */   public ArrayList extractEntities(String content)
/*     */   {
/*     */     try
/*     */     {
/*  87 */       content = content.replaceAll("\r\n", " ");
/*  88 */       content = content.replace('\r', ' ');
/*  89 */       content = content.replace('\n', ' ');
/*     */ 
/*  91 */       SortedArray entityList = new SortedArray();
/*  92 */       DocumentImpl doc = new DocumentImpl();
/*  93 */       doc.setStringContent(content);
/*  94 */       doc.init();
/*  95 */       this.corpus.clear();
/*  96 */       this.corpus.add(doc);
/*  97 */       this.annieController.setCorpus(this.corpus);
/*  98 */       this.annieController.execute();
/*     */ 
/* 100 */       AnnotationSet defaultAnnotSet = doc.getAnnotations();
/* 101 */       defaultAnnotSet = defaultAnnotSet.get(this.annotTypesRequired);
/* 102 */       if (defaultAnnotSet == null) return null;
/*     */ 
/* 104 */       Iterator it = defaultAnnotSet.iterator();
/* 105 */       while (it.hasNext()) {
/* 106 */         Annotation curAnnotation = (Annotation)it.next();
/* 107 */         Token curToken = new Token(content.substring(curAnnotation.getStartNode().getOffset().intValue(), curAnnotation.getEndNode().getOffset().intValue()));
/* 108 */         if (!entityList.add(curToken)) {
/* 109 */           curToken = (Token)entityList.get(entityList.insertedPos());
/* 110 */           curToken.addFrequency(1);
/*     */         }
/*     */       }
/* 113 */       doc.cleanup();
/* 114 */       return entityList;
/*     */     }
/*     */     catch (Exception e) {
/* 117 */       e.printStackTrace();
/* 118 */     }return null;
/*     */   }
/*     */ 
/*     */   public ArrayList extractFromDoc(Document doc)
/*     */   {
/*     */     try
/*     */     {
/* 127 */       ArrayList termList = new ArrayList(60);
/* 128 */       Paragraph pg = doc.getFirstParagraph();
/* 129 */       while (pg != null) {
/* 130 */         Sentence sent = pg.getFirstSentence();
/* 131 */         while (sent != null) {
/* 132 */           ArrayList curTermList = extractFromSentence(sent);
/* 133 */           if (curTermList != null) {
/* 134 */             termList.addAll(curTermList);
/* 135 */             curTermList.clear();
/*     */           }
/* 137 */           sent = sent.next;
/*     */         }
/* 139 */         pg = pg.next;
/*     */       }
/* 141 */       return termList;
/*     */     }
/*     */     catch (Exception e) {
/* 144 */       e.printStackTrace();
/* 145 */     }return null;
/*     */   }
/*     */ 
/*     */   public ArrayList extractFromSentence(Sentence sent)
/*     */   {
/*     */     try
/*     */     {
/* 162 */       StringBuffer content = new StringBuffer();
/* 163 */       Word curWord = sent.getFirstWord();
/* 164 */       while (curWord != null) {
/* 165 */         if (content.length() > 0)
/* 166 */           content.append(' ');
/* 167 */         curWord.setOffset(content.length());
/* 168 */         content.append(curWord.getContent());
/* 169 */         curWord = curWord.next;
/*     */       }
/* 171 */       if (content.toString().trim().length() <= 20) {
/* 172 */         return null;
/*     */       }
/* 174 */       DocumentImpl doc = new DocumentImpl();
/* 175 */       doc.setStringContent(content.toString());
/* 176 */       doc.init();
/* 177 */       this.corpus.clear();
/* 178 */       this.corpus.add(doc);
/* 179 */       this.annieController.setCorpus(this.corpus);
/* 180 */       this.annieController.execute();
/* 181 */       AnnotationSet defaultAnnotSet = doc.getAnnotations();
/* 182 */       defaultAnnotSet = defaultAnnotSet.get(this.annotTypesRequired);
/* 183 */       if (defaultAnnotSet == null) {
/* 184 */         return null;
/*     */       }
/* 186 */       SortedArray annotationList = new SortedArray(new AnnotationComparator(null));
/* 187 */       Iterator it = defaultAnnotSet.iterator();
/* 188 */       if (it == null)
/* 189 */         return null;
/* 190 */       while (it.hasNext()) {
/* 191 */         annotationList.add(it.next());
/*     */       }
/* 193 */       ArrayList termList = new ArrayList(annotationList.size());
/* 194 */       curWord = sent.getFirstWord();
/* 195 */       for (int i = 0; i < annotationList.size(); i++) {
/* 196 */         Annotation annot = (Annotation)annotationList.get(i);
/* 197 */         int start = annot.getStartNode().getOffset().intValue();
/* 198 */         int end = annot.getEndNode().getOffset().intValue();
/* 199 */         while (curWord.getOffset() < start)
/* 200 */           curWord = curWord.next;
/* 201 */         if (curWord.getOffset() == start) {
/* 202 */           Word startWord = curWord;
/* 203 */           Word endWord = curWord;
/* 204 */           curWord = curWord.next;
/* 205 */           while ((curWord != null) && (curWord.getOffset() < end)) {
/* 206 */             endWord = curWord;
/* 207 */             curWord = curWord.next;
/*     */           }
/* 209 */           Term curTerm = new Term(startWord, endWord);
/* 210 */           curTerm.setTUI(annot.getType());
/* 211 */           termList.add(curTerm);
/* 212 */           if (startWord.getAssociatedConcept() != null)
/* 213 */             startWord.setAssociatedConcept(curTerm);
/* 214 */           curWord = startWord;
/*     */         }
/*     */       }
/* 217 */       return termList;
/*     */     }
/*     */     catch (Exception e) {
/* 220 */       e.printStackTrace();
/* 221 */     }return null;
/*     */   }
/*     */ 
/*     */   public String annotate(String original)
/*     */   {
/*     */     try
/*     */     {
/* 229 */       DocumentImpl doc = new DocumentImpl();
/* 230 */       doc.setStringContent(original);
/* 231 */       doc.init();
/* 232 */       this.corpus.clear();
/* 233 */       this.corpus.add(doc);
/* 234 */       this.annieController.setCorpus(this.corpus);
/* 235 */       this.annieController.execute();
/*     */ 
/* 237 */       AnnotationSet defaultAnnotSet = doc.getAnnotations();
/* 238 */       defaultAnnotSet = defaultAnnotSet.get(this.annotTypesRequired);
/* 239 */       return "<?xml version=\"1.0\" encoding=\"windows-1252\"?>\n<doc>" + doc.toXml(defaultAnnotSet, false) + "</doc>";
/*     */     }
/*     */     catch (Exception e) {
/* 242 */       e.printStackTrace();
/* 243 */     }return null;
/*     */   }
/*     */ 
/*     */   private class AnnotationComparator implements Comparator
/*     */   {
/*     */     private AnnotationComparator() {
/*     */     }
/*     */ 
/*     */     public int compare(Object firstObj, Object secondObj) {
/* 252 */       long start1 = ((Annotation)firstObj).getStartNode().getOffset().longValue();
/* 253 */       long start2 = ((Annotation)secondObj).getStartNode().getOffset().longValue();
/* 254 */       if (start1 < start2)
/* 255 */         return -1;
/* 256 */       if (start1 == start2) {
/* 257 */         long end1 = ((Annotation)firstObj).getEndNode().getOffset().longValue();
/* 258 */         long end2 = ((Annotation)secondObj).getEndNode().getOffset().longValue();
/* 259 */         if (end1 > end2)
/* 260 */           return -1;
/* 261 */         if (end1 < end2) {
/* 262 */           return 1;
/*     */         }
/* 264 */         return 0;
/*     */       }
/*     */ 
/* 267 */       return 1;
/*     */     }
/*     */ 
/*     */     AnnotationComparator(AnnotationComparator arg2)
/*     */     {
/* 247 */       this();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.Annie
 * JD-Core Version:    0.6.2
 */