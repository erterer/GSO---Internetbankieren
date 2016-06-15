package bank.centrale;

import bank.server.*;
import bank.bankieren.CentraleBank;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import static javafx.application.Application.launch;

public class CentraleBankServer extends Application 
{
    private CentraleBank centraleBank;
    private CentraleBalieServer balieServer;
    private CentraleBalieServer balieServer2;
    private Stage stage;
    private final double MINIMUM_WINDOW_WIDTH = 600.0;
    private final double MINIMUM_WINDOW_HEIGHT = 200.0;
    private String nameCentraleBank;

    @Override
    public void start(Stage primaryStage) throws IOException 
    {
        centraleBank = new CentraleBank();
        SetupRMI();
        
        balieServer = new CentraleBalieServer();
        balieServer.addCentraleBank(centraleBank);
        balieServer.start(new Stage());
        
        balieServer2 = new CentraleBalieServer();
        balieServer2.addCentraleBank(centraleBank);
        balieServer2.start(new Stage());
    }
    
    /**
     * Instellen van RMI
     */
    private void SetupRMI()
    {
        FileOutputStream out = null;
        try 
        {
            String address = java.net.InetAddress.getLocalHost().getHostAddress();
            int port = 1099;
            Properties props = new Properties();
            nameCentraleBank = "cb";
            String rmiCentraleBank = address + ":" + port + "/" + nameCentraleBank;
            props.setProperty("cb", rmiCentraleBank);
            out = new FileOutputStream(nameCentraleBank + ".props");
            props.store(out, null);
            out.close();
            java.rmi.registry.LocateRegistry.createRegistry(port);

            Naming.rebind(nameCentraleBank, centraleBank);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(BalieServer.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally 
        {
            try 
            {
                out.close();
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(BalieServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        launch(args);
    }
}
