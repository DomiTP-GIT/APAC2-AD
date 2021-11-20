package com.ieseljust.ad.myDBMS;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

class databaseManager {

  String server;
  String port;
  String user;
  String pass;
  String dbname;

  databaseManager() {
    this.server = "127.0.0.1";
    this.port = "3308";
    this.user = "root";
    this.pass = "domi";
    this.dbname = "BDJocs";
  }

  databaseManager(String server, String port, String user, String pass, String dbname) {
    this.server = server;
    this.port = port;
    this.user = user;
    this.pass = pass;
    this.dbname = dbname;
  }

  public Connection connectDatabase() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      String connectionUrl = "jdbc:mysql://" + this.server + ":" + this.port + "/" + this.dbname + "?useUnicode=true&characterEncoding=UTF-8";

      return DriverManager.getConnection(connectionUrl, this.user, this.pass);

    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  public void showTables() {
    Connection conn = null;
    try {
      conn = connectDatabase();
      DatabaseMetaData metaData = conn.getMetaData();
      System.out.println(CC.GREEN_BOLD_BRIGHT + "\nTablas" + CC.RESET);
      ResultSet rsmd = metaData.getTables(this.dbname, null, null, null);
      while (rsmd.next()) {
        System.out.println(String.format("%-15s", rsmd.getString(3)));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } finally {
      try {
        if (!conn.isClosed())
          conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }


  public void insertIntoTable(String table) {
    // TO-DO: Afig informació a la taula indicada

    // Passos
    // 1. Estableix la connexió amb la BD
    // 2. Obtenim les columnes que formen la taula (ens interessa el nom de la columna i el tipus de dada)
    // 3. Demanem a l'usuari el valor per a cada columna de la taula
    // 4. Construim la sentència d'inserció a partir de les dades obtingudes
    //    i els valors proporcionats per l'usuari

    // Caldrà tenir en compte:
    // - Els tipus de dada de cada camp
    // - Si es tracta de columnes generades automàticament per la BD (Autoincrement)
    //   i no demanar-les
    // - Gestionar els diferents errors
    // - Si la clau primària de la taula és autoincremental, que ens mostre el valor d'aquesta quan acabe.
    ArrayList<String> bdTables = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> type = new ArrayList<>();
    ArrayList<Boolean> nullables = new ArrayList<>();
    ArrayList<Boolean> increment = new ArrayList<>();
    int nullCount = 0;

    Connection conn = null;
    try {
      // Conectar con DB y conseguir los meta datos
      conn = connectDatabase();
      DatabaseMetaData metaData = conn.getMetaData();

      ResultSet rsmd = metaData.getTables(this.dbname, null, null, null);
      while (rsmd.next()) {
        bdTables.add(rsmd.getString(3).toUpperCase());
      }
      rsmd.close();

      if (bdTables.contains(table.toUpperCase())) {
        // Obtener meta datos de las columnas de la tabla
        ResultSet columnes = metaData.getColumns(this.dbname, null, table, null);
        while (columnes.next()) {
          String name = columnes.getString(4);
          String tipus = columnes.getString(6);
          String nullable = columnes.getString(18);
          String autoincremental = columnes.getString(23);

          names.add(name);
          type.add(tipus);
          if (nullable.equalsIgnoreCase("YES")) {
            nullables.add(true);
            nullCount++;
          } else {
            nullables.add(false);
          }

          if (autoincremental.equalsIgnoreCase("YES")) {
            increment.add(true);
          } else {
            increment.add(false);
          }

        }

        columnes.close();

        // Añadir '?'
        StringBuilder sql = new StringBuilder("INSERT INTO " + table + " VALUES (");

        for (int i = 0; i < names.size() - 1; i++) {
          sql.append("?, ");
        }
        sql.append("?);");

        PreparedStatement pst = conn.prepareStatement(String.valueOf(sql));

        System.out.println(CC.CYAN_BOLD_BRIGHT + "INSERTAR DATOS" + CC.RESET);
        int cursor = 0;
        for (int i = 0; i < names.size(); i++) {
          if (increment.get(i)) {
            cursor++;
            pst.setInt(cursor, 0);
            System.out.println(CC.YELLOW_BOLD_BRIGHT + "El campo " + CC.PURPLE_BOLD_BRIGHT + names.get(i) + CC.YELLOW_BOLD_BRIGHT + " es autoincremental, no es necesario introducir datos." + CC.RESET);
          } else {
            cursor++;
            boolean check = true;
            if (nullables.get(i)) {
              System.out.print(CC.CYAN + "El campo " + names.get(i) + " puede ser nulo. ");
              check = Leer.leerBoolean("¿Quieres añadir un valor? (true/false): "+CC.YELLOW);
            }
            if (check) {
              String msg = CC.RED + "[" + CC.YELLOW + type.get(i) + CC.RED + "]" + CC.GREEN_BOLD_BRIGHT + " Introduce un valor para el campo " + CC.PURPLE_BOLD_BRIGHT + names.get(i) + CC.GREEN_BOLD_BRIGHT + ": " + CC.RESET;
              switch (type.get(i).toUpperCase()) {
                case "INT":
                  int intValue = Leer.leerEntero(msg);
                  pst.setInt(cursor, intValue);
                  break;
                case "DATETIME":
                  Date dateValue = Leer.leerFecha(msg);
                  pst.setDate(cursor, dateValue);
                  break;
                default:
                  String stringValue = Leer.leerTexto(msg);
                  pst.setString(cursor, stringValue);
                  break;
              }
            } else {
              switch (type.get(i).toUpperCase()) {
                case "INT":
                  pst.setInt(cursor, Integer.parseInt(""));
                  break;
                case "DATETIME":
                  pst.setDate(cursor, Date.valueOf(""));
                  break;
                default:
                  pst.setString(cursor, "");
                  break;
              }
            }
          }
        }

        int res = pst.executeUpdate();

        System.out.println("Resultado: " + res);
      } else {
        System.out.println(CC.RED_BOLD_BRIGHT + "La tabla no existe en la base de datos.");
      }

    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

  }


  public void showDescTable(String table) {
    Connection conn = null;
    try {
      conn = connectDatabase();
      DatabaseMetaData metaData = conn.getMetaData();
      System.out.println(CC.GREEN_UNDERLINED + String.format("%-25s %-15s %15s", "Nombre", "Tipo", "Nullable") + CC.RESET);

      ResultSet rspk = metaData.getPrimaryKeys(this.dbname, null, table);
      ArrayList<String> pks = new ArrayList<String>();

      while (rspk.next())
        pks.add(rspk.getString(4));

      rspk.close();

      ResultSet rsfk = metaData.getImportedKeys(this.dbname, null, table);
      ArrayList<String> fks = new ArrayList<String>();
      ArrayList<String> fksExt = new ArrayList<String>();
      while (rsfk.next()) {
        fks.add(rsfk.getString(8));
        fksExt.add(rsfk.getString(3));
      }

      rsfk.close();

      ResultSet columnes = metaData.getColumns(this.dbname, null, table, null);

      while (columnes.next()) {
        String columnName = columnes.getString(4);

        if (pks.contains(columnName))
          columnName += " (PK)";

        if (fks.contains(columnName))
          columnName += " (FK) --> " + fksExt.get(fks.indexOf(columnName));


        String tipus = columnes.getString(6);
        String nullable = columnes.getString(18);
        System.out.println(String.format("%-25s %-15s %15s",
            columnName,
            tipus,
            nullable));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } finally {
      try {
        if (!conn.isClosed())
          conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public void executeSelect(String query){
    Connection con = null;
    try {
      con = connectDatabase();
      ResultSet rs = con.createStatement().executeQuery(query);

      System.out.println(CC.PURPLE_BOLD_BRIGHT+"Resultado de la consulta:"+CC.GREEN_UNDERLINED);
      ResultSetMetaData rsm = rs.getMetaData();
      for (int i = 0; i < rsm.getColumnCount(); i++) {
        System.out.print(String.format("%-15s", rsm.getColumnName(i+1)));
      }
      System.out.println(CC.RESET+""+CC.BLUE_BOLD_BRIGHT);

      while (rs.next()){
        for (int i = 0; i < rsm.getColumnCount(); i++) {
          System.out.print(String.format("%-15s", rs.getString(i+1)));
        }
        System.out.println("");
      }
    } catch (Exception e){
      System.out.println(CC.RED_BOLD_BRIGHT+"Error al ejecutar la consulta."+CC.RESET);
    }
  }

  public void startShell() {

    Scanner keyboard = new Scanner(System.in);
    String command;

    do {

      System.out.print(CC.GREEN_BOLD_BRIGHT + "# (" + this.user + ") on " + this.server + ":" + this.port + " [" + CC.RED_BOLD_BRIGHT + dbname + CC.GREEN_BOLD_BRIGHT + "] > " + CC.RESET);
      command = keyboard.nextLine();


      switch (command) {
        case "sh tb":
        case "st tables":
        case "show tables":
          this.showTables();
          break;

        case "quit":
          break;
        default:
          // Com que no podem utilitzar expressions
          // regulars en un case (per capturar un "use *")
          // busquem aquest cas en el default:

          String[] subcommand = command.split(" ");
          switch (subcommand[0]) {
            case "describe":
              this.showDescTable(subcommand[1]);
              break;
            case "insert":
              this.insertIntoTable(subcommand[1]);
              break;
            case "select":
              this.executeSelect(command);
              break;

            default:
              System.out.println(CC.RED + "Unknown option" + CC.RESET);
              break;

          }

      }

    } while (!command.equals("quit"));

  }


}