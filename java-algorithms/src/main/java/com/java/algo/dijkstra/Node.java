package com.java.algo.dijkstra;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Node {

	private String name;
	
	private List<Node> shortestPath = new LinkedList<>();
	
	private Integer distance  = Integer.MAX_VALUE;
	
	Map<Node, Integer> adjacentNodes = new HashMap<>();
	
	public void addDestination(Node destination, int distance) {
		adjacentNodes.put(destination, distance);
	}
	
	public Node(String name) {
		this.name = name;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public Map<Node, Integer> getAdjacentNodes() {
		return adjacentNodes;
	}

	public void setAdjacentNodes(Map<Node, Integer> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}

	public List<Node> getShortestPath() {
		return shortestPath;
	}

	public void setShortestPath(List<Node> shortestPath) {
		this.shortestPath = shortestPath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name + " - "+ distance + " - "+shortestPath.stream().peek(e -> {System.out.print(e.name + "--" +e.distance);}).collect(Collectors.toList());
		
	}
	
	
}
