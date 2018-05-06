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
			if(!visited.containsKey(ns.id())) {
				if(best == null) best = ns;
				else if(ns.compareTo(best) > 0) {
					best = ns;
				}
			}
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
		LinkedList<Node> shortestPathtoEarth = (LinkedList<Node>) Paths.minPath(state.currentNode(), state.earth());
		LinkedList<Node> gemPath = addGemPath(shortestPathtoEarth, state);
		gemPath.poll();
		while(gemPath != null && !gemPath.isEmpty()) {
			state.moveTo(gemPath.poll());
		}
		
	}
	
	public LinkedList<Node> addGemPath(LinkedList<Node> ll, RescuePhase state) {
		if(ll.isEmpty()) {
			return null;
		}
		int sumLengths = 0;
		int maxLength = 0;
		int maxLengthIndex = 0;
		int currLength = 0;
		int maxGemIndex = 0;
		int sumGems = 0;
		int gemfuelratio = 1;
		int currGems = ll.get(0).gems();
		int maxGems = currGems;
		LinkedList<Integer> lengths = new LinkedList<Integer>();
		
		Node currNode = null;
//		for(int i=1; i<ll.size(); i++) {
//			currNode = ll.get(i);
//			currGems = currNode.gems();
//			currLength = ll.get(i-1).getEdge(currNode).fuelNeeded();
//			lengths.add(currLength);
//			sumLengths += currLength;
//			sumGems += currGems;
//			if(currGems > maxGems) maxGems = currGems; maxGemIndex = i;
//			if(currLength > maxLength) maxLength = currLength; maxLengthIndex = i-1;
//		}
		sumLengths = Paths.pathWeight(ll);

		Edge nextEdge = null;
		Edge prevEdge = null;
		int extensionlength = 0;
		int currentlength = 0;
		for(int j = ll.size()-2; j > 1; j--) {
			currNode = ll.get(j);
			for(Node n : currNode.neighbors().keySet()) {
				nextEdge = n.getEdge(ll.get(j+1));
				prevEdge = n.getEdge(ll.get(j-1));
				if(nextEdge!=null && prevEdge!=null) {
					extensionlength = prevEdge.length + nextEdge.length;
					if(j+1 < lengths.size()) {
						currentlength = lengths.get(j+1)+lengths.get(j);
					} else {
						currentlength = lengths.get(j);
					}
					if(//n.gems()/extensionlength > currGems/currentlength
							 sumLengths+extensionlength < state.fuelRemaining()) {
						//ll.remove(j);
						ll.add(j, n);
						List<Node> pathBack = Paths.minPath(n, ll.get(j+1));
						for(int k=0; k<pathBack.size(); k++) {
							ll.add(j+k+1, pathBack.get(k));
						}
						System.out.println("added node");
					}
				}
			}
		} return ll;
		
	}
	
}
