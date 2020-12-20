Project:
	
	this project represents an directional and weighted graph that support a large number of nodes,
	based on afficient cimpact representation. The project contains 2 parts: api - the data struct
	and the algorithms methods, and the secent part gameClient- that represents the pokemon challenge that
	including gui (Graphical User Interface), processes and Automatic system for playing againstד server.
	
Data Struct:
		
	this project based on HashTable. HashTable allow insert search and remove in O(1) (most cases)
	every node has unique key. the graph represented by HashTable of nodes and key(the node id),
	and by HashTable that represent the edges.

Classes:
	
	geoLocaion: implemants geo_location interface represent an 3D point
	
	edgeData: implemants edge_data interface 
		
		methods:
		constructor
		getters & setters - soure node , dest node, edge weight , info , tag	
	
	NodeData: implemants node_data interface represents the set of operations applicable
	on a node (vertex) in an directional and weighted graph.
		
		methods:
			constructor
			getters & setters - key , geo_location , weight , tag, info
			compareto - compere nodes weight used in Dijkstra method (algorithms)
		
	DWGraph_DS: implemants directed_weighted_graph interface
		
		methods: 
			constructor
			getNode: returns the node data by the node id
			getEdge: returns the data of the edge (src,dest)
			addNode: adds a new node to the graph with the given node data
			connect: connects an edge with weight w between node src to node dest
			getV: returns a pointer (shallow copy) for the collection representing
			all the nodes in the graph.
			getE: returns a pointer (shallow copy) for the collection representing
			all the edges getting out of the given node
			removeNode: deletes the node from the graph and removes
			all edges which starts or ends at this node. 
			removeEdge: deletes the edge from the graph
			nodeSize: returns the number of vertices (nodes) in the graph
			edgeSize: returns the number of edges
			getMC: returns the Mode Count - for testing changes in the graph.
	
	DWGraph_Algo: This interface represents a Directed (positive) Weighted Graph Theory Algorithms.
	
		methods: 
			init: init the graph on which this set of algorithms operates on.
			getGraph: return the underlying graph of which this class works. 
			copy: compute a deep copy of this weighted graph.
			isConnected: returns true if and only if there is a valid path from
			each node to each other node by using bfs algorithm.
			shortestPath: returns the the shortest path between src to dest
			as an ordered List of nodes by using dijkstra algorithm
			shortestPathDist: returns the length of the shortest path between src to dest
			save: save the graph to the given file name in JSON format
			load: load a graph to this graph algorithm by JSON format
	
	Ex2: is the main class of the pokemon game. the game communicate with the server by the game service interface.
	 the game launch with his gui.
	 the class contain:
		win: the main GUI frame that responsible to draw our game
		ar: the game arena contains information from the game between each move
		log: represent the game login panel, if we want to login we need to press login in the login panel 
		and then start game.
		agents: list of objects represents the agents in the game
		pokemons: list of objects represent the pokemons in the game
		id: the id of user	
		
GFrame: the main GUI of the game responsible to draw our game, the window is serializable
			
	
Login Panel: 	
	A window that appears at the beginning of the game.
	to login we have to insert id and press login to save our score and then press start game.
	we can choose between 24 (0-23) levels
	
CL_Agent:
	represent agents in the game, the agent was created by JSON String from the client in every move.
	every agent contains: speed, the current edge , the current node and the location on the graph
		
CL_Pokemon:
	represent pokemon in the game, the pokemon was created by JSON String from the client in every move.
	every pokemon contains location on the graph , edge on the graph, value of the pokemos and type
	
		
