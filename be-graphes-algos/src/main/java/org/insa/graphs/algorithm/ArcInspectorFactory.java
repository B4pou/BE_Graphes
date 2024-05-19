package org.insa.graphs.algorithm;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Arc;
//import org.insa.graphs.model.GraphStatistics;
import org.insa.graphs.model.AccessRestrictions.AccessMode;
import org.insa.graphs.model.AccessRestrictions.AccessRestriction;

public class ArcInspectorFactory {


    // All arcs allowed, length :
    public static ArcInspector allArcsL = new ArcInspector() {
        @Override
        public boolean isAllowed(Arc arc) {
            return true;
        }

        @Override
        public double getCost(Arc arc) {
            return arc.getLength();
        }

        /*
        @Override
        public int getMaximumSpeed() {
            return GraphStatistics.NO_MAXIMUM_SPEED;
        }
        */

        @Override
        public Mode getMode() {
            return Mode.LENGTH;
        }

        @Override
        public String toString() {
            return "Shortest path, all roads allowed";
        }
    } ;


    // All arcs allowed, time:
    public static ArcInspector allArcsT = new ArcInspector() {

        @Override
        public boolean isAllowed(Arc arc) {
            return true;
        }

        @Override
        public double getCost(Arc arc) {
            return arc.getMinimumTravelTime() ;
        }

        /*
        @Override
        public int getMaximumSpeed() {
            return GraphStatistics.NO_MAXIMUM_SPEED;
        }
        */

        @Override
        public Mode getMode() {
            return Mode.TIME ;
        }

        @Override
        public String toString() {
            return "Fastest path, all roads allowed";
        }
    } ;




     // Only road allowed for cars, length:
     public static ArcInspector forCarsL = new ArcInspector() {
      
        @Override
        public boolean isAllowed(Arc arc) {
            return arc.getRoadInformation().getAccessRestrictions()
                    .isAllowedForAny(AccessMode.MOTORCAR, EnumSet.complementOf(EnumSet
                            .of(AccessRestriction.FORBIDDEN, AccessRestriction.PRIVATE)));
        }

        @Override
        public double getCost(Arc arc) {
            return arc.getLength();
        }

        /*
        @Override
        public int getMaximumSpeed() {
            return GraphStatistics.NO_MAXIMUM_SPEED;
        }
        */

        @Override
        public Mode getMode() {
            return Mode.LENGTH;
        }

        @Override
        public String toString() {
            return "Shortest path, only roads open for cars";
        }
     } ;

     // Only road allowed for cars, time:
     public static ArcInspector forCarsT = new ArcInspector() {
      
        @Override
        public boolean isAllowed(Arc arc) {
            return arc.getRoadInformation().getAccessRestrictions()
                    .isAllowedForAny(AccessMode.MOTORCAR, EnumSet.complementOf(EnumSet
                            .of(AccessRestriction.FORBIDDEN, AccessRestriction.PRIVATE)));
        }

        @Override
        public double getCost(Arc arc) {
            return arc.getMinimumTravelTime();
        }

        /*
        @Override
        public int getMaximumSpeed() {
            return GraphStatistics.NO_MAXIMUM_SPEED;
        }
        */

        @Override
        public Mode getMode() {
            return Mode.TIME;
        }

        @Override
        public String toString() {
            return "Fastest path, only roads open for cars";
        }
     } ;


    

    // Non-private roads for pedestrian and bicycle:
    public static ArcInspector forBicyclesL = new ArcInspector() {
                
        @Override
        public boolean isAllowed(Arc arc) {
            return arc.getRoadInformation().getAccessRestrictions()
                    .isAllowedForAny(AccessMode.FOOT, EnumSet.complementOf(EnumSet
                            .of(AccessRestriction.FORBIDDEN, AccessRestriction.PRIVATE)));
        }

        @Override
        public double getCost(Arc arc) {
            return arc.getLength();
        }

        @Override
        public String toString() {
            return "Shortest path for pedestrian";
        }

        /*
        @Override
        public int getMaximumSpeed() {
            return 5;
        }
        */
        
        @Override
        public Mode getMode() {
            return Mode.LENGTH;
        }
    } ;


    // Non-private roads for pedestrian and bicycle:
    public static ArcInspector forBicyclesT = new ArcInspector() {
            
        static final int maxPedestrianSpeed = 5 ;
        
        @Override
        public boolean isAllowed(Arc arc) {
            return arc.getRoadInformation().getAccessRestrictions()
                    .isAllowedForAny(AccessMode.FOOT, EnumSet.complementOf(EnumSet
                            .of(AccessRestriction.FORBIDDEN, AccessRestriction.PRIVATE)));
        }

        @Override
        public double getCost(Arc arc) {
            return arc.getTravelTime(
                    Math.min(maxPedestrianSpeed, arc.getRoadInformation().getMaximumSpeed()));
        }

        @Override
        public String toString() {
            return "Fastest path for pedestrian";
        }

        /*
        @Override
        public int getMaximumSpeed() {
            return 5;
        }
        */
        
        @Override
        public Mode getMode() {
            return Mode.TIME;
        }
    } ;

    /**
     * @return List of all arc filters in this factory.
     */
    public static List<ArcInspector> getAllFilters() {
        List<ArcInspector> filters = new ArrayList<>();

        // Common filters:

        
        filters.add(allArcsL) ;
        filters.add(allArcsT) ;

        filters.add(forCarsL) ;
        filters.add(forCarsT) ;
        
        filters.add(forBicyclesL);
        filters.add(forBicyclesT);

        // Add your own filters here (do not forget to implement toString()
        // to get an understandable output!):

        return filters;
    }

}
