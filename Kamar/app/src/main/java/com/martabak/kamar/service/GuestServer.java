package com.martabak.kamar.service;

/**
 * Exposes {@link GuestService}.
 */
public class GuestServer extends Server {

    /**
     * The singleton instance.
     */
    private static GuestServer instance;

    /**
     * The service api conf.
     */
    private GuestService service;

    /**
     * Constructor.
     */
    private GuestServer() {
        super(GuestService.BASE_URL);
        service = createService(GuestService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static GuestServer getInstance() {
        if (instance == null)
            instance = new GuestServer();
        return instance;
    }

}
