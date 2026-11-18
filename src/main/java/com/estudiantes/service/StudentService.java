package com.estudiantes.service;

import com.estudiantes.exception.ResourceNotFoundException;
import com.estudiantes.model.Student;
import com.estudiantes.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StudentService {

    private StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> listar() {
        return repository.obtenerTodos();
    }

    public Student buscar(int id) {
        Student estudiante = repository.buscarPorId(id);
        if (estudiante == null) {
            throw new ResourceNotFoundException("No existe el estudiante con id " + id);
        }
        return estudiante;
    }

    public Student crear(Student nuevo) {
        validarNombre(nuevo.getNombre());
        validarCorreo(nuevo.getCorreo(), 0);
        validarTelefono(nuevo.getNumeroTelefono());
        validarIdioma(nuevo.getIdioma());
        return repository.guardar(nuevo);
    }

    public Student actualizar(int id, Student datos) {
        Student estudiante = buscar(id);
        validarNombre(datos.getNombre());
        validarCorreo(datos.getCorreo(), id);
        validarTelefono(datos.getNumeroTelefono());
        validarIdioma(datos.getIdioma());
        estudiante.setNombre(datos.getNombre());
        estudiante.setCorreo(datos.getCorreo());
        estudiante.setNumeroTelefono(datos.getNumeroTelefono());
        estudiante.setIdioma(datos.getIdioma());
        return estudiante;
    }

    public Student actualizarParcial(int id, Student datos) {
        Student estudiante = buscar(id);
        if (datos.getNombre() != null) {
            validarNombre(datos.getNombre());
        }
        if (datos.getCorreo() != null) {
            validarCorreo(datos.getCorreo(), id);
        }
        if (datos.getNumeroTelefono() != null) {
            validarTelefono(datos.getNumeroTelefono());
        }
        if (datos.getIdioma() != null) {
            validarIdioma(datos.getIdioma());
        }
        if (datos.getNombre() != null) {
            estudiante.setNombre(datos.getNombre());
        }
        if (datos.getCorreo() != null) {
            estudiante.setCorreo(datos.getCorreo());
        }
        if (datos.getNumeroTelefono() != null) {
            estudiante.setNumeroTelefono(datos.getNumeroTelefono());
        }
        if (datos.getIdioma() != null) {
            estudiante.setIdioma(datos.getIdioma());
        }
        return estudiante;
    }

    public void eliminar(int id) {
        Student estudiante = buscar(id);
        repository.eliminar(estudiante);
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty() || nombre.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El nombre es obligatorio y no puede exceder 255 caracteres");
        }
    }

    private void validarCorreo(String correo, int idActual) {
        if (correo == null || !correo.contains("@") || !correo.contains(".")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El correo no tiene un formato válido");
        }
        Student otro = repository.buscarPorCorreo(correo);
        if (otro != null && otro.getId() != idActual) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un estudiante con ese correo");
        }
    }

    private void validarTelefono(String telefono) {
        if (telefono == null || !telefono.matches("[0-9]{10}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El teléfono debe tener exactamente 10 dígitos numéricos");
        }
    }

    private void validarIdioma(String idioma) {
        if (idioma == null
                || !(idioma.equals("inglés") || idioma.equals("español") || idioma.equals("francés"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El idioma debe ser: inglés, español o francés");
        }
    }
}
