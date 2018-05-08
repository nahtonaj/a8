package student;

import controllers.Spaceship;
import models.Edge;
import models.Node;
import models.NodeStatus;

import controllers.SearchPhase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;

import controllers.RescuePhase;

/** An instance implements the methods needed to complete the mission. */
public class MySpaceship implements Spaceship {
	private HashSet<Integer> visited; //Stores visited planets in search phase
	private LinkedList<Integer> path; //Path taken in search phase
	private HashMap<Node, Boolean> depleted; //Stores planets with depleted gems in rescue phase
	
	/** The spaceship is on the location given by parameter state.
	 * Move the spaceship to Planet X and then return (with the spaceship is on
	 * Planet X). This completes the first phase of the mission.
	 * 
	 * If the spaceship continues to move after reaching Planet X, rather than
	 * returning, it will not count. If you return from this procedure while
	 * not on Planet X, it will count as a failure.
	 *
	 * There is no limit to how many steps you can take, but your score is
	 * directly related to how long it takes you to find Planet X.
	 *
	 * At every step, you know only the current planet's ID, the IDs of
	 * neighboring planets, and the strength of the signal from Planet X at
	 * each planet.
	 *
	 * In this rescuePhase,
	 * (1) In order to get information about the current state, use functions
	 * currentID(), neighbors(), and signal().
	 *
	 * (2) Use method onPlanetX() to know if you are on Planet X.
	 *
	 * (3) Use method moveTo(int id) to move to a neighboring planet with the
	 * given ID. Doing this will change state to reflect your new position.
	 */
	
	@Override
	public void search(SearchPhase state) {
		// TODO: Find the missing spaceship
		// Initialize Map visited and List path and then call recursive search
		visited = new HashSet<Integer>();
		path = new LinkedList<Integer>();
		find(state);
	}
	
	//Recursive function to search for Planet X
	private void find(SearchPhase state) {
		//Base case
		if(state.onPlanetX())
			return;
		//Mark current planet as visited if not already marked
		if(!visited.contains(state.currentID()))
			visited.add(state.currentID());
		//Find the best neighbor to travel to and move to it
		NodeStatus next = bestNeighbor(state);
		path.addFirst(state.currentID());
		state.moveTo(next.id());
		//Recurse from the new planet
		find(state);
	}
	
	//Finds the best neighbor given the current location
	private NodeStatus bestNeighbor(SearchPhase state) {
		//Finds node of max signal that has not been visited
		NodeStatus ns = maxSignalNode(state.neighbors());
		//If all neighbors have been visited, backtrack until there are unvisited neighbors
		while(ns == null) {
			visited.add(state.currentID());
			state.moveTo(path.getFirst());
			path.removeFirst();
			ns = maxSignalNode(state.neighbors());
		}
		return ns;
	}
	
	//Finds the neighbor with max signal that has not been checked
	private NodeStatus maxSignalNode(NodeStatus[] n) {
		NodeStatus best = null;
		for(NodeStatus ns: n) {
			//Ignore all visited neighbors
			if(visited.contains(ns.id())) continue;
			//Find max signal
			if(best == null) best = ns;
			if(ns.compareTo(best) > 0) best = ns;		
		}
		return best;
	}

	/** The spaceship is on the location given by state. Get back to Earth
	 * without running out of fuel and return while on Earth. Your ship can
	 * determine how much fuel it has left via method fuelRemaining().
	 * 
	 * In addition, each Planet has some gems. Passing over a Planet will
	 * automatically collect any gems it carries, which will increase your
	 * score; your objective is to return to earth successfully with as many
	 * gems as possible.
	 * 
	 * You now have access to the entire underlying graph, which can be accessed
	 * through parameter state. Functions currentNode() and earth() return Node
	 * objects of interest, and nodes() returns a collection of all nodes on the
	 * graph.
	 *
	 * Note: Use moveTo() to move to a destination node adjacent to your current
	 * node. */
	@Override
	public void rescue(RescuePhase state) {
		// TODO: Complete the rescue mission and collect gems
		// Initialize the Map depleted and call the recursive function to get back to Earth
		depleted = new HashMap<Node, Boolean>();
		backToEarth(state);
	}
	
	//Recursive function to get back to Earth
	private void backToEarth(RescuePhase state) {
		//Base case
		if(state.currentNode() == state.earth()) {
			return;
		}
		//Find best node to travel to, move there and mark this node as depleted
		Node n = bestNode(state);
		depleted.put(state.currentNode(), true);
		state.moveTo(n);
		//Recurse from the new planet
		backToEarth(state);
	}
	
	private Node bestNode(RescuePhase state) {
		//Get map of neighbors to this node
		HashMap<Node, Integer> neighbors = state.currentNode().neighbors();
		//Initialize values used to determine which node is best
		double max = 0;
		double g = 0;
		double minlength = 0;
		double edge = 0;
		double num = 0;
		double index = 0;
		Node best = null;
		//Iterate through the neighbors
		for(Node n: neighbors.keySet()) {
			//Set g to the number of gems currently on the neighbor
			if(depleted.containsKey(n)) {
				g = 0;
			}
			else {
				g = n.gems();
			}
			//Find the minPath, the minPath length, and number of planets on the
			//minPath back to Earth
			LinkedList<Node> ll = (LinkedList<Node>) Paths.minPath(n, state.earth());
			minlength = Paths.pathWeight(ll);
			num = ll.size();
			//Get distance from this node to the current neighbor being looked at
			edge = neighbors.get(n);
			//If you cannot get back to Earth from this neighbor, ignore it
			if(edge + minlength > state.fuelRemaining()) {
				continue;
			}
			//Calculate an index for the neighbors based on how far it is, how
			//many gems it has, and its path back to Earth. Weights (determined
			//by powers) found by optimizing after many different combinations
			index = (Math.pow(g, 1)*Math.pow(minlength, 50)*Math.pow(num, 1))/edge;
			//Find max index and set the best node to the neighbor with max index
			if(index > max) {
				max = index;
				best = n;
			}
		}
		//If a best neighbor cannot be found or there is no other path back to
		//Earth, take the shortest path back
		if(best == null) {
			LinkedList<Node> backup = (LinkedList<Node>) Paths.minPath(state.currentNode(), state.earth());
			backup.removeLast();
			return backup.getLast();
		}
		return best;
	}
	
	//Calculate pathLength of a given path
//	private double pathLength(LinkedList<Node> path) {
//		Iterator<Node> it1 = path.descendingIterator();
//		Iterator<Node> it2 = path.descendingIterator();
//		it2.next();
//		double sum = 0;
//		while(it2.hasNext()) {
//			sum += it1.next().getEdge(it2.next()).length;
//		}
//		return sum;
//	}
}
