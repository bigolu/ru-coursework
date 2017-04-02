var globalLat = 0;
var globalLon = 0;

(function ($) {

    'use strict';

    initMajorAndSchoolDropdown();
    initPieChart("#piechart1");
    initPieChart("#piechart2");
    initBarChart("#barchart1", []);
    initStats(0);
    setInterval(function(){ initStats2(0) }, 30000);

    function initStats2(i) {
            if(i == 0){
                $('#rando2').empty();
            }

            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    console.log(xhttp.responseText);
                    var resp = $.parseJSON(xhttp.responseText);
                    var percent = resp.percent;
                    var fact = resp.fact;
                    var color = percent.includes('-') ? 'red' : 'green';
                    var stat = "<p>FACT: <span style=\"color:COLOR\">PERCENT</span> </p>".replace("PERCENT", percent).replace("FACT", fact).replace("COLOR", color);
                    $(stat).appendTo("#rando2");
                }

            };
            xhttp.open("GET", "/random_percent", true);
            xhttp.send();
            
            if(i < 1){
                initStats2(i + 1);
            }
    }

    function initStats(i) {
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    console.log(xhttp.responseText);
                    var resp = $.parseJSON(xhttp.responseText);
                    loadStat(resp.percent, resp.fact);
                }

            };
            xhttp.open("GET", "/random_percent", true);
            xhttp.send();
            
            if(i < 5){
                initStats(i + 1);
            }
    }
    function loadStat(percent, fact) {
        var color = percent.includes('-') ? 'red' : 'green';
        var stat = "<p style=\"display:inline\">FACT: <span style=\"color:COLOR\">PERCENT</span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>".replace("PERCENT", percent).replace("FACT", fact).replace("COLOR", color);

        $(stat).appendTo("#stats-container");
    }

    function httpGetMap(theUrl) {
        var xmlHttp = new XMLHttpRequest();
        xmlHttp.open("GET", theUrl, false); // false for synchronous request
        xmlHttp.send();
        var text = xmlHttp.responseText;
        return $.parseJSON(text);
    }

    $('#state0').change(function () {
        if ($('#campus0').val() && $('#state0').val()) {
            initMap();
        }

    });
    $('#campus0').change(function () {
        if ($('#campus0').val() && $('#state0').val()) {
            initMap();
        }

    });
    $('#majors').change(function () {
        console.log('here');
        if (globalLat != 0 && globalLon != 0 && $('#state0').val() && $('#campus0').val() && $('#majors').val())
        {
            console.log('there');
            var data = getBarDataSource();
            initBarChart("#barchart1", data);
        }

    });
    $('#state').change(function () {
        if ($('#campus1').val() && $('#schools').val() && $('#state').val()) {
            var data = getPieDataSource();
            initPieChart("#piechart1", data.data);
        }
    });
    $('#campus1').change(function () {
        if ($('#campus1').val() && $('#schools').val() && $('#state').val()) {
            var data = getPieDataSource();
            initPieChart("#piechart1", data.data);
        }
    });
    $('#schools').change(function () {
        if ($('#schools').val()) {
            var data2 = getPieDataSource2();
            initPieChart("#piechart2", data2.data);
        }
        if ($('#campus1').val() && $('#schools').val() && $('#state').val()) {
            var data = getPieDataSource();
            initPieChart("#piechart1", data.data);
        }
    });

    function initMajorAndSchoolDropdown()
    {
        var selectMajors = $('#majors');
        var deg = httpGetMap('/degrees');
        var degVal = deg.degrees;

        for (var degree in degVal) {
            if (!degVal.hasOwnProperty(degree)) {
                continue ;
            }

            var stringMajors = degree;
            var optionMajors = document.createElement('option');
            
            optionMajors.innerHTML = stringMajors;
            optionMajors.value = degVal[degree];

            selectMajors.append(optionMajors);
        }
        
        var selectColleges = $('#schools');        
        var col = httpGetMap('/colleges');
        var colVal = col.colleges;

        for (var college in colVal) {
            if (!colVal.hasOwnProperty(college)) {
                continue;
            }
            var stringColleges = college;
            var optionColleges = document.createElement('option');
            
            optionColleges.innerHTML = stringColleges; //may be changed
            optionColleges.value = colVal[college]; //maj
            selectColleges.append(optionColleges);
        }

        var states = httpGetMap('/states').states;

        for(var i = 0; i < states.length; i++){
            var option = document.createElement('option');
            option.innerHTML = states[i];
            option.value = states[i];
            $('#state0').append(option);
        }

    }

    function initBarChart(x, data) {
        $(x).igDataChart({
            width: "100%",
            height: "500px",
            dataSource: data,
            title: "Value of Nearby Schools",
            subtitle: "6 Nearby Schools Cost to Earning Ratio",
            axes: [{
                name: "school",
                type: "categoryX",
                label: "school",
                gap: 0.8,
            }, {
                name: "ratio",
                type: "numericY",
                title: "Ratio of Cost to Salary",
            }],
            series: [{
                name: "College",
                title: "schools",
                type: "column",
                isHighlightingEnabled: true,
                showTooltip: true,
                tooltipTemplate: "tooltipTemplate",
                xAxis: "school",
                yAxis: "ratio",
                valueMemberPath: "ratio",
                thickness: .01
            }]
        });

    }

    function getBarDataSource() {
        var data = httpGetMap("/nearby?lat="+globalLat+"&lon="+globalLon+"&state="+$('#state0').val()+"&on_campus="+$('#campus0').val()+"&degree_id="+$('#majors').val());
        return data.colleges;
    }

    function initMap() {
        var state = $('#state0').val();
        var on_campus = $('#campus0').val();
        var data = httpGetMap("/heatmap?state="+state+"&on_campus="+on_campus);
        console.log(data);
        data = data.colleges;
        var selectionData = [];

        $("#map").igMap({
            width: "100%",
            height: "500px",
            backgroundContent: {
                type: "openStreet"
            },
            seriesMouseLeftButtonDown: function (evt, ui) {
                var lon,name;
                var lat;
                if (ui.item){
                    lon = ui.item.lon;
                    lat = ui.item.lat;
                    name = ui.item.name;
                    globalLat = lat;
                    globalLon = lon;

                    if ($('#majors').val() && $('#state0').val() && $('#campus0').val())
                    {
                        var data = getBarDataSource();
                        initBarChart('#barchart1', data);
                    }

                    //call to backend

                }
                else {
                    //NEVER REACHED
                }

                selectionData=[{ "name": name, "lat": lat, "lon": lon }];
                $("#map").igMap("option", 'series', [{ 'name': 'selectedLocation', 'dataSource': selectionData } ]);
            },
            overviewPlusDetailPaneVisibility: "visible",
            overviewPlusDetailPaneBackgroundImageUri: "http://www.igniteui.com/images/samples/maps/world.png",

            series: [{
                    type: "geographicSymbol",
                    name: "schools",
                    dataSource: data,
                    longitudeMemberPath: "lon",
                    latitudeMemberPath: "lat",
                    showTooltip: true,
                    tooltipTemplate: "geoSymbolTooltip",
                    // using custom template
                    markerType: "none",
                    markerTemplate: {
                        measure: function (measureInfo) {
                            measureInfo.width = 10;
                            measureInfo.height = 10;
                        },
                        render: function (renderInfo) {

                            var ctx = renderInfo.context;
                            var x = renderInfo.xPosition;
                            var y = renderInfo.yPosition;
                            if (renderInfo.isHitTestRender)
                                ctx.fillStyle = renderInfo.data.actualItemBrush().fill();
                            else
                                ctx.fillStyle = "black";

                            var size = 10;
                            var heightHalf = size / 2.0;
                            var widthHalf = size / 2.0;

                            if (renderInfo.isHitTestRender) {
                                ctx.fillRect(x - widthHalf, y - heightHalf, size, size);
                            } else {

                                // color markers based on population of cities
                                var pop = renderInfo.data.item()["cost"]*2;
                                if (pop > 5000)
                                    ctx.fillStyle = "rgba(255, 128, 128, 1);";
                                if (pop > 15000)
                                    ctx.fillStyle = "rgba(255, 46, 46, 1)";
                                if (pop > 30000)
                                    ctx.fillStyle = "rgba(199, 0, 0, 1)";
                                if (pop > 45000)
                                    ctx.fillStyle = "rgba(138, 0, 0, 1)";
                                if (pop > 60000)
                                    ctx.fillStyle = "rgba(87, 0, 0, 1)";

                                size = 3;
                                ctx.globalAlpha = 1;
                                ctx.strokeStyle = "rgba(20,20,20,0.36)";
                                ctx.beginPath();
                                ctx.arc(x, y, size, 0, 2.0 * Math.PI);
                                ctx.fill();
                                ctx.stroke();
                            }
                        }
                    }
            },
            {
                type: "geographicSymbol",
                name: "selectedLocation",
                dataSource: [],
                latitudeMemberPath: "lat",
                longitudeMemberPath: "lon",
                showTooltip: false,
                //  Defines marker template rendering function
                markerTemplate: {
                    measure: function (measureInfo) {
                        measureInfo.width = 10;
                        measureInfo.height = 10;
                    },
                    render: function (renderInfo) {
                        createMarker(renderInfo);
                    }
                }
            }],
            windowRect: { left: 0.1, top: 0.1, height: 0.7, width: 0.7 }
        });
    }

    function createMarker(renderInfo) {
        var ctx = renderInfo.context;
        var x = renderInfo.xPosition;
        var y = renderInfo.yPosition;
        var size = 10;
        var heightHalf = size / 2.0;
        var widthHalf = size / 2.0;

        if (renderInfo.isHitTestRender) {
            //  This is called for tooltip hit test only
            //  Rough marker rectangle size calculation
            ctx.fillStyle = renderInfo.data.actualItemBrush().fill();
            ctx.fillRect(x - widthHalf, y - heightHalf, size, size);
        } else {
            var data = renderInfo.data;
            var name = data.item()["name"];
            //  Draw text
            ctx.textBaseline = "top";
            ctx.font = '8pt Verdana';
            ctx.fillStyle = "black";
            ctx.textBaseline = "middle";
            wrapText(ctx, name, x + 3, y + 6, 80, 12);

            //  Draw marker
            ctx.beginPath();
            ctx.arc(x, y, 4, 0, 2 * Math.PI, false);

                ctx.fillStyle = "#36a815";

            ctx.fill();
            ctx.lineWidth = 1;
            ctx.strokeStyle = "black";
            ctx.stroke();
        }
    }

    //  Plots a rectangle with rounded corners with a semi-transparent frame
    function plotTextBackground(context, left, top, width, height) {
        var cornerRadius = 3;
        context.beginPath();
        //  Upper side and upper right corner
        context.moveTo(left + cornerRadius, top);
        context.lineTo(left + width - cornerRadius, top);
        context.arcTo(left + width, top, left + width, top + cornerRadius, cornerRadius);
        //  Right side and lower right corner
        context.lineTo(left + width, top + height - cornerRadius);
        context.arcTo(left + width, top + height, left + width - cornerRadius, top + height, cornerRadius);
        //  Lower side and lower left corner
        context.lineTo(left + cornerRadius, top + height);
        context.arcTo(left, top + height, left, top + height - cornerRadius, cornerRadius);
        //  Left side and upper left corner
        context.lineTo(left, top + cornerRadius);
        context.arcTo(left, top, left + cornerRadius, top, cornerRadius);
        //  Fill white with 75% opacity
        context.globalAlpha = 1;
        context.fillStyle = "white";
        context.fill();
        context.globalAlpha = 1;
        //  Plot grey frame
        context.lineWidth = 1;
        context.strokeStyle = "grey";
        context.stroke();
    }

    //  Outputs text in a word wrapped fashion in a transparent frame
    function wrapText(context, text, x, y, maxWidth, lineHeight) {
        var words = text.split(" ");
        var line = "";
        var yCurrent = y;
        var lines = [], currentLine = 0;

        //  Find the longest word in the text and update the max width if the longest word cannot fit
        for (var i = 0; n < words.length; i++) {
            var testWidth = context.measureText(words[i]);
            if (testWidth > maxWidth)
                maxWidth = metrics.width;
        }
        //  Arrange all words into lines
        for (var n = 0; n < words.length; n++) {
            var testLine = line + words[n];
            var testWidth = context.measureText(testLine).width;
            if (testWidth > maxWidth) {
                lines[currentLine] = line;
                currentLine++;
                line = words[n] + " ";
            }
            else {
                line = testLine + " ";
            }
        }
        lines[currentLine] = line;
        //  Plot frame and background
        if (lines.length > 1) {
            //  Multiline text
            plotTextBackground(context, x - 2, y - lineHeight / 2 - 2, maxWidth + 3, lines.length * lineHeight + 3);
        }
        else {
            //  Single line text
            var textWidth = context.measureText(lines[0]).width;    //  Limit frame width to the actual line width
            plotTextBackground(context, x - 2, y - lineHeight / 2 - 2, textWidth + 3, lines.length * lineHeight + 3);
        }
        //  Output lines of text
        context.fillStyle = "black";
        for (var n = 0; n < lines.length; n++) {
            context.fillText(" " + lines[n], x, yCurrent);
            yCurrent += lineHeight;
        }
    }


    function initPieChart(x, data) {
        $(x).igPieChart({
            width: "100%",
            height: "435px",
            dataSource: data, //JSON data defined above
            dataValue: "cost",
            dataLabel: "category",
            labelsPosition: "bestFit"
        });
    }

    function getPieDataSource() {
        var data = httpGetMap('/student_expenses?college_id='+$('#schools').val()+"&in_state="+$('#state').val()+"&on_campus="+$('#campus1').val());
        return data;
    }
    
    function getPieDataSource2() {
        var data = httpGetMap('college_expenses?college_id='+$('#schools').val());
        return data;
    }


}(jQuery));
