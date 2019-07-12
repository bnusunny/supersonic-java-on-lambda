cd ../vertx-on-lambda

for i in {1..100}
do 
    sls deploy -f hello --force > /dev/null
    curl -s -o /dev/null -w  "%{time_starttransfer}\n"  https://ie67ejo9r3.execute-api.ap-northeast-1.amazonaws.com/dev
done
