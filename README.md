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
Este proyecto es una herramienta para mejorar nuestras habilidades con el lenguaje Java y el paradigma de Programación Orientada a Objetos, centrándonos en la gestión de ventas para vendedores. Utiliza los patrones de diseño MVC (Modelo-Vista-Controlador) y DAO (Data Access Object) para una arquitectura robusta y modular.

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

## Instalación de Maven<br>
Para instalar Maven haga clic en la siguiente liga y siga los pasos según su sistema https://maven-apache-org.translate.goog/install.html?_x_tr_sl=en&_x_tr_tl=es&_x_tr_hl=es&_x_tr_pto=tc.<br><br>
Para instalar Maven en Linux, sigue estos pasos:
1. En la terminal ejecute: `sudo apt install maven`<br> 
2. Para verificar la correcta instalación ejecute: `mvn -v`, debería ver la versión junto con la de java.


## Configuración de la Base de Datos
Antes de ejecutar el proyecto, asegúrate de configurar la base de datos:

1. Instala MySQL: Si aún no tienes MySQL instalado, descárgalo e instálalo desde https://dev.mysql.com/downloads/mysql/.
2. Crea la Base de Datos:<br>
- En el directorio del proyecto ejecuta `mysql -u tu_usuario -p` y proporciona tu contraseña cuando se te solicite.<br>
- Ejecuta `CREATE DATABASE salesystem;` para crear la base de datos.<br>
- Ejecuta `USE salesystem;` para seleccionar la base de datos recién creada.<br>
Utiliza el script proporcionado llamado salesystem.sql para importar la base de datos y las tablas necesarias.
3. Configura la Conexión: Para configurar la conexión a la base de datos, sigue estos pasos:
   * Crea un archivo llamado config.properties en la ruta src/main/java/org/borghisales/salessystem/model/.
   * Define las propiedades de configuración para la conexión a la base de datos en el archivo config.properties. 
   * Las propiedades necesarias son db.url, db.user y db.password. Por ejemplo:
   <pre>
   db.url=jdbc:mysql://localhost:3306/salesystem
   db.user=usuario
   db.password=contraseña
   </pre>
   
## Ejecución del Proyecto
Una vez que hayas configurado la base de datos, puedes ejecutar el proyecto siguiendo estos pasos:

1. Clona el Proyecto: Clona este repositorio en tu máquina local utilizando Git o descargando el archivo ZIP.
2. Importa el Proyecto: Importa el proyecto en tu IDE preferido (como IntelliJ, Eclipse, etc.) como un proyecto Maven existente.
3. Verifica las Dependencias: Antes de compilar y ejecutar el proyecto, asegúrate de que todas las dependencias estén resueltas correctamente. Esto se puede hacer actualizando Maven o ejecutando el comando mvn clean install desde la línea de comandos en el directorio del proyecto. Esto garantizará que todas las dependencias se descarguen y configuren correctamente.<br><br>
Para poder ejecutar desde línea de comandos debe agregar el plugin de javafx en el archivo pom.xml:
```xml
<plugin>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-maven-plugin</artifactId>
    <version>0.0.8</version>
    <executions>
        <execution>
            <!-- Default configuration for running with: mvn clean javafx:run -->
            <id>default-cli</id>
            <configuration>
                <mainClass>org.borghisales.salessysten.Main</mainClass>
                <launcher>app</launcher>
                <jlinkZipName>app</jlinkZipName>
                <jlinkImageName>app</jlinkImageName>
                <noManPages>true</noManPages>
                <stripDebug>true</stripDebug>
                <noHeaderFiles>true</noHeaderFiles>
            </configuration>
        </execution>
    </executions>
</plugin>
```
4. Compila y Ejecuta: Compila y ejecuta el proyecto desde tu IDE. Asegúrate de ejecutar la clase principal adecuada (si es necesario) para iniciar la aplicación.<br>

### Para ejecutar el proyecto desde la línea de comandos:
1. Abre una terminal o línea de comandos.
2. Navega al directorio del proyecto.
3. Ejecuta `mvn clean install` para compilar el proyecto y resolver las dependencias.
4. Ejecuta `mvn javafx:run` para iniciar la aplicación JavaFX.

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

# Diagrama UML
<p align="center">
  <img src="src/main/resources/images/UML.png" />
</p>

# 🚫 Errores econtrados
Los siguientes errores fueron econtrados duerante el desarrollo del proyecto en nuestras manos.

**1. Tamaño de las ventanas en la interfaz:** Las ventanas de la interfaz no tenían el tamaño adecuado, para que el usuario pudiera navegar de forma cómoda y fácil.

**Solución:**<br>En el MenuController, dentro del try del método openNewStage se agregó primero un Parent root para obtener la raíz del layout principal del FXML, se declara una escena y se crea una ventana.
```java
Parent root = fxmlLoader.load();

Scene scene;
Stage stage = new Stage();  
```
Se crea un switch para ajustar cada ventana a una medida exacta y se deshabilita que el usuario pueda cambiar el tamaño de la ventana con su cursor.
```java
switch (fxmlFileName) {
   case MAIN_VIEW_FXML, MANAGEMENT_VIEW_FXML:
       scene = new Scene(root, 800, 600);
       stage.setResizable(false);
       break;
       
   case GENERATE_SALE_VIEW_FXML:
       scene = new Scene(root, 590, 600);
       stage.setResizable(false);
       break;

   case REPORT_VIEW_FXML:
       scene = new Scene(root, 1470, 1040);
       stage.setResizable(false);
       break;

   case PRODUCT_VIEW_FXML:
       scene = new Scene(root, 650, 500);
       stage.setResizable(false);
       break;

   case SELLER_VIEW_FXML:
       scene = new Scene(root, 700, 500);
       stage.setResizable(false);
       break;

   case CUSTOMER_VIEW_FXML:
       scene = new Scene(root, 665, 510);
       stage.setResizable(false);
       break;

       // Si ningún título coincide, usa tamaño genérico
  default:
      scene = new Scene(root, 800, 600);
}
```
**2. Título de ventanas:** Cuando se está en una ventana que no es Iniciar Sesión o Gestión, al cerrarla en vez de mostar alguno de estos dos títulos mostraba el título de la ventana anterior.<br><br>
**Solución:**<br>
En MenuController, dentro del método configureStageCloseEvent se agregó una variable parentFxml que guarda el archivo Fxml padre del actual.
```java
if (!fxmlFileName.equals(MAIN_VIEW_FXML)) {
   stage.setOnCloseRequest(e -> {
       //Se obtiene la ventana padre
       String parentFxml = getFxmlFather(fxmlFileName);
       if (parentFxml != null) {
           //Aseguramos que el título también se pase correctamente
           openNewStage(parentFxml, getParentTittle(parentFxml));
       }
   });
}
```
Se creo un método nuevo llamado getParentTittle para determinar el título de la ventana padre, que en para efectos del código, solo son dos.
```java
private String getParentTittle(String fxmlFileName) {
    //Nombre de las ventanas padres (ventanas que se ven al cerrar otras)
    switch (fxmlFileName) {
       case MAIN_VIEW_FXML:
            return "Inicio de Sesión"; 
       case MANAGEMENT_VIEW_FXML:
           return "Gestión";
      default:
          return "Ventana desconocida";
    }
}
```
**3. Botón de Ayuda:** Cuando se le daba clic al botón de ayuda la ejecución del programa se detenia, el código crasheaba.<br><br>
**Solución:**<br>
Se agregó Thread para ejecutar la tarea en un hilo aparte del de JavaFX para que la interfaz siga respondiendo. Además con la clase Desktop se verifica que el entorno pueda soportar la tarea de abrir URLs.
```java
public void help(ActionEvent actionEvent) {
    //Se usa Thread para no bloquear la interfaz gráfica
    new Thread(() -> {
        try {
            //Se verifica que el entorno pueda soportar la acción del redireccionamiento
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("https://github.com/angel-chi/Sales-System-Java-MySQL/blob/Basulto-Maga%C3%B1a/README.md"));
            }
            //En caso de que ocurra una excepción se muestra un mensaje
        } catch (Exception e) {
            e.printStackTrace();
            javafx.application.Platform.runLater(() ->
                    setAlert(Alert.AlertType.ERROR, "No se pudo abrir la URL.")
            );
        }
    }).start();
}
```
***4. Botón agregar producto:*** Cuando se le daba a agregar producto en la vista de Venta si no se tenía el lenguaje del entorno en español, soltaba el error NumberFormatException.<br><br>
**Solución:**<br>
El problema se daba debido a que el decimal se ponía como ',' y no como '.', el parseo de String a double lanzaba la excepción.<br>
Primero en la clase CarritoCompra se modificó el argumento de precio en el constructor para que se maneje como un BigDecimal y luego se convierta a double.
```java
import java.math.BigDecimal;
import java.math.RoundingMode;
```
```java
BigDecimal.valueOf(precio).multiply(BigDecimal.valueOf(cantidad)).setScale(2, RoundingMode.HALF_UP).doubleValue()
```
Luego en el controlador GenerateSaleController se importó la clase Locale, para que el valor use '.' sin importar el lenguaje.
```java
import java.util.Locale;
```
Dentro de esta misma clase se modificó el método initializeUIElements agregando una línea para setear el valor de total con '0.0'.
```java
total.setText(String.format(Locale.US, "%.2f", 0.0));
```
En el método cancel se quita total.clear() para así setear el valor de total a '0.0'.
```java
//Se deja la variable en 0.0 para que no tengamos problemas con contenido vacío
total.setText(String.format(Locale.US, "%.2f", 0.0));
```
En el método createSalesObject ahora se tiene una variable que reemplaza ',' por '.' así como otra que parsea el String a double.
```java
private Venta createSalesObject() { // Sales -> Venta
    //Se reemplaza la coma por punto decimal
    String totalText = total.getText().trim().replace(',', '.');
    //Se convierte a double
    double totalValue = Double.parseDouble(totalText);
    return new Venta(cliente.getId(), idVendedor, serial.getText(), 
            LocalDate.parse(date.getText()), totalValue,
            Venta.Estado.ACTIVO);
}
```
Por último en el método addToCartAndUpdateTotal ahora se asegura que se escriba no con ',' sino con '.' y se cumple que siempre se escriba con '.'.
```java
private void addToCartAndUpdateTotal(CarritoCompra producto) { 
    productosEnCarrito.add(producto);
    tableSale.setItems(productosEnCarrito); 
    //Para asegurar se reemplaza una coma por punto decimal
    String totalText = total.getText().trim().replace(',','.');
    double currentTotal = Double.parseDouble(totalText) + producto.total(); 
    //Siempre escribimos usando Locale.US
    total.setText(String.format(Locale.US, "%.2f", currentTotal));
}
```

# 🔧 Propuestas de Mejora

**1. Clase abstracta "Usuario":**<br><br>
¿Por qué?<br>
Las clases Cliente y Vendedor comparten algunos campos, una forma de ahorrar código sería creando una nueva clase abstracta "Usuario". Es abstracta porque
no se necesita una instancia de "Usuario" solo servirá de plantilla para saber de qué se compone un Cliente y un Vendedor. Es por esto que Cliente y Vendedor heredaran de esta clase.<br><br>
**Relación con POO**<br>
Abstracción: Tener una clase Usuario refleja el concepto general de un cliente y un vendedor.<br>
Encapsulamiento: Los atributos comunes de Usuario estan protegidos y solo se acceden a ellos por medio de getters.<br>
Herencia: Cliente y Vendedor heredan de esta clase, adquiriendo automáticamente todo lo definido por Usuario.<br><br>
La clase "Usuario" debe tener, por ejemplo:
- Un identificador.
- Un nombre.
- El enum de Estado.  
- Sus respectivos constructores y getters.

**2. Rol "Aministrador" para la clase Vendedor:**<br><br> 
¿Por qué?<br>
En cuanto a la seguridad del sistema, es más congruente que los vendedores no tengan acceso a "Productos", es decir, al stock. Un vendedor común no debe tener la capacidad de agregar, eliminar y actualizar un producto, porque esto podría causar conflictos.<br>
Lo más normal es que un administrador pueda hacerlo, por lo tanto, sugiero agregar un atributo de rol a los vendedores para saber si es "Vendedor" o "Administrador", si es el segundo, podrá hacer lo que ya hacía un vendedor, pero tendrá acceso al stock y de cambiar los roles. Con esto dicho, la interfaz no será la misma para un "Vendedor" que para un "Administrador"<br><br>
**Relación con POO:**<br>
Abstracción: El rol representa una característica importante del vendedor, si es un "Vendedor" normal o un "Administrador" con más permisos.<br>
Encapsulamiento: Las reglas de quién puede cambiar ese rol se definen dentro de las clases.

**3. Etiqueta del producto más vendido por el vendedor logeado:**<br><br>
¿Por qué?<br>
Agregar en la lógica de clases un método que permita determinar el producto más vendido por el vendedor que inició sesión.<br>
Se deberá mostrar la etiqueta en la interfaz de Venta, para que al estar generando una, se le pueda notificar al Cliente.<br><br>
**Relación con POO:**<br>
Abstracción: La abstracción permite que el controlador use este método sin saber nada de SQL ni bases de datos.<br>
Encapsulamiento: Todo el manejo de BD (conexiones, queries, errores) está oculto dentro del método del DAO.

# 🖥️ Implementaciones

**1. Clase abstracta "Usuario"**<br>
Se ecuentra agregada en el código, sin embargo, es algo que no se puede presenciar en la interfaz. Pero se agregó para que el código quede mejor estructurado. La clase cuenta con los aspectos mencionados del apartado anterior y se modificaron las clases Cliente y Vendedor, para que estas puedan heredar de Usuario.<br>
Con la introducción de la clase abstracta Usuario, tanto Cliente como Vendedor heredaron los atributos y comportamientos generales del sistema como id, nombre y estado lo que permitió simplificar sus clases y evitar duplicación de código. A partir de esto, cada modelo se enfoca únicamente en sus características específicas: Cliente conserva datos propios como correo y direccion, mientras que Vendedor maneja información adicional como identificacion, telefono, usuario y el atributo rol, que define si el usuario es vendedor o administrador. Esta reorganización hace que el modelo sea más limpio, coherente y alineado con los principios de abstracción y reutilización de la programación orientada a objetos.<br>
Tienen sus propios getters para los nuevos atributos así como sus constructores que llaman al constructor de la clase padre Usuario. Tanto Cliente como Vendedor tienen su método Factory estático para usar desde su versión en el DAO.

**2. Rol "Administrador" para la clase Vendedor**<br>
En la clase Vendedor se implementa un sistema de roles mediante un enum Rol { VENDEDOR, ADMINISTRADOR } que permite distinguir el nivel de permisos que tendrá cada usuario dentro del sistema. Este rol se asigna al momento de crear el objeto y queda encapsulado como un atributo inmutable.<br>
En VendedorDAO el rol del vendedor se integra directamente en las operaciones de persistencia, permitiendo que la lógica de permisos se aplique desde la capa de acceso a datos. Al guardar o actualizar un vendedor, el DAO almacena el rol (VENDEDOR o ADMINISTRADOR) en la base de datos y durante el inicio de sesión, recupera este valor para construir correctamente el objeto Vendedor. Además, el método update utiliza el rol del vendedor logeado para decidir si puede modificar el rol de otros usuarios, implementando así un control de permisos basado en el modelo orientado a objetos. Esto garantiza que la gestión de roles no solo viva en el modelo, sino que también se respete al nivel de las operaciones CRUD.<br>
En ManagementController el rol del vendedor logeado se usa para aplicar control de permisos directamente en la interfaz gráfica. Durante la inicialización, el controlador consulta el rol de MainController.vendedorLogeado y si se trata de un vendedor con rol VENDEDOR (no administrador), deshabilita y oculta el botón de gestión de productos (productButton). De esta forma, solo los vendedores con rol ADMINISTRADOR pueden acceder a la vista de administración de productos, mientras que los vendedores normales quedan limitados a las demás secciones. Esta lógica conecta el modelo de roles definido en la clase Vendedor con el comportamiento de la interfaz.<br>
En SellerController el rol del sistema se utiliza para controlar quién puede ver y modificar los roles de los vendedores desde la interfaz. El combo box cbRol se alimenta con la lista de roles (VENDEDOR y ADMINISTRADOR), pero durante la inicialización se verifica el rol de MainController.vendedorLogeado, si el usuario actual no es administrador, el controlador deshabilita y oculta tanto el combo de rol como su etiqueta, impidiendo que un vendedor estándar pueda cambiar el rol de otros usuarios. Cuando sí hay permisos (rol ADMINISTRADOR), el controlador permite crear y actualizar vendedores con el rol seleccionado, propagando esa información al VendedorDAO. De esta forma, la gestión de roles se integra directamente en la capa de presentación.<br>
En ProductoDAO el rol del vendedor se utiliza para aplicar control de permisos directamente sobre las operaciones de productos desde la capa de acceso a datos. A través del método privado isAdmin(), el DAO consulta el vendedorLogeado y verifica si su rol es ADMINISTRADOR; solo en ese caso permite crear, actualizar, eliminar y cargar la tabla completa de productos. Si el usuario tiene rol VENDEDOR, estos métodos se bloquean silenciosamente y no se ejecutan las operaciones sensibles sobre el inventario.<br>
En la base de datos se agregó una columna rol a la tabla vendedores y se modificaron las interfaces correspondientes para implementar visualmente la lógica.

**3. Etiqueta del producto maś vendido por el vendedor logeado**<br>
El método getBestSellingProduct obtiene el producto más vendido por el vendedor actualmente logeado mediante una consulta SQL que suma las cantidades vendidas de cada producto asociado a ese usuario. Primero se prepara un PreparedStatement donde se inserta el id del vendedor logeado, tomado desde MainController.vendedorLogeado. La consulta agrupa los productos por su identificador, ordena los resultados de mayor a menor según la cantidad total vendida y devuelve solo el primero mediante LIMIT 1. Si existe un resultado, el método construye una cadena con el nombre del producto y la cantidad vendida; en caso contrario, devuelve null.<br>
En GenerateSaleController se muestra el producto más vendido del vendedor logeado directamente en la interfaz de la vista de generación de ventas. Para ello, en el método initialize() se llama a showBestSellingProduct(), que utiliza el ProductoDAO para obtener, a partir del id de MainController.vendedorLogeado, una cadena con el nombre del producto y la cantidad total vendida. Si el DAO no encuentra registros (es decir, el vendedor no tiene ventas asociadas), la etiqueta productoMasVendido muestra el mensaje “Sin ventas registradas”; en caso contrario, se muestra el resultado devuelto por el DAO.<br>
Se agregó la etiqueta en la interfaz para su visualización.

# 💡 Fucionalidades

## Inicio de sesión
Para iniciar sesión, se requiere el usuario y la contraseña del vendedor. En la base de datos, estos corresponden a los atributos del vendedor(seller), donde el DNI se asocia con 'dni' y la contraseña con 'user'.

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

# 📝 Licencia

Este proyecto está bajo licencia. Ver el archivo [LICENSE](LICENSE) para más detalles.

[⬆ Volver al inicio](#title)<br>
