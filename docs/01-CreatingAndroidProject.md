# Creating the basic Android App in Android Studio



Android Studio>File>New>New Project...>
​	Application Name: Virtual Weight
​	Company Name:	  costigan.com	
Project Location: C:\dev\VirtualWeight

Next
​	[X] Phone and Tablet
​	API 25: Android 7.1.1 (Nougat)
Next>Empty Activity>Next
​	Activity Name: MainActivity
​	[X] Generate Layout File
​	Layout Name: activity_main
​	[X] Backwards Compatabiity (AppCompat)
Finish

This generates the following files:
`/.gradle`
`/.idea`
`/app`
`/build`
`/libs`
`/src`
​	`/androidTest`
​		`/java/com/costigan/virtualweight/ExampleInstrumentedTest.java`
​	`/main`
​		`/java/com/costigan/virtualweight/MainActivity.java`
​		`/res/`
​		`AndrioidManifest.xml`
​	`test`
`.gitignore`
`app.iml`
`build.gradle`
`proguarf-rules.pro`
`/gradle`
`.gitignore`
`build.gradle`
`gradle.properties`
`gradlew`
`gradlew.bat`
`local.properties`
`settings.gradle`
`VirtualWeight.iml`



### Running the App in an Emulator on an AVD (Android Virtual Device)

Select "app" in the project window.
Android Studio>Run>Run app>
Available Virtual Device>Pixel XL API 25>Ok



### Running the App on a real Device

Ensure USB Debugging is enabled on the device.

Connect the device

A notification, "Android System * USB Charging this device ", tap on this and change it to (*) Transfer files. 

*(The Device name should appear in the File Manager. e.g. "ONEPLUS A3003")*



Android Studio>Run>Run App>Motorola Moto C Plus (Android 8.0.0, API 26)

*The first time, the following message will appear:*

*Instant Run requires that the platform corresponding to your device (Android 8.0 (Oreo)) is installed.*

*Select Install and Continue*

The App now appears on the Phone.



#### Putting Project under Source control

##### Goto GIT and create REPO

https://github.com/ > New Repository
​	SoftDevJohn/VirtualWeight
​	(*) Public
​	[CHECKED] Initialize with a README

>Create Repository
>Clone or Download>Copy the address of the project which is:
>https://github.com/SoftDevJohn/mountaineering.git



##### Open command shell to initialse GIT in local PC Folder

cd C:\dev\VirtualWeight
git init
{this creates an empty local repository .gitin the current directory}

##### Add the files to GIT

git add -A

*("git add" stages modifications for the next "git commit")*

git commit -m "Added my initial mountaineering project"
4) Add Github link to my local git repoistory
git remote add origin https://github.com/SoftDevJohn/mountaineering.git

##### Push the files to Github

git push -u -f origin master
*NB: The -f overwrites everything on the Github repository, which is Ok, because this is the initial creation.



#### Adding extra files to Git

After creating the following files:

- README.md; 
- ./docs; and 
- ./docs/01-CreatingAndroidProject.md

These new files are untracked and not under source control.

##### Staging these files for the next commit

git add README.md

git add ./docs

git add ./docs/01-CreatingAndroidProject.md

These files are now tracked and ready to be committed to the local file system.

However, if we modify this file then the recent change will be untracked.

Running "git commit" commits the snapshot when the last "git add" was run. "git add" stages modifications to be commited by the next "git commit".

Commiting the staged files

git commit -m "Committing two new files"



##### Pushing to the server

At this point these new files are stored in the GIT repository on my local PC.

