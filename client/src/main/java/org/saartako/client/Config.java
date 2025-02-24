package org.saartako.client;

public class Config {

    public static final int APP_WIDTH = 1024;

    public static final int APP_HEIGHT = 720;

    public static final int GRID_LARGE_COLUMNS = 3;

    public static final int GAP_SMALL = 4;
    public static final int GAP_MEDIUM = 8;
    public static final int GAP_LARGE = 16;
    public static final int GAP_HUGE = 40;

    // TODO: use environment variables instead
    public static final String serverUrl = "http://localhost:8080";

    private Config() {
    }

}
