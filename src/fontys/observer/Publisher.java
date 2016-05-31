package fontys.observer;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Fontys Hogeschool Informatica</p>
 *
 * @author Frank Peeters
 * @version 1.0
 */
public interface Publisher {

  /**
   * listener abonneert zich op PropertyChangeEvent's zodra property is gewijzigd
   * @param listener PropertyListener
   * @param property mag null zijn, dan abonneert listener zich op alle
   * properties
   */
  void addListener(RemotePropertyListener listener, String property);

  /**
   * het abonnement van listener voor PropertyChangeEvent's mbt property wordt
   * opgezegd
   * @param listener PropertyListener
   * @param property mag null zijn, dan worden alle abonnementen van listener
   * opgezegd
   */
  void removeListener(RemotePropertyListener listener, String property);


  void removeAllListeners();
}