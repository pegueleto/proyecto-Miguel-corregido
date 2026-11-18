# Sistema de Gestión de Estudiantes

Requisitos: JDK 17 y Maven.

Pruebas: mvn test
Ejecutar: mvn spring-boot:run

La API queda en http://localhost:8080/students

Crear:
curl -X POST http://localhost:8080/students -H "Content-Type: application/json" -d '{"nombre":"Juan Pérez","correo":"juan.perez@example.com","numero_telefono":"1234567890","idioma":"español"}'

Listar:
curl http://localhost:8080/students

Por id:
curl http://localhost:8080/students/1

Editar completo:
curl -X PUT http://localhost:8080/students/1 -H "Content-Type: application/json" -d '{"nombre":"Juan P.","correo":"juan.perez@example.com","numero_telefono":"1234567890","idioma":"inglés"}'

Editar parcial:
curl -X PATCH http://localhost:8080/students/1 -H "Content-Type: application/json" -d '{"idioma":"francés"}'

Eliminar:
curl -X DELETE http://localhost:8080/students/1
