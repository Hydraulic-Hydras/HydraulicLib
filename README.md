        ![alt text](https://github.com/Tea505/HydraulicLib/blob/master/HydraLib/9384-logo.png) 
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

## Util
Util contains the basic class (Contraption) that can be applied to your hardware class to
extend it and implement simple 'init' and 'loop' methods.
