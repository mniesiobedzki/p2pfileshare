package node;

import pl.edu.pjwstk.mteam.jcsync.core.implementation.util.JCSyncObservable;

import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: marek
 * Date: 12/5/13
 * Time: 19:32
 * To change this template use File | Settings | File Templates.
 */
public class CollectionListener implements Observer {

    private JCSyncObservable jcs_obs;

    public CollectionListener(JCSyncObservable jcs_obs) {

        this.jcs_obs = jcs_obs;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("UPDATE UPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATEUPDATE");

    }
}
