# Traffic light simulator

A traffic light simulator combined with reinforcement learning system ( Q-learning algorithm )

#### state description

  ```
  State (vertical, horizontal, light setting, delay):
      Closest car position from intersection on the vertical road(0-8, 9 if no cars) X
      Closest car position from intersection on the horizontal road(0-8, 9 if no cars) X
      Light setting( 1 = traffic light on vertical road  is red, 0 = traffic light on horizontal road is red)
      Delay( 0-3, Time needed to wait until the next switch action can be executed)
  ```
  
#### Policy

``` 
Ɛ-greedy: choose the action that has highest estimated reward (wherther if switch the light) > choose random action
  
Ɛ-soft: choose the action that has highest estimated reward  < choose random action
  
Different Ɛ value was selected to compare the perfromance  
  
  ```
  
#### Q table evaluation

 ```
 Q <-- Q + α [ r + γ max(Q') - Q]
 
 r - reward ( r = 0, if no car stopped at the intersection, otherwise r = -1) 
 α - learning rate
 γ - discount factor
 Max(Q') - the reward obtained after taking the optimal action
 
 ```
 
 #### Perfromance measurement
 
 ```
  Traffic jam time = Sum(waiting time of each car)
  
  ```
  
  

