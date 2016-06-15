package bank.internettoegang;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import static bank.internettoegang.IBankiersessie.GELDIGHEIDSDUUR;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;

public class Bankiersessie extends UnicastRemoteObject implements IBankiersessie
{
    private static final long serialVersionUID = 1L;
    private long laatsteAanroep;
    private int reknr;
    private IBank bank;
    public long money;
    private IBalie balie;
    private BasicPublisher publisher;
    private final String prop = "bank";

    public Bankiersessie(int reknr, IBank bank, IBalie balie) throws RemoteException 
    {
        laatsteAanroep = System.currentTimeMillis();
        this.reknr = reknr;
        this.bank = bank;
        this.balie = balie;
        this.publisher = new BasicPublisher();
    }

    public boolean isGeldig() 
    {
        return System.currentTimeMillis() - laatsteAanroep < GELDIGHEIDSDUUR;
    }

    @Override
    public boolean maakOver(int bestemming, Money bedrag) throws NumberDoesntExistException, 
        InvalidSessionException, RemoteException 
    {
        updateLaatsteAanroep();

        if (reknr == bestemming)
        {
            throw new RuntimeException("source and destination must be different");
        }
        if (!bedrag.isPositive())
        { 
            throw new RuntimeException("amount must be positive");
        }

        if (bank.maakOver(reknr, bestemming, bedrag))
        {
            this.update();
            return true;
        }
        return false;
    }

    private void updateLaatsteAanroep() throws InvalidSessionException 
    {
        if (!isGeldig()) 
        {
            throw new InvalidSessionException("session has been expired");
        }	
        laatsteAanroep = System.currentTimeMillis();
    }

    @Override
    public IRekening getRekening() throws InvalidSessionException,
                    RemoteException {

            updateLaatsteAanroep();

            return bank.getRekening(reknr);
}

    @Override
    public void logUit() throws RemoteException 
    {
	UnicastRemoteObject.unexportObject(this, true);
    }


    @Override
    public void update() throws RemoteException 
    {
            try 
            {
                publisher.inform(this, prop, null, this.getRekening());
            } 
            catch (InvalidSessionException ex) 
            {
                System.out.println("InvalidSessionException ex: " + ex.getMessage());
            }
    }
    
    @Override
    public int getReknr() 
    {
        return this.reknr;
    }

    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException 
    {
        this.publisher.addListener(listener, property);
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException 
    {
        this.publisher.removeListener(listener, property);
    }
}
