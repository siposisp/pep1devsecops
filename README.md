# Lab2TBD-1-2024 . README
Este proyecto es un sistema de gestión de emergencias que permite la coordinación entre voluntarios y coordinadores para responder eficazmente a situaciones de emergencia. Para esta segunda entrega se agregaron recursos de localización con la herrameinta postGIS.

## Características principales

- Registro y gestión de voluntarios.
- Asignación de tareas a voluntarios por parte de los coordinadores.
- Seguimiento en tiempo real del estado de las tareas asignadas.
- Generación de reportes de actividad y desempeño.

## Tecnologías utilizadas

- Java (JDK 17)
- Spring Boot
- PostgreSQL 
- PostGIS

## Instalación

### Requisitos previos

- JDK 8 o superior (hasta JDK 17) instalado
- Maven
- PostgreSQL Server
- PostGIS

### Pasos para la instalación

#### Backend
1. Clona el repositorio a tu máquina local.
2. Crea una base de datos PostgreSQL con el nombre `LabTBD-2-2024`.
3. Abrir y configurar PostGis con las credneciales de Postgres y el nombre de la base de dato. 
4. Ejecutar los script .sql en la carpeta "Database" en el siguiente orden: dbCreateV2, dbTriggersV2, dbProceduresV2, dbloadDataV2.
5. Abre el proyecto en tu IDE preferido (por ejemplo, IntelliJ IDEA, Eclipse).
6. Configura las credenciales de la base de datos en el archivo `application.properties` ubicado en `src/main/resources/`.
7. Ejecuta la aplicación. La aplicación estará disponible en [http://localhost:8090](http://localhost:8090).
#### Frontend
1. Con el repositorio clonado, ir a a la carpeta Frontend.
2. Instalar los paquetes necesarios usando npm (Instalar node de ser necesario).
```
npm i
```
3. Crear un archivo .env en esta carpeta y agregar lo siguiente, remplazando LLAVE_API_DE_GOOGLE por su propia llave.
```
VITE_GOOGLE_API = LLAVE_API_DE_GOOGLE
```
4. Ejecutar el frontend con el siguiente comando.
```
npm run dev
```
5. La aplicación estará disponible en [http://localhost:5173](http://localhost:5173).


## Uso

1. Ejecuta el backend y el frontend de la aplicación en tus editores de código favoritos.
2. Accede a la aplicación desde tu navegador web.
3. Inicia sesión como coordinador o voluntario según corresponda.
4. Explora las diferentes funcionalidades ofrecidas por el sistema, como registro de voluntarios, asignación de tareas, seguimiento de tareas, etc.
5. ¡Comienza a utilizar el sistema para gestionar emergencias de manera eficiente!

## Contribución

Las contribuciones son bienvenidas. Si deseas contribuir al desarrollo de este proyecto, por favor sigue estos pasos:

1. Haz un fork del repositorio.
2. Crea una rama para tu funcionalidad (`git checkout -b feature/AmazingFeature`).
3. Realiza tus cambios.
4. Haz commit de tus cambios (`git commit -m 'Add some AmazingFeature'`).
5. Haz push a la rama (`git push origin feature/AmazingFeature`).
6. Abre un pull request.

## Soporte

Para obtener ayuda o reportar problemas, por favor contacta al equipo de desarrollo.

## Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles.
