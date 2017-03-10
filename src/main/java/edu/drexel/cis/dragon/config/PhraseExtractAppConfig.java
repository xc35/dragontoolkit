/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.nlp.tool.Lemmatiser;
/*     */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*     */ import edu.drexel.cis.dragon.nlp.tool.xtract.SimpleXtract;
/*     */ import edu.drexel.cis.dragon.onlinedb.CollectionReader;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class PhraseExtractAppConfig
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  29 */     if (args.length != 2) {
/*  30 */       System.out.println("Please input two parameters: configuration xml file and phrase extraction applicaiton id");
/*  31 */       return;
/*     */     }
/*     */ 
/*  34 */     ConfigureNode root = new BasicConfigureNode(args[0]);
/*  35 */     ConfigUtil util = new ConfigUtil();
/*  36 */     ConfigureNode phraseAppNode = util.getConfigureNode(root, "phraseextractapp", Integer.parseInt(args[1]));
/*  37 */     if (phraseAppNode == null)
/*  38 */       return;
/*  39 */     PhraseExtractAppConfig phraseApp = new PhraseExtractAppConfig();
/*  40 */     phraseApp.phraseExtract(phraseAppNode);
/*     */   }
/*     */ 
/*     */   public void phraseExtract(ConfigureNode phraseAppNode)
/*     */   {
/*  56 */     int maxSpan = phraseAppNode.getInt("maxspan", 4);
/*  57 */     String indexFolder = phraseAppNode.getString("indexfolder");
/*  58 */     String phraseFile = phraseAppNode.getString("phrasefile");
/*  59 */     boolean indexing = phraseAppNode.getBoolean("indexing", true);
/*  60 */     double strength = phraseAppNode.getDouble("strength", 1.0D);
/*  61 */     double peakZScore = phraseAppNode.getDouble("peakzscore", 1.0D);
/*  62 */     double spread = phraseAppNode.getDouble("spread", maxSpan);
/*  63 */     double expandRatio = phraseAppNode.getDouble("expandratio", 0.75D);
/*  64 */     String vobFile = phraseAppNode.getString("vobfile", null);
/*  65 */     int maxPhraseLength = phraseAppNode.getInt("maxphraselength", 4);
/*     */ 
/*  67 */     if (indexing) {
/*  68 */       CollectionReaderConfig collectionConfig = new CollectionReaderConfig();
/*  69 */       LemmatiserConfig lemmatiserConfig = new LemmatiserConfig();
/*  70 */       TaggerConfig taggerConfig = new TaggerConfig();
/*  71 */       int lemmatiserID = phraseAppNode.getInt("lemmatiser", 0);
/*  72 */       Lemmatiser lemmatiser = lemmatiserConfig.getLemmatiser(phraseAppNode, lemmatiserID);
/*  73 */       int taggerID = phraseAppNode.getInt("tagger", 0);
/*  74 */       Tagger tagger = taggerConfig.getTagger(phraseAppNode, taggerID);
/*  75 */       String collectionIDs = phraseAppNode.getString("collectionreader");
/*  76 */       String[] arrCollection = collectionIDs.split(";");
/*  77 */       CollectionReader[] arrCollectionReader = new CollectionReader[arrCollection.length];
/*  78 */       for (int i = 0; i < arrCollection.length; i++) {
/*  79 */         arrCollectionReader[i] = collectionConfig
/*  80 */           .getCollectionReader(phraseAppNode, 
/*  80 */           Integer.parseInt(arrCollection[i]));
/*     */       }
/*  82 */       String wordDelimitor = getWordDelimitor(phraseAppNode.getString("notworddelimitor", "."));
/*  83 */       phraseExtract(indexFolder, maxSpan, arrCollectionReader, lemmatiser, tagger, wordDelimitor, 
/*  84 */         strength, peakZScore, spread, expandRatio, maxPhraseLength, phraseFile, vobFile);
/*     */     }
/*     */     else {
/*  87 */       phraseExtract(indexFolder, maxSpan, strength, peakZScore, spread, expandRatio, maxPhraseLength, phraseFile, vobFile);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void phraseExtract(String indexFolder, int maxSpan, double strength, double peakZScore, double spread, double expandRatio, int maxPhraseLength, String phraseFile, String vobFile)
/*     */   {
/*  94 */     SimpleXtract xtract = new SimpleXtract(maxSpan, indexFolder);
/*  95 */     xtract.extract(strength, spread, peakZScore, expandRatio, phraseFile);
/*  96 */     if (vobFile != null)
/*  97 */       generateVocabulary(phraseFile, maxPhraseLength, vobFile);
/*     */   }
/*     */ 
/*     */   public void phraseExtract(String indexFolder, int maxSpan, CollectionReader[] crs, Lemmatiser lemmatiser, Tagger tagger, String wordDelimitor, double strength, double peakZScore, double spread, double expandRatio, int maxPhraseLength, String phraseFile, String vobFile)
/*     */   {
/* 105 */     SimpleXtract xtract = new SimpleXtract(maxSpan, indexFolder);
/* 106 */     xtract.index(crs, tagger, lemmatiser, wordDelimitor);
/* 107 */     xtract.extract(strength, spread, peakZScore, expandRatio, phraseFile);
/* 108 */     if (vobFile != null)
/* 109 */       generateVocabulary(phraseFile, maxPhraseLength, vobFile);
/*     */   }
/*     */ 
/*     */   public void generateVocabulary(String phraseFile, int maxPhraseLen, String vobFile) {
/* 113 */     generateVocabulary(postProcessExtractedPhrase(phraseFile), maxPhraseLen, vobFile);
/*     */   }
/*     */ 
/*     */   private void generateVocabulary(ArrayList phraseList, int maxLen, String outputFile)
/*     */   {
/*     */     try
/*     */     {
/* 123 */       System.out.println(new Date().toString() + " Printing vocabulary file...");
/* 124 */       BufferedWriter bw = FileUtil.getTextWriter(outputFile);
/* 125 */       int min = 2147483647;
/* 126 */       int max = 0;
/* 127 */       ArrayList newList = new ArrayList(phraseList.size());
/* 128 */       for (int i = 0; i < phraseList.size(); i++) {
/* 129 */         int num = getTokenNum((String)phraseList.get(i));
/* 130 */         if (num <= maxLen) {
/* 131 */           newList.add(phraseList.get(i));
/* 132 */           if (num > max)
/* 133 */             max = num;
/* 134 */           if (num < min) {
/* 135 */             min = num;
/*     */           }
/*     */         }
/*     */       }
/* 139 */       bw.write(newList.size() + "\t" + min + "\t" + max + "\n");
/* 140 */       for (int i = 0; i < newList.size(); i++)
/*     */       {
/* 142 */         bw.write((String)newList.get(i));
/* 143 */         bw.write(9);
/* 144 */         bw.write(String.valueOf(i));
/* 145 */         bw.write(10);
/*     */       }
/* 147 */       bw.close();
/*     */     }
/*     */     catch (Exception e) {
/* 150 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private SortedArray postProcessExtractedPhrase(String phraseFile)
/*     */   {
/*     */     try
/*     */     {
/* 161 */       System.out.println(new Date().toString() + " Postprocessing Extracted Phrases...");
/* 162 */       SortedArray list = new SortedArray();
/* 163 */       BufferedReader br = FileUtil.getTextReader(phraseFile);
/* 164 */       br.readLine();
/*     */       String line;
/* 166 */       while ((line = br.readLine()) != null) {
/* 167 */         int pos = line.indexOf('\t');
/* 168 */         if (pos >= 0)
/* 169 */           line = line.substring(0, pos);
/* 170 */          line = postProcessPhrase(line);
/* 171 */         if (line.indexOf(' ') > 0)
/* 172 */           list.add(line);
/*     */       }
/* 174 */       return list;
/*     */     }
/*     */     catch (Exception e) {
/* 177 */       e.printStackTrace();
/* 178 */     }return null;
/*     */   }
/*     */ 
/*     */   private String postProcessPhrase(String content)
/*     */   {
/*     */     try
/*     */     {
/* 185 */       content = content.replace('-', ' ');
/* 186 */       content = content.replace('_', ' ');
/* 187 */       content = content.replace('\'', ' ');
/* 188 */       content = content.replaceAll("   ", " ");
/* 189 */       content = content.replaceAll("  ", " ");
/* 190 */       content = content.replaceAll("  ", " ");
/* 191 */       content = removePersonTitle(content);
/* 192 */       return content.toLowerCase();
/*     */     }
/*     */     catch (Exception e) {
/* 195 */       e.printStackTrace();
/* 196 */     }return null;
/*     */   }
/*     */ 
/*     */   private String removePersonTitle(String content)
/*     */   {
/* 203 */     content = content.trim();
/* 204 */     int pos = content.indexOf(' ');
/* 205 */     if ((pos > 0) && 
/* 206 */       (content.charAt(pos - 1) == '.') && (content.lastIndexOf('.', pos - 2) < 0)) {
/* 207 */       return removePersonTitle(content.substring(pos + 1));
/*     */     }
/*     */ 
/* 210 */     return content;
/*     */   }
/*     */ 
/*     */   private int getTokenNum(String term)
/*     */   {
/* 216 */     int count = 0;
/* 217 */     for (int i = 0; i < term.length(); i++)
/* 218 */       if (Character.isWhitespace(term.charAt(i)))
/* 219 */         count++;
/* 220 */     return count + 1;
/*     */   }
/*     */ 
/*     */   private String getWordDelimitor(String notWordDelimitor)
/*     */   {
/* 228 */     StringBuffer sb = new StringBuffer();
/* 229 */     String delimitors = " \r\n\t_-.;,?/\"'`:(){}!+[]><=%$#*@&^~|\\";
/* 230 */     if ((notWordDelimitor == null) && (notWordDelimitor.length() == 0))
/* 231 */       return delimitors;
/* 232 */     for (int i = 0; i < delimitors.length(); i++) {
/* 233 */       if (notWordDelimitor.indexOf(delimitors.charAt(i)) < 0)
/* 234 */         sb.append(delimitors.charAt(i));
/*     */     }
/* 236 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.PhraseExtractAppConfig
 * JD-Core Version:    0.6.2
 */