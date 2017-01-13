package com.martabak.kamar.activity.chat;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.martabak.kamar.R;
import com.martabak.kamar.activity.YiannisTestActivity;
import com.martabak.kamar.activity.staff.StaffHomeActivity;
import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.chat.ChatMessage;
import com.martabak.kamar.domain.chat.GuestChat;
import com.martabak.kamar.domain.Room;
import com.martabak.kamar.service.ChatServer;
import com.martabak.kamar.service.GuestServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * A chat service that routinely checks for new incoming chat messages for staff.
 */
public class StaffChatService extends IntentService {

    private static final int POLL_EVERY_SECONDS_AMOUNT = 20;

    private static final Class RESULT_ACTIVITY = StaffHomeActivity.class;

    /**
     * Construct a staff chat service.
     */
    public StaffChatService() {
        super("StaffChatService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (true) {
            Log.d(StaffChatService.class.getCanonicalName(), "Checking for new chats to staff");

            // Get list of room numbers.
            GuestServer.getInstance(this).getRoomNumbers().subscribe(new Action1<List<Room>>() {
                @Override
                public void call(List<Room> roomsResult) {
                    Log.d(StaffChatService.class.getCanonicalName(), "Got rooms");

                    Observable.from(roomsResult)
                            // Convert to list of checked in guests.
                            .flatMap(new Func1<Room, Observable<Guest>>() {
                                @Override public Observable<Guest> call(Room room) {
                                    return GuestServer.getInstance(StaffChatService.this).getGuestInRoom(room.number);
                                }
                            })
                            // Filter out any empty rooms.
                            .filter(new Func1<Guest, Boolean>() {
                                @Override public Boolean call(Guest guest) {
                                    return guest != null;
                                }
                            })
                            // Convert to list of room chats.
                            .flatMap(new Func1<Guest, Observable<GuestChat>>() {
                                @Override public Observable<GuestChat> call(Guest guest) {
                                    return ChatServer.getInstance(StaffChatService.this).getGuestChat(guest._id);
                                }
                            })
                            // Add to list of unread messages from guests.
                            .flatMap(new Func1<GuestChat, Observable<ChatMessage>>() {
                                @Override public Observable<ChatMessage> call(GuestChat guestChat) {
                                    List<ChatMessage> unreadMessages = new ArrayList();
                                    for (ChatMessage m : guestChat.messages) {
                                        if (m.from.equals("GUEST") && m.read == null) {
                                            // Return only if an unread message from GUEST exists.
                                            unreadMessages.add(m);
                                        }
                                    }
                                    return Observable.from(unreadMessages);
                                }
                            })
                            .subscribe(new Action1<ChatMessage>() {
                                @Override public void call(ChatMessage unreadMessage) {
                                    Log.d(GuestChatService.class.getCanonicalName(), "New chat message to staff from " + unreadMessage.guestId);
                                    createNotification(0, unreadMessage); // FIXME use proper int ID here
                                }
                            });
                }
            });

            try {
                Log.d(StaffChatService.class.getCanonicalName(), "Going to sleep for " + POLL_EVERY_SECONDS_AMOUNT + " seconds");
                Thread.sleep(POLL_EVERY_SECONDS_AMOUNT * 1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void createNotification(final int nId, final ChatMessage message) {
        GuestServer.getInstance(this).getGuest(message.guestId)
                .subscribe(new rx.Observer<Guest>() {
                    String roomNumber = "";
                    @Override public void onCompleted() {
                        createNotification(nId, message, roomNumber);
                    }
                    @Override public void onError(Throwable e) {
                        createNotification(nId, message, roomNumber);
                    }
                    @Override public void onNext(Guest guest) {
                        roomNumber = guest.roomNumber;
                    }
                });
    }

    private void createNotification(int nId, ChatMessage message, String roomNumber) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_share)
                .setContentTitle(getString(R.string.chat_message_from_guest) + " " + roomNumber)
                .setContentText(message.message);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, RESULT_ACTIVITY);
        //go to staff chat fragment
        resultIntent.putExtra("FragType","StaffChatFragment");

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
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // nId allows you to update the notification later on.
        mNotificationManager.notify("chat", nId, mBuilder.build());
    }
}
