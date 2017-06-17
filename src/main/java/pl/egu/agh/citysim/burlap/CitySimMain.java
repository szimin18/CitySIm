package pl.egu.agh.citysim.burlap;

import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.statehashing.simple.SimpleHashableStateFactory;

import static com.google.common.collect.Maps.newHashMap;


public class CitySimMain {

	public static void main(String [] args){

		final CitySimWorld citySimWorld = new CitySimWorld();
		final SADomain citySimWorldDomain = citySimWorld.generateDomain();

        State initialState = new CitySimState(newHashMap());

		//set up the state hashing system for looking up states - ???
		final SimpleHashableStateFactory hashingFactory = new SimpleHashableStateFactory();

		/**
		 * Create factory for Q-learning agent
		 */
		LearningAgentFactory qLearningFactory = new LearningAgentFactory() {

			public String getAgentName() {
				return "Q-learning";
			}

			public LearningAgent generateAgent(){
				return new QLearning(citySimWorldDomain, 0.99, hashingFactory, 0.3, 0.1);
			}
		};

		//define learning environment
		SimulatedEnvironment env = new SimulatedEnvironment(citySimWorldDomain, initialState);

		//define experiment
		LearningAlgorithmExperimenter exp = new LearningAlgorithmExperimenter(env,
				10, 100, qLearningFactory);

		exp.setUpPlottingConfiguration(500, 250, 2, 1000,
				TrialMode.MOST_RECENT_AND_AVERAGE,
				PerformanceMetric.CUMULATIVE_STEPS_PER_EPISODE,
				PerformanceMetric.AVERAGE_EPISODE_REWARD);


		//start experiment
		exp.startExperiment();


	}

}
