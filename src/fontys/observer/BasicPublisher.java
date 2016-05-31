package fontys.observer;

import java.util.*;
import java.beans.*;

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
 * @version 1.1
 * TreeSet is vervangen door HashSet; het veld allListeners is verwijderd
 */
/**
 * @author prs
 *
 */
public class BasicPublisher implements Publisher {

  /**
   * de listeners die onder de null-String staan geregistreerd zijn listeners
   * die op alle properties zijn geabonneerd
   */
  private HashMap<String,Set<RemotePropertyListener>> listenersTable;

  public BasicPublisher() {
    listenersTable = new HashMap<String,Set<RemotePropertyListener>>();
  }

  public void addListener(RemotePropertyListener listener, String property) {
    Set<RemotePropertyListener> thisPropertyListeners = listenersTable.get(property);
    if (thisPropertyListeners == null) {
      thisPropertyListeners = new HashSet<RemotePropertyListener>();
      listenersTable.put(property, thisPropertyListeners);
    }
    thisPropertyListeners.add(listener);
  }

  public void removeListener(RemotePropertyListener listener, String property) {
    Set<RemotePropertyListener> thisPropertyListeners = listenersTable.get(property);
    if (thisPropertyListeners != null) thisPropertyListeners.remove(listener);

    if (property == null) {
      Set<String> keyset = listenersTable.keySet();
      for (String key : keyset) {
        listenersTable.get(key).remove(listener);
      }
    }
  }

  public void removeAllListeners() {
    listenersTable.clear();
  }

  /**
   * alle listeners voor property en de listeners met een algemeen abonnement krijgen
   * een aanroep van propertyChange
   * @param source de publisher 
   * @param property de eigenschap van de publisher
   * @param oldValue oorspronkelijke waarde van de property van de publisher
   * @param newValue nieuwe waarde van de property van de publisher
   */
  public void inform(Object source, String property, Object oldValue, Object newValue) {
    Set<RemotePropertyListener> alertable;
    alertable = listenersTable.get(property);
    if (alertable==null) {
      alertable = new HashSet<RemotePropertyListener>();
    }
    if (property!=null) {
          Set<RemotePropertyListener> generalListeners = listenersTable.get(null);
      if (generalListeners!=null) alertable.addAll(generalListeners);
    }

    for (RemotePropertyListener listener : alertable) {
      try {
        listener.propertyChange(new PropertyChangeEvent(source, property, oldValue, newValue));
      }
      catch (java.rmi.ConnectException exc) {
        removeListener(listener,property);
        exc.printStackTrace();
      }
      catch (java.rmi.NoSuchObjectException exc) {
        removeListener(listener,property);
        exc.printStackTrace();
      }
      catch (java.rmi.RemoteException exc) {
        exc.printStackTrace();
      }
    }
  }
}