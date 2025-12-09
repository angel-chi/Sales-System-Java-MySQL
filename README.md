<h1 align="center" id="title"> Sistema de ventas en Java FX</h1>
<h6 align="center"> Aplicación de Gestión de Ventas con Java 17, JavaFX, MySQL y Patrones de Diseño MVC y DAO </h6>
<h1></h1>

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

<p align="center">
  <img src="src/main/resources/images/carritodecompra.png" />
</p>

<!-- TOC -->
* [📑 Descripción](#-descripcion)
* [💻 Entorno](#-entorno)
* [🚀 Instalación](#-instalacion)
    * [Instalacion del JDK 17](#instalación-del-jdk-17)
    * [Configuración de la Base de Datos](#configuración-de-la-base-de-datos)
    * [Ejecución del Proyecto](#ejecución-del-proyecto)
    * [Modificación de las Vistas con Scene Builder](#modificación-de-las-vistas-con-scene-builder)
* [🧬 Estructura Básica](#-estructura-basica)
* [🗄️ Diagrama de Base de Datos](#-diagrama-de-base-de-datos)
* [💡 Fucionalidades](#-fucionalidades)
    * [Inicio de Sesión](#inicio-de-sesión)
    * [Pantalla Principal](#pantalla-principal)
        * [Ventas](#sales)
        * [Administración](#management)
        * [Reportes](#reports)
* [📧 Contacto](#-contacto)
* [📝 Licencia](#-licencia)
<!-- TOC -->

# 📑 Descripción
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


# 🚀 Instalación
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

# 🧬 Estructura Básica
<pre>
+ java
  |-- controllers // Controladores de la aplicacion
  |-- model	// Modelos de datos de la aplicación
  --Main.java // Donde se inicia la ejecución del programa      
+ Resources
  |-- images // Imágenes utilizadas en la aplicación
  |-- views // Vistas de la aplicación FXML
  |-- reports // Informes generados por la aplicación
</pre>

# 🗄️ Diagrama de Base de Datos
<p align="center">
  <img src="src/main/resources/images/diagramaBD.png" />
</p>


# 💡 Fucionalidades

## Inicio de sesión
Para iniciar sesión, se requiere el usuario y la contraseña del vendedor. En la base de datos, estos corresponden a los atributos del vendedor(seller), donde el usuario se asocia con 'dni' y la contraseña con 'user'.

<p align="center">
  <img src="src/main/resources/images/iniciodesesion.png" />
</p>

## Pantalla principal
La pantalla principal muestra las siguientes ventanas: (¡Ya traducidas al español!)
* Menu: incluyen la posibilidad de salir o visitar la documentación.
<p align="center">
  <img src="src/main/resources/images/menu.png" />
</p>

* Ventas: Permite generar nuevas ventas.
<p align="center">
  <img src="src/main/resources/images/ventas.png" />
</p>

* Administración: Ofrece operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para clientes, productos y vendedores.
<p align="center">
  <img src="src/main/resources/images/administracion.png" />
</p>

* Reportes: Aquí se encuentran las operaciones de reportes y estadísticas relacionadas.
<p align="center">
  <img src="src/main/resources/images/reportes.png" />
</p>

# ☢️ Corrección de Errores
* Corrección de Interfaces de JavaFX: Se ajustaron tamaños y se hizo uso de AnchorPanes y TabPanes para mantener todo centrado.
* Corrección de Error al generar una venta: Un campo en la clase ShoppingCart era de tipo parse.Double y no se convertía correctamente el valor.
* Corrección de Nombre de Pestañas: Al cambiar una pestaña se mantenía el nombre de la pestaña cerrada y no de la actual. Se implementa un HashMap en dicho fichero con el nombre de cada ventana y se cambió la lógica de modo que se relacione la pestaña padre con la hija, permitiendo que se mantenga el título de dicha pestaña.
* Corrección de Help. Anteriormente utilizar esta función crasheaba el programa por completo. Ahora, se implementó una ventana de Ayuda para cumplir su objetivo inicial.

# Diagrama UML

<p align="center">
  <img src="src/main/resources/images/Diagrama%20UML.png" />
</p>


## AbstractBaseDAO\<T>
Se implementa la clase base AbstractBaseDAO\<T>, la cual centraliza la lógica repetitiva de las operaciones CRUD. Se aplican dos principios SOLID:
* SSP (Single Responsability Principle): La clase base se encarga únicamente de la gestión de conexiones, manejo de excepciones SQL y ejecución de sentencias, mientras que sus hijas solo definen consultas específicas y mapeo de objetos!
* OCP (Open / Closed Principle): El sistema está abierto a la extensión pero cerrado a la modificación de la lógica base de persistencia.

A su vez, se utilizó el patrón de diseño Template Method Pattern, porque la clase abstracta define el "esqueleto", delegando los pasos específicos a las subclases concretas.

## AbstractCRUDController\<T>
Se crea la clase AbstractCRUDController\<T> con el mismo objetivo que el punto anterior, estandarizar el flujo de interacción en las vistas de gestión. Otros principios que se utilizan son:
* DRY (Don't Repeat Yourself): Se eliminó una cantidad magistral de lógica repetida en la validación de formularios, limpieza de campos, actualización de tablas y manejo de alertas de usuario.
* Polimorfismo: Aprovechando el polimorfismo de Java, un controlador base puede gestionar el flujo de cualquier entidad (Product, Customer, Seller).

## validacionEntrada()
Se implementa un interface con un método para validar si un campo es vacío. Ideal para poder implementarse a lo largo del código.

## Menú de Help
Se implementa un pequeño Menú de Help en donde anteriormente no había nada con el fin de acceder al README del repositorio.

## Traducción de Interfaces
Se traduce al Español toda la interfaz de usuario de la aplicación.

# 📧 Contacto
Si tienes alguna pregunta, sugerencia o crítica sobre el proyecto, no dudes en contactarme por correo electrónico a [tomasborghi13@gmail.com](mailto:tomasborghi13@gmail.com).

# 📝 Licencia

Este proyecto está bajo licencia. Ver el archivo [LICENSE](LICENSE) para más detalles.

[⬆ Volver al inicio](#title)<br>
