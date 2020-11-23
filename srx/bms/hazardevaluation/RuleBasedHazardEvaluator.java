package bms.hazardevaluation;

import bms.sensors.HazardSensor;
import bms.sensors.OccupancySensor;
import bms.sensors.Sensor;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class RuleBasedHazardEvaluator implements HazardEvaluator {
    /**
     * the list of sensor
     */
    private List<HazardSensor> sensors;

    /**
     * Creates a new rule-based hazard evaluator
     * with the given list of sensors.
     *
     * @param sensors sensors to be used in the hazard level calculation
     */
    public RuleBasedHazardEvaluator(List<HazardSensor> sensors) {
        this.sensors = sensors;
    }

    /**
     * Returns a calculated hazard level based on applying a set of rules
     * to the list of sensors passed to the constructor.
     * <p>
     * The rules to be applied are as follows. Note that square brackets
     * [] have been used to indicate mathematical grouping.
     * If there are no sensors, return 0.
     * If there is only one sensor, return that sensor's current hazard
     * level as per HazardSensor.getHazardLevel().
     * If there is more than one sensor:
     * If any sensor that is not an OccupancySensor has a
     * hazard level of 100, return 100.
     * Calculate the average hazard level of all sensors that are
     * not an OccupancySensor. Floating point division should be used
     * when finding the average.
     * If there is an OccupancySensor in the list, multiply the average
     * calculated in the previous step by [the occupancy sensor's current
     * hazard level divided by 100, using floating point division].
     * Return the final average rounded to the nearest
     * integer between 0 and 100.
     * You can assume that there is no more than one OccupancySensor
     * in the list passed to the constructor.
     *
     * @return calculated hazard level according to a set of rules
     */
    @Override
    public int evaluateHazardLevel() {
        float average = 0;
        if (sensors.isEmpty()) {
            return 0;
        } else if (sensors.size() == 1) {
            return sensors.get(0).getHazardLevel();
        } else {
            for (HazardSensor sensor : sensors) {
                if (sensor.getClass() != OccupancySensor.class
                        && sensor.getHazardLevel() == 100) {
                    return 100;
                }
                if (sensor.getClass() != OccupancySensor.class) {
                    average += (float) sensor.getHazardLevel();
                } else {
                    average *= ((float) sensor.getHazardLevel() / 100);
                }
            }
            return (int) average;
        }
    }

    /**
     * Returns the string representation of this hazard evaluator.
     * The format of the string to return is simply
     * "RuleBased" without double quotes.
     * <p>
     * See the demo save file for an example (uqstlucia.txt).
     *
     * @return string representation of this room
     */
    @Override
    public String toString() {
        return "RuleBased";
    }
}
