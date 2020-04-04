package core;

import agent.ActionA2;
import agent.ActionB2;
import agent.Agent;
import agent.AgentStatus;
import agent.IAction;
import agent.IAgent;
import main.Sfmt;
import tools.LogExporter;
import tools.LogWriter;

public class VirtualEnvironment2 implements IEnvironment {
	//ex2-4

	private int time = 0;
	private Field2 field;
	private AgentManager agentManager;
	private Sfmt rnd;
	private int sim_number;
	private LogExporter lex;
	private LogWriter graphWriter;
	private LogWriter scoreWriter;

	private int counter = 0;
	private String[][] buffer;

	//エージェント
	private IAgent player1;
	private IAgent player2;

	//プレイヤーのスコア
	private int score1 = 0;
	private int score2 = 0;

	//フィールドの設定
	private final int width = 5;
	private final int height = 5;

	//ログ表示設定
	private boolean print_battle_field;
	private boolean print_score;

	public VirtualEnvironment2(Sfmt rnd, int sim_number, LogExporter lex, boolean print_battle_field, boolean print_score) {
		this.rnd = rnd;
		this.sim_number = sim_number;
		this.lex = lex;
		this.print_battle_field = print_battle_field;
		this.print_score = print_score;
		//変更
		field = new Field2(width, height, rnd);
		agentManager = new AgentManager();

		//環境出力
		graphWriter = this.lex.createWriter("graph");
		printGraph();

		scoreWriter = this.lex.createWriter("score");
		if(print_score)
			scoreWriter.writeLine("time,score1,score2");
	}

	@Override
	public void update() {

		//ログ出力
		if(time % 10000 == 0)
			System.out.println("[" + sim_number + "]: " + time + "steps");

		//イベント発生
		field.happen_event();

		//エージェントのステータス確認と復帰
		agentManager.checkStatus(time, rnd, field);

		//エージェントが次の行動を決定
		int[] action = agentManager.action(field, score1, score2);

		//エージェントが移動する
		agentManager.move(field);

		//エージェントが攻撃する
		agentManager.attack(time);

		//同じマスにいる場合はどちらも報酬は獲得できず報酬が消滅する
		if(agentManager.getPos(1).equals(agentManager.getPos(2)))
			field.get_event(agentManager.getPos(1));
		else {
			if(agentManager.getStatus(1) != AgentStatus.Broken)
				score1 += field.get_event(agentManager.getPos(1));
			if(agentManager.getStatus(2) != AgentStatus.Broken)
				score2 += field.get_event(agentManager.getPos(2));
		}

		//ログ出力(コンソール)
		//それぞれのエージェントがこのターンにとった行動を出力
		System.out.println("action: player1: " + action[0] + ", player2: " + action[1]);
		//それぞれのエージェントのこのターンの座標を出力
		System.out.println("position: player1: (" + agentManager.getPos(1).getX() + "," + agentManager.getPos(1).getY()
				+ "), player2: (" + agentManager.getPos(2).getX() + "," + agentManager.getPos(2).getY() + ")");
		//それぞれのエージェントがこれまでに獲得している総スコアを出力
		System.out.println("score: player1: " + score1 + ", player2: " + score2);
		System.out.println();

		//ログ出力(ファイル)
		//バトルフィールドの出力
		if(print_battle_field)
			printBattleField(agentManager.getPos(1), agentManager.getPos(2), field.getMap(), field.getEvent(), action);
		//スコアの出力
		if(print_score)
			printScore(time, score1, score2);

		//時間更新
		time++;
	}

	@Override
	public void createAgent() {

		//自分のエージェント
		Sfmt rnd1 = new Sfmt(7);
		IAction action1 = new ActionA2(rnd1);
		player1 = new Agent(action1);
		agentManager.addAgent(player1, 0, 0);

		//相手のエージェント
		Sfmt rnd2 = new Sfmt(11);
		IAction action2 = new ActionB2(rnd2);
		player2 = new Agent(action2);
		agentManager.addAgent(player2, width-1, height-1);

	}

	private void printGraph() {
		String[][] map = new String[width+2][height+2];
		double[] p_set = field.get_p_set();
		double[][] p = field.getP();

		for(int i=0; i<width+2; i++) {
			for(int j=0; j<height+2; j++) {
				if(i == 0 || i == width+1 || j == 0 || j == height+1) {
					map[i][j] = String.valueOf(3);
				}
				else {
					map[i][j] = String.valueOf(2);
					for(int k=0; k<p_set.length; k++) {
						if(p[i-1][height-j] == p_set[k]) {
							map[i][j] = String.valueOf(k);
						}
					}
				}
			}
		}

		graphWriter.writeLineMatrix(map);

		System.out.println("Printing graph completed.");
	}

	private void printBattleField(Coordinate pos1, Coordinate pos2, int[][] map, int[][] event, int[] action) {
		String[][] ans_map = new String[2*width+1][2*height+1];

		for(int i=0; i<2*height+1; i++) {
			for(int j=0; j<2*width+1; j++) {
				if(i == 0 || i == 2*height || j == 0 || j == 2*width) {
					ans_map[i][j] = String.valueOf(3);
				}
				else if(i % 2 == 0 && j % 2 == 0) {
					ans_map[i][j] = String.valueOf(3);
				}
				else if(i % 2 != 0 && j % 2 != 0) {
					int value = 2;

					int x = j/2;
					int y = height-1 -i/2;

					//イベント(オレンジ)
					if(event[x][y] == 1) {
						value = 1;
					}

					//プレイヤー１(青)
					if(pos1.getX() == x && pos1.getY() == y) {
						value = 4;
					}

					//プレイヤー２(緑)
					if(pos2.getX() == x && pos2.getY() == y) {
						value = 5;
					}

					ans_map[i][j] = String.valueOf(value);
				}
				else if(j % 2 == 0) {
					int value = 2;

					int x = j/2 - 1;
					int y = height-1 -i/2;

					if(field.getMapInfo(new Coordinate(x, y), new Coordinate(x+1, y)) == 0)
						value = 3;

					ans_map[i][j] = String.valueOf(value);
				}
				else {
					int value = 2;

					int x = j/2;
					int y = height-1 -(i-1)/2;

					if(field.getMapInfo(new Coordinate(x, y), new Coordinate(x, y-1)) == 0)
						value = 3;

					ans_map[i][j] = String.valueOf(value);
				}
			}
		}

		//攻撃(赤)

		//プレイヤー１
		if(agentManager.getStatus(1) != AgentStatus.Broken) {
			//上攻撃
			if(action[0] == 4) {
				int i_max = (height-1 -pos1.getY()) * 2 + 1;
				for(int i=1; i<i_max; i++) {
					ans_map[i][pos1.getX()*2+1] = String.valueOf(0);
				}
			}
			//右攻撃
			if(action[0] == 5) {
				int j_min = pos1.getX()*2 + 2;
				for(int j=j_min; j<2*width; j++) {
					ans_map[(height-1 -pos1.getY()) * 2 + 1][j] = String.valueOf(0);
				}
			}
			//下攻撃
			if(action[0] == 6) {
				int i_min = (height-1 -pos1.getY()) * 2 + 2;
				for(int i=i_min; i<2*height; i++) {
					ans_map[i][pos1.getX()*2+1] = String.valueOf(0);
				}
			}
			//左攻撃
			if(action[0] == 7) {
				int j_max = pos1.getX()*2 + 1;
				for(int j=1; j<j_max; j++) {
					ans_map[(height-1 -pos1.getY()) * 2 + 1][j] = String.valueOf(0);
				}
			}
		}

		//プレイヤー2
		if(agentManager.getStatus(2) != AgentStatus.Broken) {
			//上攻撃
			if(action[1] == 4) {
				int i_max = (height-1 -pos2.getY()) * 2 + 1;
				for(int i=1; i<i_max; i++) {
					ans_map[i][pos2.getX()*2+1] = String.valueOf(0);
				}
			}
			//右攻撃
			if(action[1] == 5) {
				int j_min = pos2.getX()*2 + 2;
				for(int j=j_min; j<2*width; j++) {
					ans_map[(height-1 -pos2.getY()) * 2 + 1][j] = String.valueOf(0);
				}
			}
			//下攻撃
			if(action[1] == 6) {
				int i_min = (height-1 -pos2.getY()) * 2 + 2;
				for(int i=i_min; i<2*height; i++) {
					ans_map[i][pos2.getX()*2+1] = String.valueOf(0);
				}
			}
			//左攻撃
			if(action[1] == 7) {
				int j_max = pos2.getX()*2 + 1;
				for(int j=1; j<j_max; j++) {
					ans_map[(height-1 -pos2.getY()) * 2 + 1][j] = String.valueOf(0);
				}
			}
		}

		String path = lex.makeDir("BattleField");
		LogWriter battleFieldWriter = lex.createWriter(path + "/" + "battleField_" + time);
		battleFieldWriter.writeLineMatrix(ans_map);
	}

	private void printScore(int time, int score1, int score2) {
		//書き込み間隔
		int write_interval = 100;

		int line_number = counter % write_interval;

		//初期化
		if(line_number == 0) {
			buffer = new String[write_interval][3];
		}

		//ログ保存
		buffer[line_number][0] = String.valueOf(time);
		buffer[line_number][1] = String.valueOf(score1);
		buffer[line_number][2] = String.valueOf(score2);

		//ログ書き出し
		if(line_number == write_interval -1) {
			scoreWriter.writeLineMatrix(buffer);
		}

		counter++;

	}

}
