package com.martabak.kamar.domain.managers;

import android.content.Context;
import android.util.Log;

import com.martabak.kamar.domain.Room;
import com.martabak.kamar.service.GuestServer;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;

public class RoomManager implements Manager {

    private static RoomManager instance;

    private List<Room> rooms = null;
    private List<Room> roomsWithGuests = null;
    private List<Room> roomsWithoutGuests = null;

    private RoomManager() {}

    public static RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
            Managers.register(instance);
        }
        return instance;
    }

    public void clear() {
        roomsWithGuests = null;
        roomsWithoutGuests = null;
    }

    public Observable<List<Room>> getRooms(Context c) {
        if (rooms == null) {
            Observable<List<Room>> roomsObs = GuestServer.getInstance(c).getRoomNumbers();
            roomsObs.subscribe(new Observer<List<Room>>() {
                @Override public void onCompleted() {
                    Log.d(RoomManager.class.getCanonicalName(), "Fetched all rooms");
                }
                @Override public void onError(Throwable e) {
                    Log.e(RoomManager.class.getCanonicalName(), "Error fetching all rooms", e);
                }
                @Override public void onNext(List<Room> rooms) {
                    RoomManager.this.rooms = rooms;
                }
            });
            return roomsObs;
        }
        return Observable.just(rooms);
    }

    public Observable<Room> getRoomsWithGuests(Context c) {
        if (roomsWithGuests == null) {
            roomsWithGuests = new ArrayList<>();
            Observable<Room> roomsObs = GuestServer.getInstance(c).getRoomNumbersWithoutGuests();
            roomsObs.subscribe(new Observer<Room>() {
                @Override public void onCompleted() {
                    Log.d(RoomManager.class.getCanonicalName(), "Fetched rooms with guests");
                }
                @Override public void onError(Throwable e) {
                    Log.e(RoomManager.class.getCanonicalName(), "Error fetching rooms with guests", e);
                }
                @Override public void onNext(Room room) {
                    roomsWithGuests.add(room);
                }
            });
            return roomsObs;
        }
        return Observable.from(roomsWithGuests);
    }

    public Observable<Room> getRoomsWithoutGuests(Context c) {
        if (roomsWithoutGuests == null) {
            roomsWithoutGuests = new ArrayList<>();
            Observable<Room> roomsObs = GuestServer.getInstance(c).getRoomNumbersWithoutGuests();
            roomsObs.subscribe(new Observer<Room>() {
                @Override public void onCompleted() {
                    Log.d(RoomManager.class.getCanonicalName(), "Fetched rooms without guests");
                }
                @Override public void onError(Throwable e) {
                    Log.e(RoomManager.class.getCanonicalName(), "Error fetching rooms without guests", e);
                }
                @Override public void onNext(Room room) {
                    roomsWithoutGuests.add(room);
                }
            });
            return roomsObs;
        }
        return Observable.from(roomsWithoutGuests);
    }

}
