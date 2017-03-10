/*     */ package edu.drexel.cis.dragon.qa.qc;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Phrase;
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Token;
/*     */ import edu.drexel.cis.dragon.nlp.extract.AbstractTokenExtractor;
/*     */ import edu.drexel.cis.dragon.nlp.extract.ConceptFilter;
/*     */ import edu.drexel.cis.dragon.nlp.extract.EngDocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.BasicVocabulary;
/*     */ import edu.drexel.cis.dragon.nlp.ontology.Vocabulary;
/*     */ import edu.drexel.cis.dragon.nlp.tool.BrillTagger;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ import edu.drexel.cis.dragon.nlp.tool.WordNetDidion;
/*     */ import edu.drexel.cis.dragon.nlp.tool.lemmatiser.EngLemmatiser;
/*     */ import edu.drexel.cis.dragon.qa.util.QuestionSentence;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import net.didion.jwnl.data.IndexWord;
/*     */ import net.didion.jwnl.data.POS;
/*     */ import net.didion.jwnl.data.Pointer;
/*     */ import net.didion.jwnl.data.PointerType;
/*     */ import net.didion.jwnl.data.Synset;
/*     */ import net.didion.jwnl.dictionary.Dictionary;
/*     */ 
/*     */ public class SemanticFeatureExtractor extends AbstractTokenExtractor
/*     */ {
/*     */   private Tagger tagger;
/*     */   private Dictionary wordnet;
/*     */   private SimplePhraseExtractor pe;
/*     */   private boolean semanticFeature;
/*     */   private boolean phraseFeature;
/*     */   private boolean posFeature;
/*     */   private boolean abbrFeature;
/*     */   private boolean wordFeature;
/*     */   private boolean biQuestionWordFeature;
/*     */   private boolean entityFeature;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  38 */     SemanticFeatureExtractor extractor = new SemanticFeatureExtractor(new BasicVocabulary("qa/uiuc/uiuc.vob"), new BrillTagger(), new EngLemmatiser(false, false));
/*  39 */     EngDocumentParser parser = new EngDocumentParser();
/*  40 */     ArrayList list = extractor.extractFromSentence(parser.parseSentence("who is Abraham Lincoln"));
/*  41 */     for (int i = 0; i < list.size(); i++)
/*  42 */       System.out.println(((Token)list.get(i)).getName());
/*     */   }
/*     */ 
/*     */   public SemanticFeatureExtractor(Vocabulary vob, Tagger tagger, Lemmatiser lemmatiser) {
/*  46 */     super(lemmatiser);
/*  47 */     this.tagger = tagger;
/*  48 */     this.wordnet = new WordNetDidion().getDictionary();
/*  49 */     this.pe = new SimplePhraseExtractor(vob, lemmatiser, tagger);
/*  50 */     Phrase.setNameMode(1);
/*  51 */     this.semanticFeature = true;
/*  52 */     this.phraseFeature = true;
/*  53 */     this.posFeature = false;
/*  54 */     this.abbrFeature = true;
/*  55 */     this.wordFeature = true;
/*  56 */     this.biQuestionWordFeature = true;
/*  57 */     this.entityFeature = true;
/*     */   }
/*     */ 
/*     */   public void setFeatureOption(boolean wordFeature, boolean semanticFeature, boolean phraseFeature, boolean biQuestionWordFeature, boolean posFeature, boolean abbrFeature, boolean entityFeature)
/*     */   {
/*  62 */     this.wordFeature = wordFeature;
/*  63 */     this.semanticFeature = semanticFeature;
/*  64 */     this.phraseFeature = phraseFeature;
/*  65 */     this.biQuestionWordFeature = biQuestionWordFeature;
/*  66 */     this.posFeature = posFeature;
/*  67 */     this.abbrFeature = abbrFeature;
/*  68 */     this.entityFeature = entityFeature;
/*     */   }
/*     */ 
/*     */   public ArrayList extractFromSentence(Sentence sent)
/*     */   {
/*  79 */     dragon.nlp.Word cur = sent.getFirstWord();
/*  80 */     if ((cur != null) && (cur.getPOSIndex() < 0))
/*  81 */       this.tagger.tag(sent);
/*  82 */     while (cur != null) {
/*  83 */       if (cur.getLemma() == null)
/*  84 */         cur.setLemma(this.lemmatiser.lemmatize(cur.getContent(), cur.getPOSIndex()));
/*  85 */       cur = cur.next;
/*     */     }
/*     */ 
/*  88 */     QuestionSentence qsent = new QuestionSentence(sent);
/*  89 */     dragon.nlp.Word questionWord = qsent.getQuestionWord();
/*  90 */     ArrayList tokenList = new SortedArray();
/*  91 */     dragon.nlp.Word verb = qsent.getFirstVerb();
/*     */ 
/*  94 */     if ((this.semanticFeature) && ((questionWord == null) || (" what which ".indexOf(questionWord.getContent().toLowerCase()) >= 0)))
/*     */     {
/*  96 */       dragon.nlp.Word headNoun = qsent.getHeadNoun(questionWord);
/*  97 */       if ((headNoun != null) && (verb != null) && (QuestionSentence.isVerbDo(verb.getContent())) && (headNoun.getPosInSentence() > verb.getPosInSentence()))
/*  98 */         headNoun = null;
/*  99 */       if ((headNoun != null) && 
/* 100 */         (" name type kind ".indexOf(" " + headNoun.getContent().toLowerCase() + " ") >= 0)) {
/* 101 */         headNoun = qsent.getPostModifierNoun(headNoun);
/*     */       }
/* 103 */       if (headNoun != null) {
/* 104 */         SortedArray list = getHypernyms(headNoun.getLemma(), 1);
/* 105 */         if (list != null) {
/* 106 */           for (int i = 0; i < list.size(); i++)
/* 107 */             addToken((String)list.get(i), tokenList);
/* 108 */           list.clear();
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 114 */     if (this.posFeature) {
/* 115 */       cur = sent.getFirstWord();
/* 116 */       while (cur != null) {
/* 117 */         if (cur.getType() != 4)
/* 118 */           addToken(cur.getPOSLabel(), tokenList);
/* 119 */         cur = cur.next;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 124 */     if (this.abbrFeature) {
/* 125 */       cur = sent.getFirstWord();
/* 126 */       while (cur != null) {
/* 127 */         if ((cur.getContent().length() >= 3) && (cur.isAllCapital()))
/* 128 */           addToken("ABBR", tokenList);
/* 129 */         cur = cur.next;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 134 */     if (this.phraseFeature) {
/* 135 */       ArrayList phraseList = this.pe.extractFromSentence(sent);
/* 136 */       for (int i = 0; i < phraseList.size(); i++)
/* 137 */         addToken(((Phrase)phraseList.get(i)).getName(), tokenList);
/* 138 */       phraseList.clear();
/*     */     }
/*     */ 
/* 142 */     if ((this.biQuestionWordFeature) && (qsent.isBigramQuestionWord(questionWord))) {
/* 143 */       addToken(questionWord.getContent() + " " + 
/* 144 */         questionWord.next.getContent(), tokenList);
/*     */     }
/*     */ 
/* 147 */     if ((this.entityFeature) && (verb != null) && (verb.next != null) && (QuestionSentence.isVerbBe(verb.getContent()))) {
/* 148 */       cur = verb.next;
/* 149 */       if (cur.getPOSIndex() == 7)
/* 150 */         cur = cur.next;
/* 151 */       if ((cur != null) && (cur.getContent().equals("\"")) && (sent.getLastWord().getContent().equals("\""))) {
/* 152 */         addToken("ENTITY", tokenList);
/*     */       } else {
/* 154 */         while ((cur != null) && ((cur.getPOSIndex() == 3) || (cur.getPOSIndex() == 1)))
/* 155 */           cur = cur.next;
/* 156 */         if (cur == null) {
/* 157 */           addToken("ENTITY", tokenList);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 162 */     if (this.wordFeature) {
/* 163 */       cur = sent.getFirstWord();
/* 164 */       while (cur != null) {
/* 165 */         if (cur.getType() != 4)
/* 166 */           addToken(cur.getLemma(), tokenList);
/* 167 */         cur = cur.next;
/*     */       }
/*     */     }
/* 170 */     return tokenList;
/*     */   }
/*     */ 
/*     */   private Token addToken(String value, ArrayList tokenList) {
/* 174 */     return addToken(value, 1, tokenList);
/*     */   }
/*     */ 
/*     */   private Token addToken(String value, int freq, ArrayList tokenList)
/*     */   {
/* 180 */     if ((this.conceptFilter_enabled) && (!this.cf.keep(value))) {
/* 181 */       return null;
/*     */     }
/* 183 */     Token token = new Token(value);
/* 184 */     token.setFrequency(freq);
/* 185 */     tokenList.add(token);
/* 186 */     return token;
/*     */   }
/*     */ 
/*     */   private SortedArray getHypernyms(String word, int posIndex)
/*     */   {
/*     */     try
/*     */     {
/* 194 */       IndexWord indexWord = this.wordnet.lookupIndexWord(getPOS(posIndex), word);
/* 195 */       if (indexWord == null)
/* 196 */         return null;
/* 197 */       SortedArray list = new SortedArray();
/* 198 */       getHypernyms(indexWord.getSense(1), list);
/* 199 */       return list;
/*     */     } catch (Exception e) {
/* 201 */       e.printStackTrace();
/* 202 */     }return null;
/*     */   }
/*     */ 
/*     */   private void getHypernyms(Synset synset, SortedArray list)
/*     */   {
/*     */     try
/*     */     {
/* 211 */       Pointer[] arrPointer = getPointers(synset, PointerType.HYPERNYM);
/* 212 */       if (arrPointer == null)
/* 213 */         return;
/* 214 */       for (int i = 0; i < arrPointer.length; i++)
/* 215 */         if (!arrPointer[i].isLexical()) {
/* 216 */           synset = arrPointer[i].getTargetSynset();
/* 217 */           int num = synset.getWordsSize();
/* 218 */           for (int j = 0; j < num; j++)
/* 219 */             list.add(synset.getWord(j).getLemma());
/* 220 */           getHypernyms(synset, list);
/*     */         }
/*     */     }
/*     */     catch (Exception e) {
/* 224 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private POS getPOS(int index) {
/* 229 */     switch (index) {
/*     */     case 1:
/* 231 */       return POS.NOUN;
/*     */     case 2:
/* 233 */       return POS.VERB;
/*     */     case 3:
/* 235 */       return POS.ADJECTIVE;
/*     */     case 4:
/* 237 */       return POS.ADVERB;
/*     */     }
/* 239 */     return null;
/*     */   }
/*     */ 
/*     */   private Pointer[] getPointers(Synset synset, PointerType type)
/*     */   {
/* 247 */     Pointer[] pointers = synset.getPointers();
/* 248 */     if (pointers == null) {
/* 249 */       return null;
/*     */     }
/* 251 */     ArrayList list = new ArrayList();
/* 252 */     for (int i = 0; i < pointers.length; i++) {
/* 253 */       if (type.equals(pointers[i].getType())) {
/* 254 */         list.add(pointers[i]);
/*     */       }
/*     */     }
/* 257 */     return (Pointer[])list.toArray(new Pointer[list.size()]);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.qc.SemanticFeatureExtractor
 * JD-Core Version:    0.6.2
 */