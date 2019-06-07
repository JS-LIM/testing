import java.io.*;

public class Help
/****************************************************************************
*  AUTH:  Truly, Yours                    DATE:  Oct. 1999                  *
*  DEPT:  Computer Science, CS-200        ORG.:  Colorado State University  *
*****************************************************************************
*                                                                           *
*  FILE:  Help.java                                                         *
*                                                                           *
*  DESC:  Contains the member functions for the Help Class.        .        *
*                                                                           *
****************************************************************************/
{

  //***************************************************************************
  public static void General()
  {
     System.out.println("Help infomation");
  }

  //***************************************************************************
  public static void Command(char cmd) 
  {
	  /* 관련 명령어에 대한 정보를 출력할 수 있도록 수정하였다. */
      // Now call the command routine on the basis of the user command

      switch (Character.toUpperCase(cmd)) 
      {

        //========= TERMINATE EDITOR COMMANDS =================================
       
          case 'Q': System.out.println("QUIT (& Update File) Command");// 
                    break;

          case 'X': System.out.println("EXIT (& DO NOT Update File) Command");
                    break;

        //========= TERMINATE EDITOR COMMANDS =================================

          case 'H': System.out.println("HELP (with optional command argument) Command");
                    break;
       
        //========= MOVE/SHOW CURRENT-LINE-NUMBER/CLN EDITOR COMMANDS =========

          case 'T': System.out.println("TOP (Move CLN to Top of File) Command");
                    break;

          case 'E': System.out.println("END (Move CLN to End of File) Command");
                    break;

          case 'N': System.out.println("NEXT Lines (Move CLN forward) Command");
                    break;

          case 'B': System.out.println("BACK Lines (Move CLN backward) Command");
                    break;

          case 'W': System.out.println("WHERE (Print CLN) Command");
                    break;

          case 'C': System.out.println("COUNT (Print Total File Lines) Command");
                    break;

        //========= PRINT LINES EDITOR COMMANDS ===============================

          case 'L': System.out.println("LIST (Move CLN) Lines Command");
                    break;

          case 'S': System.out.println("SHOW (DO NOT Move CLN) Lines Command");
                    break;

        //========= DELETE/ADD LINES EDITOR COMMANDS ==========================

          case 'D': System.out.println("DELETE Lines Command");
                    break;

          case 'A': System.out.println("ADD Lines Command");
                    break;

        //========= STRING FIND/REPLACE LINES EDITOR COMMANDS =================

          case 'F': System.out.println("FIND String In Lines Command");    
                    break;

          case 'R': System.out.println("REPLACE String In Lines Command");
                    break;

        //========= COPY/CUT & PASTE LINES EDITOR COMMANDS ====================

          case 'Y': System.out.println("YANK (to Yank Buffer) Lines Command");
                    break;

          case 'Z': System.out.println("YANK DELETE (to Yank Buffer) Lines Command");
                    break;

          case 'P': System.out.println("PUT (Yank Buffer) Lines Command");
                    break;

        //========= INDEX/KEYWORD EDITOR COMMANDS =============================

          case 'I': System.out.println("INDEX Keywords Command");
                    break;

          case 'K': System.out.println("Print KEYWORD In Which Lines Command");
                    break;

        //========= SPECIAL EDITOR COMMANDS ===================================

          case 'O': System.out.println("ORDER (Sort L-H) Lines Command");
                    break;

          case 'M': System.out.println("MARGIN (Set Margins/Window) Command");
                    break;

        //========= INTERNAL PROGRAM ERROR CASE ===============================
   
          default : Msg.wMsg("INTERNAL SYSTEM ERROR:");
                    Msg.wLMsg("    Cmd_Driver: Illegal edit cmd name");
      }
  }

} // EndClass Help