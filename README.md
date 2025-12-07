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


# 📝 Licencia

Este proyecto está bajo licencia. Ver el archivo [LICENSE](LICENSE) para más detalles.

[⬆ Volver al inicio](#title)<br>

<h1 align="center" id="title"> README equipo 3 (nuestros aportes)</h1>

## Diagrama UML

![image alt](https://github.com/angel-chi/Sales-System-Java-MySQL/blob/9c4cf0bf42704f11ab4cc64c8afaddc3c6a9a313/UML.png)

## Instalacion de Maven

1. Actualiza los repositorios usando: _sudo apt uptdate_
2. Instala Maven usando: _sudo apt install Maven_
3. Verifica la instalación usando: _mvn -version_
_Si se imprime algo similar a esto, la instalación fue correcta (nota: Revisar que la versón de Maven sea igual o superio a v3.8 y Java version igual a v17)

![image alt](https://github.com/angel-chi/Sales-System-Java-MySQL/blob/2f4832054a223c41183e473d574709fdc230f15d/mvn.png)

## Instrucciones para construir y ejecutar sin IDE

1. Compila y empaqueta el proyecto usando: _mvn clean install_
(Esto limpia compilaciones previas, compila el código, corre pruebas y genera el .jar )
2. Ejecuta la aplicación JavaFX usando: _mvn javafx:run_

## 📋 Changelog

### [v2.0] - 08-12-2025
**Bug Fixes:**

Error 1: Se terminaba la ejecución del programa y cerraba IntelliJ 
- _**¿Cuándo ocurrió?:**_ Al presionar el botón del help de la interfaz de _Control del punto de venta_ (Error de ejecución).
- _**Causa:**_ Trata de redirigir  al usuario al repositorio de github pero por alguna razón la librería no conseguía hacerlo y se quedaba congelada, sospechamos que fue debido al sistema operativo.
- _**Solución:**_ Reconstruimos totalmente la función del botón de help, la cual ya no manda directo al usuario a un repositorio de github, si no que ahora abre una interfaz de soporte implementada en el propio programa donde se proporciona todo lo necesario al usuario respecto a un servicio de ayuda.

Error 2: Creación de ventanas emergentes con tamaño erróneo 
- _**¿Cuándo ocurrió?:**_ Cada que se abría una nueva ventana (Error de ejecución).
- _**Causa:**_ No se inicializaba un tamaño mínimo a la hora de llamar al método que creaba las pestañas.
- _**Solución:**_ Se añadió 2 parametros extras a [openNewStage] para definir un tamaño mínimo a la hora de crear una nueva ventana.

Error 3: Error al generar una venta 
- _**¿Cuándo ocurrió?:**_ Cuando añadias una venta (Error de ejecución).
- _**Causa:**_ Idioma de la computadora, si se encuentra en ingles parece no generar error pero si se encuentra en español si, esto debido a que en ingles se usa comas para separar enteros de decimales y en español se usa punto decimal.
- _**Solución:**_ El método Double.parseDouble esperaba un punto decimal en su valor y recibía una coma, por lo que antes de que el método reciba el valor retornado por price.getText() se añadió un .replace(",", ".") para sustituir la coma por punto decimal

## Cambios en la UI

Traducción del programa de ingles a español:
  _Se tradujeron las siguientes interfaces y todos sus elementos visibles por el usuario:_
  - Login -> Inicio de Sesión
  - Management -> Control de punto de venta
  - Shopping cart -> Carrito de compra
  - Customer -> Clientes
  - Products -> Productos
  - Seller -> Vendedor
  - Sales -> Ventas

Cambios para que la UI sea entendible por cualquier usuario
  Se tradujeron las interfaces mencionadas en el punto anterior, tambien se reestructuro la interfaz de _Control de punto de venta_, de tal forma se eliminaron los paneles “Menu, Sales, Management, Reports” y se decidió integrar todas sus funcionalidades individuales en una misma interfaz para que el usuario tenga mayor facilidad para acceder a cada ventana del sistema.
  
## Propuestas de mejora




## Implementación de mejoras en la UI



## Video del proyecto

**Link:** 

## Integrantes del equipo

Jarib Alberto Novelo Hernández
- **Matricula**: 24216374
- **Usuario de github**: novelo03
- **Rol desempeñado**:  



[⬆ Volver al inicio](#title)<br>


## 🔨 Mejoras propuestas e implementadas
- Se propuso e implementó un buscador de clientes via ID comparando el ID escrito en el TextField con los ID ya registrados previamente de los clientes en la base de datos, de manera que sí, el ID escrito concuerda con los y registrados, llenará los demas datos (usuario, nombre y drección) en las casillas de la ventana "Clientes", de no ser así, se arrojara una ventana emergente alertando al usuario diciendo que el cliente no esta registrado en la base de datos.
- Se propuso e implementó un metodo de seguridad extra a la hora de ingresar al menú de vendedores y a la hora de borrar clientes desde el menú de clientes, ya que sí una persona distinta al propietario de la licencia de uso del software accede a la vista de los vendedores, este podrá tener acceso a sus claves de acceso (usuario y contraseña), nombre completo y telefono, poniendo en riesgo sus datos personales y tambien evitamos que cualquier persona pueda eliminar los datos de un cliente a menos que el dueño de la clave principal lo requiera, esto se logró creando un método que abre un nuevo archivo fxml (mainPasswordView) donde a continuación se necesitara de la contraseña princiapl, otro método que valida si la contraseña ingresada en el TextField es la correcta y de ser así dará acceso a la funcionalidad de borrar clientes y acceder al menú vendedores por medio de un if.
- En futuras actulizaciones se puede implementar una alerta que avise al usuario cuando las unidades de un producto bajen de cierta cantidad notificando que el inventario se esta acabando especificamente de ese producto, implementando cambios en ProductController creando un metodo que se ejecute a la par con el metodo que se encarga de crear una venta en GenerateSaleController comparando la cantidad actual del producto menos la cantidad vendida y si sobrepasa el limite minimo deseado, ejecutar una ventana de notificación de que el producto se esta agotando.
- Por ultimo, implementar en un futuro la opción de activar descuentos en productos deseados, se puede lograr seleccionando productos en un combobox para que despues una clase generateDiscount se encargue de aplicar el descuento deseado en los productos previamente seleccionados.




