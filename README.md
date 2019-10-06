# Approach:
* To build a chatroom i need bidirectional connection between client and server -> thinking websocket. Searching for basic java websocket examples yields javax.websocket-api and spring STOMP
* The requirement is to have an admin page to delete and create room -> thinking some java MVC framework to collect web inputs and render responses
* I don't want to spent too much time setting up dev env (web server, database, ...) so i need some framework and a base code.
* I decided to go with Springboot since i can use both websocket and MVC features and most oh the heavylifting is done by the generator. So I copy base code from:
https://www.callicoder.com/spring-boot-websocket-chat-example/
https://spring.io/guides/gs/messaging-stomp-websocket/#scratch
then adding spring MVC to fill in the create/destroy room feature.
Also add spring boot security to only "allow a set of fixed users" to use the app
# Architecture:
* This project has 2 parts:
  * The serverside is java app built on springboot to
    * handle standard http requests to create/delete room
    * receive and route STOMP messages from web client through websocket to another web client
  * The client side is a javascript application running on browser to send STOMP request to server and render STOMP
message on browser
* Authentication is form based against an on-memory storage
* At persistent layer, all data (chatroom, chat message, user presence..) is stored on memory
* The http requests are handled by embedded tomcat, the artifact is runnable without a need to deployed on a web container
# Implementation detail:
* To only "allow a set of fixed users" to use the app, user must be registered in advance and they need to login to use the app
* Registering user is not in the scope of this demo, thus, I am using a in-memory user database (defined in WebSecurityConfig.java)
* * There is no configuration to distinguish a normal user from an admin
* The collection of chat room is kept in memory in a Map
* A user can be present in multiple room at the same time
* A message must have a **type** (JOIN, LEAVE, CHAT), **from** and **room** fields
* Messages without the **to** field are destined for the whole room 
* Messages with **to** field are meant for a user in the room (private message)
* Private messages will be drop if either user is missing from the room 
# Build and run
* To build:
  ./mvnw clean package
* To run:
  java -jar target/chatdemo-0.0.1-SNAPSHOT.jar
* To debug:
  java -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005  target/chatdemo-0.0.1-SNAPSHOT.jar
# Application flow
0. There are 3 user account preconfigured: thai:thai1, sarah:sarah1, jose:jose1
1. An admin must login and use localhost:8080/admin page to create a chatroom for authenticated users to use(There is no special security configuration for /admin page yet thus, any authenticated user can access /admin)
2. A user accesses the app at localhost:8080/ and be prompted for credential
3. Upon successfully authenticated, the user is asked to select a chatroom and pick a nickname
4. Upon selecting chatroom and nickname:
   * the user is connected to the chat room if it exist, the nickname may has been auto-assigned if the selected one conflict with another user in the room
   * the user is brought back to the room selection page if the room no longer exist
5. To broadcast a message to the whole room, user enter the msg and leave the **to** field blank
6. To send a private message to another user in the same room he must include the nickname of the other user in the **to** field
# To be improved:
## Server side:
1. persist user account, room and room member on disk so that upon app restart, they are not lost
2. use Oauth or SAML to secure app to avoid keeping user credentials on service provider (chat)
3. user is currently added to the room before websocket is connected, if something wrong happen before ws connected (browser crash for example) then there is no websocket event to remove the user from the room
4. add ability to kick a user out of the room for maintenance purpose
## Client side
(this demo do not focus on client implementation anyway)
1. list all users available in room
2. click to select user to send private message
3. also show private messages on the sender page
4. add button to leave room which bring user back to room selection page which effectively close ws and thus trigger **LEAVE**