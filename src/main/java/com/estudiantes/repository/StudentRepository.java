package com.estudiantes.repository;

import com.estudiantes.model.Student;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepository {

    private List<Student> estudiantes = new ArrayList<>();
    private int siguienteId = 1;

    public List<Student> obtenerTodos() {
        return estudiantes;
    }

    public Student buscarPorId(int id) {
        for (Student s : estudiantes) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public Student buscarPorCorreo(String correo) {
        for (Student s : estudiantes) {
            if (s.getCorreo().equalsIgnoreCase(correo)) {
                return s;
            }
        }
        return null;
    }

    public Student guardar(Student estudiante) {
        estudiante.setId(siguienteId);
        siguienteId++;
        estudiantes.add(estudiante);
        return estudiante;
    }

    public void eliminar(Student estudiante) {
        estudiantes.remove(estudiante);
    }
}
