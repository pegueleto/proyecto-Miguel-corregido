package com.estudiantes.controller;

import com.estudiantes.model.Student;
import com.estudiantes.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Student> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Student buscar(@PathVariable int id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student crear(@RequestBody Student estudiante) {
        return service.crear(estudiante);
    }

    @PutMapping("/{id}")
    public Student actualizar(@PathVariable int id, @RequestBody Student estudiante) {
        return service.actualizar(id, estudiante);
    }

    @PatchMapping("/{id}")
    public Student actualizarParcial(@PathVariable int id, @RequestBody Student estudiante) {
        return service.actualizarParcial(id, estudiante);
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable int id) {
        service.eliminar(id);
        return "Estudiante eliminado correctamente";
    }
}
