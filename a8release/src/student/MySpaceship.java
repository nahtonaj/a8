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
	private HashMap<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
	private LinkedList<Integer> path = new LinkedList<Integer>();

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
		if(state.onPlanetX())
			return;
		if(!visited.containsKey(state.currentID()))
			visited.put(state.currentID(), true);
		NodeStatus ns = max(state.neighbors(), state);
		while(ns == null) {
			visited.put(state.currentID(), true);
			state.moveTo(path.getFirst());
			path.removeFirst();
			ns = max(state.neighbors(), state);
		}
		path.addFirst(state.currentID());
		state.moveTo(ns.id());
		search(state);
		
	}
	
	private NodeStatus max(NodeStatus[] n, SearchPhase state) {
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
		LinkedList<Node> ll = (LinkedList) Paths.minPath(state.currentNode(), state.earth());
		ll.removeLast();
		int d = ll.size();
		for(int i = 0; i < d; i++) {
			state.moveTo(ll.getLast());
			ll.removeLast();
		}
		return;
	}
}
