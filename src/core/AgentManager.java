package core;

import agent.AgentStatus;
import agent.IAgent;
import main.Sfmt;

public class AgentManager {
	//エージェントの管理を行うクラス

	private final int agent_number = 2;

	private int agent_index = 0;
	private IAgent[] player = new IAgent[agent_number];
	private int[] action = new int[agent_number];
	private Coordinate[] pPos = new Coordinate[agent_number];

	private AgentStatus[] status = {AgentStatus.Active, AgentStatus.Active};
	private int[] brokenTime = {-1, -1};
	private int[] returnTime = {-1, -1};

	//復帰時間
	private final int recoveryTime = 5;
	//無敵時間
	private final int invincibleTime = 3;

	public void addAgent(IAgent agent, int x, int y) {

		player[agent_index] = agent;
		pPos[agent_index] = new Coordinate(x, y);
		agent_index++;

	}

	void checkStatus(int time, Sfmt rnd, IField field) {

		for(int i=0; i<agent_number; i++) {

			//エージェントの復帰
			if(status[i] == AgentStatus.Broken) {
				if(time - brokenTime[i] > recoveryTime) {
					//無敵モードで復帰
					status[i] = AgentStatus.Invincible;
					returnTime[i] = time;
					//ランダムな場所を指定
					int x = (int)(rnd.NextUnif()*field.getWidth());
					int y = (int)(rnd.NextUnif()*field.getHeight());
					pPos[i] = new Coordinate(x, y);
				}
			}

			//無敵モードの終了
			if(status[i] == AgentStatus.Invincible) {
				if(time - returnTime[i] > invincibleTime) {
					//通常モードに戻る
					status[i] = AgentStatus.Active;

				}
			}
		}

	}

	int[] action(IField field, int score0, int score1) {

		//エージェントが行動(action = 0(上移動), 1(右移動), 2(下移動), 3(左移動),
		//4(上攻撃), 5(右攻撃), 6(下攻撃), 7(左攻撃), 8(何もしない))
		action[0] = 8;
		action[1] = 8;

		//現在の座標を更新
		Coordinate c0 = new Coordinate(pPos[0].getX(), pPos[0].getY());
		player[0].setPos(c0);
		Coordinate c1 = new Coordinate(pPos[1].getX(), pPos[1].getY());
		player[1].setPos(c1);

		if(status[0] != AgentStatus.Broken) {
			AgentStatus[] s = new AgentStatus[agent_number];
			System.arraycopy(status, 0, s, 0, status.length);
			action[0] = player[0].action(c1, s[0], s[1], field.getMap(), field.getEvent(), field.getP(), score0, score1);
		}

		if(status[1] != AgentStatus.Broken) {
			AgentStatus[] s = new AgentStatus[agent_number];
			System.arraycopy(status, 0, s, 0, status.length);
			action[1] = player[1].action(c0, s[1], s[0], field.getMap(), field.getEvent(), field.getP(), score1, score0);
		}

		int[] a = new int[agent_number];
		System.arraycopy(action, 0, a, 0, action.length);
		return a;
	}

	void move(IField field) {

		for(int i=0; i<agent_number; i++) {
			int x = pPos[i].getX();
			int y = pPos[i].getY();

			Coordinate new_c;

			switch(action[i]) {
			case 0:		//上移動
				new_c = new Coordinate(x, y+1);
				//移動可能であれば
				if(y+1 < field.getHeight() && field.getMapInfo(pPos[i], new_c) == 1)
					pPos[i] = new_c;

				break;
			case 1:		//右移動
				new_c = new Coordinate(x+1, y);
				//移動可能であれば
				if(x+1 < field.getWidth() && field.getMapInfo(pPos[i], new_c) == 1)
					pPos[i] = new_c;

				break;
			case 2:		//下移動
				new_c = new Coordinate(x, y-1);
				//移動可能であれば
				if(y-1 >= 0 && field.getMapInfo(pPos[i], new_c) == 1)
					pPos[i] = new_c;

				break;
			case 3:		//左移動
				new_c = new Coordinate(x-1, y);
				//移動可能であれば
				if(x-1 >= 0 && field.getMapInfo(pPos[i], new_c) == 1)
					pPos[i] = new_c;

				break;
			}
		}
	}

	void attack(int time) {

		//エージェント0(プレイヤー1)
		if(status[1] == AgentStatus.Active) {
			switch(action[0]) {
			case 4:		//上攻撃
				//攻撃がヒットした場合
				if(pPos[0].getX() == pPos[1].getX() && pPos[0].getY() < pPos[1].getY()) {
					status[1] = AgentStatus.Broken;
					brokenTime[1] = time;
					pPos[1] = new Coordinate(-1, -1);
				}
				break;
			case 5:		//右攻撃
				//攻撃がヒットした場合
				if(pPos[0].getY() == pPos[1].getY() && pPos[0].getX() < pPos[1].getX()) {
					status[1] = AgentStatus.Broken;
					brokenTime[1] = time;
					pPos[1] = new Coordinate(-1, -1);
				}
				break;
			case 6:		//下攻撃
				//攻撃がヒットした場合
				if(pPos[0].getX() == pPos[1].getX() && pPos[0].getY() > pPos[1].getY()) {
					status[1] = AgentStatus.Broken;
					brokenTime[1] = time;
					pPos[1] = new Coordinate(-1, -1);
				}
				break;
			case 7:		//左攻撃
				//攻撃がヒットした場合
				if(pPos[0].getY() == pPos[1].getY() && pPos[0].getX() > pPos[1].getX()) {
					status[1] = AgentStatus.Broken;
					brokenTime[1] = time;
					pPos[1] = new Coordinate(-1, -1);
				}
				break;
			}
		}

		//エージェント1(プレイヤー2)
		if(status[0] == AgentStatus.Active) {
			switch(action[1]) {
			case 4:		//上攻撃
				//攻撃がヒットした場合
				if(pPos[1].getX() == pPos[0].getX() && pPos[1].getY() < pPos[0].getY()) {
					status[0] = AgentStatus.Broken;
					brokenTime[0] = time;
					pPos[0] = new Coordinate(-1, -1);
				}
				break;
			case 5:		//右攻撃
				//攻撃がヒットした場合
				if(pPos[1].getY() == pPos[0].getY() && pPos[1].getX() < pPos[0].getX()) {
					status[0] = AgentStatus.Broken;
					brokenTime[0] = time;
					pPos[0] = new Coordinate(-1, -1);
				}
				break;
			case 6:		//下攻撃
				//攻撃がヒットした場合
				if(pPos[1].getX() == pPos[0].getX() && pPos[1].getY() > pPos[0].getY()) {
					status[0] = AgentStatus.Broken;
					brokenTime[0] = time;
					pPos[0] = new Coordinate(-1, -1);
				}
				break;
			case 7:		//左攻撃
				//攻撃がヒットした場合
				if(pPos[1].getY() == pPos[0].getY() && pPos[1].getX() > pPos[0].getX()) {
					status[0] = AgentStatus.Broken;
					brokenTime[0] = time;
					pPos[0] = new Coordinate(-1, -1);
				}
				break;
			}
		}

	}

	public Coordinate getPos(int player_number) {
		int x = pPos[player_number-1].getX();
		int y = pPos[player_number-1].getY();
		return new Coordinate(x, y);
	}

	public AgentStatus getStatus(int player_number) {
		return status[player_number-1];
	}

}
