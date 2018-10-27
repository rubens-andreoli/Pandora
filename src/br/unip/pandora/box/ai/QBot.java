package br.unip.pandora.box.ai;

import br.unip.pandora.box.entity.Creature;

public class QBot {
    
    private final double alpha = 0.1; // Learning rate
    private final double gamma = 0.9; // Eagerness - 0 looks in the near future, 1 looks in the distant future
    private double[][] qTable;
    
    private Creature c;
     
    public QBot(Creature c){
	this.c = c;
	qTable = new double[c.getActionCount()][c.getStateCount()];
    }
    
    private void calculateQ() {
	// Pick a random action from the ones possible
//	int index = Generator.RANDOM.nextInt(actionCount);
//	int nextState = actionsFromCurrentState[index];
//
//	// Q(state,action)= Q(state,action) + alpha * (R(state,action) + gamma * Max(next state, all actions) - Q(state,action))
//	double q = Q[crtState][nextState];
//	double maxQ = maxQ(nextState);
//	int r = R[crtState][nextState];
//
//	double value = q + alpha * (r + gamma * maxQ - q);
//	Q[crtState][nextState] = value;
//
//	crtState = nextState;
    }

    
}
