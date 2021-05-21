      *--------------------------------------------------------------*
      *
      * Sample rpgle program to calculate Fibonacci series.
      *
      *--------------------------------------------------------------*
     D PPNUM1          S              8
     D PPNUM2          S              8
     D NBR             S             21  0 
     D RESULT          S             21  0 
     D COUNT           S             21  0 
     D A               S             21  0 
     D B               S             21  0 INZ(1)
      *--------------------------------------------------------------*
      * Main
      *--------------------------------------------------------------*
      *
     C     *ENTRY        PLIST
     C                   PARM                    PPNUM1 
     C                   PARM                    PPNUM2                          
      *
      * Fibonacci of PPNUM1
     C                   EVAL      RESULT=0
     C                   EVAL      NBR = %DEC(PPNUM1:8:0)
      *
     C                   IF        NBR = 1
     C                   EVAL      RESULT = 1
     C                   ELSE
     C                   FOR       COUNT = 2 TO NBR
     C                   EVAL      RESULT = A + B
     C                   EVAL      A = B
     C                   EVAL      B = RESULT
     C                   ENDFOR
     C                   ENDIF
      *
     C     RESULT        DSPLY
      *
     C                   EVAL      PPNUM2=%TRIM(%CHAR(RESULT))
      *
     C                   SETON                                        LR
      *--------------------------------------------------------------*

