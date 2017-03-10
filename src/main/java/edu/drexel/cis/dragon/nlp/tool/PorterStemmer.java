/*     */ package edu.drexel.cis.dragon.nlp.tool;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class PorterStemmer
/*     */   implements Lemmatiser
/*     */ {
/*     */   private char[] b;
/*     */   private int i;
/*     */   private int i_end;
/*     */   private int j;
/*     */   private int k;
/*     */   private static final int INC = 50;
/*     */ 
/*     */   public PorterStemmer()
/*     */   {
/*  21 */     this.b = new char[50];
/*  22 */     this.i = 0;
/*  23 */     this.i_end = 0;
/*     */   }
/*     */ 
/*     */   public String stem(String word)
/*     */   {
/*  29 */     for (int i = 0; i < word.length(); i++)
/*  30 */       add(word.charAt(i));
/*  31 */     stem();
/*  32 */     return toString();
/*     */   }
/*     */ 
/*     */   public String lemmatize(String word) {
/*  36 */     return stem(word);
/*     */   }
/*     */ 
/*     */   public String lemmatize(String word, int POS) {
/*  40 */     return stem(word);
/*     */   }
/*     */ 
/*     */   public void add(char ch)
/*     */   {
/*  50 */     if (this.i == this.b.length) {
/*  51 */       char[] new_b = new char[this.i + 50];
/*  52 */       for (int c = 0; c < this.i; c++) {
/*  53 */         new_b[c] = this.b[c];
/*     */       }
/*  55 */       this.b = new_b;
/*     */     }
/*  57 */     this.b[(this.i++)] = ch;
/*     */   }
/*     */ 
/*     */   public void add(char[] w, int wLen)
/*     */   {
/*  66 */     if (this.i + wLen >= this.b.length) {
/*  67 */       char[] new_b = new char[this.i + wLen + 50];
/*  68 */       for (int c = 0; c < this.i; c++) {
/*  69 */         new_b[c] = this.b[c];
/*     */       }
/*  71 */       this.b = new_b;
/*     */     }
/*  73 */     for (int c = 0; c < wLen; c++)
/*  74 */       this.b[(this.i++)] = w[c];
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  84 */     return new String(this.b, 0, this.i_end);
/*     */   }
/*     */ 
/*     */   public int getResultLength()
/*     */   {
/*  91 */     return this.i_end;
/*     */   }
/*     */ 
/*     */   public char[] getResultBuffer()
/*     */   {
/* 100 */     return this.b;
/*     */   }
/*     */ 
/*     */   private final boolean cons(int i)
/*     */   {
/* 106 */     switch (this.b[i]) {
/*     */     case 'a':
/*     */     case 'e':
/*     */     case 'i':
/*     */     case 'o':
/*     */     case 'u':
/* 112 */       return false;
/*     */     case 'y':
/* 114 */       return i == 0;
/*     */     }
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */   private final int m()
/*     */   {
/* 131 */     int n = 0;
/* 132 */     int i = 0;
/*     */     while (true) {
/* 134 */       if (i > this.j) {
/* 135 */         return n;
/*     */       }
/* 137 */       if (!cons(i)) {
/*     */         break;
/*     */       }
/* 140 */       i++;
/*     */     }
/* 142 */     i++;
/*     */     while (true)
/*     */     {
/* 145 */       if (i > this.j) {
/* 146 */         return n;
/*     */       }
/* 148 */       if (!cons(i))
/*     */       {
/* 151 */         i++;
/*     */       } else {
/* 153 */         i++;
/* 154 */         n++;
/*     */         while (true) {
/* 156 */           if (i > this.j) {
/* 157 */             return n;
/*     */           }
/* 159 */           if (!cons(i)) {
/*     */             break;
/*     */           }
/* 162 */           i++;
/*     */         }
/* 164 */         i++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private final boolean vowelinstem()
/*     */   {
/* 172 */     for (int i = 0; i <= this.j; i++) {
/* 173 */       if (!cons(i)) {
/* 174 */         return true;
/*     */       }
/*     */     }
/* 177 */     return false;
/*     */   }
/*     */ 
/*     */   private final boolean doublec(int j)
/*     */   {
/* 183 */     if (j < 1) {
/* 184 */       return false;
/*     */     }
/* 186 */     if (this.b[j] != this.b[(j - 1)]) {
/* 187 */       return false;
/*     */     }
/* 189 */     return cons(j);
/*     */   }
/*     */ 
/*     */   private final boolean cvc(int i)
/*     */   {
/* 200 */     if ((i < 2) || (!cons(i)) || (cons(i - 1)) || (!cons(i - 2))) {
/* 201 */       return false;
/*     */     }
/*     */ 
/* 204 */     int ch = this.b[i];
/* 205 */     if ((ch == 119) || (ch == 120) || (ch == 121)) {
/* 206 */       return false;
/*     */     }
/*     */ 
/* 209 */     return true;
/*     */   }
/*     */ 
/*     */   private final boolean ends(String s) {
/* 213 */     int l = s.length();
/* 214 */     int o = this.k - l + 1;
/* 215 */     if (o < 0) {
/* 216 */       return false;
/*     */     }
/* 218 */     for (int i = 0; i < l; i++) {
/* 219 */       if (this.b[(o + i)] != s.charAt(i)) {
/* 220 */         return false;
/*     */       }
/*     */     }
/* 223 */     this.j = (this.k - l);
/* 224 */     return true;
/*     */   }
/*     */ 
/*     */   private final void setto(String s)
/*     */   {
/* 231 */     int l = s.length();
/* 232 */     int o = this.j + 1;
/* 233 */     for (int i = 0; i < l; i++) {
/* 234 */       this.b[(o + i)] = s.charAt(i);
/*     */     }
/* 236 */     this.k = (this.j + l);
/*     */   }
/*     */ 
/*     */   private final void r(String s)
/*     */   {
/* 242 */     if (m() > 0)
/* 243 */       setto(s);
/*     */   }
/*     */ 
/*     */   private final void step1()
/*     */   {
/* 265 */     if (this.b[this.k] == 's') {
/* 266 */       if (ends("sses")) {
/* 267 */         this.k -= 2;
/*     */       }
/* 270 */       else if (ends("ies")) {
/* 271 */         setto("i");
/*     */       }
/* 274 */       else if (this.b[(this.k - 1)] != 's') {
/* 275 */         this.k -= 1;
/*     */       }
/*     */     }
/* 278 */     if (ends("eed")) {
/* 279 */       if (m() > 0) {
/* 280 */         this.k -= 1;
/*     */       }
/*     */ 
/*     */     }
/* 284 */     else if (((ends("ed")) || (ends("ing"))) && (vowelinstem())) {
/* 285 */       this.k = this.j;
/* 286 */       if (ends("at")) {
/* 287 */         setto("ate");
/*     */       }
/* 290 */       else if (ends("bl")) {
/* 291 */         setto("ble");
/*     */       }
/* 294 */       else if (ends("iz")) {
/* 295 */         setto("ize");
/*     */       }
/* 298 */       else if (doublec(this.k)) {
/* 299 */         this.k -= 1;
/*     */ 
/* 301 */         int ch = this.b[this.k];
/* 302 */         if ((ch == 108) || (ch == 115) || (ch == 122)) {
/* 303 */           this.k += 1;
/*     */         }
/*     */ 
/*     */       }
/* 307 */       else if ((m() == 1) && (cvc(this.k))) {
/* 308 */         setto("e");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void step2()
/*     */   {
/* 316 */     if ((ends("y")) && (vowelinstem()))
/* 317 */       this.b[this.k] = 'i';
/*     */   }
/*     */ 
/*     */   private final void step3()
/*     */   {
/* 326 */     if (this.k == 0) {
/* 327 */       return;
/*     */     }
/* 329 */     switch (this.b[(this.k - 1)]) {
/*     */     case 'a':
/* 331 */       if (ends("ational")) {
/* 332 */         r("ate");
/*     */       }
/* 335 */       else if (ends("tional"))
/* 336 */         r("tion");
/* 337 */       break;
/*     */     case 'c':
/* 341 */       if (ends("enci")) {
/* 342 */         r("ence");
/*     */       }
/* 345 */       else if (ends("anci"))
/* 346 */         r("ance");
/* 347 */       break;
/*     */     case 'e':
/* 351 */       if (ends("izer"))
/* 352 */         r("ize");
/* 353 */       break;
/*     */     case 'l':
/* 357 */       if (ends("bli")) {
/* 358 */         r("ble");
/*     */       }
/* 361 */       else if (ends("alli")) {
/* 362 */         r("al");
/*     */       }
/* 365 */       else if (ends("entli")) {
/* 366 */         r("ent");
/*     */       }
/* 369 */       else if (ends("eli")) {
/* 370 */         r("e");
/*     */       }
/* 373 */       else if (ends("ousli"))
/* 374 */         r("ous");
/* 375 */       break;
/*     */     case 'o':
/* 379 */       if (ends("ization")) {
/* 380 */         r("ize");
/*     */       }
/* 383 */       else if (ends("ation")) {
/* 384 */         r("ate");
/*     */       }
/* 387 */       else if (ends("ator"))
/* 388 */         r("ate");
/* 389 */       break;
/*     */     case 's':
/* 393 */       if (ends("alism")) {
/* 394 */         r("al");
/*     */       }
/* 397 */       else if (ends("iveness")) {
/* 398 */         r("ive");
/*     */       }
/* 401 */       else if (ends("fulness")) {
/* 402 */         r("ful");
/*     */       }
/* 405 */       else if (ends("ousness"))
/* 406 */         r("ous");
/* 407 */       break;
/*     */     case 't':
/* 411 */       if (ends("aliti")) {
/* 412 */         r("al");
/*     */       }
/* 415 */       else if (ends("iviti")) {
/* 416 */         r("ive");
/*     */       }
/* 419 */       else if (ends("biliti"))
/* 420 */         r("ble");
/* 421 */       break;
/*     */     case 'g':
/* 425 */       if (ends("logi"))
/* 426 */         r("log"); break;
/*     */     case 'b':
/*     */     case 'd':
/*     */     case 'f':
/*     */     case 'h':
/*     */     case 'i':
/*     */     case 'j':
/*     */     case 'k':
/*     */     case 'm':
/*     */     case 'n':
/*     */     case 'p':
/*     */     case 'q':
/*     */     case 'r': }  } 
/* 435 */   private final void step4() { switch (this.b[this.k]) {
/*     */     case 'e':
/* 437 */       if (ends("icate")) {
/* 438 */         r("ic");
/*     */       }
/* 441 */       else if (ends("ative")) {
/* 442 */         r("");
/*     */       }
/* 445 */       else if (ends("alize"))
/* 446 */         r("al");
/* 447 */       break;
/*     */     case 'i':
/* 451 */       if (ends("iciti"))
/* 452 */         r("ic");
/* 453 */       break;
/*     */     case 'l':
/* 457 */       if (ends("ical")) {
/* 458 */         r("ic");
/*     */       }
/* 461 */       else if (ends("ful"))
/* 462 */         r("");
/* 463 */       break;
/*     */     case 's':
/* 467 */       if (ends("ness"))
/* 468 */         r("");
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void step5()
/*     */   {
/* 478 */     if (this.k == 0) {
/* 479 */       return;
/*     */     }
/* 481 */     switch (this.b[(this.k - 1)]) {
/*     */     case 'a':
/* 483 */       if (!ends("al")) {
/*     */         return;
/*     */       }
/*     */       break;
/*     */     case 'c':
/* 488 */       if (!ends("ance"))
/*     */       {
/* 491 */         if (!ends("ence"))
/*     */           return;
/*     */       }
/*     */       break;
/*     */     case 'e':
/* 496 */       if (!ends("er")) {
/*     */         return;
/*     */       }
/*     */       break;
/*     */     case 'i':
/* 501 */       if (!ends("ic")) {
/*     */         return;
/*     */       }
/*     */       break;
/*     */     case 'l':
/* 506 */       if (!ends("able"))
/*     */       {
/* 509 */         if (!ends("ible"))
/*     */           return;
/*     */       }
/*     */       break;
/*     */     case 'n':
/* 514 */       if (!ends("ant"))
/*     */       {
/* 517 */         if (!ends("ement"))
/*     */         {
/* 520 */           if (!ends("ment"))
/*     */           {
/* 525 */             if (!ends("ent")) return; 
/*     */           }
/*     */         }
/*     */       }
/*     */       break;
/*     */     case 'o':
/* 530 */       if ((!ends("ion")) || (this.j < 0) || ((this.b[this.j] != 's') && (this.b[this.j] != 't')))
/*     */       {
/* 535 */         if (!ends("ou")) {
/*     */           return;
/*     */         }
/*     */       }
/*     */       break;
/*     */     case 's':
/* 541 */       if (!ends("ism")) {
/*     */         return;
/*     */       }
/*     */       break;
/*     */     case 't':
/* 546 */       if (!ends("ate"))
/*     */       {
/* 549 */         if (!ends("iti"))
/*     */           return;
/*     */       }
/*     */       break;
/*     */     case 'u':
/* 554 */       if (!ends("ous")) {
/*     */         return;
/*     */       }
/*     */       break;
/*     */     case 'v':
/* 559 */       if (!ends("ive")) {
/*     */         return;
/*     */       }
/*     */       break;
/*     */     case 'z':
/* 564 */       if (!ends("ize")) return; break;
/*     */     case 'b':
/*     */     case 'd':
/*     */     case 'f':
/*     */     case 'g':
/*     */     case 'h':
/*     */     case 'j':
/*     */     case 'k':
/*     */     case 'm':
/*     */     case 'p':
/*     */     case 'q':
/*     */     case 'r':
/*     */     case 'w':
/*     */     case 'x':
/*     */     case 'y':
/*     */     default:
/* 569 */       return;
/*     */     }
/* 571 */     if (m() > 1)
/* 572 */       this.k = this.j;
/*     */   }
/*     */ 
/*     */   private final void step6()
/*     */   {
/* 579 */     this.j = this.k;
/* 580 */     if (this.b[this.k] == 'e') {
/* 581 */       int a = m();
/* 582 */       if ((a > 1) || ((a == 1) && (!cvc(this.k - 1)))) {
/* 583 */         this.k -= 1;
/*     */       }
/*     */     }
/* 586 */     if ((this.b[this.k] == 'l') && (doublec(this.k)) && (m() > 1))
/* 587 */       this.k -= 1;
/*     */   }
/*     */ 
/*     */   public void stem()
/*     */   {
/* 597 */     this.k = (this.i - 1);
/* 598 */     if (this.k > 1) {
/* 599 */       step1();
/* 600 */       step2();
/* 601 */       step3();
/* 602 */       step4();
/* 603 */       step5();
/* 604 */       step6();
/*     */     }
/* 606 */     this.i_end = (this.k + 1);
/* 607 */     this.i = 0;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 617 */     char[] w = new char[501];
/* 618 */     PorterStemmer s = new PorterStemmer();
/* 619 */     for (int i = 0; i < args.length; i++)
/*     */       try {
/* 621 */         FileInputStream in = new FileInputStream(args[i]);
/*     */         try
/*     */         {
/*     */           while (true)
/*     */           {
/* 627 */             int ch = in.read();
/* 628 */             if (Character.isLetter((char)ch)) {
/* 629 */               int j = 0;
/*     */               do {
/* 631 */                 ch = Character.toLowerCase((char)ch);
/* 632 */                 w[j] = ((char)ch);
/* 633 */                 if (j < 500) {
/* 634 */                   j++;
/*     */                 }
/* 636 */                 ch = in.read();
/* 637 */               }while (Character.isLetter((char)ch));
/*     */ 
/* 639 */               for (int c = 0; c < j; c++) {
/* 640 */                 s.add(w[c]);
/*     */               }
/*     */ 
/* 646 */               s.stem();
/*     */ 
/* 651 */               String u = s.toString();
/*     */ 
/* 656 */               System.out.print(u);
/*     */             }
/*     */ 
/* 662 */             if (ch < 0) {
/*     */               break;
/*     */             }
/* 665 */             System.out.print((char)ch);
/*     */           }
/*     */         }
/*     */         catch (IOException e) {
/* 669 */           System.out.println("error reading " + args[i]);
/*     */         }
/*     */       }
/*     */       catch (FileNotFoundException e)
/*     */       {
/* 674 */         System.out.println("file " + args[i] + " not found");
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.nlp.tool.PorterStemmer
 * JD-Core Version:    0.6.2
 */