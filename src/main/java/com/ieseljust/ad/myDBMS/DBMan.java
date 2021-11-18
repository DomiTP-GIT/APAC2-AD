package com.ieseljust.ad.myDBMS;

// Imports per a entrada de dades

import java.util.Scanner;

public class DBMan {
    /*
    Esta és la classe llançadora de l'aplicació
    Conté el mètode main que recull la informació del servidor
    i inicia una instància de connectionManager per 
    gestionar les connexions
    */

  public static void main(String[] args) {

    connectionManager cm;

    Scanner keyboard = new Scanner(System.in);

    String user, pass, ip, port;

    do {

      System.out.print(CC.GREEN_BOLD_BRIGHT + "# Server: " + CC.RESET);
      ip = keyboard.nextLine();

      System.out.print(CC.GREEN_BOLD_BRIGHT + "# Port: " + CC.RESET);
      port = keyboard.nextLine();

      System.out.print(CC.GREEN_BOLD_BRIGHT + "# Username: " + CC.RESET);
      user = keyboard.nextLine();

      System.out.print(CC.GREEN_BOLD_BRIGHT + "# Password: " + CC.BLACK);
      pass = keyboard.nextLine();
      System.out.print(CC.RESET);

      cm = new connectionManager(ip, port, user, pass);

    } while (cm.connectDBMS() == null);

    cm.startShell();

  }

}
