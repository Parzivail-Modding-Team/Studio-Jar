package com.dumbcodemc.studio.animation.instance;

import com.dumbcodemc.studio.model.CubeInfo;

public interface AnimatedCube {
    CubeInfo getInfo();
    void setRotation(float x, float y, float z);
    void setPosition(float x, float y, float z);
}
