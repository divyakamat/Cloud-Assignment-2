<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Heatmaps</title>
    <style>
        html, body, #map-canvas {
            height: 100%;
            margin: 0px;
            padding: 0px
        }
        #panel {
            position: absolute;
            top: 5px;
            left: 50%;
            margin-left: -180px;
            z-index: 5;
            background-color: #fff;
            padding: 5px;
            border: 1px solid #999;
        }
    </style>



    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true&libraries=visualization"></script>
    <script>
        // Adding 500 Data Points
        //var map, pointarray, heatmap;

        //var pointArray;

        var pointArray;
        var heatmap;

        $(document).ready(function(){
            $("#show_map").click(function(){
                $.get("api/tweet", function(data, status){
                    pointArray = new google.maps.MVCArray([]);
                    for(var i=0 ; i<data.length; i++){
                        pointArray.push(new google.maps.LatLng(data[i].latitude, data[i].longitude));
                    }
                    heatmap.setData(pointArray);
                });
            });
        });

        $(document).ready(function(){
            $("#select_hash").click(function(){
                $("#select_hash").attr('disabled',true);
                var path = "api/tweet/getHashTag/"+ $("#HashTags").val();
                $.get(path, function(data, status){
                    pointArray = new google.maps.MVCArray([]);
                    for(var i=0 ; i<data.length; i++){
                        pointArray.push(new google.maps.LatLng(data[i].latitude, data[i].longitude));
                    }
                    heatmap.setData(pointArray);
                });
                $("#select_hash").attr('disabled',false);
            });
        });


        function initialize() {
            var mapOptions = {
                zoom: 13,
                center: new google.maps.LatLng(37.774546, -122.433523),
                mapTypeId: google.maps.MapTypeId.SATELLITE
            };

            map = new google.maps.Map(document.getElementById('map-canvas'),
                    mapOptions);

            pointArray = new google.maps.MVCArray([]);

            heatmap = new google.maps.visualization.HeatmapLayer({
                data: pointArray
            });

            heatmap.setMap(map);
            heatmap.set('radius' ,20);
        }

        function toggleHeatmap() {
            heatmap.setMap(heatmap.getMap() ? null : map);
        }

        function changeGradient() {
            var gradient = [
                'rgba(0, 255, 255, 0)',
                'rgba(0, 255, 255, 1)',
                'rgba(0, 191, 255, 1)',
                'rgba(0, 127, 255, 1)',
                'rgba(0, 63, 255, 1)',
                'rgba(0, 0, 255, 1)',
                'rgba(0, 0, 223, 1)',
                'rgba(0, 0, 191, 1)',
                'rgba(0, 0, 159, 1)',
                'rgba(0, 0, 127, 1)',
                'rgba(63, 0, 91, 1)',
                'rgba(127, 0, 63, 1)',
                'rgba(191, 0, 31, 1)',
                'rgba(255, 0, 0, 1)'
            ]
            heatmap.set('gradient', heatmap.get('gradient') ? null : gradient);
        }

        function changeRadius() {
            heatmap.set('radius', heatmap.get('radius') ? null : 20);
        }

        function changeOpacity() {
            heatmap.set('opacity', heatmap.get('opacity') ? null : 0.2);
        }

        google.maps.event.addDomListener(window, 'load', initialize);

    </script>
</head>

<body>
<div id="panel">
    <select id="HashTags">
        <option value="kca">#KCA</option>
        <option value="vote5sos">#vote5sos</option>
        <option value="votefifthharmony">#votefifthharmony</option>
        <option value="android">#android</option>

        <option value="thevoice">#thevoice</option>
        <option value="follow">#follow</option>
        <option value="thedress">#thedress</option>
        <option value="rt">#rt</option>

        <option value="teamfollowback">#teamfollowback</option>
        <option value="love">#love</option>
        <option value="iphone">#iphone</option>
        <option value="mma14">#mma14</option>

        <option value="ipad">#ipad</option>
        <option value="gameinsight">#gameinsight</option>
        <option value="votedulcemania">#votedulcemania</option>
        <option value="jobs">#jobs</option>

        <option value="art">#art</option>
        <option value="music">#music</option>
        <option value="win">#win</option>
        <option value="isis">#isis</option>
    </select>
    <button id="select_hash">Search HashTag</button>
    <button onclick="toggleHeatmap()">Toggle Heatmap</button>
    <button onclick="changeGradient()">Change gradient</button>
    <button onclick="changeRadius()">Change radius</button>
    <button onclick="changeOpacity()">Change opacity</button>
    <button id="show_map">Show All Tweets</button>
</div>
<div id="map-canvas"></div>
</body>
</html>