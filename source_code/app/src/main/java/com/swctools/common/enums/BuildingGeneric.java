package com.swctools.common.enums;

public enum BuildingGeneric {

    //3x3:
    CONTRABANDCANTINA("ContrabandCantina") {
        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isTrap() {
            return false;
        }
    },
    CREDITGENERATOR("CreditGenerator") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    MATERIALSSTORAGE("MaterialsStorage") {
        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isTrap() {
            return false;
        }
    },
    NAVIGATIONCENTER("NavigationCenter") {
        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isTrap() {
            return false;
        }
    },
    MATERIALSGENERATOR("MaterialsGenerator") {
        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isTrap() {
            return false;
        }
    },
    CREDITSTORAGE("CreditStorage") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    OFFENSELAB("OffenseLab") {
        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isTrap() {
            return false;
        }
    },
    CONTRABANDGENERATOR("ContrabandGenerator") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    CONTRABANDSTORAGE("ContrabandStorage") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    BARRACKS("Barracks") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    STARPORT("Starport") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },


    //4x4:
    ARMORY("Armory") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    TACTICALCOMMAND("TacticalCommand") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    FLEETCOMMAND("FleetCommand") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    PLATFORMDROIDEKA("PlatformDroideka") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    PLATFORMHEAVYDROIDEKA("PlatformHeavyDroideka") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },


    //traps:
    TRAPDROPSHIP("TrapDropship") {
        @Override
        public boolean isTrap() {
            return true;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    TRAPSTRIKEAOE("TrapStrikeAOE") {
        @Override
        public boolean isTrap() {
            return true;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    TRAPSTRIKEGENERIC("TrapStrikeGeneric") {
        @Override
        public boolean isTrap() {
            return true;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    TRAPSTRIKEHEAVY("TrapStrikeHeavy") {
        @Override
        public boolean isTrap() {
            return true;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    //rebelTrapDropshipCreature1
    TRAPDROPSHIPCREATURE("TrapDropshipCreature") {
        @Override
        public boolean isTrap() {
            return true;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },

    //turrets:
    BURSTTURRET("BurstTurret") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    RAPIDFIRETURRET("RapidFireTurret") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    MORTARTURRET("Mortar") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    SONICTURRET("SonicTurret") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    ROCKETTURRET("RocketTurret") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    //OTHER CRAP
    FACTORY("Factory") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    SHEILDGENERATOR("ShieldGenerator") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    DROIDHUT("DroidHut") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    SCOUTTOWER("ScoutTower") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    WALL("Wall") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    HQ("HQ") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    SQUADBUILDING("SquadBuilding") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    },
    //JUNK:
    ROCKSMALL("rockSmall") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return true;
        }
    },
    ROCKMEDIUM("rockMedium") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return true;
        }
    },
    ROCKLARGE("rockLarge") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return true;
        }
    },
    JUNKSMALL("junkSmall") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return true;
        }
    },
    JUNKMEDIUM("junkMedium") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return true;
        }
    },
    JUNKLARGE("junkLarge") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return true;
        }
    },
    ALPHA("Alpha") {
        @Override
        public boolean isTrap() {
            return false;
        }

        @Override
        public boolean isJunk() {
            // TODO Auto-generated method stub
            return false;
        }
    };

    private final String name;

    public abstract boolean isJunk();

    public abstract boolean isTrap();

    BuildingGeneric(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
