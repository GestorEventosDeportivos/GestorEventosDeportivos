# Gestor eventos deportivos

Este proyecto es un aplicativo web desarrollado en Java que utiliza páginas JSP para la interfaz de usuario y servlets para manejar las peticiones HTTP (GET, POST, PUT). La aplicación permite gestionar eventos deportivos, equipos y jugadores, y además ofrece un endpoint para obtener estadísticas relevantes sobre los eventos.

## Tecnologías Utilizadas

- **Java**: Lógica de negocio y desarrollo de servlets. 
- **Maven** – Gestión de dependencias y build.
- **JSP**: Generación dinámica de páginas web.
- **Servlets**: Procesamiento de peticiones HTTP.
- **Gson**: Conversión entre objetos Java y JSON.
- **HTML/CSS/Tailwind CSS**: Diseño y estilos de la interfaz.
- **Servidor de Aplicaciones**: Apache Tomcat.

## Estructura del Proyecto

- **src/main/java**: Código fuente en Java.
    - **DAO**: Clases para el acceso a datos (EquipoDAO, EventoDAO, JugadorDAO).
    - **Servlets**: Controladores que extienden `HttpServlet` y se encargan de recibir las peticiones HTTP (GET, POST, PUT), invocar la lógica de negocio correspondiente y enviar las respuestas en formato JSON.
    - **Servicios**: Clases que implementan la lógica de negocio y validaciones de la aplicación. Actúan como puente entre los servlets y los repositorios, coordinando operaciones, procesando datos y realizando cálculos.
    - **Repositorios**: Clases responsables del acceso y persistencia de los datos (lectura y escritura). Aquí se implementa la lógica para consultar, actualizar y eliminar información de la fuente de datos en memoria.
- **src/main/webapp**: Páginas JSP y recursos estáticos (CSS, JavaScript, imágenes).
- **WEB-INF**: Configuración del despliegue (web.xml, etc.).
- **pom.xml / build.mvn**: Archivo de configuración para dependencias y compilación (según la herramienta de build que utilices).

## Funcionalidades

- **Gestión de Eventos**: Permite crear, editar, listar eventos deportivos.
- **Gestión de Equipos**: Administración de equipos con datos como nombre, deporte, ciudad, fecha de fundación,.. etc.
- **Gestión de Jugadores**: Registro y administración de jugadores asociados a equipos.
- **Estadísticas**: Endpoint GET `/estadisticas` que retorna:
    - Cantidad de eventos por deporte.
    - Promedio de jugadores por equipo.
    - Equipos con más eventos programados.
    - Porcentaje de ocupación de cada evento (entradas vendidas / capacidad).

## Requisitos

- **Java JDK 8 o superior**
- **Apache Tomcat 8 o superior** 
- **Maven** (para la gestión de dependencias y compilación)

## Instalación y Despliegue

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/jacobouribe/GestorEventosDeportivos.git

2. **Ir al proyecto**:
    ```bash
   cd gestoreventosdeportivos
   
3. **Instalar dependencias**:
    ```bash
   mvn clean install

4. **Arrancar servidor**:
Acceder a la aplicacion en: http://localhost:8080/GestorEventosDeportivos_war_exploded/



   
