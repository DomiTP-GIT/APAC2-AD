package com.ieseljust.ad.myDBMS;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

class connectionManager {

  String server;
  String port;
  String user;
  String pass;

  connectionManager() {
    this.server = "127.0.0.1";
    this.port = "3306";
    this.user = "root";
    this.pass = "root";
  }

  connectionManager(String server, String port, String user, String pass) {
    this.server = server;
    this.port = port;
    this.user = user;
    this.pass = pass;
  }

  public Connection connectDBMS() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      String connectionUrl = "jdbc:mysql://" + this.server + ":" + this.port + "?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";

      return DriverManager.getConnection(connectionUrl, this.user, this.pass);

    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }

    return null;

  }

  public void showInfo() {
    try {
      DatabaseMetaData metaData = connectDBMS().getMetaData();
      System.out.println(CC.GREEN_BOLD_BRIGHT + "\nInformación del SGBD" + CC.RESET);
      System.out.println(CC.PURPLE_BOLD_BRIGHT + "Nom SGBD: " + CC.RESET + metaData.getDatabaseProductName());
      System.out.println(CC.PURPLE_BOLD_BRIGHT + "Driver: " + CC.RESET + metaData.getDriverName());
      System.out.println(CC.PURPLE_BOLD_BRIGHT + "URL: " + CC.RESET + metaData.getURL());
      System.out.println(CC.PURPLE_BOLD_BRIGHT + "Nom Usuari: " + CC.RESET + metaData.getUserName());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void showDatabases() {
    try {
      DatabaseMetaData metaData = connectDBMS().getMetaData();
      System.out.println(CC.GREEN_BOLD_BRIGHT + "\nBases de Datos" + CC.RESET);
      ResultSet rs = metaData.getCatalogs();
      while (rs.next()) {
        System.out.println(rs.getString(1));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public ArrayList<String> dbList() {
    ArrayList<String> db = new ArrayList<>();
    try {
      DatabaseMetaData metaData = connectDBMS().getMetaData();
      ResultSet rs = metaData.getCatalogs();
      while (rs.next()) {
        db.add(rs.getString(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return db;
  }

  public void importScript(String script) {
    File scriptFile = new File("sql/"+script);
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(scriptFile));
    } catch (FileNotFoundException e) {
      System.out.println(CC.RED_BOLD_BRIGHT + "El script no existe"+CC.RESET);
    }
    String linea = null;
    StringBuilder sb = new StringBuilder();
    String saltoLinea = System.getProperty("line.separator");

    try {
      while ((linea = br.readLine()) != null){
        sb.append(linea);
        sb.append(saltoLinea);
      }
    } catch (IOException e) {
      System.out.println(CC.RED_BOLD_BRIGHT+"Error de I/O"+CC.RESET);
    }

    String query = sb.toString();
    System.out.println(CC.PURPLE_BOLD_BRIGHT+"Ejecutando consulta..."+CC.RESET);

    try {
      Statement st = connectDBMS().createStatement();
      int res = st.executeUpdate(query);
      System.out.println(CC.GREEN_BOLD_BRIGHT+"El Script se ha ejecutado, resultado: "+CC.PURPLE_BOLD_BRIGHT+res+CC.RESET);
      st.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void startShell() {

    Scanner keyboard = new Scanner(System.in);
    String command;

    do {

      System.out.print(CC.GREEN_BOLD_BRIGHT + "# (" + this.user + ") on " + this.server + ":" + this.port + "> " + CC.RESET);
      command = keyboard.nextLine();


      switch (command) {
        case "sh db":
        case "show databases":
          this.showDatabases();
          break;

        case "info":
          this.showInfo();
          break;

        case "quit":
          break;
        default:

          String[] subcommand = command.split(" ");
          switch (subcommand[0]) {
            case "use":
              if (dbList().contains(subcommand[1])) {
                databaseManager db = new databaseManager(this.server, this.port, this.user, this.pass, subcommand[1]);
                db.startShell();
              } else {
                System.out.println(CC.RED_BOLD_BRIGHT + "La base de datos no existe.");
              }
              break;

            case "import":
              String[] sql = subcommand[1].split("\\.");
              if (sql.length < 2){
                System.out.println(CC.RED_BOLD_BRIGHT+"Tienes que especificar el nombre del archivo completo.");
                System.out.println(CC.YELLOW_BRIGHT+"Ej: import jugadores.sql"+CC.RESET);
              } else {
                this.importScript(subcommand[1]);
              }
              break;

            default:
              System.out.println(CC.RED + "Unknown option" + CC.RESET);
              break;

          }

      }

    } while (!command.equals("quit"));


  }


}