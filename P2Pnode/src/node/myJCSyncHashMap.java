package node;

import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncHashMap;

import java.io.Serializable;

/**
 * Created for test to handle lack of serialization in Arctite
 * User: marek
 * Date: 3/6/13
 * Time: 18:22
 * To change this template use File | Settings | File Templates.
 */
public class MyJCSyncHashMap<K, E> extends JCSyncHashMap<K, E> implements Serializable {

    private JCSyncHashMap<K, E> jcSyncHashMap;

    public MyJCSyncHashMap(JCSyncHashMap jcSyncHashMap) {
        super(jcSyncHashMap);
        //this.jcSyncHashMap = jcSyncHashMap;
    }

    public MyJCSyncHashMap() {
        super();
    }


}
