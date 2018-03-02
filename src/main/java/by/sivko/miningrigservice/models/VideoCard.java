package by.sivko.miningrigservice.models;


/**
 * class VideoCard
 */
public class VideoCard {

    private String name;
    private int temperature;
    private int power;
    private int memory;

    //constructors
    private VideoCard() {
    }

    public VideoCard(String name, int temperature, int power, int memory) {
        this();
        this.name = name;
        this.temperature = temperature;
        this.power = power;
        this.memory = memory;
    }

    //getters and setters
    public void setName(String name) {
        this.name = name;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getMemory() {
        return memory;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getPower() {
        return power;
    }

}
