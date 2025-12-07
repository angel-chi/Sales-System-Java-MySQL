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

# Errores encontrados

## Falta de validaciones

### Validacion de conexion

En caso que no se pueda conectar con la base de datos y/o que no se pueda encontrar el archivo de configuraciones, no muestra esta falla hasta que se intenta iniciar sesi�n o bien ni ello

### Validacion en tipo de dato

Varios campos de los formularios permiten varios tipos de datos, pese a que el objeto o entidad a la que se hace referencia tiene otro tipo de dato, esto provoca que el programa colapse al no poder procesar estos datos y no haber un control de exepciones

### Validacion de seguridad

Esto aplica unicamente para los vendedores, esto es una falta de logica en practicas, pues la vista actual muestra control absoluto de productos, clientes e incluso otros vendedores para cualqueira que sea el vendedor que inicio sesion.
Esto es un grave error, pues en lo practico, si un vendedor nuevo tiene tal acceso, podria incluso eliminar al vendedor con mayor experiencia o eliminar productos/clientes sin raz�n.

## Botones problematicos

### Boton cancelar 

En la pestaña de generar ventas, este boton no tenia funcionalidad original, suponiendo debiera cancelar el proceso de generar la venta, deberia limpiar las celdas

### Boton help

Este boton originalmente buscaba abrir directamente el repositorio del proyecto para buscar detalles de este en el propio github.
Sin embargo, no habria el enlace y en su lugar provocaba una falla en el programa que lo congelaba

# Propuestas

## Incorporacion en base de datos

### Nuevos atributos

Debido al login, agregamos un atributo de password a la entidad de vendedor. Ademas de hacer que el dni se unico para evitar duplicaciones, al igual que el usuario.
Para tener una forma de contacto relacionado y que ademas funcione con los involucrados

### Nueva entidad

Dado que existe los clientes y vendedores, cuando el stock esta vacio deber�a haber una forma de comprar m�s, por ello se propuso crear una entidad llamada proveedor
Esto involucra una tabla intermedia, que ser�a la de compraProveedor, en la cual relaciona tanto el proveedor, el producto como el encargado (Vendedor tipo Contador).
En donde basicamente se registra cuando se compro los nuevos productos, cuando, quien lo autorizo, que productos se compraron, de quien provienen, cuanto costo cada producto

## Agregar una seguridad en operaciones

### Eliminacion de objetos

Para mejorar la logica de negocio en cuanto a las transacciones se ha agregado un pequeño seguro, esto se basa en evitar desatar un problema en cascada al intentar eliminar un producto, cliente o vendedor, puesto que si estos ya estan relacionados con alguna transaccion esto dejaria un vacio.
Para solucionar esto, se incorporo un pequeño verificador que busca si este objeto esta asociado con alguna transaccion antes de eliminarlo, de esta forma podria eliminar solo los 'fantasmas' que no han dejado un rastro que afecte a otras entidades.
Esto se implemento con una consulta anidada que busca algun rastro en las transacciones, esto aplicado para estos 3 principales

### Agregacion de jerarquia en vendedores

Para mejorar la seguridad, se ha incorporado un atributo similar a un ENUM para la entidad vendedores.
Esto es una medida de seguridad para evitar darle todo el control a cualquier vendedor. Esto se basa en que ahora los vendedores con el estado 'Vendedor' solo pueden vender, y la pestaña de control para clientes/productos/vendedores e incluso la de reportes se limitara
Ahora existen por ejemplo los contadores, que no pueden ni manejar el control, pero tienen acceso a los reportes
El jefe que no puede generar ventas, pero tiene acceso al control y los reportes
Finalmente el admin que puede ver todo.

## Centralizar atributos comunes

### Atributos similares para vendedores y clientes
En las entidades para clientes y vendedores hay varios aspectos iguales, como los dni, nombres y otros atributos, por lo que para hacerlo central y seguir las reglas del Poo, se utilizo una interfaz que reune estos atributos comunes, debido a ser records se ocupaba la interfaz para ser compatible

### Atributo igual en varias entidades
Para varias entidades que tenian un atributo ENUM basicamente igual, se opto por centralizar este ENUM
Para la jerarquia de carpetas se realizo un pequeño cambio dentro de models: separando las entidades, Daos y configuracion

## Nuevas vistas

### Vista compra

Esta se basa en el poder comprar y reabaster los stoks con los productos

# 📝 Licencia

Este proyecto está bajo licencia. Ver el archivo [LICENSE](LICENSE) para más detalles.

[⬆ Volver al inicio](#title)<br>
