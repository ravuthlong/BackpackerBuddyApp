package ravtrix.backpackerbuddy;

/**
 * Created by Ravinder on 10/8/16.
 */

// Keeps track how many picasso images have been loaded onto grid view
public class Counter {

    private int count = 0; // Indexing for array start at 0 so first item added should start at 0

    public Counter() {}
    public Counter(int count) {
        this.count = count;
    }

    public void setCount() {
        this.count = 0;
    }
    public void addCount() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
