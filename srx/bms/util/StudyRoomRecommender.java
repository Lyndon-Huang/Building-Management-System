package bms.util;

import bms.building.Building;
import bms.floor.Floor;
import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.sensors.*;

public class StudyRoomRecommender {
    /**
     * Returns a room in the given building that is
     * most suitable for study purposes.
     * <p>
     * Any given room's suitability for study is based
     * on several criteria, including:
     * the room's type - it must be a study room (see RoomType)
     * the room's status - it must be open, not being evacuated or
     * in maintenance (see Room.evaluateRoomState())
     * the room's comfort level based on its available sensors
     * (see ComfortSensor.getComfortLevel())
     * which floor the room is on - rooms on lower floors are better
     * Since travelling up the floors of a building often requires walking
     * up stairs, the process for choosing a study room begins by looking
     * for rooms on the first floor, and only considers higher floors if
     * doing so would improve the comfort level of the room chosen.
     * Similarly, once on a floor, walking back down more than one floor
     * to a previously considered study room is also not optimal.
     * If there are no rooms on the first floor of a building that meet
     * the basic criteria, then the algorithm should recommend that the
     * building be avoided entirely, even if there are suitable
     * rooms on higher floors.
     * <p>
     * Based on these requirements, the algorithm for determining
     * the most suitable study room is as follows:
     * <p>
     * 1. If there are no rooms in the building, return null.
     * 2. Consider only rooms on the first floor.
     * 3. Eliminate any rooms that are not study rooms or are not open.
     * If there are no remaining candidate rooms, return the room with
     * the highest comfort level on the previous floor,
     * or null if there is no previous floor.
     * 4. Calculate the comfort level of all remaining rooms on this
     * floor, using the average of the comfort levels of each room's
     * available comfort sensors. If a room has no comfort sensors,
     * its comfort level should be treated as 0.
     * 5. Keep a reference to the room with the highest comfort level
     * on this floor based on the calculation in the previous step.
     * 6. If there is a tie between two or more rooms, any of these
     * may be chosen.If the highest comfort level of any room on this floor is
     * less than or equal to the highest comfort level of any room
     * on the previous floor, return the room on the previous floor
     * with the highest comfort level.
     * 7. If this is the top floor of the building, return the room
     * found in step 5. Otherwise, repeat steps 2-7 for the next floor up.
     *
     * @param building building in which to search for a study room
     * @return the most suitable study room in the building;
     * null if there are none
     */
    public static Room recommendStudyRoom(Building building) {
        Room recommendRoom = null;
        double comfortLevel = -1;
        if (building.getFloors().isEmpty()) {
            return null;
        }
        for (int i = 0; i < building.getFloors().size(); i++) {
            if (building.getFloorByNumber(i + 1)
                    .getRooms().isEmpty()) {
                break;
            }
            for (Room room : building.getFloorByNumber(i + 1)
                    .getRooms()) {
                if (room.getType() == RoomType.STUDY &&
                        room.evaluateRoomState() == RoomState.OPEN) {
                    int currentComfortLevel = 0;
                    if (!room.getSensors().isEmpty()) {
                        for (Sensor sensor : room.getSensors()) {
                            if (sensor.getClass() ==
                                    CarbonDioxideSensor.class) {
                                currentComfortLevel +=
                                        ((CarbonDioxideSensor) sensor)
                                                .getComfortLevel();
                            } else if (sensor.getClass() ==
                                    NoiseSensor.class) {
                                currentComfortLevel += ((NoiseSensor) sensor)
                                        .getComfortLevel();
                            } else if (sensor.getClass() ==
                                    OccupancySensor.class) {
                                currentComfortLevel +=
                                        ((OccupancySensor) sensor)
                                                .getComfortLevel();
                            } else if (sensor.getClass() ==
                                    TemperatureSensor.class) {
                                currentComfortLevel +=
                                        ((TemperatureSensor) sensor)
                                                .getComfortLevel();
                            }
                        }
                    }
                    if (!room.getSensors().isEmpty()) {
                        currentComfortLevel /= room.getSensors().size();
                    }
                    if (currentComfortLevel > comfortLevel) {
                        comfortLevel = currentComfortLevel;
                        recommendRoom = room;
                    }
                }
            }
        }
        return recommendRoom;
    }
}
