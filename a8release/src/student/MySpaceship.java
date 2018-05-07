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

import controllers.RescuePhase;

/** An instance implements the methods needed to complete the mission. */
public class MySpaceship implements Spaceship {
	private HashMap<Integer, Boolean> visited;
	private LinkedList<Integer> path;
	private HashMap<Node, Boolean> depleted;
	
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
		visited = new HashMap<Integer, Boolean>();
		path = new LinkedList<Integer>();
		find(state);
	}
	
	private void find(SearchPhase state) {
		if(state.onPlanetX())
			return;
		if(!visited.containsKey(state.currentID()))
			visited.put(state.currentID(), true);
		NodeStatus next = bestNeighbor(state);
		path.addFirst(state.currentID());
		state.moveTo(next.id());
		find(state);
	}
	
	private NodeStatus bestNeighbor(SearchPhase state) {
		NodeStatus ns = maxSignalNode(state.neighbors());
		while(ns == null) {
			visited.put(state.currentID(), true);
			state.moveTo(path.getFirst());
			path.removeFirst();
			ns = maxSignalNode(state.neighbors());
		}
		return ns;
	}
	
	private NodeStatus maxSignalNode(NodeStatus[] n) {
		NodeStatus best = null;
		for(NodeStatus ns: n) {
			if(visited.containsKey(ns.id()))
				continue;
			if(best == null) best = ns;
			if(ns.compareTo(best) > 0)
				best = ns;
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
<<<<<<< HEAD

=======
>>>>>>> 21a27b9a8a92edaf146c4492394b84bd703a9798
		depleted = new HashMap<Node, Boolean>();
		backToEarth(state);
	}
	
	private void backToEarth(RescuePhase state) {
		if(state.currentNode() == state.earth()) {
			return;
		}
		Node n = bestNode(state);
		depleted.put(state.currentNode(), true);
		state.moveTo(n);
		backToEarth(state);
	}
	
	private Node bestNode(RescuePhase state) {
		HashMap<Node, Integer> neighbors = state.currentNode().neighbors();
		double max = 0;
		double g = 0;
		double minlength = 0;
		double edge = 0;
		double num = 0;
		double index = 0;
		Node best = null;
		for(Node n: neighbors.keySet()) {
			if(depleted.containsKey(n)) {
				g = 0;
			}
			else {
				g = n.gems();
			}
			LinkedList<Node> ll = (LinkedList) Paths.minPath(n, state.earth());
			minlength = pathLength(ll);
			num = ll.size();
			edge = neighbors.get(n);
			if(edge + minlength > state.fuelRemaining()) {
				continue;
			}
			index = (Math.pow(g, 1)*Math.pow(minlength, 50)*Math.pow(num, 1))/edge;
			if(index > max) {
				max = index;
				best = n;
			}
		}
		if(best == null) {
			LinkedList<Node> backup = (LinkedList) Paths.minPath(state.currentNode(), state.earth());
			backup.removeLast();
			return backup.getLast();
		}
		return best;
	}
	
	private double pathLength(LinkedList<Node> path) {
		Iterator<Node> it1 = path.descendingIterator();
		Iterator<Node> it2 = path.descendingIterator();
		it2.next();
		double sum = 0;
		while(it2.hasNext()) {
			sum += it1.next().getEdge(it2.next()).length;
		}
		return sum;
	}
<<<<<<< HEAD

=======
>>>>>>> 21a27b9a8a92edaf146c4492394b84bd703a9798
}
