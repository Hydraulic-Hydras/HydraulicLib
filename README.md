<p align="center">
  <img src="https://github.com/Tea505/HydraulicLib/blob/master/HydraLib/Hydraulic%20Hydras.png">
</p>

## Hydraulic Lib
HydraulicLib is a library designed to improve the initial programming experience for new members as 
well as greatly enhance the efficiency of code for veterans.


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
        implementation 'com.github.Hydraulic-Hydras:HydraulicLib:Tag'
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
