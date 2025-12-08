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


# APORTACIONES EQUIPO 5

## ⚙️ Errores Encontrados
#### Error en botòn HELP 
Descripciòn: El problema era que, cuando accedìas a la ventana de inicio, al querer presionar el botòn "HELP" no realizaba ninguna acciòn, solo se quedaba trabado.
Soluciòn: Se importò la clase Platform de java fx, la cual nos cede el control de el hilo de java fx, es decir, nos permite implementar un hilo nuevo, una acciòn nueva sin necesidad de que se termine la ejecuciòn de la interfaz. De este modo, ahora se permite ejecutar las acciones destinadas del help sin problema alguno, la funciǹ de este botòn consiste en redireccionar al usuario al repositorio de github del creador del proyecto.

#### Problema para generar una venta
Descripciòn: Al entrar a cualquier espacio que necesitara generar una nueva venta, esta permitìa llenar los campos pero al intentar generarla no realizaba acciòn.
Soluciòn: El problema se generaba debido a que se estaban tomando los campos nùmericos con entradas incorrectas, por ello se quedaba congelado asì que se solucionò un try-catch que permitìa leer los datos correctos o imprimir un error.

#### Problema para buscar un cliente o producto no registrado dentro de las ventas
Descripciòn: Al entrar buscar un producto o cliente no registrado no permitìa la busqueda.
Soluciòn: Se añadiò un try-catch que intentaba buscar los identificadores dentro de la base de datos y, sin no los encuentra, arroja un error.

#### Tamaño de la interfaz
Descripciòn: Al comenzar a ejecutar la interfaz de usuario o abrir cualquier ventana el tamaño no se adaptaba, evitando que los campos de informaciòn fueran visibles y los botones no fueran accesibles.
Soluciòn: Se implmentaron cambios en los parametros de la interfaz desde los archivos fxml.

#### Problemas al agregar productos
Descripciòn: Al entrar al espacio de agregar un nuevo producto, la interfaz no permitìa generarlo, solo se quedaba pausado en la ventana.
Soluciòn: Como la acciòn de agregar un producto solo toma las entradas y llama al constructor, lo que se hizo fue, antes de llamar al constructor del objeto, hacer una verificaciòn que se asegura que los tipos de datos ingresados coincidan con el tipo de dato establecido en la clase, para posteriormente llamar al constructor y ahora sì ingresarlo en la base de datos.

## 🗂 Implementaciòn de Mejoras
#### Interfaz màs intuitiva
La interfaz propuesta por el creador era poco intuitiva para un usuario comùn, asì que se realizaron varios cambios para volverla màs sencilla.
El principal cambio fue en las etiquetas que se muestran, se tradujeron todas del idioma inglès, y aquellas que tenìan algùn tìtulo tècnico fueron sustituidas por algùn nombre màs comùn.
Se cambiaron os tamaños de las ventanas y se agrandaron los textos mostrados para que la interfaz sea màs legible.
Ademàs se hizo una restructuraciòn general de botones y espacios de escritura para que estos se encuentren mejor distribuidos y le den a la interfaz una visiòn màs limpia y còmoda mediante nuevas hojas de estilo.
Se añadiò el mètodo loadPage, que se convirtiò uno de los mètodos principales, ya que permite que cada vez que se cargue una nueva view se mabtenga en una sola ventana, sin abrir una nueva para cada apartado. 

#### Comprobaciòn de campos al ingresar un nuevo objeto
La interfza original permitìa ingresar un cliente, vendedor o producto nuevo incluso si los campos de informaciòn estaban vacìos. Asì que se decidiò agregar una nueva clase abstracta Validator con un mètodo validate, que solo tiene como entrada una entidad y un mètodo nombrado validate, el cual es heradado en todas las demàs clases con el propòsito de verificar que los campos de informaciòn no se encuentran vacìos. Si los campos no son llenados por completo, antes de ejecutar una acciòn en la base de datos, se abre la ventana emergente de que la acciòn no se puede concretar.

#### Comprobaciòn de datos
El programa maneja la entrada de datos como entradas String lo que provoca que no se de ningùn error sin importar què dato se ingrese, sin embargo, hay campos que deben de tener un formato especìfico, por ejemplo el telèfono en los vendedores deben ser puros nùmeros entre 8 y 15 dìgitos, los id deben ser una secuencia de nùmeros y, en el producto el precio o existencias no deben ser negativos ni 0 (por coherencia). Asì que lo que se hizo fue implementar nuevas funciones sobre el mètodo validate ya existente, que se asegurara de que los datos cumplieran estas caracterìsticas, si en el proceso de validaciòn se encontraba una contradicciòn inmediatamente se termina el proceso y se lanza una ventana emergente al usuario con la advertencia de que la informaciòn es incorrecta.

#### Verificaciòn antes de eliminar
Desde la gestiòn de producto cliente y vendedor existe la acciòn de eliminar a cada uno de estos, desde su vena correpondiente. Lo que observamos es que se podìa seleccionar y simplemente eliminar la informaciòn, lo que consideramos un tanto peligroso en el uso real, por ello implementamos una funciòn que, dependiendo del resgitro seleccionado para eliminar, ejecuta una ventana emergente que indica que la acciòn que se realiza es la eliminaciòn y el nombre del producto o la persona para que el usuario vea què es lo que se eliminarà, pidiendo la confirmaciòn para realizar la eliminaciòn o, de lo contrario cancelar la acciòn y simplemente quedarse en la ventana esperando por una nueva acciòn.

#### Espacio de bùsqueda en la generaciòn de ventas
Ademàs de la soluciòn del problema inicial que se tenìa en este espacio se añadiò una funcionalidad para que el usuario pueda buscar el id o el nombre tanto del prducto como del cliente. En caso de que buscara por algo que no se encuentra registrado en la base de datos lo que se hace es abrir una ventana que explica que no està registrado y se le prroporciona la opciòn de agregarlo como un elemento nuevo, si el usuario lo acepta, se redirige tanto a la venta como el proceso de gestiòn de clientes o productos, segùn sea el caso.

#### Nuevos elemntos de la UI
Dentro del nuevo diseño de la interfaz dentro de las ventanas de esta se añadiò informaciòn que se muestra todo el tiempo, como la fecha, el id y el usuario que corresponden segùn el inicio de sesiòn. Ademàs se añadiò un nuevo controlador Welcome para gestionar esta nueva view y guardar los datos que deben mostrar.

#### Actualizar stock
Cada que se genere una nueva venta el programa actualizarà la existencia del producto, haciendo que al entrar nuevamente a generar una venta diferente la cantidad del producto vendido se haya reducido.

#### Propuesta (no implementada) Generar un recibo
Desde la ventana de generaciòn de venta permita decidir si se genera un recibo o no, asì que se deberìa implementar una funciòn, que dependiendo del nùmero de serie se acceda al registro de lo que se incluyo en esa venta, escribienod en la factura cada unidad de lo que fue comprado, el precio y los datos del cliente, vendedor y cada producto. Y està tendrà el mismo funcionamiento para cualquier registro de venta pero los resultados cambiaràn dependiendo del nùmero de serie de la venta.

## ⿻ Diagrama UML
[Diagrama UML](https://drive.google.com/file/d/1XxLzHsK2-wGoHITZquxy-Mo3jaoV9pt7/view?usp=drive_link)
<img width="766" height="1211" alt="UML drawio" src="https://github.com/user-attachments/assets/27bb96f9-0741-4f11-baf0-865325c5a798" />


## 🎥 Vìdeo
[Reza-Ayuso](https://youtu.be/OkKGALtZwkU?si=vBKXfj1smZSz4tzG)

