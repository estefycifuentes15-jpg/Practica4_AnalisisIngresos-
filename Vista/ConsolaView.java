package Vista;

import Controlador.IngresoController;
import java.util.Scanner;

public class ConsolaView {
    public void iniciarMenu() {
        Scanner scanner = new Scanner(System.in);
        IngresoController controller = new IngresoController();
        int opcion;

        do {
            System.out.println("\n....... ANALISIS DE INGRESOS .......");
            System.out.println("1. Cargar y procesar 'registro_ingresos.txt'");
            System.out.println("2. Mostrar listado de aportadores y validaciones");
            System.out.println("3. Mostrar total de ingresos válidos y resumen de errores");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    controller.procesarArchivo("registro_ingresos.txt");
                    break;
                case 2:
                    System.out.println("\n--- LISTADO DE APORTADORES ---");
                    for (var ap : controller.getListaAportadores()) {
                        System.out.println(ap);
                    }
                    break;
                case 3:
                    System.out.println("\n--- RESULTADOS FINALES ---");
                    System.out.println("Total Ingresos Válidos: $" + controller.calcularTotalValidos());
                    System.out.println(controller.obtenerResumenErrores());
                    break;
                case 4:
                    System.out.println("Saliendo de la aplicación...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 4);
        scanner.close();
    }
}