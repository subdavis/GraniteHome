package nl.logicbit.HomePlugin;

import java.io.Serializable;

/**
 * @author matthijs2704
 * @version 0.1
 * @since 27-10-14
 */
public class SerializableLocation implements Serializable {

    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    SerializableLocation (double x, double y, double z, float yaw, float pitch){
        this.x = x;
        this.y = y;
        this.z = z;

        this.yaw = yaw;
        this.pitch = pitch;
    }
    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
