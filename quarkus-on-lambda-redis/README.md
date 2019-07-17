This is a simple lambda function write a simple key to redis. We use quarkus to compile the java lambda function to native image, and package the native image into custom runtime. 

### how to compile and deploy the lambda function

Make sure aws cli is configured properly and has enough privilege to create a lambda function. 

Run script deploy.sh. It will use maven to build the native image, packaged custom runtime, and deploy the lambda function. 


