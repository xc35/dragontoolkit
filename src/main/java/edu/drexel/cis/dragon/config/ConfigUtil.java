/*     */ package edu.drexel.cis.dragon.config;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ public class ConfigUtil
/*     */ {
/*     */   protected ConfigureNode root;
/*     */ 
/*     */   public ConfigUtil()
/*     */   {
/*  17 */     this.root = null;
/*     */   }
/*     */ 
/*     */   public ConfigUtil(ConfigureNode root) {
/*  21 */     this.root = root;
/*     */   }
/*     */ 
/*     */   public ConfigUtil(String configFile) {
/*  25 */     this.root = new BasicConfigureNode(configFile);
/*     */   }
/*     */ 
/*     */   public ConfigureNode getConfigureNode(ConfigureNode curNode, String nodeType, int nodeID)
/*     */   {
/*  31 */     if ((curNode.getNodeType().equalsIgnoreCase(nodeType)) && (curNode.getNodeID() == nodeID)) {
/*  32 */       return curNode;
/*     */     }
/*     */ 
/*  35 */     ConfigureNode nextNode = curNode.getFirstChild();
/*  36 */     while (nextNode != null) {
/*  37 */       if ((nextNode.getNodeType().equalsIgnoreCase(nodeType)) && (nextNode.getNodeID() == nodeID))
/*  38 */         return nextNode;
/*  39 */       nextNode = nextNode.getNextSibling();
/*     */     }
/*     */ 
/*  43 */     nextNode = curNode.getParentNode();
/*  44 */     if (nextNode != null) {
/*  45 */       nextNode = nextNode.getFirstChild();
/*  46 */       while (nextNode != null) {
/*  47 */         if ((nextNode.getNodeType().equalsIgnoreCase(nodeType)) && (nextNode.getNodeID() == nodeID))
/*  48 */           return nextNode;
/*  49 */         nextNode = nextNode.getNextSibling();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  54 */     if (this.root == null) {
/*  55 */       nextNode = curNode.getParentNode();
/*  56 */       if (nextNode == null)
/*  57 */         return null;
/*  58 */       while (nextNode != null) {
/*  59 */         this.root = nextNode;
/*  60 */         nextNode = nextNode.getParentNode();
/*     */       }
/*     */     }
/*  63 */     nextNode = this.root.getFirstChild();
/*  64 */     while (nextNode != null) {
/*  65 */       if ((nextNode.getNodeType().equalsIgnoreCase(nodeType)) && (nextNode.getNodeID() == nodeID))
/*  66 */         return nextNode;
/*  67 */       nextNode = nextNode.getNextSibling();
/*     */     }
/*  69 */     return null;
/*     */   }
/*     */ 
/*     */   public Object loadResource(ConfigureNode node)
/*     */   {
/*     */     try
/*     */     {
/*  80 */       Class curClass = node.getNodeClass();
/*  81 */       if (curClass == null)
/*  82 */         return null;
/*  83 */       String shortClassName = curClass.getName();
/*  84 */       if (shortClassName.lastIndexOf('.') > 0)
/*  85 */         shortClassName = shortClassName.substring(shortClassName.lastIndexOf('.') + 1);
/*  86 */       String methodName = "get" + shortClassName;
/*  87 */       Class[] params = new Class[1];
/*  88 */       params[0] = Class.forName("dragon.config.ConfigureNode");
/*  89 */       Method method = curClass.getMethod(methodName, params);
/*  90 */       if (method == null) {
/*  91 */         System.out.println("Please define the method in class " + curClass.getName() + ": public static " + shortClassName + 
/*  92 */           " " + methodName + "(ConfigureNode node)");
/*  93 */         return null;
/*     */       }
/*  95 */       int modifierCode = method.getModifiers();
/*  96 */       if ((!Modifier.isPublic(modifierCode)) || (!Modifier.isStatic(modifierCode))) {
/*  97 */         System.out.println("The method " + methodName + " should be defined as public and static");
/*  98 */         return null;
/*     */       }
/* 100 */       Object[] objParams = new Object[1];
/* 101 */       objParams[0] = node;
/* 102 */       return method.invoke(curClass, objParams);
/*     */     }
/*     */     catch (Exception e) {
/* 105 */       e.printStackTrace();
/* 106 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.config.ConfigUtil
 * JD-Core Version:    0.6.2
 */