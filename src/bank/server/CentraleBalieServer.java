package bank.server;

import bank.bankieren.Bank;
import bank.bankieren.CentraleBank;
import bank.bankieren.ICentraleBank;
import bank.gui.BankierClient;
import bank.internettoegang.Balie;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;

public class CentraleBalieServer extends Application 
{
    private Stage stage;
    private final double MINIMUM_WINDOW_WIDTH = 600.0;
    private final double MINIMUM_WINDOW_HEIGHT = 200.0;
    private String nameBank;
    private ICentraleBank centraleBank;
    private Balie myBalie;

    @Override
    public void start(Stage primaryStage) throws IOException 
    {
        try 
        {
            stage = primaryStage;
            stage.setTitle("Bankieren");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            gotoBankSelect();
            primaryStage.show();
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }
    
    public void addCentraleBank(CentraleBank cb)
    {
        this.centraleBank = cb;
    }

    public boolean startBalie(String nameBank) 
    {
        try
        {
            this.nameBank = nameBank;
            Bank bank = new Bank(nameBank, centraleBank);
            myBalie = new Balie(bank);
            bank.addBalie(myBalie);
            centraleBank.addBank(bank);
            return true;
        }
        catch(RemoteException ex)
        {
            System.out.println("RemoteException: " + ex.getMessage());
            return false;
        }
    }

    public void gotoBankSelect() 
    {
        try 
        {
            BalieControllerCustom bankSelect = (BalieControllerCustom) replaceSceneContent("Balie.fxml");
            bankSelect.setApp(this);
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(BankierClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Initializable replaceSceneContent(String fxml) throws Exception 
    {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = CentraleBalieServer.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(CentraleBalieServer.class.getResource(fxml));
        AnchorPane page;
        try 
        {
            page = (AnchorPane) loader.load(in);
        } 
        finally 
        {
            in.close();
        }
        Scene scene = new Scene(page, 800, 600);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        launch(args);
    }
}
