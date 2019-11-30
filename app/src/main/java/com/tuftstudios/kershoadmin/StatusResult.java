package com.tuftstudios.kershoadmin;

import com.google.gson.annotations.SerializedName;

public class StatusResult {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("availability")
    private int availability;

    public Boolean getError() {
        return error;
    }

    public int getAvailability() {
        return availability;
    }
}
