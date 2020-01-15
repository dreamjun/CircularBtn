package com.junjun.circularbtn;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

public class Pie {
    private String name;
    private String id;
    private int color;
    private Region region;
    private boolean isTouch;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setRegion(Path path) {
        Region re = new Region();
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        re.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        this.region = re;
    }

    public boolean isInRegion(float x, float y) {
        return region != null && region.contains((int) x, (int) y);
    }

    public boolean isTouch() {
        return isTouch;
    }

    public void setTouch(boolean touch) {
        isTouch = touch;
    }
}
