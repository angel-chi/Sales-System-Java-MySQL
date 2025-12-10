<h1 align="center" id="title"> Sistema de ventas en Java FX</h1>
<h6 align="center"> Aplicación de Gestión de Ventas con Java 17, JavaFX, MySQL y Patrones de Diseño MVC y DAO </h6>
<h1></h1>

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

<p align="center">
  <img src="src/main/resources/images/PROYECTO-SYSTEMSALES/CDC2.png" />
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

# Cambios 
El primer cambio que hice es el cambio de idioma a la ventana de inicio de sesion y la ventana de managmente, tambien se cambio el tamaño con el que se abre todo y aparte que los botones y demas cosas se ajustan al tamaño de la ventana asi que si el usuario lo mueve este cambia tambien de tamaño 
# 💡 Fucionalidades
1. Implementación de un campo (atributo) en la tabla seller para designar el un rol al vendedor (ADMIN o NORMAL), se usa encapsulamiento ya que el método update() en SellerDAO encapsula la lógica de actualización en la base de datos.
2. Poder cambiar el rol del vendedor desde la ventana de gestion de vendedores
3. Ajustes dinamicos de todos los campos en la ventana
4. Distintas medidas de ventanas para una mejor experiencia
5. Traduccion de la interfaz a español
6. Descuento de 0% a 100% para un producto añadido en el carrito de venta y su descuento como el valor pos descuento actualizado este usa encapsulamiento ya que tiene variables privadas

# Errores eliminados
1. Se elimino el error del tamaño de la pantalla al abrir cada ventana y se soluciono con un switch en la funcion que abre las ventanas para que cada una de ellas tenga un distinto tamaño
2. Error del voton de cancelar ventas que no funcionaba y se corrigio para que quite la venta ya añadida
3. El nombre de la pestaña cuando vuelves no era el correcto y se tuvo que cambiar
## Inicio de sesión
Para iniciar sesión, se requiere el Usuario y la contraseña del vendedor. En la base de datos, estos corresponden a los atributos del vendedor(seller), donde el Usuario se asocia con 'dni' y la contraseña con 'user'.

<p align="center">
  <img src="src/main/resources/images/PROYECTO-SYSTEMSALES/INCIO%20DE%20SESION.png" />
</p>

## Pantalla principal
La pantalla principal muestra las siguientes ventanas
* Menu: incluyen la posibilidad de salir o visitar la documentación
<p align="center">
  <img src="src/main/resources/images/PROYECTO-SYSTEMSALES/MENU%201.png" />
</p>

* VENTAS: Permite generar nuevas ventas.
<p align="center">
<img src="src/main/resources/images/PROYECTO-SYSTEMSALES/MENU%202.png" />
<img src="src/main/resources/images/PROYECTO-SYSTEMSALES/CARRITO%20DE%20COMPRAS.png" />
<img src="src/main/resources/images/PROYECTO-SYSTEMSALES/CDC%20DESCUENTO.png" />
</p>

* GESTION: Ofrece operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para clientes, productos y vendedores.
<p align="center">
  <img src="src/main/resources/images/PROYECTO-SYSTEMSALES/MENU%203.png" />
<img src="src/main/resources/images/PROYECTO-SYSTEMSALES/GESTION%20CLIENTES.png" />
<img src="src/main/resources/images/PROYECTO-SYSTEMSALES/GC2.png" />
</p>

* REPORTES: Aquí se encuentran las operaciones de reportes y estadísticas relacionadas.
<p align="center">
  <img src="src/main/resources/images/PROYECTO-SYSTEMSALES/MENU%204.png" />
<img src="src/main/resources/images/PROYECTO-SYSTEMSALES/REPORTES%20VENTAS1.png" />
<img src="src/main/resources/images/PROYECTO-SYSTEMSALES/REPORTE%20VENTAS2.png" />
</p>
# VIDEO
https://alumnosuady-my.sharepoint.com/:f:/g/personal/a24216355_alumnos_uady_mx/IgBk0ufzkngPSqZhsgHTjWgAARKpVGoDS4ZfrLV5QiUZVPM?e=mbIGDV
# DIAGRAMA UML
<p align="center">
  <img src="src/main/resources/images/PROYECTO-SYSTEMSALES/proyectofinalPoo.drawio.png" />

</p>

# Cambios (Alejandro)

Se han implementado cambios para mejorar el dinamismo de la UI y proveer más información más completa con respecto a los clientes para un hipotético uso.

# Funcionalidades implementadas

1. Se implementó el atributo "marca" a los productos para mostrar la información de modo más completo al usuario. Se aplicó encapsulación en el atributo debido a que en el DAO se encuentra la lógica necesaria para actualizar los datos de este campo.
2. Se implementó el atributo "email" a los clientes para mejorar la comunicación de los mismos con los vendedores en un caso de uso hipotético. De igual manera se aplicó el encapsulamiento debido a que en la capa DAO se encuentra la lógica necesaria para modificar el campo.

# Errores corregidos

1. El primer error con el que me enfrenté en el código fue que no funcionaba el botón de la sección help del primer menú luego de inicar sesión. Lo solucioné por medio de la biblioteca de HostService de JavaFX, usando la fucnión showDocument para abrir el link al repositorio de GitHub.
2. El segundo error con el que me enfrenté fue el hecho de que el programa no tenía un modo de pasar entre ventanas sin loguearse continuamente (es decir, si entrabas a una ventana no podías salir de la misma y para volver atraś tenías que reiniciar el programa). Lo que hice fue construir un event handler para el caso en el que hubiera un cierre de ventana para redirigr a la ventana anterior.

# Cambios sugeridos
Pues uno de los cambios principales que sugiero sería crear una clase "BaseController" para que sirva a modo de padre con todas las clases controlador, ya que algunas comparten ciertos métodos y atributos que se podrían beneficiar de ser heredados de una clase padre.

# 📝 Licencia

Este proyecto está bajo licencia. Ver el archivo [LICENSE](LICENSE) para más detalles.

[⬆ Volver al inicio](#title)<br>
