  ![alt text](https://github.com/Tea505/HydraulicLib/blob/master/HydraLib/Hydraulic%20Hydras.png)
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

## Welcome
Welcome to Hydraulic Lib

It is thrilling to have you here! Whether you're an experienced developer or just starting out, 
this open-source project is a collaborative space where innovation thrives. Dive into our codebase, 
explore our documentation, and join our vibrant community of contributors from around the globe.

Feel free to raise issues, submit pull requests, or share your ideas. Together, 
let's build something remarkable and make a positive impact in the world of software development.

Happy coding! 
