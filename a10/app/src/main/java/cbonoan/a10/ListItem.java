package cbonoan.a10;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ListItem implements Serializable {
    private long dateTime;
    private String item;

    public ListItem(String item) {
        this.item = item;
        dateTime = System.nanoTime();
    }

    public ListItem(long dateTime, String item) {
        this.dateTime = dateTime;
        this.item = item;
    }

    @NonNull
    @Override
    public String toString() {
        return item;
    }

    public long getDateTime() {
        return dateTime;
    }

    public String getItem() {
        return item;
    }
}
