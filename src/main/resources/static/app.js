var stompClient = null;

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    var room = $('#room').val();
    var username = $('#name').val();

    setConnected(true);

    stompClient.subscribe(
        '/broadcast/' + room,
        onMessageBroadcasted
    );

    stompClient.subscribe(
        '/pm/' + room + '/' + username ,
        function (msg) {
            var msgObj =JSON.parse(msg.body);
            showPmMsg(msgObj.from + ' says: ' + msgObj.body);
        }
    );

    // announce your presence
    stompClient.send(
        "/app/chat/" + room,
        {},
        JSON.stringify({from: username, room: room, type: 'JOIN'})
    )
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);

    //lock room and username
    $("#room").prop("disabled", connected);
    $("#name").prop("disabled", connected);

    if (connected) {
        $("#conversation").show();
        $("#pmconversation").show();
    }
    else {
        $("#conversation").hide();
        $("#pmconversation").hide();
    }

    //$("#conversation").html("");
}

function onError(error) {
}

function onMessageBroadcasted(msg) {
    var msgObj =JSON.parse(msg.body);
    if (msgObj.type == 'JOIN') {
        showMsg(msgObj.from + ' joined');
    } else if (msgObj.type == 'LEAVE') {
        showMsg(msgObj.from + ' left');
    } else if (msgObj.type == 'CHAT') {
        showMsg(msgObj.from + ' says: ' + msgObj.body);
    } else {
        //ignore unknown message type
    }
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
        'room': room,
        'from': $("#name").val(),
        'to': to,
        'body': $("#msg").val(),
        'type': 'CHAT'
    };
    if (to == null || to == '') {
        stompClient.send("/app/chat/" + room, {}, JSON.stringify(msg));
    }
    else {
        stompClient.send("/app/chat/" + room + '/' + to, {}, JSON.stringify(msg));
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