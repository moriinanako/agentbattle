package simulation;

import core.IEnvironment;
import core.VirtualEnvironment;
import main.Sfmt;
import tools.LogExporter;

public class Simulation implements ISimulation {
	//ex2-3

	int sim_step;
	Sfmt rnd;
	IEnvironment env;
	LogExporter lex;

	public Simulation() {

	}

	public Simulation(int seed, int step, int sim_number, String date, boolean print_battle_field, boolean print_score) {
		rnd = new Sfmt(seed);
		sim_step = step;
		lex = new LogExporter("log" + "/" + "ex2-3" + "/" + date);
		env = new VirtualEnvironment(rnd, sim_number, lex, print_battle_field, print_score);
	}

	@Override
	public void start() {

		//エージェントの作成
		env.createAgent();

		for(int time=0; time<sim_step; time++) {
			env.update();
		}
	}

}
