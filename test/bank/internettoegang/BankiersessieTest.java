/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.internettoegang;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import java.rmi.RemoteException;
import java.util.concurrent.CountDownLatch;
import junit.framework.AssertionFailedError;
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
public class BankiersessieTest
{

    Bankiersessie bankierSessie;

    public BankiersessieTest()
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
    public void setUp() throws RemoteException
    {
        IBank bank = new Bank("ABN Amro");
        bank.openRekening("newRekening", "Amsterdam");
        bankierSessie = new Bankiersessie(100000000, bank);
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of isGeldig method, of class Bankiersessie.
     */
    @Test
    public void testIsGeldig() throws InterruptedException
    {
        System.out.println("isGeldig");
        Bankiersessie instance = bankierSessie;
        boolean result = instance.isGeldig();
        assertEquals(true, result);
    }

    /**
     * Test of maakOver method, of class Bankiersessie.
     */
    @Test(expected = RuntimeException.class)
    public void testMaakOverZelfdeRekeningException() throws Exception
    {
        System.out.println("maakOver");

        Money bedrag = new Money(500, "EURO");
        Bankiersessie instance = bankierSessie;
        boolean expResult = false;
        boolean result = instance.maakOver(100000000, bedrag);
        assertEquals(expResult, result);
    }

    @Test(expected = RuntimeException.class)
    public void testMaakOverNegatiefBedrag() throws Exception
    {
        System.out.println("maakOver");

        Money bedrag = new Money(-500, "EURO");
        Bankiersessie instance = bankierSessie;
        boolean expResult = false;
        boolean result = instance.maakOver(100000000, bedrag);
        assertEquals(expResult, result);
    }

    @Test(expected = RuntimeException.class)
    public void testMaakOver() throws Exception
    {
        System.out.println("maakOver");

        Money bedrag = new Money(500, "EURO");
        Bankiersessie instance = bankierSessie;
        boolean expResult = true;
        boolean result = instance.maakOver(100000000, bedrag);
        assertEquals(expResult, result);
    }

    /**
     * Test of getRekening method, of class Bankiersessie.
     */
    @Test
    public void testGetRekening() throws Exception
    {
        System.out.println("getRekening");

        Bankiersessie instance = bankierSessie;
        assertEquals("newRekening", instance.getRekening().getEigenaar().getNaam());
        assertEquals(100000000, instance.getRekening().getNr());
    }

    /**
     * Test of logUit method, of class Bankiersessie.
     */
    @Test(expected = AssertionFailedError.class)
    public void testLogUit() throws Exception
    {
        System.out.println("logUit");
        Bankiersessie instance = bankierSessie;
        instance.logUit();
        instance.getRekening();
    }

}
