# Funkos Asíncronos

## Autores

- Jaime Medina
- Eva Gómez Uceda

## Arquitectura seguida en el código.

La arquitectura se ha dividido por partes:  
En la carpeta `models` contamos con el apartado de datos, como la clase **Funkos** e **IdGenerator**. La parte de lógica
se  
encuentra en las carpetas `repositories` y `services`, en las que contamos con clases como **FunkoRepositoryImpl**,  
**FunkoServiceImpl** o **DataBaseManager**. Todas estas se encargan de la parte lógica del programa. Luego también
contamos con carpetas como `routes` o `exceptions`, estas contienen las rutas y las excepciones usadas en el programa.
La carpeta `adapters` con la clase **LocalDateAdapter** implementada por JsonSerializer la usamos para personalizar la
serialización de objetos de la clase Funko a formato JSON. En `enums` tenemos definidos los tipos de **Modelo** del que
pueden ser los Funkos, y finalmente tendríamos la carpeta `controllers` donde realizamos todas las consultas sobre los
datos del CSV.

## Repositories

En esta carpeta nos encontramos con dos paquetes:

- **crud**
  En él tenemos la interfaz  `CrudRepository`, usada para definir los métodos básicos del Crud y métodos requeridos para
  este proyecto, siendo estos guardar, actualizar, buscar por ID, buscar por nombre, buscar todos, eliminar por ID y
  eliminar todos, estas operaciones son realizadas en una colección de objetos de tipo T, utilizando identificadores de
  tipo ID. Todo ello ha sido realizado de manera asíncrona utilizando `CompletableFuture`.
- **funkos**
  En este paquete nos encontramos con una interfaz, `FunkoRepository` que extiende la interfaz `CrudRepository`. Y la
  clase `FunkoRepositotyImpl`que implementa la interfaz `FunkoRepository` , usamos el patrón Singleton y el
  objeto `DataBaseManager` para gestionar la conexión a la base de datos, los métodos son implementados de manera
  asíncrona usando `CompletableFuture.runAsync`, lo que nos permite realizar las operaciones de manera concurrente
  mejorando el rendimiento y manejando así mejor las excepciones relacionadas con la base de datos y la serialización de
  datos en formato JSON.

## Services

En esta carpeta nos encontramos con tres paquetes:

- **cache**
  En este paquete nos encontramos con la interfaz `Cache`en la que se definen los métodos básicos para gestionar una
  caché, permitiendo agregar, recuperar, eliminar datos, limpiar la caché y apagarla adecuadamente cuando ya no es
  necesaria.
- **database**
  Aquí tenemos la clase `DataBaseManager`que, además de usar el patrón Singleton, utiliza la biblioteca HikariCP para
  administrar la conexión a la base de datos. En su constructor lama al método `openConnection` para abrir una conexión
  a la base de datos e inicializa el cargador de clases del controlador de base de datos.
  La clase implementa la interfaz `AutoCloseable`, lo que significa que se puede utilizar en un
  bloque `try-with-resources` para asegurarse de que la conexión se cierre adecuadamente cuando ya no se necesite.
- **funkos**
  En este paquete contamos con dos interfaces, `FunkoCache`que extiende de `Cache`, y `FunkoService`en el que se definen
  métodos de operaciones básicas de consulta siendo estos buscar todos, buscar por nombre, buscar por ID, guardar,
  actualizar, eliminar por ID y eliminar todos.
  También nos encontramos con dos clases, la primera sería `FunkoCacheImpl`que implementa a `FunkoCache`, definimos el
  tamaño máximo de la cache como `maxSize`, que se establece en 10, utilizamos un LinkedHashMap, que mantiene un orden
  de acceso, para que cuando se alcance el tamaño máximo de la caché, se eliminen los objetos menos utilizados según el
  orden de acceso.
  Utilizamos un `lock` para asegurar que las operaciones en la caché se realicen de manera segura en entornos multi-hilo
  y un `ScheduledExecutorService` al que llamamos`cleaner` para programar la limpieza automática de la caché cada dos
  minutos.
  El método `shutdown` permite cerrar la caché y detener el programador de limpieza.
  Finalmente tenemos la clase `FunkosServiceImpl` 
