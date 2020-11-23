package bms.hazardevaluation;

import bms.sensors.HazardSensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeightingBasedHazardEvaluator implements HazardEvaluator {
    /**
     * the map hold the hazardSensor--integer
     */
    private Map<HazardSensor, Integer> sensors;

    /**
     * Creates a new weighting-based hazard evaluator with the
     * given sensors and weightings.
     * <p>
     * Each weighting must be between 0 and 100 inclusive,
     * and the total sum of all weightings must equal 100.
     *
     * @param sensors - mapping of sensors to their respective weighting
     * @throws IllegalArgumentException if any weighting is below 0 or
     *                                  above 100; or if the sum of
     *                                  all weightings is not equal to 100
     */
    public WeightingBasedHazardEvaluator(Map<HazardSensor, Integer> sensors)
            throws IllegalArgumentException {
        int sum = 0;
        for (Integer integer : sensors.values()) {
            if (integer < 0 || integer > 100) {
                throw new IllegalArgumentException();
            }
            sum += integer;
            if (sum > 100) {
                throw new IllegalArgumentException();
            }
        }
        this.sensors = sensors;
    }

    /**
     * Returns the weighted average of the current hazard levels
     * of all sensors in the map passed to the constructor.
     * <p>
     * The weightings given in the constructor should be used.
     * The final evaluated hazard level should be rounded to the
     * nearest integer between 0 and 100.
     * <p>
     * For example, given the following sensors and weightings,
     * this method should return a value of 28.
     * <p>
     * WeightingBasedHazardEvaluator example
     * <t>Sensor Type</t><t>Current Hazard Level</t><t>Weighting</t>
     * <t>OccupancySensor</t><t>40</t><t>20</t>
     * <t>NoiseSensor</t><t>65</t><t>30</t>
     * <t>TemperatureSensor</t><t>0</t><t>50</t>
     *
     * @return weighted average of current sensor hazard levels
     */
    @Override
    public int evaluateHazardLevel() {
        float hazardLevel = 0;
        float weight = 0;
        for (Map.Entry<HazardSensor, Integer> sensor : sensors.entrySet()) {
            hazardLevel += sensor.getKey().getHazardLevel()
                    * sensor.getValue();
            weight += sensor.getValue();
        }
        return (int) Math.ceil(hazardLevel / weight);
    }

    /**
     * Returns a list containing the weightings associated with all of
     * the sensors monitored by this hazard evaluator.
     *
     * @return weightings
     */
    public List<Integer> getWeightings() {
        return new ArrayList<>(sensors.values());
    }

    /**
     * Returns the string representation of this hazard evaluator.
     * <p>
     * The format of the string to return is simply "WeightingBased"
     * without the double quotes.
     * <p>
     * See the demo save file for an example (uqstlucia.txt).
     *
     * @return string representation of this hazard evaluator
     */
    @Override
    public String toString() {
        return "WeightingBased";
    }
}
