package core;

public interface IField {

	public int getMapInfo(Coordinate c1, Coordinate c2);

	public int[][] getMap();

	public int[][] getEvent();

	public double[][] getP();

	public int getWidth();

	public int getHeight();

	public double[] get_p_set();

}
