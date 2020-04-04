package agent;

import core.Coordinate;
import main.Sfmt;

public class ActionA2 implements IAction {

	private Sfmt rnd;

	//ここにフィールドを書く
	private int width = 5;


	public ActionA2(Sfmt rnd) {
		this.rnd = rnd;
	}

	//この関数内に考えたアルゴリズムを記述する
	//乱数を使いたい場合はrnd.NextUnif()で0以上1未満の乱数を得られる
	//(他の範囲の乱数もある。main/Sfmt.java参照)
	@Override
	public int act(Coordinate myPos, Coordinate ePos, AgentStatus myStatus, AgentStatus eStatus,
			int[][] map, int[][] event, double[][] p, int myScore, int eScore) {

		return (int)(rnd.NextUnif()*9);
	}

	//2つの座標間が移動可能かの判定(0:不可能 1:可能)
	private int getMapInfo(int[][] map, Coordinate c1, Coordinate c2) {

		return map[toId(c1)][toId(c2)];
	}

	//座標をidに変換するメソッド
	private int toId(Coordinate c){
		return c.getX() + c.getY() * width;
	}

	//必要に応じてここに追加のメソッドを書く



}
