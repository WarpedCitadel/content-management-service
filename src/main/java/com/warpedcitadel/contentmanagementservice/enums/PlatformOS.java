package com.warpedcitadel.contentmanagementservice.enums;

public enum PlatformOS {

    BROWSER(1),
    WINDOWS(2),
    LINUX(3),
    MACOS(4);

    private final int code;

    PlatformOS(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
