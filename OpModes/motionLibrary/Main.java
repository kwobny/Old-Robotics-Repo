package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import java.util.ArrayList;
import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Main {
  //sub objects
  public MadHardware mhw;

  public WaitConditionClass waitConditions = new WaitConditionClass();
  public WaitCallbackClass waitCallbacks = new WaitCallbackClass();

  public WaitCore wait = new WaitCore(waitConditions, waitCallbacks);
  public Motions move;
  public RPS rps = new RPS();

  public Main(MadHardware hmw) {
    mhw = hmw;

    move = new Motions(mhw, wait);
  }

  //loop for the motions
  //allows movements which are time dependent, like rotational translation
  //also allows execution of other stuff
  public void loop() {
    wait.executeIntervals();
    wait.runTimeouts();
  }

  //function that you call at the end of autonomous sequence to wait for program to end. This should be the absolute last function executed in program.
  public void endProgram() {
    while (true) {
      loop();
    }
  }
}