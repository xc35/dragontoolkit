/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.tool.HeppleTagger;
/*    */ import edu.drexel.cis.dragon.nlp.tool.MedPostTagger;
/*    */ import edu.drexel.cis.dragon.nlp.tool.Tagger;
/*    */ 
/*    */ public class TaggerConfig extends ConfigUtil
/*    */ {
/*    */   public TaggerConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public TaggerConfig(ConfigureNode root)
/*    */   {
/* 20 */     super(root);
/*    */   }
/*    */ 
/*    */   public TaggerConfig(String configFile) {
/* 24 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public Tagger getTagger(int taggerID) {
/* 28 */     return getTagger(this.root, taggerID);
/*    */   }
/*    */ 
/*    */   public Tagger getTagger(ConfigureNode node, int taggerID) {
/* 32 */     return loadTagger(node, taggerID);
/*    */   }
/*    */ 
/*    */   private Tagger loadTagger(ConfigureNode node, int taggerID)
/*    */   {
/* 39 */     ConfigureNode taggerNode = getConfigureNode(node, "tagger", taggerID);
/* 40 */     if (taggerNode == null)
/* 41 */       return null;
/* 42 */     String taggerName = taggerNode.getNodeName();
/* 43 */     return loadTagger(taggerName, taggerNode);
/*    */   }
/*    */ 
/*    */   protected Tagger loadTagger(String taggerName, ConfigureNode taggerNode)
/*    */   {
/* 49 */     String dataDir = taggerNode.getString("directory", null);
/* 50 */     if (taggerName.equalsIgnoreCase("MedPostTagger")) {
/* 51 */       if (dataDir == null) {
/* 52 */         return new MedPostTagger();
/*    */       }
/* 54 */       return new MedPostTagger(dataDir);
/*    */     }
/* 56 */     if (taggerName.equalsIgnoreCase("HeppleTagger")) {
/* 57 */       if (dataDir == null) {
/* 58 */         return new HeppleTagger();
/*    */       }
/* 60 */       return new HeppleTagger(dataDir);
/*    */     }
/*    */ 
/* 63 */     return (Tagger)loadResource(taggerNode);
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.TaggerConfig
 * JD-Core Version:    0.6.2
 */