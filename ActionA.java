package agent;

import core.Coordinate;
import main.Sfmt;

public class ActionA implements IAction {

	private Sfmt rnd;

	//ここにフィールドを書く
	private int width = 5;
	private int height = 5;
	//報酬までの距離
	private int pointMH[][] = new int[width][height];
	private int minx=-1,miny=-1,min=20;


	public ActionA(Sfmt rnd) {
		this.rnd = rnd;
	}

	//この関数内に考えたアルゴリズムを記述する
	//乱数を使いたい場合はrnd.NextUnif()で0以上1未満の乱数を得られる
	//(他の範囲の乱数もある。main/Sfmt.java参照)
	@Override
	public int act(Coordinate myPos, Coordinate ePos, AgentStatus myStatus, AgentStatus eStatus,
			int[][] map, int[][] event, double[][] p, int myScore, int eScore) {
		int x = myPos.getX();
		int y = myPos.getY();


		/*
		//フィールドの表示
		for(int i=height-1;i>=0;i--) {
			for (int j = 0; j < width; j++) {
			    if(j==x&&i==y) System.out.print("■");
				if(event[j][i]==1) System.out.print("*");
				else System.out.print("-");
			}
			System.out.println();
		}
		*/

		minx=-1;miny=-1;min=20;


		//報酬までの距離を算出(次狙う報酬の決定）
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){

				if(event[i][j]==1){
					pointMH[i][j]=GetManhattan(x,y,i,j);
					if(pointMH[i][j]<=min){
						min=pointMH[i][j];
						minx=i;
						miny=j;
					}
				}else{
					pointMH[i][j]=20;
				}
			}
		}


		//System.out.println("minx: "+minx+"  miny: "+miny+" x:"+x+"y"+y);
		if(minx>x) return 1;
		else if(minx<x) return 3;
		else if(miny>y) return 0;
		else if(miny<y) return 2;
		else return (int)(rnd.NextUnif()*9);
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

	//２点間の移動距離を調べる
	private int GetManhattan (int x1, int y1, int x2, int y2) {
		int d = Math.abs(x1-x2)+Math.abs(y1-y2);
		return d;
	}
}
