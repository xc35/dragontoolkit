/*     */ package edu.drexel.cis.dragon.qa.eva;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.Sentence;
/*     */ import edu.drexel.cis.dragon.nlp.Word;
/*     */ import edu.drexel.cis.dragon.nlp.extract.EngDocumentParser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.BrillTagger;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ import edu.drexel.cis.dragon.nlp.tool.lemmatiser.EngLemmatiser;
/*     */ import edu.drexel.cis.dragon.qa.util.QuestionSentence;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SimpleDictionary;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ 
/*     */ public class TrecQAUtil
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  14 */     convertAraneaOutput("qa/aranea/person.questions.trec", "qa/aranea/aranea_person.txt");
/*     */   }
/*     */ 
/*     */   public static void generatePatterns(String originalPatternFile, String newPatternFile)
/*     */   {
/*     */     try
/*     */     {
/*  27 */       String lastQNo = "";
/*  28 */       BufferedReader br = FileUtil.getTextReader(originalPatternFile);
/*  29 */       BufferedWriter bw = FileUtil.getTextWriter(newPatternFile);
/*     */       String line;
/*  30 */       while ((line = br.readLine()) != null)
/*     */       {
/*  31 */         if (line.trim().length() != 0)
/*     */         {
/*  33 */           int pos = line.indexOf('\t');
/*  34 */           if (pos < 0)
/*  35 */             pos = line.indexOf(' ');
/*  36 */           if (pos >= 0)
/*     */           {
/*  38 */             String curQNo = line.substring(0, pos);
/*  39 */             if (curQNo.equalsIgnoreCase(lastQNo)) {
/*  40 */               bw.write(9);
/*  41 */               bw.write(line.substring(pos + 1));
/*     */             }
/*     */             else {
/*  44 */               if (lastQNo.length() > 0)
/*  45 */                 bw.write(10);
/*  46 */               bw.write(curQNo + "\t" + line.substring(pos + 1));
/*  47 */               lastQNo = curQNo;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*  50 */       br.close();
/*  51 */       bw.close();
/*     */     }
/*     */     catch (Exception e) {
/*  54 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void filterQuestions(String originalQuestionFile, String wantedQuestionListFile, String newQuestionFile)
/*     */   {
/*     */     try
/*     */     {
/*  66 */       SortedArray list = new SortedArray();
/*  67 */       BufferedReader br = FileUtil.getTextReader(wantedQuestionListFile);
/*     */       String line;
/*  68 */       while ((line = br.readLine()) != null)
/*     */       {
/*  69 */         if (line.trim().length() != 0)
/*     */         {
/*  71 */           list.add(line);
/*     */         }
/*     */       }
/*  73 */       br.close();
/*     */ 
/*  75 */       br = FileUtil.getTextReader(originalQuestionFile);
/*  76 */       BufferedWriter bw = FileUtil.getTextWriter(newQuestionFile);
/*  77 */       while ((line = br.readLine()) != null)
/*  78 */         if (line.trim().length() != 0)
/*     */         {
/*  80 */           int pos = line.indexOf('\t');
/*  81 */           if (pos < 0)
/*  82 */             pos = line.indexOf(' ');
/*  83 */           if (pos >= 0)
/*     */           {
/*  85 */             String qno = line.substring(0, pos);
/*  86 */             if (list.binarySearch(qno) >= 0)
/*  87 */               bw.write(qno + "\t" + line.substring(pos + 1).trim() + "\n"); 
/*     */           }
/*     */         }
/*  89 */       br.close();
/*  90 */       bw.close();
/*     */     }
/*     */     catch (Exception e) {
/*  93 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void genLocationQuestions(String originalQuestionFile, String newQuestionFile)
/*     */   {
/*     */     try
/*     */     {
/* 111 */       Tagger tagger = new BrillTagger();
/* 112 */       Lemmatiser lemmatiser = new EngLemmatiser(false, false);
/* 113 */       EngDocumentParser parser = new EngDocumentParser();
/* 114 */       SimpleDictionary dict = new SimpleDictionary();
/* 115 */       dict.add("capital");
/* 116 */       dict.add("country");
/* 117 */       dict.add("city");
/* 118 */       dict.add("state");
/* 119 */       dict.add("mountain");
/* 120 */       dict.add("river");
/*     */ 
/* 122 */       BufferedReader br = FileUtil.getTextReader(originalQuestionFile);
/* 123 */       BufferedWriter bw = FileUtil.getTextWriter(newQuestionFile);
/*     */       String line;
/* 124 */       while ((line = br.readLine()) != null)
/*     */       {
/* 125 */         if (line.trim().length() != 0)
/*     */         {
/* 127 */           int pos = line.indexOf('\t');
/* 128 */           if (pos < 0)
/* 129 */             pos = line.indexOf(' ');
/* 130 */           if (pos >= 0)
/*     */           {
/* 132 */             String qno = line.substring(0, pos);
/* 133 */             Sentence sent = parser.parseSentence(line.substring(pos + 1));
/* 134 */             String firstWord = sent.getFirstWord().getContent();
/* 135 */             if (firstWord.equalsIgnoreCase("where")) {
/* 136 */               bw.write(qno + "\t" + line.substring(pos + 1) + "\n");
/* 137 */             } else if ((firstWord.equalsIgnoreCase("what")) || (firstWord.equalsIgnoreCase("which"))) {
/* 138 */               tagger.tag(sent);
/* 139 */               QuestionSentence qsent = new QuestionSentence(sent);
/* 140 */               Word headNoun = qsent.getHeadNoun(sent.getFirstWord());
/* 141 */               if ((headNoun != null) && (dict.exist(lemmatiser.lemmatize(headNoun.getContent(), 1))))
/* 142 */                 bw.write(qno + "\t" + line.substring(pos + 1) + "\n"); 
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 145 */       br.close();
/* 146 */       bw.close();
/*     */     }
/*     */     catch (Exception e) {
/* 149 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void convertAraneaOutput(String inputFile, String outputFile)
/*     */   {
/*     */     try
/*     */     {
/* 160 */       String curNum = "";
/* 161 */       int count = 0;
/* 162 */       BufferedReader br = FileUtil.getTextReader(inputFile);
/* 163 */       BufferedWriter bw = FileUtil.getTextWriter(outputFile);
/*     */       String line;
/* 164 */       while ((line = br.readLine()) != null)
/*     */       {
/* 165 */         if (line.startsWith("Question")) {
/* 166 */           if (curNum.length() > 0)
/* 167 */             bw.write(10);
/* 168 */           curNum = line.substring(9, line.indexOf(':'));
/* 169 */           bw.write(curNum);
/* 170 */           count = 0;
/*     */         }
/* 172 */         else if ((curNum.length() > 0) && (line.startsWith(curNum)) && (count < 5)) {
/* 173 */           int start = line.indexOf("aranea");
/* 174 */           bw.write(9);
/* 175 */           bw.write(line.substring(start + 7));
/* 176 */           count++;
/*     */         }
/*     */       }
/* 179 */       br.close();
/* 180 */       bw.close();
/*     */     }
/*     */     catch (Exception e) {
/* 183 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.qa.eva.TrecQAUtil
 * JD-Core Version:    0.6.2
 */