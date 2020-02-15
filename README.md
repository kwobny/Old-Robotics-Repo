# RoboticsRepo
# Todo:
Create a private Auxiliary (Aux) buffer which is a second Universal Buffer

Create a public function titled setSyncDirection which sets from which buffer (motor buffers, aux, or universal/main) to which buffer (aux or universal/main) the syncMotors syncs the motors to and from. Make it by creating an enum for buffers (motor buffers, aux, and main/universal). Or just make 2 public fields in Base class detailing which buffer to and from. Use enum for this option.

Edit syncMotors function so that if from is the motor buffers, then another specialized syncing action occurs. Also make it so that automatic syncing of universal buffer to actuators is removed. Also edit it so that function runs motorCali if "to" is the universal buffer. Make sure is public.

In motorCali, multiply the universal/main buffer values by the buffer's scale factor value when uploading those values to the actuator motors.

Also think about what the period of execution should be

Think about combining setSyncDirection and syncMotors, if you are going to do it, and how to do it.

Consider/think about renaming the Universal buffer to the main buffer.

Consider multiplying aux and main buffer values by their respective scale factors when syncing them to each other.

---

Edit moveLinTrans function so that it stores the values of rx and ry in class fields, so that they can be accessed by wait for distance ILI.

Make wait for distance have an Enable Incremental Linear Interpolation option which allows you to slow down to the next speed that is inputted at the end. Accomplish this by calculating the scale factor (shown below) and setting the universal buffer's motor value multiplier every period/so often to decrease slowly. Make sure to reset this to 1 when done with maneuver/traveled full distance.

Also determine your input units for the ILI.

Make ILI input be 1 double for speed (includes a final speed of 0), 2 doubles for speed in x and y direction, or just a value of true (no doubles) to get the speed information from the rx and ry class fields, which would be set by setting the sync direction to "from motor buffers to aux" and applying various motions so that linTrans is called and applied.

For the last option, make it so that at the end, the values in aux buffer are automatically transfered to main (Universal) and main buffer is synced w/ motors. Also for the last option, set the sync direction back to "from motor buffers to main" once wait function is called and then begins, but debate about this in head before proceeding (you probably will do it).

Make sure motor buffer wheel values are all initialized to 0 in beginning
