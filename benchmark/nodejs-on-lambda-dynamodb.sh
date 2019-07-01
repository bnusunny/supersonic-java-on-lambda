cd ../nodejs-on-lambda-dynamodb

for i in {1..100}
do 
    sls deploy -f app --force > /dev/null
    curl -s -o /dev/null -w  "%{time_starttransfer}\n" -X POST https://veh8mn74nl.execute-api.ap-northeast-1.amazonaws.com/dev/quote
done
