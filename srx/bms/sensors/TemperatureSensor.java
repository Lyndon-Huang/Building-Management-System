package bms.sensors;

/**
 * A sensor that measures ambient temperature in a room.
 *
 * @ass1
 */
public class TemperatureSensor extends TimedSensor
        implements HazardSensor, ComfortSensor {

    /**
     * Creates a new temperature sensor with the given sensor readings and
     * update frequency.
     * <p>
     * For safety reasons, all temperature sensors <b>must</b> have an update
     * frequency of 1 minute.
     *
     * @param sensorReadings a non-empty array of sensor readings
     * @ass1
     * @see TimedSensor#TimedSensor(int[], int)
     */
    public TemperatureSensor(int[] sensorReadings) {
        super(sensorReadings, 1);
    }

    /**
     * Returns the hazard level as detected by this sensor.
     * <p>
     * A temperature sensor detects a hazard if the current temperature reading
     * ({@link #getCurrentReading()}) is greater than or equal to 68 degrees,
     * indicating a fire.
     * In this case, a hazard level of 100 should be returned.
     * Otherwise, the returned hazard level is 0.
     *
     * @return sensor's current hazard level, 0 to 100
     * @ass1
     */
    @Override
    public int getHazardLevel() {
        if (this.getCurrentReading() >= 68) {
            return 100;
        }
        return 0;
    }

    /**
     * Returns the human-readable string representation of this temperature
     * sensor.
     * <p>
     * The format of the string to return is
     * "TimedSensor: freq='updateFrequency', readings='sensorReadings',
     * type=TemperatureSensor"
     * without the single quotes, where 'updateFrequency' is this sensor's
     * update frequency (in minutes) and 'sensorReadings' is a comma-separated
     * list of this sensor's readings.
     * <p>
     * For example: "TimedSensor: freq=1, readings=24,25,25,23,26,
     * type=TemperatureSensor"
     *
     * @return string representation of this sensor
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s, type=TemperatureSensor", super.toString());
    }

    /**
     * Returns the comfort level as detected by this sensor.
     * <p>
     * The returned comfort level is determined by the following rules:
     * <p>
     * If the current temperature reading (TimedSensor.getCurrentReading())
     * is between 20 and 26 degrees (inclusive), the ambient temperature
     * is deemed "comfortable" and 100 should be returned.
     * For every degree that the current temperature reading exceeds 26
     * degrees or goes below 20 degrees, the returned comfort level
     * should be reduced by 20 from a starting value of 100.
     * If the current temperature reading is 15 degrees or below,
     * or is 31 degrees or above, 0 should be returned.
     *
     * @return
     */
    @Override
    public int getComfortLevel() {
        int currentReading = this.getCurrentReading();
        if (currentReading >= 20 && currentReading <= 26) {
            return 100;
        } else if (currentReading > 31 || currentReading < 15) {
            return 0;
        } else {
            return 80;
        }
    }

    /**
     * Returns the machine-readable string representation
     * of this temperature sensor.
     * <p>
     * The format of the string to return is:
     * <p>
     * TemperatureSensor:sensorReading1,sensorReading2,...,sensorReadingN
     * <p>
     * where 'sensorReadingX' is the Xth sensor reading in this sensor's
     * list of readings, from 1 to N where N is the number of readings.
     * There should be no newline at the end of the string.
     *
     * @return
     */
    public String encode() {
        return (String.format("TemperatureSensor:%s", super.encode()));
    }
}
