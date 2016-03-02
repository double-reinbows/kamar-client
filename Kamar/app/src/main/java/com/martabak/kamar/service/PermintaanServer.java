package com.martabak.kamar.service;

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
    private PermintaanServer() {
        super(PermintaanService.BASE_URL);
        service = createService(PermintaanService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static PermintaanServer getInstance() {
        if (instance == null)
            instance = new PermintaanServer();
        return instance;
    }

}
