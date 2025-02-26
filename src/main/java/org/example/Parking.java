package org.example;

import java.util.HashMap;
import java.util.Map;

public class Parking {
  private static final String FORMATO_MATRICULA = "^[0-9]{4}[BCDFGHJKLMNPRSTVWXYZ]{3}$"; // Ejemplo: 1234BCD
  private final Map<String, Boolean> abonados = new HashMap<>(); // Cada matrícula se asocia a un booleano (VIP o no VIP)
  private String nombre;
  private int plazas;

  public Parking(String nombre, int plazas) {
    this.nombre = nombre;
    this.plazas = plazas;
  }
  /**
   * Registra un vehículo como abonado en el sistema de parking.
   *
   * @param matricula Matrícula del vehículo a registrar. Debe cumplir con el formato "0000BBB".
   * @param esVIP Indica si el abonado es VIP (true) o no (false).
   * @return true si el registro es exitoso, false si la matrícula ya estaba registrada.
   * @throws IllegalArgumentException si la matrícula es nula, vacía o no cumple el formato.
   */
  public boolean registrarAbonado(String matricula, boolean esVIP) {
    if (matricula == null || matricula.trim().isEmpty()) {
      throw new IllegalArgumentException("Matrícula inválida");
    }

    if (!matricula.matches(FORMATO_MATRICULA)) {
      throw new IllegalArgumentException("Formato de matrícula incorrecto");
    }

    if (abonados.containsKey(matricula)) {
      return false; // La matrícula ya está registrada
    }

    abonados.put(matricula, esVIP);
    return true; // Registro exitoso
  }

  /**
   * Verifica si un vehículo está registrado como abonado.
   *
   * @param matricula Matrícula del vehículo a verificar.
   * @return true si el vehículo está registrado como abonado VIP, false en caso contrario.
   */
  public boolean esAbonadoVip(String matricula) {
    return abonados.getOrDefault(matricula, false);
  }

  /**
   * Calcula la tarifa a pagar por el estacionamiento de un vehículo.
   *
   * @param horas Número de horas completas de estacionamiento.
   * @param minutos Fracción de hora adicional (0-59).
   * @param esAbonado Indica si el vehículo es abonado.
   * @param tarifaNocturna Indica si el estacionamiento se realizó en horario nocturno (22:00 - 06:00).
   * @return Tarifa a pagar por el estacionamiento.
   * @throws IllegalArgumentException si los valores de horas o minutos son inválidos.
   */
  public double calcularTarifa(int horas, int minutos, boolean esAbonado, boolean tarifaNocturna) {
    if (horas < 0 || minutos < 0 || minutos >= 60) {
      throw new IllegalArgumentException("Tiempo inválido");
    }

    double tarifa = 0;

    // Si el tiempo supera las 10 horas, se cobra tarifa fija
    if (horas >= 10) {
      tarifa = 18;
    } else {
      tarifa = horas * 2.0;  // 2€/hora

      // Si hay fracción mayor a 15 min, se cobra una hora más
      if (minutos > 15) {
        tarifa += 2.0;
      }

      // Si es horario nocturno (22:00 - 06:00), se cobra al 50%
      if (tarifaNocturna) {
        tarifa *= 0.5;
      }
    }

    // Descuento para abonados
    if (esAbonado) {
      tarifa *= 0.8;
    }

    return tarifa;
  }
}