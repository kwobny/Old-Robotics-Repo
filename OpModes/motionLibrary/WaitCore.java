package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import java.util.ArrayList;
import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitCore {
  public WaitConditionClass waiters;
  public WaitCallbackClass waitCallbacks;

  WaitCore(WaitConditionClass a, WaitCallbackClass b) {
    waiters = a;
    waitCallbacks = b;
  }
}