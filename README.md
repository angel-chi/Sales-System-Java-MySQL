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

# PROYECTO EQUIPO 6

**Integrantes:**
* **Rivera Manzanero Alessandra Anelisse**
* **Polanco Casares Fernando**
---
##  Link del video

---
##  Diagrama UML
<details>
  <summary>Diagrama UML</summary>

  <div style="overflow-x: auto; max-width: 100%;">

    <img src="UML/UML.svg" 
         alt="Diagrama de Clases UML del Proyecto" 
         width="1200px" />

  </div>
</details>

##  Errores encontrados y soluciones implementadas

### 1. Error en la Funcionalidad "Help" (Congelación de UI)

| Aspecto                   | Detalles                                                                                                                                                                                                                                                      |
|:--------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Descripción del Error** | Al hacer clic en la opción "Help" (Ayuda), la interfaz de usuario se congelaba, impidiendo que se abriera el enlace; Concluimos que podría ser error por el sistema operativo .                                                                               |
| **Solución Implementada** | Se movió la lógica de la apertura del enlace a un hilo en segundo plano para que si no se logra abrir el enlace, no se congele el programa.                                                                                                                   |
| **Implementación**        | Se agregó un bloque try-catch para verificar si es posible abrir el enlace. La lógica de apertura fue trasladada a otro hilo para evitar la congelación de la UI, permitiendo que la interfaz se maneje mientras la operación externa se ejecuta en paralelo. |

### 2. Error al Agregar Productos (Validación de Precio)

| Aspecto                   | Detalles                                                                                                                                                                                                          |
|:--------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Descripción del Error** | Al agregar un producto, si el usuario ingresaba texto o dejaba vacío el campo de precio, el programa fallaba al intentar convertir la entrada a un tipo numérico ( `NumberFormatException`).                      |
| **Solución Implementada** | Implementación de manejo de errores y validación de entrada                                                                                                                                                       |
| **Implementación**        | Se añadió una validación del campo y un bloque try-catch en el controlador. Si la entrada no es un valor numérico válido, se muestra una alerta al usuario indicándole el error y solicitando una entrada válida. |

### 3. Error al mostrar el nombre de las ventanas (No se actualizaba el título)
| Aspecto                   | Detalles                                                                                                                                                                                                                                                                                                                      |
|:--------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Descripción del Error** | Al abrir una ventana nueva y después cerrarla, la nueva ventana mantenía el nombre de la ventana cerrada.                                                                                                                                                                                                                     |
| **Solución Implementada** | Asignar a cada ventana su nombre correspondiente.                                                                                                                                                                                                                                                                             |
| **Implementación**        | Se creo un HashMap llamado fxmlTitles para asignarle a cada ventana su nombre correspondiente. Luego mediante un constructor se inicializa el fxmlTitle, por último en el método `configurateStageCloseEvent` se creó una variable que almacene el título de la ventana padre que debe mostrarse al cerrar la ventana actual. |


---

##  Modificaciones en la Interfaz de Usuario (UI)

* **Idioma:** Se realizó el cambio del idioma de la interfaz de inglés a español para asegurar que el producto sea completamente accesible y usable para el público objetivo.
* **Ajuste de Distribución:** Se ajustó la distribución y el diseño en varias ventanas, especialmente en la ventana de reportes, ya que el contenido no se visualizaba completo al abrirla, mejorando la experiencia de usuario.

---

## Implementaciones Propuestas:

### Propuesta Implementada #1: Validación de Campos Mediante Interfaz

* **Objetivo** Fortalecer la solidez de la aplicación previniendo la inserción de datos vacíos o no válidos para atributos esenciales, promoviendo la **integridad de los datos**.
* **Implementación:**
    1.  Se creó la interfaz **`IValidable`**.
    2.  Esta interfaz se implementó en los controladores principales: `SellerController`, `ProductController`, y `CustomerController`.
    3.  La interfaz obliga a la implementación del método de validación de campos para no permitir agregar elementos con atributos esenciales vacíos.
* **Resultado:** 
    1.  No se permiten agregar productos, clientes y vendedores con campos vacíos, informa al usuario que debe de ingresar una entrada válida

### Propuesta Implementada #2: Refactorización Arquitectónica (Principios SOLID: S y D)

#### 1. Separación de Responsabilidades (SOLID: Single Responsibility Principle - SRP)

* **Objetivo:** Asegurar un límite claro y estricto entre la capa de Acceso a Datos (DAO) y la capa de Presentación (Controladores).
* **Acción:**
    * Se eliminaron todas las responsabilidades específicas de la Interfaz de Usuario (UI) (llamadas a `MenuController.setAlert()`) de las clases DAO (`CustomerDAO`, `SellerDAO`, `ProductDAO`, `SalesDAO`).
* **Resultado:**
    * Las DAOs ahora tienen una Responsabilidad Única: gestionar la persistencia y devolver un simple resultado (`true` para éxito, `false` para fallo, o un objeto de modelo/`null`).
    * Los Controladores ahora son los responsables de interpretar el resultado del DAO y manejar la lógica de la presentación (mostrar alertas de éxito o error al usuario).

#### 2. Inversión de Dependencias (SOLID: Dependency Inversion Principle - DIP)

* **Objetivo:** Desacoplar los módulos de alto nivel de los módulos de bajo nivel, promoviendo la flexibilidad.
* **Acción:**
    * Se modificaron las declaraciones de los Controladores para que dependan de la interfaz abstracta `CRUD<T>` en lugar de depender de la implementación concreta de cada DAO.
* **Resultado:**
    * Los módulos de alto nivel (Controladores) ya no dependen directamente de los módulos de bajo nivel (DAOs concretos).
    * Esto desacopla la aplicación, facilitando la posibilidad de cambiar el mecanismo de persistencia sin la necesidad de modificar el código del Controlador.

### 3. Propuesta Implementada #3: Agrega puestos de vendedor (Seller y Manager)
* **Objetivo:** Diferenciar entre vendedores y gerentes, otorgando permisos específicos a cada puesto.
* **Acción:**
  * Se modificó la base de datos para incluir un nuevo atributo "role" en la tabla "sellers".
  * Se agregó una verificación al intentar abrir las ventanas de Product y Seller donde solo los gerentes pueden acceder.
  * Se agregó el atributo "role" al modelo Seller y se ajustaron los métodos de autenticación para considerar este nuevo atributo.
  * Se actualizó la interfaz de agregar vendedores para incluir la selección del rol (vendedor o gerente).
* **Resultado:**
  * Solo los gerentes pueden acceder a la gestión de productos y vendedores, mientras que los vendedores solo pueden realizar ventas y gestionar clientes.
---

# 📧 Contacto
Si tienes alguna pregunta, sugerencia o crítica sobre el proyecto, no dudes en contactarme por correo electrónico a [tomasborghi13@gmail.com](mailto:tomasborghi13@gmail.com).


# 📝 Licencia

Este proyecto está bajo licencia. Ver el archivo [LICENSE](LICENSE) para más detalles.

[⬆ Volver al inicio](#title)<br>
