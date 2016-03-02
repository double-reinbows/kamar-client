package com.martabak.kamar.service;

/**
 * Exposes {@link ChatService}.
 */
public class ChatServer extends Server {

    /**
     * The singleton instance.
     */
    private static ChatServer instance;
    /**
     * The service api conf.
     */
    private ChatService service;

    /**
     * Constructor.
     */
    private ChatServer() {
        super(ChatService.BASE_URL);
        service = createService(ChatService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static ChatServer getInstance() {
        if (instance == null)
            instance = new ChatServer();
        return instance;
    }

}
