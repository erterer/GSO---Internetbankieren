package bank.bankieren;

import java.io.Serializable;

public interface IKlant extends Serializable,Comparable<IKlant> 
{
  /**
   * Haalt de naam op van een klant
   * @return De naam van de Klant
   */
  String getNaam();
  
  /**
   * Haalt de plaats op van een klant
   * @return De naam van de klant
   */
  String getPlaats();
}

