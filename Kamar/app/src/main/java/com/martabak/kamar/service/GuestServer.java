package com.martabak.kamar.service;

import android.content.Context;

import com.martabak.kamar.domain.Guest;
import com.martabak.kamar.domain.Room;
import com.martabak.kamar.service.response.AllResponse;
import com.martabak.kamar.service.response.PostResponse;
import com.martabak.kamar.service.response.ViewResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
    private GuestServer(Context c) {
        super(c);
        service = createService(GuestService.class);
    }

    /**
     * Obtains singleton instance.
     * @return The singleton instance.
     */
    public static GuestServer getInstance(Context c) {
        if (instance == null)
            instance = new GuestServer(c);
        return instance;
    }

    /**
     * Create a new guest.
     * @param guest The guest model to be created.
     * @return The guest model that was added.
     */
    public Observable<Guest> createGuest(Guest guest) {
        return service.createGuest(guest)
                .flatMap(new Func1<PostResponse, Observable<Guest>>() {
                    @Override public Observable<Guest> call(PostResponse response) {
                        return service.getGuest(response.id);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @return All the guests currently checked in.
     */
    public Observable<Guest> getGuestsCheckedIn() {
        return service.getGuestsCheckedIn()
                .flatMap(new Func1<ViewResponse<Guest>, Observable<Guest>>() {
                    @Override public Observable<Guest> call(ViewResponse<Guest> response) {
                        for (ViewResponse<Guest>.ViewResult<Guest> i : response.rows) {
                            return Observable.just(i.value);
                        }
                        return Observable.just(null);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Get the guest currently in the given room, if there is one.
     * @param roomNumber The room number.
     * @return The guest in the room, if there is one.
     */
    public Observable<Guest> getGuestInRoom(String roomNumber) {
        return service.getGuestInRoom('"' + roomNumber + '"')
                .flatMap(new Func1<ViewResponse<Guest>, Observable<Guest>>() {
                    @Override public Observable<Guest> call(ViewResponse<Guest> response) {
                        for (ViewResponse<Guest>.ViewResult<Guest> i : response.rows) {
                            return Observable.just(i.value);
                        }
                        return Observable.just(null);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @return All room numbers of the hotel.
     */
    public Observable<List<Room>> getRoomNumbers() {
        return service.getRooms()
                .flatMap(new Func1<AllResponse<Room>, Observable<List<Room>>>() {
                    @Override public Observable<List<Room>> call(AllResponse<Room> response) {
                        List<Room> toReturn = new ArrayList<>(response.total_rows);
                        for (AllResponse<Room>.AllResult<Room> i : response.rows) {
                            toReturn.add(i.doc);
                        }
                        return Observable.just(toReturn);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @return All room numbers of the hotel that have a guest currently checked in.
     */
    public Observable<List<Room>> getRoomNumbersWithGuests() {
        final List<Room> roomsWithGuests = new ArrayList<>();
        service.getRooms()
            .flatMap(new Func1<AllResponse<Room>, Observable<Room>>() {
                @Override public Observable<Room> call(AllResponse<Room> response) {
                    List<Room> toReturn = new ArrayList<>(response.total_rows);
                    for (AllResponse<Room>.AllResult<Room> i : response.rows) {
                        toReturn.add(i.doc);
                    }
                    return Observable.from(toReturn);
                }
            })
            // Only return rooms with a guest.
            .subscribe(new Observer<Room>() {
                @Override public void onCompleted() {}
                @Override public void onNext(final Room room) {
                     getGuestInRoom(room.number)
                        .subscribe(new Action1<Guest>() {
                            @Override public void call(Guest guest) {
                                if (guest != null) roomsWithGuests.add(room);
                            }
                        });
                }
                @Override public void onError(Throwable e) {
                    e.printStackTrace();
                }
            });
        return Observable.just(roomsWithGuests);
    }

    /**
     * @return All room numbers of the hotel that <i>do not</i> have a guest currently checked in.
     */
    public Observable<List<Room>> getRoomNumbersWithoutGuests() {
        final List<Room> roomsWithoutGuests = new ArrayList<>();
        service.getRooms()
                .flatMap(new Func1<AllResponse<Room>, Observable<Room>>() {
                    @Override public Observable<Room> call(AllResponse<Room> response) {
                        List<Room> toReturn = new ArrayList<>(response.total_rows);
                        for (AllResponse<Room>.AllResult<Room> i : response.rows) {
                            toReturn.add(i.doc);
                        }
                        return Observable.from(toReturn);
                    }
                })
                // Only return rooms without a guest.
                .subscribe(new Observer<Room>() {
                    @Override public void onCompleted() {}
                    @Override public void onNext(final Room room) {
                        getGuestInRoom(room.number)
                                .subscribe(new Action1<Guest>() {
                                    @Override public void call(Guest guest) {
                                        if (guest == null) roomsWithoutGuests.add(room);
                                    }
                                });
                    }
                    @Override public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        return Observable.just(roomsWithoutGuests);
    }

}
