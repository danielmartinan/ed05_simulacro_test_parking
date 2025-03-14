package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParkingTest {

    Parking parking;

    @BeforeEach
    public void setUp() {
        parking = new Parking("Parking de la Plaza", 100);
    }

    // Pruebas de caja blanca del método calcularTarifa.
    //| Nº | Camino de prueba | Descripción |
    //    |----|-------------------|-------------|
    //    | 1  | A-B-F-T | `Horas < 0` |
    //    | 2  | A-B-C-F-T | `Minutos < 0` |
    //    | 3  | A-B-C-D-F-T | `Minutos >= 60` |
    //    | 4  | A-B-C-D-E-G-H-P-R-S-T | `Horas >= 10` y `esAbonado == true` |
    //    | 5  | A-B-C-D-E-G-H-P-S-T | `Horas >= 10` y `esAbonado == false` |
    //    | 6  | A-B-C-D-E-G-J-K-L-M-N-P-S-T | `Horas < 10`, `minutos > 15`, `tarifaNoctura == true` y `esAbonado == false` |
    //    | 7  | A-B-C-D-E-G-J-K-M-N-P-S-T | `Horas < 10`, `minutos <= 15`, `tarifaNoctura == true`  y `esAbonado == false` |
    //    | 8  | A-B-C-D-E-G-J-K-M-P-S-T | `Horas < 10`, `minutos <= 15`, `tarifaNoctura == false`  y `esAbonado == false` |

    @Test
    void testCalcularTarifaHorasNegativo() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> parking.calcularTarifa(-1, 30, false, false),
            "Se esperaba una IllegalArgumentException"
        );
        assertTrue(thrown.getMessage().contains("Tiempo inválido"));
    }

    @Test
    void testCalcularTarifaMinutosNegativo() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> parking.calcularTarifa(2, -1, false, false),
            "Se esperaba una IllegalArgumentException"
        );
        assertTrue(thrown.getMessage().contains("Tiempo inválido"));
    }

    @Test
    void testCalcularTarifaMinutosIgualA60() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> parking.calcularTarifa(2, 60, false, false),
            "Se esperaba una IllegalArgumentException"
        );
        assertTrue(thrown.getMessage().contains("Tiempo inválido"));
    }

    @Test
    void testCalcularTarifaHorasMayorIgualA10EsAbonado() {
        assertEquals(14.4, parking.calcularTarifa(10, 0, true, false), 0.01);
    }

    @Test
    void testCalcularTarifaHorasMayorIgualA10NoEsAbonado() {
        assertEquals(18, parking.calcularTarifa(10, 0, false, false), 0.01);
    }

    @Test
    void testCalcularTarifaHorasMenorA10MinutosMayorA15TarifaNocturnaNoAbonado() {
        assertEquals(10, parking.calcularTarifa(9, 30, false, true), 0.01);
    }

    @Test
    void testCalcularTarifaHorasMenorA10MinutosMenorA15TarifaNocturnaNoAbonado() {
        assertEquals(9, parking.calcularTarifa(9, 0, false, true), 0.01);
    }

    @Test
    void testCalcularTarifaHorasMenorA10MinutosMenorA15TarifaDiurnaNoAbonado() {
        assertEquals(18, parking.calcularTarifa(9, 0, false, false), 0.01);
    }

    // Tests de caja negra para registrarAbonado
    //    | Caso de prueba | Clases de equivalencia | matricula | esVIP | Resultado esperado |
    //    |----------------|----------------------- |-----------|-------|--------------------|
    //    | CP1            | V1, V2                 | "0000BBB" | true  | true               |
    //    | CP2            | V1, NV8                | "0000BBB" | false | true               |
    //    | CP3            | NV2, V2                | null      | true  | IllegalArgumentException |
    //    | CP4            | NV3, V2                | ""        | true  | IllegalArgumentException |
    //    | CP5            | NV4, V2                | "0000 BB" | true  | IllegalArgumentException |
    //    | CP6            | NV5, V2                | "A000BBB" | true  | IllegalArgumentException |
    //    | CP7            | NV6, V2                | "0000bbb" | true  | IllegalArgumentException |

    @Test
    public void testRegistrarAbonadoMatriculaValidaEsVip() {
        assertTrue(parking.registrarAbonado("1234BBC", true));
    }

    @Test
    public void testRegistrarAbonadoMatriculaValidaNoEsVip() {
        assertTrue(parking.registrarAbonado("1234BBC", false));
    }

    @Test
    public void testRegistrarAbonadoMatriculaNulaEsVip() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> parking.registrarAbonado(null, true),
            "Se esperaba una IllegalArgumentException"
        );
        assertTrue(thrown.getMessage().contains("Matrícula inválida"));
    }

    @Test
    public void testRegistrarAbonadoMatriculaVaciaEsVip() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> parking.registrarAbonado("", true),
            "Se esperaba una IllegalArgumentException"
        );
        assertTrue(thrown.getMessage().contains("Matrícula inválida"));
    }

    @Test
    public void testRegistrarAbonadoMatriculaMatriculaConEspaciosEsVip() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> parking.registrarAbonado("0000 BB", true),
            "Se esperaba una IllegalArgumentException"
        );
        assertTrue(thrown.getMessage().contains("Formato de matrícula incorrecto"));
    }

    @Test
    public void testRegistrarAbonadoMatriculaMatriculaConLetraInicialEsVip() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> parking.registrarAbonado("A000BBB", true),
            "Se esperaba una IllegalArgumentException"
        );
        assertTrue(thrown.getMessage().contains("Formato de matrícula incorrecto"));
    }

    @Test
    public void testRegistrarAbonadoMatriculaMatriculaEnMinusculasEsVip() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> parking.registrarAbonado("0000bbb", true),
            "Se esperaba una IllegalArgumentException"
        );
        assertTrue(thrown.getMessage().contains("Formato de matrícula incorrecto"));
    }


    //Otros tests
    @Test
    public void testRegistrarAbonado() {
        assertTrue(parking.registrarAbonado("1234BBC", true));
        assertFalse(parking.registrarAbonado("1234BBC", false));
    }

    @Test
    public void testEsAbonadoVip() {
        parking.registrarAbonado("1234BBC", true);
        assertTrue(parking.esAbonadoVip("1234BBC"));
        assertFalse(parking.esAbonadoVip("5678DDF"));
    }

    @Test
    public void testCalcularTarifa() {
        assertEquals(6.0, parking.calcularTarifa(2, 30, false, false), 0.01);
        assertEquals(4.80, parking.calcularTarifa(3, 15, true, false), 0.01);
        assertEquals(2, parking.calcularTarifa(1, 45, false, true), 0.01);
    }

}
