package by.sivko.miningrigservice.services.configs;

import by.sivko.miningrigservice.models.configs.MinerConfig;

public interface MinerConfigService {

    MinerConfig getMinerConfigById(Long id);
    MinerConfig removeMinerConfigById(Long id);
    void addMinerConfig(MinerConfig minerConfig);
}
