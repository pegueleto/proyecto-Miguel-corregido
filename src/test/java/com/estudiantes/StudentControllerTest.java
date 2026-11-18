package com.estudiantes;

import com.estudiantes.controller.StudentController;
import com.estudiantes.exception.ResourceNotFoundException;
import com.estudiantes.model.Student;
import com.estudiantes.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService service;

    @InjectMocks
    private StudentController controller;

    private Student estudiante() {
        return new Student(1, "Juan Pérez", "juan@example.com", "1234567890", "español");
    }

    @Test
    void listarDevuelveLosEstudiantes() {
        List<Student> lista = new ArrayList<>();
        lista.add(estudiante());
        when(service.listar()).thenReturn(lista);

        assertEquals(1, controller.listar().size());
    }

    @Test
    void buscarDevuelveElEstudiante() {
        when(service.buscar(1)).thenReturn(estudiante());

        assertEquals("Juan Pérez", controller.buscar(1).getNombre());
    }

    @Test
    void buscarInexistenteLanzaError() {
        when(service.buscar(9)).thenThrow(new ResourceNotFoundException("No existe"));

        assertThrows(ResourceNotFoundException.class, () -> controller.buscar(9));
    }

    @Test
    void crearLlamaAlService() {
        Student nuevo = estudiante();
        when(service.crear(nuevo)).thenReturn(nuevo);

        assertEquals(nuevo, controller.crear(nuevo));
    }

    @Test
    void actualizarLlamaAlService() {
        Student datos = estudiante();
        when(service.actualizar(1, datos)).thenReturn(datos);

        assertEquals(datos, controller.actualizar(1, datos));
    }

    @Test
    void actualizarParcialLlamaAlService() {
        Student datos = new Student();
        datos.setIdioma("francés");
        when(service.actualizarParcial(1, datos)).thenReturn(datos);

        assertEquals(datos, controller.actualizarParcial(1, datos));
    }

    @Test
    void eliminarDevuelveConfirmacion() {
        String mensaje = controller.eliminar(1);

        assertEquals("Estudiante eliminado correctamente", mensaje);
        verify(service).eliminar(1);
    }
}
