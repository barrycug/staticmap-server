# Static Map Server application

## Installation

Clone the repository

git clone https://github.com/germanosin/staticmap-server.git

Create dist archive

./activator dist

Unzip and run

unzip target/universal/staticmap-1.0-SNAPSHOT.zip

bin/staticmap

## Usage

http://localhost:9000/image?baseMap=osm&size=1024x1024&center=55,49&zoom=6

baseMap - name of Base Map Layer. You can create ownself. Example is https://github.com/germanosin/staticmap-server/blob/master/app/org/gradoservice/mapRender/layers/OSM.java
And then register it in factory https://github.com/germanosin/staticmap-server/blob/master/app/Global.java

size - width+'x'+height in pixels

center - latitude+','+longtitude

zoom - map zoom

markers - markers usage: &markers[]=icon:default|55,49

paths - paths usage: &paths[]=color:0xff0000|55,49|55.1,49.1|55.2,49.2

geojsons = geojson elements: &geojsons[]={type: Point, coordinates: [49,55]}

fitBounds - if you have markers or paths and you want to fit map on them just add &fitBounds=true

All parameters are valid both for GET and POST request



