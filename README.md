Train Service
=============

The application implements a simple web interface for importing the train map files in CSV format and a REST web interface. Once the map files are imported they are stored on the graph database (Neo4j). All routing on the train network is managed via the Neo4j database.

![alt text](https://github.com/momenso/codechallenge/raw/graphdb/images/diagram.png "Overview Diagram")

How to use
----------

Once the application is started for the first time it is necessary to import the map files. This can be done by navigating to the server web address and upload the required CSV files.

![alt text](https://github.com/momenso/codechallenge/raw/graphdb/images/uploadform.png "Upload Form")

When the upload is complete, the database is updated and the service is ready to respond to routing queries.

The list of stations between an origin and destination can be obtained with the following REST call:
```
/api/v1/test/route/{origin}/{destination}
```
Where _{origin}_ and _{destination}_ are station IDs.

A test method is available for querying routes between two train stations by name. In the following example we are requesting the route between Queensway station and Marble Arch station.
```
http://<server>:<port>/api/v1/test/route?from=Holborn&to=Baker+Street
```
A JSON formated response is then provided as follows.
```javascript
{
  "routePlan": {
    "estimatedTime": 6,
    "stations": {
      "station": [
        {
          "id": 64,
          "name": "Queensway",
          "line": 2
        },
        {
          "id": 63,
          "name": "Lancaster Gate",
          "line": 2
        },
        {
          "id": 34,
          "name": "Marble Arch",
          "line": 2
        }
      ]}}
}
```

References:

[1] London Underground maps https://commons.wikimedia.org/wiki/London_Underground_geographic_maps/CSV

[2] Java Jersey https://jersey.java.net

[3] SuperCSV http://supercsv.sourceforge.net/

[4] Neo4j Graph database http://www.neo4j.org/
