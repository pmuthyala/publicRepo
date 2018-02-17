package com.java.algo.dijkstra;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import javax.jws.soap.SOAPBinding;
import javax.xml.transform.Source;

public class Dijkstra {

	public static void main(String []a) {
		Node nodeA = new Node("A");
		Node nodeB = new Node("B");
		Node nodeC = new Node("C");
		Node nodeD = new Node("D");
		Node nodeE = new Node("E");
		Node nodeF = new Node("F");
		
		nodeA.addDestination(nodeB, 10);
		nodeA.addDestination(nodeC, 15);
		
		nodeB.addDestination(nodeD, 12);
		nodeB.addDestination(nodeF, 15);
		
		nodeC.addDestination(nodeE, 10);
		
		nodeD.addDestination(nodeE, 2);
		nodeD.addDestination(nodeF, 1);
		
		nodeF.addDestination(nodeE, 5);
		
		Dijkstra.calculateShortestPathFromSource(nodeA);
	}
	
	public static void calculateShortestPathFromSource(Node source) {
		source.setDistance(0);
		Set<Node> unsettledNodes = new HashSet<>();
		Set<Node> settledNodes = new HashSet<>();
		
		unsettledNodes.add(source);
		while(unsettledNodes.size() > 0) {
			Node currentNode = getLowestDistanceNode(unsettledNodes);
			unsettledNodes.remove(currentNode);
			
			for(Entry<Node, Integer> adjacencyPair: currentNode.getAdjacentNodes().entrySet()) {
				Node adjacentNode = adjacencyPair.getKey();
				Integer edgeWeight = adjacencyPair.getValue();
				System.out.println("Evaluating node "+adjacentNode.getName() + " shortest path "+ adjacentNode.getShortestPath());
				if(!settledNodes.contains(adjacentNode)) {
					calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
					unsettledNodes.add(adjacentNode);
				}
			}
			settledNodes.add(currentNode);
		}
	}
	
	public static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
		Node lowestDistanceNode = null;
		int lowestDistance = Integer.MAX_VALUE;
		for(Node node: unsettledNodes) {
			int nodeDistance  = node.getDistance();
			if(nodeDistance < lowestDistance) {
				lowestDistance = nodeDistance;
				lowestDistanceNode = node;
			}
		}
		return lowestDistanceNode;
	}
	
	public static void calculateMinimumDistance(Node evaluationNode, Integer edgeWeight, Node sourceNode) {
		Integer sourceDistance = sourceNode.getDistance();
		if(sourceDistance + edgeWeight < evaluationNode.getDistance()) {
			evaluationNode.setDistance(sourceDistance + edgeWeight);
			LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
			shortestPath.add(sourceNode);
			evaluationNode.setShortestPath(shortestPath);			
		}
	}
}