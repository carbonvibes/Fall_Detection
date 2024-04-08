
## Fall Detection with Fuzzy Logic and Alert System (Simulink)

This project implements a fall detection mechanism using fuzzy logic in Simulink (MATLAB). It simulates an Android application designed to address the following problem statement:

---
### Problem Statement

Design and deploy a Fuzzy logic based Android/iOS App that performs the following:

1.  **Continuous Monitoring:**  The microphone and accelerometer sensors remain active throughout the app operation to detect potential dangers/falls.
2.  **Fall Detection with Fuzzy Logic:**  The app continuously analyzes the phone's accelerometer data and microphone input. The fuzzy logic block analyzes the ongoing sound level (classified as Low, Medium, High) and the accelerometer readings (again classified as Low, Medium, High) to estimate the level of danger (Low, Medium, High).

3.  **Alert Transmission:**  If the danger level is classified as High, transmits an alert to a designated phone using a UDP network. The alert includes the GPS location as well as the audio captured by the microphone of the child's phone.
4.  **Buzzer Alarm:**  Plays a buzzer sound (alarm) on the phone with a volume proportional to the danger level (Medium or High).

---
 **Team Members**

-   Sriram Srinivasan
-   Raghavpravin K S
-   Arjun Sekar
---
**Course & Institution**

This project was completed as part of the Control Systems course at the Indian Institute of Technology Gandhinagar (IIT Gandhinagar) under the guidance of [Professor Nitin George](https://iitgn.ac.in/faculty/ee/fac-nithin).

The demonstration of the application and the explanation of the fuzzy logic can be found in this [YouTube link](https://youtu.be/x8rSORfWRAI?si=VHYO4uVkyF_HuO15).
