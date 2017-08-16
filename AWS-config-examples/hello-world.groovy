Response response = new Response(Status.OK)
String localhostname = java.net.InetAddress.getLocalHost().getHostName()
response.entity = "Hello World! from ${localhostname}" as String
return response
