cd ../java-on-lambda

SERVICE_ENDPOINT=`sls info -v | grep ServiceEndpoint | cut -d" " -f2`

sls deploy -f hello --force > /dev/null
curl -s -o /dev/null -w  "%{time_starttransfer}\n" $SERVICE_ENDPOINT

for i in {1..17280}
do 
    curl -s -o /dev/null -w  "%{time_starttransfer}\n" $SERVICE_ENDPOINT
    sleep 5
done
