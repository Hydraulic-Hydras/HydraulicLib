<p align="center">
  <img src="https://github.com/Tea505/HydraulicLib/blob/master/HydraLib/Hydraulic%20Hydras.png">
</p>

## Hydraulic Lib
Welcome to HydraulicLib, a versatile and comprehensive library tailored to simplify the journey of robotics programming, whether you're an experienced developer or just starting out. Our mission is to provide a robust yet accessible toolkit that empowers programmers to tackle complex challenges with confidence.

## Getting Started
To begin leveraging HydraulicLib in your robotics projects, simply install the library and explore the comprehensive documentation provided. With its intuitive interface and robust functionality, HydraulicLib aims to simplify the process of robotics programming, enabling teams to focus on innovation and problem-solving.

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
HydraulicLib is crafted with care to accompany you throughout your robotics journey, offering a wealth of features and functionalities designed to enhance your coding experience. Whether you're a seasoned veteran or a novice enthusiast, our library is here to support and assist you every step of the way. At HydraulicLib, we are committed to excellence in both design and functionality. Each component of our library is meticulously crafted to meet the highest standards, ensuring reliability, efficiency, and ease of use for all users. With HydraulicLib, you can trust that you are equipped with a tool of unparalleled quality and performance. We believe in the power of innovation to drive progress and change the world for the better. That's why HydraulicLib is designed to empower programmers to unleash their creativity and explore new possibilities in robotics programming. Whether you're building a simple prototype or a sophisticated robotic system, HydraulicLib provides the tools you need to turn your ideas into reality.

HydraulicLib is more than just a library - it's a vibrant community of passionate developers and enthusiasts united by a shared love for robotics and technology. Join us as we embark on this exciting journey together, exchanging ideas, sharing knowledge, and supporting each other every step of the way.
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

### Command
Command houses an array of CommandSequences and functions tailored for constructing robot paths. These commands are designed to be used in conjunction with trajectories, providing teams with a flexible framework for executing complex maneuvers. For alternative methods, users are encouraged to explore FTCLIB on GitHub.
