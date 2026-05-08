package com.travelagency.model;

/**
 * Hotel Room model representing individual rooms
 */
public class HotelRoom extends BaseEntity {
    private int hotelId;
    private String roomNumber;
    private RoomType roomType;
    private int capacity;
    private double pricePerNight;
    private boolean isOccupied;
    private boolean isActive;

    public enum RoomType {
        SINGLE, DOUBLE, SUITE, DELUXE
    }

    public HotelRoom() {
        super();
        this.isActive = true;
        this.isOccupied = false;
    }

    public HotelRoom(int id, int hotelId, String roomNumber, RoomType roomType,
                     int capacity, double pricePerNight) {
        super(id);
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.isActive = true;
        this.isOccupied = false;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "HotelRoom{" +
                "id=" + id +
                ", hotelId=" + hotelId +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomType=" + roomType +
                ", pricePerNight=" + pricePerNight +
                ", isOccupied=" + isOccupied +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelRoom room = (HotelRoom) o;
        return id == room.id && hotelId == room.hotelId && roomNumber.equals(room.roomNumber);
    }

    @Override
    public int hashCode() {
        return id * 31 + roomNumber.hashCode();
    }
}
