package com.martabak.kamar.activity.chat;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.YiannisTestActivity;
import com.martabak.kamar.domain.chat.ChatMessage;
import com.martabak.kamar.domain.chat.GuestChat;
import com.martabak.kamar.service.ChatServer;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * A chat service that routinely checks for new incoming chat messages for a particular guest.
 * <p>
 *     Expects intent extra: "guestId" mapping to the guest's ID.
 * </p>
 */
public class GuestChatService extends IntentService {

    private static final int POLL_EVERY_SECONDS_AMOUNT = 60;

    private static final Class RESULT_ACTIVITY = GuestChatActivity.class;

    /**
     * Construct a guest chat service.
     */
    public GuestChatService() {
        super("GuestChatService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String guestId = intent.getExtras().getString("guestId");

        while (true) {
            Log.d(GuestChatService.class.getCanonicalName(), "Checking for new chats to guest " + guestId);
            // Get the guest's full chat.
            ChatServer.getInstance(this).getGuestChat(guestId)
                    // Convert it into a list of any unread messages.
                    .flatMap(new Func1<GuestChat, Observable<ChatMessage>>() {
                        @Override public Observable<ChatMessage> call(GuestChat guestChat) {
                            List<ChatMessage> unreadMessages = new ArrayList();
                            for (ChatMessage m : guestChat.messages) {
                                if (!m.from.equals("GUEST") && m.read == null) {
                                    // Return only if an unread message from RESTAURANT or FRONTDESK exists.
                                    unreadMessages.add(m);
                                }
                            }
                            return Observable.from(unreadMessages);
                        }
                    }).subscribe(new Action1<ChatMessage>() {
                        @Override public void call(ChatMessage unreadMessage) {
                            Log.d(GuestChatService.class.getCanonicalName(), "On call");
                            createNotification(0, unreadMessage); // FIXME use proper int ID here
                        }
                    });

            try {
                Log.d(GuestChatService.class.getCanonicalName(), "Going to sleep for " + POLL_EVERY_SECONDS_AMOUNT + " seconds");
                Thread.sleep(POLL_EVERY_SECONDS_AMOUNT * 1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void createNotification(int nId, ChatMessage message) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_share)
                        .setContentTitle("New message from staff " + message.from)
                        .setContentText(message.message);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, RESULT_ACTIVITY);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(RESULT_ACTIVITY);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // nId allows you to update the notification later on.
        mNotificationManager.notify("chat", nId, mBuilder.build());
    }
}
