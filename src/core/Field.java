package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.Sfmt;

public class Field implements IField {
	//ex2-3のフィールドを表すクラス

	int height;
	int width;
	Sfmt rnd;

	int[][] map;
	int[][] event;
	double[][] p;

	double[] p_set = {0.05, 0.01, 0.001};

	public Field() {

	}

	public Field(int height, int width, Sfmt rnd){
		this.height = height;
		this.width = width;
		this.rnd = rnd;

		map = new int[width*height][width*height];
		event = new int[width][height];
		p = new double[width][height];

		initializeField();
	}

	void happen_event() {

		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				if(rnd.NextUnif() < p[x][y]){
					event[x][y] = 1;
				}
			}
		}

	}

	int get_event(Coordinate c) {

		int x = c.getX();
		int y = c.getY();

		int amount = event[x][y];
		event[x][y] = 0;
		return amount;

	}

	@Override
	public int getMapInfo(Coordinate c1, Coordinate c2) {

		return map[toId(c1)][toId(c2)];
	}

	@Override
	public int[][] getMap(){
		int[][] m = new int[width*height][width*height];
		for(int i=0; i<width*height; i++) {
			System.arraycopy(map[i], 0, m[i], 0, map[i].length);
		}
		return m;
	}

	@Override
	public int[][] getEvent(){
		int[][] e = new int[width][height];
		for(int i=0; i<width; i++) {
			System.arraycopy(event[i], 0, e[i], 0, event[i].length);
		}
		return e;
	}

	@Override
	public double[][] getP(){
		double[][] pro = new double[width][height];
		for(int i=0; i<width; i++) {
			System.arraycopy(p[i], 0, pro[i], 0, p[i].length);
		}
		return pro;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	void initializeField() {

		//マップの初期化
		for(int id=0; id<width*height; id++) {
			for(int id2=0; id2<width*height; id2++) {
				map[id][id2] = 0;
			}
		}

		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				Coordinate c = new Coordinate(x, y);

				if(x+1 < width) {
					map[toId(c)][toId(new Coordinate(x+1, y))] = 1;
					map[toId(new Coordinate(x+1, y))][toId(c)] = 1;
				}
				if(y+1 < height) {
					map[toId(c)][toId(new Coordinate(x, y+1))] = 1;
					map[toId(new Coordinate(x, y+1))][toId(c)] = 1;
				}
			}
		}

		//イベントの初期化
		for(int x=0; x<width; x++) {
			for(int y=0; y<height; y++) {
				event[x][y] = 0;
			}
		}

		//確率の初期化
		double[] ratio = {0.12, 0.48, 0.4};
		List<Double> p_list = new ArrayList<Double>(width*height);

		int n = 0;
		int p_n = 1;
		for(int i=0; i<p_set.length; i++) {
			if(i == p_set.length-1)
				p_n = width * height - n;
			else {
				p_n = (int)(width * height * ratio[i]);
				n += p_n;
			}

			for(int j=0; j<p_n; j++) {
				p_list.add(p_set[i]);
			}
		}

		Collections.shuffle(p_list);

		for(int x=0; x<width; x++) {
			for(int y=0; y<height; y++) {
				p[x][y] = p_list.get(toId(new Coordinate(x, y)));
			}
		}

	}

	int toId(Coordinate c){
		return c.getX() + c.getY() * width;
	}

	@Override
	public double[] get_p_set() {
		double[] ps = new double[p_set.length];
		System.arraycopy(p_set, 0, ps, 0, p_set.length);
		return ps;
	}

}
