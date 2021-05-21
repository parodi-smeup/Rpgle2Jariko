# Rpgle2Jariko
This project gives a way to easly integrate java program execution inside an rpgle program.
The goal is to run 'rpgInterpreterCore.jar' (aka Jariko) directly from 'AS400 system environment', using rpgle program instead of QSHELL. 

Need 'rpgle' code to be develop (see samples below), where the rpgle program will instantiate and use java classes from its 'internal'.
Maybe more efficient way, due to capability of start java virtual machine, instantiate needed java object, and use them many times, until the java virtual machine will be stopped programmatically. 
In this case, the rpgle program must be used as statefull (ends with RT indicator ON).

Following rpgle samples, shows how the caller 'JARIKORPGC.rpgle' calls the 'JARICKORPG.rpgle' that
uses java classes.

**JARIKORPGC (the main rpg caller program)**
```sh

      *----------------------------------------------------------------*
      * Program flow
      * 1. call 'JARIKORPG.rpgle' to initialize/instantiate environments
      *    and java needed objects.
      * 2. call 'JARIKORPG.rpgle' to setup attributes for 'in-memory'
      *    java objects previously istantiated.
      * 3. call 'JARIKORPG.rpgle' to run 'HELLO.rpgle' by java intepreter.
      * 4. call 'JARIKORPG.rpgle' to run 'CALCFIB.rpgle' by java intepreter.
      * 5. call 'JARIKORPG.rpgle' to run 'DIVBY0.rpgle' by java intepreter
      *    needed to generate a RuntimeException.
      * 6. call 'JARIKORPG.rpgle' to run 'HELLO.rpgle' by java intepreter
      *    to be sure that jvm is enable to run java programs again.
      * 7. call 'JARIKORPG.rpgle' to run 'CALCFIB.rpgle' by java intepreter
      *    to be sure that jvm is enable to run java programs again.
      *----------------------------------------------------------------*
      * Main                                                           *
      *----------------------------------------------------------------*
     D p_function      s             10
     D p_interopPkg    s            128
     D p_rpgDir        s            128
     D p_rpgPgm        s            128
     D p_rpgParms      s             10    dim(5)
     D p_result        s            100
     D p_errorCode     s             10
      *----------------------------------------------------------------*
     D log             s           1000    dim(9999)
     D logIdx          s              5  0 inz(1)
      *----------------------------------------------------------------*
      *
      * .Enable/Disable collecting log (50=*OFF -> disable)
     C                   SETON                                          50
      * .Enable/Disable dsply result (51=*OFF -> disable)
     C                   SETOFF                                         51
      *
      * Initialize (start jvm and create any needed class instances)
     C                   eval      p_function = 'INITIALIZE'
     C                   eval      p_interopPkg = ''
     C                   eval      p_rpgDir = ''
     C                   eval      p_rpgPgm = ''
     C                   eval      p_rpgParms = ''
     C                   eval      p_result = ''
     C                   eval      p_errorCode = ''
     C                   exsr      $callPgm
      *
      * Setup
     C                   eval      p_function = 'SETUP'
     C                   eval      p_interopPkg = 'com.smeup.api'
     C                   eval      p_rpgDir = '/smedoc/jariko400/rpg'
     C                   eval      p_rpgPgm = ''
     C                   eval      p_rpgParms = ''
     C                   eval      p_result = ''
     C                   eval      p_errorCode = ''
     C                   exsr      $callPgm
      *
      * Call rpg program HELLO.rpgle
     C                   eval      p_function = 'CALL'
     C                   eval      p_interopPkg = ''
     C                   eval      p_rpgDir = ''
     C                   eval      p_rpgPgm = 'HELLO.rpgle'
     C                   eval      p_rpgParms = ''
     C                   eval      p_result = ''
     C                   eval      p_errorCode = ''
     C                   exsr      $callPgm
      *
      * Call rpg program CALCFIB.rpgle to calculate Fibonacci's of 15.
      * Expected change of 2nd parameter value from '25' to '610' by called.
     C                   eval      p_function = 'CALL'
     C                   eval      p_interopPkg = ''
     C                   eval      p_rpgDir = ''
     C                   eval      p_rpgPgm = 'CALCFIB.rpgle'
     C                   eval      p_rpgParms(1) = '15'
     C                   eval      p_rpgParms(2) = '25'
     C                   eval      p_result = ''
     C                   eval      p_errorCode = ''
     C                   exsr      $callPgm
      *
      * Call rpg program DIVBY0.rpgle to thrown a runtime exception.
     C                   eval      p_function = 'CALL'
     C                   eval      p_interopPkg = ''
     C                   eval      p_rpgDir = ''
     C                   eval      p_rpgPgm = 'DIVBY0.rpgle'
     C                   eval      p_rpgParms = ''
     C                   eval      p_result = ''
     C                   eval      p_errorCode = ''
     C                   exsr      $callPgm
      *
      * Call rpg program HELLO.rpgle
     C                   eval      p_function = 'CALL'
     C                   eval      p_interopPkg = ''
     C                   eval      p_rpgDir = ''
     C                   eval      p_rpgPgm = 'HELLO.rpgle'
     C                   eval      p_rpgParms = ''
     C                   eval      p_result = ''
     C                   eval      p_errorCode = ''
     C                   exsr      $callPgm
      *
      * Call rpg program CALCFIB.rpgle to calculate Fibonacci's of 11.
     C                   eval      p_function = 'CALL'
     C                   eval      p_interopPkg = ''
     C                   eval      p_rpgDir = ''
     C                   eval      p_rpgPgm = 'CALCFIB.rpgle'
     C                   eval      p_rpgParms(1) = '11'
     C                   eval      p_result = ''
     C                   eval      p_errorCode = ''
     C                   exsr      $callPgm
      *
      * Terminate
     C                   eval      p_function = 'TERMINATE'
     C                   eval      p_interopPkg = ''
     C                   eval      p_rpgDir = ''
     C                   eval      p_rpgPgm = ''
     C                   eval      p_rpgParms = ''
     C                   eval      p_result = ''
     C                   eval      p_errorCode = ''
     C                   exsr      $callPgm
      *
     C                   seton                                        lr
      *----------------------------------------------------------------*
      * Call rpgle program
      *----------------------------------------------------------------*
     C     $callPgm      begsr
      *
     C                   call      'JARIKORPG'
     C                   parm                    p_function
     C                   parm                    p_interopPkg
     C                   parm                    p_rpgDir
     C                   parm                    p_rpgPgm
     C                   parm                    p_rpgParms
     C                   parm                    p_result
     C                   parm                    p_errorCode
      *
     C                   exsr      $collectLog
     C                   exsr      $showResults
      *
     C                   endsr
      *----------------------------------------------------------------*
      * Collect log
      *----------------------------------------------------------------*
     C     $collectLog   begsr
      *
     C                   if        *in50=*off
     C                   leavesr
     C                   endif
      *
     C                   clear                   parmsAsString   200
     C                   do        5             x                 1 0
     C                   eval      parmsAsString=%trim(parmsAsString) +
     C                                           '('+%char(x)+')'+
     C                                           %trim(p_rpgParms(x))+','
     C                   enddo
      *
     C                   eval      log(logIdx)=
     C                              'p_function=' + %trim(p_function) +
     C                              ',  p_interopPkg=' + %trim(p_interopPkg) +
     C                              ',  p_rpgDir=' + %trim(p_rpgDir) +
     C                              ',  p_rpgPgm=' + %trim(p_rpgPgm) +
     C                              ',  p_rpgParms=' + %trim(parmsAsString) +
     C                              ',  p_result=' + %trim(p_result) +
     C                              ',  p_errorCode=' + %trim(p_errorCode)
     C                   eval      logIdx+=1
      *
     C                   endsr
      *----------------------------------------------------------------*
      * Showresult DSPLY
      *----------------------------------------------------------------*
     C     $showResults  begsr
      *
     C                   if        *in51=*off
     C                   leavesr
     C                   endif
      *
     C                   clear                   toDsply          52
      *
     C                   if        p_errorCode <> ''
     C                   eval      toDsply='['+%trim(p_function)+'] '+
     C                                     %trim(p_result) +
     C                                     '['+%trim(p_errorCode)+']'
     C                   else
     C                   eval      toDsply='['+%trim(p_function)+'] '+
     C                                     %trim(p_result)
     C                   endif
      *
     C     toDsply       dsply
      *
     C                   endsr
      *----------------------------------------------------------------*

```

**JARICORPG.rpgle (uses java classes from its internal)**
```sh

      * ALWNULL(*YES)
     H THREAD(*SERIALIZE)
   COP* DFTACTGRP(*NO)
      *----------------------------------------------------------------
     D rmvvar          s            100    dim(100) ctdata perrcd(1)
     D jars            s            100    dim(100) ctdata perrcd(1)
     D addvar_1        s            100    dim(100) ctdata perrcd(1)
     D addvar_2        s            100    dim(100) ctdata perrcd(1)
     D prop            s            100    dim(100) ctdata perrcd(1)
      *----------------------------------------------------------------
     D p_function      s             10
     D p_interopPkg    s            128
     D p_rpgDir        s            128
     D p_rpgPgm        s            128
     D p_rpgParms      s             10    dim(5)
     D p_result        s            100
     D p_errorCode     s             10
      *----------------------------------------------------------------
     D result          s            100a   varying
     D resultParms     s             10a   dim(5) varying
      *----------------------------------------------------------------
     D Jrpgle          s               O   Class(*JAVA:
     D                                     'com.smeup.connector.Jrpgle')
      *----------------------------------------------------------------
     D string          s               O   Class(*JAVA:'java.lang.String')
      *----------------------------------------------------------------
      * String type
     D newString       PR              O   EXTPROC(*JAVA:'java.lang.String':
     D                                     *CONSTRUCTOR)
     D                              256    Const Varying
      *
      * Class mapping
     D newJrpgle       PR              O   EXTPROC(*JAVA:
     D                                     'com.smeup.connector.Jrpgle':
     D                                     *CONSTRUCTOR)
      * Methods mapping
     D setup           PR              O   EXTPROC(*JAVA:
     D                                     'com.smeup.connector.Jrpgle':
     D                                     'setup')
     D                                     CLASS(*JAVA:'java.lang.String')
     D                                 O   Class(*JAVA:'java.lang.String')
     D                                 O   Class(*JAVA:'java.lang.String')
      *
     D call            PR              O   EXTPROC(*JAVA:
     D                                     'com.smeup.connector.Jrpgle':
     D                                     'call')
     D                                     CLASS(*JAVA:'java.lang.String')
     D                                 O   Class(*JAVA:'java.lang.String')
     D                                 O   Class(*JAVA:'java.lang.String')
     D                                     DIM(5)
      *
      * Standard methods
     D getBytes        PR          1024A   varying
     D                                     extproc(*JAVA:
     D                                     'java.lang.String':
     D                                     'getBytes')
      *
      * Java parameters
     D interopPkg      s               O   Class(*JAVA:'java.lang.String')
     D rpgDir          s               O   Class(*JAVA:'java.lang.String')
     D rpgPgm          s               O   Class(*JAVA:'java.lang.String')
      *
     D rpgParms        s               O   Class(*JAVA:'java.lang.String')
     D                                     DIM(5)
      *----------------------------------------------------------------*
      /COPY QILEGEN,£PDSQQ
      *----------------------------------------------------------------*
      * Main                                                           *
      *----------------------------------------------------------------*
      *
     C     *entry        plist
     C                   parm                    p_function
     C                   parm                    p_interopPkg
     C                   parm                    p_rpgDir
     C                   parm                    p_rpgPgm
     C                   parm                    p_rpgParms
     C                   parm                    p_result
     C                   parm                    p_errorCode
      *
     C                   monitor
     C                   select
      * .initialize
     C                   when      p_function = 'INITIALIZE'
     C                   exsr      $initialize
      * .'setup' method execution
     C                   when      p_function = 'SETUP'
     C                   exsr      $setup
      * .'call' method execution
     C                   when      p_function = 'CALL'
     C                   exsr      $call
      * .terminate
     C                   when      p_function = 'TERMINATE'
     C                   exsr      $terminate
      * .other
     C                   other
     C                   eval      p_result = 'Function ' +
     C                                        %trim(p_function) +
     C                                        'not allowed'
     C                   endsl
      * .monitoring exceptions
     C                   on-error
     C                   eval      p_result = ''
     C                   eval      p_errorCode = £QDSMI
     C                   endmon
      *
     C   55              seton                                        rt
     C  n55              seton                                        lr
      *----------------------------------------------------------------*
     D* Initializations
      *----------------------------------------------------------------*
     C     $initialize   begsr
      *
     C                   z-add     1024          cmdLen           15 5
     C                   clear                   jcmd           1024
     C                   z-add     0             i                 3 0
     C                   eval      p_result=''
     C                   eval      Jrpgle=*null
     C                   seton                                        55
      * remove all java environment variables
     C                   clear                   jcmd
     C                   z-add     0             i
     C                   do        50            i
     C                   if        %trim(rmvvar(i))=''
     C                   leave
     C                   endif
     C                   eval      jcmd=%trim(rmvvar(i))
     C                   call      'QCMDEXC'                            37
     C                   parm                    jcmd
     C                   parm                    cmdLen
     C                   enddo
      * jar libs
     C                   clear                   jcmd
     C                   z-add     0             i
     C                   do        50            i
     C                   if        %trim(jars(i))=''
     C                   leave
     C                   endif
     C                   eval      jcmd=%trim(jcmd)+%trim(jars(i))
     C                   enddo
     C                   call      'QCMDEXC'                            37
     C                   parm                    jcmd
     C                   parm                    cmdLen
     C   37              goto      endrout
      * addvar_1
     C*                  clear                   jcmd
     C*                  z-add     0             i
     C*                  do        50            i
     C*                  if        %trim(addvar_1(i))=''
     C*                  leave
     C*                  endif
     C*                  eval      jcmd=%trim(jcmd)+%trim(addvar_1(i))
     C*                  enddo
     C*                  call      'QCMDEXC'                            37
     C*                  parm                    jcmd
     C*                  parm                    cmdLen
     C*  37              goto      endrout
      * addvar_2
     C*                  clear                   jcmd
     C*                  z-add     0             i
     C*                  do        50            i
     C*                  if        %trim(addvar_2(i))=''
     C*                  leave
     C*                  endif
     C*                  eval      jcmd=%trim(jcmd)+%trim(addvar_2(i))
     C*                  enddo
     C*                  call      'QCMDEXC'                            37
     C*                  parm                    jcmd
     C*                  parm                    cmdLen
     C*  37              goto      endrout
      * properties
     C                   clear                   jcmd
     C                   z-add     0             i
     C                   do        50            i
     C                   if        %trim(prop(i))=''
     C                   leave
     C                   endif
     C                   eval      jcmd=%trim(jcmd)+%trim(prop(i))
     C                   enddo
     C                   call      'QCMDEXC'                            37
     C                   parm                    jcmd
     C                   parm                    cmdLen
     C   37              goto      endrout
      * class instance
     C                   eval      Jrpgle=newJrpgle()
      *
     C     endrout       tag
     C                   if        *in37 or Jrpgle=*null
     C                   eval      p_result=''
     C                   eval      p_errorCode='N/D'
     C                   endif
      *
     C                   endsr
      *----------------------------------------------------------------*
     D* 'setup' method execution
      *----------------------------------------------------------------*
     C     $setup        begsr
      *
     C                   eval      interopPkg =
     C                                newString(%trim(p_interopPkg))
     C                   eval      rpgDir =
     C                                newString(%trim(p_rpgDir))
      *
      * setup and retrieve result
     C                   eval      string = setup(Jrpgle:
     C                                           interopPkg:
     C                                           rpgDir)
     C                   eval      result = getBytes(string)
     C                   movel(p)  result        p_result
      *
     C                   endsr
      *----------------------------------------------------------------*
     D* 'call' method execution
      *----------------------------------------------------------------*
     C     $call         begsr
      *
      * .rpg program to call
     C                   eval      rpgPgm = newString(%trim(p_rpgPgm))
      * .rpg program parameters
     C                   do        5             i
     C                   eval      rpgParms(i) =
     C                                 newString(%trim(p_rpgParms(i)))
     C                   enddo
      * call and retrieve result
     C                   eval      string = call(Jrpgle:
     C                                           rpgPgm:
     C                                           rpgParms)
     C                   eval      result = getBytes(string)
      *
      * .get new parameters values (due to by-reference behaviour)
     C                   do        5             i
     C                   eval      resultParms(i) =
     C                                 getBytes(rpgParms(i))
     C                   enddo
      *
     C                   movel(p)  result        p_result
      *
     C                   endsr
      *----------------------------------------------------------------*
     D* Terminate
      *----------------------------------------------------------------*
     C     $terminate    begsr
      *
     C                   setoff                                       55
     C                   eval      Jrpgle = *null
      *
     C                   endsr
      *----------------------------------------------------------------*
** rmvvar
RMVENVVAR ENVVAR(CLASSPATH)
RMVENVVAR ENVVAR(QIBM_USE_DESCRIPTOR_STDIO)
RMVENVVAR ENVVAR(QIBM_RPG_JAVA_EXCP_TRACE)
RMVENVVAR ENVVAR(QIBM_RPG_JAVA_PROPERTIES)
** jars
ADDENVVAR ENVVAR(CLASSPATH) VALUE('
/smedoc/jariko400/lib/Rpgle2Jariko.jar:
/smedoc/jariko400/lib/rpgJavaInterpreter-core-all.jar:
') REPLACE(*YES)
** addvar_1
ADDENVVAR ENVVAR(QIBM_USE_DESCRIPTOR_STDIO) VALUE('
Y
') REPLACE(*YES)
** addvar_2
ADDENVVAR ENVVAR(QIBM_RPG_JAVA_EXCP_TRACE) VALUE('
Y
') REPLACE(*YES)
** prop
ADDENVVAR ENVVAR(QIBM_RPG_JAVA_PROPERTIES) VALUE('
-Djava.version=1.8;
') REPLACE(*YES)

```
