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

    public static String validarProducto(String name, String price) {

        if (name == null || name.isEmpty()) {
            return "El nombre del producto es obligatorio";
        }

        if (price == null || price.isEmpty()) {
            return "El precio es obligatorio";
        }

        double valorPrecio;
        try {
            valorPrecio = Double.parseDouble(price);
            if (valorPrecio<=0) {
                return "El precio debe ser mayor a 0";
            }
        } catch (NumberFormatException e) {
            return "El precio debe ser un número válido";
        }

        return "Validado";
    }

    public static String validarCliente(String dni, String name, String address) {

        if (dni == null || dni.isEmpty()) {
            return "El DNI del cliente es obligatorio";
        }

        if (!dni.matches("[0-9]+")) {
            return "El DNI del cliente debe ser numérico";
        }

        if (name == null || name.isEmpty()) {
            return "El nombre del cliente es obligatorio";
        }

        if (address == null || address.isEmpty()) {
            return "La dirección es obligatoria";
        }

        return "Validado";
    }

    public static String validarVendedor(String dni, String name, String phone, String user) {

        if (dni == null || dni.isEmpty()) {
            return "El DNI es obligatorio";
        }
        if (!dni.matches("\\d+")) {
            return "El DNI debe ser numérico";
        }

        if (name == null || name.isEmpty()) {
            return "El nombre es obligatorio";
        }


        if (phone == null || phone.isEmpty()) {
            return "El teléfono es obligatorio";
        }
        if (!phone.matches("[0-9]+")) {
            return "El teléfono debe ser numérico";
        }

        if (user == null || user.isEmpty()) {
            return "El usuario es obligatorio";
        }

        return "Validado";
    }

}
