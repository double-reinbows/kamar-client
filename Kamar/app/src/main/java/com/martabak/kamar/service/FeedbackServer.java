package com.martabak.kamar.service;

/**
 * Exposes {@link FeedbackService}.
 */
public class FeedbackServer extends Server {

    /**
     * The singleton instance.
     */
    private static FeedbackServer instance;

    /**
     * The service api conf.
     */
    private FeedbackService service;

    /**
     * Constructor.
     */
    private FeedbackServer() {
        super(FeedbackService.BASE_URL);
        service = createService(FeedbackService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static FeedbackServer getInstance() {
        if (instance == null)
            instance = new FeedbackServer();
        return instance;
    }

}
