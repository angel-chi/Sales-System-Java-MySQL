<h1 align="center" id="title"> Sistema de ventas en Java FX</h1>
<h6 align="center"> Aplicación de Gestión de Ventas con Java 17, JavaFX, MySQL y Patrones de Diseño MVC y DAO </h6>
<h1></h1>

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

<p align="center">
  <img src="src/main/resources/images/shopping cart.png" />
</p>

<!-- TOC -->
* [📑 Descripcion](#-descripcion)
* [💻 Entorno](#-entorno)
* [🚀 Instalacion](#-instalacion)
    * [Instalacion del JDK 17](#instalación-del-jdk-17)
    * [Configuracion de la Base de Datos](#configuración-de-la-base-de-datos)
    * [Ejecucion del Proyecto](#ejecución-del-proyecto)
    * [Modificacion de las Vistas con Scene Builder](#modificación-de-las-vistas-con-scene-builder)
* [🧬 Estructura Basica](#-estructura-basica)
* [🗄️ Diagrama de Base de Datos](#-diagrama-de-base-de-datos)
* [💡 Fucionalidades](#-fucionalidades)
    * [Inicio de Sesion](#inicio-de-sesión)
    * [Pantalla Principal](#pantalla-principal)
        * [Sales](#sales)
        * [Management](#management)
        * [Reports](#reports)
* [📧 Contacto](#-contacto)
* [📝 Licencia](#-licencia)
<!-- TOC -->

# 📑 Descripcion
Este proyecto es una herramienta que diseñé para mejorar mis habilidades con el lenguaje Java, centrándome en la gestión de ventas para vendedores. Utiliza los patrones de diseño MVC (Modelo-Vista-Controlador) y DAO (Data Access Object) para una arquitectura robusta y modular.

Con esta aplicación, puedes iniciar sesión como vendedor, administrar tus productos y clientes, así como realizar ventas de manera sencilla. Además, cuenta con una sección de reportes donde puedes ver detalles de tus ventas, filtrarlas y generar informes personalizados.

Todos los datos se almacenan de forma segura en una base de datos MySQL, utilizando el patrón DAO para separar la lógica de acceso a datos de la lógica de negocio. Esto garantiza un código más limpio, mantenible y escalable.

Además, hay una sección de estadísticas que te muestra cuántas ventas has realizado de cada producto y cómo han variado a lo largo del tiempo, utilizando el patrón MVC para separar la lógica de presentación de la lógica de negocio y la manipulación de datos.

# 💻 Entorno

Este proyecto requiere las siguientes herramientas y versiones:

* SO: Windows <br>
* Java: 17<br>
* Maven: 3.8.5<br>
* MySQL: 8.0.33<br>
* JavaFX: 21


# 🚀 Instalacion
Para utilizar este proyecto, simplemente clona el repositorio en tu máquina local y sigue estos pasos:

## Instalación del JDK 17
Para ejecutar este proyecto, necesitarás tener instalado el JDK 17. Sigue estos pasos para instalarlo:

1. Descarga del JDK 17: Visita la página de descargas de Oracle JDK en https://www.oracle.com/java/technologies/javase-jdk17-downloads.html.
2. Selecciona tu sistema operativo: Descarga la versión adecuada del JDK 17 para tu sistema operativo. Asegúrate de seleccionar la versión correcta para Windows.
3. Instalación: Una vez descargado el archivo de instalación, sigue las instrucciones proporcionadas por Oracle para instalar el JDK 17 en tu sistema.
4. Configuración de las Variables de Entorno (Opcional): Después de instalar el JDK 17, puedes configurar las variables de entorno JAVA_HOME y PATH en tu sistema para que apunten al directorio de instalación del JDK. Esto facilitará el uso del JDK desde la línea de comandos.

## Configuración de la Base de Datos
Antes de ejecutar el proyecto, asegúrate de configurar la base de datos:

1. Instala MySQL: Si aún no tienes MySQL instalado, descárgalo e instálalo desde https://dev.mysql.com/downloads/mysql/.
2. Crea la Base de Datos: Utiliza el script proporcionado llamado salesystem.sql para importar la base de datos y las tablas necesarias.
3. Configura la Conexión: Para configurar la conexión a la base de datos, sigue estos pasos:
   * Crea un archivo llamado config.properties en la ruta src/main/java/org/borghisales/salessystem/model/.
   * Define las propiedades de configuración para la conexión a la base de datos en el archivo config.properties. 
   * Las propiedades necesarias son db.url, db.user y db.password. Por ejemplo:
   <pre>
   db.url=jdbc:mysql://localhost:3306/salesystem
   db.user=usuario
   db.password=contraseña
   </pre>
   
   Asegúrate de reemplazar nombre_basedatos, usuario y contraseña con los valores correspondientes de tu entorno de desarrollo.
## Ejecución del Proyecto
Una vez que hayas configurado la base de datos, puedes ejecutar el proyecto siguiendo estos pasos:

1. Clona el Proyecto: Clona este repositorio en tu máquina local utilizando Git o descargando el archivo ZIP.
2. Importa el Proyecto: Importa el proyecto en tu IDE preferido (como IntelliJ, Eclipse, etc.) como un proyecto Maven existente.
3. Verifica las Dependencias: Antes de compilar y ejecutar el proyecto, asegúrate de que todas las dependencias estén resueltas correctamente. Esto se puede hacer actualizando Maven o ejecutando el comando mvn clean install desde la línea de comandos en el directorio del proyecto. Esto garantizará que todas las dependencias se descarguen y configuren correctamente.
4. Compila y Ejecuta: Compila y ejecuta el proyecto desde tu IDE. Asegúrate de ejecutar la clase principal adecuada (si es necesario) para iniciar la aplicación.

## Modificación de las Vistas con Scene Builder
Si deseas modificar las vistas de la aplicación, puedes utilizar Scene Builder, una herramienta gráfica para diseñar interfaces de usuario JavaFX. Para instalar Scene Builder, sigue estos pasos:

1. Descarga Scene Builder: Puedes descargar Scene Builder desde el sitio web oficial de Gluon https://gluonhq.com/products/scene-builder/.
2. Instalación: Una vez descargado, sigue las instrucciones de instalación para tu sistema operativo.

# 🧬 Estructura Basica
<pre>
+ java
  |-- controllers // controladores de la aplicacion
  |-- model	// modelos de datos de la aplicación
  --Main.java // donde se inicia la ejecución del programa      
+ Resources
  |-- images // imágenes utilizadas en la aplicación
  |-- views // vistas de la aplicación fxml
  |-- reports // informes generados por la aplicación
</pre>

# 🗄️ Diagrama de Base de Datos
<p align="center">
  <img src="src/main/resources/images/diagramaBD.png" />
</p>


# 💡 Fucionalidades

## Inicio de sesión
Para iniciar sesión, se requiere el DNI y la contraseña del vendedor. En la base de datos, estos corresponden a los atributos del vendedor(seller), donde el DNI se asocia con 'dni' y la contraseña con 'user'.

<p align="center">
  <img src="src/main/resources/images/login.png" />
</p>

## Pantalla principal
La pantalla principal muestra las siguientes ventanas
* Menu: incluyen la posibilidad de salir o visitar la documentación
<p align="center">
  <img src="src/main/resources/images/menu.png" />
</p>

* Sales: Permite generar nuevas ventas.
<p align="center">
  <img src="src/main/resources/images/sales.png" />
</p>

* Management: Ofrece operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para clientes, productos y vendedores.
<p align="center">
  <img src="src/main/resources/images/management.png" />
</p>

* Reports: Aquí se encuentran las operaciones de reportes y estadísticas relacionadas.
<p align="center">
  <img src="src/main/resources/images/reports.png" />
</p>

## Sales
https://github.com/Borghii/Sales-System/assets/137845283/60872beb-31af-47b0-b84d-83f9b4807ac5
## Management
https://github.com/Borghii/Sales-System/assets/137845283/4f85ec7c-f2de-44ae-815b-218c9ca25b10
## Reports
https://github.com/Borghii/Sales-System/assets/137845283/f85f1026-6693-4152-a793-6bfe02a8869f

# 📧 Contacto
Si tienes alguna pregunta, sugerencia o crítica sobre el proyecto, no dudes en contactarme por correo electrónico a [tomasborghi13@gmail.com](mailto:tomasborghi13@gmail.com).

# Entrega del proyecto
## Errores encontrados
### Archivo faltante (config.properties)
No se notificaba correctamente al usuario de que configue el archivo config.properties
#### Solucion
Crear una ventana que notifique al usuario para crear y configurar el archivo, además en el código se intentaban realizar acciones en la base de datos sin verificar si la conexión era válida.

la solución fue verifica si la conexión no era nula

```java
try (Connection conn = DBConnection.connection()){
        if(conn ==null){
        return false;
        }
        ...
}
```

### Error al buscar vacío
En algunas entradas se convierte una cadena en un número entero, al hacer este proceso si la cadena era vacía o no contenía un número válido no ejecutaba nada y el usuario no era notificado de este error, se solucionó agregando esas validaciones

```java
if(codCustomer.getText().isBlank()) {
    setAlert(Alert.AlertType.ERROR, "El texto a buscar es una cadena vacía");
    return;
}
int customerId;

try {
    customerId = Integer.parseInt(codCustomer.getText());
}
catch(NumberFormatException e) {
    setAlert(Alert.AlertType.ERROR, "El texto a buscar no es un número entero.\nPor favor ingrese únicamente números.");
    return;
}
```

### Agregar usuario vacío
Al agregar un cliente se permitía agregar un cliente con información vacía. Se agregó una validación para evitar ese caso
```java
String usuario = dni.getText();
String nombre = name.getText();
String direccion = address.getText();

if(usuario.isBlank()) {
    MenuController.setAlert(Alert.AlertType.ERROR, "Error al agregar\nEl usuario está en blanco");
    return;
}

if(nombre.isBlank()) {
    MenuController.setAlert(Alert.AlertType.ERROR, "Error al agregar\nEl nombre está en blanco");
    return;
}

if(direccion.isBlank()) {
    MenuController.setAlert(Alert.AlertType.ERROR, "Error al agregar\nLa dirección está en blanco");
    return;
}

Customer customer = new Customer(usuario, nombre, direccion, cbState.getValue());
```

Lo mismo sucedía con los productos y con los empleados, se usó la misma solución

## Propuestas
### Agregar atributo garantía a los productos
Agregarle garantía a los productos que se venden, debido a que son productos tecnológicos en la gran mayoría de los casos los artículos vienen con garantía para cubrir cualquier fallo que pudiera presentarse en un periodo de tiempo.

Esto ayuda con la abstracción de los objetos, debido a que es una propiedad importante para los productos.

## Aplicación de propuestas
### Atributo garantía
Para implementarlo se tuvo que crear una nueva clase (en este caso un ENUM) que representara los tipos de garantía
```java
public enum Garantia {
    SIN_GARANTIA("SIN GARANTIA"), MES_1("1 MES"), MESES_3("3 MESES"), MESES_6("6 MESES"), ANO_1("1 AÑO"), ANO_2("2 AÑOS");

    final private String text;

    Garantia(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static Garantia getFromString(String str) {
        return switch(str) {
            case "1 MES" -> MES_1;
            case "3 MESES" -> MESES_3;
            case "6 MESES" -> MESES_6;
            case "1 AÑO" -> ANO_1;
            case "2 AÑOS" -> ANO_2;
            default -> SIN_GARANTIA;
        };
    };

    @Override
    public String toString() {
        return text;
    }
}
```
Este enum se encuentra dentro de la clase Product.

Además de ello, se tuvo que adaptar diversas funciones para lograr que el programa registrara y leyera los datos en la base de datos.

Y para una mejor integración con la interfaz de usuario se requirió agregar un overload a la función de buscar producto, para poder buscar por nombre de producto en lugar de por ID.
```java
public class ProductDao implements CRUD<Product> {
    ...

    public Product searchProduct(String productName) {
        String sql = "SELECT * FROM product WHERE name = ?";

        try (Connection conn = DBConnection.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productName);

            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return Product.fromResultSet(rs);
            }
        } catch (SQLException e) {
            return null;
        }
    }
    ...
}
```

# 📝 Licencia

Este proyecto está bajo licencia. Ver el archivo [LICENSE](LICENSE) para más detalles.

[⬆ Volver al inicio](#title)<br>
