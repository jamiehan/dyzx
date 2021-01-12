// Set the rosbridge and mjpeg_server port
var rosbridgePort = "9090";
var mjpegPort = "8080";

// Get the current hostname
thisHostName = "192.168.1.102";//document.location.hostname;

// If the rosbridge server is running on the webserver host, then this will
// work.
var rosbridgeHost = thisHostName;
var mjpegHost = thisHostName;
var serverURL = "ws://" + rosbridgeHost + ":" + rosbridgePort;

// Are we on a touch device?
var isTouchDevice = 'ontouchstart' in document.documentElement;

// Key modifiers
var shiftKey = false;

// Our ROS parameter namespace
var param_ns = '/robot_gui';

// Launch files for various nodes
var openni_node = {
    name: "openni_node",
    command: "roslaunch rbx2_vision openni_node.launch"
};

var uvc_cam_node = {
    name: "uvc_cam_node",
    command: "roslaunch rbx1_vision uvc_cam.launch"
};

// The ROS connection
var ros;

// Configurable options
var options = {
    // Speed parameters
    cmdVelTopic: '/cmd_vel',
    defaultLinearSpeed: 0.18,
    defaultAngularSpeed: 1.0,
    maxLinearSpeed: 0.3,
    maxAngularSpeed: 1.2,
    minLinearSpeed: 0.05,
    minAngularSpeed: 0.1,
    vxKeyIncrement: 0.02,
    vzKeyIncrement: 0.05,
    deadZoneVz: 0.2,

    //Starting Point and Charging Point of Robot
    moveBaseGoalTopic: '/move_base_simple/goal',

    // Use this topic for the fake battery
    robotBatteryTopic: '/voltage',

    // Use a topic like this for a real battery
    //robotBatteryTopic: '/arduino/sensor/main_voltage',

    // Video parameters
    videoTopic: '/camera/color/image_raw',
    videoQuality: 50,
    fovWidthRadians: 0.99, // Asus 57 degrees
    fovHeightRadians: 0.78 // Asus 45 degrees


};

// The global linear and angular speed variables
var vx = 0;
var vz = 0;

// Global video parameters
var videoWidth;
var videoHeight;
var videoViewer;
var videoScale;

if (isTouchDevice) videoScale = 0.75;
else videoScale = 1.0;

var videoStageWidth;
var videoStageHeight;
var videoHandle = null;

// A flag to indicate when the mouse is down
var mouseDown = false;

// A timer handle
var pubHandle = null;

// Rate for the main ROS publisher loop
var rate = 5;

var ros = new ROSLIB.Ros();

var topicList = [];
var paramList = [];
var lastParamValue;

function init_ros() {
    ros.connect(serverURL);

    // Get the current window width and height
    var windowWidth = this.window.innerWidth;
    var windowHeight = this.window.innerHeight;

    // Set the video width to 60% of the window width and scale the height
	// appropriately.
    videoWidth = Math.round(0.6 * windowWidth);
    videoHeight = Math.round(videoWidth * 240 / 320);
    videoStageWidth = videoWidth * videoScale;
    videoStageHeight = videoHeight * videoScale;
}

// If there is an error on the backend, an 'error' emit will be emitted.
ros.on('error', function(event) {
    console.log("Error connecting to ROS Bridge. Check to make sure you have launched the rosbridge server.");
});

// Wait until a connection is made before continuing
ros.on('connection', function() {
    console.log('Rosbridge connected.');

    // Create the video viewer.
    // videoViewer = new MJPEGCANVAS.Viewer({
	// divID : 'videoCanvas',
	// host : mjpegHost,
	// port: mjpegPort,
	// width : videoStageWidth,
	// height : videoStageHeight,
	// quality: options['videoQuality'],
	// topic : options['videoTopic']
    // });

    // Create the main Navigation viewer.
    var navViewer = new ROS2D.Viewer({
	divID : 'navCanvas',
	width : 615,//700,
	height : 620 //500
    });
    var zoomView = new ROS2D.ZoomView({
	    rootObject: navViewer.scene
    });
   //navViewer.scene.scaleX*=2;
    //navViewer.scene.scaleY*=2;
   /*
    // Setup the map client.
    var gridClient = new ROS2D.OccupancyGridClient({
        ros : ros,
        rootObject : navViewer.scene,
        viewer : navViewer,
        withOrientation: true,
        serverName : '/move_base',
        topic: '/map'
        });
*/
    // Setup the Navigation client.
    var navGridClient = new NAV2D.OccupancyGridClientNav({
	ros : ros,
	rootObject : navViewer.scene,
	viewer : navViewer,
	withOrientation: true,
	serverName : '/move_base'
    });
 
   
    

/*
   //Scale the canvas to fit the map
    navGridClient.on('change',  () => {
            navViewer.scaleToDimensions(navGridClient.currentGrid.width, navGridClient.currentGrid.height);
            navViewer.shift(navGridClient.currentGrid.pose.position.x, navGridClient.currentGrid.pose.position.y);
    });
*/



function cancel() {
    navGrigridClientdClient.navigator.cancelGoal();
  }

  var myimage = navViewer.scene.canvas;//document.getElementById("map");
  if (myimage.addEventListener) {
    // IE9, Chrome, Safari, Opera
    myimage.addEventListener("mousewheel", MouseWheelHandler, false);
    // Firefox
    myimage.addEventListener("DOMMouseScroll", MouseWheelHandler, false);
  }
  // IE 6/7/8
  else myimage.attachEvent("onmousewheel", MouseWheelHandler);

  var zoomLevel = 100;

  function MouseWheelHandler(e) {
    // cross-browser wheel delta
    var delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));

    if(delta > 0) {
      if(zoomLevel > 0) {
        zoomView.zoom(1.1);
        zoomLevel = zoomLevel - 1;
      }
    }
    else {
      if(zoomLevel < 100) {
        zoomView.zoom(1/1.1);
        zoomLevel = zoomLevel + 1;
      }
    }

    e.preventDefault();
    return false;
  }

  function getMousePos(canvas, evt) {
    var rect = canvas.getBoundingClientRect();
    return {
      x: evt.clientX - rect.left,
      y: evt.clientY - rect.top
    };
  }

  navViewer.scene.canvas.addEventListener('mousemove', function(evt) {
        var mousePos = getMousePos(navViewer.scene.canvas, evt);

        zoomView.startZoom(mousePos.x, mousePos.y);
    }, false);





    // Create a Param object for the max linear speed
    var maxLinearSpeedParam = new ROSLIB.Param({
	ros : ros,
	name : param_ns + '/maxLinearSpeed'
    });

    // Get the value of the max linear speed paramater
    maxLinearSpeedParam.get(function(value) {
	if (value != null) {
	    maxLinearSpeed = value;
	    
	    // Update the value on the GUI
	    var element = document.getElementById('maxLinearSpeed');
	    // element.setAttribute("max", maxLinearSpeed);
	    // element.setAttribute("value", options['defaultLinearSpeed']);
	}
    });

    // Create a Param object for the max angular speed
    var maxAngularSpeedParam = new ROSLIB.Param({
	ros : ros,
	name : param_ns + '/maxAngularSpeed'
    });

    // Get the value of the max angular speed paramater
    maxAngularSpeedParam.get(function(value) {
	if (value != null) {
	    maxAngularSpeed = value;
	    
	    // Update the value on the GUI
	    var element = document.getElementById('maxAngularSpeed');
	    // element.setAttribute("max", maxAngularSpeed);
	    // element.setAttribute("value", options['defaultAngularSpeed']);
	}
    });

    deleteParamService = new ROSLIB.Service({
		ros : ros,
		name : '/rosapi/delete_param',
		serviceType : 'rosapi/DeleteParam'
    });

    getParamService = new ROSLIB.Service({
		ros : ros,
		name : '/rosapi/get_param',
		serviceType : 'rosapi/GetParam'
    });
    
    setParamService = new ROSLIB.Service({
		ros : ros,
		name : '/rosapi/set_param',
		serviceType : 'rosapi/SetParam'
    });

   

    // Start subscribers
    startSubscribers();

    // Start the publisher loop
    pubLoop();
});

function connectDisconnect() {
    var connect = document.getElementById('connectROS').checked;
    
    if (connect) connectServer();
    else disconnectServer();
}

function disconnectServer() {
    console.log("Disconnecting from ROS.");
    videoViewer.changeStream();
    ros.close();
}

function connectServer() {
    rosbridgeHost = document.getElementById("rosbridgeHost").value;
    rosbridgePort = document.getElementById("rosbridgePort").value;
    serverURL = "ws://" + rosbridgeHost + ":" + rosbridgePort;
    videoViewer.changeStream(options['videoTopic']);
    try {
	ros.connect(serverURL);
	console.log("Connected to ROS.");
    }
    catch(error) {
	console.write(error);
    }
}

function startSubscribers() {
    // Subscribe to the robot battery topic

    // Use for fake battery level published on /battery_level
    var subRobotBattery = new ROSLIB.Topic({
	ros : ros,
	name : options['robotBatteryTopic'],
	messageType: 'std_msgs/Int8',
	throttle_rate: 2000 // milliseconds
    });
	 
    subRobotBattery.subscribe(function(msg){ 
	var color =	getBatteryColor(msg.data);
	// $("#robotBatteryGauge").jqxLinearGauge({
	//     value: msg.data,
	//     pointer: {size: '20%', style: {fill: color}}});
    });

    console.log('Subscribed to ' + options['robotBatteryTopic']);

    
}

function getBatteryColor(value) {
    if (value >= 0 && value <= 30) return '#FF0000';
    if (value > 30 && value <= 50) return '#FFFF00';
    if (value > 50) return '#00FF00';
}

var cmdVelPub = new ROSLIB.Topic({
    ros : ros,
    name : options['cmdVelTopic'],
    messageType : 'geometry_msgs/Twist'
});

let moveBaseGoalPub = new ROSLIB.Topic({
    ros : ros,
    name : options['moveBaseGoalTopic'],
    messageType: 'geometry_msgs/PoseStamped'
});

function pubMoveBaseGoal(px, py, pz, ox, oy, oz, ow) {
    let moveBaseGoalMsg = new ROSLIB.Message({
        header: {frame_id: 'map'},
        pose : {
            position : {
                x : px,
                y : py,
                z : pz
            },
            orientation : {
                x : ox,
                y : oy,
                z : oz,
                w : ow
            }
        }
    });

    moveBaseGoalPub.publish(moveBaseGoalMsg);
}

function pubCmdVel() {
    vx = Math.min(Math.abs(vx), options['maxLinearSpeed']) * sign(vx);
    vz = Math.min(Math.abs(vz), options['maxAngularSpeed']) * sign(vz);

    var cmdVelMsg = new ROSLIB.Message({
	linear : {
	    x : vx,
	    y : 0.0,
	    z : 0.0
	},
	angular : {
	    x : 0.0,
	    y : 0.0,
	    z : vz
	}
    });

    cmdVelPub.publish(cmdVelMsg);

    //updateCmdVelMarker();

    //writeMessageById("baseMessages", " vx: " + Math.round(vx * 100)/100 + ", vz: " + Math.round(vz*100)/100);
}

// Base speed control using the keyboard or mouse
function setSpeed(code) {
    // Stop if the deadman key (Shift) is not depressed
   

    // Use space bar to stop
    if (code == 32) {
        // Space bar
	vx = 0;
	vz = 0;
    }
    else if (code == "left" || code == 37 || code == 65) {
        // Left arrow or "a"
	vz += options['vzKeyIncrement'];
    }
    else if (code == 'forward' || code == 38 || code == 87) {
        // Up arrow or "w"
        vx += options['vxKeyIncrement'];
    }
    else if (code == 'right' || code == 39 || code == 68) {
        // Right arrow or "d"
	vz -= options['vzKeyIncrement'];
    }
    else if (code == 'backward' || code == 40 || code == 88) {
        // Down arrow or "x"
	vx -= options['vxKeyIncrement'];
    }
}

function stopRobot() {
    vx = vz = 0;
    pubCmdVel();
}

function setMaxLinearSpeed(spd) {
    options['maxLinearSpeed'] = spd;
}

function setMaxAngularSpeed(spd) {
    options['maxAngularSpeed'] = spd;
}


// Draw the position of the speed control on the base pad
function updateBasePadMarker(vx, vz) {
    markerX = vz / options['maxAngularSpeed'];
    markerY = vx / options['maxLinearSpeed'];
    markerX *= basePadWidth / 2;
    pos = basePad.getAbsolutePosition();
    console.log(pos);
    markerX = basePad.x + basePadWidth / 2 - markerX;
    markerY *= basePadHeight / 2;
    markerY = basePad.y + basePadHeight / 2 - markerY;
    basePadMarker.setX(markerX);
    basePadMarker.setY(markerY);
    baseMarkerLayer.draw();
    writeMessageById("baseMessages", " vx: " + Math.round(vx * 100)/100 + ", vz: " + Math.round(vz*100)/100);
}

function updateCmdVelMarker() {
    var x = videoStageWidth / 2 - vz * 100 * videoScale;
    var y = videoStageHeight - vx * 500 * videoScale - 100;

    if (vx == 0 && vz == 0) y = videoStageHeight;

    cmdVelMarker.setPoints([videoStageWidth/2, videoStageHeight, x, y]);

    // videoMarkerLayer.draw();
}

function getParam(param) {
    var paramValueResult = document.getElementById('getParamResult');

    var request = new ROSLIB.ServiceRequest({
    	name: param,
        default: ''
    });
    getParamService.callService(request, function(result) {
    	paramValueResult.innerHTML = result.value;
    });
}

function setParam() {
    var item = $('#setParamListPullDown').jqxDropDownList('getItem', args.index);
    var paramValue = document.getElementById('setParamValue');
    var param = new ROSLIB.Param({
		ros : ros,
		name : item.label
    });
    
    if (isNumeric(paramValue.value)) {
		param.set(parseFloat(paramValue.value));
    }
    else {
    	param.set(paramValue.value);
    }
}

function setGUIParam() {
    var paramName = document.getElementById('setParamName');
    var paramValue = document.getElementById('setParamValue');
    var param = new ROSLIB.Param({
		ros : ros,
		name : param_ns + '/' + paramName.value
    });
    param.set(paramValue.value);
}

function getGUIParam() {
    var paramName = document.getElementById('getParamName');
    var paramValue = document.getElementById('getParamValue');
    var param = new ROSLIB.Param({
		ros : ros,
		name : param_ns + '/' + paramName.value
    });
    param.get(function(value) {
    	paramValue.value = value;
    });
}

var topicTypeClient = new ROSLIB.Service({
    ros : ros,
    name : '/rosapi/topic_type',
    serviceType : 'rosapi/TopicType'
});


function loadTopics() {
    ros.getTopics(function(topics) {
	$("#topicListPullDown").jqxDropDownList({ source: topics, selectedIndex: -1, width: '300', height: '25', theme: 'ui-start', placeHolder: "Select Topic" });
	$('#topicListPullDown').on('select', function (event) {
	    var args = event.args;
	    var topicType;
	    var topicTypeLabel = document.getElementById('topicType');
	    var item = $('#topicListPullDown').jqxDropDownList('getItem', args.index);
/*
	    if (item != null) {
		topicTypeLabel.innerHTML = item.value
		});
	    }

	    function callback(result) {
	    	topicTypeLabel.value = result.type; 
	    }
*/

	});
    });
}

function loadParamNames() {
    ros.getParams(function(params) {
	$("#getParamListPullDown").jqxDropDownList({ source: params, selectedIndex: -1, width: '300', height: '25', theme: 'ui-start', placeHolder: "Select Parameter" });
	$('#getParamListPullDown').on('select', function (event) {
	    var args = event.args;
	    var item = $('#getParamListPullDown').jqxDropDownList('getItem', args.index);
	    if (item != null) {
	    	getParam(item.label);
	    }
	});

	$("#setParamListPullDown").jqxDropDownList({ source: params, selectedIndex: -1, width: '300', height: '25', theme: 'ui-start', placeHolder: "Select Parameter" });
	$('#setParamListPullDown').on('select', function (event) {
	    var args = event.args;
	    var item = $('#setParamListPullDown').jqxDropDownList('getItem', args.index);	    
	});
    });
}

function pubChatter(rate) {
    var message = document.getElementById('chatterMessage');

    var chatter = new ROSLIB.Topic({
    	ros : ros,
    	name : '/chatter',
    	messageType : 'std_msgs/String',
    	throttle_rate: 1
    });

    var msg = new ROSLIB.Message({
    	data : message.value
    });

    chatter.publish(msg);
}

function subChatter() {
    var subscribe = $("#subChatterButton").jqxToggleButton('toggled');
    var chatterData = document.getElementById('chatterData');
    var listener = new ROSLIB.Topic({
		ros : ros,
		name : '/chatter',
		messageType : 'std_msgs/String'
    });

    if (subscribe) {
		console.log('Subscribed to ' + listener.name);
		chatterSubscribed = true;
		listener.subscribe(function(msg) {
		    chatterData.innerHTML = msg.data;
		});
    }
    else {
		listener.unsubscribe();
		chatterSubscribed = false;
		console.log('Unsubscribed from ' + listener.name);
    }
}

function refreshPublishers() {
    // var pubChatterOn = $("#pubChatterButton").jqxToggleButton('toggled');
    // if (pubChatterOn) pubChatter();

    //pubCmdVel();
}

function pubLoop() {
    console.log("Starting publishers");
    pubHandle = setInterval(refreshPublishers, 1000 / rate);
}

function sleep(delay) {
    var start = new Date().getTime();
    while (new Date().getTime() < start + delay);
}

function sign(x)
{
    if (x < 0) { return -1; }
    if (x > 0) { return 1; }
    return 0;
}


function isNumeric(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}


