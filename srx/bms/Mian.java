package bms;

import bms.building.Building;
import bms.building.BuildingInitialiser;
import bms.exceptions.*;
import bms.floor.Floor;
import bms.floor.MaintenanceSchedule;
import bms.hazardevaluation.HazardEvaluator;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;
import bms.util.StudyRoomRecommender;
import com.sun.tools.javac.Main;
import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.*;

public class Mian {
    public static void main(String[] args) throws DuplicateSensorException, InsufficientSpaceException, DuplicateRoomException, FileFormatException, IOException {
//        Room room1 = new Room(1, RoomType.STUDY, 23);
//        Room room2 = new Room(1, RoomType.STUDY, 23);
//        NoiseSensor noiseSensor = new NoiseSensor(new int[]{1,2,3}, 1);
//        CarbonDioxideSensor carbonDioxideSensor = new CarbonDioxideSensor(new int[]{1,2,3}, 1, 1, 1);
//        TemperatureSensor temperatureSensor = new TemperatureSensor(new int[]{1,2,3});
//        room1.addSensor(noiseSensor);
//        room1.addSensor(carbonDioxideSensor);
//        room2.addSensor(carbonDioxideSensor);
//        room2.addSensor(noiseSensor);
//        //System.out.println(room1.equals(room2));
//        room2.addSensor(temperatureSensor);
//        //System.out.println(room1.equals(room2));
//        HazardSensor hazardSensor = new CarbonDioxideSensor(new int[]{1,2,3}, 1, 1, 1);
//        //System.out.println(hazardSensor.getClass() == carbonDioxideSensor.getClass());
//        Map<Integer, Integer> map = new HashMap<>();
//        map.put(40, 20);
//        map.put(65, 30);
//        map.put(0, 50);
//        float level = 0;
//        float weight = 0;
//        for (Map.Entry<Integer, Integer> sensor : map.entrySet()) {
//            level += sensor.getKey() * sensor.getValue();
//            weight += sensor.getValue();
//        }
//
////        System.out.println((int)Math.ceil(level/weight));
////        System.out.println(Math.round(1.5));
////        System.out.println(Math.round(1.4));
//
//        Room rooms1 = new Room(101, RoomType.STUDY, 23);
//        Room rooms2 = new Room(102, RoomType.LABORATORY, 12);
//        Room rooms3 = new Room(103, RoomType.STUDY, 25);
//        Room rooms4 = new Room(104, RoomType.STUDY, 22);
//        Room rooms5 = new Room(105, RoomType.OFFICE, 13);
//
//        //System.out.println(rooms5.hashCode() == rooms5.hashCode());
//        List<Room> rooms = new ArrayList<>();
////        rooms.add(rooms1);
////        rooms.add(rooms2);
//        rooms.add(rooms4);
//        rooms.add(rooms3);
//        rooms.add(rooms5);
//        List<Room> roomsget = new ArrayList<>();
//        roomsget.add(rooms3);
//        roomsget.add(rooms4);
//        roomsget.add(rooms5);
//        //System.out.println(rooms.equals(roomsget));
//
//        Floor floor1 = new Floor(1, 50, 50);
//        Floor floor2 = new Floor(1, 50, 50);
//        floor1.addRoom(rooms1);
//        floor1.addRoom(rooms2);
//        floor1.addRoom(rooms3);
//        floor1.addRoom(rooms4);
//        floor2.addRoom(rooms3);
//        floor2.addRoom(rooms1);
//        floor2.addRoom(rooms2);
//        floor2.addRoom(rooms4);
//        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(floor2.getRooms());
//        floor2.createMaintenanceSchedule(floor2.getRooms());
//        //System.out.println(maintenanceSchedule.encode());
//        System.out.println(floor2.encode());
//        //System.out.println(floor1.equals(floor2));
//        //System.out.println(floor1.hashCode() == floor2.hashCode()); // false
//
//        double k = 0;
//        k = (double)191 / (double)200;
//        //System.out.println((int)((1-k)*100));
//
//        int[] sensorReadings = {1,2,3,4,5,6,7};
//        //System.out.println(String.join(",", Arrays.stream(sensorReadings)
//        //        .mapToObj(String::valueOf)
//        //        .toArray(String[]::new)));
//        System.out.println((int)(8.4));

        List<Building> buildings = BuildingInitialiser.loadBuildings("saves/uqstlucia.txt");
        Room room = StudyRoomRecommender.recommendStudyRoom(buildings.get(0));
//        BufferedWriter file = new BufferedWriter(new FileWriter("saves/uqstlucia.txt"));
//        file.write(12356);
//        file.close();
//        Object o = new int[]{1,2,3,4,5};
//        Object k = new String("nihaoma");
//        System.out.println(k.getClass());

        System.out.println("asda");
    }
}
