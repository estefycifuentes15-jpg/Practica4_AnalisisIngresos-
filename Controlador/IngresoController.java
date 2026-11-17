package Controlador;

import Modelo.Aportador;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IngresoController {
    private ArrayList<Aportador> listaAportadores;
    private int erroresCedula = 0;
    private int erroresCorreo = 0;
    private int erroresMonto = 0;

    public IngresoController() {
        listaAportadores = new ArrayList<>();
    }

    public void procesarArchivo(String rutaArchivo) {
        listaAportadores.clear();
        erroresCedula = 0;
        erroresCorreo = 0;
        erroresMonto = 0;

        try {
            File archivo = new File(rutaArchivo);
            Scanner scanner = new Scanner(archivo);

            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();

                Pattern patCedula = Pattern.compile("\\b(\\d{8,10})\\b");
                Pattern patCorreo = Pattern.compile("([\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6})");
                Pattern patMonto = Pattern.compile("(\\$?\\s*\\d+(\\.\\d{1,2})?)\\s*(dólares|USD)?", Pattern.CASE_INSENSITIVE);

                Matcher matCedula = patCedula.matcher(linea);
                Matcher matCorreo = patCorreo.matcher(linea);
                Matcher matMonto = patMonto.matcher(linea);

                if (matCedula.find() || matCorreo.find()) {
                    
                    matCedula.reset();
                    matCorreo.reset();

                    String cedulaStr = matCedula.find() ? matCedula.group(1) : "";
                    String correoStr = matCorreo.find() ? matCorreo.group(1) : "";
                    
                    double montoVal = 0.0;
                    if (matMonto.find()) {
                        String montoLimpio = matMonto.group(1).replace("$", "").trim();
                        try {
                            montoVal = Double.parseDouble(montoLimpio);
                        } catch (NumberFormatException e) {
                            montoVal = 0.0;
                        }
                    }

                    if (!cedulaStr.isEmpty() || !correoStr.isEmpty()) {
                        Aportador ap = new Aportador(cedulaStr, correoStr, montoVal);

                        boolean cedulaValida = cedulaStr.matches("\\d{10}");
                        boolean correoValido = correoStr.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
                        boolean montoValido = montoVal > 0;

                        ap.setCedulaValida(cedulaValida);
                        ap.setCorreoValido(correoValido);
                        ap.setMontoValido(montoValido);

                        if (!cedulaValida) erroresCedula++;
                        if (!correoValido) erroresCorreo++;
                        if (!montoValido) erroresMonto++;

                        listaAportadores.add(ap);
                    }
                }
            }
            scanner.close();
            System.out.println("¡Archivo procesado y validado exitosamente!");
        } catch (FileNotFoundException e) {
            System.out.println("Error: No se encontró el archivo " + e.getMessage());
        }
    }

    public ArrayList<Aportador> getListaAportadores() {
        return listaAportadores;
    }

    public double calcularTotalValidos() {
        double total = 0;
        for (Aportador a : listaAportadores) {
            if (a.isCedulaValida() && a.isCorreoValido() && a.isMontoValido()) {
                total += a.getMonto();
            }
        }
        return total;
    }

    public String obtenerResumenErrores() {
        return "Resumen de Errores -> Cédulas erróneas: " + erroresCedula + 
               " | Correos erróneos: " + erroresCorreo + 
               " | Montos erróneos: " + erroresMonto;
    }
}