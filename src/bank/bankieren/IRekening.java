package bank.bankieren;

import java.io.Serializable;

public interface IRekening extends Serializable 
{
  /**
   * Haalt het rekeningnummer op
   * @return Het rekeningnummer van de rekening
   */  
  int getNr();
  
  /**
   * Haalt het saldo van de rekening op
   * @return Het saldo van de ingegeven rekening
   */
  Money getSaldo();
  
  /**
   * Haalt de eigenaar van de rekening op
   * @return De eigenaar van de rekening in een Klant object
   */
  IKlant getEigenaar();
  
  /**
   * Haalt het kredietlimiet op
   * @return Het kredietlimiet in centen
   */
  int getKredietLimietInCenten();
}

