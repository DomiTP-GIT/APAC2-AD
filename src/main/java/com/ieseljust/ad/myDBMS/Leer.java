package com.ieseljust.ad.myDBMS;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class Leer {
    private final static BufferedReader entradaConsola = new BufferedReader(new java.io.InputStreamReader(System.in));

    public static String leerTexto(String mensaje) {
        String respuesta = null;
        do {
            try {
                System.out.print(mensaje);
                respuesta = entradaConsola.readLine();
            } catch (IOException ex) {
                return "";
            }
        } while (respuesta == null);
        return respuesta;
    }

    public static char leerChar(String mensaje) {
        char respuesta = ' ';
        do {
            try {
                System.out.print(mensaje);
                respuesta = (char) entradaConsola.read();
            } catch (IOException ex) {
                return ' ';
            }
        } while (respuesta == ' ');
        return respuesta;
    }

    public static int leerEntero(String mensaje) {
        int n = 0;
        boolean correcto = false;
        while (!correcto) {
            try {
                n = Integer.parseInt(leerTexto(mensaje));
                correcto = true;
            } catch (NumberFormatException ex) {
                System.out.println("Tienes que introducir un número correcto");
            }
        }
        return n;
    }

    public static double leerDouble(String mensaje) {
        double n = 0.0;
        boolean correcto = false;
        while (!correcto) {
            try {
                n = Double.parseDouble(leerTexto(mensaje));
                correcto = true;
            } catch (NumberFormatException ex) {
                System.out.println("Tienes que introducir un número correcto");
            }
        }
        return n;
    }

    public static float leerFloat(String mensaje) {
        float n = 0;
        boolean correcto = false;
        while (!correcto) {
            try {
                n = Float.parseFloat(leerTexto(mensaje));
                correcto = true;
            } catch (NumberFormatException ex) {
                System.out.println("Tienes que introducir un número correcto");
            }
        }
        return n;
    }

    public static Date leerFecha(String mensaje) {
        java.util.Date fecha = new java.util.Date();
        DateFormat formatar = new SimpleDateFormat("yyyy-MM-dd");
        boolean correcto = false;
        while (!correcto) {
            try {
                String entrada = leerTexto("Formato yyyy-MM-dd: ");
                fecha = formatar.parse(entrada);
                correcto = true;
            } catch (NumberFormatException | ParseException ex) {
                System.out.println("Tienes que introducir una fecha correcta.");
            }
        }
        return (Date) fecha;
    }

    public static boolean leerBoolean(String mensaje) {
        boolean n = false;
        n = Boolean.parseBoolean(leerTexto(mensaje));
        return n;
    }
}
