# Campus Recursos — Entrega 1 (MPA)

Prototipo de la primera entrega del proyecto "Sistema inteligente para la gestion
de reservas, prestamos y mantenimiento de recursos en un campus universitario".

## Como ejecutarlo

Requisitos: JDK 17 y Maven (o el wrapper `./mvnw` incluido).

```bash
./mvnw spring-boot:run
```

La aplicacion queda disponible en `http://localhost:8080`.

La base de datos es H2 en memoria: se recrea desde cero cada vez que se reinicia
la aplicacion, y el Programa Batch (`DatosIniciales`, en el paquete `config`)
la vuelve a poblar automaticamente con categorias, ubicaciones, recursos y un
par de reservas de ejemplo. Se puede inspeccionar la base de datos en
`http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:campusdb`, usuario
`sa`, sin contraseña).

## Alcance implementado (segun la seccion "Aclaraciones de las entregas")

- Gestion de recursos y disponibilidad: CRUD de recursos, categorias y
  ubicaciones; catalogo con filtros por categoria, ubicacion, tipo y estado.
- Gestion de reservas: crear, consultar, modificar y cancelar; validacion de
  solapamiento (RN-01) y de bloqueo (RN-02) en el servidor.
- Prestamos y devoluciones: registro de entrega y devolucion, con deteccion de
  devolucion tardia (RN-03 tambien validada).
- Incidentes: formulario de reporte; un incidente critico bloquea el recurso
  automaticamente (RN-04).
- Analitica inicial: tablero con el estado de cada recurso y el numero de
  reservas por recurso.
- Programa Batch en Spring (`CommandLineRunner`) que carga los datos maestros.

## Fuera de alcance (a proposito)

- **Mantenimiento** como modulo independiente: el enunciado lo ubica
  explicitamente en la segunda entrega. Lo unico relacionado que si aplica a
  esta entrega es el bloqueo automatico por incidente critico (RN-04), que si
  esta implementado. Un gestor puede desbloquear un recurso manualmente desde
  el formulario de edicion de Recurso (cambiando su estado), como solucion
  simple mientras no existe el modulo formal de mantenimiento.
- **Autenticacion y roles**: el enunciado indica expresamente que no se
  implementan hasta la tercera entrega. Por eso "solicitante" y "responsable"
  son campos de texto libres en vez de una relacion a un usuario con login.

## Decisiones tecnicas (y por que)

- **Spring Boot + Spring MVC + Thymeleaf + Spring Data JPA**, en vez de
  Servlets crudos: es el patron de MPA que sí se usa con JPA en el curso (ver
  los ejercicios de biblioteca y de tareas). Los Servlets puros vistos en
  clase nunca se combinan con persistencia.
- **H2 en memoria**: simplifica la ejecucion del prototipo sin depender de un
  motor de base de datos externo instalado. Cambiar a MySQL/PostgreSQL en el
  futuro solo implica ajustar `application.properties` y la dependencia del
  driver en el `pom.xml`.
- **CSS plano**, sin Tailwind ni otro framework: en el modulo de CSS del
  curso, los frameworks se mencionan como opcion, pero no se ensena su uso a
  fondo.
- **Fragmentos de Thymeleaf** (`fragments/nav.html`, `fragments/mensajes.html`):
  no aparecen explicitamente en los ejemplos de clase, pero son una
  caracteristica basica de Thymeleaf que evita duplicar el menu de navegacion
  y el manejo de mensajes de exito/error en cada una de las ~15 pantallas.
