package bank.bankieren;

import bank.internettoegang.IBalie;
import bank.internettoegang.IBankiersessie;
import fontys.util.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Bank extends UnicastRemoteObject implements IBank  
{
    private static final long serialVersionUID = -8728841131739353765L;
    private Map<Integer,IRekeningTbvBank> accounts;
    private Collection<IKlant> clients;
    private int nieuwReknr;
    private String name;
    private ICentraleBank centraleBank;
    private IBalie balie;

    public Bank(String name, ICentraleBank cb) throws RemoteException 
    {
        accounts = new HashMap<Integer,IRekeningTbvBank>();
	clients = new ArrayList<IKlant>();
	this.name = name;
        nieuwReknr = 100000000;
        centraleBank = cb;
        switch (name) 
        {
            case "RaboBank":
                nieuwReknr = 100000000;
                break;
            case "ING":
                nieuwReknr = 200000000;
                break;
            case "SNS":
                nieuwReknr = 300000000;
                break;
            case "ABN AMRO":
                nieuwReknr = 400000000;
                break;
            case "ASN":
                nieuwReknr = 500000000;
                break;
        }
    }

    public int openRekening(String name, String city) 
    {
	if (name.equals("") || city.equals(""))
        {
            return -1;
        }
	IKlant klant = getKlant(name, city);
	IRekeningTbvBank account = new Rekening(nieuwReknr, klant, Money.EURO);
	accounts.put(nieuwReknr,account);
	nieuwReknr++;
	return nieuwReknr-1;
    }

    private IKlant getKlant(String name, String city) 
    {
        for (IKlant k : clients) 
        {
            if (k.getNaam().equals(name) && k.getPlaats().equals(city))
            {
                return k;
            }
        }
        IKlant klant = new Klant(name, city);
        clients.add(klant);
        return klant;
    }

    public IRekening getRekening(int nr) 
    {
            return accounts.get(nr);
    }

    public boolean maakOver(int source, int destination, Money money) throws NumberDoesntExistException 
    {
        Money negative = Money.difference(new Money(0, money.getCurrency()), money);
        if (source == destination)
                throw new RuntimeException("cannot transfer money to your own account");
        if (!money.isPositive())
                throw new RuntimeException("money must be positive");

        IRekeningTbvBank source_account = (IRekeningTbvBank) getRekening(source);
        if (source_account == null) 
        {
            throw new NumberDoesntExistException("account " + destination + " unknown at " + name);
        }

        boolean success = source_account.muteer(negative);
        if (!success)
        {
            return false;
        }
        
        IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(destination);
        if (dest_account == null) 
        {
            Bank bank = null;

            try
            {
                bank = (Bank)centraleBank.getBank(destination);
            }
            catch(RemoteException ex)
            {
                System.out.println("RemoteException: " + ex.getMessage());
            }

            if(bank == null) 
            {
                source_account.muteer(money);
                throw new NumberDoesntExistException("account " + source + " unknown at every bank");
            }
            else 
            {
                if(!bank.maakOverAndereBank(destination, money)) 
                {
                    source_account.muteer(money);
                    return false;
                }
                try
                {
                    IBankiersessie ibs = bank.getBalie().getBankiersessie(destination);
                    if (ibs != null)
                    {
                        ibs.update();
                    }
                }
                catch (RemoteException ex)
                {
                    System.out.println("RemoteException: " + ex.getMessage());
                }
                return true;
            }
        }
        success = dest_account.muteer(money);
        if (!success) // rollback
        {
            source_account.muteer(money);
        }
        else
        {
            try
            {
                IBankiersessie ibs = balie.getBankiersessie(destination);
                if (ibs != null)
                {
                    ibs.update();
                }
            }
            catch (RemoteException ex)
            {
                System.out.println("RemoteException: " + ex.getMessage());
            }
        }
        return success;
    }

    public boolean maakOverAndereBank(int destination, Money money)
    {
        boolean success;
        Money negative = Money.difference(new Money(0, money.getCurrency()), money);
        IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(destination);
        success = dest_account.muteer(money);
        return success;
    }

    @Override
    public String getName() 
    {
        return name;
    }

    @Override
    public IBalie getBalie()
    {
        return this.balie;
    }

    @Override
    public void addBalie(IBalie balie)
    {
        this.balie = balie;
    }
}
