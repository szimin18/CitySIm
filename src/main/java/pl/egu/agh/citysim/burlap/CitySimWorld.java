package pl.egu.agh.citysim.burlap;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.model.FactoredModel;
import pl.egu.agh.citysim.SimulationRunner;

import java.util.List;

public class CitySimWorld implements DomainGenerator {

    private final SimulationRunner simulationRunner;

    public CitySimWorld(SimulationRunner simulationRunner){
        this.simulationRunner = simulationRunner;
    }

    @Override
    public SADomain generateDomain() {
        final SADomain domain = new SADomain();
        simulationRunner.createAllActions().forEach(domain::addActionType);

        CitySimWorldStateModel stateModel = new CitySimWorldStateModel();
        CitySimRewardFunction rewardFunction = new CitySimRewardFunction(simulationRunner);
        CitySimTerminalFunction terminalFunction = new CitySimTerminalFunction();

        domain.setModel(new FactoredModel(stateModel, rewardFunction, terminalFunction));

        return domain;
    }
}
