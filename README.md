Train Service
=============

The application implements a simple web interface for importing the train map files in CSV format and two web service methods. One used by the web interface to import the map files and the other to be queried by a mobile device requesting the route between two train stations.

![alt text](https://github.com/momenso/codechallenge/raw/graphdb/diagram.png "Overview Diagram")

Once the map files are imported they are stored on the graph database (Neo4j). All routing on the train network is managed via the Neo4j database.

The request format for the route between two train stations is described below. In the following example we are requesting the route between Baker Street station and Oxford Circus station.
```
http://<server>:<port>/api/train-service/route?from=Baker+Street&to=Oxford+Circus
```
A JSON formated response is then provided as follows.
```javascript
{
  "routePlan": {
    "estimatedTime":6,
    "route":[
      {"id":"11","name":"Baker Street"},
      {"id":"212","name":"Regent's Park"},
      {"id":"192","name":"Oxford Circus"}
    ]
}}
```

References:

[1] London Underground maps https://commons.wikimedia.org/wiki/London_Underground_geographic_maps/CSV

[2] Java Jersey https://jersey.java.net

[3] SuperCSV http://supercsv.sourceforge.net/

[4] Neo4j Graph database http://www.neo4j.org/
