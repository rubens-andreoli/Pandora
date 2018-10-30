package br.unip.pandora.box.ai;

import br.unip.pandora.box.entity.Creature;
import br.unip.pandora.box.entity.Creature.Action;
import br.unip.pandora.box.entity.Creature.State;
import br.unip.pandora.engine.Generator;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class QBot {
    
    private double alpha = 0.1; // learning rate
    private double gamma = 0.8; // 0 doesn't look to future, 1 looks in the distant future
    private double epsilon = 0.1; //0 exploitation, 1 exploration
    private double[][] qTable; //q[state][action]
    
    private Creature c;
     
    public QBot(Creature c){
	this.c = c;
	qTable = new double[c.getStateCount()][c.getActionCount()];
    }
    
    public void update(){
	if(!c.isMoving()){
	    c.doAction(calculateQ());
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
	try(BufferedWriter br = new BufferedWriter(new FileWriter("QTABLE.TXT"))){
	    for(Action a : Action.values()){
		br.write(a.name()+" ");
	    }
	    br.write("\n");
	    for(int s=0; s<qTable.length; s++){
		br.write(State.values()[s].name());
		for(int a=0; a<qTable[s].length; a++){
		    br.write(String.format(" %.2f;", qTable[s][a]));
		}
		br.write("\n");
	    }
	} catch (IOException ex) {}
    }
}
