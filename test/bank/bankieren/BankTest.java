/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import fontys.util.NumberDoesntExistException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniël-1
 */
public class BankTest
{
    Bank bank;
    
    public BankTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
        bank = new Bank("ABN Amro");
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of openRekening method, of class Bank.
     */
    @Test
    public void testOpenRekening()
    {
        System.out.println("openRekening");
        String name = "rekening";
        String city = "Amsterdam";
        Bank instance = bank;
        int expResult = 100000000;
        int result = instance.openRekening(name, city);
        assertEquals("openRekening", expResult, result);
        
        assertEquals("openRekening met lege strings", -1, instance.openRekening("", ""));
        assertEquals("openRekening met lege strings", -1, instance.openRekening(name, ""));
        assertEquals("openRekening met lege strings", -1, instance.openRekening("", city));
        
        int result2 = instance.openRekening(name, city);
        assertEquals("openRekening", 100000001, result2);
    }

    /**
     * Test of getRekening method, of class Bank.
     */
    @Test
    public void testGetRekening()
    {
        System.out.println("getRekening");
        bank.openRekening("Rekeningnaam", "Amsterdam");
        IRekening result = bank.getRekening(100000000);
        assertNotNull(result);
    }

    /**
     * Test of maakOver method, of class Bank.
     */

    @Test(expected = RuntimeException.class)  
    public void testOvermakenEigenRekeningException() throws Exception {    
        bank.maakOver(1, 1, new Money(500, "EURO"));
    }
    
    @Test(expected = RuntimeException.class)  
    public void testOvermakenNegatiefBedragException() throws Exception {    
        bank.maakOver(1, 2, new Money(-500, "EURO"));
    }

    @Test(expected = NumberDoesntExistException.class)  
    public void testOvermakenOnbekendeSourceException() throws Exception {    
        bank.maakOver(1, 2, new Money(500, "EURO"));
    }
    
    @Test(expected = RuntimeException.class)  
    public void testOvermakenOnbekendeDestinationException() throws Exception {  
        int rekeningNrSource = bank.openRekening("name", "city");
        //System.out.println(rekeningNrSource);
        bank.maakOver(rekeningNrSource, 1, new Money(200, "EURO"));
    }

//    @Test
//    public void testOvermakenSuccesvol() throws NumberDoesntExistException 
//    {
//        int rekeningNrSource = bank.openRekening("name", "city");
//        int rekeningNrDestination = bank.openRekening("name2", "city");
//        Rekening rekening = new Rekening(100000003, new Klant("name3", "city"), new Money(1000, "EURO"));
//        boolean result = bank.maakOver(rekening.getNr(), rekeningNrDestination, new Money(500, "EURO"));
//        assertEquals(true, result);
//    }
    /**
     * Test of getName method, of class Bank.
     */
    @Test
    public void testGetName()
    {
        System.out.println("getName");
        Bank instance = bank;
        String expResult = "ABN Amro";
        String result = instance.getName();
        assertEquals(expResult, result);
    }
    
}
