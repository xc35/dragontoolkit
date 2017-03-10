/*     */ package edu.drexel.cis.dragon.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class SortedArray extends ArrayList
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private Comparator comparator;
/*     */   private int insertedPos;
/*     */ 
/*     */   public SortedArray(int capacity, Comparator comparator)
/*     */   {
/*  20 */     super(capacity);
/*  21 */     this.comparator = comparator;
/*     */   }
/*     */ 
/*     */   public SortedArray(int capacity) {
/*  25 */     super(capacity);
/*  26 */     this.comparator = null;
/*     */   }
/*     */ 
/*     */   public SortedArray(Comparator comparator)
/*     */   {
/*  32 */     this.comparator = comparator;
/*     */   }
/*     */ 
/*     */   public SortedArray()
/*     */   {
/*  37 */     this.comparator = null;
/*     */   }
/*     */ 
/*     */   public int insertedPos() {
/*  41 */     return this.insertedPos;
/*     */   }
/*     */ 
/*     */   public boolean add(Object key)
/*     */   {
/*     */     int pos;
/*  47 */     if (this.comparator == null)
/*  48 */       pos = Collections.binarySearch(this, key);
/*     */     else
/*  50 */       pos = Collections.binarySearch(this, key, this.comparator);
/*  51 */     if (pos < 0) {
/*  52 */       this.insertedPos = (pos * -1 - 1);
/*  53 */       super.add(this.insertedPos, key);
/*  54 */       return true;
/*     */     }
/*     */ 
/*  57 */     this.insertedPos = pos;
/*  58 */     return false;
/*     */   }
/*     */ 
/*     */   public int binarySearch(Object key)
/*     */   {
/*  63 */     if (this.comparator == null) {
/*  64 */       return Collections.binarySearch(this, key);
/*     */     }
/*  66 */     return Collections.binarySearch(this, key, this.comparator);
/*     */   }
/*     */ 
/*     */   public int binarySearch(Object key, int start) {
/*  70 */     return binarySearch(this, key, start, size() - 1, this.comparator);
/*     */   }
/*     */ 
/*     */   public int binarySearch(Object key, int start, int end) {
/*  74 */     return binarySearch(this, key, start, end, this.comparator);
/*     */   }
/*     */ 
/*     */   public boolean contains(Object key)
/*     */   {
/*     */     int pos;
/*  80 */     if (this.comparator == null)
/*  81 */       pos = Collections.binarySearch(this, key);
/*     */     else
/*  83 */       pos = Collections.binarySearch(this, key, this.comparator);
/*  84 */     return pos >= 0;
/*     */   }
/*     */ 
/*     */   public SortedArray copy(Comparator comparator)
/*     */   {
/*  90 */     SortedArray newList = new SortedArray();
/*  91 */     newList.addAll(this);
/*  92 */     newList.setComparator(comparator);
/*  93 */     return newList;
/*     */   }
/*     */ 
/*     */   public SortedArray copy() {
/*  97 */     return copy(null);
/*     */   }
/*     */ 
/*     */   public Comparator getComparator() {
/* 101 */     return this.comparator;
/*     */   }
/*     */ 
/*     */   public void setComparator(Comparator comparator) {
/* 105 */     this.comparator = comparator;
/* 106 */     if (comparator == null)
/* 107 */       Collections.sort(this);
/*     */     else
/* 109 */       Collections.sort(this, comparator);
/*     */   }
/*     */ 
/*     */   public static int binarySearch(List list, Object obj) {
/* 113 */     return binarySearch(list, obj, 0, list.size() - 1, null);
/*     */   }
/*     */ 
/*     */   public static int binarySearch(List list, Object obj, Comparator comparator) {
/* 117 */     return binarySearch(list, obj, 0, list.size() - 1, comparator);
/*     */   }
/*     */ 
/*     */   public static int binarySearch(List list, Object obj, int start, int end) {
/* 121 */     return binarySearch(list, obj, start, end, null);
/*     */   }
/*     */ 
/*     */   public static int binarySearch(List list, Object obj, int start, int end, Comparator comparator)
/*     */   {
/* 127 */     while (start <= end)
/*     */     {
/* 129 */       int middle = (start + end) / 2;
/*     */       int retvalue;
/* 130 */       if (comparator == null)
/* 131 */         retvalue = ((Comparable)obj).compareTo(list.get(middle));
/*     */       else
/* 133 */         retvalue = comparator.compare(obj, list.get(middle));
/* 134 */       if (retvalue == 0)
/* 135 */         return middle;
/* 136 */       if (retvalue > 0)
/* 137 */         start = middle + 1;
/*     */       else
/* 139 */         end = middle - 1;
/*     */     }
/* 141 */     return -(start + 1);
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.util.SortedArray
 * JD-Core Version:    0.6.2
 */