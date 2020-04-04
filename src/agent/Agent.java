package agent;

import core.Coordinate;

public class Agent implements IAgent {
	private Coordinate myPos;
	private IAction action;


	public Agent(IAction action) {
		this.action = action;
	}

	@Override
	public int action(Coordinate ePos, AgentStatus myStatus, AgentStatus eStatus,
			int[][] map, int[][] event, double[][] p, int myScore, int eScore) {
		return action.act(myPos, ePos, myStatus, eStatus, map, event, p, myScore, eScore);
	}

	@Override
	public void setPos(Coordinate c) {
		myPos = c;
	}

}
