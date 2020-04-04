package main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import simulation.ISimulation;
import simulation.Simulation;
import simulation.Simulation2;

public class Launcher {
	private static final int[] seeds = new int[]{ 7, 11, 17, 19, 31, 57, 123, 203, 223, 331,
			347, 407, 431, 473, 523, 559, 601, 687, 657,
			713, 761, 777, 809, 867, 897, 923, 957, 971,
			1001, 1013, 1053, 1123, 1157, 1271, 1303,1307,
			1333, 1361, 1367, 1373, 1381, 1399, 1409, 1423,
			1427, 1429, 1433, 1439, 1447, 1451, 1453, 1459,
			1471, 1481, 1483, 1487, 1489, 1493, 1499, 1511,
			1523, 1531, 1543, 1549, 1553, 1559, 1567, 1571,
			1579, 1583, 1597, 1601, 1607, 1609, 1613, 1619,
			1621, 1627, 1637, 1657, 1663, 1667, 1669, 1693,
			1697, 1699, 1709, 1721, 1723, 1733, 1741, 1747,
			1753, 1759, 1777, 1783, 1787, 1789, 1801, 1811,
			1823, 1831, 1847, 2617, 2621, 2633, 2647, 2657,
			2659, 2663, 2671, 2677, 2683, 2687, 2689, 2693,
			2699, 2707, 2711, 2713, 2719, 2729, 2731, 2741,
			2749, 2753, 2767, 2777, 2789, 2791, 2797, 2801,
			2803, 2819, 2833, 2837, 2843, 3917, 3919, 3923,
			3929, 3931, 3943, 3947, 3967, 3989, 4001, 4003,
			4007 };

	//ここがスタート
	public static void main(String[] args) {

		System.out.println("Game start!");

		//ex2-3
		simulation1();
//		//ex2-4
//		simulation2();

		System.out.println("Game finish!");

	}

	private static void simulation1() {

		//シミュレーションの設定(ここの数字を必要に応じて変える)
		//シミュレーション自体の回数
		int simulation_time = 1;		//default:1
		//1回のシミュレーション内のステップ数
		int step = 100000;				//default:100000

		//ログ出力(必要な時以外はfalseにすることをお勧めします)(ログを出力すると遅くなるので)
		//バトルフィールドを出力するか
		boolean print_battle_field = true;
		//スコアの推移を出力するか
		boolean print_score = true;

		for(int i=0; i<simulation_time; i++) {
			ISimulation simulation = new Simulation(seeds[i], step, i,
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd HH-mm-ss")),
					print_battle_field, print_score);
			simulation.start();
		}
	}

	private static void simulation2() {

		//シミュレーションの設定(ここの数字を必要に応じて変える)
		//シミュレーション自体の回数
		int simulation_time = 1;		//default:1
		//1回のシミュレーション内のステップ数
		int step = 100000;				//default:100000

		//ログ出力(必要な時以外はfalseにすることをお勧めします)(ログを出力すると遅くなるので)
		//バトルフィールドを出力するか
		boolean print_battle_field = true;
		//スコアの推移を出力するか
		boolean print_score = true;

		for(int i=0; i<simulation_time; i++) {
			ISimulation simulation = new Simulation2(seeds[i], step, i,
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd HH-mm-ss")),
					print_battle_field, print_score);
			simulation.start();
		}
	}

}
