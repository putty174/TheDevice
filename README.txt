Simply make a new workspace, and then from the empty project directory:

Right Mouse Click > Import > Existing Projects > ../TheDevice

You should see 15 projects to import. That's right, import all of them.


To run on Desktop:
Right mouse click TheDevice-desktop > Run as... > Java Application

To run on Android:
Right mouse click TheDevice-android > Run as... > Android Application

To run on iOS:
Right mouse click TheDevice-robovm > Run as... > iOS Application (Actually I have no idea what it should say, but you get the idea what to do from the other two methods.)


Assets go to this address:
..\TheDevice\TheDevice-android\assets

Yes, that is the Android project, and not the main project. That is because ALL PROJECTS, including the main project, refer to the Android asset folder, to simplify assets so we don't have to copy them all over the place.