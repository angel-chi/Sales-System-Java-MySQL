package org.borghisales.salessysten.utils;

public class InputValidator {

    public static String validarDatosVenta(String customerName, String productName, String price, Integer quantity) {
        if (customerName == null || customerName.isEmpty()) {
            return "El código del cliente es requerido";
        }
        if (productName == null || productName.isEmpty()) {
            return "El código del producto es requerido";
        }
        if (price == null || price.isEmpty()) {
            return "El precio es requerido";
        }

        double numero;
        try {
            numero=Double.parseDouble(price);
            if (numero<=0) {
                return "El precio debe ser mayor a 0";
            }
        } catch (NumberFormatException e) {
            return "El precio debe ser un número válido";
        }

        if (quantity == null || quantity <= 0) {
            return "La cantidad debe ser mayor a 0";
        }
        return "Validado";
    }

    public static String validarCódigoCliente(String code) {
        if (code == null || code.isEmpty()) {
            return "El código del cliente no puede ser nulo";
        }
        if (!code.matches("[0-9]+")) {
            return "El código del cliente debe ser numérico";
        }
        return "Validado";
    }

    public static String validarCódigoProducto(String code) {
        if (code == null || code.isEmpty()) {
            return "El código del producto no puede ser nulo";
        }
        if (!code.matches("[0-9]+")) {
            return "El código del producto debe ser numérico";
        }
        return "Validado";
    }


}
