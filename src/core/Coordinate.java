package core;

import java.util.Objects;

public class Coordinate {
	//座標を表すクラス

	private int x;
	private int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object obj) {

		if(obj instanceof Coordinate) {
			Coordinate c = (Coordinate) obj;

			if(c.getX() == this.getX() && c.getY() == this.getY())
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
