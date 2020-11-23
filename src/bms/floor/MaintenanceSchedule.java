package bms.floor;

import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.util.Encodable;
import bms.util.TimedItem;

import java.util.List;

public class MaintenanceSchedule implements TimedItem, Encodable {
    /**
     * list to hold the Room which in maintenance schedule
     */
    private List<Room> roomOrder;
    /**
     * the number of minutes that have elapsed
     */
    private int timeElapsed;
    /**
     * the index of the roomOrder
     */
    private int currentIndex = 0;
    /**
     * he room which is currently in the process of being maintained.
     */
    private Room currentMaintenanceRoom;

    /**
     * Creates a new maintenance schedule for a floor's list of rooms.
     * <p>
     * In this constructor, the new maintenance schedule should be
     * registered as a timed item with the timed item manager.
     *
     * @param roomOrder list of rooms on which to perform maintenance,
     *                  in order
     */
    public MaintenanceSchedule(List<Room> roomOrder) {
        if (roomOrder != null && roomOrder.size() > 0) {
            this.roomOrder = roomOrder;
            this.roomOrder.get(this.currentIndex).setMaintenance(true);
            this.timeElapsed = 0;
        }
    }

    /**
     * Progresses the maintenance schedule by one minute.
     * If the room currently being maintained has a room state of
     * EVACUATE, then no action should occur.
     * <p>
     * If enough time has elapsed such that the room currently being
     * maintained has completed its maintenance
     * (according to getMaintenanceTime(Room)), then:
     * <p>
     * the current room should have its maintenance status set to false
     * ( see Room.setMaintenance(boolean))
     * the next room in the list passed to the constructor should be set as
     * the new current room. If the end of the list has been reached, the
     * new current room should "wrap around" to the first room in the list.
     * the new current room should have its maintenance status set to true
     */
    @Override
    public void elapseOneMinute() {
        this.timeElapsed++;
        Room room = getCurrentRoom();
        if (room.evaluateRoomState() != RoomState.EVACUATE) {
            if (this.timeElapsed >= getMaintenanceTime(room)) {
                room.setMaintenance(false);
                this.timeElapsed = this.timeElapsed - getMaintenanceTime(room);
                this.currentIndex = (this.currentIndex + 1) % roomOrder.size();
                this.roomOrder.get(this.currentIndex).setMaintenance(true);
            }
        }
    }

    /**
     * Returns the machine-readable string representation
     * of this maintenance schedule.
     * The format of the string to return is:
     * roomNumber1,roomNumber2,...,roomNumberN
     * <p>
     * where 'roomNumberX' is the room number of the Xth room in this
     * maintenance schedule's room order, from 1 to N where N is the
     * number of rooms in the maintenance order.
     * There should be no newline at the end of the string.
     * <p>
     * See the demo save file for an example (uqstlucia.txt).
     *
     * @return encoded string representation of this maintenance schedule
     */
    @Override
    public String encode() {
        return String.join(",", this.roomOrder.stream()
                .mapToInt(Room::getRoomNumber)
                .mapToObj(String::valueOf)
                .toArray(String[]::new));
    }

    /**
     * Returns the room which is currently in the process of being maintained.
     *
     * @return room currently in maintenance
     */
    public Room getCurrentRoom() {
        for (Room room : roomOrder) {
            if (room.maintenanceOngoing()) {
                this.currentMaintenanceRoom = room;
                break;
            }
        }
        return this.currentMaintenanceRoom;
    }

    /**
     * Returns the time taken to perform maintenance
     * on the given room, in minutes.
     * The maintenance time for a given room depends on its size
     * (larger rooms take longer to maintain) and its room type
     * (rooms with more furniture and equipment take take longer to maintain).
     * <p>
     * The formula for maintenance time is calculated as the room's
     * base maintenance time multiplied by its room type multiplier,
     * and finally rounded to the nearest integer. Floating point operations
     * should be used during all steps of the calculation, until the final
     * rounding to integer.
     * <p>
     * Rooms with an area of Room.getMinArea() have a base maintenance
     * time of 5.0 minutes.
     * <p>
     * Rooms with areas greater than Room.getMinArea() have a base
     * maintenance time of 5.0 minutes, plus 0.2 minutes for every
     * square metre the room's area is over Room.getMinArea().
     * <p>
     * A room's room type multiplier is given in the table below.
     * <p>
     * Room type multiplier table
     * <t>Room Type</t><t>Room Type Multiplier</t>
     * <t>STUDY</t><t>1</t>
     * <t>OFFICE</t><t>1.5</t>
     * <t>LABORATORY</t><t>2</t>
     *
     * @param room room on which to perform maintenance
     * @return room's maintenance time in minutes
     */
    public int getMaintenanceTime(Room room) {
        double time = 0;
        if (room.getArea() > Room.getMinArea()) {
            if (room.getType() == RoomType.STUDY) {
                time = ((room.getArea() - Room.getMinArea())
                        * 0.2 + 5.0) * 1;
            } else if (room.getType() == RoomType.OFFICE) {
                time = ((room.getArea() - Room.getMinArea())
                        * 0.2 + 5.0) * 1.5;
            } else if (room.getType() == RoomType.LABORATORY) {
                time = ((room.getArea() - Room.getMinArea())
                        * 0.2 + 5.0) * 2;
            }
        } else {
            time = 5.0;
        }
        return (int) (Math.round(time));
    }

    /**
     * Returns the number of minutes that have elapsed while maintaining
     * the current room (getCurrentRoom()).
     *
     * @return time elapsed maintaining current room
     */
    public int getTimeElapsedCurrentRoom() {
        return this.timeElapsed;
    }

    /**
     * Stops the in-progress maintenance of the current room
     * and progresses to the next room.
     * <p>
     * The same steps should be undertaken as described in
     * the dot point list in elapseOneMinute().
     */
    public void skipCurrentMaintenance() {
        this.roomOrder.get(this.currentIndex).setMaintenance(false);
        this.timeElapsed = 0;
        this.currentIndex = (this.currentIndex + 1) % roomOrder.size();
        this.roomOrder.get(this.currentIndex).setMaintenance(true);
    }

    /**
     * Returns the human-readable string representation
     * of this maintenance schedule.
     * <p>
     * The format of the string to return is
     * MaintenanceSchedule: currentRoom=#currentRoomNumber,
     * currentElapsed=elapsed
     * <p>
     * where 'currentRoomNumber' is the room number of the room
     * currently being maintained, and 'elapsed' is the number of
     * minutes that have elapsed while maintaining the current room.
     *
     * @return string representation of this maintenance schedule
     */
    @Override
    public String toString() {
        return String.format("MaintenanceSchedule: currentRoom=#%d," +
                        " currentElapsed=%d",
                this.currentMaintenanceRoom.getRoomNumber(),
                this.timeElapsed);
    }
}
