package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02;

/**
 * Created by student on 21.05.2018.
 */

public class Timestamp {
    private String value;
    private long timestamp;

    public Timestamp(String value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Timestamp{" +
                "value='" + value + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
