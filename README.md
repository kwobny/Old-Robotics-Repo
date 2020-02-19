# RoboticsRepo
# Notes:
Main move commands are rotate robot and moveLinTrans. Main wait commands are wait for time, for distance or displacement traveled (you pick which quantity), and for angle traveled/for certain orientation passed.

One complex wait command you can write is wait for degrees traveled in turn drive/rotational translate. This would use wait for distance/displacement

This library is easily applicable to robots with the same library/MadHardware api (would need to rewrite/look over whole code if not), with the same number of wheels (need to rewrite motor buffers and a whole bunch of stuff), and with the same type of wheels (need to rewrite lin trans move if not).

# Todos for Todos:
Create an algorithm to isolate lintrans component from any set of buffer values, for the wait for displacement.

Find how to use the lintrans motor buffer, combined with the lintrans component of the change in distance to generate an rx and ry value, which also accounts for error values in the change in distance buffer.

Find how to get time taken to traverse lengthened distance, for ILI.

# Todo:
IMPORTANT: Consider using the java object wait method, or the Thread.sleep method to execute code in a small period of seconds. Look up the difference between the 2 methods. This will be separate from the wait for time implementation, and will only be used to execute polling code as often as necessary. This will not be used when the robot waits during autonomous. For example, this can be used to execute motorCali every interval instead of the ineffecient polling wait for time command.

If using the above methods, then you will need to take the things executing periodically in execute interval for the system itself, and the methods executing in the loop that are part of they system, and put it in the thing.

Make a system for implementing your own custom wait code. Make it so that you can poll and generate data for all the waits used through the motions class, and define a structure for custom wait code. Most likely will be a while loop with condition, and inside it the loop method will be executing. For this to work, make sure the loop method is public.

Make this a general rule: When modifying any of the values in the individual motor buffers, always do it with the intent of syncing the motors right after the action occurs. Do this so that individual motor buffer values stay consistant with the universal buffer. This is mainly for the wait for distance lintrans vs. universal buffer ratio calculation.

Make sure rot drive radius measures radius and power from the center point of the robot.

Make sure motor buffer wheel values are all initialized to 0 in beginning

Get rid of multiple buffers concept. There will only be a single set of buffers. It was found that ILI for wait for distance does not need to get values from another buffer.

Edit syncMotors so that automatic syncing of universal buffer to actuators is removed. Also edit it so that function runs motorCali instead. Make sure this function is public, so that robot code can execute it.

In motorCali, multiply the universal/main buffer values by the buffer's scale factor value when uploading those values to the actuator motors.

Make a subclass for the linear translate buffer and other buffers where necessary, which inherits from the base buffer class. Do this to accommodate the set of rx and ry values for just the linear translation. Look far below for instructions on making lin trans buffer subclass

Think of making an empty wait, or a wait until program ends, so that it can be used at the end of code to continue executing loop functions like motorCali. If implementing, think about integrating this wait into the base code itself instead of outside, to add efficiency.

Think about making 2 periods of execution for native interval commands, one for high frequency call/execute and one for low frequency execution.

Also think about what the period of morotCali execution should be. It can be 0.1 seconds or so i guess?

Consider removing syncMotor execution in rotate robot and lintrans function, and adding syncMotor execution in more comprex commands which use the lintrans and rotate basic functions. Do this so that when using in teleOp and applying both commands, that syncMotors is executed once instead of twice.

Consider making wait for distance execute every period of seconds instead of in the loop, to make code more efficient and fast.

Decide whether or not wait for distance is wait for distance, or displacement, or include both. Probably will do distance, because that is just easier to implrement.

---

Put a set of rx and ry values in the lin trans buffer class, so that these values will not have to be recalculated. Do this so that values can be accessed by wait for distance ILI.

Make wait for distance have an Enable Incremental Linear Interpolation option which allows you to slow down to the next speed that is inputted at the end. Accomplish this by calculating the scale factor (shown below) and setting the universal buffer's motor value multiplier every period/so often to decrease slowly. Make sure to reset this to 1 when done with maneuver/traveled full distance.

For ILI, make it clear in code, instructions, and documentation that the ILI is only to be used when the magnitude of the linear translation component stays constant. Direction can be non-constant, but the speed of the robot should be constant when calculating for the time taken to travel a distance.

Also determine your input units for the ILI.

Make ILI input be 1 double for speed (includes a final speed of 0), and 2 doubles for speed in x and y direction. There will be no getting values from any aux buffer because it was deemed unnecessary.

# Ideas for wait commands:
For wait for distance, this is an idea: whenever the poll for the wait distance occurs,
1. Get the change in the distance (displacement) traveled by each motor. If getPos command for each motor returns the wheel angle, then get the change in the angle (watch out for crossing over from 360 to 0 degrees), and multiply it by a constant (2 * pi * r?) to get change in distance.
2. Multiply that change by the amount of power the linear translate contributes for that wheel divided by the universal value for that wheel (which would be linTrans/Universal, also take into account the scale factor for the lintrans buffer and how it is considered when adding lintrans to universal). This will extract the linear translation component of the distance traveled. Because this ratio may always change due to a change in linTrans command (for example rotational translate), this is the reason why the total distance must be measured in changes, instead of the whole thing.
3. Take all four of these change values, and compare them against the current linear translate power values and rx and ry, in some way to come out with a dx and dy value. Make sure to account for inconsistancies in motor readings (maybe take averages for certain quantities?).
4. Take the dx and dy value and find the resultant of these using pythagorean theorem. This will give you the total distance traversed by the robot from the last time the poll was executed. Sum up all of these change in distance values to get the total distance traversed by the robot since the start.
5. Compare this total distance with the distance condition given, and return either true or false.

Additional idea:
Have a value titled totalDistance. Make sure to reset this at the beginning of every wait for distance thing.
whenever syncMotors executes, have it execute a function that, if in a distance wait state, gets the distance traversed from the last time syncMotors was called, to now. This will happen using the steps listed above. Do this step before the function executes motorCali and actually uploads values to the wheels.
Add the distance traversed to total distance.
Whenever the actual distance poll executes, measure the change in wheel position from the last syncMotor command. Use these values to calculate the displacement from last syncMotor command, but do not add it to the total distance variable. only use it and the total distance to calculate total distance as of now.

Another idea:
Alongside wait for distance traversed, also make a function which waits for displacement. Do this by getting the change in each wheel distance from the start of the maneuver, and using some sort of algorithm (develop this) to isolate the linear translate portion of the distance. Use this then to find the displacement from the starting position using inverse of the lintrans command.
