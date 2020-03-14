package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import java.util.ArrayList;
import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Main {
  public WaitConditionClass waitConditions = new WaitConditionClass();
  public WaitCallbackClass waitCallbacks = new WaitCallbackClass();

  public WaitCore wait = new WaitCore(waitConditions, waitCallbacks);
}