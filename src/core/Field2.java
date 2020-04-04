package core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

import main.Sfmt;

public class Field2 extends Field {
	//ex2-4のフィールドを表すクラス

	private List<IdPair> wall_list = new ArrayList<IdPair>();
	private Deque<Pillar> current_wall_list = new ArrayDeque<Pillar>();
	private List<Pillar> start_pillar_list;

	public Field2(int height, int width, Sfmt rnd){
		this.height = height;
		this.width = width;
		this.rnd = rnd;

		map = new int[width*height][width*height];
		event = new int[width][height];
		p = new double[width][height];

		initializeField();
		//ランダムで壁を生成
		makeRandomWall();
	}

	void makeRandomWall() {
		//壁伸ばし法

		//開始候補の柱の作成
		start_pillar_list = new ArrayList<Pillar>((width+1) * (height+1));
		for(double x=0.5; x<=width-1.5; x++) {
			for(double y=0.5; y<=height-1.5; y++) {
				start_pillar_list.add(new Pillar(x, y));
			}
		}

		//壁が拡張できなくなるまでループ
		while(start_pillar_list.size() > 0) {
			Collections.shuffle(start_pillar_list);
			Pillar p = start_pillar_list.get(0);
			start_pillar_list.remove(0);
			double x = p.x;
			double y = p.y;

			current_wall_list.clear();
			extendWall(x, y);
			start_pillar_list.removeAll(current_wall_list);
		}

		int counter = 0;
		Collections.shuffle(wall_list);
		for(IdPair pair: wall_list) {
			if(counter < 10) {
				map[pair.id1][pair.id2] = 0;
				map[pair.id2][pair.id1] = 0;
				counter++;
			}
		}

	}

	private void extendWall(double x, double y) {


		List<Integer> direction = new ArrayList<Integer>();

		if(map[toId(new Coordinate((int)(x-0.5), (int)(y+0.5)))][toId(new Coordinate((int)(x+0.5), (int)(y+0.5)))] == 1
				&& !current_wall_list.contains(new Pillar(x, y+1))) {
			direction.add(0);
		}
		if(map[toId(new Coordinate((int)(x+0.5), (int)(y-0.5)))][toId(new Coordinate((int)(x+0.5), (int)(y+0.5)))] == 1
				&& !current_wall_list.contains(new Pillar(x+1, y))) {
			direction.add(1);
		}
		if(map[toId(new Coordinate((int)(x-0.5), (int)(y-0.5)))][toId(new Coordinate((int)(x+0.5), (int)(y-0.5)))] == 1
				&& !current_wall_list.contains(new Pillar(x, y-1))) {
			direction.add(2);
		}
		if(map[toId(new Coordinate((int)(x-0.5), (int)(y-0.5)))][toId(new Coordinate((int)(x-0.5), (int)(y+0.5)))] == 1
				&& !current_wall_list.contains(new Pillar(x-1, y))) {
			direction.add(3);
		}

		if(direction.size() > 0) {
			current_wall_list.push(new Pillar(x, y));

			boolean isPath = false;
			int d_index = (int)(rnd.NextUnif() * direction.size());
			switch(direction.get(d_index)) {
			case 0:		//上移動
				if(y+1 < height-1 && !current_wall_list.contains(new Pillar(x, y+1))
						&& start_pillar_list.contains(new Pillar(x, y+1)))
					isPath = true;

				wall_list.add(new IdPair(toId(new Coordinate((int)(x-0.5), (int)(y+0.5))), toId(new Coordinate((int)(x+0.5), (int)(y+0.5)))));
				y++;
				break;
			case 1:		//右移動
				if(x+1 < width-1 && !current_wall_list.contains(new Pillar(x+1, y))
						&& start_pillar_list.contains(new Pillar(x+1, y)))
					isPath = true;

				wall_list.add(new IdPair(toId(new Coordinate((int)(x+0.5), (int)(y-0.5))), toId(new Coordinate((int)(x+0.5), (int)(y+0.5)))));
				x++;
				break;
			case 2:		//下移動
				if(y-1 > 0 && !current_wall_list.contains(new Pillar(x, y-1))
						&& start_pillar_list.contains(new Pillar(x, y-1)))
					isPath = true;

				wall_list.add(new IdPair(toId(new Coordinate((int)(x-0.5), (int)(y-0.5))), toId(new Coordinate((int)(x+0.5), (int)(y-0.5)))));
				y--;
				break;
			case 3:		//左移動
				if(x-1 > 0 && !current_wall_list.contains(new Pillar(x-1, y))
						&& start_pillar_list.contains(new Pillar(x-1, y)))
					isPath = true;

				wall_list.add(new IdPair(toId(new Coordinate((int)(x-0.5), (int)(y-0.5))), toId(new Coordinate((int)(x-0.5), (int)(y+0.5)))));
				x--;
				break;
			}
			if(isPath) {
				extendWall(x, y);
			}
		}
		else {
			Pillar before_p = current_wall_list.pop();
			wall_list.remove(wall_list.size()-1);
			extendWall(before_p.x, before_p.y);
		}
	}

	class Pillar{
		double x;
		double y;

		Pillar(double x, double y){
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object obj) {

			if(obj instanceof Pillar) {
				Pillar p = (Pillar) obj;

				if(p.x == this.x && p.y == this.y)
					return true;
				else
					return false;
			}
			return false;
		}

		@Override
		public int hashCode() {

			return Objects.hash(this.x, this.y);
		}
	}

	class IdPair{
		int id1;
		int id2;

		IdPair(int id1, int id2){
			this.id1 = id1;
			this.id2 = id2;
		}
	}
}
