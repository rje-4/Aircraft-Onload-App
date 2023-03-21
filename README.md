# Aircraft-Onload-App
Redesign of FedEx Aircraft Onload Verification Software


HOW TO RUN:

Requirements:
  Android Studio
  NPM Json-Server
  
 Instructions:
  1. Download project and open in Android Studio. Do not include 'flight.json'. Separate this file to another directory.
  2. Open a command prompt and find your local IPv4 address.
  3. Inside a command prompt open to the directory that you stored 'flight.json'. Example: "cd C:\Users\Ryan\Desktop"
  4. Run the following command to locally host the json file on your own machine. "json-server --host [INSERT YOUR IPv4 HERE] --watch flight.json"
  5. Test this is working by going to "http://[YOUR IP]:3000" You should be greeted with a json-server page confirming the server is running. 
     Do not close the command prompt window. (Port should be 3000. If not, use the port json-server is using.)
  6. Inside of Android Studio navigate to the MainActivity.java file.
  7. At the very top of MainActivity change the IP string to your own IPv4 address.
  8. Run the program.
