package bank.internettoegang;

public interface ILoginAccount 
{
    /**
     * Haalt de naam op van een account
     * @return De accountnaam van het opgegeven account
     */
    String getNaam();
    
    /**
     * Haalt het rekeningnummer op van een account
     * @return Het rekeningnummer van het opgegeven account
     */
    int getReknr(); 
    
    /**
     * Checkt of het wachtwoord correct is
     * @param wachtwoord Het ingegeven wachtwoord
     * @return True als het wachtwoord juist is, anders false
     */
    boolean checkWachtwoord(String wachtwoord);
}

