# Funkos Asíncronos

## Autores

- Jaime Medina
- Eva Gómez Uceda

## Arquitectura seguida en el código.

La arquitectura se ha dividido por partes:
En la carpeta `models` contamos con el apartado de datos, como la clase Funkos e IdGenerator. La parte de lógica se
encuentra en las carpetas `repositories` y `services`, en las que contamos con clases como **FunkoRepositoryImpl**,
**FunkoServiceImpl** o **DataBaseManager**. Todas estas se encargan de la parte lógica del programa. Luego tambien
contamos con carpetas como `routes` o `exceptions`, estas contienen las rutas y las excepciones usadas en el programa.

