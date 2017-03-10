/*     */ package edu.drexel.cis.dragon.ml.seqmodel.crf;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class LBFGS
/*     */ {
/*  96 */   public static double gtol = 0.9D;
/*     */ 
/* 105 */   public static double stpmin = 1.0E-020D;
/*     */ 
/* 114 */   public static double stpmax = 1.0E+020D;
/*     */ 
/* 125 */   public static double[] solution_cache = null;
/*     */ 
/* 127 */   private static double gnorm = 0.0D; private static double stp1 = 0.0D; private static double ftol = 0.0D; private static double[] stp = new double[1]; private static double ys = 0.0D; private static double yy = 0.0D; private static double sq = 0.0D; private static double yr = 0.0D; private static double beta = 0.0D; private static double xnorm = 0.0D;
/* 128 */   private static int iter = 0; private static int nfun = 0; private static int point = 0; private static int ispt = 0; private static int iypt = 0; private static int maxfev = 0; private static int[] info = new int[1]; private static int bound = 0; private static int npt = 0; private static int cp = 0; private static int i = 0; private static int[] nfev = new int[1]; private static int inmc = 0; private static int iycn = 0; private static int iscn = 0;
/* 129 */   private static boolean finish = false;
/*     */ 
/* 131 */   private static double[] w = null;
/*     */ 
/*     */   public static int nfevaluations()
/*     */   {
/* 138 */     return nfun;
/*     */   }
/*     */ 
/*     */   public static void lbfgs(int n, int m, double[] x, double f, double[] g, boolean diagco, double[] diag, int[] iprint, double eps, double xtol, int[] iflag)
/*     */     throws LBFGS.ExceptionWithIflag
/*     */   {
/* 278 */     boolean execute_entire_while_loop = false;
/*     */ 
/* 280 */     if ((w == null) || (w.length != n * (2 * m + 1) + 2 * m))
/*     */     {
/* 282 */       w = new double[n * (2 * m + 1) + 2 * m];
/*     */     }
/*     */ 
/* 285 */     if (iflag[0] == 0)
/*     */     {
/* 289 */       solution_cache = new double[n];
/* 290 */       System.arraycopy(x, 0, solution_cache, 0, n);
/*     */ 
/* 292 */       iter = 0;
/*     */ 
/* 294 */       if ((n <= 0) || (m <= 0))
/*     */       {
/* 296 */         iflag[0] = -3;
/* 297 */         throw new ExceptionWithIflag(iflag[0], "Improper input parameters  (n or m are not positive.)");
/*     */       }
/*     */ 
/* 300 */       if (gtol <= 0.0001D)
/*     */       {
/* 302 */         System.err.println("LBFGS.lbfgs: gtol is less than or equal to 0.0001. It has been reset to 0.9.");
/* 303 */         gtol = 0.9D;
/*     */       }
/*     */ 
/* 306 */       nfun = 1;
/* 307 */       point = 0;
/* 308 */       finish = false;
/*     */ 
/* 310 */       if (diagco)
/*     */       {
/* 312 */         for (i = 1; i <= n; i += 1)
/*     */         {
/* 314 */           if (diag[(i - 1)] <= 0.0D)
/*     */           {
/* 316 */             iflag[0] = -2;
/* 317 */             throw new ExceptionWithIflag(iflag[0], "The " + i + "-th diagonal element of the inverse hessian approximation is not positive.");
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 323 */         for (i = 1; i <= n; i += 1)
/*     */         {
/* 325 */           diag[(i - 1)] = 1.0D;
/*     */         }
/*     */       }
/* 328 */       ispt = n + 2 * m;
/* 329 */       iypt = ispt + n * m;
/*     */ 
/* 331 */       for (i = 1; i <= n; i += 1)
/*     */       {
/* 333 */         w[(ispt + i - 1)] = (-g[(i - 1)] * diag[(i - 1)]);
/*     */       }
/*     */ 
/* 336 */       gnorm = Math.sqrt(ddot(n, g, 0, 1, g, 0, 1));
/* 337 */       stp1 = 1.0D / gnorm;
/* 338 */       ftol = 0.0001D;
/* 339 */       maxfev = 20;
/*     */ 
/* 341 */       if (iprint[0] >= 0) lb1(iprint, iter, nfun, gnorm, n, m, x, f, g, stp, finish);
/*     */ 
/* 343 */       execute_entire_while_loop = true;
/*     */     }
/*     */ 
/*     */     while (true)
/*     */     {
/* 348 */       if (execute_entire_while_loop)
/*     */       {
/* 350 */         iter += 1;
/* 351 */         info[0] = 0;
/* 352 */         bound = iter - 1;
/* 353 */         if (iter != 1)
/*     */         {
/* 355 */           if (iter > m) bound = m;
/* 356 */           ys = ddot(n, w, iypt + npt, 1, w, ispt + npt, 1);
/* 357 */           if (!diagco)
/*     */           {
/* 359 */             yy = ddot(n, w, iypt + npt, 1, w, iypt + npt, 1);
/*     */ 
/* 361 */             for (i = 1; i <= n; i += 1)
/*     */             {
/* 363 */               diag[(i - 1)] = (ys / yy);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 368 */             iflag[0] = 2;
/* 369 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 374 */       if ((execute_entire_while_loop) || (iflag[0] == 2))
/*     */       {
/* 376 */         if (iter != 1)
/*     */         {
/* 378 */           if (diagco)
/*     */           {
/* 380 */             for (i = 1; i <= n; i += 1)
/*     */             {
/* 382 */               if (diag[(i - 1)] <= 0.0D)
/*     */               {
/* 384 */                 iflag[0] = -2;
/* 385 */                 throw new ExceptionWithIflag(iflag[0], "The " + i + "-th diagonal element of the inverse hessian approximation is not positive.");
/*     */               }
/*     */             }
/*     */           }
/* 389 */           cp = point;
/* 390 */           if (point == 0) cp = m;
/* 391 */           w[(n + cp - 1)] = (1.0D / ys);
/*     */ 
/* 393 */           for (i = 1; i <= n; i += 1)
/*     */           {
/* 395 */             w[(i - 1)] = (-g[(i - 1)]);
/*     */           }
/*     */ 
/* 398 */           cp = point;
/*     */ 
/* 400 */           for (i = 1; i <= bound; i += 1)
/*     */           {
/* 402 */             cp -= 1;
/* 403 */             if (cp == -1) cp = m - 1;
/* 404 */             sq = ddot(n, w, ispt + cp * n, 1, w, 0, 1);
/* 405 */             inmc = n + m + cp + 1;
/* 406 */             iycn = iypt + cp * n;
/* 407 */             w[(inmc - 1)] = (w[(n + cp + 1 - 1)] * sq);
/* 408 */             daxpy(n, -w[(inmc - 1)], w, iycn, 1, w, 0, 1);
/*     */           }
/*     */ 
/* 411 */           for (i = 1; i <= n; i += 1)
/*     */           {
/* 413 */             w[(i - 1)] = (diag[(i - 1)] * w[(i - 1)]);
/*     */           }
/*     */ 
/* 416 */           for (i = 1; i <= bound; i += 1)
/*     */           {
/* 418 */             yr = ddot(n, w, iypt + cp * n, 1, w, 0, 1);
/* 419 */             beta = w[(n + cp + 1 - 1)] * yr;
/* 420 */             inmc = n + m + cp + 1;
/* 421 */             beta = w[(inmc - 1)] - beta;
/* 422 */             iscn = ispt + cp * n;
/* 423 */             daxpy(n, beta, w, iscn, 1, w, 0, 1);
/* 424 */             cp += 1;
/* 425 */             if (cp == m) cp = 0;
/*     */           }
/*     */ 
/* 428 */           for (i = 1; i <= n; i += 1)
/*     */           {
/* 430 */             w[(ispt + point * n + i - 1)] = w[(i - 1)];
/*     */           }
/*     */         }
/*     */ 
/* 434 */         nfev[0] = 0;
/* 435 */         stp[0] = 1.0D;
/* 436 */         if (iter == 1) stp[0] = stp1;
/*     */ 
/* 438 */         for (i = 1; i <= n; i += 1)
/*     */         {
/* 440 */           w[(i - 1)] = g[(i - 1)];
/*     */         }
/*     */       }
/*     */ 
/* 444 */       Mcsrch.mcsrch(n, x, f, g, w, ispt + point * n, stp, ftol, xtol, maxfev, info, nfev, diag);
/*     */ 
/* 446 */       if (info[0] == -1)
/*     */       {
/* 448 */         iflag[0] = 1;
/* 449 */         return;
/*     */       }
/*     */ 
/* 452 */       if (info[0] != 1)
/*     */       {
/* 454 */         iflag[0] = -1;
/* 455 */         throw new ExceptionWithIflag(iflag[0], "Line search failed. See documentation of routine mcsrch. Error return of line search: info = " + info[0] + " Possible causes: function or gradient are incorrect, or incorrect tolerances.");
/*     */       }
/*     */ 
/* 458 */       nfun += nfev[0];
/* 459 */       npt = point * n;
/*     */ 
/* 461 */       for (i = 1; i <= n; i += 1)
/*     */       {
/* 463 */         w[(ispt + npt + i - 1)] = (stp[0] * w[(ispt + npt + i - 1)]);
/* 464 */         w[(iypt + npt + i - 1)] = (g[(i - 1)] - w[(i - 1)]);
/*     */       }
/*     */ 
/* 467 */       point += 1;
/* 468 */       if (point == m) point = 0;
/*     */ 
/* 470 */       gnorm = Math.sqrt(ddot(n, g, 0, 1, g, 0, 1));
/* 471 */       xnorm = Math.sqrt(ddot(n, x, 0, 1, x, 0, 1));
/* 472 */       xnorm = Math.max(1.0D, xnorm);
/*     */ 
/* 474 */       if (gnorm / xnorm <= eps) finish = true;
/*     */ 
/* 476 */       if (iprint[0] >= 0) lb1(iprint, iter, nfun, gnorm, n, m, x, f, g, stp, finish);
/*     */ 
/* 485 */       System.arraycopy(x, 0, solution_cache, 0, n);
/*     */ 
/* 487 */       if (finish)
/*     */       {
/* 489 */         iflag[0] = 0;
/* 490 */         return;
/*     */       }
/*     */ 
/* 493 */       execute_entire_while_loop = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void lb1(int[] iprint, int iter, int nfun, double gnorm, int n, int m, double[] x, double f, double[] g, double[] stp, boolean finish)
/*     */   {
/* 535 */     if (iter == 0)
/*     */     {
/* 537 */       System.err.println("*************************************************");
/* 538 */       System.err.println("  n = " + n + "   number of corrections = " + m + "\n       initial values");
/* 539 */       System.err.println(" f =  " + f + "   gnorm =  " + gnorm);
/* 540 */       if (iprint[1] >= 1)
/*     */       {
/* 542 */         System.err.print(" vector x =");
/* 543 */         for (int i = 1; i <= n; i++)
/* 544 */           System.err.print("  " + x[(i - 1)]);
/* 545 */         System.err.println("");
/*     */ 
/* 547 */         System.err.print(" gradient vector g =");
/* 548 */         for (i = 1; i <= n; i++)
/* 549 */           System.err.print("  " + g[(i - 1)]);
/* 550 */         System.err.println("");
/*     */       }
/* 552 */       System.err.println("*************************************************");
/* 553 */       System.err.println("\ti\tnfn\tfunc\tgnorm\tsteplength");
/*     */     }
/*     */     else
/*     */     {
/* 557 */       if ((iprint[0] == 0) && (iter != 1) && (!finish)) return;
/* 558 */       if (iprint[0] != 0)
/*     */       {
/* 560 */         if (((iter - 1) % iprint[0] == 0) || (finish))
/*     */         {
/* 562 */           if ((iprint[1] > 1) && (iter > 1))
/* 563 */             System.err.println("\ti\tnfn\tfunc\tgnorm\tsteplength");
/* 564 */           System.err.println("\t" + iter + "\t" + nfun + "\t" + f + "\t" + gnorm + "\t" + stp[0]);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 573 */         if ((iprint[1] > 1) && (finish))
/* 574 */           System.err.println("\ti\tnfn\tfunc\tgnorm\tsteplength");
/* 575 */         System.err.println("\t" + iter + "\t" + nfun + "\t" + f + "\t" + gnorm + "\t" + stp[0]);
/*     */       }
/* 577 */       if ((iprint[1] == 2) || (iprint[1] == 3))
/*     */       {
/* 579 */         if (finish)
/*     */         {
/* 581 */           System.err.print(" final point x =");
/*     */         }
/*     */         else
/*     */         {
/* 585 */           System.err.print(" vector x =  ");
/*     */         }
/* 587 */         for (int i = 1; i <= n; i++)
/* 588 */           System.err.print("  " + x[(i - 1)]);
/* 589 */         System.err.println("");
/* 590 */         if (iprint[1] == 3)
/*     */         {
/* 592 */           System.err.print(" gradient vector g =");
/* 593 */           for (i = 1; i <= n; i++)
/* 594 */             System.err.print("  " + g[(i - 1)]);
/* 595 */           System.err.println("");
/*     */         }
/*     */       }
/* 598 */       if (finish)
/* 599 */         System.err.println(" The minimization terminated without detecting errors. iflag = 0");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void daxpy(int n, double da, double[] dx, int ix0, int incx, double[] dy, int iy0, int incy)
/*     */   {
/* 613 */     if (n <= 0) return;
/*     */ 
/* 615 */     if (da == 0.0D) return;
/*     */ 
/* 617 */     if ((incx != 1) || (incy != 1))
/*     */     {
/* 619 */       int ix = 1;
/* 620 */       int iy = 1;
/*     */ 
/* 622 */       if (incx < 0) ix = (-n + 1) * incx + 1;
/* 623 */       if (incy < 0) iy = (-n + 1) * incy + 1;
/*     */ 
/* 625 */       for (int i = 1; i <= n; i++)
/*     */       {
/* 627 */         dy[(iy0 + iy - 1)] += da * dx[(ix0 + ix - 1)];
/* 628 */         ix += incx;
/* 629 */         iy += incy;
/*     */       }
/*     */ 
/* 632 */       return;
/*     */     }
/*     */ 
/* 635 */     int m = n % 4;
/* 636 */     if (m != 0)
/*     */     {
/* 638 */       for (int i = 1; i <= m; i++)
/*     */       {
/* 640 */         dy[(iy0 + i - 1)] += da * dx[(ix0 + i - 1)];
/*     */       }
/*     */ 
/* 643 */       if (n < 4) return;
/*     */     }
/*     */ 
/* 646 */     int mp1 = m + 1;
/* 647 */     for (int i = mp1; i <= n; i += 4)
/*     */     {
/* 649 */       dy[(iy0 + i - 1)] += da * dx[(ix0 + i - 1)];
/* 650 */       dy[(iy0 + i + 1 - 1)] += da * dx[(ix0 + i + 1 - 1)];
/* 651 */       dy[(iy0 + i + 2 - 1)] += da * dx[(ix0 + i + 2 - 1)];
/* 652 */       dy[(iy0 + i + 3 - 1)] += da * dx[(ix0 + i + 3 - 1)];
/*     */     }
/*     */   }
/*     */ 
/*     */   public static double ddot(int n, double[] dx, int ix0, int incx, double[] dy, int iy0, int incy)
/*     */   {
/* 667 */     double dtemp = 0.0D;
/*     */ 
/* 669 */     if (n <= 0) return 0.0D;
/*     */ 
/* 671 */     if ((incx != 1) || (incy != 1))
/*     */     {
/* 673 */       int ix = 1;
/* 674 */       int iy = 1;
/* 675 */       if (incx < 0) ix = (-n + 1) * incx + 1;
/* 676 */       if (incy < 0) iy = (-n + 1) * incy + 1;
/* 677 */       for (int i = 1; i <= n; i++)
/*     */       {
/* 679 */         dtemp += dx[(ix0 + ix - 1)] * dy[(iy0 + iy - 1)];
/* 680 */         ix += incx;
/* 681 */         iy += incy;
/*     */       }
/* 683 */       return dtemp;
/*     */     }
/*     */ 
/* 686 */     int m = n % 5;
/* 687 */     if (m != 0)
/*     */     {
/* 689 */       for (int i = 1; i <= m; i++)
/*     */       {
/* 691 */         dtemp += dx[(ix0 + i - 1)] * dy[(iy0 + i - 1)];
/*     */       }
/* 693 */       if (n < 5) return dtemp;
/*     */     }
/*     */ 
/* 696 */     int mp1 = m + 1;
/* 697 */     for (int i = mp1; i <= n; i += 5)
/*     */     {
/* 699 */       dtemp = dtemp + dx[(ix0 + i - 1)] * dy[(iy0 + i - 1)] + dx[(ix0 + i + 1 - 1)] * dy[(iy0 + i + 1 - 1)] + dx[(ix0 + i + 2 - 1)] * dy[(iy0 + i + 2 - 1)] + dx[(ix0 + i + 3 - 1)] * dy[(iy0 + i + 3 - 1)] + dx[(ix0 + i + 4 - 1)] * dy[(iy0 + i + 4 - 1)];
/*     */     }
/*     */ 
/* 702 */     return dtemp;
/*     */   }
/*     */ 
/*     */   public static class ExceptionWithIflag extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     public int iflag;
/*     */ 
/*     */     public ExceptionWithIflag(int i, String s)
/*     */     {
/*  84 */       super(); this.iflag = i; } 
/*  85 */     public String toString() { return getMessage() + " (iflag == " + this.iflag + ")"; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\dragontoolikt\dragontool.jar
 * Qualified Name:     dragon.ml.seqmodel.crf.LBFGS
 * JD-Core Version:    0.6.2
 */