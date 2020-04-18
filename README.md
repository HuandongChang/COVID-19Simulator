# COVID-19Simulator
## Introduction
In this project, we use Java DrawingPanel package to simulate the spread of COVID-19 under different circumstances, such as medical conditions, social distancing, population densition, etc. The purpose of this project is to show the effects of changing one or more of the factors that may potentially influence the spread of Coronavirus.

## Run the Code
Download the four source files in src folder, and compile and run VirusSpreadSimulator.java file in your terminal.

## Demos

## Functions and Features
In this simplified simulation, we assume the population is randomly located in a city and ignore all kinds of facilities except for a hospital. Features of our simulator include but are not limited to:

- Hospital: The only facility in the city is a hospital. Hospital will continue accept patients until they are full. Patients will be removed from the hospital once they are recovered or dead. User can adjust the capacity of the patient.

- Each Person has several features: age, speed, probability of being infected, recovery time after infected, and death probability.

- Each Person's Mode:
  - Healthy: Green, not infected.
  - Sick: Pink, no symptom but infectious, will not be hospitalized; 
  - Hospitalize: Red, infectious in the city and will be hospitalized if the hospital is not full.
  - Recovered: Blue, once a person is cured by the hospital. 
  - Death: Disappear, once a person is turned into Hospitalize State, this patient has a certain chance to die. 
  
- Quarantine Mode: People can choose the quarantine mode, then each person will be limited to a certain area without interacting with people in other areas.

- Population Density: User can adjust population density either by adjusting the number of people in the city or enlarge or reduce the city size using mouse.



## Models and Calculations

#### Model

The features of this project serve the R = DOTS formula for person-to-person transmission. 
Rate of infection = Duration * Opportunity * Transmission probability * Susceptibility.

#### Data and Calculation

- Every 0.4 second represents a day.
- Population Age Distrubution: Age is uniformly distributed between 0 and 90. 
- Speed: Each person's speed is negatively correlated to their age.
- Every person has 50%-90% chance of getting infected, positively corrected to the age. Sick state will last for 14 days after infected for everyone, then turn into Hospitalize State.
- After turning into Hospitalize State, this patient will recover after 14 to 42 days, positively correlated to the age except the patient dies. Note the getting into hospital can halve the recovery time.
- Death rate for different ages
[Estimates of the severity of coronavirus disease 2019: a model-based analysis](https://www.thelancet.com/journals/laninf/article/PIIS1473-3099(20)30243-7/fulltext):
0-10: 0.0000161
10-20:0.0000695
20-30: 0.000309
30-40: 0.0844
40-50: 0.0016
50-60: 0.00595
60-70: 0.0193
70-80: 0.0428
80-: 0.078





## Reference
https://github.com/AzizZayed/Virus-Spread-Simulation
https://www.nytimes.com/2020/03/05/health/coronavirus-deaths-rates.html?referringSource=articleShare
https://www.thelancet.com/journals/laninf/article/PIIS1473-3099(20)30243-7/fulltext
