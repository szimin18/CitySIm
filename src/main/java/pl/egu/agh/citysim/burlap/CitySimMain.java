package pl.egu.agh.citysim.burlap;

import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import pl.egu.agh.citysim.SimulationRunner;


public class CitySimMain {

    public static void main(final String[] args) {

        // stuff we implement - car simulation as each learning step
        final SimulationRunner simulationRunner = new SimulationRunner();
        final CitySimWorld citySimWorld = new CitySimWorld(simulationRunner);
        final SADomain citySimWorldDomain = citySimWorld.generateDomain();

        // stuff for QLearning
        final SimpleHashableStateFactory hashingFactory = new SimpleHashableStateFactory();
        final LearningAgentFactory qLearningFactory = new LearningAgentFactory() {
            @Override
            public String getAgentName() {
                return "Q-learning";
            }

            @Override
            public LearningAgent generateAgent() {
                return new QLearning(citySimWorldDomain, 0.99, hashingFactory, 0.3, 0.1);
            }
        };

        //define learning environment
        // if we don't want to have our environment reset to 3000ms lights after each trial, we can pass stateGenerator
        // as a second argument instead of initialState
        final SimulatedEnvironment env = new SimulatedEnvironment(citySimWorldDomain, simulationRunner.createInitialState());

        //define experiment. TrialLength probably means how many times city-sim will be run,
        // nTrials is for resetting environment and returning to initial state (in our case 3000ms of lights)
        // so it's probably nTrials * trialLength of cit-sim runs
        // oh, and check MAX_NUM_OF_EPOCHS in CitySimState, it's set to 100 and after this state is considered terminal,
        // it might finish simulation early if trialLength is bigger
        final LearningAlgorithmExperimenter exp = new LearningAlgorithmExperimenter(env,
                10, 10, qLearningFactory);

        // we're expecting rewards to get lower after some time on chart, other columns are less important
        exp.setUpPlottingConfiguration(500, 250, 2, 1000,
                TrialMode.MOST_RECENT_AND_AVERAGE,
                PerformanceMetric.CUMULATIVE_STEPS_PER_EPISODE,
                PerformanceMetric.AVERAGE_EPISODE_REWARD);

        //start experiment
        exp.startExperiment();
    }
}
