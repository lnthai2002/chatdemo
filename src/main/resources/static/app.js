var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#room").prop("disabled", connected);
    $("#name").prop("disabled", connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var room = $('#room').val();
    var username = $('#name').val();
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/broadcast/' + room , function (msg) {
            var msgObj =JSON.parse(msg.body);
            showMsg(msgObj.from + ' says: ' + msgObj.body);
        });
        stompClient.subscribe('/pm/' + username , function (msg) {
            var msgObj =JSON.parse(msg.body);
            showPmMsg(msgObj.from + ' says: ' + msgObj.body);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    var room = $("#room").val();
    var to = $('#to').val();
    var msg = {
        'roomName': room,
        'from': $("#name").val(),
        'to': to,
        'body': $("#msg").val()
    };
    if (to == null || to == '') {
        stompClient.send("/app/chat/"+room, {}, JSON.stringify(msg));
    }
    else {
        stompClient.send("/pm/" + to, {}, JSON.stringify(msg));
    }
}

function showMsg(message) {
    $("#roomChat").append("<tr><td>" + message + "</td></tr>");
}

function showPmMsg(message) {
    $("#pmChat").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});