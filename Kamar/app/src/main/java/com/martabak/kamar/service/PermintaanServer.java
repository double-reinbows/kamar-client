package com.martabak.kamar.service;

import android.content.Context;

/**
 * Exposes {@link PermintaanService}.
 */
public class PermintaanServer extends Server {

    /**
     * The singleton instance.
     */
    private static PermintaanServer instance;
    /**
     * The service api conf.
     */
    private PermintaanService service;

    /**
     * Constructor.
     */
    private PermintaanServer(Context c) {
        super(c, PermintaanService.BASE_URL);
        service = createService(PermintaanService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static PermintaanServer getInstance(Context c) {
        if (instance == null)
            instance = new PermintaanServer(c);
        return instance;
    }

}
