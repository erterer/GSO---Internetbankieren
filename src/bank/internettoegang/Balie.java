package bank.internettoegang;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import bank.bankieren.*;

public class Balie extends UnicastRemoteObject implements IBalie 
{
    private static final long serialVersionUID = -4194975069137290780L;
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private IBank bank;
    private HashMap<String, ILoginAccount> loginaccounts;
    private Collection<IBankiersessie> sessions;
    private java.util.Random random;

    public Balie(IBank bank) throws RemoteException 
    {
	this.bank = bank;
	loginaccounts = new HashMap<String, ILoginAccount>();
	sessions = new HashSet<IBankiersessie>();
	random = new Random();
    }

    public String openRekening(String naam, String plaats, String wachtwoord) 
    {
	if (naam == null || naam.equals(""))
        {
            return null;
        }
	if (plaats == null || plaats.equals(""))
        {
            return null;
        }
		
	if (wachtwoord == null || wachtwoord.length() < 4 || wachtwoord.length() > 8)
        {
            return null;
        }
               
        int nr = -1;
        try
        {
            nr = bank.openRekening(naam, plaats);
        }
        catch(RemoteException ex)
        {
            System.out.println("RemoteException: " + ex.getMessage());
            return null;
        }
	if (nr == -1)
        {
            return null;
        }

	String accountname = generateId(8);
	while (loginaccounts.containsKey(accountname))
        {
            accountname = generateId(8);
        }
	loginaccounts.put(accountname, new LoginAccount(accountname, wachtwoord, nr));
	return accountname;
    }

    public IBankiersessie logIn(String accountnaam, String wachtwoord) throws RemoteException 
    {
        ILoginAccount loginaccount = loginaccounts.get(accountnaam);
        if (loginaccount == null)
        {
            return null;
        }                    
        if (loginaccount.checkWachtwoord(wachtwoord)) 
        {
            IBankiersessie sessie = new Bankiersessie(loginaccount.getReknr(), bank, (IBalie)this);
            sessions.add(sessie);
            return sessie;
        }
        else
        {
            return null;
        }
    }

    private String generateId(int x) 
    {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < x; i++) 
        {
            int rand = random.nextInt(36);
            s.append(CHARS.charAt(rand));
        }
	return s.toString();
    }

    @Override
    public IBankiersessie getBankiersessie(int reknr) throws RemoteException 
    {
        for (IBankiersessie ibs : sessions)
        {
            if (ibs.getReknr() == reknr)
            {
                return ibs;
            }
        }
        return null;
    }
}
