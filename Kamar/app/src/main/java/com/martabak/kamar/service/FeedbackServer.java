package com.martabak.kamar.service;

import android.content.Context;

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
    private FeedbackServer(Context c) {
        super(c, FeedbackService.BASE_URL);
        service = createService(FeedbackService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static FeedbackServer getInstance(Context c) {
        if (instance == null)
            instance = new FeedbackServer(c);
        return instance;
    }

}
