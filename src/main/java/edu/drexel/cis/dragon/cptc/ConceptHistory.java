/*     */ package edu.drexel.cis.dragon.cptc;
/*     */ 
/*     */ import edu.drexel.cis.dragon.util.FileUtil;
/*     */ import edu.drexel.cis.dragon.util.SortedArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ 
/*     */ public class ConceptHistory
/*     */ {
/*     */   private SortedArray list;
/*     */   private boolean modified;
/*     */   private String filename;
/*     */ 
/*     */   public ConceptHistory(String filename)
/*     */   {
/*  12 */     this.modified = false;
/*  13 */     this.filename = filename;
/*  14 */     this.list = load(filename);
/*     */   }
/*     */ 
/*     */   public boolean add(ConceptCategory concept)
/*     */   {
/*  20 */     boolean ret = this.list.add(concept);
/*  21 */     if (ret)
/*  22 */       this.modified = true;
/*  23 */     return ret;
/*     */   }
/*     */ 
/*     */   public boolean delete(String concept)
/*     */   {
/*  29 */     int pos = this.list.binarySearch(new ConceptCategory(concept));
/*  30 */     if (pos < 0) {
/*  31 */       return false;
/*     */     }
/*  33 */     this.list.remove(pos);
/*  34 */     this.modified = true;
/*  35 */     return true;
/*     */   }
/*     */ 
/*     */   public ConceptCategory search(String concept)
/*     */   {
/*  42 */     int pos = this.list.binarySearch(new ConceptCategory(concept));
/*  43 */     if (pos < 0) {
/*  44 */       return null;
/*     */     }
/*  46 */     return (ConceptCategory)this.list.get(pos);
/*     */   }
/*     */ 
/*     */   public void close() {
/*  50 */     if (this.modified)
/*  51 */       save(this.filename);
/*     */   }
/*     */ 
/*     */   private void save(String filename)
/*     */   {
/*     */     try
/*     */     {
/*  61 */       BufferedWriter bw = FileUtil.getTextWriter(filename);
/*  62 */       bw.write(this.list.size() + "\n");
/*  63 */       for (int i = 0; i < this.list.size(); i++) {
/*  64 */         ConceptCategory concept = (ConceptCategory)this.list.get(i);
/*  65 */         bw.write(concept.getConcept());
/*  66 */         bw.write(9);
/*  67 */         if (concept.getPrediction() != null) {
/*  68 */           String[] arrLabel = concept.getPrediction();
/*  69 */           for (int j = 0; j < arrLabel.length; j++) {
/*  70 */             bw.write(arrLabel[j]);
/*  71 */             if (j < arrLabel.length - 1)
/*  72 */               bw.write(59);
/*     */           }
/*     */         }
/*  75 */         bw.write(9);
/*  76 */         if (concept.getActual() != null)
/*  77 */           bw.write(concept.getActual());
/*  78 */         bw.write(10);
/*     */       }
/*  80 */       bw.close();
/*     */     }
/*     */     catch (Exception e) {
/*  83 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private SortedArray load(String filename)
/*     */   {
/*  93 */     if (!FileUtil.exist(filename))
/*  94 */       return new SortedArray();
/*     */     try
/*     */     {
/*  97 */       BufferedReader br = FileUtil.getTextReader(filename);
/*  98 */       String line = br.readLine();
/*  99 */       if (br == null)
/* 100 */         return new SortedArray();
/* 101 */       SortedArray list = new SortedArray(Integer.parseInt(line));
/* 102 */       while ((line = br.readLine()) != null) {
/* 103 */         String[] arrField = line.split("\t");
/* 104 */         ConceptCategory concept = new ConceptCategory(arrField[0]);
/* 105 */         if ((arrField.length > 1) && (arrField[1] != null)) {
/* 106 */           concept.setPrediction(arrField[1].split(";"));
/*     */         }
/* 108 */         if ((arrField.length > 2) && (arrField[2] != null)) {
/* 109 */           concept.setActual(arrField[2]);
/*     */         }
/* 111 */         list.add(concept);
/*     */       }
/* 113 */       return list;
/*     */     }
/*     */     catch (Exception e) {
/* 116 */       e.printStackTrace();
/* 117 */     }return new SortedArray();
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.cptc.ConceptHistory
 * JD-Core Version:    0.6.2
 */