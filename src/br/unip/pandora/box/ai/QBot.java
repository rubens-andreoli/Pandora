package br.unip.pandora.box.ai;

import br.unip.pandora.box.entity.Creature;
import br.unip.pandora.box.entity.Creature.Action;
import br.unip.pandora.box.entity.Creature.State;
import br.unip.pandora.engine.Generator;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class QBot {
    
    /*Learning Rate: 
    * Alpha determines to what extent the newly acquired information will override the old information. 
    * A factor of 0 will make the agent not learn anything; 
    * A factor of 1 would make the agent consider only the most recent information.
    */
    private double alpha = 0.1; 
    /*Discount Factor:
    * Gamma determines the importance of future rewards. 
    * A factor of 0 will make the agent short-sighted by only considering current rewards; 
    * A factor approaching 1 will make it strive for a long-term high reward. 
    */
    private double gamma = 0.7;
    /*Exploration vs Exploitation:
    * Epsilon-greedy algorithms chooses the action that has the best reward with probability 1-epsilon;
    * Otherwise, a new action is explored at random with probability epsilon.
    */
    private double epsilon = 0.1;
    private double[][] qTable; //q[state][action]
    private int episodes;
    
    private Creature c;
     
    public QBot(Creature c){
	this.c = c;
	qTable = new double[c.getStateCount()][c.getActionCount()];
    }
    
    public void update(){
	if(!c.isMoving()){
	    c.doAction(calculateQ());
	    episodes++;
	} 
	c.update();
    }
    
    //Q-Learning: model-free approach to Markov Decision Process (MDP)
    private Action calculateQ(){
	int currentState = c.getCurrentState().ordinal();
	int nextAction = Generator.randomBoolean(epsilon) ? //epsilon-greedy:
		Generator.RANDOM.nextInt(c.getActionCount()) : //exploration
		bestAction(c.getCurrentState().ordinal()); //exploitation
	int nextState = c.getNextState(Action.values()[nextAction]).ordinal();

	// Time-Difference Learning                   |                      LEARNED                        |  |      OLD      |
	// Q(state,action)= Q(state,action) + alpha * (R(state,action) + gamma * Max(next state, all actions) - Q(state,action))
	double qValue = qTable[currentState][nextAction];
	int reward = c.getReward(Action.values()[nextAction]);
	double maxQ = maxQ(nextState);
	qTable[currentState][nextAction] = qValue + alpha * (reward + gamma * maxQ - qValue);

	return Action.values()[nextAction];
    }
    
    private int bestAction(int state){
	int bestA = 0;
	double qVal = qTable[state][bestA];
	for(int i=1; i<qTable[state].length; i++){
	    if(qTable[state][i] > qVal){
		qVal = qTable[state][i];
		bestA = i;
	    }
	}
	return bestA;
    }
    
    private double maxQ(int nextState){
	double maxQ = qTable[nextState][0];
	for(int i=1; i<qTable[nextState].length; i++){
	    if(qTable[nextState][i] > maxQ) maxQ = qTable[nextState][i];
	}
	return maxQ;
    }

    public void saveQTable(){
	String lineBreak = System.getProperty("line.separator");
	try(BufferedWriter br = new BufferedWriter(new FileWriter("QTABLE.TXT"))){
	    br.write("Nº Episodes: "+episodes+lineBreak+"\t\t");
	    for(Action a : Action.values()){
		br.write(a.name()+"\t");
	    }
	    br.write(System.getProperty("line.separator"));
	    for(int s=0; s<qTable.length; s++){
		br.write(String.format("%10s", State.values()[s].name()));
		for(int a=0; a<qTable[s].length; a++){
		    br.write(String.format("\t%.2f\t", qTable[s][a]));
		}
		br.write(lineBreak);
	    }
	} catch (IOException ex) {}
    }
}
