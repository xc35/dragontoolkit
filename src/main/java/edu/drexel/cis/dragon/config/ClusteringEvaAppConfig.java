/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import edu.drexel.cis.dragon.ir.clustering.Clustering;
/*     */ import edu.drexel.cis.dragon.ir.clustering.ClusteringEva;
/*     */ import edu.drexel.cis.dragon.ir.clustering.DocCluster;
/*     */ import edu.drexel.cis.dragon.ir.clustering.DocClusterSet;
/*     */ import edu.drexel.cis.dragon.ir.index.IRDoc;
/*     */ import edu.drexel.cis.dragon.ir.index.IndexReader;
/*     */ import edu.drexel.cis.dragon.nlp.SimpleElementList;
/*     */ import edu.drexel.cis.dragon.nlp.compare.IndexComparator;
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class ClusteringEvaAppConfig
/*     */ {
/*     */   private TreeMap map;
/*     */   private ArrayList labelList;
/*     */   private int maxCategory;
/*     */ 
/*     */   public ClusteringEvaAppConfig()
/*     */   {
/*  26 */     this.map = new TreeMap();
/*  27 */     this.maxCategory = 0;
/*  28 */     this.labelList = new ArrayList();
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  37 */     if (args.length != 2) {
/*  38 */       System.out.println("Please input two parameters: configuration xml file and clustering evaluation id");
/*  39 */       return;
/*     */     }
/*     */ 
/*  42 */     ConfigureNode root = new BasicConfigureNode(args[0]);
/*  43 */     ConfigUtil util = new ConfigUtil();
/*  44 */     ConfigureNode clusteringAppNode = util.getConfigureNode(root, "clusteringevaapp", Integer.parseInt(args[1]));
/*  45 */     if (clusteringAppNode == null)
/*  46 */       return;
/*  47 */     ClusteringEvaAppConfig clusteringApp = new ClusteringEvaAppConfig();
/*  48 */     String appName = clusteringAppNode.getNodeName();
/*  49 */     if (appName.equalsIgnoreCase("AgglomerativeEvaApp"))
/*  50 */       clusteringApp.evaAgglomerativeClustering(clusteringAppNode);
/*  51 */     else if (appName.equalsIgnoreCase("PartitionEvaApp"))
/*  52 */       clusteringApp.evaPartitionClustering(clusteringAppNode);
/*     */     else;
/*     */   }
/*     */ 
/*     */   public void evaAgglomerativeClustering(ConfigureNode node)
/*     */   {
/*  63 */     String docKeyFile = node.getString("dockeyfile");
/*     */     SimpleElementList docKeyList;
/*  64 */     if (docKeyFile == null)
/*  65 */       docKeyList = null;
/*     */     else
/*  67 */       docKeyList = new SimpleElementList(docKeyFile, false);
/*  68 */     int clusteringID = node.getInt("clustering");
/*  69 */     Clustering clusteringMethod = new ClusteringConfig().getClustering(node, clusteringID);
/*  70 */     String answerKey = node.getString("answerkey");
/*  71 */     String outputFile = node.getString("outputfile");
/*  72 */     String runName = node.getString("runname");
/*  73 */     evaAgglomerativeClustering(clusteringMethod, docKeyList, answerKey, runName, outputFile);
/*     */   }
/*     */ 
/*     */   public void evaAgglomerativeClustering(Clustering clusterMethod, String answerKeyFile, String runName, String outFile) {
/*  77 */     evaAgglomerativeClustering(clusterMethod, null, answerKeyFile, runName, outFile);
/*     */   }
/*     */ 
/*     */   public void evaAgglomerativeClustering(Clustering clusterMethod, SimpleElementList docKeyList, String answerKeyFile, String runName, String outFile)
/*     */   {
/*  85 */     this.map.clear();
/*  86 */     this.maxCategory = 0;
/*  87 */     this.labelList.clear();
/*  88 */     IRDoc[] arrDoc = getValidDocs(clusterMethod.getIndexReader(), docKeyList, answerKeyFile);
/*  89 */     DocClusterSet human = readHumanClusterSet(arrDoc, this.maxCategory);
/*  90 */     clusterMethod.cluster(arrDoc);
/*  91 */     DocClusterSet machine = clusterMethod.getClusterSet();
/*  92 */     for (int i = 0; i < machine.getClusterNum(); i++) {
/*  93 */       System.out.println(machine.getDocCluster(i).getDocNum());
/*     */     }
/*  95 */     printHeader(outFile);
/*  96 */     evaluate(machine, human, runName, outFile);
/*     */   }
/*     */ 
/*     */   public void evaPartitionClustering(ConfigureNode node)
/*     */   {
/* 105 */     String docKeyFile = node.getString("dockeyfile");
/*     */     SimpleElementList docKeyList;
/* 106 */     if (docKeyFile == null)
/* 107 */       docKeyList = null;
/*     */     else
/* 109 */       docKeyList = new SimpleElementList(docKeyFile, false);
/* 110 */     int clusteringID = node.getInt("clustering");
/* 111 */     Clustering clusteringMethod = new ClusteringConfig().getClustering(node, clusteringID);
/* 112 */     String answerKey = node.getString("answerkey");
/* 113 */     String outputFile = node.getString("outputfile");
/* 114 */     String runName = node.getString("runname");
/* 115 */     int run = node.getInt("run", 1);
/* 116 */     evaPartitionClustering(clusteringMethod, docKeyList, answerKey, run, runName, outputFile);
/*     */   }
/*     */ 
/*     */   public void evaPartitionClustering(Clustering clusterMethod, String answerKeyFile, int run, String runName, String outFile) {
/* 120 */     evaPartitionClustering(clusterMethod, null, answerKeyFile, run, runName, outFile);
/*     */   }
/*     */ 
/*     */   public void evaPartitionClustering(Clustering clusterMethod, SimpleElementList docKeyList, String answerKeyFile, int run, String runName, String outFile)
/*     */   {
/* 130 */     this.map.clear();
/* 131 */     this.maxCategory = 0;
/* 132 */     this.labelList.clear();
/* 133 */     long randomSeed = clusterMethod.getRandomSeed();
/* 134 */     if ((randomSeed < 0L) && (run > 1))
/* 135 */       randomSeed = 0L;
/* 136 */     IRDoc[] arrDoc = getValidDocs(clusterMethod.getIndexReader(), docKeyList, answerKeyFile);
/* 137 */     DocClusterSet human = readHumanClusterSet(arrDoc, this.maxCategory);
/* 138 */     printHeader(outFile);
/* 139 */     for (int i = 0; i < run; i++) {
/* 140 */       clusterMethod.setRandomSeed(randomSeed);
/* 141 */       clusterMethod.cluster(arrDoc);
/* 142 */       DocClusterSet machine = clusterMethod.getClusterSet();
/* 143 */       for (int j = 0; j < machine.getClusterNum(); j++)
/* 144 */         System.out.println(machine.getDocCluster(j).getDocNum());
/*     */       String curRunName;
/* 146 */       if (runName.length() == 0)
/* 147 */         curRunName = String.valueOf(randomSeed);
/*     */       else
/* 149 */         curRunName = runName + " " + String.valueOf(randomSeed);
/* 150 */       evaluate(machine, human, curRunName, outFile);
/* 151 */       randomSeed += 100L;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void evaluate(DocClusterSet machine, DocClusterSet human, String runName, String outFile)
/*     */   {
/* 158 */     ClusteringEva eva = new ClusteringEva();
/* 159 */     eva.evaluate(machine, human);
/* 160 */     System.out.println("Number of Clusters: " + human.getClusterNum());
/* 161 */     System.out.println("Entropy: " + eva.getEntropy());
/* 162 */     System.out.println("FScore: " + eva.getFScore());
/* 163 */     System.out.println("Purity:" + eva.getPurity());
/* 164 */     System.out.println("MutualInformation:" + eva.getMI());
/* 165 */     System.out.println("NMI:" + eva.getNMI());
/* 166 */     System.out.println("Geometry NMI:" + eva.getGeometryNMI());
/* 167 */     printResult(eva, runName, outFile);
/*     */   }
/*     */ 
/*     */   private void printResult(ClusteringEva ce, String runName, String outFile) {
/*     */     try {
/* 172 */       BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outFile), true));
/* 173 */       if ((runName != null) && (runName.length() > 0))
/* 174 */         bw.write(runName + "\t");
/* 175 */       bw.write(ce.getEntropy() + "\t" + ce.getFScore() + "\t" + ce.getPurity() + "\t" + ce.getMI() + "\t" + 
/* 176 */         ce.getNMI() + "\t" + ce.getGeometryNMI() + "\n");
/* 177 */       bw.flush();
/* 178 */       bw.close();
/*     */     }
/*     */     catch (Exception ex) {
/* 181 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void printHeader(String outputFile)
/*     */   {
/*     */     try {
/* 188 */       BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputFile), true));
/* 189 */       bw.write("Run\tEntropy\tFScore\tPurity\tMI\tNMI\tG-NMI\n");
/* 190 */       bw.flush();
/* 191 */       bw.close();
/*     */     }
/*     */     catch (Exception ex) {
/* 194 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private DocClusterSet readHumanClusterSet(IRDoc[] arrDoc, int clusterNum)
/*     */   {
/* 202 */     DocClusterSet docClusterSet = new DocClusterSet(clusterNum);
/* 203 */     for (int i = 0; i < arrDoc.length; i++)
/* 204 */       docClusterSet.addDoc(arrDoc[i].getCategory(), arrDoc[i]);
/* 205 */     for (int i = 0; i < clusterNum; i++)
/* 206 */       docClusterSet.getDocCluster(i).setClusterName((String)this.labelList.get(i));
/* 207 */     return docClusterSet;
/*     */   }
/*     */ 
/*     */   private IRDoc[] getValidDocs(IndexReader indexReader, SimpleElementList docKeyList, String answerKeyFile)
/*     */   {
/*     */     try
/*     */     {
/* 220 */       BufferedReader br = FileUtil.getTextReader(answerKeyFile);
/* 221 */       SortedArray answerKeyList = new SortedArray(Integer.parseInt(br.readLine()), new IndexComparator());
/*     */       String line;
/* 222 */       while ((line = br.readLine()) != null)
/*     */       {
/* 223 */         String[] arrTopic = line.split("\t");
/*     */         IRDoc irDoc;
/* 224 */         if (indexReader != null) {
/* 225 */           irDoc = indexReader.getDoc(arrTopic[1]);
/* 226 */           if (irDoc == null) continue; if (irDoc.getTermNum() < 1)
/* 227 */             continue;
/*     */         }
/*     */         else {
/* 230 */           irDoc = new IRDoc(arrTopic[1]);
/* 231 */           irDoc.setIndex(docKeyList.search(arrTopic[1]));
/*     */         }
/* 233 */         Integer curCategory = (Integer)this.map.get(arrTopic[0]);
/* 234 */         if (curCategory == null) {
/* 235 */           curCategory = new Integer(this.maxCategory);
/* 236 */           this.maxCategory += 1;
/* 237 */           this.map.put(arrTopic[0], curCategory);
/* 238 */           this.labelList.add(arrTopic[0]);
/*     */         }
/* 240 */         irDoc.setCategory(curCategory.intValue());
/* 241 */         answerKeyList.add(irDoc);
/*     */       }
/* 243 */       br.close();
/* 244 */       IRDoc[] arrDoc = new IRDoc[answerKeyList.size()];
/* 245 */       for (int i = 0; i < answerKeyList.size(); i++)
/* 246 */         arrDoc[i] = ((IRDoc)answerKeyList.get(i));
/* 247 */       return arrDoc;
/*     */     }
/*     */     catch (Exception ex) {
/* 250 */       ex.printStackTrace();
/* 251 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.ClusteringEvaAppConfig
 * JD-Core Version:    0.6.2
 */