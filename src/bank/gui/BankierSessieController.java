package bank.gui;

import bank.bankieren.IRekening;
import bank.bankieren.Money;
import bank.internettoegang.IBalie;
import bank.internettoegang.IBankiersessie;
import fontys.observer.RemotePropertyListener;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BankierSessieController extends UnicastRemoteObject implements Initializable, RemotePropertyListener {

    @FXML
    private Hyperlink hlLogout;

    @FXML
    private TextField tfNameCity;
    @FXML
    private TextField tfAccountNr;
    @FXML
    private TextField tfBalance;
    @FXML
    private TextField tfAmount;
    @FXML
    private TextField tfToAccountNr;
    @FXML
    private Button btTransfer;
    @FXML
    
    private TextArea taMessage;

    private BankierClient application;
    private IBalie balie;
    private IBankiersessie sessie;
    private final String prop = "bank";

    public BankierSessieController() throws RemoteException 
    {
        
    }
    
    public void setApp(BankierClient application, IBalie balie, IBankiersessie sessie) 
    {
        this.balie = balie;
        this.sessie = sessie;
        this.application = application;
        
        try 
        {
            this.sessie.addListener(this, prop);
        } 
        catch (RemoteException ex) 
        {
            ex.printStackTrace();
        }
        
        IRekening rekening = null;
        try 
        {
            rekening = sessie.getRekening();
            tfAccountNr.setText(rekening.getNr() + "");
            tfBalance.setText(rekening.getSaldo() + "");
            String eigenaar = rekening.getEigenaar().getNaam() + " te "
                    + rekening.getEigenaar().getPlaats();
            tfNameCity.setText(eigenaar);
        } 
        catch (InvalidSessionException ex) 
        {
            taMessage.setText("bankiersessie is verlopen");
            Logger.getLogger(BankierSessieController.class.getName()).log(Level.SEVERE, null, ex);

        } 
        catch (RemoteException ex) 
        {
            taMessage.setText("verbinding verbroken");
            Logger.getLogger(BankierSessieController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        
    }

    @FXML
    private void logout(ActionEvent event) 
    {
        try 
        {
            sessie.logUit();
            application.gotoLogin(balie, "");
        }
        catch (RemoteException e) 
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void transfer(ActionEvent event) 
    {
        System.out.println("Starting transfer");
        
        try 
        {
            int from = Integer.parseInt(tfAccountNr.getText());
            int to = Integer.parseInt(tfToAccountNr.getText());
            if (from == to) 
            {
                taMessage.setText("can't transfer money to your own account");
            }
            long centen = (long) (Double.parseDouble(tfAmount.getText()) * 100);
            boolean success = sessie.maakOver(to, new Money(centen, Money.EURO));
            taMessage.setText("Transfer success: " + success);
        } 
        catch (RemoteException e1) 
        {
            e1.printStackTrace();
            taMessage.setText("verbinding verbroken");
        } 
        catch (NumberDoesntExistException | InvalidSessionException e1) 
        {
            e1.printStackTrace();
            taMessage.setText(e1.getMessage());
        }
        System.out.println("Transfer complete ");
    }
    
    public void updateSaldo(long cents)
    {
        tfBalance.setText(Money.EURO + cents/100 + "");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException 
    {
        BankierSessieController app = this;
        Platform.runLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                if (evt.getNewValue() == null) 
                {
                    taMessage.setText("Session expired.");
                    try
                    {
                        sessie.removeListener(app, prop);
                    } 
                    catch (RemoteException ex) 
                    {
                        System.out.println("RemoteException: " + ex.getMessage());
                    }
                } 
                else 
                {
                    try 
                    {
                        if (sessie.isGeldig())
                        {
                            IRekening rek = (IRekening) evt.getNewValue();
                            tfBalance.setText(rek.getSaldo() + "");
                        }
                        else
                        {
                            sessie.removeListener(app, prop);
                        }
                    } 
                    catch (RemoteException ex) 
                    {
                        System.out.println("RemoteException: " + ex.getMessage());
                    }
                }
            }
        });
    }
}
