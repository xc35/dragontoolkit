/*    */ package edu.drexel.cis.dragon.config;
/*    */ 
/*    */ import edu.drexel.cis.dragon.nlp.tool.Annie;
/*    */ import edu.drexel.cis.dragon.nlp.tool.NER;
/*    */ 
/*    */ public class NERConfig extends ConfigUtil
/*    */ {
/*    */   public NERConfig()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NERConfig(ConfigureNode root)
/*    */   {
/* 20 */     super(root);
/*    */   }
/*    */ 
/*    */   public NERConfig(String configFile) {
/* 24 */     super(configFile);
/*    */   }
/*    */ 
/*    */   public NER getNER(int taggerID) {
/* 28 */     return getNER(this.root, taggerID);
/*    */   }
/*    */ 
/*    */   public NER getNER(ConfigureNode node, int nerID) {
/* 32 */     return loadNER(node, nerID);
/*    */   }
/*    */ 
/*    */   private NER loadNER(ConfigureNode node, int nerID)
/*    */   {
/* 38 */     ConfigureNode nerNode = getConfigureNode(node, "ner", nerID);
/* 39 */     if (nerNode == null) {
/* 40 */       return null;
/*    */     }
/* 42 */     String nerName = nerNode.getNodeName();
/* 43 */     return loadNER(nerName, nerNode);
/*    */   }
/*    */ 
/*    */   protected NER loadNER(String nerName, ConfigureNode nerNode) {
/* 47 */     if (nerName.equalsIgnoreCase("Annie")) {
/* 48 */       return loadAnnie(nerNode);
/*    */     }
/* 50 */     return (NER)loadResource(nerNode);
/*    */   }
/*    */ 
/*    */   private Annie loadAnnie(ConfigureNode curNode)
/*    */   {
/*    */     try
/*    */     {
/* 58 */       String gateHome = curNode.getString("gatehome", null);
/* 59 */       String annotationTypes = curNode.getString("entitytypes", "Person;Location;Organization");
/*    */       Annie ner;
/* 61 */       if (gateHome == null)
/* 62 */         ner = new Annie();
/*    */       else
/* 64 */         ner = new Annie(gateHome);
/* 65 */       ner.setAnnotationTypes(annotationTypes.split(";"));
/* 66 */       return ner;
/*    */     }
/*    */     catch (Exception e) {
/* 69 */       e.printStackTrace();
/* 70 */     }return null;
/*    */   }
/*    */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.NERConfig
 * JD-Core Version:    0.6.2
 */