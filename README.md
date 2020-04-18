# COVID-19Simulator
## Introduction
In this project, we use Java DrawingPanel package to simulate the spread of COVID-19 under different circumstances, such as medical conditions, social distancing, population densition, etc.

## Run the Code
Download the four source files in src folder, and compile and run VirusSpreadSimulator.java file in your terminal.

## Demos

## Functions and Features
In this simplified simulation, we assume the population is randomly located in a city and ignore all kinds of facilities except for a hospital. Features of our simulator include but are not limited to:

- Hospital: The only facility in the city is a hospital. Hospital will continue accept patients until they are full. Patients will be removed from the hospital once they are recovered or dead. User can adjust the capacity of the patient.

- Each Person's Mode:
  - Healthy: Green, not infected.
  
  - Sick: Pink, no symptom but infectious, will not be hospitalized; Sick state will last for 14 days after infected.
  - Hospitalize: Red, infectious in the city and will be hospitalized if the hospital is not full.
  - Recovered: Blue, once a person is turned into Hospitalize State, this patient will recover after 14 to 42 days, depending on the age except the patient dies. Note the getting into hospital can halve the recovery time.
  - Death: Disappear, once a person is turned into Hospitalize State, this patient has a certain chance to die. Probability depends on age.
  
- Quarantine Mode: People can choose the quarantine mode, then each person will be limited to a certain area without interacting with people in other areas.

- Population Density: User can adjust population density either by adjusting the number of people in the city or enlarge or reduce the city size using mouse.

- Age Distrubution: Age is uniformly distributed between 0 and 80. 

- Speed: Each person's speed is correlated to their age.

## Models and Calculations


## Reference
https://www.nytimes.com/2020/03/05/health/coronavirus-deaths-rates.html?referringSource=articleShare
