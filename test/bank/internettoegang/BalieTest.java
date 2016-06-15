/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.internettoegang;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.ICentraleBank;
import java.rmi.RemoteException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Sven
 */
public class BalieTest
{
    Balie balie;
    IBank bank;
    ICentraleBank cbank;
    
    @Before
    public void initialize() throws RemoteException
    {
        bank = new Bank("Rabobank", cbank);
        balie = new Balie(bank);
    }
    
    /**
     * Test of openRekening method, of class Balie.
     */
    @Test
    public void testOpenRekening()
    {
        System.out.println("Test: openRekening");
        
        //Volledig correct
        assertNotNull(balie.openRekening("Sven", "Horst", "Wachtw"));
        
        //Incorrecte naam
        assertNull(balie.openRekening("", "Horst", "Wachtw"));
        
        //Incorrecte stad
        assertNull(balie.openRekening("Sven", "", "Wachtw"));
        
        //Tekort wacthtwoord
        assertNull(balie.openRekening("Sven", "Horst", "Wac"));
        
        //Telang wachtwoord
        assertNull(balie.openRekening("Sven", "Horst", "Wachtwoordddd"));
    }

    /**
     * Test of logIn method, of class Balie.
     */
    @Test
    public void testLogIn() throws Exception
    {
        System.out.println("Test: logIn");
        String account = (balie.openRekening("Sven", "Horst", "Wachtw"));
        
        //Geldige login
        System.out.println("Accountlogin: " + account);
        assertNotNull(balie.logIn(account, "Wachtw"));
        
        //Ongeldige username
        assertNull(balie.logIn("Sven", "Wachtw"));
        
        //Ongeldig wachtwoord
        assertNull(balie.logIn(account, "Wachtwoo"));
    }
    
}
