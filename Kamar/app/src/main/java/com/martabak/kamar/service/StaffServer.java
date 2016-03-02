package com.martabak.kamar.service;

/**
 * Exposes {@link StaffService}.
 */
public class StaffServer extends Server {

    /**
     * The singleton instance.
     */
    private static StaffServer instance;

    /**
     * The service api conf.
     */
    private StaffService service;

    /**
     * Constructor.
     */
    private StaffServer() {
        super(GuestService.BASE_URL);
        service = createService(StaffService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static StaffServer getInstance() {
        if (instance == null)
            instance = new StaffServer();
        return instance;
    }

}
