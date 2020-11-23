package bms.building;

import bms.exceptions.FileFormatException;
import bms.floor.Floor;
import bms.hazardevaluation.HazardEvaluator;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import bms.hazardevaluation.WeightingBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildingInitialiser {
    /**
     * Create the list to contain building
     */
    private static final List<Building> buildings = new ArrayList<>();

    /**
     * create the fileReader to load the file
     */
    private static FileReader fileReader;

    /**
     * Loads a list of buildings from a save file with the given filename.
     * <p>
     * Save files have the following structure.
     * Square brackets indicate that the data inside them is optional.
     * See the demo save file for an example (uqstlucia.txt).
     *
     * @param filename - path of the file from which to
     *                 load a list of buildings
     * @return a list containing all the buildings loaded from the file
     * @throws IOException         if an IOException is encountered
     *                             when calling any IO methods
     * @throws FileFormatException if the file format of the given file
     *                             is invalid according to the rules above
     */
    public static List<Building> loadBuildings(String filename)
            throws IOException, FileFormatException {
        try {
            fileReader = new FileReader(filename);
        } catch (FileNotFoundException notFoundException) {
            notFoundException.printStackTrace();
        }
        // ***********************************************
        File file = new File(filename);
        InputStream inputStream = new FileInputStream(filename);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        FileReader fileReader = new FileReader(filename);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        // ***********************************************
        BufferedReader bufferedReaderFile = new BufferedReader(fileReader);
        String readLine = bufferedReaderFile.readLine();
        Building building = null;
        int floorCount = 0;
        while (readLine != null) {
            String[] lineHold;
            // read the building name
            if (!readLine.contains(":")) {
                building = new Building(readLine);
            }
            readLine = bufferedReaderFile.readLine();
            // read the floor number
            if (!readLine.contains(":")) {
                try {
                    floorCount = Integer.parseInt(readLine);
                } catch (Exception e) {
                    throw new FileFormatException("cant parse to int");
                }
            }

            // load floor
            for (int i = 0; i < floorCount; i++) {
                int floorNumber;
                double floorWidth;
                double floorLength;
                int roomCount;
                List<Room> roomsOrder = new ArrayList<>();
                String[] roomOrderString = null;
                readLine = bufferedReaderFile.readLine();
                lineHold = readLine.split(":");
                try {
                    floorNumber = Integer.parseInt(lineHold[0]);
                    floorWidth = Double.parseDouble(lineHold[1]);
                    floorLength = Double.parseDouble(lineHold[2]);
                    roomCount = Integer.parseInt(lineHold[3]);
                    if (lineHold.length == 5) {
                        roomOrderString = lineHold[4].split(",");
                    }
                } catch (Exception e) {
                    throw new FileFormatException("floorNumber, floorWidth," +
                            " floorLength, roomNum cant parse to int");
                }
                if (floorLength < Floor.getMinLength() ||
                        floorWidth < Floor.getMinLength()) {
                    throw new FileFormatException("floor length < " +
                            "min length or floor width < min width");
                }
                Floor floor = new Floor(floorNumber, floorWidth, floorLength);
                assert building != null;
                try {
                    building.addFloor(floor);
                } catch (Exception e) {
                    throw new FileFormatException("Cant add floor");
                }
                //load room
                for (int j = 0; j < roomCount; j++) {
                    int roomNumber;
                    RoomType roomType;
                    double roomArea;
                    int roomSensorNum;
                    String evaluator = null;
                    int sumWeighting = 0;
                    List<HazardSensor> ruleSensors = new ArrayList<>();
                    Map<HazardSensor, Integer> weightingSensors =
                            new HashMap<>();
                    readLine = bufferedReaderFile.readLine();
                    lineHold = readLine.split(":");
                    try {
                        roomNumber = Integer.parseInt(lineHold[0]);
                        if (lineHold[1].equals("STUDY")) {
                            roomType = RoomType.STUDY;
                        } else if (lineHold[1].equals("OFFICE")) {
                            roomType = RoomType.OFFICE;
                        } else if (lineHold[1].equals("LABORATORY")) {
                            roomType = RoomType.LABORATORY;
                        } else {
                            throw new FileFormatException(
                                    "invalid room type");
                        }
                        roomArea = Double.parseDouble(lineHold[2]);
                        roomSensorNum = Integer.parseInt(lineHold[3]);
                        if (lineHold.length == 5) {
                            evaluator = lineHold[4];
                        }
                    } catch (Exception e) {
                        throw new FileFormatException("roomNumber, " +
                                "roomArea, roomSensorNum cant parse to int");
                    }
                    if (roomArea < Room.getMinArea() || roomNumber <= 0) {
                        throw new FileFormatException("room area < " +
                                "min area or room number error");
                    }
                    Room room = new Room(roomNumber, roomType, roomArea);
                    try {
                        floor.addRoom(room);
                    } catch (Exception e) {
                        throw new FileFormatException("cant add room");
                    }

                    //load sensor
                    for (int k = 0; k < roomSensorNum; k++) {
                        String sensorName;
                        int frequency = 0;
                        int idealValue;
                        int varLimit = 0;
                        int capacity = 0;
                        Integer weighting = null;
                        readLine = bufferedReaderFile.readLine();
                        lineHold = readLine.split(":");
                        sensorName = lineHold[0];
                        int[] sensorReadings = new int[lineHold[1].
                                split(",").length];
                        if (sensorName.equals("CarbonDioxideSensor")) {
                            try {
                                for (int s = 0; s < lineHold[1].
                                        split(",").length; s++) {
                                    sensorReadings[s] = Integer.parseInt
                                            (lineHold[1].split(",")[s]);
                                }
                                frequency = Integer.parseInt(lineHold[2]);
                                idealValue = Integer.parseInt(lineHold[3]);
                                if (evaluator != null && evaluator.
                                        equals("WeightingBased")) {
                                    varLimit = Integer.parseInt
                                            (lineHold[lineHold.length - 1]
                                                    .split("@")[0]);
                                    weighting = Integer.parseInt
                                            (lineHold[lineHold.length - 1]
                                                    .split("@")[1]);
                                    sumWeighting += weighting;
                                } else {
                                    varLimit = Integer.parseInt(lineHold[4]);
                                }
                            } catch (Exception e) {
                                throw new FileFormatException(
                                        "CarbonDioxideSensor's " +
                                                "variables are invalid");
                            }
                            try {
                                CarbonDioxideSensor carbonDioxideSensor =
                                        new CarbonDioxideSensor(
                                                sensorReadings,
                                                frequency,
                                                idealValue,
                                                varLimit);
                                if (evaluator != null && evaluator.equals(
                                        "WeightingBased")) {
                                    weightingSensors.put(carbonDioxideSensor,
                                            weighting);
                                } else if (evaluator != null && evaluator
                                        .equals("RuleBased")) {
                                    ruleSensors.add(carbonDioxideSensor);
                                }
                                room.addSensor(carbonDioxideSensor);
                            } catch (Exception e) {
                                throw new FileFormatException(
                                        "create CO2 error or " +
                                                "cant add CO2 sensor");
                            }
                        } else if (sensorName.equals("NoiseSensor")) {
                            try {
                                for (int s = 0; s < lineHold[1].split(",")
                                        .length; s++) {
                                    sensorReadings[s] = Integer.parseInt
                                            (lineHold[1].split(",")[s]);
                                }
                                if (evaluator != null && evaluator
                                        .equals("WeightingBased")) {
                                    frequency = Integer.parseInt
                                            (lineHold[lineHold.length - 1]
                                                    .split("@")[0]);
                                    weighting = Integer.parseInt
                                            (lineHold[lineHold.length - 1]
                                                    .split("@")[1]);
                                    sumWeighting += weighting;
                                } else {
                                    frequency = Integer.parseInt(lineHold[2]);
                                }
                            } catch (Exception e) {
                                throw new FileFormatException(
                                        "NoiseSensor's variables are invalid");
                            }
                            try {
                                NoiseSensor noiseSensor =
                                        new NoiseSensor(sensorReadings,
                                                frequency);
                                if (evaluator != null && evaluator
                                        .equals("WeightingBased")) {
                                    weightingSensors.put(noiseSensor,
                                            weighting);
                                } else if (evaluator != null &&
                                        evaluator.equals("RuleBased")) {
                                    ruleSensors.add(noiseSensor);
                                }
                                room.addSensor(noiseSensor);
                            } catch (Exception e) {
                                throw new FileFormatException("create " +
                                        "NoiseSensor error or cant " +
                                        "add NoiseSensor sensor");
                            }
                        } else if (sensorName.equals("OccupancySensor")) {
                            try {
                                for (int s = 0; s < lineHold[1]
                                        .split(",").length; s++) {
                                    sensorReadings[s] = Integer.parseInt
                                            (lineHold[1].split(",")[s]);
                                }
                                frequency = Integer.parseInt(lineHold[2]);
                                if (evaluator != null && evaluator
                                        .equals("WeightingBased")) {
                                    capacity = Integer.parseInt
                                            (lineHold[lineHold.length - 1]
                                                    .split("@")[0]);
                                    weighting = Integer.parseInt
                                            (lineHold[lineHold.length - 1]
                                                    .split("@")[1]);
                                    sumWeighting += weighting;
                                } else {
                                    capacity = Integer.parseInt(lineHold[3]);
                                }
                            } catch (Exception e) {
                                throw new FileFormatException(
                                        "OccupancySensor's " +
                                                "variables are invalid");
                            }
                            try {
                                OccupancySensor occupancySensor =
                                        new OccupancySensor(sensorReadings,
                                                frequency, capacity);
                                if (evaluator != null && evaluator
                                        .equals("WeightingBased")) {
                                    weightingSensors.put(occupancySensor,
                                            weighting);
                                } else if (evaluator != null && evaluator
                                        .equals("RuleBased")) {
                                    ruleSensors.add(occupancySensor);
                                }
                                room.addSensor(occupancySensor);
                            } catch (Exception e) {
                                throw new FileFormatException("create " +
                                        "OccupancySensor error or cant " +
                                        "add OccupancySensor sensor");
                            }
                        } else if (sensorName.equals("TemperatureSensor")) {
                            try {
                                if (evaluator != null && evaluator.
                                        equals("WeightingBased")) {
                                    for (int s = 0; s < lineHold[1]
                                            .split(",").length; s++) {
                                        if (s != lineHold[1].split(",")
                                                .length - 1) {
                                            sensorReadings[s] = Integer
                                                    .parseInt(lineHold[1]
                                                            .split(",")[s]);
                                        } else {
                                            sensorReadings[s] = Integer
                                                    .parseInt(lineHold[1]
                                                            .split(",")[s]
                                                            .split("@")[0]);
                                        }
                                    }
                                    weighting = Integer.parseInt
                                            (lineHold[lineHold.length - 1]
                                                    .split("@")[1]);
                                    sumWeighting += weighting;
                                } else {
                                    for (int s = 0; s < lineHold[1]
                                            .split(",").length; s++) {
                                        sensorReadings[s] = Integer.parseInt
                                                (lineHold[1].split(",")[s]);
                                    }
                                }
                            } catch (Exception e) {
                                throw new FileFormatException(
                                        "TemperatureSensor's " +
                                                "variables are invalid");
                            }
                            try {
                                TemperatureSensor temperatureSensor =
                                        new TemperatureSensor(sensorReadings);
                                if (evaluator != null && evaluator
                                        .equals("WeightingBased")) {
                                    weightingSensors.put(temperatureSensor,
                                            weighting);
                                } else if (evaluator != null && evaluator
                                        .equals("RuleBased")) {
                                    ruleSensors.add(temperatureSensor);
                                }
                                room.addSensor(temperatureSensor);
                            } catch (Exception e) {
                                throw new FileFormatException(
                                        "create TemperatureSensor " +
                                                "error or cant add " +
                                                "TemperatureSensor sensor");
                            }
                        } else {
                            throw new FileFormatException("wrong sensor name");
                        }
                    }
                    if (sumWeighting != 100 && evaluator != null &&
                            evaluator.equals("WeightingBased")) {
                        throw new FileFormatException("the sum of " +
                                "weighting is not 100");
                    }
                    if (evaluator != null && evaluator
                            .equals("WeightingBased")) {
                        HazardEvaluator hazardEvaluator =
                                new WeightingBasedHazardEvaluator(
                                        weightingSensors);
                        room.setHazardEvaluator(hazardEvaluator);
                    } else if (evaluator != null && evaluator
                            .equals("RuleBased")) {
                        HazardEvaluator hazardEvaluator =
                                new RuleBasedHazardEvaluator(ruleSensors);
                        room.setHazardEvaluator(hazardEvaluator);
                    } else if (evaluator != null) {
                        throw new FileFormatException("invalid evaluator");
                    }
                }
                if (roomOrderString != null) {
                    for (String roomInOrderString : roomOrderString) {
                        for (int k = 0; k < floor.getRooms().size(); k++) {
                            if (floor.getRooms().get(k).getRoomNumber() ==
                                    Integer.parseInt(roomInOrderString)) {
                                roomsOrder.add(floor.getRooms().get(k));
                            }
                        }
                    }
                }
                if (!roomsOrder.isEmpty()) {
                    floor.createMaintenanceSchedule(roomsOrder);
                }
            }
            buildings.add(building);
            readLine = bufferedReaderFile.readLine();
        }
        fileReader.close();
        return buildings;
    }

}
