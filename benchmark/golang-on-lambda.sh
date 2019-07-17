cd ../golang-on-lambda/src/golang-on-lambda

SERVICE_ENDPOINT=`sls info -v | grep ServiceEndpoint | cut -d" " -f2`

for i in {1..100}
do 
    sls deploy -f hello --force > /dev/null
    curl -s -o /dev/null -w  "%{time_starttransfer}\n"  $SERVICE_ENDPOINT
done
