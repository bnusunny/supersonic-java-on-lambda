cd ../nodejs-on-lambda-redis

echo 
echo "*** Deploying the function ***"
echo 

./deploy.sh

SERVICE_ENDPOINT=`cat stack.json | jq '.ServiceEndpoint' -r`

echo 
echo "*** Begin to run 100 cold start tests ***"
echo 

for i in {1..100}
do 
    sls deploy -f app --force > /dev/null
    curl -s -o /dev/null -w  "%{time_starttransfer}\n" $SERVICE_ENDPOINT
done

echo 
echo "*** Tests are completed ***"
echo 

