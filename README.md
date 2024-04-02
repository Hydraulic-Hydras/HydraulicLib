<p align="center">
  <img src="https://github.com/Tea505/HydraulicLib/blob/master/HydraLib/Hydraulic%20Hydras.png">
</p>

## Hydraulic Lib
HydraulicLib is a comprehensive custom library designed to streamline programming for both seasoned veterans and aspiring young programmers in the field of robotics. As of February 26, 2024, the library has been officially released to the public.


To install the library, in `settings.gradle` add:
</br>

   ```gradle
   dependencyResolutionManagement {
		 repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
		    repositories {
			mavenCentral()
                   maven { url "https://jitpack.io" }
		}
	}
   ```  

Then in `build.dependencies.gradle` add:
</br>

   ```gradle
   repositories { 
        jcenter()
        mavenCentral()
        maven { url "https://jitpack.io" }
   }
   dependencies {
        implementation 'com.github.Hydraulic-Hydras:HydraulicLib:VERSION'
    }
   ```  
Replacing `VERSION` with the latest release

## Welcome
Welcome to Hydraulic Lib

It is thrilling to have you here! Whether you're an experienced developer or just starting out, 
I hope that this project accompanies and helps you throughout your journey.

Feel free to raise issues, submit pull requests, share ideas or hit me up on discord.
Username: tea7003

Happy coding! 

## Overview

HydraulicLib encompasses a range of classes tailored to various aspects of robotic programming, including kinematics, mathematical utilities, command sequences, and motion control.

### Util

The Util module serves as the foundational component of HydraulicLib, featuring the versatile `Contraption` class. This class can be seamlessly integrated into hardware classes to extend functionality, facilitating the implementation of simple 'init' and 'loop' methods.

### Path

Path introduces a novel class system developed through extensive research into trajectories and motion profiled movements. Its primary objective is to empower teams in creating customized movement patterns utilizing command sequences.

### Motion

Motion comprises classes dedicated to implementing motion profiled movement, with a focus on contraptions such as motion profiled DcMotors, ensuring smooth and precise robotic motion.

### Kinematics

Kinematics houses essential mathematical functions utilized in Odometry pods. Additionally, it is poised to include kinematics for Mecanum, tank, and Swerve (differential and Coaxial) drive systems in future updates.

### Input

Input serves as the central repository for gamepad input, providing a convenient interface for referencing all gamepad buttons within the library.

### Geometry

Geometry encompasses fundamental mathematical operations utilized across various modules within HydraulicLib, serving as the cornerstone for complex robotic computations.

### Controller

Controller currently features a PIDF controller file, facilitating the seamless integration of PIDF control into mechanisms and drivetrains, enhancing precision and stability.

### Command

Command houses an array of CommandSequences and functions tailored for constructing robot paths. These commands are designed to be used in conjunction with trajectories, providing teams with a flexible framework for executing complex maneuvers. For alternative methods, users are encouraged to explore FTCLIB on GitHub.

## Getting Started

To begin leveraging HydraulicLib in your robotics projects, simply install the library and explore the comprehensive documentation provided. With its intuitive interface and robust functionality, HydraulicLib aims to simplify the process of robotics programming, enabling teams to focus on innovation and problem-solving.
