
package fontys.observer;

import java.util.*;
import java.rmi.*;
import java.beans.*;

/**
 *
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
public interface RemotePropertyListener
    extends EventListener, Remote {

    /**
     * op basis van de informatie die evt overdraagt kan deze listener er voor
     * zorgen dat de bijbehorende observer gesynchroniseerd wordt
     * @param evt PropertyChangeEvent @see java.beans.PropertyChangeEvent
     * @throws RemoteException
     */
    void propertyChange(PropertyChangeEvent evt) throws RemoteException;
}