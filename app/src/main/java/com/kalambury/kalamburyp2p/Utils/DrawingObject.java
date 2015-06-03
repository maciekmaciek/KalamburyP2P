package com.kalambury.kalamburyp2p.Utils;

import android.graphics.Path;

import java.io.Serializable;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2014-07-27.
 */
public class DrawingObject implements Serializable {
    private static final long serialVersionUID = 3200l;
    private Path path;
    private int color;
    private int size;

    public DrawingObject(Path path, int color, int size) {
        this.path = path;
        this.color = color;
        this.size = size;
    }

    public int getColor() {
        return color;
    }

    public int getSize() {
        return size;
    }

    public Path getPath() {
        return path;
    }
}