# Aircraft-Onload-App
Redesign of FedEx Aircraft Onload Verification Software


HOW TO RUN:

Requirements:
  1. Android Studio https://developer.android.com/studio
  2. NPM Json-Server https://www.npmjs.com/package/json-server (Make sure Node.js is installed then run "npm install -g json-server")
  
 Instructions:
  1. Download project and open in Android Studio. Do not include 'flight.json'. Separate this file to another directory.
  2. Inside a command prompt open to the directory that you stored 'flight.json'. Example: "cd C:\Users\Ryan\Desktop"
  
  ---------If Running App on Physical Device---------
  3. Open a command prompt and find your local IPv4 address.
  4. Ensure the device that is going to be used to run the application is connected to the same network as your machine.
  5. Run the following command to locally host the json file on your own machine. "json-server --host [INSERT YOUR IPv4 HERE] --watch flight.json"
  6. Test this is working by going to "http://[YOUR IP]:3000" You should be greeted with a json-server page confirming the server is running. 
     Do not close the command prompt window. (Port should be 3000. If not, use the port json-server is using.)
  7. Inside of Android Studio navigate to the MainActivity.java file.
  8. At the very top of MainActivity change the IP string to your own IPv4 address.
  9. Run the program.
  
  ---------If Running App on Virtual Emulator---------
  3. Open a command prompt and find your local IPv4 address.
  4. Run the following command to locally host the json file on your own machine. "json-server --watch flight.json"
  5. Test this is working by going to "http://localhost:3000" You should be greeted with a json-server page confirming the server is running. 
     Do not close the command prompt window. (Port should be 3000. If not, use the port json-server is using.)
  6. Inside of Android Studio navigate to the MainActivity.java file.
  7. At the very top of MainActivity change the IP string to 10.0.2.2 (This is the 'localhost' equivalent inside the emulator).
  8. Run the program.
  
