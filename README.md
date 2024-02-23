## Hydraulic Lib
HydraulicLib is a library designed to improve the initial programming experience for new members as 
well as greatly enhance the efficiency of code for veterans.

To install the library, in `build.dependencies.gradle` add:
</br>

   ```gradle
   dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			jcenter()
            maven { url "https://jitpack.io" }
		}
	}
   dependencies {
         implementation 'com.github.Tea505:HydraulicLib:VERSION'
   }
   ```  
Replacing `VERSION` with the latest release

## How does it work?

The library contains of many classes including kinematics, MathUtils, Command sequences and Motion.

### Util
Util contains the basic class (Contraption) that can be applied to your hardware class to
extend it and implement simple 'init' and 'loop' methods.

### Path
Path is a new class system that I developed by studying a lot about trajectories and motion
profiled movements. The purpose of it is to help teams create their own movement patterns
using command sequences.

### Motion
Motion consists of classes that implement MotionProfiled movement with contraptions such as a
motion profiled DcMotor.

### Kinematics
Kinematics contains the math used for Odometry pods and it will also contain Mecanum, tank
and Swerve (differential and Coaxil) kinematics in the future.

### Input
Input is your basic gamepad input place where all the gamepads buttons are referenced.

### Geometry
Geometry contains of all the basic Math I've used for other other files. Some could say it is
the foundation for other classes as well.

### Controller
Controller as of right now contains my PIDF controller file which helps team implement it for
their mechanisms and drivetrain.

### Command
Command consists of all the CommandSequences and functions you can use to built a path for your
robot. These Commands are supposed to be built with trajectories for now so you if you'd like
to try a different method I would recommend you check out FTCLIB on github. 