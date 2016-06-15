package bank.bankieren;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICentraleBank extends Remote 
{
    
    /**
     * Haalt de bank op die het gegeven rekeningnummer bevat
     * @param rekeningNr Het gegeven rekeningnummer
     * @return De bank die het rekeningnummer bevat, anders wordt er null teruggegeven
     * @throws RemoteException als er Remote iets fout gaat
     */
    IBank getBank(int rekeningNr) throws RemoteException;
    
    /**
     * Haalt de bank op die de gegeven naam bevat
     * @param bankName De gegeven banknaam
     * @return Return de Bank met de gegeven banknaam, anders wordt er null teruggegeven
     * @throws RemoteException als er Remote iets fout gaat
     */
    IBank getBankFromName(String bankName) throws RemoteException;
    
    /**
     * Voegt een bank toe aan de centrale server
     * @param bank De bank die moet worden toegevoegd
     * @throws RemoteException als er Remote iets fout gaat
     */
    void addBank(IBank bank) throws RemoteException;
}
