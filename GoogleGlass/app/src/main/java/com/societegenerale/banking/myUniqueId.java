package com.societegenerale.banking;

/**
 * Created by romeo on 04/10/2014.
 */
public class myUniqueId {

    public int UniqueId;

    private myUniqueId()
    {
    }

    private static myUniqueId Instance = null;

    public static synchronized myUniqueId GetInstance()
    {
        if (Instance == null)
            Instance = new myUniqueId();
        return Instance;
    }

}
