package pl.egu.agh.citysim.burlap;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.model.FactoredModel;

public class CitySimWorld implements DomainGenerator {

    @Override
    public SADomain generateDomain() {
        final SADomain domain = new SADomain();

        domain.addActionTypes();

        CitySimWorldStateModel stateModel = new CitySimWorldStateModel();
        CitySimRewardFunction rewardFunction = new CitySimRewardFunction();
        CitySimTerminalFunction terminalFunction = new CitySimTerminalFunction();

        domain.setModel(new FactoredModel(stateModel, rewardFunction, terminalFunction));

        return domain;
    }
}
