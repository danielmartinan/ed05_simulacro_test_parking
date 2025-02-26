package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

  public static void main(String[] args) {
    Parking parking = new Parking("Parking de la Plaza", 100);

    parking.registrarAbonado("1234BBC", true);
    parking.registrarAbonado("5678DDF", false);
    parking.registrarAbonado("9012GHJ", true);
    parking.registrarAbonado("3456JKL", false);

    System.out.println("1234BBC es VIP: " + parking.esAbonadoVip("1234BBC"));
    System.out.println("5678DDF es VIP: " + parking.esAbonadoVip("5678DDF"));
    System.out.println("9012GHJ es VIP: " + parking.esAbonadoVip("9012GHJ"));
    System.out.println("3456JKL es VIP: " + parking.esAbonadoVip("3456JKL"));


    // escribimos los resultados de las tarifas con dos decimales
    System.out.printf("Tarifa 1 a pagar: %.2f € %n", parking.calcularTarifa(2, 30, false, false));
    System.out.printf("Tarifa 2 a pagar: %.2f € %n", parking.calcularTarifa(3, 15, true, false));
    System.out.printf("Tarifa 3 a pagar: %.2f € %n", parking.calcularTarifa(1, 45, false, true));
  }
}