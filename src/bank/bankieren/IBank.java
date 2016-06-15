package bank.bankieren;

import bank.internettoegang.IBalie;
import fontys.util.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author 871059
 * 
 */
public interface IBank extends Remote {

    /**
     * creatie van een nieuwe bankrekening met een identificerend rekeningnummer; 
     * alleen als de klant, geidentificeerd door naam en plaats, nog niet bestaat 
     * wordt er ook een nieuwe klant aangemaakt
     * 
     * @param naam
     *            van de eigenaar van de nieuwe bankrekening
     * @param plaats
     *            de woonplaats van de eigenaar van de nieuwe bankrekening
     * @return -1 zodra naam of plaats een lege string en anders het nummer van de
     *         gecreeerde bankrekening
     */
    int openRekening(String naam, String plaats) throws RemoteException;

    /**
     * er wordt bedrag overgemaakt van de bankrekening met nummer bron naar de
     * bankrekening met nummer bestemming, mits het afschrijven van het bedrag
     * van de rekening met nr bron niet lager wordt dan de kredietlimiet van deze
     * rekening 
     * 
     * @param bron
     * @param bestemming
     *            ongelijk aan bron
     * @param bedrag
     *            is groter dan 0
     * @return <b>true</b> als de overmaking is gelukt, anders <b>false</b>
     * @throws NumberDoesntExistException
     *             als een van de twee bankrekeningnummers onbekend is
     */
    boolean maakOver(int bron, int bestemming, Money bedrag)
            throws NumberDoesntExistException, RemoteException;

    /**
     * @param nr
     * @return de bankrekening met nummer nr mits bij deze bank bekend, anders null
     */
    IRekening getRekening(int nr) throws RemoteException;

    /**
     * @return de naam van deze bank
     */
    String getName() throws RemoteException;

    /**
     * Gets de balie
     * @return Returnt de balie van de Bank
     * @throws RemoteException 
     */
    IBalie getBalie() throws RemoteException;
    
    /**
     * Voegt de bijbehorende balie toe aan de Bank
     * @param balie De balie die toegevoegd moet worden
     * @throws RemoteException 
     */
    void addBalie(IBalie balie) throws RemoteException;
}
