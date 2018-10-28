package br.unip.pandora.box.ai;

import br.unip.pandora.box.entity.Creature;
import br.unip.pandora.box.entity.Creature.Action;
import br.unip.pandora.engine.Generator;

public class QBot {
    
    private double alpha = 0.1; // learning rate
    private double gamma = 0.5; // eagerness - 0 looks in the near future, 1 looks in the distant future
    private int variation = 10; //action variation in percentage
    private double[][] qTable; //q(s,a)
    
    private Creature c;
     
    public QBot(Creature c){
	this.c = c;
	qTable = new double[c.getStateCount()][c.getActionCount()];
    }
    
    public void update(){
	if(!c.isMoving()){
	    calculateQ();
	} 
	c.update();
    }
    
    private void calculateQ() {
	int currentState = c.getCurrentState().ordinal();
	int nextAction = Generator.randomBoolean(variation) ? 
		Generator.RANDOM.nextInt(c.getActionCount()) :
		bestAction(c.getCurrentState().ordinal());
	int nextState = c.getNextState(Action.values()[nextAction]).ordinal();

	// Q(state,action)= Q(state,action) + alpha * (R(state,action) + gamma * Max(next state, all actions) - Q(state,action))
	double q = qTable[currentState][nextAction];
	double maxQ = maxQ(nextState);
	int r = c.getReward(Action.values()[nextAction]);

	double value = q + alpha * (r + gamma * maxQ - q);
	qTable[currentState][nextAction] = value;

	c.doAction(Action.values()[nextAction]);
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

    
}
