package com.estudiantes;

import com.estudiantes.model.Student;
import com.estudiantes.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryTest {

    private StudentRepository repository;

    @BeforeEach
    void preparar() {
        repository = new StudentRepository();
    }

    private Student nuevoEstudiante(String correo) {
        return new Student(0, "Juan Pérez", correo, "1234567890", "español");
    }

    @Test
    void guardarAsignaIdAutoincrementable() {
        Student primero = repository.guardar(nuevoEstudiante("a@example.com"));
        Student segundo = repository.guardar(nuevoEstudiante("b@example.com"));

        assertEquals(1, primero.getId());
        assertEquals(2, segundo.getId());
    }

    @Test
    void obtenerTodosDevuelveLosEstudiantesGuardados() {
        repository.guardar(nuevoEstudiante("a@example.com"));
        repository.guardar(nuevoEstudiante("b@example.com"));

        assertEquals(2, repository.obtenerTodos().size());
    }

    @Test
    void obtenerTodosEmpiezaVacio() {
        assertTrue(repository.obtenerTodos().isEmpty());
    }

    @Test
    void buscarPorIdEncuentraAlEstudiante() {
        Student guardado = repository.guardar(nuevoEstudiante("a@example.com"));

        assertEquals(guardado, repository.buscarPorId(guardado.getId()));
    }

    @Test
    void buscarPorIdInexistenteDevuelveNull() {
        assertNull(repository.buscarPorId(99));
    }

    @Test
    void buscarPorCorreoEncuentraAlEstudiante() {
        Student guardado = repository.guardar(nuevoEstudiante("a@example.com"));

        assertEquals(guardado, repository.buscarPorCorreo("a@example.com"));
    }

    @Test
    void buscarPorCorreoIgnoraMayusculas() {
        Student guardado = repository.guardar(nuevoEstudiante("a@example.com"));

        assertEquals(guardado, repository.buscarPorCorreo("A@EXAMPLE.COM"));
    }

    @Test
    void buscarPorCorreoInexistenteDevuelveNull() {
        repository.guardar(nuevoEstudiante("a@example.com"));

        assertNull(repository.buscarPorCorreo("otro@example.com"));
    }

    @Test
    void eliminarQuitaAlEstudiante() {
        Student guardado = repository.guardar(nuevoEstudiante("a@example.com"));

        repository.eliminar(guardado);

        assertTrue(repository.obtenerTodos().isEmpty());
        assertNull(repository.buscarPorId(guardado.getId()));
    }

    @Test
    void elIdNoSeReutilizaDespuesDeEliminar() {
        Student primero = repository.guardar(nuevoEstudiante("a@example.com"));
        repository.eliminar(primero);

        Student segundo = repository.guardar(nuevoEstudiante("b@example.com"));

        assertEquals(2, segundo.getId());
    }
}