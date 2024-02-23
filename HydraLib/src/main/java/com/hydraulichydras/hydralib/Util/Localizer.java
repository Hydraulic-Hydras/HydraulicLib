package com.hydraulichydras.hydralib.Util;

import com.hydraulichydras.hydralib.Geometry.Pose;

public interface Localizer {
    void periodic();
    Pose getPos();
    void setPos(Pose pose);
}
