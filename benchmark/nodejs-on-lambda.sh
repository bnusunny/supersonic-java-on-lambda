cd ../nodejs-on-lambda

for i in {1..100}
do 
    sls deploy -f app --force > /dev/null
    curl -s -o /dev/null -w  "%{time_starttransfer}\n" https://s0zqp57p8c.execute-api.ap-northeast-1.amazonaws.com/dev
done
