# RoboticsRepo
# Notes:
Main move commands are rotate robot and moveLinTrans. Main wait commands are wait for time, for distance or displacement traveled (you pick which quantity), and for angle traveled/for certain orientation passed.

One complex wait command you can write is wait for degrees traveled in turn drive/rotational translate. This would use wait for distance/displacement

This library is easily applicable to robots with the same library/MadHardware api (would need to rewrite/look over whole code if not), with the same number of wheels (need to rewrite motor buffers and a whole bunch of stuff), and with the same type of wheels (need to rewrite lin trans move if not).

# Todo:
Create a private Auxiliary (Aux) buffer which is a second Universal Buffer

Create a public function titled setSyncDirection which sets from which buffer (motor buffers, aux, or universal/main) to which buffer (aux or universal/main) the syncMotors syncs the motors to and from. Make it by creating an enum for buffers (motor buffers, aux, and main/universal). Make sure that this function stores the from and to enum in 2 class fields, as well as switching the references to each thing.

(Look at above) Or consider just make 2 public fields in Base class detailing which buffer to and from. Use enum for this option.

Edit syncMotors function so that if from is the motor buffers, then another specialized syncing action occurs. Also make it so that automatic syncing of universal buffer to actuators is removed. Also edit it so that function runs motorCali if "to" is the universal buffer. Make sure is public.

In motorCali, multiply the universal/main buffer values by the buffer's scale factor value when uploading those values to the actuator motors.

Make a subclass for the linear translate buffer and other buffers where necessary, which inherits from the base buffer class. Do this to accommodate the 2 sets of rx and ry values for just the linear translation. Look far below for instructions on making lin trans buffer subclass

Think about making 2 periods of execution for native interval commands, one for high frequency call/execute and one for low frequency execution.

Also think about what the period of execution should be. It can be 0.1 seconds or so i guess?

Think about combining setSyncDirection and syncMotors, if you are going to do it, and how to do it.

Consider/think about renaming the Universal buffer to the main buffer.

Consider multiplying aux and main buffer values by their respective scale factors when syncing them to each other.

Consider removing syncMotor execution in rotate robot and lintrans function, and adding syncMotor execution in more comprex commands which use the lintrans and rotate basic functions. Do this so that when using in teleOp and applying both commands, that syncMotors is executed once instead of twice.

---

Put 2 sets of rx and ry values in the lin trans buffer class, one for the main buffer and one for aux buffer. Also Edit moveLinTrans function so that it stores its rx and ry values in the corresponding set of fields depending on which sync direction mode is set. Do this so that values can be accessed by wait for distance ILI.

Accomplish this (task above) in this way: if "from" is the motor buffers, then store the values in the set pointed to by "to". Else, either store the values in the set as pointed in the "from", the "to", or just pick a default set to store in (most likely universal/main set). Most likely, values will be stored in set pointed in the "to", in the case that "from" is not the motor buffers. But still consider the options.

Make wait for distance have an Enable Incremental Linear Interpolation option which allows you to slow down to the next speed that is inputted at the end. Accomplish this by calculating the scale factor (shown below) and setting the universal buffer's motor value multiplier every period/so often to decrease slowly. Make sure to reset this to 1 when done with maneuver/traveled full distance.

Also determine your input units for the ILI.

Make ILI input be 1 double for speed (includes a final speed of 0), 2 doubles for speed in x and y direction, or just a value of true (no doubles) to get the speed information from the rx and ry class fields, which would be set by setting the sync direction to "from motor buffers to aux" and applying various motions so that linTrans is called and applied.

For the last option, make it so that at the end, the values in aux buffer are automatically transfered to main (Universal) and main buffer is synced w/ motors. Also for the last option, set the sync direction back to "from motor buffers to main" once wait function is called and then begins, but debate about this in head before proceeding (you probably will do it).

Make sure motor buffer wheel values are all initialized to 0 in beginning
