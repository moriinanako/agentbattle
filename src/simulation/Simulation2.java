package simulation;

import core.VirtualEnvironment2;
import main.Sfmt;
import tools.LogExporter;

public class Simulation2 extends Simulation {
	//ex2-4

	public Simulation2(int seed, int step, int sim_number, String date, boolean print_battle_field, boolean print_score) {
		rnd = new Sfmt(seed);
		sim_step = step;
		lex = new LogExporter("log" + "/" + "ex2-4" + "/" + date);
		env = new VirtualEnvironment2(rnd, sim_number, lex, print_battle_field, print_score);
	}

}
