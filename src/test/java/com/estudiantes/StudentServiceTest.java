package com.estudiantes;

import com.estudiantes.exception.ResourceNotFoundException;
import com.estudiantes.model.Student;
import com.estudiantes.repository.StudentRepository;
import com.estudiantes.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    @Test
    void crearGuardaElEstudiante() {
        Student nuevo = new Student(0, "Juan Pérez", "juan@example.com", "1234567890", "español");
        when(repository.guardar(nuevo)).thenReturn(nuevo);

        Student resultado = service.crear(nuevo);

        assertEquals("Juan Pérez", resultado.getNombre());
        verify(repository).guardar(nuevo);
    }

    @Test
    void crearConNombreVacioLanzaError() {
        Student nuevo = new Student(0, "", "juan@example.com", "1234567890", "español");

        assertThrows(ResponseStatusException.class, () -> service.crear(nuevo));
        verify(repository, never()).guardar(any());
    }

    @Test
    void crearConCorreoInvalidoLanzaError() {
        Student nuevo = new Student(0, "Juan", "juan.com", "1234567890", "español");

        assertThrows(ResponseStatusException.class, () -> service.crear(nuevo));
    }

    @Test
    void crearConCorreoRepetidoLanzaError() {
        Student existente = new Student(1, "Ana", "juan@example.com", "0987654321", "inglés");
        when(repository.buscarPorCorreo("juan@example.com")).thenReturn(existente);
        Student nuevo = new Student(0, "Juan", "juan@example.com", "1234567890", "español");

        assertThrows(ResponseStatusException.class, () -> service.crear(nuevo));
        verify(repository, never()).guardar(any());
    }

    @Test
    void crearConTelefonoInvalidoLanzaError() {
        Student nuevo = new Student(0, "Juan", "juan@example.com", "123", "español");

        assertThrows(ResponseStatusException.class, () -> service.crear(nuevo));
    }

    @Test
    void crearConIdiomaInvalidoLanzaError() {
        Student nuevo = new Student(0, "Juan", "juan@example.com", "1234567890", "alemán");

        assertThrows(ResponseStatusException.class, () -> service.crear(nuevo));
    }

    @Test
    void buscarInexistenteLanzaError() {
        when(repository.buscarPorId(9)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.buscar(9));
    }

    @Test
    void actualizarCambiaTodosLosDatos() {
        Student existente = new Student(1, "Juan Pérez", "juan@example.com", "1234567890", "español");
        when(repository.buscarPorId(1)).thenReturn(existente);
        Student datos = new Student(0, "Ana", "ana@example.com", "0987654321", "inglés");

        Student resultado = service.actualizar(1, datos);

        assertEquals("Ana", resultado.getNombre());
        assertEquals("ana@example.com", resultado.getCorreo());
        assertEquals("0987654321", resultado.getNumeroTelefono());
        assertEquals("inglés", resultado.getIdioma());
    }

    @Test
    void actualizarParcialSoloCambiaElIdioma() {
        Student existente = new Student(1, "Juan Pérez", "juan@example.com", "1234567890", "español");
        when(repository.buscarPorId(1)).thenReturn(existente);
        Student datos = new Student();
        datos.setIdioma("francés");

        Student resultado = service.actualizarParcial(1, datos);

        assertEquals("francés", resultado.getIdioma());
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals("juan@example.com", resultado.getCorreo());
    }

    @Test
    void eliminarQuitaAlEstudiante() {
        Student existente = new Student(1, "Juan Pérez", "juan@example.com", "1234567890", "español");
        when(repository.buscarPorId(1)).thenReturn(existente);

        service.eliminar(1);

        verify(repository).eliminar(existente);
    }
}
