package data_generation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.api.core.v01.population.PopulationWriter;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;


public class PopGeneration {

    public static void main(String args[]) {

        /*
         * Create population from sample input data.
         */
//        Scenario scenario = createPopulationFromFile("C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\resources\\EvacuationPlan_orig.csv");
//        Scenario scenario = createPopulationFromFile("C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\resources\\EvacuationPlan_covid.csv");
//        Scenario scenario = createPopulationFromFile("C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\resources\\Updated_Inputs\\EvacuationPlan_orig.csv");
        Scenario scenario = createPopulationFromFile("C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\resources\\Updated_Inputs\\EvacuationPlan_covid.csv");
        /*  "C:\\Users\\yuh46\\Desktop\\Tampa\\ReadResults\\GeneratedTrips.csv"
         * Write population to file.
         */
        PopulationWriter populationWriter = new PopulationWriter(scenario.getPopulation(), scenario.getNetwork());
//        populationWriter.write("C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\java\\Scenario1\\SFLpopulation12.xml");
        populationWriter.write("C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\java\\Scenario2\\SFLpopulation22.xml");
//        populationWriter.write("C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\java\\Scenario3\\SFLpopulation32.xml");
        populationWriter.write("C:\\Users\\HanY1\\Documents\\matsim-example-project\\src\\main\\java\\Scenario4\\SFLpopulation42.xml");
    }

    private static Scenario createPopulationFromFile(String EvacuationFile) {
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());

        List<AgentInfo> TripEntries = new AgentTrips().readFile( EvacuationFile );

        Population population = scenario.getPopulation();
        PopulationFactory populationFactory = population.getFactory();

        Map<Integer, List<AgentInfo>> personEntryMapping = new TreeMap<>();

        for (AgentInfo tripEntry : TripEntries) {
            List<AgentInfo> entries = personEntryMapping.get(tripEntry.id_person);
            /*
             * If no mapping exists -> create a new one
             */
            if (entries == null) {
                entries = new ArrayList<>();
                personEntryMapping.put(tripEntry.id_person, entries);
            }
            entries.add(tripEntry);
        }

        for (List<AgentInfo> personEntries : personEntryMapping.values()) {
            AgentInfo entry = personEntries.get(0);
            /*
             * Get id of the person from the censusEntry.
             */
            int idPerson = entry.id_person;
            Person person = populationFactory.createPerson(Id.create(idPerson, Person.class));
            population.addPerson(person);
            Plan plan = populationFactory.createPlan();
            person.addPlan(plan);
            Coord homeCoord = new Coord(entry.h_x, entry.h_y);
            Activity homeActivity = populationFactory.createActivityFromCoord("home", homeCoord);
            homeActivity.setStartTime(0.0);
            plan.addActivity(homeActivity);
            Coord endCoord = null;
            String transportMode = null;
            Leg leg = null;
            Activity activity = null;
            Activity previousActivity = homeActivity;

            for (AgentInfo personEntry : personEntries) {
                endCoord            = new Coord(personEntry.d_x, personEntry.d_y);
                transportMode       = getTransportMode( personEntry.tripmode );
                String activityType = getActivityType(personEntry.trippurpose);

                leg     = populationFactory.createLeg(transportMode);
                leg.setDepartureTime(personEntry.starttime );
                leg.setTravelTime(personEntry.tripduration );
                previousActivity.setEndTime(personEntry.endtime);

                activity = populationFactory.createActivityFromCoord(activityType, endCoord);
                activity.setStartTime(personEntry.starttime + personEntry.tripduration );

                plan.addLeg(leg);
                plan.addActivity(activity);

//				leg = populationFactory.createLeg(transportMode);
//				leg.setDepartureTime(personEntry.endtime * 60);
//				leg.setTravelTime(personEntry.tripduration * 60);
//				previousActivity.setEndTime(personEntry.endtime);
//
//				activity = populationFactory.createActivityFromCoord("home", homeCoord);
//				activity.setStartTime(personEntry.endtime * 60 + personEntry.tripduration * 60);
//				plan.addLeg(leg);
//				plan.addActivity( activity );

                previousActivity = activity;
            }
            if (activity.getCoord().equals(homeCoord)) {
                activity.setType("home");
            }
        }
        return scenario;

    }

    private static String getTransportMode(int mode) {
        switch (mode) {
            case 1: return TransportMode.walk;
            case 2: return TransportMode.bike;
            case 3: return TransportMode.car;
            case 4: return TransportMode.pt;
            case 5: return "undefined";
            default: return "undefined";
        }
    }

    private static String getActivityType(int activityType) {
        switch (activityType) {
            case 1: return "evacuation";
            case 2: return "education";
            case 3: return "shop";
            case 4: return "leisure";
            case 5: return "other";
            default: return "undefined";
        }
    }

}
