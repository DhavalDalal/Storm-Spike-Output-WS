package realtime

import groovy.util.logging.Slf4j
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer

import javax.jms.Connection
import javax.jms.ConnectionFactory
import javax.jms.Destination
import javax.jms.Message
import javax.jms.MessageConsumer
import javax.jms.MessageListener
import javax.jms.Session
import javax.jms.TextMessage

@Slf4j
class BarWebSocketServer extends WebSocketServer implements MessageListener {

    private def connections = []

    public BarWebSocketServer(String host, int port, ConnectionFactory connectionFactory, Destination destination) {
        super(new InetSocketAddress(host, port))
        Connection connection = connectionFactory.createConnection()
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
        log.info("Created Session $session")
        MessageConsumer consumer = session.createConsumer(destination)
        log.info("Created Consumer $consumer")
        consumer.setMessageListener((MessageListener) this)
        connection.start()
    }

    @Override
    void onOpen(WebSocket conn, ClientHandshake handshake) {
        println("New Connection Received From: ${conn.remoteSocketAddress.address.hostAddress}...")
        if(connections.contains(conn)) {
            return
        }
        connections << conn
        println "Added Subscriber, Total Subscribers = ${connections.size()}"
    }

    @Override
    void onClose(WebSocket conn, int code, String reason, boolean remote) {
        println("Closed Connection: $conn, Code: $code, Reason: $reason, Remote: $remote")
        if(connections.contains(conn)) {
            connections.remove(conn)
        }
        println "Removed Subscriber, Total Subscribers = ${connections.size()}"
    }

    @Override
    void onMessage(WebSocket conn, String message) {
        conn.send("Server ACKing Content: $message")
        if (message.contains("Total"))
            println("Received Message : $message")
    }

    @Override
    void onError(WebSocket conn, Exception ex) {
        println("Error from client: $conn, Error: $ex.message")
        ex.printStackTrace()
    }

    void push(String message) {
        println "Pushing $message to all WebSocket Connections..."
        connections.each { connection ->
            connection.send(message)
        }
    }

    @Override
    public void onMessage(Message message) {
        log.info("Received message $message")
        if(message instanceof TextMessage) {
            def textMessage = message as TextMessage
            push(textMessage.text)
            message.acknowledge()
        } else {
            log.info("Cannot Deliver Non-Text Message: $message")
        }
    }
}