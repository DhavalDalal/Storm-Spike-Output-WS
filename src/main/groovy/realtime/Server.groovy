package realtime

import org.apache.activemq.command.ActiveMQQueue
import org.apache.activemq.spring.ActiveMQConnectionFactory

import javax.jms.Destination

def cli = new CliBuilder(usage:'server [-s <ip or hostname>] [-p <number>] [-b <jmsBrokerUrl>] -q <queueName>')
cli.with {
    s  args:1, argName: 'serverUrl', longOpt:'serverUrl', 'OPTIONAL, Server IP/URL, default is localhost', optionalArg:true
    p  args:1, argName: 'port', longOpt:'port', 'OPTIONAL, Server Port, default is 9080', optionalArg:true
    b  args:1, argName: 'brokerUrl', longOpt:'brokerUrl', 'OPTIONAL, Broker URL, default is tcp://localhost:61616', optionalArg:true
    q  args:1, argName: 'queueName', longOpt:'queueName', 'REQUIRED, Queue Name', optionalArg:false
}

def options = cli.parse(args)

if(!options) {
    cli.usage()
    return
}


if(options.arguments()){
    println "Cannot understand ${options.arguments()}"
    cli.usage()
    return
}

def host = 'localhost'

if(options.s) {
    host = options.s
}

def port = 9080
if(options.port) {
    port = Integer.parseInt(options.port)
}

def brokerURL = 'tcp://localhost:61616'
if(options.brokerUrl) {
    brokerUrl = options.brokerUrl
}

def queueName = options.queueName

println "Starting Server...ip = $host, port = $port"

ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory()
connectionFactory.brokerURL = brokerURL
Destination destination = new ActiveMQQueue(queueName)
def barServer = new BarWebSocketServer(host, port, connectionFactory, destination)
println 'Ready to accept websocket connections.'
barServer.start()
